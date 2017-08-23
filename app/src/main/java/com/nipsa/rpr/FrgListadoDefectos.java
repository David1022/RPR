package com.nipsa.rpr;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.ListFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.Vector;

import static com.nipsa.rpr.MostrarRevisiones.dbRevisiones;

public class FrgListadoDefectos extends Fragment {

    private String[] listaGrupoDefectosLAMT = {"Aislamiento", "Conductor", "Cruces", "Herrajes",
                                "Cimientos", "Estructura", "Paso por zonas", "Apoyos",
                                "Aparamenta", "Puestas a Tierra", "Entorno perimetral", "Observaciones"};
    private String[] listaGrupoDefectosCT = {"Aislamiento", "Transformador", "Aparamenta y dispositivos de maniobra",
                                "Puestas a Tierra", "Secundario BT del transformador", "Protecciones y obra civil",
                                "Embarrados y conexiones", "Observaciones"};
    private String[] listaGrupoDefectosPT = {"Aislamiento", "Distancias (D)", "Transformador", "Herrajes",
                                "Cimientos", "Estructura", "Distancias (L)", "Apoyos",
                                "Aparamenta y dispositivos de maniobra", "Puestas a Tierra", "Secundario BT del transformador",
                                "Entorno perimetral", "Embarrados y conexiones", "Observaciones"};
    private FrgListadoDefectos.GrupoDefectoListener listener;
    private ListView lstListado;
    private String[] listaAMostrar;
    public DBRevisiones dbRevisiones;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {

        switch (Aplicacion.tipoActual){
            case Aplicacion.LAMT:
                listaAMostrar = listaGrupoDefectosLAMT;
                break;
            case Aplicacion.CT:
                listaAMostrar = listaGrupoDefectosCT;
                break;
            case Aplicacion.PT:
                listaAMostrar = listaGrupoDefectosPT;
                break;
            default:
                break;
        }
        return inflater.inflate(R.layout.frg_listado_defectos, container, false);

    }

    @Override
    public void onActivityCreated(Bundle state) {
        super.onActivityCreated(state);

        dbRevisiones = new DBRevisiones(getContext());
        lstListado = (ListView)getView().findViewById(R.id.listadoDefectos);
        lstListado.setAdapter(new AdaptadorGrupoDefecto(this));
        lstListado.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> list, View view, int pos, long id) {
                if (listener!=null) {
                    listener.onGrupoDefectoSeleccionado(
                            (String) lstListado.getAdapter().getItem(pos));
                }
            }
        });

        Button bMostrarDefectos = (Button) getView().findViewById(R.id.botonMostrarDefectos);
        bMostrarDefectos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mostrarDefectosAsociados();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    public interface GrupoDefectoListener {
        void onGrupoDefectoSeleccionado(String string);
    }

    public void setGrupoDefectoListener(FrgListadoDefectos.GrupoDefectoListener listener) {
        this.listener = listener;
    }

    public void mostrarDefectosAsociados () {
        FragmentManager fragmentManager = getFragmentManager();
        DialogoDefectosAsociados dialogo = new DialogoDefectosAsociados();
        dialogo.show(fragmentManager, "tagDailogo");
    }

    class AdaptadorGrupoDefecto extends ArrayAdapter<String> {

        Activity context;

        AdaptadorGrupoDefecto(Fragment context) {
            super(context.getActivity(), R.layout.listitem_grupo_defectos, listaAMostrar);
            this.context = context.getActivity();
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            View item = convertView;

            if (item == null) {
                LayoutInflater inflater = context.getLayoutInflater();
                item = inflater.inflate(R.layout.listitem_grupo_defectos, null);
            }

            TextView texto = (TextView)item.findViewById(R.id.itemGrupoDefecto);
            String s = listaAMostrar[position];
            texto.setText(s);

            return(item);
        }
    }

}
