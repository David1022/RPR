package com.nipsa.rpr;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class TomarCoordenadas extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mapa;
    private LatLng posicionActual = new LatLng(41.382521, 2.177171); // Posición por defecto
    private final static int NIVEL_ZOOM = 18;

    private DBRevisiones dbRevisiones;
    private String revisionActual, equipoActual, tramoActual, defectoActual;
    private Button botonAceptarCoordenadas, botonCancelar;
    private Double lat, lng;
    private MarkerOptions markerOptions;
    private static Marker markerActual;
    private String tipo; // Tomara los valores Equipo o Defecto para saber que actividad solicita las coordenadas

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tomar_coordenadas);
        Bundle extras = getIntent().getExtras();
        tipo = extras.getString(Aplicacion.TIPO);

        // Inicializamos los elementos de la AppBar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarTomarCoordenada);
        setSupportActionBar(toolbar);

        dbRevisiones = new DBRevisiones(this);
        this.revisionActual = Aplicacion.revisionActual;
        this.equipoActual = Aplicacion.equipoActual;
        this.tramoActual = Aplicacion.tramoActual;
        this.defectoActual = Aplicacion.defectoActual;

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.mapTomarCoordenadas);
        mapFragment.getMapAsync(this);

        botonAceptarCoordenadas = (Button) findViewById(R.id.botonAceptarCoordenadas);
        botonAceptarCoordenadas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                actualizarCoordenadasEnBD();
                abrirActividadAnterior();
            }
        });
        botonCancelar = (Button) findViewById(R.id.botonCancelarCoordenadas);
        botonCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                abrirActividadAnterior();
            }
        });

    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mapa = googleMap;
        Apoyo apoyoActual = dbRevisiones.solicitarApoyo(revisionActual, equipoActual, tramoActual);
        markerOptions = null;
        markerActual = null;

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

        mapa.setMapType(GoogleMap.MAP_TYPE_HYBRID);

        // Se inicializa el Marker en función del item que se ha de actualizar (equipo o defecto)

        if (tipo.equals(Aplicacion.EQUIPO)) {
            if (apoyoActual.getLatitud().equals("") || apoyoActual.getLongitud().equals("")) {
                if (MostrarEquipos.localizacion != null) {
                    posicionActual = new LatLng(MostrarEquipos.localizacion.getLatitude(),
                            MostrarEquipos.localizacion.getLongitude());
                    markerOptions = new MarkerOptions().position(posicionActual)
                            .title(getResources().getString(R.string.posicion_actual));
                }
            } else {
                try {
                    lat = Double.parseDouble(apoyoActual.getLatitud());
                    lng = Double.parseDouble(apoyoActual.getLongitud());
                    posicionActual = new LatLng(lat, lng);
                    markerOptions = new MarkerOptions().position(posicionActual)
                            .title(apoyoActual.getNombreEquipo());
                } catch (Exception e) {
                    Log.e(Aplicacion.TAG, "Error al convertir coordenadas");
                    Aplicacion.print(Aplicacion.TAG + "Error al convertir coordenadas");
                }
            }
        } else {
            Defecto def = dbRevisiones.solicitarDefecto(equipoActual, defectoActual, tramoActual);
            if (def.getLatitud().equals("") || def.getLongitud().equals("")) {
                if (MostrarEquipos.localizacion != null) {
                    posicionActual = new LatLng(MostrarEquipos.localizacion.getLatitude(),
                            MostrarEquipos.localizacion.getLongitude());
                    markerOptions = new MarkerOptions().position(posicionActual)
                            .title(getResources().getString(R.string.posicion_actual));
                }
            } else {
                try {
                    lat = Double.parseDouble(def.getLatitud());
                    lng = Double.parseDouble(def.getLongitud());
                    posicionActual = new LatLng(lat, lng);
                    markerOptions = new MarkerOptions().position(posicionActual)
                            .title(def.getCodigoDefecto());
                } catch (Exception e) {
                    Log.e(Aplicacion.TAG, "Error al convertir coordenadas");
                    Aplicacion.print(Aplicacion.TAG + "Error al convertir coordenadas");
                }
            }
        }

        if(markerOptions != null) {
            markerOptions.draggable(true);
            // Se pone el color del marcador en funcion del estado del Equipo
            Equipo equipo = dbRevisiones.solicitarEquipo(revisionActual, equipoActual, tramoActual);
            switch (equipo.getEstado()) {
                case Aplicacion.ESTADO_PENDIENTE:
                    markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
                    break;
                case Aplicacion.ESTADO_EN_CURSO:
                    markerOptions.icon((BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE)));
                    break;
                case Aplicacion.ESTADO_FINALIZADA:
                    markerOptions.icon((BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
                    break;
                default:
                    markerOptions.icon((BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
                    break;
            }
            markerActual = mapa.addMarker(markerOptions);
        }

        mapa.moveCamera(CameraUpdateFactory.newLatLngZoom(posicionActual,NIVEL_ZOOM));
        mapa.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {
            @Override
            public void onMarkerDragStart(Marker marker) {

            }

            @Override
            public void onMarkerDrag(Marker marker) {

            }

            @Override
            public void onMarkerDragEnd(Marker marker) {
                markerActual.setPosition(marker.getPosition());
            }
        });
        mapa.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng latLng) {
                if (markerActual == null) {
                    if (markerOptions == null){
                        markerOptions = new MarkerOptions().position(latLng)
                                .title(Aplicacion.equipoActual);
                    }
                    markerActual = mapa.addMarker(markerOptions);
                }
                markerActual.setPosition(latLng);
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        abrirActividadAnterior();
    }

    /**
     * Recoge la ubicacion del marcador y la almacena en la BDD en funcion de quien solicita las
     * coordenadas (Equipo o Defecto)
     */
    public void actualizarCoordenadasEnBD (){
        if (markerActual != null) {
            LatLng ubicacion = markerActual.getPosition();
            String latitud = Double.toString(ubicacion.latitude);
            String longitud = Double.toString(ubicacion.longitude);
            String tabla;
            // Se escoje la tabla en la que guardar las coordenadas en funcion del item que se ha de actualizar (equipo o defecto)
            if (tipo.equals(Aplicacion.EQUIPO)) {
                dbRevisiones.actualizarItemEquipoApoyo(DBRevisiones.TABLA_APOYOS, "Latitud", latitud, equipoActual);
                dbRevisiones.actualizarItemEquipoApoyo(DBRevisiones.TABLA_APOYOS, "Longitud", longitud, equipoActual);
            } else {
                dbRevisiones.actualizarItemDefecto(defectoActual, "Latitud", latitud);
                dbRevisiones.actualizarItemDefecto(defectoActual, "Longitud", longitud);
            }
        }
    }

    /**
     * Abre la actividad que ha solicitado las coordenadas (MostrarEquipos o DatosDefecto)
     */
    public void abrirActividadAnterior () {
        Intent intent;
        if (tipo.equals(Aplicacion.EQUIPO)) {
            intent = new Intent(this, MostrarEquipos.class);
        } else {
            intent = new Intent(this, DatosDefecto.class);
        }
        startActivity(intent);
        finish();
    }
}