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
    public void onDismiss(DialogInterface dialog) {
        if (nombreArchivo.contains(".")) {
            String nombreRevision = nombreArchivo.substring(0, nombreArchivo.lastIndexOf("."));
            File archivo = Aplicacion.recuperarArchivo(direccionArchivosEntrada, nombreArchivo);

            dbRevisiones.incluirRevision(nombreRevision, Aplicacion.ESTADO_PENDIENTE);

            // Se lee el archivo XML
            HiloLeerArchivoXML hilo = new HiloLeerArchivoXML();
            hilo.execute(archivo);
            //Se lee el archivo KML
            //leerArchivoCoord(archivo);

            Aplicacion.revisionActual = nombreRevision;

            onResume();
        }
    }

    /**
     * Se recogerá el resultado de hacer la foto. Se toma la Uri del archivo donde se ha guardado la imagen
     *
     * @param requestCode
     * @param resultCode
     * @param data
     *
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_CANCELED) {
            //Cancelado por el usuario, no se hace nada
        }
        if ((resultCode == RESULT_OK) && (requestCode == ABRIR_ARCHIVO )) {
            //Procesar el resultado
            Uri uri = data.getData(); //obtener el uri content
            leerArchivoXML(uri);
        }
    }*/

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
     * Método que moverá el archivo recibido por parametro al directorio de destino recibido por parametro
     *
     * @param archivoOrigen
     * @param rutaDestino
     * @throws Exception
     */
    public void moverArchivo(File archivoOrigen, String rutaDestino) throws Exception{
        String rutaNuevoArchivo = rutaDestino + archivoOrigen.getName();
        File f = new File(rutaDestino);
        File nuevoArchivo = new File(rutaNuevoArchivo);
        // Si el directorio no existe se crea (solo sucederá en la primera instalación de la app)
        if (!f.exists()){
            f.mkdirs();
        }
        // Se asigna la nueva ruta al archivo
        archivoOrigen.renameTo(nuevoArchivo);
    }

/*
    */
/**
     * Método que recibe el archivo XML de referencia al cual se asociará el archivo KML. Leerá el
     * archivo KML y asociará las coordenadas recogidas al equipo correspondiente
     *
     * @param archivoXML con extensión XML
     * @throws ParserConfigurationException
     * @throws SAXException
     * @throws IOException
     *//*


    public void leerArchivoCoord (File archivoXML) {
        // Se recupera el nombre y la ruta del archivo XML para recuperar el archivo KML con el mismo nombre
        String nombreRevision = archivoXML.getName();
        nombreRevision = nombreRevision.substring(0, nombreRevision.lastIndexOf("."));
        String nombreKML = nombreRevision + ".kml";
        String ruta = archivoXML.getAbsolutePath();
        ruta = ruta.substring(0, ruta.lastIndexOf("/"));
        File archivoCoord = Aplicacion.recuperarArchivo(ruta, nombreKML);
        //Si se encuentra el archivo KML con el mismo nombre que el XML se leerá
        if (archivoCoord != null) {
            // Se lee el archivo en un hilo secundario
            HiloLeerArchivosCoord hilo = new HiloLeerArchivosCoord();
            hilo.execute(archivoCoord);
            //LectorCoord lector = new LectorCoord(nombreRevision);
            //lector.leer(archivoCoord);

        } else { // Si no se encuentra se muestra un mensaje por pantalla para informar al usuario
            Aplicacion.print("No se ha encontrado archivo KML asociado a la revision " + nombreRevision);
        }

    }
*/

    /**
     * Método que leerá un archivo ".xml" basandose en la clase ManejadorXML
     *
     * @param
     * @throws ParserConfigurationException
     * @throws SAXException
     * @throws IOException
     *
    public void leerArchivoXML(Uri uri) {
        if (esXML(uri.getPath())) {
            ProgressDialog pd = abrirDialogoProgreso("Leyendo archivo XML...");
            String path = uri.getPath();
            nombreRevision = path.substring(path.lastIndexOf("/") + 1);
            nombreRevision = nombreRevision.substring(0, nombreRevision.lastIndexOf("."));
            Aplicacion.revisionActual = nombreRevision;

            try {
                InputStream inputStream = this.getContentResolver().openInputStream(uri);
                InputSource entrada = new InputSource(inputStream);

                SAXParserFactory fabrica = SAXParserFactory.newInstance();
                SAXParser parser = fabrica.newSAXParser();
                XMLReader lector = parser.getXMLReader();
                ManejadorXML manejadorXML = new ManejadorXML();
                lector.setContentHandler(manejadorXML);
                lector.parse(entrada);

                leerArchivoCoord(uri);
                dbRevisiones.incluirRevision(nombreRevision, Aplicacion.ESTADO_PENDIENTE);
                inputStream.close();

            } catch (ParserConfigurationException parseException) {
                Log.e (Aplicacion.TAG, "Error al parsear el archivo XML " + parseException.toString());
            } catch (SAXException saxException) {
                Log.e (Aplicacion.TAG, "Error SAX al leer el archivo XML " + saxException.toString());
            } catch (IOException ioException) {
                Log.e (Aplicacion.TAG, "Error IOExcetion al parsear el archivo XML " + ioException.toString());
            } finally {
                pd.dismiss();
            }
        } else {
            Aplicacion.print("El archivo debe tener extension XML");
        }

    }*/

    /*
    private ProgressDialog abrirDialogoProgreso (String titulo) {
        ProgressDialog pd = new ProgressDialog(this);
        pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        pd.setMessage(titulo);
        pd.setCancelable(false);
        pd.setIndeterminate(true);
        pd.show();

        return pd;
    }*/

