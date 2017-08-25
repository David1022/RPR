package com.nipsa.rpr;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import java.util.Vector;

public class MostrarDefectos extends AppCompatActivity implements FrgListadoDefectos.GrupoDefectoListener {

    private DBGlobal dbGlobal;
    private DBRevisiones dbRevisiones;
    private FrgListadoDefectos frgListadoDefectos;
    private String revisionActual, equipoActual, tramoActual;
    public static Vector<ListaDef> listaAMostrar;
    public static Vector<Defecto> registroDefectos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mostrar_defectos);

        this.revisionActual = Aplicacion.revisionActual;
        this.equipoActual = Aplicacion.equipoActual;
        this.tramoActual = Aplicacion.tramoActual;
        dbGlobal = new DBGlobal(this);
        dbRevisiones = new DBRevisiones(this);

        // Se recoge el nombre de la línea a revisar para mostrarlo en el título
        String nombreLinea = MostrarEquipos.listaEquipos.get(0).getCodigoTramo();
        if (nombreLinea.contains("-")) {
            nombreLinea = nombreLinea.substring(nombreLinea.lastIndexOf("-") + 1);
        }

        // Inicialización de los elementos de la AppBar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarMostrarDefectos);
        toolbar.setTitle(getResources().getString(R.string.titulo_defectos) + " " + Aplicacion.revisionActual +
                " - " + nombreLinea + ": " + Aplicacion.equipoActual);
        setSupportActionBar(toolbar);

        // Listener del FloatingActionButton
        FloatingActionButton fabFinalizarRevisionEquipo = (FloatingActionButton) findViewById(R.id.fabFinRevisionEquipo);
        fabFinalizarRevisionEquipo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finalizarRevisionEquipo();
            }
        });

        // Se carga el fragment estatico que listará las revisiones cargadas
        frgListadoDefectos = (FrgListadoDefectos) getSupportFragmentManager().findFragmentById(R.id.FrgListadoDefectos);
        // Se asigna listener para escuchar clicks sobre el listado
        frgListadoDefectos.setGrupoDefectoListener(this);

    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(this, MostrarEquipos.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onGrupoDefectoSeleccionado(String grupo) {
        actualizarListaDefectosAMostrar(grupo);
        registroDefectos = dbRevisiones.solicitarDefectosRegistrados(revisionActual, equipoActual, tramoActual);
        refrescarFragment();
    }

    public void actualizarListaDefectosAMostrar(String grupo) {
        String codigoGrupo = "A";
        switch (Aplicacion.tipoActual){
            case Aplicacion.LAMT:
                switch (grupo){
                    case "Aislamiento":
                        codigoGrupo = "A";
                        break;
                    case "Conductor":
                        codigoGrupo = "C";
                        break;
                    case "Cruces":
                        codigoGrupo = "G";
                        break;
                    case "Herrajes":
                        codigoGrupo = "H";
                        break;
                    case "Cimientos":
                        codigoGrupo = "I";
                        break;
                    case "Estructura":
                        codigoGrupo = "J";
                        break;
                    case "Paso por zonas":
                        codigoGrupo = "L";
                        break;
                    case "Apoyos":
                        codigoGrupo = "P";
                        break;
                    case "Aparamenta":
                        codigoGrupo = "R";
                        break;
                    case "Puestas a Tierra":
                        codigoGrupo = "T";
                        break;
                    case "Entorno perimetral":
                        codigoGrupo = "V";
                        break;
                    case "Observaciones":
                        codigoGrupo = "Z";
                        break;
                    default:
                        break;
                }
                break;
            case Aplicacion.CT:
                switch (grupo) {
                    case "Aislamiento":
                        codigoGrupo = "A";
                        break;
                    case "Transformador":
                        codigoGrupo = "F";
                        break;
                    case "Aparamenta y dispositivos de maniobra":
                        codigoGrupo = "R";
                        break;
                    case "Puestas a Tierra":
                        codigoGrupo = "T";
                        break;
                    case "Secundario BT del transformador":
                        codigoGrupo = "U";
                        break;
                    case "Protecciones y obra civil":
                        codigoGrupo = "V";
                        break;
                    case "Embarrados y conexiones":
                        codigoGrupo = "X";
                        break;
                    case "Observaciones":
                        codigoGrupo = "Z";
                        break;
                    default:
                        break;
                }
                break;
            case Aplicacion.PT:
                switch (grupo){
                    case "Aislamiento":
                        codigoGrupo = "A";
                        break;
                    case "Distancias (D)":
                        codigoGrupo = "D";
                        break;
                    case "Transformador":
                        codigoGrupo = "F";
                        break;
                    case "Herrajes":
                        codigoGrupo = "H";
                        break;
                    case "Cimientos":
                        codigoGrupo = "I";
                        break;
                    case "Estructura":
                        codigoGrupo = "J";
                        break;
                    case "Distancias (L)":
                        codigoGrupo = "L";
                        break;
                    case "Apoyos":
                        codigoGrupo = "P";
                        break;
                    case "Aparamenta y dispositivos de maniobra":
                        codigoGrupo = "R";
                        break;
                    case "Puestas a Tierra":
                        codigoGrupo = "T";
                        break;
                    case "Secundario BT del transformador":
                        codigoGrupo = "U";
                        break;
                    case "Entorno perimetral":
                        codigoGrupo = "V";
                        break;
                    case "Embarrados y conexiones":
                        codigoGrupo = "X";
                        break;
                    case "Observaciones":
                        codigoGrupo = "Z";
                        break;
                }
                break;
            default:
                break;
        }
        listaAMostrar = dbGlobal.solicitarListaDef(Aplicacion.tipoActual, codigoGrupo);
    }

    public void refrescarFragment(){
        boolean hayDetalle =
                (getSupportFragmentManager().findFragmentById(R.id.contenedorFragmentDetalleDefecto) != null);

        FrgDetalleDefectos fdd = new FrgDetalleDefectos();
        if (!hayDetalle) {
            getSupportFragmentManager().beginTransaction().add(R.id.contenedorFragmentDetalleDefecto, fdd).commit();
        } else {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.contenedorFragmentDetalleDefecto, fdd);
            transaction.commit();
        }

    }

    public void finalizarRevisionEquipo () {
        Equipo equipo = dbRevisiones.solicitarEquipo(revisionActual, equipoActual, tramoActual);
        String s = equipo.getEstado();
        String s2 = Aplicacion.ESTADO_FINALIZADA;
        boolean b = s.equals(s2);
        if (!equipo.getEstado().equals(Aplicacion.ESTADO_FINALIZADA)){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setPositiveButton(R.string.aceptar, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    actualizarDefectos();
                }
            });
            builder.setNegativeButton(R.string.cancelar, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            builder.setCancelable(false)
                    .setTitle(getResources().getString(R.string.titulo_confirmar_finalizar_revision))
                    .setMessage(getResources().getString(R.string.texto_confirmar_finalizar_revision));
            builder.show();
        } else {
            Aplicacion.print("La revisión ya ha sido marcada como finalizada");
        }
    }

    public void actualizarDefectos () {
        Vector <ListaDef> lista = dbGlobal.solicitarListaDef(Aplicacion.tipoActual);
        for (int i=0; i<lista.size(); i++) {
            String codigo = lista.elementAt(i).getCodigoEndesa2012();
            Defecto def = dbRevisiones.solicitarDefecto(equipoActual, codigo, tramoActual);
            if (def == null) {
                dbRevisiones.incluirDefecto(Aplicacion.revisionActual, equipoActual, codigo, tramoActual);
                dbRevisiones.actualizarItemDefecto(codigo, "EsDefecto", Aplicacion.NO);
            }
            dbRevisiones.actualizarItemEquipoApoyo(DBRevisiones.TABLA_EQUIPOS, "Estado", Aplicacion.ESTADO_FINALIZADA,
                                                        revisionActual, equipoActual, tramoActual);
        }

        finish();
        Intent intent = new Intent(this, MostrarEquipos.class);
        startActivity(intent);
    }
}