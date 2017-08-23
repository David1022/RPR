package com.nipsa.rpr;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.Xml;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import org.xmlpull.v1.XmlSerializer;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.Vector;

public class MostrarEquipos extends AppCompatActivity implements FrgListadoEquipos.EquipoListener, LocationListener{

    private DBRevisiones dbRevisiones;
    public static Vector<Equipo> listaEquipos;
    private FrgListadoEquipos frgListadoEquipos;
    public static String nombreEquipo = "";
    private static LocationManager manejador;
    private static String proveedor;
    private static final long TIEMPO_MIN = 10 * 1000 ; // 10 segundos
    private static final long DISTANCIA_MIN = 5; // 5 metros
    public static Location localizacion = null;
    public static Context contexto;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mostrar_equipos);

        contexto = this;
        dbRevisiones = new DBRevisiones(this);
        listaEquipos = dbRevisiones.solicitarListaEquipos(Aplicacion.revisionActual);

        // Se recoge el nombre de la línea a revisar para mostrarlo en el título
        String nombreLinea = listaEquipos.get(0).getCodigoTramo();
        if (nombreLinea.contains("-")) {
            nombreLinea = nombreLinea.substring(nombreLinea.lastIndexOf("-") + 1);
        }

        // Inicialización de los elementos de la AppBar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarMostrarEquipos);
        toolbar.setTitle(getResources().getString(R.string.titulo_equipos) + ": " + Aplicacion.revisionActual +
                " - " + nombreLinea);
        setSupportActionBar(toolbar);


        // Se carga el fragment estatico que listará las revisiones cargadas
        frgListadoEquipos =(FrgListadoEquipos) getSupportFragmentManager().findFragmentById(R.id.FrgListadoEquipos);
        // Se asigna listener para escuchar clicks sobre el listado
        frgListadoEquipos.setEquipoListener(this);

        // Activar localización
        manejador = (LocationManager) getSystemService(LOCATION_SERVICE);
        Criteria criterio = new Criteria();
        criterio.setCostAllowed(false);
        criterio.setAltitudeRequired(false);
        criterio.setAccuracy(Criteria.ACCURACY_FINE);
        proveedor = manejador.getBestProvider(criterio, true);
        localizacion = getUltimaLocalizacionConocida();

    }

    // .................. Métodos del ciclo de vida de la actividad .......................
    @Override
    public void onPostResume() {
        super.onPostResume();
        Equipo equipo = dbRevisiones.solicitarEquipo(Aplicacion.revisionActual, Aplicacion.equipoActual, Aplicacion.tramoActual);

        if (equipo != null) {
            boolean hayDetalle =
                    (getSupportFragmentManager().findFragmentById(R.id.contenedorFragmentDetalleEquipo) != null);
            TextView titulo = (TextView) findViewById(R.id.tituloEquipoSeleccionado);

            if (equipo.getNombreEquipo().equals("")) {
                if(equipo.getTipoInstalcion().toString().equals("L")){
                    nombreEquipo = getResources().getString(R.string.titulo_datos_equipo) + ": " + equipo.getEquipoApoyo();
                } else {
                    nombreEquipo = getResources().getString(R.string.titulo_datos_equipo) + ": " + equipo.getCodigoBDE();
                }
            } else {
                    nombreEquipo = getResources().getString(R.string.titulo_datos_equipo) + ": " + equipo.getNombreEquipo();
            }
            titulo.setText(nombreEquipo);

            FrgDetalleEquipo fdi = new FrgDetalleEquipo();
            if (!hayDetalle) {
                getSupportFragmentManager().beginTransaction().add(R.id.contenedorFragmentDetalleEquipo, fdi).commit();
            } else {
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.contenedorFragmentDetalleEquipo, fdi);
                transaction.commit();
            }
        }

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.
                ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            manejador.requestLocationUpdates(proveedor, TIEMPO_MIN, DISTANCIA_MIN,
                    this);
        }

    }

    @Override
    protected void onPause() {
        super.onPause();
        manejador.removeUpdates(this);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(this, MostrarRevisiones.class);
        startActivity(intent);
        finish();
    }


    // .................. Métodos de la interfaz LocationListener .......................
    public void onLocationChanged(Location location) {
    }

    public void onProviderDisabled(String proveedor) {
    }

    public void onProviderEnabled(String proveedor) {
    }

    public void onStatusChanged(String proveedor, int estado, Bundle extras) {
    }

    // .................. Métodos del menú .......................
    @Override
    public boolean onCreateOptionsMenu (Menu menu) {
        // Se añade el menu
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_equipo, menu);
        return super.onCreateOptionsMenu(menu);
    }

    /*
     * Listener de los elementos del menu
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_mapa:
                abrirMapa();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /*
     * Metodo para abrir el mapa con los apoyos existentes
     */
    private void abrirMapa () {
        Intent intent = new Intent(this, Mapa.class);
        startActivity(intent);
        finish();

    }

    // .................. Métodos de la actividad .......................
    /*
     * Listener del listado
     */
    @Override
    public void onEquipoSeleccionado(Equipo equipo) {
        boolean hayDetalle =
                (getSupportFragmentManager().findFragmentById(R.id.contenedorFragmentDetalleEquipo) != null);
        TextView titulo = (TextView) findViewById(R.id.tituloEquipoSeleccionado);

        if (equipo.getNombreEquipo().equals("")) {
            if(equipo.getTipoInstalcion().toString().equals("L")){
                nombreEquipo = getResources().getString(R.string.titulo_datos_equipo) + ": " + equipo.getEquipoApoyo();
            } else {
                nombreEquipo = getResources().getString(R.string.titulo_datos_equipo) + ": " + equipo.getCodigoBDE();
            }
        } else {
            nombreEquipo = getResources().getString(R.string.titulo_datos_equipo) + ": " + equipo.getNombreEquipo();
        }
        titulo.setText(nombreEquipo);
        Aplicacion.equipoActual = equipo.getNombreEquipo();
        Aplicacion.tipoActual = equipo.getTipoInstalcion();
        Aplicacion.tramoActual = equipo.getCodigoTramo();

        FrgDetalleEquipo fdi = new FrgDetalleEquipo();
        if (!hayDetalle) {
            getSupportFragmentManager().beginTransaction().add(R.id.contenedorFragmentDetalleEquipo, fdi).commit();
        } else {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.contenedorFragmentDetalleEquipo, fdi);
            transaction.commit();
        }

    }

    public static Location getUltimaLocalizacionConocida () {
        boolean b = ContextCompat.checkSelfPermission(contexto, Manifest.permission.
                ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
        if (b) {
            localizacion = manejador.getLastKnownLocation(proveedor);
        } else {
            Toast.makeText(contexto, "No hay permisos para determinar la localización", Toast.LENGTH_SHORT).show();
        }
        return localizacion;
    }

    public void moverFotos () {
        Vector<File> listaArchivos = new Vector<File>();
        String rutaOrigen = Environment.getExternalStoragePublicDirectory
                                (Environment.DIRECTORY_PICTURES) + "/" + Aplicacion.revisionActual + "/";
        String rutaDestino = Environment.getExternalStoragePublicDirectory
                                (Environment.DIRECTORY_DOWNLOADS) + "/" + Aplicacion.revisionActual + "/";

        File file = new File(rutaOrigen);
        File[] archivos = file.listFiles();

        for (int i=0; i<archivos.length; i++) {
            File file1 = archivos[i];
            if (file1.isDirectory()) {
                String rutaOrigen2 = rutaOrigen + file1.getName() + "/";
                String rutaDestino2 = rutaDestino + file1.getName() + "/";
                File file2 = new File(rutaOrigen2);
                File[] archivos2 = file2.listFiles();
                for (int j=0; j<archivos2.length; j++) {
                    File file3 = archivos2[i];
                    moverArchivo(file3, rutaDestino2);
                }
            } else if (file1.isFile()) {
                moverArchivo(file1, rutaDestino);
            }
        }
    }

    public void moverArchivo (File archivoOrigen, String rutaDestino) {
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

}
