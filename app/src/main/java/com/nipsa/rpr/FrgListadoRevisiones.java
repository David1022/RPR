package com.nipsa.rpr;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.Vector;

public class FrgListadoRevisiones extends Fragment {

    private RevisionListener listener;
    private ListView lstListado;
    private Vector<Revision> listaAMostrar;
    private DBRevisiones dbRevisiones;
    private Button bNuevaRevision;
    private AdaptadorRevisiones mAdapter;
    private int mSelected;
    private final int ABRIR_ARCHIVO = 1;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.frg_listado_revisiones, container, false);

        dbRevisiones = new DBRevisiones(getContext());
        listaAMostrar = new Vector<Revision>();
/*
        listaAMostrar.clear();
        listaAMostrar.addAll(dbRevisiones.solicitarListaRevisiones());
*/
        mSelected = -1;
        lstListado = (ListView) v.findViewById(R.id.listadoRevisiones);
        mAdapter = new AdaptadorRevisiones(this);
        bNuevaRevision = (Button) v.findViewById(R.id.botonNuevaRevision);
        lstListado.setAdapter(mAdapter);
        lstListado.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> list, View view, int pos, long id) {
                if (listener != null) {
                    mSelected = pos;
                    mAdapter.notifyDataSetChanged();
                    listener.onRevisionSeleccionada((Revision) lstListado.getAdapter().getItem(pos));
                }
            }
        });

        bNuevaRevision.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lanzarDialogoNuevaRevision();
                //abrirSelectorArchivos();
            }
        });

        return v;

    }

    @Override
    public void onActivityCreated(Bundle state) {
        super.onActivityCreated(state);

        registerForContextMenu(lstListado);
    }

    @Override
    public void onResume() {
        super.onResume();
        listaAMostrar.clear();
        listaAMostrar.addAll(dbRevisiones.solicitarListaRevisiones());
        mAdapter.notifyDataSetChanged();
        // Si hay revision actual se busca y para cambiarle el color del background y ponerla en primer lugar
        if(Aplicacion.revisionActual != null) {
            if (!Aplicacion.revisionActual.equals("")) {
                for (int i=0; i<listaAMostrar.size(); i++) {
                    if (listaAMostrar.get(i).getNombre().equals(Aplicacion.revisionActual)) {
                        mSelected = i;
                        lstListado.setSelectionFromTop(i, 0);
                        mAdapter.notifyDataSetChanged();
                        break;
                    }
                }
            }
        }


    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getActivity().getMenuInflater();

        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
        Revision rev = (Revision) lstListado.getAdapter().getItem(info.position);
        menu.setHeaderTitle(rev.getNombre());
        inflater.inflate(R.menu.menu_longclick_revisiones, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        Revision rev = (Revision) lstListado.getAdapter().getItem(info.position);

        switch (item.getItemId()) {
            case R.id.backup:
                Aplicacion.backup(rev.getNombre());
                break;
            case R.id.finalizar:
                finalizarRevision (rev);
                break;
            case R.id.eliminar:
                lanzarDialogoConfirmacionEliminar(rev);
/*
                eliminarRevision (rev);
                if (Aplicacion.revisionActual != null) {
                    if (Aplicacion.revisionActual.equals(rev.getNombre())) {
                        Aplicacion.revisionActual = "";
                    }
                }
*/
                break;
            default:
                break;
        }

        return super.onContextItemSelected(item);
    }

    public void finalizarRevision (Revision revision) {
        if (dbRevisiones.equiposFinalizados(revision.getNombre())){
            Aplicacion.backup(revision.getNombre());
            // Se actualiza el estado de la revisión
            dbRevisiones.actualizarItemRevision(revision.getNombre(), "Estado", Aplicacion.ESTADO_FINALIZADA);

            //Se actualiza el listview
            listaAMostrar.clear();
            listaAMostrar.addAll(dbRevisiones.solicitarListaRevisiones());
            mAdapter.notifyDataSetChanged();

            // Se mueven las fotos al directorio de salida
            Aplicacion.moverFotos(revision);
        } else {
            Aplicacion.print("Comprueba que hayas revisado todos los equipos");
        }
    }

    public void lanzarDialogoConfirmacionEliminar(final Revision revision) {
        LayoutInflater li = LayoutInflater.from(getContext());
        View vista = li.inflate(R.layout.dialogo_confirmacion_eliminar, null);

        AlertDialog.Builder dialogo = new AlertDialog.Builder(getContext());
        dialogo.setView(vista);
        dialogo.setPositiveButton(R.string.aceptar, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                eliminarRevision(revision);
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

    public void eliminarRevision (Revision revision) {
        DBRevisiones dbRevisiones = new DBRevisiones(getContext());

        // Se borran los registros de la BDD y se borran las fotos
        dbRevisiones.borrarRevision(revision);
        Aplicacion.borrarFotos(revision);

        // Se actualiza el listView (FrgListadoRevisiones)
        listaAMostrar.clear();
        listaAMostrar.addAll(dbRevisiones.solicitarListaRevisiones());
        mAdapter.notifyDataSetChanged();

        // Si se está mostrando la revisión eliminada se borra de la pantalla
        if ((Aplicacion.revisionActual == null) || (Aplicacion.revisionActual.equals(revision.getNombre()))) {
            Aplicacion.revisionActual = "";
            boolean hayDetalle = (getActivity().getSupportFragmentManager()
                    .findFragmentById(R.id.contenedorFragmentDetalleRevision) != null);
            Fragment f = getActivity().getSupportFragmentManager().findFragmentById(R.id.contenedorFragmentDetalleRevision);
            FrgDetalleRevision fdr = new FrgDetalleRevision();
            if (hayDetalle) {
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.remove(f);
                transaction.commit();
            }
        }

    }

    public void lanzarDialogoNuevaRevision () {
        FragmentManager fragmentManager = getFragmentManager();
        DialogoNuevaRevision dialogo = new DialogoNuevaRevision();
        dialogo.show(fragmentManager, "tagDailogo");
    }


    public interface RevisionListener {
        void onRevisionSeleccionada(Revision r);
    }

    public void setRevisionListener(RevisionListener listener) {
        this.listener=listener;
    }

    class AdaptadorRevisiones extends ArrayAdapter<Revision> {

        Activity context;

        AdaptadorRevisiones(Fragment context) {
            super(context.getActivity(), R.layout.listitem_revisiones, listaAMostrar);
            this.context = context.getActivity();
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            View item = convertView;

            if (item == null) {
                LayoutInflater inflater = context.getLayoutInflater();
                item = inflater.inflate(R.layout.listitem_revisiones, null);
            }

            // Se cambia el color de background si está seleccionado
            if (position == mSelected) {
                item.setBackgroundColor(getResources().getColor(R.color.background_seleted_listview));

            } else {
                item.setBackgroundColor(getResources().getColor(R.color.blanco));
            }

            TextView textNombre = (TextView)item.findViewById(R.id.itemRevisionNombre);
            Revision rev = listaAMostrar.get(position);

            textNombre.setText(rev.getNombre());
            switch (rev.getEstado()) {
                case Aplicacion.ESTADO_PENDIENTE:
                    textNombre.setTextColor(getResources().getColor(R.color.pendientes));
                    break;
                case Aplicacion.ESTADO_EN_CURSO:
                    textNombre.setTextColor(getResources().getColor(R.color.enCurso));
                    break;
                case Aplicacion.ESTADO_FINALIZADA:
                    textNombre.setTextColor(getResources().getColor(R.color.finalizadas));
                    break;
                default:
                    break;
            }

            return(item);
        }

    }

}