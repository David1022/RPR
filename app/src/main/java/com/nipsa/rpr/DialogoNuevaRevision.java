package com.nipsa.rpr;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Environment;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.PrivateKey;
import java.util.Vector;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

public class DialogoNuevaRevision extends DialogFragment {

    private final String DIRECTORIO_ENTRADA = "/RPR/Input/";
    private String direccionArchivosEntrada;
    private ListView lstListado;
    private TextView titulo;
    private Vector<File> listaNuevas;
    private DBRevisiones dbRevisiones;
    private DialogFragment dialog;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        dialog = this;
        dbRevisiones = new DBRevisiones(getContext());
        direccionArchivosEntrada = Environment.getExternalStoragePublicDirectory
                (Environment.DIRECTORY_DOWNLOADS) + DIRECTORIO_ENTRADA;

        try {
            listaNuevas = MostrarRevisiones.listarArchivosDirEntrada(direccionArchivosEntrada);
            int i = 0;
            while (i<listaNuevas.size()) {
                String nombreRevision = listaNuevas.elementAt(i).getName();
                nombreRevision = nombreRevision.substring(nombreRevision.lastIndexOf("/") + 1);
                boolean existe = dbRevisiones.existeRevision(nombreRevision);
                if (existe) {
                    listaNuevas.remove(i);
                } else {
                    i++;
                }
            }

            LayoutInflater inflater = getActivity().getLayoutInflater();
            View vista = inflater.inflate(R.layout.dialogo_nueva_revision, null);
            titulo = (TextView) vista.findViewById(R.id.tituloNuevaRevision);
            if (listaNuevas.size() == 0) {
                titulo.setText("No hay revisiones pendientes de incluir");
            }
            lstListado = (ListView)vista.findViewById(R.id.listNuevaRevision);
            lstListado.setAdapter(new DialogoNuevaRevision.AdaptadorNuevaRevision(this));

            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setView(vista);
            return builder.create();

        } catch (Exception e) {
            return null;
        }

    }

    class AdaptadorNuevaRevision extends ArrayAdapter<File> {

        Activity context;

        AdaptadorNuevaRevision(Fragment context) {
            super(context.getActivity(), R.layout.listitem_nueva_revision, listaNuevas);
            this.context = context.getActivity();
        }

        public View getView(final int position, View convertView, ViewGroup parent) {
            View item = convertView;

            if (item == null) {
                LayoutInflater inflater = context.getLayoutInflater();
                item = inflater.inflate(R.layout.listitem_nueva_revision, null);
            }

            String revision = listaNuevas.elementAt(position).getName();
            TextView texto = (TextView) item.findViewById(R.id.itemNuevaRevision);
            texto.setText(revision);
            texto.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String nombreArchivo = listaNuevas.elementAt(position).getName();
                    String nombreRevision = nombreArchivo.substring(0, nombreArchivo.lastIndexOf("."));
                    File archivo = Aplicacion.recuperarArchivo(direccionArchivosEntrada, nombreArchivo);
                    leerArchivoXML(archivo);
                    leerArchivoCoord(archivo);
                    Aplicacion.revisionActual = nombreRevision;
                    dbRevisiones.incluirRevision(nombreRevision, Aplicacion.ESTADO_PENDIENTE);
                    dialog.dismiss();
                }
            });

            return(item);
        }
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        final Activity activity = getActivity();
        if (activity instanceof DialogInterface.OnDismissListener) {
            ((DialogInterface.OnDismissListener) activity).onDismiss(dialog);
        }

    }

    /**
     * Método que leerá un archivo ".xml" basandose en la clase ManejadorXML
     *
     * @param archivo
     * @throws ParserConfigurationException
     * @throws SAXException
     * @throws IOException
     */
    public void leerArchivoXML(File archivo) {
        //HiloLeerArchivoXML hilo = new HiloLeerArchivoXML();
        //hilo.execute(archivo);

        if (MostrarRevisiones.esXML(archivo.getPath())) {
            String path = archivo.getPath();
            String nombreRevision = path.substring(path.lastIndexOf("/") + 1);
            nombreRevision = nombreRevision.substring(0, nombreRevision.lastIndexOf("."));
            Aplicacion.revisionActual = nombreRevision;

            try {
                InputSource entrada = new InputSource(new InputStreamReader(new FileInputStream(archivo)));
                SAXParserFactory fabrica = SAXParserFactory.newInstance();
                SAXParser parser = fabrica.newSAXParser();
                XMLReader lector = parser.getXMLReader();
                ManejadorXML manejadorXML = new ManejadorXML();
                lector.setContentHandler(manejadorXML);
                lector.parse(entrada);

            } catch (ParserConfigurationException parseException) {
                Log.e (Aplicacion.TAG, "Error al parsear el archivo XML " + parseException.toString());
            } catch (SAXException saxException) {
                Log.e (Aplicacion.TAG, "Error SAX al leer el archivo XML " + saxException.toString());
            } catch (IOException ioException) {
                Log.e (Aplicacion.TAG, "Error IOExcetion al parsear el archivo XML " + ioException.toString());
            }
        } else {
            Aplicacion.print("El archivo debe tener extension XML");
        }

    }

    /**
     * Método que recibe el archivo XML de referencia al cual se asociará el archivo KML. Leerá el
     * archivo KML y asociará las coordenadas recogidas al equipo correspondiente
     *
     * @param archivoXML con extensión XML
     * @throws ParserConfigurationException
     * @throws SAXException
     * @throws IOException
     */
    public void leerArchivoCoord (File archivoXML) {
        // Se recupera el nombre y la ruta del archivo XML para recuperar el archivo KML con el mismo nombre
        String nombreRevision = archivoXML.getName();
        nombreRevision = nombreRevision.substring(0, nombreRevision.lastIndexOf("."));
        String nombreKML = nombreRevision + ".kml";
        String ruta = archivoXML.getAbsolutePath();
        ruta = ruta.substring(0, ruta.lastIndexOf("/"));
        File archivoCoord = Aplicacion.recuperarArchivo(ruta, nombreKML);
        //Si se encuentra el archivo KML con el mismo nombre que el XML se leerá
        if (archivoCoord != null) {
            // Se lee el archivo en un hilo secundario
            //HiloLeerArchivosCoord hilo = new HiloLeerArchivosCoord();
            //hilo.execute(archivoCoord);
            LectorCoord lector = new LectorCoord(nombreRevision);
            lector.leer(archivoCoord);

        } else { // Si no se encuentra se muestra un mensaje por pantalla para informar al usuario
            Aplicacion.print("No se ha encontrado archivo KML asociado a la revision " + nombreRevision);
        }

    }

}
