package com.nipsa.rpr;

import android.app.Activity;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.Vector;

public class FrgListadoEquipos extends Fragment {

    private FrgListadoEquipos.EquipoListener listener;
    private ListView lstListado;
    private Vector<Equipo> listaAMostrar;
    private DBRevisiones dbRevisiones;
    private int mSelected;
    private AdaptadorEquipos mAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.frg_listado_equipos, container, false);
        dbRevisiones = new DBRevisiones(getContext());
        listaAMostrar = dbRevisiones.solicitarListaEquipos(Aplicacion.revisionActual);
        mSelected = -1;

        return v;

    }

    @Override
    public void onActivityCreated(Bundle state) {
        super.onActivityCreated(state);

        lstListado = (ListView)getView().findViewById(R.id.listadoEquipos);
        mAdapter = new AdaptadorEquipos(this);
        lstListado.setAdapter(mAdapter);
        lstListado.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> list, View view, int pos, long id) {
                if (listener!=null) {
                    mSelected = pos;
                    mAdapter.notifyDataSetChanged();
                    listener.onEquipoSeleccionado((Equipo) lstListado.getAdapter().getItem(pos));
                }
            }
        });

        // Si hay revision actual se busca y para cambiarle el color del background y ponerla en primer lugar
        if((Aplicacion.equipoActual != null) && (Aplicacion.tramoActual != null)) {
            if ((!Aplicacion.equipoActual.equals("")) && (!Aplicacion.tramoActual.equals(""))) {
                for (int i=0; i<listaAMostrar.size(); i++) {
                    if (listaAMostrar.get(i).getNombreEquipo().equals(Aplicacion.equipoActual)) {
                        if(listaAMostrar.get(i).getCodigoTramo().contains(Aplicacion.tramoActual)) {
                            mSelected = i;
                            lstListado.setSelectionFromTop(i, 0);
                            mAdapter.notifyDataSetChanged();
                            break;
                        }
                    }
                }
            }
        }

        Button bNuevoEquipo = (Button) getView().findViewById(R.id.botonNuevoEquipo);
        bNuevoEquipo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                incluirNuevoEquipo();
            }
        });

    }

    // .................. Métodos del ciclo de vida del Fragment .......................

    @Override
    public void onResume() {
        super.onResume();
    }

    // .................. Métodos del Fragment ........................................

    public interface EquipoListener {
        void onEquipoSeleccionado(Equipo equipo);
    }

    public void setEquipoListener(FrgListadoEquipos.EquipoListener listener) {
        this.listener=listener;
    }

    public void incluirNuevoEquipo(){
        FragmentManager fragmentManager = getFragmentManager();
        DialogoDatosNuevoEquipo dialogo = new DialogoDatosNuevoEquipo();
        dialogo.show(fragmentManager, "tagDailogo");

    }

    class AdaptadorEquipos extends ArrayAdapter<Equipo> {

        Activity context;

        AdaptadorEquipos(Fragment context) {
            super(context.getActivity(), R.layout.listitem_equipos, listaAMostrar);
            this.context = context.getActivity();
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            View item = convertView;

            if (item == null) {
                LayoutInflater inflater = context.getLayoutInflater();
                item = inflater.inflate(R.layout.listitem_equipos, null);
            }

            // Se cambia el color de background si está seleccionado
            if (position == mSelected) {
                item.setBackgroundColor(getResources().getColor(R.color.background_seleted_listview));

            } else {
                item.setBackgroundColor(getResources().getColor(R.color.blanco));
            }

            TextView textTipo = (TextView)item.findViewById(R.id.itemEquipoTipo);
            TextView textNombre = (TextView)item.findViewById(R.id.itemEquipoNombre);
            TextView textTramo = (TextView)item.findViewById(R.id.itemEquipoTramo);
            Equipo eq = listaAMostrar.get(position);

            textTipo.setText(eq.getTipoInstalcion());
            textNombre.setText(eq.getNombreEquipo());
            String tramo = eq.getCodigoTramo();
            tramo = DBRevisiones.recuperarNumDeTramo(tramo);
            textTramo.setText(tramo);

            switch (eq.getEstado()) {
                case Aplicacion.ESTADO_PENDIENTE:
                    textTipo.setTextColor(getResources().getColor(R.color.pendientes));
                    textNombre.setTextColor(getResources().getColor(R.color.pendientes));
                    textTramo.setTextColor(getResources().getColor(R.color.pendientes));
                    break;
                case Aplicacion.ESTADO_EN_CURSO:
                    textTipo.setTextColor(getResources().getColor(R.color.enCurso));
                    textNombre.setTextColor(getResources().getColor(R.color.enCurso));
                    textTramo.setTextColor(getResources().getColor(R.color.enCurso));
                    break;
                case Aplicacion.ESTADO_FINALIZADA:
                    textTipo.setTextColor(getResources().getColor(R.color.finalizadas));
                    textNombre.setTextColor(getResources().getColor(R.color.finalizadas));
                    textTramo.setTextColor(getResources().getColor(R.color.finalizadas));
                    break;
                default:
                    break;
            }


            return(item);
        }
    }

}
