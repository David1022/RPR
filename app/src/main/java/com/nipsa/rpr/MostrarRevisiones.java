package com.nipsa.rpr;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContentResolverCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.util.Vector;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

public class MostrarRevisiones extends AppCompatActivity implements FrgListadoRevisiones.RevisionListener,
                                                                        DialogInterface.OnDismissListener{

    public static Activity activity;
    public static Context contexto;
    private Aplicacion aplicacion = new Aplicacion();
    public static final String DIRECTORIO_ENTRADA = "/RPR/Input/";
    public static final String DIRECTORIO_INTERNO_APP = "/Android/data/com.nipsa.rpr/files/";
    private String direccionArchivosEntrada;
    private String direccionMemInternaApp;
    public static Vector<Revision> listaRevisiones;
    public static DBRevisiones dbRevisiones;
    public static String nombreRevision = "";
    public static String nombreArchivo;
    private FrgListadoRevisiones frgListado;
    private final int ABRIR_ARCHIVO = 1;


    /**
     * Se inicializa Toolbar y variables. Se infla el fragment estatico
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mostrar_revisiones);

        // Inicialización de los elementos de la AppBar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarMostrarRevisiones);
        setSupportActionBar(toolbar);

        activity = this;
        contexto = this;
        dbRevisiones = new DBRevisiones(this);
        listaRevisiones = new Vector<Revision>();

        // Se asignan los directorios de trabajo
        direccionArchivosEntrada = Environment.getExternalStoragePublicDirectory
                (Environment.DIRECTORY_DOWNLOADS) + DIRECTORIO_ENTRADA;
        direccionMemInternaApp = Environment.getExternalStorageDirectory() + DIRECTORIO_INTERNO_APP;

        try{
            listaRevisiones = dbRevisiones.solicitarListaRevisiones();
        } catch (Exception e){
            Log.e("ErrorRPR: ", e.toString());
            Aplicacion.print(e.toString());
        }

    }

    /**
     * Se infla el fragment dinámico si procede
     */
    @Override
    protected void onResume() {
        super.onResume();
        listaRevisiones = dbRevisiones.solicitarListaRevisiones();

        Revision revision = dbRevisiones.solicitarRevision(Aplicacion.revisionActual);

        // Si procede se infla el fragment dinamico (Listado Revisión)
        boolean hayListado = (getFragmentManager().findFragmentById(R.id.contenedorFragmentListadoRevision) != null);
        FrgListadoRevisiones flr = new FrgListadoRevisiones();
        flr.setRevisionListener(this);

        if (hayListado) {
            getSupportFragmentManager().beginTransaction().add(R.id.contenedorFragmentListadoRevision, flr).commit();
        } else {
            FragmentTransaction tr = getSupportFragmentManager().beginTransaction();
            tr.replace(R.id.contenedorFragmentListadoRevision, flr);
            tr.commit();
        }

        // Si procede se infla el fragment dinamico (Detalle revisión)
        if (revision != null) {
            boolean hayDetalle =
                    (getSupportFragmentManager().findFragmentById(R.id.contenedorFragmentDetalleRevision) != null);

            FrgDetalleRevision fdi = new FrgDetalleRevision();

            if (!hayDetalle) { // Si el fragment no esta inflado se infla
                getSupportFragmentManager().beginTransaction().add(R.id.contenedorFragmentDetalleRevision, fdi).commit();
            } else { // Si el fragment está inflado se refresca
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.contenedorFragmentDetalleRevision, fdi);
                transaction.commit();
            }
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
/*
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_revisiones, menu);
*/
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_importar_revision:
                Intent intent = new Intent(this, ImportarRevision.class);
                startActivity(intent);
                finish();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Se lanzará cuando se cierre el diálogo contextual para mostrar las opciones de una revisión
     * @param dialog
     */
    @Override
    public void onDismiss(DialogInterface dialog) {
        if (nombreArchivo.contains(".")) {
            String nombreRevision = nombreArchivo.substring(0, nombreArchivo.lastIndexOf("."));
            File archivo = Aplicacion.recuperarArchivo(direccionArchivosEntrada, nombreArchivo);

            dbRevisiones.incluirRevision(nombreRevision, Aplicacion.ESTADO_PENDIENTE);

            // Se lee el archivo XML
            HiloLeerArchivoXML hilo = new HiloLeerArchivoXML();
            hilo.execute(archivo);

            Aplicacion.revisionActual = nombreRevision;

            onResume();
        }
    }

    /**
     * Método para listar los archivos de una ruta determinada
     *
     * @param ruta
     * @return vector con los archivos con extension .xml encontrados en la ruta pasada por parámetro
     * @throws Exception
     */
    public static Vector<File> listarArchivosDirEntrada(String ruta) throws Exception{
        Vector<File> listaArchivos = new Vector<File>();
        //Se define la ruta donde buscar los ficheros
        File f = new File(ruta);
        //Se crea el array de tipo File con el contenido de la carpeta
        File[] archivos = f.listFiles();
        // Si hay nuevos archivos en el directorio de entrada se gestionaran
        if (archivos != null) {
            // Loop por cada fichero para extraer el nombre de cada uno
            for (int i = 0; i < archivos.length; i++) {
                //Se saca del array files un fichero
                File file = archivos[i];
                //Si es fichero (no es directorio)...
                if (file.isFile()) {
                    String nombreFichero = file.getName();
                    if (esXML(nombreFichero)) { // Solo se listaran los ficheros si son XML
                        listaArchivos.add(file);
                    }
                }
            }
        } else throw new NullPointerException();

        return listaArchivos;
    }

    /**
     * Método para determinar si la extensión de una archivo es ".xml"
     *
     * @param nombre
     * @return
     */
    public static boolean esXML(String nombre){
        boolean esXML = false;
        String extension = nombre.substring(nombre.lastIndexOf("."));

        if (extension.equals(".xml")){
            esXML = true;
        }

        return esXML;
    }

    /**
     * Método que escuchará la selección de un elemento de la lista de revisiones
     * @param revision
     */
    @Override
    public void onRevisionSeleccionada(Revision revision) {
        boolean hayDetalle =
                (getSupportFragmentManager().findFragmentById(R.id.contenedorFragmentDetalleRevision) != null);

        nombreRevision = revision.getNombre();
        Aplicacion.revisionActual = nombreRevision;
        Aplicacion.equipoActual = "";

        FrgDetalleRevision fdr = new FrgDetalleRevision();
        if (!hayDetalle) {
            getSupportFragmentManager().beginTransaction().add(R.id.contenedorFragmentDetalleRevision, fdr).commit();
        } else {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.contenedorFragmentDetalleRevision, fdr);
            transaction.commit();
        }

    }

    /**
     * Metodo para incluir un nuevo equipo (apoyo/CT) en la tabla "Equipos"
     *
     * @param datos
     */
    public static void incluirEquipoEnDBRevisiones (Vector<String> datos) {

        dbRevisiones.incluirEquipo(datos);

    }

    /**
     * Metodo para incluir un apoyo (apoyo/CT) en la tabla "Apoyos"
     *
     * @param datos
     */
    public static void incluirApoyoEnDBRevisiones (Vector<String> datos) {

        dbRevisiones.incluirApoyo(datos);

    }

    /**
     * Metodo para incluir un nuevo apoyo (apoyo/CT) en la tabla "NuevosApoyos"
     *
     * @param datos
     */
    public static void incluirApoyoNoRevisableEnDBRevisiones (Vector<String> datos) {

        dbRevisiones.incluirApoyoNoRevisable(datos);

    }


    /**
     * Actualiza la "revision" correspondiente con los datos recogidos del FrgDetalleRevision
     *
     * @param inspector1
     * @param inspector2
     * @param colegiado
     * @param equiposUsados
     * @param metodologia
     * @param codigoNipsa
     * @param trabajo
     * @param codigoInspeccion
     */
    public static void actualizarRevision (String inspector1, String inspector2, String colegiado,
                                           String equiposUsados, String metodologia, String codigoNipsa,
                                           String trabajo, String codigoInspeccion) {

        dbRevisiones.actualizarRevision(Aplicacion.revisionActual, inspector1, inspector2, colegiado, equiposUsados, metodologia,
                codigoNipsa, trabajo, codigoInspeccion);

    }

    /**
     * Se actualizan las coordenadas recogidas en el KML
     *
     * @param longitud
     * @param latitud
     * @param equipo
     */
    public static void actualizarCoordenadasEquipo (String longitud, String latitud, String equipo) {

        dbRevisiones.actualizarItemEquipoApoyo(DBRevisiones.TABLA_APOYOS, "Longitud", longitud, equipo);
        dbRevisiones.actualizarItemEquipoApoyo(DBRevisiones.TABLA_APOYOS, "Latitud", latitud, equipo);

    }

    /**
     * Se actualizan las coordenadas de un tramo recogidas en el KML
     *
     * @param longitud
     * @param latitud
     * @param revision
     */
    public static void actualizarCoordenadasTramo (String longitud, String latitud, String revision, String tramo) {

        dbRevisiones.incluirTramo(revision, tramo, longitud, latitud);

    }

}