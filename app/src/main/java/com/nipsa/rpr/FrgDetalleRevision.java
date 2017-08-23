package com.nipsa.rpr;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class FrgDetalleRevision extends Fragment {

    private Aplicacion aplicacion;
    private DBRevisiones dbRevisiones;
    private EditText etInspector1, etInspector2, etColegiado, etEquiposUsados, etMetodologia;
    private EditText etCodigoNipsa, etTrabajo, etCodigoInspeccion;
    private String inspector1, inspector2, colegiado, equiposUsados, metodologia;
    private String codigoNipsa, trabajo, codigoInspeccion, revisionActual;
    private View v;

    /**
     *
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.frg_detalle_revision, container, false);
        aplicacion = (Aplicacion) getActivity().getApplication();
        dbRevisiones = new DBRevisiones(aplicacion.getApplicationContext());
        revisionActual = Aplicacion.revisionActual;

        // Se inicializan todas las variables
        etInspector1 = (EditText) v.findViewById(R.id.ETInspector1);
        etInspector2 = (EditText) v.findViewById(R.id.ETInspector2);
        etColegiado = (EditText) v.findViewById(R.id.ETColegiado);
        etEquiposUsados = (EditText) v.findViewById(R.id.ETEquipos);
        etMetodologia = (EditText) v.findViewById(R.id.ETMetodologia);
        etCodigoNipsa = (EditText) v.findViewById(R.id.ETCodigoNipsa);
        etTrabajo = (EditText) v.findViewById(R.id.ETTrabajo);
        etCodigoInspeccion = (EditText) v.findViewById(R.id.ETCodigoInterno);
        Button boton = (Button) v.findViewById(R.id.botonIniciarInspeccion);

        // Se asignan los listeners de los EditText y los botones
        etInspector1.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    if (Aplicacion.revisionActual != null) {
                        if (!Aplicacion.revisionActual.equals("")) {
                            dbRevisiones.actualizarItemRevision(revisionActual, "Inspector1",
                                    etInspector1.getText().toString());
                        }
                    }
                }
            }
        });
        etInspector1.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                etInspector2.requestFocus();
                return false;
            }
        });
        etInspector2.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    if (Aplicacion.revisionActual != null) {
                        if (!Aplicacion.revisionActual.equals("")) {
                            dbRevisiones.actualizarItemRevision(revisionActual, "Inspector2",
                                    etInspector2.getText().toString());
                        }
                    }
                }
            }
        });
        etInspector2.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                etColegiado.requestFocus();
                return false;
            }
        });
        etColegiado.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    if (Aplicacion.revisionActual != null) {
                        if (!Aplicacion.revisionActual.equals("")) {
                            dbRevisiones.actualizarItemRevision(revisionActual, "Colegiado",
                                    etColegiado.getText().toString());
                        }
                    }
                }
            }
        });
        etColegiado.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                etEquiposUsados.requestFocus();
                return false;
            }
        });
        etEquiposUsados.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    if (Aplicacion.revisionActual != null) {
                        if (!Aplicacion.revisionActual.equals("")) {
                            dbRevisiones.actualizarItemRevision(revisionActual, "EquipoUsado",
                                    etEquiposUsados.getText().toString());
                        }
                    }
                }
            }
        });
        etEquiposUsados.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                etMetodologia.requestFocus();
                return false;
            }
        });
        etMetodologia.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    if (Aplicacion.revisionActual != null) {
                        if (!Aplicacion.revisionActual.equals("")) {
                            dbRevisiones.actualizarItemRevision(revisionActual, "Metodologia",
                                    etMetodologia.getText().toString());
                        }
                    }
                }
            }
        });
        etMetodologia.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                etCodigoNipsa.requestFocus();
                return false;
            }
        });
        etCodigoNipsa.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    if (Aplicacion.revisionActual != null) {
                        if (!Aplicacion.revisionActual.equals("")) {
                            dbRevisiones.actualizarItemRevision(revisionActual, "CodigoNipsa",
                                    etCodigoNipsa.getText().toString());
                        }
                    }
                }
            }
        });
        etCodigoNipsa.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                etTrabajo.requestFocus();
                return false;
            }
        });
        etTrabajo.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    if (Aplicacion.revisionActual != null) {
                        if (!Aplicacion.revisionActual.equals("")) {
                            dbRevisiones.actualizarItemRevision(revisionActual, "NumeroTrabajo",
                                    etTrabajo.getText().toString());
                        }
                    }
                }
            }
        });
        etTrabajo.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                etCodigoInspeccion.requestFocus();
                return false;
            }
        });
        etCodigoInspeccion.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    if (Aplicacion.revisionActual != null) {
                        if (!Aplicacion.revisionActual.equals("")) {
                            dbRevisiones.actualizarItemRevision(revisionActual, "CodigoInspeccion",
                                    etCodigoInspeccion.getText().toString());
                        }
                    }
                }
            }
        });
        boton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inspector1 = etInspector1.getText().toString();
                inspector2 = etInspector2.getText().toString();
                colegiado = etColegiado.getText().toString();
                equiposUsados = etEquiposUsados.getText().toString();
                metodologia = etMetodologia.getText().toString();
                codigoNipsa = etCodigoNipsa.getText().toString();
                trabajo = etTrabajo.getText().toString();
                codigoInspeccion = etCodigoInspeccion.getText().toString();
                if (seHanIntroducidoDatos()){
                    abrirMostrarEquipos();
                }
            }
        });

        mostrarTextoAlmacenado();

        return v;
    }

    /**
     * Se muestran los datos almacenados
     */
    public void mostrarTextoAlmacenado() {
        Revision rev = dbRevisiones.solicitarRevision(Aplicacion.revisionActual);
        String texto = Aplicacion.revisionActual;
        if (texto == null || texto.equals("")) {
            texto = getResources().getString(R.string.no_se_muestran_revisiones);
        }
        TextView titulo = (TextView) v.findViewById(R.id.tituloRevisionSeleccionada);
        titulo.setText(texto);
        if (rev != null) {
            etInspector1.setText(rev.getInspector1());
            etInspector2.setText(rev.getInspector2());
            etColegiado.setText(rev.getColegiado());
            etEquiposUsados.setText(rev.getEquiposUsados());
            etMetodologia.setText(rev.getMetodologia());
            etCodigoNipsa.setText(rev.getCodigoNipsa());
            etTrabajo.setText(rev.getNumTrabajo());
            etCodigoInspeccion.setText(rev.getCodigoInspeccion());
        }
    }

    /**
     *
     * @return  true: si se han introducido datos en todos los EditText
     *          false: si al menos un EditText no tiene datos introducidos
     */
    public boolean seHanIntroducidoDatos() {
        boolean ok = false;
        if ((inspector1.length() > 0) && (inspector2.length() > 0) && (colegiado.length() > 0) &&
                (equiposUsados.length() > 0) && (metodologia.length() > 0) &&
                (codigoNipsa.length() > 0) && (trabajo.length() > 0) && (codigoInspeccion.length() > 0)){
            ok = true;
        } else {
            Aplicacion.print(getResources().getString(R.string.debe_introducir_datos));
        }

        return ok;
    }

    /**
     * Abre la siguiente actividad
     */
    public void abrirMostrarEquipos() {
       if ((Aplicacion.revisionActual != null) || (!Aplicacion.revisionActual.equals(""))) {
           MostrarRevisiones.actualizarRevision(inspector1, inspector2, colegiado, equiposUsados,
                   metodologia, codigoNipsa, trabajo, codigoInspeccion);
            Intent intent = new Intent(getContext(), MostrarEquipos.class);
            startActivity(intent);
            getActivity().finish();
       }
    }

}