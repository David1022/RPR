package com.nipsa.rpr;

import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.util.Vector;

public class FrgDetalleDefectos extends Fragment {

    private FrgDetalleDefectos.DefectoListener listener;
    private String equipoActual, tramoActual;
    private ListView lstListado;
    private DBRevisiones dbRevisiones;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {

        this.equipoActual = Aplicacion.equipoActual;
        this.tramoActual = Aplicacion.tramoActual;

        return inflater.inflate(R.layout.frg_detalle_defectos, container, false);
    }

    @Override
    public void onActivityCreated(Bundle state) {
        super.onActivityCreated(state);

        lstListado = (ListView)getView().findViewById(R.id.listadoDetalleDefectos);
        lstListado.setAdapter(new FrgDetalleDefectos.AdaptadorDefectos(this));
        lstListado.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> list, View view, int pos, long id) {
                if (listener!=null) {
                    listener.onDefectoSeleccionado(
                            (ListaDef) lstListado.getAdapter().getItem(pos));
                }
            }
        });
        dbRevisiones = new DBRevisiones(this.getActivity());
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    public void abrirDatosDefecto (String defecto) {
        Aplicacion.defectoActual = defecto;
        Intent intent = new Intent(getActivity(), DatosDefecto.class);
        startActivity(intent);
        getActivity().finish();
    }

    public interface DefectoListener {
        void onDefectoSeleccionado(ListaDef listaDef);
    }

    public void setDefectoListener(FrgDetalleDefectos.DefectoListener listener) {
        this.listener = listener;
    }

    class AdaptadorDefectos extends ArrayAdapter<ListaDef> {

        Activity context;
        RadioButton rbSi, rbNo;
        TextView textCodigo, textDescripcion;

        AdaptadorDefectos(Fragment context) {
            super(context.getActivity(), R.layout.listitem_detalle_defectos, MostrarDefectos.listaAMostrar);
            this.context = context.getActivity();
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            View item = convertView;

            //if (item == null) {
                LayoutInflater inflater = context.getLayoutInflater();
                item = inflater.inflate(R.layout.listitem_detalle_defectos, null);
            //}

            final ListaDef listaDef = MostrarDefectos.listaAMostrar.get(position);
            final Defecto defecto = dbRevisiones.solicitarDefecto(equipoActual, listaDef.getCodigoEndesa2012(), tramoActual);
            final String codigo = listaDef.getCodigoEndesa2012();

            RadioGroup rg = (RadioGroup)item.findViewById(R.id.rgDefectos);
            rbSi = (RadioButton) item.findViewById(R.id.radioDefectosSi);
            rbNo = (RadioButton) item.findViewById(R.id.radioDefectosNo);
            textCodigo = (TextView)item.findViewById(R.id.itemCodigoDefectos);
            textDescripcion = (TextView)item.findViewById(R.id.itemDescripcionDefectos);

            rg.clearCheck(); // Por defecto no habrá ninguna opción seleccionada
            if (defecto != null) {
                String def;
                def = defecto.getEsDefecto();
                if (def.equals(Aplicacion.SI)) {
                    rg.check(R.id.radioDefectosSi);
                    mostrarTextoEnRojo();
                } else if (def.equals(Aplicacion.NO)){
                    rg.check(R.id.radioDefectosNo);
                    mostrarTextoEnNegro();
                }
            }
            rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                    String codigo = listaDef.getCodigoEndesa2012();
                    switch (checkedId){
                        case R.id.radioDefectosSi:
                            mostrarTextoEnRojo();
                            if (defecto == null) {
                                dbRevisiones.incluirDefecto(Aplicacion.revisionActual, Aplicacion.equipoActual,
                                                                codigo, Aplicacion.tramoActual);
                            }
                            dbRevisiones.actualizarItemDefecto(codigo, "EsDefecto", Aplicacion.SI);
                            Aplicacion.defectoActual = codigo;
                            Intent intent = new Intent(getActivity(), DatosDefecto.class);
                            startActivity(intent);
                            getActivity().finish();
                            break;
                        case R.id.radioDefectosNo:
                            mostrarTextoEnNegro();
                            if (defecto == null) {
                                dbRevisiones.incluirDefecto(Aplicacion.revisionActual, Aplicacion.equipoActual,
                                                                codigo, Aplicacion.tramoActual);
                            }
                            dbRevisiones.actualizarItemDefecto(Aplicacion.defectoActual, "Corregido", Aplicacion.NO);
                            dbRevisiones.actualizarItemDefecto(Aplicacion.defectoActual, "FechaCorreccion", "");
                            dbRevisiones.actualizarItemDefecto(codigo, "EsDefecto", Aplicacion.NO);
                            break;
                        default:
                            break;
                    }
                }
            });

            textCodigo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (defecto == null) {
                        dbRevisiones.incluirDefecto(Aplicacion.revisionActual, Aplicacion.equipoActual,
                                                        codigo, Aplicacion.tramoActual);
                        dbRevisiones.actualizarItemDefecto(codigo, "EsDefecto", Aplicacion.NO);
                    }
                    abrirDatosDefecto(codigo);
                }
            });
            textDescripcion.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (defecto == null) {
                        dbRevisiones.incluirDefecto(Aplicacion.revisionActual, Aplicacion.equipoActual,
                                                        codigo, Aplicacion.tramoActual);
                        dbRevisiones.actualizarItemDefecto(codigo, "EsDefecto", Aplicacion.NO);
                    }
                    abrirDatosDefecto(codigo);
                }
            });

            textCodigo.setText(listaDef.getCodigoEndesa2012());
            textDescripcion.setText(listaDef.getDescripcionEndesa());

            return(item);
        }

        /**
         * Pone el texto del item en rojo al estar activado como defecto
         */
        private void mostrarTextoEnRojo () {
            rbSi.setTextColor(getResources().getColor(R.color.texto_defecto));
            rbNo.setTextColor(getResources().getColor(R.color.texto_defecto));
            textCodigo.setTextColor(getResources().getColor(R.color.texto_defecto));
            textDescripcion.setTextColor(getResources().getColor(R.color.texto_defecto));
        }

        /**
         * Pone el texto del item en negro al estar desactivado como defecto
         */
        private void mostrarTextoEnNegro () {
            rbSi.setTextColor(getResources().getColor(R.color.texto));
            rbNo.setTextColor(getResources().getColor(R.color.texto));
            textCodigo.setTextColor(getResources().getColor(R.color.texto));
            textDescripcion.setTextColor(getResources().getColor(R.color.texto));
        }
    }

}