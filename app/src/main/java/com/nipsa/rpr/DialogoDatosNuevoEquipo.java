package com.nipsa.rpr;

import android.app.Activity;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.os.Bundle;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.Vector;

public class DialogoDatosNuevoEquipo extends DialogFragment {

    private EditText etNombreEquipo;
    private DBRevisiones dbRevisiones;
    private Spinner spTipo, spTramo;
    private String textoTipo, textoTramo;
    private Vector<String> listaTramos;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        dbRevisiones = new DBRevisiones(getContext());
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View vista = inflater.inflate(R.layout.dialogo_datos_nuevo_equipo, null);

        etNombreEquipo = (EditText) vista.findViewById(R.id.etNombreEquipo);
        // Se puebla el spinner estaticamente
        spTipo = (Spinner) vista.findViewById(R.id.SpinnerTipoEquipo);
        ArrayAdapter spTipoAdapter = new ArrayAdapter(getContext(), R.layout.spinner_detalle_equipo,
                                                        getResources().getTextArray(R.array.spinner_tipo_equipo));
        spTipoAdapter.setDropDownViewResource(R.layout.spinner_dropdown);
        spTipo.setAdapter(spTipoAdapter);

        textoTipo = Aplicacion.LAMT; // Se le asigna un valor por defecto que cambiará con el spinner
        // Se puebla el spinner dinamicamente
        listaTramos = dbRevisiones.solicitarTramos(Aplicacion.revisionActual);
        textoTramo = listaTramos.get(0); // Se le asigna un valor por defecto que cambiará con el spinner
        spTramo = (Spinner) vista.findViewById(R.id.SpinnerTramoEquipo);
        ArrayAdapter spTramoAdapter = new ArrayAdapter(getContext(), R.layout.spinner_detalle_equipo, listaTramos);
        spTramoAdapter.setDropDownViewResource(R.layout.spinner_dropdown);
        spTramo.setAdapter(spTramoAdapter);

        spTipo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                textoTipo = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
        spTramo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                textoTramo = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spTipo.setSelection(0);

        builder.setView(vista)
                .setCancelable(false)
                .setIcon(R.drawable.logo_nipsa)
                .setNegativeButton(R.string.cancelar, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setPositiveButton(R.string.aceptar, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String nombreEquipo = etNombreEquipo.getText().toString();
                        if(!nombreEquipo.equals("")) {
                            if (dbRevisiones.numEquiposIguales(Aplicacion.revisionActual, nombreEquipo) == 0) {
                                String tipoInst = "L";
                                Aplicacion.equipoActual = nombreEquipo;
                                Aplicacion.tramoActual = textoTramo;
                                switch (textoTipo){
                                    case Aplicacion.LAMT:
                                        tipoInst = "L";
                                        break;
                                    case Aplicacion.CT:
                                        tipoInst = "Z";
                                        break;
                                    case Aplicacion.PT:
                                        tipoInst = "Z";
                                        break;
                                    default:
                                        break;
                                }
                                // Se incluyen los datos del nuevo equipo en la tabla Equipos
                                dbRevisiones.incluirNuevoEquipo(Aplicacion.revisionActual, nombreEquipo, tipoInst, textoTramo);
                                if (tipoInst.equals("L")) {
                                    dbRevisiones.actualizarItemEquipoApoyoActual(DBRevisiones.TABLA_EQUIPOS,
                                            "CodigoBDE", Aplicacion.revisionActual);
                                }
                                dbRevisiones.actualizarItemEquipoApoyoActual(DBRevisiones.TABLA_EQUIPOS, "TipoEquipo", textoTipo);
                                dbRevisiones.actualizarItemEquipoApoyoActual(DBRevisiones.TABLA_EQUIPOS, "TipoInstalacion", tipoInst);
                                // Se incluyen los datos del nuevo equipo en la tabla Apoyos
                                if (tipoInst.equals("L")) {
                                    dbRevisiones.actualizarItemEquipoApoyoActual(DBRevisiones.TABLA_APOYOS,
                                                                                    "CodigoApoyo", nombreEquipo);
                                    dbRevisiones.actualizarItemEquipoApoyoActual(DBRevisiones.TABLA_APOYOS,
                                            "NombreInstalacion", Aplicacion.revisionActual);
                                    dbRevisiones.actualizarItemEquipoApoyoActual(DBRevisiones.TABLA_APOYOS,
                                            "MaxTension", "MT");
                                } else {
                                    dbRevisiones.actualizarItemEquipoApoyoActual(DBRevisiones.TABLA_APOYOS,
                                            "NombreInstalacion", nombreEquipo);
                                }
                                dbRevisiones.actualizarItemEquipoApoyoActual(DBRevisiones.TABLA_APOYOS, "TipoInstalacion", tipoInst);

                                dbRevisiones.actualizarItemEquipoApoyoActual(DBRevisiones.TABLA_APOYOS, "CodigoTramo", textoTramo);
                                Intent intent = new Intent(getContext(), MostrarEquipos.class);
                                startActivity(intent);
                                getActivity().finish();
                            } else {
                                Aplicacion.print("El nombre del equipo ya existe");
                            }
                        } else {
                            Aplicacion.print("No se ha introducido nombre para el nuevo apoyo");
                        }
                    }
                });

        return builder.create();
    }

}