/*
    public void leerArchivoCoord (Uri uriXML) {
        // Se recupera el nombre y la ruta del archivo XML para recuperar el archivo KML con el mismo nombre
        Uri.Builder ub = new Uri.Builder();
        ub.authority(uriXML.getAuthority());
        String path = uriXML.getPath();
        path = path.substring(0, path.lastIndexOf("."));
        path += ".kml";
        ub.appendPath(path);
        ub.scheme(uriXML.getScheme());

        try {
            InputStream inputStream = this.getContentResolver().openInputStream(ub.build());
            LectorCoord lector = new LectorCoord(nombreRevision);
            lector.leer(inputStream);
        } catch (Exception e) {

        }
/*
        File archivoXML = new File(uriXML.getPath());
        String nombre = archivoXML.getName();
        nombre = nombre.substring(0, nombre.lastIndexOf("."));
        nombre = nombre + ".kml";
        String ruta = archivoXML.getAbsolutePath();
        ruta = ruta.substring(0, ruta.lastIndexOf("/"));
        File archivoCoord = Aplicacion.recuperarArchivo(ruta, nombre);
        //Si se encuentra el archivo KML con el mismo nombre que el XML se leerá
        if (archivoCoord != null) {
            // Se lee el archivo en un hilo secundario
            HiloLeerArchivosCoord hilo = new HiloLeerArchivosCoord();
            hilo.execute(archivoCoord);
        } else { // Si no se encuentra se muestra un mensaje por pantalla para informar al usuario
            Aplicacion.print("No se ha encontrado archivo KML asociado a la revision " + nombre);
        }
*
    }*/

    public static void refrescarFragmentDetalle () {
        // Si procede se infla el fragment dinamico (Detalle revisión)
/*
        if (Aplicacion.revisionActual != null) {
            boolean hayDetalle =
                    (activity.getFragmentManager().findFragmentById(R.id.contenedorFragmentDetalleRevision) != null);

            FrgDetalleRevision fdi = new FrgDetalleRevision();

            if (!hayDetalle) { // Si el fragment no esta inflado se infla
                getSupportFragmentManager().beginTransaction().add(R.id.contenedorFragmentDetalleRevision, fdi).commit();
            } else { // Si el fragment está inflado se refresca
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.contenedorFragmentDetalleRevision, fdi);
                transaction.commit();
            }
*/

        }

    /**
     * Método que escuchará la selección de un elemento de la lista de revisiones
     *
     * @param r
     */
    @Override
    public void onRevisionSeleccionada(Revision r) {
        boolean hayDetalle =
                (getSupportFragmentManager().findFragmentById(R.id.contenedorFragmentDetalleRevision) != null);

        nombreRevision = r.getNombre();
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