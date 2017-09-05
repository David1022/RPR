package com.nipsa.rpr;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.ContextMenu;
import android.view.KeyEvent;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.Vector;

/**
 * Para no mostrar el teclado al iniciar la actividad incluir la siguiente línea en la declaración
 * de la activity correspondiente en el Manifest:
 *          android:windowSoftInputMode="stateAlwaysHidden"
 */

public class DatosDefecto extends AppCompatActivity {

    Activity activity;
    private DBRevisiones dbRevisiones;
    private DBGlobal dbGlobal;
    private String revisionActual, equipoActual, tramoActual, defectoActual;

    private TextView tvEquipo, tvDefecto, tvMostrarFoto, tvMedida;
    private TextView tvDatoFechaCorrecion, tvTrafo2, tvTrafo3;
    private EditText etLatitud, etLongitud, etOcurrencias, etMedida, etObservaciones;
    private EditText etMedidaTrafo2, etMedidaTrafo3;
    private CheckBox cbEsDefecto, cbCorregido, cbPatUnidas;
    private ImageView ivAddTrafo2, ivAddTrafo3, ivDelTrafo2, ivDelTrafo3;

    private String foto1, foto2, fechaCorreccion;
    private Uri uriFoto;
    private File fotoFile;
    private final int RESULTADO_FOTO = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_datos_defecto);
        activity = this;

        // Se inicializa variable para las consultas a la BBDD
        dbRevisiones = new DBRevisiones(this);
        dbGlobal = new DBGlobal(this);
        this.revisionActual = Aplicacion.revisionActual;
        this.equipoActual = Aplicacion.equipoActual;
        this.tramoActual = Aplicacion.tramoActual;
        this.defectoActual = Aplicacion.defectoActual;

        // Se recoge el nombre de la línea a revisar para mostrarlo en el título
        String nombreLinea = MostrarEquipos.listaEquipos.get(0).getCodigoTramo();
        if (nombreLinea.contains("-")) {
            nombreLinea = nombreLinea.substring(nombreLinea.lastIndexOf("-") + 1);
        }

        // Inicialización de los elementos de la AppBar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarDatosDefecto);
        toolbar.setTitle(getResources().getString(R.string.titulo_defectos) + " " + Aplicacion.revisionActual +
                " - " + nombreLinea + ": " + Aplicacion.equipoActual);
        setSupportActionBar(toolbar);

        // Se asignan las variables a su View correspondiente
        tvEquipo = (TextView) findViewById(R.id.tvDatoEquipo);
        tvDefecto = (TextView) findViewById(R.id.tvDatoCodigoDefecto);
        etLatitud = (EditText) findViewById(R.id.etDatoDefLatitud);
        etLongitud = (EditText) findViewById(R.id.etDatoDefLongitud);
        tvMedida = (TextView) findViewById(R.id.tvMedida);
        tvTrafo2 = (TextView) findViewById(R.id.tvTrafo2);
        tvTrafo3 = (TextView) findViewById(R.id.tvTrafo3);
        tvDatoFechaCorrecion = (TextView) findViewById(R.id.tvDatoFechaCorreccion);
        tvDatoFechaCorrecion.setText("");
        etOcurrencias = (EditText) findViewById(R.id.etDatoOcurrencias);
        cbPatUnidas = (CheckBox) findViewById(R.id.cbPatUnidas);
        etMedida = (EditText) findViewById(R.id.etDatoMedida);
        etMedidaTrafo2 = (EditText) findViewById(R.id.etTrafo2);
        etMedidaTrafo3= (EditText) findViewById(R.id.etTrafo3);
        etObservaciones = (EditText) findViewById(R.id.etDatoObservaciones);
        ivAddTrafo2 = (ImageView) findViewById(R.id.ivAddTrafo2);
        ivDelTrafo2 = (ImageView) findViewById(R.id.ivDelTrafo2);
        ivAddTrafo3 = (ImageView) findViewById(R.id.ivAddTrafo3);
        ivDelTrafo3 = (ImageView) findViewById(R.id.ivDelTrafo3);
        cbEsDefecto = (CheckBox) findViewById(R.id.cbDatoEsDefecto);
        cbCorregido = (CheckBox) findViewById(R.id.cbDatoCorregido);

        etOcurrencias.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (etMedida.isFocusable()) {
                    etMedida.requestFocus();
                } else {
                    etObservaciones.requestFocus();
                    etObservaciones.setSelection(etObservaciones.getText().length());
                }
                return false;
            }
        });
        cbPatUnidas.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    dbRevisiones.actualizarItemDefecto(defectoActual, "PaTUnidas", Aplicacion.SI);
                } else {
                    dbRevisiones.actualizarItemDefecto(defectoActual, "PaTUnidas", Aplicacion.NO);
                }
            }
        });
        etMedida.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (etMedidaTrafo2.getVisibility() == View.VISIBLE) {
                    etMedidaTrafo2.requestFocus();
                } else {
                    etObservaciones.requestFocus();
                    etObservaciones.setSelection(etObservaciones.getText().length());
                }
                return false;
            }
        });
        etMedidaTrafo2.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (etMedidaTrafo3.getVisibility() == View.VISIBLE) {
                    etMedidaTrafo3.requestFocus();
                } else {
                    etObservaciones.requestFocus();
                    etObservaciones.setSelection(etObservaciones.getText().length());
                }
                return false;
            }
        });
        etMedidaTrafo3.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                etObservaciones.requestFocus();
                etObservaciones.setSelection(etObservaciones.getText().length());
                return false;
            }
        });
        ivAddTrafo2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activarTr2();
            }
        });
        ivDelTrafo2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                desactivarTr2();
            }
        });
        ivAddTrafo3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activarTr3();
            }
        });
        ivDelTrafo3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                desactivarTr3();
            }
        });
        if (defectoActual.equals("T53D") || defectoActual.equals("T55D") || defectoActual.equals("T62D")) {
            ivAddTrafo2.setVisibility(View.VISIBLE);
            ivAddTrafo2.setClickable(true);
        }
        Equipo equipo = dbRevisiones.solicitarEquipo(revisionActual, equipoActual, tramoActual);
        Defecto defecto = dbRevisiones.solicitarDefecto(equipoActual, defectoActual, tramoActual);
        String textoObservaciones = equipo.getObservaciones();
        if (defecto.getObservaciones().equals("")){ // Si aún no se han introducido observaciones
            if (textoObservaciones.contains(Aplicacion.NUM_APOYO)){ // Se confirma que se haya introducido número de apoyo
                if (textoObservaciones.contains("-")) { // Si también hay observaciones del equipo se recoge sólo el num. apoyo
                    textoObservaciones = textoObservaciones.substring(0, textoObservaciones.indexOf("-"));
                }
                dbRevisiones.actualizarItemDefecto(defectoActual, "Observaciones", textoObservaciones);
            }
        }
        cbEsDefecto.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    if (sePuedeCorregir()) {
                        cbCorregido.setEnabled(true);
                        cbCorregido.setTextColor(getResources().getColor(R.color.negro));
                    }
                    dbRevisiones.actualizarItemDefecto(Aplicacion.defectoActual, "EsDefecto", Aplicacion.SI);
                    textoEsDefectoEnRojo(true);
                    alertaCorreccionInmediata();
                } else {
                    cbCorregido.setEnabled(false);
                    cbCorregido.setTextColor(getResources().getColor(R.color.gris));
                    cbCorregido.setChecked(false);
                    dbRevisiones.actualizarItemDefecto(Aplicacion.defectoActual, "Corregido", Aplicacion.NO);
                    dbRevisiones.actualizarItemDefecto(Aplicacion.defectoActual, "FechaCorreccion", "");
                    dbRevisiones.actualizarItemDefecto(Aplicacion.defectoActual, "EsDefecto", Aplicacion.NO);
                    textoEsDefectoEnRojo(false);
                }
            }
        });
        cbCorregido.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                String corregido;
                if (isChecked) {
                    Vector<Integer> vFecha = fechaAMostrar();
                    corregido = Aplicacion.SI;
                    fechaCorreccion = vFecha.get(0).toString() + "/" +      // Dia
                                        vFecha.get(1).toString() + "/" +    // Mes
                                        vFecha.get(2).toString();           // Año
                } else {
                    corregido = Aplicacion.NO;
                    fechaCorreccion = "";
                }
                dbRevisiones.actualizarItemDefecto(Aplicacion.defectoActual, "Corregido", corregido);
                dbRevisiones.actualizarItemDefecto(Aplicacion.defectoActual, "FechaCorreccion", fechaCorreccion);
                //dbRevisiones.actualizarItemEquipoApoyo(DBRevisiones.TABLA_EQUIPOS, "FechaCorrecion", fechaCorreccion,
                //                                            revisionActual, equipoActual, tramoActual);
                tvDatoFechaCorrecion.setText(fechaCorreccion);
            }
        });

        tvDatoFechaCorrecion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(cbCorregido.isChecked()) {
                    Vector<Integer> vFecha = fechaAMostrar();
                    int mDay = vFecha.get(0);
                    int mMonth = vFecha.get(1);
                    mMonth = mMonth - 1;
                    int mYear = vFecha.get(2);

                    // date picker dialog
                    DatePickerDialog datePickerDialog = new DatePickerDialog(activity,
                            new DatePickerDialog.OnDateSetListener() {

                                @Override
                                public void onDateSet(DatePicker view, int year,
                                                      int monthOfYear, int dayOfMonth) {
                                    String fechaSeleccionada = dayOfMonth + "/" + (monthOfYear + 1) + "/" + year;
                                    dbRevisiones.actualizarItemDefecto(Aplicacion.defectoActual, "FechaCorreccion", fechaSeleccionada);
                                    tvDatoFechaCorrecion.setText(fechaSeleccionada);
                                }
                            }, mYear, mMonth, mDay);
                    datePickerDialog.show();
                }

            }
        });

        tvMostrarFoto = (TextView) findViewById(R.id.tvMostrarFotos);
        tvMostrarFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mostrarFotos();
            }
        });

        // Listeners de los botones
        Button botonAceptar = (Button) findViewById(R.id.botonOkDefecto);
        ImageButton botonTomarFoto = (ImageButton) findViewById(R.id.botonFotosDefecto);
        ImageButton botonTomarCoordenadas = (ImageButton) findViewById(R.id.botonCoordenadasDefecto);
        botonAceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recogerDatos ();
                aceptarDatos ();
            }
        });
        botonTomarFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tomarFoto ();
            }
        });
        botonTomarCoordenadas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerForContextMenu(v);
                openContextMenu(v);
            }
        });

    }

    /**
     * Al volver el foco a la actividad se mostrarán los resultados guardados hasta el momento
     */
    @Override
    protected void onResume() {
        super.onResume();
        mostrarDatosAlmacenados ();
    }

    /**
     * Al presionar el boton volver se abrirá la actividad anterior. si el check "es defecto" está
     * activado se harán las comprobaciones necesarias
     */
    @Override
    public void onBackPressed() {
        recogerDatos();
        if (cbEsDefecto.isChecked()) {
            aceptarDatos();
        } else  {
            abrirActividadMostrarDefectos();
        }
    }

    /**
     * Cuando la actividad pierde el foco se guardan los datos introducidos en los EditText para que
     * no se pierdan
     */
    @Override
    protected void onPause() {
        super.onPause();
        guardarEditText();
    }

    /**
     * Se captura el resultado devuelto por la camara y si es ok, se guarda el nombre del archivo de la foto en la BD
    */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        if (requestCode == RESULTADO_FOTO && resultCode == Activity.RESULT_OK
                && uriFoto != null){
            guardarFotoEnBD(uriFoto);
        } else if (resultCode == Activity.RESULT_CANCELED && uriFoto != null) {
            // Si no se hace la foto, se borra el archivo generado
            File f = new File(uriFoto.getPath());
            f.delete();
        }

    }

    /**
     * Se genera el menú contextual que aparecera al presionar el boton "Tomar coordenadas"
     *
     * @param menu
     * @param v
     * @param menuInfo
     */
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = this.getMenuInflater();

        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
        menu.setHeaderTitle(getResources().getString(R.string.titulo_menu_aceptar_coordenadas));
        inflater.inflate(R.menu.menu_aceptar_coordenadas, menu);

    }

    /**
     * Se tratan el listener del menu contextual
     * @param item
     * @return
     */
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();

        switch (item.getItemId()) {
            case R.id.coordenadasEquipo:
                copiarCoordenadasEquipo(Aplicacion.equipoActual);
                break;
            case R.id.nuevasCoordenadas:
                tomarCoordenadas();
                break;
            default:
                break;
        }

        return super.onContextItemSelected(item);
    }

    /**
     * Se muestran los datos recogidos hasta el momento
     */
    public void mostrarDatosAlmacenados () {
        Defecto defecto = dbRevisiones.solicitarDefecto(equipoActual, defectoActual, tramoActual);
        String descripcionDefecto = dbGlobal.solicitarDescripcionPorCodigo(Aplicacion.defectoActual);

        // Si el defecto ya había sido inicializado se muestran los datos guardados en la BD
        if (defecto != null){
            boolean patUnidas = hayPatUnidas();
            tvEquipo.setText(Aplicacion.equipoActual);
            tvDefecto.setText(Aplicacion.defectoActual +  " - " + descripcionDefecto);
            etLatitud.setText(defecto.getLatitud());
            etLongitud.setText(defecto.getLongitud());
            etOcurrencias.setText(defecto.getOcurrencias());
            cbPatUnidas.setChecked(hayPatUnidas());
            switch (defectoActual) {
                case "T22B":
                    cbPatUnidas.setVisibility(View.INVISIBLE);
                    cbPatUnidas.setEnabled(false);
                    tvMedida.setText("Valor PaT:");
                    etMedida.setFocusable(true);
                    break;
                case "T53D":
                    tvMedida.setText("Rm - Tr1:");
                    etMedida.setFocusable(true);
                    if (!defecto.getMedidaTr3().equals("")) {
                        activarTr2();
                        activarTr3();
                        etMedidaTrafo2.setText(defecto.getMedidaTr2());
                        etMedidaTrafo3.setText(defecto.getMedidaTr3());
                    } else {
                        if (!defecto.getMedidaTr2().equals("")) {
                            activarTr2();
                            etMedidaTrafo2.setText(defecto.getMedidaTr2());
                        }
                    }
                    break;
                case "T62D":
                    //cbPatUnidas.setVisibility(View.INVISIBLE);
                    cbPatUnidas.setEnabled(false);
                    cbPatUnidas.setTextColor(getResources().getColor(R.color.gris));
                    tvMedida.setText("Rn: - Tr1:");
                    etMedida.setFocusable(true);
                    if (!defecto.getMedidaTr3().equals("")) {
                        activarTr2();
                        activarTr3();
                        etMedidaTrafo2.setText(defecto.getMedidaTr2());
                        etMedidaTrafo3.setText(defecto.getMedidaTr3());
                    } else {
                        if (!defecto.getMedidaTr2().equals("")) {
                            activarTr2();
                            etMedidaTrafo2.setText(defecto.getMedidaTr2());
                        }
                    }
                    break;
                case "T55D":
                    //cbPatUnidas.setVisibility(View.INVISIBLE);
                    cbPatUnidas.setEnabled(false);
                    cbPatUnidas.setTextColor(getResources().getColor(R.color.gris));
                    tvMedida.setText("Rc: - Tr1:");
                    etMedida.setFocusable(true);
                    if (!defecto.getMedidaTr3().equals("")) {
                        activarTr2();
                        activarTr3();
                        etMedidaTrafo2.setText(defecto.getMedidaTr2());
                        etMedidaTrafo3.setText(defecto.getMedidaTr3());
                    } else {
                        if (!defecto.getMedidaTr2().equals("")) {
                            activarTr2();
                            etMedidaTrafo2.setText(defecto.getMedidaTr2());
                        }
                    }
                    break;
                case "T53C":
                    cbPatUnidas.setVisibility(View.INVISIBLE);
                    cbPatUnidas.setEnabled(false);
                    tvMedida.setText("Rm - Tr1:");
                    etMedida.setFocusable(true);
                    ivAddTrafo2.setVisibility(View.INVISIBLE);
                    ivAddTrafo2.setFocusable(false);
                    break;
                case "T62C":
                    cbPatUnidas.setVisibility(View.INVISIBLE);
                    cbPatUnidas.setEnabled(false);
                    tvMedida.setText("Rn: - Tr1:");
                    etMedida.setFocusable(true);
                    ivAddTrafo2.setVisibility(View.INVISIBLE);
                    ivAddTrafo2.setFocusable(false);
                    break;
                case "T55C":
                    cbPatUnidas.setVisibility(View.INVISIBLE);
                    cbPatUnidas.setEnabled(false);
                    tvMedida.setText("Rc: - Tr1:");
                    etMedida.setFocusable(true);
                    ivAddTrafo2.setVisibility(View.INVISIBLE);
                    ivAddTrafo2.setFocusable(false);
                    break;
                default:
                    cbPatUnidas.setVisibility(View.INVISIBLE);
                    cbPatUnidas.setEnabled(false);
                    tvMedida.setTextColor(getResources().getColor(R.color.gris));
                    etMedida.setFocusable(false);
                    break;
            }
            etMedida.setText(defecto.getMedida());
            etMedidaTrafo2.setText(defecto.getMedidaTr2());
            etMedidaTrafo3.setText(defecto.getMedidaTr3());
            etObservaciones.setText(defecto.getObservaciones());

            if (defecto.getEsDefecto().equals(Aplicacion.SI)) {
                cbEsDefecto.setChecked(true);
                if (sePuedeCorregir()) {
                    cbCorregido.setEnabled(true);
                    cbCorregido.setTextColor(getResources().getColor(R.color.negro));
                }
                textoEsDefectoEnRojo(true);
            } else {
                cbEsDefecto.setChecked(false);
                cbCorregido.setEnabled(false);
                cbCorregido.setTextColor(getResources().getColor(R.color.gris));
                textoEsDefectoEnRojo(false);
            }

            if (defecto.getCorregido().equals(Aplicacion.SI)) {
                cbCorregido.setChecked(true);
                cbCorregido.setTextColor(getResources().getColor(R.color.negro));
                Vector<Integer> vFecha = fechaAMostrar();
                fechaCorreccion = vFecha.get(0).toString() + "/" +      // Dia
                                    vFecha.get(1).toString() + "/" +    // Mes
                                    vFecha.get(2).toString();           // Año
            } else {
                cbCorregido.setChecked(false);
                cbCorregido.setTextColor(getResources().getColor(R.color.gris));
                fechaCorreccion = "";
            }
            tvDatoFechaCorrecion.setText(fechaCorreccion);

            if (!defecto.getFoto1().equals("")) {
                activarMostrarFoto();
                foto1 = defecto.getFoto1();
            } else {
                desactivarMostrarFoto();
            }
            if (!defecto.getFoto2().equals("")) {
                foto2 = defecto.getFoto1();
            }

        }

    }

    /**
     * Consulta si se han marcado PaT unidas o no para el equipo actual
     * @return
     */
    private boolean hayPatUnidas () {
        Defecto def = dbRevisiones.solicitarDefecto(equipoActual, "T53D", tramoActual);
        boolean hayPatUnidas = false;

        if (def != null) {
            String s = def.getPatUnidas();
            if (s.equals(Aplicacion.SI)) {
                hayPatUnidas = true;
            }
        }

        return hayPatUnidas;
    }

    /**
     * Activa la visibilidad para los datos del trafo 2
     */
    private void activarTr2 () {
        ivAddTrafo2.setVisibility(View.INVISIBLE);
        ivAddTrafo2.setClickable(false);
        tvTrafo2.setVisibility(View.VISIBLE);
        tvTrafo2.setText("Tr2:");
        etMedidaTrafo2.setVisibility(View.VISIBLE);
        ivDelTrafo2.setVisibility(View.VISIBLE);
        ivDelTrafo2.setClickable(true);
        ivAddTrafo3.setVisibility(View.VISIBLE);
        ivAddTrafo3.setClickable(true);

    }

    /**
     * Desactiva la visibilidad para los datos del trafo 2
     */
    private void desactivarTr2 () {
        ivAddTrafo2.setVisibility(View.VISIBLE);
        ivAddTrafo2.setClickable(true);
        tvTrafo2.setVisibility(View.INVISIBLE);
        etMedidaTrafo2.setVisibility(View.INVISIBLE);
        ivDelTrafo2.setVisibility(View.INVISIBLE);
        ivDelTrafo2.setClickable(false);
        ivAddTrafo3.setVisibility(View.INVISIBLE);
        ivAddTrafo3.setClickable(false);
        etMedidaTrafo2.setText("");
        if (etMedidaTrafo2.hasFocus()) {
            etMedida.requestFocus();
        }
        //dbRevisiones.actualizarItemDefecto(defectoActual, "MedidaTr2", "");

    }

    /**
     * Activa la visibilidad para los datos del trafo 3
     */
    private void activarTr3 () {
        ivAddTrafo3.setVisibility(View.INVISIBLE);
        ivAddTrafo3.setClickable(false);
        tvTrafo3.setVisibility(View.VISIBLE);
        tvTrafo3.setText("Tr3:");
        etMedidaTrafo3.setVisibility(View.VISIBLE);
        ivDelTrafo2.setVisibility(View.INVISIBLE);
        ivDelTrafo2.setClickable(false);
        ivDelTrafo3.setVisibility(View.VISIBLE);
        ivDelTrafo3.setClickable(true);

    }

    /**
     * Desactiva la visibilidad para los datos del trafo 3
     */
    private void desactivarTr3 () {
        ivDelTrafo2.setVisibility(View.VISIBLE);
        ivDelTrafo2.setClickable(true);
        ivAddTrafo3.setVisibility(View.VISIBLE);
        ivAddTrafo3.setClickable(true);
        tvTrafo3.setVisibility(View.INVISIBLE);
        etMedidaTrafo3.setVisibility(View.INVISIBLE);
        ivDelTrafo3.setVisibility(View.INVISIBLE);
        ivDelTrafo3.setClickable(false);
        etMedidaTrafo3.setText("");
        if (etMedidaTrafo3.hasFocus()) {
            etMedidaTrafo2.requestFocus();
        }

    }

    /**
         * Se cambia el color del texto del check "Es Defecto"
         *
         * @param enRojo
         */
    private void textoEsDefectoEnRojo (boolean enRojo) {
        if (enRojo) {
            cbEsDefecto.setTextColor(getResources().getColor(R.color.texto_defecto));
        } else {
            cbEsDefecto.setTextColor(getResources().getColor(R.color.texto));
        }
    }

    /**
     * Se toma la fecha a mostrar
     *
     * @return
     */
    private Vector<Integer> fechaAMostrar () {
        Defecto defecto = dbRevisiones.solicitarDefecto(equipoActual, defectoActual, tramoActual);
        String fechaGuardada = defecto.getFechaCorreccion();
        Vector<Integer> fechaAMostrar = new Vector<Integer>();
        int mDay, mMonth, mYear;

        // Si hay fecha guardada se recupera la fecha de la BDD
        if (!fechaGuardada.equals("")) {
            mYear = Integer.parseInt(fechaGuardada.substring(fechaGuardada.lastIndexOf("/") + 1)); // Año guardado
            mMonth = Integer.parseInt(fechaGuardada.substring(
                    (fechaGuardada.indexOf("/") + 1), fechaGuardada.lastIndexOf("/")));; // Mes guardado
            mDay = Integer.parseInt(fechaGuardada.substring(0, fechaGuardada.indexOf("/"))); // Dia guardado
        } else { // Si no se recoge la fecha actual
            Calendar c = Calendar.getInstance();
            mYear = c.get(Calendar.YEAR); // Año actual
            mMonth = c.get(Calendar.MONTH); // Mes actual
            mMonth = mMonth + 1; // El mes es zero-index
            mDay = c.get(Calendar.DAY_OF_MONTH); // Día actual
        }
        fechaAMostrar.clear();
        fechaAMostrar.add(mDay);
        fechaAMostrar.add(mMonth);
        fechaAMostrar.add(mYear);

        return fechaAMostrar;
    }

    /**
     * Se copian las coordenadas del equipo al defecto. Esto permite que se pueda asociar un defecto
     * en un punto distinto al apoyo (p.e. en mitad de un vano)
     *
     * @param nombreEquipo
     */
    private void copiarCoordenadasEquipo (String nombreEquipo) {
        Cursor c = dbRevisiones.solicitarItem(DBRevisiones.TABLA_APOYOS, "Latitud", "NombreEquipo", nombreEquipo);
        c.moveToFirst();
        String lat = c.getString(0);
        c.close();
        c = dbRevisiones.solicitarItem(DBRevisiones.TABLA_APOYOS, "Longitud", "NombreEquipo", nombreEquipo);
        c.moveToFirst();
        String lng = c.getString(0);
        c.close();
        dbRevisiones.actualizarItemDefecto(Aplicacion.defectoActual, "Latitud", lat);
        dbRevisiones.actualizarItemDefecto(Aplicacion.defectoActual, "Longitud", lng);

        Defecto defecto = dbRevisiones.solicitarDefecto(equipoActual, defectoActual, tramoActual);
        etLatitud.setText(defecto.getLatitud());
        etLongitud.setText(defecto.getLongitud());

    }

    /**
     * Se abre la actividad que mostrará el mapa para tomar las coordenadas de la ubicación del defecto
     */
    private void tomarCoordenadas() {
        Intent intent = new Intent(this, TomarCoordenadas.class);
        intent.putExtra(Aplicacion.TIPO, Aplicacion.DEFECTO);
        startActivity(intent);
        finish();

    }

    /**
     * Se toman todos los datos introducidos hasta el momento
     */
    private void recogerDatos () {
        String corregido;

        dbRevisiones.actualizarItemDefecto(Aplicacion.defectoActual,
                                                "Latitud",
                                                etLatitud.getText().toString());
        dbRevisiones.actualizarItemDefecto(Aplicacion.defectoActual,
                                                "Longitud",
                                                etLongitud.getText().toString());
        guardarEditText();
        if (cbCorregido.isChecked()) {
            corregido = Aplicacion.SI;
        } else {
            corregido = Aplicacion.NO;
        }
        dbRevisiones.actualizarItemDefecto(Aplicacion.defectoActual, "Corregido", corregido);
        dbRevisiones.actualizarItemDefecto(Aplicacion.defectoActual, "FechaCorreccion", fechaCorreccion);
        //dbRevisiones.actualizarItemEquipoApoyo(DBRevisiones.TABLA_EQUIPOS, "FechaCorrecion", fechaCorreccion,
        //        revisionActual, equipoActual, tramoActual);

    }

    /**
     * Se comprueba que se hayan recogido los datos mínimos necesarios para el defecto
     */
    private void aceptarDatos () {
        if (cbEsDefecto.isChecked()) {
            if (!etLatitud.getText().toString().equals("") && !etLongitud.getText().toString().equals("")) {
                if (foto1 != null) {
                    if (!etOcurrencias.getText().toString().equals("")){
                        abrirActividadMostrarDefectos();
                    } else {
                        Aplicacion.print("Introduce las OCURRENCIAS del defecto o desmarca el check \"Es defecto\"");
                    }
                } else {
                    Aplicacion.print("Haz FOTO del defecto o desmarca el check \"Es defecto\"");
                }
            } else {
                Aplicacion.print("Toma COORDENADAS del defecto o desmarca el check \"Es defecto\"");
            }
        } else {
            abrirActividadMostrarDefectos();
        }

    }

    /**
     * Se guardan en la BDD los datos introducidos hasta este momento en los EditText
     */
    private void guardarEditText () {
        dbRevisiones.actualizarItemDefecto(defectoActual, "Ocurrencias", etOcurrencias.getText().toString());
        dbRevisiones.actualizarItemDefecto(defectoActual, "Medida", etMedida.getText().toString());
        dbRevisiones.actualizarItemDefecto(defectoActual, "MedidaTr2", etMedidaTrafo2.getText().toString());
        dbRevisiones.actualizarItemDefecto(defectoActual, "MedidaTr3", etMedidaTrafo3.getText().toString());
        dbRevisiones.actualizarItemDefecto(defectoActual, "Observaciones", etObservaciones.getText().toString());

    }

    /**
     * Crea diálogo para alertar que el defecto es de corrección inmediata
     */
    private void alertaCorreccionInmediata () {
        String aviso = dbGlobal.solicitarItem(Aplicacion.defectoActual, "CorreccioInmediata");

        if (aviso.equalsIgnoreCase(Aplicacion.SI)) {
            AlertDialog.Builder dialogo = new AlertDialog.Builder(this);
            dialogo.setTitle(R.string.titulo_correccion_inmediata)
                    .setMessage(Aplicacion.defectoActual + getResources().getString(R.string.mensaje_correccion_inmediata))
                    .setCancelable(false)
                    .setPositiveButton(getResources().getString(R.string.aceptar), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    })
                    .create()
                    .show();
        }
    }

    /**
     * Se vuelve a la actividad anterior
     */
    public void abrirActividadMostrarDefectos () {
        Intent intent = new Intent(this, MostrarDefectos.class);
        startActivity(intent);
        finish();
    }

    /**
     * Se genera el intent para tomar la foto
     */
    public void tomarFoto() {

        Intent tomarFotoIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Se confirma que existe alguna app que utilice la camera para manejar el intent
        if (tomarFotoIntent.resolveActivity(this.getPackageManager()) != null) {
            // Se genera el fichero donde se guardará la foto
            fotoFile = null;
            try {
                fotoFile = Aplicacion.crearArchivoImagen();
            } catch (IOException ex) {
                // Si hay error al generar el archivo
                Aplicacion.print("Error al generar el archivo");
            }
            if (fotoFile != null) {
                uriFoto = Uri.fromFile(fotoFile);
                String s = uriFoto.toString();
                tomarFotoIntent.putExtra(MediaStore.EXTRA_OUTPUT, uriFoto);
                startActivityForResult(tomarFotoIntent, RESULTADO_FOTO);
            }
        }
    }

    /**
     * Se mostrará la foto en el ImageView correspondiente y se guardará en la BDD
     *
     * @param uri
     */
    public void guardarFotoEnBD(Uri uri) {
        Defecto defecto = dbRevisiones.solicitarDefecto(equipoActual, defectoActual, tramoActual);
        // String rutaRelativa = Aplicacion.equipoActual + "/";

        if (defecto.getFoto1().equals("")) {
            foto1 = uri.toString();
            foto1 = foto1.substring(foto1.lastIndexOf("/")+1);
            // rutaRelativa = rutaRelativa + foto1;
            dbRevisiones.actualizarItemDefecto(Aplicacion.defectoActual, "Foto1", foto1);
            activarMostrarFoto();
        } else if (defecto.getFoto2().equals("")) {
            foto2 = uri.toString();
            foto2 = foto2.substring(foto2.lastIndexOf("/")+1);
            // rutaRelativa = rutaRelativa + foto2;
            dbRevisiones.actualizarItemDefecto(Aplicacion.defectoActual, "Foto2", foto2);
        } else {
            Aplicacion.print("La foto se ha guardado correctamente pero sólo se han asociado 2 fotos al defecto");
        }
    }

    /**
     * Se modifica el texto para permitir tomar una foto
     */
    public void activarMostrarFoto() {
        tvMostrarFoto.setText(R.string.mostrar_fotos);
        tvMostrarFoto.setTextColor(getResources().getColor(R.color.texto_clickable));

    }

    /**
     * Se modifica el texto para no permitir tomar una foto
     */
    public void desactivarMostrarFoto() {
        tvMostrarFoto.setText(R.string.no_hay_fotos);
        tvMostrarFoto.setTextColor(getResources().getColor(R.color.texto));

    }

    /**
     * Abre la actividad que mostrará las fotos realizadas hasta el momento
     */
    public void mostrarFotos () {
        Defecto defecto = dbRevisiones.solicitarDefecto(equipoActual, defectoActual, tramoActual);
        boolean hayFoto = !defecto.getFoto1().equals("");

        if (hayFoto) {
            Intent intent = new Intent(this, FotosDefecto.class);
            startActivity(intent);
        }

    }

    private boolean sePuedeCorregir () {
        boolean sePuedeCorregir = false;
        if (defectoActual.startsWith("J51") || defectoActual.startsWith("J61") || defectoActual.startsWith("V25")
                || defectoActual.startsWith("V31") || defectoActual.startsWith("V32")
                || defectoActual.startsWith("V2Q") || defectoActual.startsWith("R4D")) {
            sePuedeCorregir = true;
        }

        return sePuedeCorregir;
    }

}