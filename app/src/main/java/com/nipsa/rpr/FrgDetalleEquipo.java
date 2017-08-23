package com.nipsa.rpr;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;

public class FrgDetalleEquipo extends Fragment {

    private Aplicacion aplicacion;
    private DBRevisiones dbRevisiones;
    private String revisionActual, equipoActual, tramoActual;
    private Uri uriFoto;
    private File fotoFile;
    private final int RESULTADO_FOTO = 1;
    private TextView tvDatoInstalacion, tvDatoTramo, tvDatoTerritorio, tvDatoLatitud, tvDatoLongitud;
    private TextView tvFechaRevision, tvMostrarFoto;
    private EditText etObservaciones, etNumApoyo;
    private LinearLayout lMaterial, lCruces, lManiobra, lConversion, lNumApoyo;
    private Spinner spMaterial, spTipo;
    private RadioGroup rgCruces, rgManiobra, rgConversion;
    private ImageButton botonAlternarEquipo, botonCoord, botonFoto, botonNoRevisables;
    private Button botonInspeccion;
    private Double lat, lng;
    private String textoMaterial, textoTipo, hayCruces, hayManiobra, hayConversion;

    /**
     * Se inicializan las variables y se asignan los listeners
     *
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return la vista creada
     */
    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.frg_detalle_equipo, container, false);
        aplicacion = (Aplicacion) getActivity().getApplication();
        dbRevisiones = new DBRevisiones(aplicacion);
        this.revisionActual = Aplicacion.revisionActual;
        this.equipoActual = Aplicacion.equipoActual;
        this.tramoActual = Aplicacion.tramoActual;

        // Se asignan las variables a su View correspondiente
        tvDatoInstalacion = (TextView) v.findViewById(R.id.TVDatoInstalacion);
        tvDatoTramo = (TextView) v.findViewById(R.id.TVDatoTramo);
        tvDatoTerritorio = (TextView) v.findViewById(R.id.TVDatoTerritorio);
        tvFechaRevision = (TextView) v.findViewById(R.id.TVDatoFecha);
        tvDatoLatitud = (TextView) v.findViewById(R.id.TVDatoLatitud);
        tvDatoLongitud = (TextView) v.findViewById(R.id.TVDatoLongitud);
        spTipo = (Spinner) v.findViewById(R.id.SpinnerTipoEquipo);
        etObservaciones = (EditText) v.findViewById(R.id.etObservacionesEquipo);

        lNumApoyo = (LinearLayout) v.findViewById(R.id.lNumApoyo);
        etNumApoyo = (EditText) v.findViewById(R.id.etNumApoyo);
        lMaterial = (LinearLayout) v.findViewById(R.id.lMaterial);
        spMaterial = (Spinner) v.findViewById(R.id.SpinnerMaterial);
        lCruces = (LinearLayout) v.findViewById(R.id.lCruces);
        rgCruces = (RadioGroup) v.findViewById(R.id.rgCruces);
        hayCruces = "";
        lManiobra = (LinearLayout) v.findViewById(R.id.lManiobra);
        rgManiobra = (RadioGroup) v.findViewById(R.id.rgManiobra);
        hayManiobra = "";
        lConversion = (LinearLayout) v.findViewById(R.id.lConversion);
        rgConversion = (RadioGroup) v.findViewById(R.id.rgConversion);
        hayConversion = "";

        botonAlternarEquipo = (ImageButton) v.findViewById(R.id.botonAlternarEquipo);
        botonCoord = (ImageButton) v.findViewById(R.id.botonCoordenadas);
        botonFoto = (ImageButton) v.findViewById(R.id.botonFotosEquipo);
        botonNoRevisables = (ImageButton) v.findViewById(R.id.botonEquipoNoRevisable);
        tvMostrarFoto = (TextView) v.findViewById(R.id.TVMostrarFoto);
        botonInspeccion = (Button) v.findViewById(R.id.botonInspeccionEquipo);

        ArrayAdapter spMaterialAdapter = new ArrayAdapter(getContext(), R.layout.spinner_detalle_equipo,
                getResources().getTextArray(R.array.spinner_material));
        spMaterialAdapter.setDropDownViewResource(R.layout.spinner_dropdown);
        spMaterial.setAdapter(spMaterialAdapter);

        ArrayAdapter spTipoAdapter = new ArrayAdapter(getContext(), R.layout.spinner_detalle_equipo,
                getResources().getTextArray(R.array.spinner_tipo_equipo));
        spTipoAdapter.setDropDownViewResource(R.layout.spinner_dropdown);
        spTipo.setAdapter(spTipoAdapter);

        // Asignacion de listeners
        tvFechaRevision.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Se recupera la fecha guardada
                Cursor cursor = dbRevisiones.solicitarItem(DBRevisiones.TABLA_EQUIPOS,
                        "FechaInspeccion", "NombreEquipo", Aplicacion.equipoActual);
                cursor.moveToFirst();
                String fechaGuardada = cursor.getString(0);

                int mYear;
                int mMonth;
                int mDay;

                if (!fechaGuardada.equals("")) {
                    mYear = Integer.parseInt(fechaGuardada.substring(fechaGuardada.lastIndexOf("/") + 1)); // Año guardado
                    mMonth = Integer.parseInt(fechaGuardada.substring(
                            (fechaGuardada.indexOf("/") + 1), fechaGuardada.lastIndexOf("/")));; // Mes guardado
                    mMonth = mMonth - 1; // El spinner del mes es zero-index
                    mDay = Integer.parseInt(fechaGuardada.substring(0, fechaGuardada.indexOf("/"))); // Dia guardado
                } else {
                    Calendar c = Calendar.getInstance();
                    mYear = c.get(Calendar.YEAR); // Año actual
                    mMonth = c.get(Calendar.MONTH); // Mes actual
                    mDay = c.get(Calendar.DAY_OF_MONTH); // Día actual
                }

                // date picker dialog
                DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(),
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                String fechaSeleccionada = dayOfMonth + "/" + (monthOfYear + 1) + "/" + year;
                                dbRevisiones.actualizarItemEquipoApoyo(DBRevisiones.TABLA_EQUIPOS, "FechaInspeccion",
                                            fechaSeleccionada, revisionActual, equipoActual, tramoActual);
                                tvFechaRevision.setText(fechaSeleccionada);

                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });

        spTipo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                textoTipo = parent.getItemAtPosition(position).toString();
                switch (textoTipo){
                    case Aplicacion.LAMT:
                        dbRevisiones.actualizarItemEquipoApoyo(DBRevisiones.TABLA_EQUIPOS, "TipoEquipo",
                                Aplicacion.LAMT, revisionActual, equipoActual, tramoActual);
                        dbRevisiones.actualizarItemEquipoApoyo(DBRevisiones.TABLA_EQUIPOS, "TipoInstalacion",
                                "L", revisionActual, equipoActual, tramoActual);
                        break;
                    case Aplicacion.CT:
                        dbRevisiones.actualizarItemEquipoApoyo(DBRevisiones.TABLA_EQUIPOS, "TipoEquipo",
                                Aplicacion.CT, revisionActual, equipoActual, tramoActual);
                        dbRevisiones.actualizarItemEquipoApoyo(DBRevisiones.TABLA_EQUIPOS, "TipoInstalacion",
                                "Z", revisionActual, equipoActual, tramoActual);
                        break;
                    case Aplicacion.PT:
                        dbRevisiones.actualizarItemEquipoApoyo(DBRevisiones.TABLA_EQUIPOS, "TipoEquipo", Aplicacion.PT,
                                revisionActual, equipoActual, tramoActual);
                        dbRevisiones.actualizarItemEquipoApoyo(DBRevisiones.TABLA_EQUIPOS, "TipoInstalacion", "Z",
                                revisionActual, equipoActual, tramoActual);
                        break;
                    default:
                        break;
                }
                mostrarDatosAlmacenados();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                textoTipo = "";
            }
        });

        etObservaciones.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    String texto;
                    String obs = etObservaciones.getText().toString();
                    String num = etNumApoyo.getText().toString();
                    if (num.equals("")) {
                        texto = obs;
                    } else if (obs.equals("")) {
                        texto = "Num. apoyo:" + num;
                    } else {
                        texto = "Num. apoyo:" + num + "-" + etObservaciones.getText().toString();
                    }
                    dbRevisiones.actualizarItemEquipoApoyo(DBRevisiones.TABLA_EQUIPOS,
                                                                    "Observaciones", texto, revisionActual,
                                                                    equipoActual, tramoActual);
                    dbRevisiones.actualizarItemEquipoApoyo(DBRevisiones.TABLA_APOYOS,
                                                                    "Observaciones", texto, revisionActual,
                                                                    equipoActual, tramoActual);

                }
            }
        });

        etNumApoyo.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    String texto;
                    String obs = etObservaciones.getText().toString();
                    String num = etNumApoyo.getText().toString();
                    if (num.equals("")) {
                        texto = obs;
                    } else if (obs.equals("")) {
                        texto = "Num. apoyo:" + num;
                    } else {
                        texto = "Num. apoyo:" + num + "-" + etObservaciones.getText().toString();
                    }
                    dbRevisiones.actualizarItemEquipoApoyo(DBRevisiones.TABLA_EQUIPOS, "Observaciones",
                                                                    texto, revisionActual, equipoActual, tramoActual);
                    dbRevisiones.actualizarItemEquipoApoyo(DBRevisiones.TABLA_APOYOS,
                            "Observaciones", texto, revisionActual,
                            equipoActual, tramoActual);

                }
            }
        });

        spMaterial.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                textoMaterial = parent.getItemAtPosition(position).toString();
                dbRevisiones.actualizarItemEquipoApoyo(DBRevisiones.TABLA_APOYOS, "Material", textoMaterial,
                        revisionActual, equipoActual, tramoActual);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                textoMaterial = "";
            }
        });

        rgCruces.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                switch (checkedId) {
                    case R.id.radioCrucesSi:
                        hayCruces = Aplicacion.SI;
                        dbRevisiones.actualizarItemEquipoApoyo(DBRevisiones.TABLA_EQUIPOS, "HayCruces", hayCruces,
                                revisionActual, equipoActual, tramoActual);
                        break;
                    case R.id.radioCrucesNo:
                        hayCruces = Aplicacion.NO;
                        dbRevisiones.actualizarItemEquipoApoyo(DBRevisiones.TABLA_EQUIPOS, "HayCruces", hayCruces,
                                revisionActual, equipoActual, tramoActual);
                        break;
                    default:
                        break;
                }
            }
        });
        rgManiobra.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                switch (checkedId) {
                    case R.id.radioManiobraSi:
                        hayManiobra = Aplicacion.SI;
                        dbRevisiones.actualizarItemEquipoApoyo(DBRevisiones.TABLA_EQUIPOS, "HayManiobra", hayManiobra,
                                revisionActual, equipoActual, tramoActual);
                        break;
                    case R.id.radioManiobraNo:
                        hayManiobra = Aplicacion.NO;
                        dbRevisiones.actualizarItemEquipoApoyo(DBRevisiones.TABLA_EQUIPOS, "HayManiobra", hayManiobra,
                                revisionActual, equipoActual, tramoActual);
                        break;
                    default:
                        break;
                }
            }
        });
        rgConversion.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                switch (checkedId) {
                    case R.id.radioConversionSi:
                        hayConversion = Aplicacion.SI;
                        dbRevisiones.actualizarItemEquipoApoyo(DBRevisiones.TABLA_EQUIPOS, "HayConversion", hayConversion,
                                revisionActual, equipoActual, tramoActual);
                        break;
                    case R.id.radioConversionNo:
                        hayConversion = Aplicacion.NO;
                        dbRevisiones.actualizarItemEquipoApoyo(DBRevisiones.TABLA_EQUIPOS, "HayConversion", hayConversion,
                                revisionActual, equipoActual, tramoActual);
                        break;
                    default:
                        break;
                }
            }
        });
        tvMostrarFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (hayFoto(1)) {
                    mostrarFotos();
                }
            }
        });

        botonAlternarEquipo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alternarEquipo();
            }
        });
        botonCoord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tomarCoordenadas();
            }
        });
        botonFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tomarFoto();
            }
        });
        if (dbRevisiones.marcadoNoRevisable(revisionActual, equipoActual, tramoActual)) {
            ponerNoRevisable();
        } else {
            ponerSiRevisable();
        }
        botonInspeccion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iniciarInspeccion();
            }
        });
        return v;
    }

    /**
     * Cada vez que la vista pasa a primer plano se refresca el fragment para actualizar los datos
     * que se muestran ya que hay valores que pueden modificar sus caracteristicas en funcion de los
     * valores modificados
     */
    @Override
    public void onResume() {
        super.onResume();
        mostrarDatosAlmacenados();
    }

    /**
     * Se muestran los valores almacenados
     */
    public void mostrarDatosAlmacenados() {
        Equipo equipo = dbRevisiones.solicitarEquipo(revisionActual, equipoActual, tramoActual);
        Apoyo apoyo = dbRevisiones.solicitarApoyo(revisionActual, equipoActual, tramoActual);

        if (equipo != null && apoyo!= null) {
            tvDatoInstalacion.setText(equipo.getCodigoBDE());
            tvDatoTramo.setText(equipo.getCodigoTramo());

            // Se actualiza la fecha con la fecha de la BD. Si no hay fecha guardada se actualiza con la fecha actual
            String fecha = equipo.getFechaInspeccion();
            if (fecha.equals("")) {
                Calendar c = Calendar.getInstance();
                int year = c.get(Calendar.YEAR); // Año actual
                int month = c.get(Calendar.MONTH); // Mes actual
                int day = c.get(Calendar.DAY_OF_MONTH); // Día actual
                fecha = day + "/" + (month + 1) + "/" + year;
            }
            tvFechaRevision.setText(fecha);

            // Se actualizan las observaciones y el numero de apoyo
            String obs = equipo.getObservaciones();
            if (obs.equals("")) {
                etObservaciones.setText("");
                etNumApoyo.setText("");
            } else if (obs.startsWith("Num. apoyo:")){
                String num;
                if (obs.contains("-")) {
                    etObservaciones.setText(obs.substring(obs.indexOf("-") + 1));
                    num = obs.substring((obs.indexOf(":") + 1), obs.indexOf("-"));
                } else {
                    num = obs.substring((obs.indexOf(":") + 1));
                }
                etNumApoyo.setText(num);
            } else {
                etObservaciones.setText(obs);
                etNumApoyo.setText("");
            }

            // Se actualiza el tipo de equipo en función de si es CT o LAMT
            switch (equipo.getTipoEquipo()) {
                case Aplicacion.LAMT:
                    spTipo.setSelection(0);
                    lNumApoyo.setVisibility(View.VISIBLE);
                    lMaterial.setVisibility(View.VISIBLE);
                    lCruces.setVisibility(View.VISIBLE);
                    lManiobra.setVisibility(View.VISIBLE);
                    lConversion.setVisibility(View.VISIBLE);
                    if (equipo.getHayCruces().equals(Aplicacion.SI)) {
                        rgCruces.check(R.id.radioCrucesSi);
                    }
                    if (equipo.getHayManiobra().equals(Aplicacion.SI)) {
                        rgManiobra.check(R.id.radioManiobraSi);
                    }
                    if (equipo.getHayConversion().equals(Aplicacion.SI)) {
                        rgConversion.check(R.id.radioConversionSi);
                    }
                    break;
                case Aplicacion.CT:
                    lNumApoyo.setVisibility(View.INVISIBLE);
                    lMaterial.setVisibility(View.INVISIBLE);
                    lCruces.setVisibility(View.INVISIBLE);
                    lManiobra.setVisibility(View.INVISIBLE);
                    lConversion.setVisibility(View.INVISIBLE);
                    spTipo.setSelection(1);
                    spTipo.setEnabled(true);
                    break;
                case Aplicacion.PT:
                    lNumApoyo.setVisibility(View.INVISIBLE);
                    lMaterial.setVisibility(View.INVISIBLE);
                    lCruces.setVisibility(View.INVISIBLE);
                    lManiobra.setVisibility(View.INVISIBLE);
                    lConversion.setVisibility(View.INVISIBLE);
                    spTipo.setSelection(2);
                    spTipo.setEnabled(true);
                    break;
                default:
                    switch (equipo.getTipoInstalcion()) {
                        case "L":
                            spTipo.setSelection(0);
                            //spTipo.setEnabled(false);
                            dbRevisiones.actualizarItemEquipoApoyo(DBRevisiones.TABLA_EQUIPOS, "TipoEquipo",
                                    Aplicacion.LAMT, revisionActual, equipoActual, tramoActual);
                            if (equipo.getHayCruces().equals(Aplicacion.SI)) {
                                rgCruces.check(R.id.radioCrucesSi);
                            }
                            if (equipo.getHayManiobra().equals(Aplicacion.SI)) {
                                rgManiobra.check(R.id.radioManiobraSi);
                            }
                            if (equipo.getHayConversion().equals(Aplicacion.SI)) {
                                rgConversion.check(R.id.radioConversionSi);
                            }
                            break;
                        case "Z":
                            lNumApoyo.setVisibility(View.INVISIBLE);
                            lMaterial.setVisibility(View.INVISIBLE);
                            lCruces.setVisibility(View.INVISIBLE);
                            lManiobra.setVisibility(View.INVISIBLE);
                            lConversion.setVisibility(View.INVISIBLE);
                            spTipo.setSelection(1);
                            spTipo.setEnabled(true);
                            dbRevisiones.actualizarItemEquipoApoyo(DBRevisiones.TABLA_EQUIPOS, "TipoEquipo",
                                    Aplicacion.CT, revisionActual, equipoActual, tramoActual);
                            break;
                        default:
                            break;
                    }
                    break;
            }

            // Se actualizan las coordenadas
            tvDatoLatitud.setText(apoyo.getLatitud());
            tvDatoLongitud.setText(apoyo.getLongitud());
            // Se selecciona el item correspondiente en funcion del material del apoyo
            String material = apoyo.getMaterial();
            if (material.toLowerCase().contains("hor")) {
                spMaterial.setSelection(0);
            } else if (material.toLowerCase().contains("mad")) {
                spMaterial.setSelection(1);
            } else if (material.toLowerCase().contains("ace")) {
                spMaterial.setSelection(2);
            } else if (material.toLowerCase().contains("met")) {
                spMaterial.setSelection(3);
            } else if (material.toLowerCase().contains("fib")) {
                spMaterial.setSelection(4);
            } else if (material.toLowerCase().contains("otr")) {
                spMaterial.setSelection(5);
            } else {
                spMaterial.setSelection(6);
            }

            // Se actualiza la visualización del dato "Tiene cruces"
            if (equipo.getHayCruces().equals(Aplicacion.SI)) {
                rgCruces.check(R.id.radioCrucesSi);
                hayCruces = Aplicacion.SI;
            } else {
                rgCruces.check(R.id.radioCrucesNo);
                hayCruces = Aplicacion.NO;
                if (!equipo.getHayCruces().equals((Aplicacion.NO))) {
                    dbRevisiones.actualizarItemEquipoApoyo(DBRevisiones.TABLA_EQUIPOS, "HayCruces", hayCruces,
                            revisionActual, equipoActual, tramoActual);
                }
            }
            // Se actualiza la visualización del dato "Hay maniobra"
            if (equipo.getHayManiobra().equals(Aplicacion.SI)) {
                rgManiobra.check(R.id.radioManiobraSi);
                hayManiobra = Aplicacion.SI;
                dbRevisiones.actualizarItemEquipoApoyo(DBRevisiones.TABLA_EQUIPOS, "HayManiobra", hayManiobra,
                        revisionActual, equipoActual, tramoActual);
            } else {
                rgManiobra.check(R.id.radioManiobraNo);
                hayManiobra = Aplicacion.NO;
                if (!equipo.getHayManiobra().equals((Aplicacion.NO))) {
                    dbRevisiones.actualizarItemEquipoApoyo(DBRevisiones.TABLA_EQUIPOS, "HayManiobra", hayManiobra,
                            revisionActual, equipoActual, tramoActual);
                }
            }
            // Se actualiza la visualización del dato "Hay conversion"
            if (equipo.getHayConversion().equals(Aplicacion.SI)) {
                rgConversion.check(R.id.radioConversionSi);
                hayConversion = Aplicacion.SI;
                dbRevisiones.actualizarItemEquipoApoyo(DBRevisiones.TABLA_EQUIPOS, "HayConversion", hayConversion,
                        revisionActual, equipoActual, tramoActual);
            } else {
                rgConversion.check(R.id.radioConversionNo);
                hayConversion = Aplicacion.NO;
                if (!equipo.getHayConversion().equals((Aplicacion.NO))) {
                    dbRevisiones.actualizarItemEquipoApoyo(DBRevisiones.TABLA_EQUIPOS, "HayConversion", hayConversion,
                            revisionActual, equipoActual, tramoActual);
                }
            }
            // Se actualiza el texto para mostrar las fotos
            if (hayFoto(1)) {
                activarMostrarFoto();
            } else {
                desactivarMostrarFoto();
            }

            // Se actualiza el boton NoRevisable
            if (dbRevisiones.marcadoNoRevisable(revisionActual, equipoActual, tramoActual)) {
                ponerNoRevisable();
            } else {
                ponerSiRevisable();
            }
        }

    }

    /**
     * Se determina si hay más equipos con el mismo nombre. Si es así se dará la opción (mediante el
     * DialogoAlternarEquipo) de elegir entre los equipos con diferentes revisiones o tramos
     */
    public void alternarEquipo () {

        if (dbRevisiones.numEquiposIguales(equipoActual) > 1) {
            abrirDialogoAlternarEquipo();
        } else {
            Aplicacion.print("No hay más equipos con el mismo nombre");
        }

    }

    /**
     * Se iniciara la inspeccion (abriendo la actividad MostrarDefectos) si se han tomado las coordenadas
     * y al menos una foto
     */
    public void iniciarInspeccion () {
        if (!dbRevisiones.marcadoNoRevisable(revisionActual, equipoActual, tramoActual)) {
            if (hayFoto(1)) {
                if (hayCoordenadas()) {
                    // Si la revisión del equipo aún no está inicializada se actualiza el estado
                    Equipo equipo = dbRevisiones.solicitarEquipo(revisionActual, equipoActual, tramoActual);
                    if (equipo.getEstado().equals(Aplicacion.ESTADO_PENDIENTE)) {
                        dbRevisiones.actualizarItemEquipoApoyo(DBRevisiones.TABLA_EQUIPOS, "Estado",
                                Aplicacion.ESTADO_EN_CURSO, revisionActual, equipoActual, tramoActual);
                    }
                    // Se actualiza el tramo si no se había recogido aún
                    if (equipo.getPosicionTramo().equals("")) {
                        String tramo = equipo.getCodigoTramo();
                        try {
                            tramo = tramo.substring((tramo.indexOf("-") + 1), tramo.lastIndexOf("-"));
                            dbRevisiones.actualizarItemEquipoApoyo(DBRevisiones.TABLA_EQUIPOS, "PosicionTramo",
                                    tramo, revisionActual, equipoActual, tramoActual);
                        } catch (Exception e) {}
                    }
                    // Se actualiza la fecha de la revisión del equipo
                    dbRevisiones.actualizarItemEquipoApoyo(DBRevisiones.TABLA_EQUIPOS, "FechaInspeccion",
                            tvFechaRevision.getText().toString(), revisionActual, equipoActual, tramoActual);
                    // Se actualiza el número de trabajo del equipo
                    Revision rev = dbRevisiones.solicitarRevision(Aplicacion.revisionActual);
                    dbRevisiones.actualizarItemEquipoApoyo(DBRevisiones.TABLA_EQUIPOS, "TrabajoInspeccion",
                            rev.getNumTrabajo(), revisionActual, equipoActual, tramoActual);
                    Aplicacion.tipoActual = textoTipo;
                    Intent intent = new Intent(getContext(), MostrarDefectos.class);
                    startActivity(intent);
                    getActivity().finish();
                } else {
                    Aplicacion.print("Debes tomar las coordenadas");
                }
            } else {
                Aplicacion.print("Debes tomar foto del equipo");
            }
        } else {
            Aplicacion.print(("El equipo está marcado como no revisable"));
        }
    }

    public void abrirDialogoAlternarEquipo () {
        FragmentManager fragmentManager = getFragmentManager();
        DialogoAlternarEquipo dialogoAlternarEquipo = new DialogoAlternarEquipo();
        dialogoAlternarEquipo.show(fragmentManager, "TAG");

    }

    /**
     * Se confirma si se ha tomado foto del equipo
     *
     * @param numFoto numero de foto a comprobar si existe (1-para comprobar si hay alguna foto,
     *                2-para comprobar si hay una segunda foto guardada)
     * @return si existe la foto solicitada
     */
    public boolean hayFoto (int numFoto) {
        boolean hayFoto = false;
        Cursor cursor;

        if (numFoto == 1) {
            cursor = dbRevisiones.solicitarFotoEquipo(DBRevisiones.TABLA_EQUIPOS, "DocumentosAsociar",
                                                        revisionActual, equipoActual, tramoActual);
        } else {
            cursor = dbRevisiones.solicitarFotoEquipo(DBRevisiones.TABLA_EQUIPOS, "DescripcionDocumentos",
                                                        revisionActual, equipoActual, tramoActual);
        }

        cursor.moveToFirst();
        String foto = cursor.getString(0);
        if (!foto.equals("")) {
            hayFoto = true;
        }
        cursor.close();

        return hayFoto;
    }

    /**
     * Comprueba si se han tomado coordenadas del equipo
     * @return si se han tomado coordenadas
     */
    public boolean hayCoordenadas () {
        boolean hayCoordenadas = false;
        if (!tvDatoLatitud.getText().equals("") || !tvDatoLongitud.getText().equals("")){
            hayCoordenadas = true;
        }

        return hayCoordenadas;
    }

    /**
     * Abre la actividad TomarCoordenadas para recoger las coordenadas del equipo
     */
    public void tomarCoordenadas() {
        Intent intent = new Intent(getContext(), TomarCoordenadas.class);
        intent.putExtra(Aplicacion.TIPO, Aplicacion.EQUIPO);
        startActivity(intent);
        getActivity().finish();
    }

    /**
     * Abre la actividad de la camara para realizar la foto
     */
    public void tomarFoto() {

        Intent tomarFotoIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Se confirma que existe alguna app que utilice la camera para manejar el intent
        if (tomarFotoIntent.resolveActivity(aplicacion.getPackageManager()) != null) {
            // Se genera el fichero donde se guardará la foto
            fotoFile = null;
            try {
                fotoFile = Aplicacion.crearArchivoImagen();
            } catch (IOException ex) {
                // Si hay error al generar el archivo
                Aplicacion.print("Error al generar el archivo");
            }
            // Se abre la actividad de la camara para realizar la foto
            if (fotoFile != null) {
                uriFoto = Uri.fromFile(fotoFile);
                tomarFotoIntent.putExtra(MediaStore.EXTRA_OUTPUT, uriFoto);
                startActivityForResult(tomarFotoIntent, RESULTADO_FOTO);
            }
        }
    }

    /**
     * Se captura el resultado de tomar la foto y si es ok, se guarda el nombre del archivo de la foto en la BD
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        String s = Integer.toString(requestCode);
        s = Integer.toString(resultCode);

        if (requestCode == RESULTADO_FOTO && resultCode == Activity.RESULT_OK
                && uriFoto != null) {
            String nombreFoto = fotoFile.getName();
            if (!hayFoto(1)) {
                dbRevisiones.actualizarFotoEquipo(revisionActual, equipoActual, tramoActual, "DocumentosAsociar", nombreFoto);
            } else if (!hayFoto(2)) {
                dbRevisiones.actualizarFotoEquipo(revisionActual, equipoActual, tramoActual, "DescripcionDocumentos", nombreFoto);
            } else {
                Aplicacion.print("La foto se ha guardado pero no está asociada en la base datos");
            }
            //Aplicacion.print(nombreFoto);
        } else if (resultCode == Activity.RESULT_CANCELED && uriFoto != null) {
            // Si no se hace la foto, se borra el archivo generado
            File f = new File(uriFoto.getPath());
            f.delete();
        }

    }

    /**
     * Abre la actividad que mostrara las fotos almacenadas
     */
    public void mostrarFotos() {
        Intent intent = new Intent(getContext(), FotosEquipo.class);
        startActivity(intent);
    }

    /**
     * Activa el texto que permitira mostrar las fotos al clickarlo
     */
    public void activarMostrarFoto() {
        tvMostrarFoto.setText(R.string.mostrar_fotos);
        tvMostrarFoto.setTextColor(getResources().getColor(R.color.texto_clickable));

    }

    /**
     * Desactiva el texto que permitira mostrar las fotos al clickarlo
     */
    public void desactivarMostrarFoto() {
        tvMostrarFoto.setText(R.string.no_hay_fotos);
        tvMostrarFoto.setTextColor(getResources().getColor(R.color.texto));

    }



    /**
     * Metodo que lanzara dialogo_no_revisable, recogera el motivo y marcara un equipo como "No Revisable"
     */
    public void marcarNoRevisable() {
        if (!dbRevisiones.marcadoNoRevisable(revisionActual, equipoActual, tramoActual)) {
            LayoutInflater li = LayoutInflater.from(getContext());
            View vista = li.inflate(R.layout.dialogo_no_revisable, null);
            TextView tituloMotivo = (TextView) vista.findViewById(R.id.tvMotivo);
            tituloMotivo.setText(getResources().getString(R.string.texto_motivo) + " " + Aplicacion.equipoActual);

            AlertDialog.Builder prompt = new AlertDialog.Builder(getContext());
            prompt.setView(vista);

            final EditText etMotivo = (EditText) vista.findViewById(R.id.etMotivo);
            prompt.setCancelable(false)
                    .setPositiveButton(R.string.aceptar, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            String motivo = etMotivo.getText().toString();
                            if (!motivo.equals("")) {
                                confirmarNoRevision(motivo);
                            } else {
                                Aplicacion.print("No se ha marcado el equipo como no revisable " +
                                        "al no haber introducido ningún motivo");
                                marcarNoRevisable();
                            }
                        }
                    })
                    .setNegativeButton(R.string.cancelar, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
            AlertDialog dialogo = prompt.create();
            dialogo.show();
        } else {
            Aplicacion.print("El equipo ya ha sido marcado como no revisable");
        }
    }

    /**
     * Crea y muestra el dialogo de confirmacion de equipo no revisable
     *
     * @param motivo Texto que solicita el motivo de la no_revision
     */
    public void confirmarNoRevision(final String motivo) {
        LayoutInflater li = LayoutInflater.from(getContext());
        View vista = li.inflate(R.layout.dialogo_conf_no_revisable, null);

        AlertDialog.Builder dialogo = new AlertDialog.Builder(getContext());
        dialogo.setView(vista);


        dialogo.setPositiveButton(R.string.aceptar, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Se incluye el apoyo en la lista de no revisables
                dbRevisiones.incluirApoyoNoRevisable(Aplicacion.revisionActual,
                        equipoActual, motivo, tramoActual);
                // Se actualiza el estado del apoyo
                dbRevisiones.actualizarItemEquipoApoyo(DBRevisiones.TABLA_EQUIPOS, "Estado",
                        Aplicacion.ESTADO_FINALIZADA, revisionActual, equipoActual, tramoActual);
                // Se borra la fecha de revisión si la hubiera
                dbRevisiones.actualizarItemEquipoApoyo(DBRevisiones.TABLA_EQUIPOS, "FechaInspeccion", "",
                        revisionActual, equipoActual, tramoActual);
                // Se borran los posibles defectos que pudiera haber asociados al equipo
                dbRevisiones.borrarDefectosEquipo(revisionActual, equipoActual, tramoActual);
                refrescarActividad();
            }
        });
        dialogo.setNegativeButton(R.string.cancelar, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        dialogo.setCancelable(false);
        dialogo.show();

    }

    /**
     * Modifica el color y el listener del boton para que el equipo sea NoRevisable
     */
    public void ponerSiRevisable () {
        botonNoRevisables.setBackgroundColor(getResources().getColor(R.color.rojo));
        botonNoRevisables.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                marcarNoRevisable();
            }
        });

    }

    /**
     * Modifica el color y el listener del boton para que el equipo vuelva a ser revisable
     */
    public void ponerNoRevisable () {
        botonNoRevisables.setBackgroundColor(getResources().getColor(R.color.verde));
        botonNoRevisables.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                marcarRevisable();
            }
        });

    }

    /**
     * Modifica la BDD para que el equipo pueda volver a ser revisable
     */
    public void marcarRevisable () {
        AlertDialog.Builder dialog = new AlertDialog.Builder(getContext());

        dialog.setTitle(Aplicacion.equipoActual)
                .setMessage(R.string.texto_revisable)
                .setPositiveButton(R.string.aceptar, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dbRevisiones.borrarNoRevisable(revisionActual,
                                equipoActual, tramoActual);
                        dbRevisiones.actualizarItemEquipoApoyo(DBRevisiones.TABLA_EQUIPOS, "Estado",
                                Aplicacion.ESTADO_PENDIENTE, revisionActual, equipoActual, tramoActual);
                        refrescarActividad();

                    }
                })
                .setNegativeButton(R.string.cancelar, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        dialog.create().show();

    }

    /**
     * Se vuelve a cargar la actividad para actualizar los fragments
     */
    public void refrescarActividad () {
        Intent intent = new Intent(getContext(), MostrarEquipos.class);
        startActivity(intent);
        getActivity().finish();
    }

}