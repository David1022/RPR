package com.nipsa.rpr;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.Vector;

public class Mapa extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnInfoWindowClickListener {

    private GoogleMap mapa;
    private final static int NIVEL_ZOOM = 18;
    private Vector<Apoyo> listaApoyos;
    private Vector<Tramo> listaTramos;
    private DBRevisiones dbRevisiones;
    private LatLng posicionActual = new LatLng(41.382521, 2.177171); // Posición por defecto
    Double lat = null;
    Double lng = null;

    //SELECT COUNT(Latitud), _id, NombreRevision, NombreTramo, Longitud, Latitud FROM Tramos GROUP BY Latitud, Longitud HAVING COUNT (Latitud<2) ORDER BY _id

    /**
     * Llamado al crear la vista. Se Inicializan la Toolbar, el fragment del mapa y la lista de apoyos
     * que se mostrarán
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mapa);

        // Inicializamos los elementos de la AppBar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarMapa);
        setSupportActionBar(toolbar);

        // Se recogen los apoyos a mostrar
        dbRevisiones = new DBRevisiones(this);
        listaApoyos = dbRevisiones.solicitarListaApoyos(Aplicacion.revisionActual);
        listaTramos = dbRevisiones.solicitarListaTramosCoord(Aplicacion.revisionActual);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     *
     * Se inicializa el mapa y los elementos que se representarán en él
     *
     * @param googleMap
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mapa = googleMap;

        // Se carga el mapa en modo hibrido (vista mapa/satelite)
        mapa.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        if (MostrarEquipos.localizacion != null) {
            posicionActual = new LatLng(MostrarEquipos.localizacion.getLatitude(),
                                            MostrarEquipos.localizacion.getLongitude());
        }

        // Se recorre la lista de apoyos para visualizarlos en el mapa
        for (int i=0; i<listaApoyos.size();i++) {
            Apoyo apoyo = listaApoyos.elementAt(i);
            lat = null;
            lng = null;

            // Se recogen las coordenadas guardadas en la BDD
            try {
                lat = Double.parseDouble(apoyo.getLatitud());
                lng = Double.parseDouble(apoyo.getLongitud());
            } catch (Exception e) {
            }
            // Si hay coordenadas se muestra el equipo
            if(lat != null && lng != null){
                LatLng pos = new LatLng(lat,lng);

                // Se asignan las características del marcador
                MarkerOptions marker = new MarkerOptions().position(pos);
                marker.title(apoyo.getNombreEquipo());
                String sSnippet = "Tipo instalación: " + apoyo.getTipoInstalacion() + "\n" +
                        "Código: " + apoyo.getCodigoApoyo() + "\n" +
                        "Material: " + apoyo.getMaterial() + "\n" +
                        "Tramo: " + apoyo.getCodigoTramo();
                marker.snippet(sSnippet);
                Cursor cursor = dbRevisiones.solicitarItem(DBRevisiones.TABLA_EQUIPOS, "TipoInstalacion",
                        "NombreEquipo", apoyo.getNombreEquipo());
                if (cursor.moveToFirst()) {
                    String tipo = cursor.getString(0);
                    cursor = dbRevisiones.solicitarItem(DBRevisiones.TABLA_EQUIPOS,
                            "Estado", "NombreEquipo", apoyo.getNombreEquipo());
                    if (cursor.moveToFirst()) {
                        String estado = cursor.getString(0);
                        // Se asigna el icono y el color del marcador ( https://mapicons.mapsmarker.com/about/iconos-para-mapas/ )
                        if (tipo.equals("L")) {
                            switch (estado) {
                                case Aplicacion.ESTADO_PENDIENTE:
                                    marker.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
                                    break;
                                case Aplicacion.ESTADO_EN_CURSO:
                                    marker.icon((BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE)));
                                    break;
                                case Aplicacion.ESTADO_FINALIZADA:
                                    marker.icon((BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
                                    break;
                                default:
                                    marker.icon((BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
                                    break;
                            }
                        } else {
                            switch (estado) {
                                case Aplicacion.ESTADO_PENDIENTE:
                                    marker.icon(BitmapDescriptorFactory.fromResource(R.drawable.home_red));
                                    break;
                                case Aplicacion.ESTADO_EN_CURSO:
                                    marker.icon((BitmapDescriptorFactory.fromResource(R.drawable.home_orange)));
                                    break;
                                case Aplicacion.ESTADO_FINALIZADA:
                                    marker.icon((BitmapDescriptorFactory.fromResource(R.drawable.home_green)));
                                    break;
                                default:
                                    marker.icon((BitmapDescriptorFactory.fromResource(R.drawable.home_blue)));
                                    break;
                            }
                        }
                    }
                }

                cursor.close();
                mapa.addMarker(marker);
            }
        }

        //Se recorre la lista de tramos para visualizar los tramos
        //mapa.addPolyline(incluirTramos());

        // Se centra la visualización del mapa en la posición actual y el nivel de zoom
        mapa.moveCamera(CameraUpdateFactory.newLatLngZoom(posicionActual,NIVEL_ZOOM));
        mapa.setOnInfoWindowClickListener(this);

        // Si hay permisos para acceder a la "localizacion" se activan las siguientes opciones
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mapa.setMyLocationEnabled(true); // Acceso a "mi ubicación"
            mapa.getUiSettings().setZoomControlsEnabled(true); // Controles para el zoom
            mapa.getUiSettings().setCompassEnabled(true); // Control brújula
            mapa.getUiSettings().setMyLocationButtonEnabled(true); // Control "mi ubicación"

        } else {
            mapa.getUiSettings().setMyLocationButtonEnabled(false);
        }
    }

    /**
     * Se abre la actividad anterior (MostrarEquipos.class)
     */
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(this, MostrarEquipos.class);
        startActivity(intent);
        finish();
    }

    /**
     * Al presionar sobre la ventana de información de un marcador se abrirá la actividad MostrarEquipos
     * visualizando el equipo que se ha seleccionado
     *
     * @param marker
     */
    @Override
    public void onInfoWindowClick(Marker marker) {
        String tramo = marker.getSnippet();
        tramo = tramo.substring(tramo.lastIndexOf("Tramo: ") + 7);
        //tramo = tramo.substring(tramo.indexOf(" ") + 1);
        Equipo equipo = dbRevisiones.solicitarEquipo(Aplicacion.revisionActual, marker.getTitle(), tramo);
        if (equipo != null){
            // Se asigna como equipo actual el equipo sobre el que se ha presionado
            Aplicacion.equipoActual = marker.getTitle();
            Aplicacion.tramoActual = tramo;
            Intent intent = new Intent(this, MostrarEquipos.class);
            startActivity(intent);
            finish();
        } else {
            Aplicacion.print("Selecciona un apoyo");
        }
    }

    private PolylineOptions incluirTramos() {
        PolylineOptions polOptions = new PolylineOptions();

        if ((listaTramos.size() > 0) && (listaTramos != null)) {
            for (int i=0; i<listaTramos.size(); i++) {
                Tramo tr = listaTramos.get(i);
                Double lng, lat;
                lng = Double.parseDouble(tr.getLng());
                lat = Double.parseDouble(tr.getLat());
                polOptions.add(new LatLng(lat, lng));
            }
        }
        polOptions.width(5);
        polOptions.color(getResources().getColor(R.color.azul));

        return polOptions;
    }
}