package com.nipsa.rpr;
/**
 * Abrir terminal e ir a la carpeta
 *              C:\Program Files\Java\jdk1.8.0_111\bin
 * Ejecutar el comando
 *              keytool -list -v -alias "Key alias" -keystore "Key store path" -storepass "Key store password" -keypass "Key password"
 *              EJEMPLO: keytool -list -v -alias RPR -keystore "C:/Users/david.mendano/Desktop/APP RPR/AndroidStudioProjects/Nipsa.jks" -storepass nipsa2017 -keypass nipsarpr
 * Copiar huella deigital
 *              8D:BD:18:00:27:32:EE:AE:B3:F3:9F:9A:D0:71:93:17:92:EF:A9:89
 * La nueva clave se debe poner en el archivo correspondiente segun la huella digital (debug o release)
 *              C:\Users\david.mendano\Desktop\APP RPR\AndroidStudioProjects\RPR\app\src\----DEBUG----\res\values
 *              C:\Users\david.mendano\Desktop\APP RPR\AndroidStudioProjects\RPR\app\src\----RELEASE----\res\values
 */

import android.app.Application;
import android.content.Context;
import android.database.Cursor;
import android.os.Environment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Vector;

public class Aplicacion extends Application {

    public static final String DIRECTORIO_SALIDA_BD = "/RPR/OUTPUT/";

    public static final String ESTADO_PENDIENTE = "Pendiente";
    public static final String ESTADO_EN_CURSO = "En curso";
    public static final String ESTADO_FINALIZADA = "Finalizada";
    public static final String LAMT = "LAMT";
    public static final String CT = "CT";
    public static final String PT = "PT";
    public static final String SI = "Si";
    public static final String NO = "No";
    public static final String TAG = "ErrorRPR: ";
    public static final String TIPO = "Tipo";
    public static final String EQUIPO = "Equipo";
    public static final String DEFECTO = "Defecto";
    public static final String NUM_APOYO = "Num. apoyo:";
    public static String revisionActual;
    public static String equipoActual;
    public static String tipoActual;
    public static String defectoActual;
    public static String tramoActual;
    public static String grupoDefectoActual;
    public static String tituloRevision;
    private static Context contexto;

    public static int ANCHOFOTO = 100;
    public static int ALTOFOTO = 100;

    public static String[] listaTitulosRevisiones = {"Nombre", "Estado", "Inspector1", "Inspector2", "Colegiado", "Equipo usado",
            "Metodologia", "Código Nipsa", "Numero de Trabajo", "Código Inspección"};
    public static String[] listaTitulosEquipos = {"Tipo Instalación", "Cód. BDE Instalación", "Descripción Instalación",
            "Posición, Tramo o Localización", "Nombre de Tramo/Código de Tramo : Nombre de Tramo del CD",
            "Descripción Tramo", "Equipo o Apoyo", "Fecha de Inspección",
            "Defecto o Medida", "Descripción Defecto/Medida", "Crit", "Ocurrencias o Medida",
            "Estado Inst", "Trabajo de Inspección", "Valoración", "Importe", "Límite de Corrección",
            "Trabajo de corrección", "Fecha de Corrección", "D", "C", "Código de Inspección",
            "Observaciones", "Documento/s a asociar", "Descripción documento/s", "Fecha de Alta",
            "TPL", "Km. Aéreos"};

    /**
     *
     */
    @Override
    public void onCreate() {
        super.onCreate();
        contexto = this;
    }

    /**
     * Método para generar un Toast e imprimirlo por pantalla
     *
     * @param texto
     */
    public static void print (String texto) {
        Toast.makeText(contexto, texto, Toast.LENGTH_LONG).show();
    }

    public static void printCenter (String texto) {
        Toast t = Toast.makeText(contexto, texto, Toast.LENGTH_SHORT);
        t.setGravity(Gravity.CENTER, 0, 0);
        View view = t.getView();
        view.setBackgroundColor(contexto.getResources().getColor(R.color.rojo));
        t.show();
    }

    /**
     * Genera todos los archivos de salida
     *
     * @param revision
     */
    public static void backup (Revision revision) {
        HiloBackup hilo = new HiloBackup();
        hilo.execute(revision);

    }

    /**
     * Metodo para crear una copia exacta de la base de datos en un directorio accesible externamente
     */
    public static void backupBaseDatos() {
        String timeStamp = fechaHoraActual();

        // Ruta fichero interno BD
        final String inFileName = "/data/data/com.nipsa.rpr/databases/" + DBRevisiones.DATABASE_NAME;
        try {
            File dbFile = new File(inFileName);
            FileInputStream fis = new FileInputStream(dbFile);
            // Ruta fichero salida BD
            String directorio = Environment.getExternalStoragePublicDirectory
                    (Environment.DIRECTORY_DOWNLOADS) + DIRECTORIO_SALIDA_BD;

            File d = new File(directorio);
            if (!d.exists()) {
                d.mkdirs();
            }
            // Nombre fichero salida BD
            String outFileName = directorio + "/" + DBRevisiones.DATABASE_NAME +
                                                        // "_" + timeStamp +
                                                        ".sqlite";

            // MostrarRevisiones de la copia
            OutputStream output = new FileOutputStream(outFileName);

            byte[] buffer = new byte[1024];
            int length;
            while ((length = fis.read(buffer)) > 0) {
                output.write(buffer, 0, length);
            }

            output.flush();
            output.close();
            fis.close();
        } catch (Exception e) {
            Log.e("ErrorRPR: ", e.toString());
        }

    }

    public static void generarBackupRevision(String revision) {
        DBBackup dbBackup = new DBBackup(contexto);
        dbBackup.crearBackup(revision);
    }

    public static void finalizarRevision (Revision revision) {
        HiloFinalizarRevision hilo = new HiloFinalizarRevision();
        hilo.execute(revision);
    }

    /**
     * Metodo para generar un String con la fecha actual
     *
     * @return String con la fecha actual
     */
    public static String fechaHoraActual () {
        Date d = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("ddMMyyyy_HHmmss"); // Formato en el que se proporcionaran los datos
        String fechaHoraActual = sdf.format(d); // Se genera un String con el formato indicado
        return fechaHoraActual;
    }

    /**
     * Corrige la fecha para darle un formato dd/mm/yyyy
     * @param fecha
     * @return
     */
    public static String corregirFecha (String fecha){
        String fechaCorregida = "";
        if (fecha != null){
            if (!fecha.equals("")){
                try {
                    String dia = fecha.substring(0, fecha.indexOf("/"));
                    String mes = fecha.substring((fecha.indexOf("/") + 1), fecha.lastIndexOf("/"));
                    if (dia.length() == 1) {
                        dia = "0" + dia;
                    }
                    if (mes.length() == 1) {
                        mes = "0" + mes;
                    }
                    fechaCorregida = dia + "/" + mes + (fecha.substring(fecha.lastIndexOf("/")));
                } catch (Exception e) {}
                finally {
                    return fechaCorregida;
                }
            } else {
                return "";
            }
        } else {
            return null;
        }
    }

    /**
     * Metodo para leer la BDD con los Defectos
     */
    public static void lecturaBDDefectos() {

        // Ruta ubicacion fichero BD
        String outFileName = "/data/data/com.nipsa.rpr/databases/";
        try {
            File dbFile = new File(outFileName);
            if (!dbFile.exists()) {
                dbFile.mkdirs();
            }
            InputStream inputRawFileName = contexto.getResources().openRawResource(R.raw.defectos);
            outFileName = outFileName + DBGlobal.DATABASE_NAME;

            // MostrarRevisiones de la copia
            OutputStream output = new FileOutputStream(outFileName);

            byte[] buffer = new byte[1024];
            int length;
            while ((length = inputRawFileName.read(buffer)) > 0) {
                output.write(buffer, 0, length);
            }

            output.flush();
            output.close();
            inputRawFileName.close();
        } catch (Exception e) {
            Log.e("ErrorRPR: ", e.toString());
            Aplicacion.print(e.toString());
        }

    }

    /**
     * Crea un archivo con un nombre concreto para guardar una imagen tomada con la cámara
     *
     * @return
     * @throws IOException
     */
    public static File crearArchivoImagen() throws IOException {

        String fecha = Aplicacion.fechaHoraActual();
        String nombreFichero = equipoActual + "_Imagen_" + fecha;
        //String nombreFichero = "Imagen_" + fecha;
        File ruta = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES +
                "/" + Aplicacion.revisionActual + "/" + Aplicacion.equipoActual);
        if (!ruta.exists()){
            ruta.mkdirs();
        }

        File imagen = File.createTempFile(nombreFichero, ".jpg", ruta);

        return imagen;
    }

    /**
     * Método que devuelve el archivo con el nombre y ruta recibidos por parámetro.
     * Devolverá "null" si no se encuentra el archivo
     *
     * @param ruta
     * @param nombre
     * @return el archivo solicitado o null si no encuentra el archivo
     */
    public static File recuperarArchivo (String ruta, String nombre) {
        File archivo = null;

        File f = new File(ruta);
        //Se listan los archivos de la carpeta del apoyo
        File[] listaArchivos = f.listFiles();

        if(listaArchivos != null) {
            for (int i=0; i<listaArchivos.length; i++) {
                String nombreArchivo = listaArchivos[i].getName();
                if (nombreArchivo.equalsIgnoreCase(nombre)) {
                    archivo = listaArchivos[i];
                    break;
                }
            }
        }

        return archivo;
    }

    /**
     * Genera el archivo XML con los datos recogidos hasta ese momento
     *
     * @param revision
     */
    public static void generarXML (String revision) {
        String nombreArchivo = revision + ".xml";
        File archivo = crearArchivoSalida(revision, nombreArchivo);

        try {
            FileOutputStream fos = new FileOutputStream(archivo);
            fos.write(Auxiliar.ENCABEZADO_XML_ARCHIVO.getBytes());
            fos.write(generarCuerpoXML(revision).getBytes());
            fos.close();
        } catch (Exception e) {
            Log.e ("Error RPR: ", e.toString());
            //Aplicacion.print("ErrorRPR: Error al generar el archivo XML" + e.toString());
        }
    }

    /**
     * Crea un archivo de salida con el nombre recibido por parámetro
     *
     * @param revision
     * @param nombreArchivo
     * @return
     */
    public static File crearArchivoSalida (String revision, String nombreArchivo) {
        File archivoSalida = null;

        // Ruta ubicacion fichero salida
        try {
            File directorio = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS +
                    "/RPR/Output/" + revision + "/");
            if (!directorio.exists()) {
                directorio.mkdirs();
            }
            archivoSalida = new File (directorio, nombreArchivo);
        } catch (Exception e) {
            Log.e ("ErrorRPR: ", "Error al generar el archivo " + e.toString());
            //Aplicacion.print("Error al generar el archivo :" + e.toString());
            return null;
        }

        return archivoSalida;
    }

    /**
     * Genera el cuerpo del XML (la cabecera se incluye de forma estática)
     * @param revision
     * @return
     */
    public static String generarCuerpoXML(String revision) {
        StringBuffer texto = new StringBuffer();
        DBRevisiones dbRevisiones = new DBRevisiones(contexto);
        // Corrección del nombre de la hoja para evitar problemas de compatibilidad con el Excel
        String nombreHoja = "UNIDADES_REVISION_" + revision + "_1.xls";
        nombreHoja = nombreHoja.replace("-", "_");
        if (nombreHoja.length()>30) {
            nombreHoja = nombreHoja.substring(0, 29);
        }

        Cursor cEquipos = dbRevisiones.solicitarDatosEquipos(revision);

        // Hoja 1: Equipos (Equipos de la revisión)
        texto.append(" <Worksheet ss:Name=\"" + nombreHoja + "\">\n");
        texto.append("  <Table ss:ExpandedColumnCount=\"32\" " +
                "   x:FullRows=\"1\" ss:StyleID=\"s64\" ss:DefaultColumnWidth=\"60\"\n"+
                "   ss:DefaultRowHeight=\"11.25\">\n");
        texto.append(Auxiliar.ENCABEZADO_XML_HOJA1_A);
        texto.append(revision);
        texto.append(Auxiliar.ENCABEZADO_XML_HOJA1_B);
        if (cEquipos != null && cEquipos.moveToFirst()) {
            do {
                // De cada equipo se toman los defectos/medidas asociados
                int col = cEquipos.getColumnIndex("NombreEquipo");
                String nombreEquipo = cEquipos.getString(col);
                col = cEquipos.getColumnIndex("PosicionTramo");
                String tramo = cEquipos.getString(col);
                Vector<Defecto> listaDefectos = dbRevisiones.solicitarDefectosMedidas(revision, nombreEquipo, tramo);
                col = cEquipos.getColumnIndex("TipoInstalacion");
                String tipo = cEquipos.getString(col);

                // Si no hay defectos/medidas asociados se incluye el defecto tal cual
                //if ((listaDefectos == null) || (listaDefectos.size()==0)) {
                // Se incluye el equipo en el Excel
                    texto.append("<Row>\n");
                    for (int i=1; i<=28; i++) {
                        String fecha, imagen;
                        switch (i) {
                            case 4:
                                texto.append("<Cell ss:StyleID=\"s79\">");
                                texto.append("<Data ss:Type=\"String\">");
                                if (tipo.equals("Z")) { // En los CTs no debe aparecer la PosiciónTramo
                                    texto.append("");
                                } else {
                                    texto.append(cEquipos.getString(i));
                                }
                                break;
                            case 8:
                                texto.append("<Cell ss:StyleID=\"s79\">");
                                texto.append("<Data ss:Type=\"String\">");
                                fecha = cEquipos.getString(i);
                                texto.append(corregirFecha(fecha));
                                break;
                            case 19:
                                texto.append("<Cell ss:StyleID=\"s79\">");
                                texto.append("<Data ss:Type=\"String\">");
                                fecha = cEquipos.getString(i);
                                texto.append(corregirFecha(fecha));
                                break;
                            case 24:
                                imagen = cEquipos.getString(i);
                                //texto.append("<Cell ss:StyleID=\"s102\" ss:HRef=\"Fotos\\" + imagen + "\">");
                                texto.append("<Cell ss:StyleID=\"s102\" ss:HRef=\"" + imagen + "\">");
                                texto.append("<Data ss:Type=\"String\">");
                                texto.append(imagen);
                                break;
                            case 25:
                                imagen = cEquipos.getString(i);
                                //texto.append("<Cell ss:StyleID=\"s102\" ss:HRef=\"Fotos\\" + imagen + "\">");
                                texto.append("<Cell ss:StyleID=\"s102\" ss:HRef=\"" + imagen + "\">");
                                texto.append("<Data ss:Type=\"String\">");
                                texto.append(cEquipos.getString(i));
                                break;
                            default:
                                texto.append("<Cell ss:StyleID=\"s79\">");
                                texto.append("<Data ss:Type=\"String\">");
                                texto.append(cEquipos.getString(i));
                                break;
                        }
                        texto.append("</Data>");
                        texto.append("</Cell>\n");
                    }
                    texto.append("</Row>\n");
                //} else { // Si hay defecto y/o medida se recorren todos los defectos
                if ((listaDefectos != null) && (listaDefectos.size()>0)) {
                    for (int i=0; i<listaDefectos.size(); i++) {
                        Defecto def = listaDefectos.elementAt(i);
                        boolean esDefecto =  def.getEsDefecto().equals(Aplicacion.SI);
                        // Si es defecto se incluye el defecto y el código correspondiente
                        if (esDefecto) {
                            texto.append("<Row>\n");
                            for (int j=1; j<=28; j++) {
                                String fecha, imagen;
                                switch (j){
                                    case 4:
                                        texto.append("<Cell ss:StyleID=\"s79\">");
                                        texto.append("<Data ss:Type=\"String\">");
                                        if (tipo.equals("Z")) {
                                            texto.append("");
                                        } else {
                                            texto.append(cEquipos.getString(j));
                                        }
                                        break;
                                    case 8:
                                        texto.append("<Cell ss:StyleID=\"s79\">");
                                        texto.append("<Data ss:Type=\"String\">");
                                        fecha = cEquipos.getString(j);
                                        texto.append(corregirFecha(fecha));
                                        break;
                                    case 9: // Se incluye el codigo del defecto
                                        texto.append("<Cell ss:StyleID=\"s79\">");
                                        texto.append("<Data ss:Type=\"String\">");
                                        texto.append(def.getCodigoDefecto());
                                        break;
                                    case 12: // Se incluye el número de ocurrencias del defecto
                                        texto.append("<Cell ss:StyleID=\"s79\">");
                                        texto.append("<Data ss:Type=\"String\">");
                                        texto.append(def.getOcurrencias());
                                        break;
                                    case 19: // Si el defecto se ha corregido se incluye la fecha de corrección
                                        texto.append("<Cell ss:StyleID=\"s79\">");
                                        texto.append("<Data ss:Type=\"String\">");
                                        if (def.getCorregido().equals(Aplicacion.SI)) {
                                            texto.append(corregirFecha(def.getFechaCorreccion()));
                                        } else {
                                            texto.append(cEquipos.getString(j));
                                        }
                                        break;
                                    case 23: // Se incluyen las observaciones del defecto además de las del equipo
                                        texto.append("<Cell ss:StyleID=\"s79\">");
                                        texto.append("<Data ss:Type=\"String\">");
                                        texto.append(def.getObservaciones());
                                        break;
                                    case 24: // Se incluye la foto con hipervínculo
                                        imagen = def.getFoto1();
                                        //texto.append("<Cell ss:StyleID=\"s102\" ss:HRef=\"Fotos\\" + imagen + "\">");
                                        texto.append("<Cell ss:StyleID=\"s102\" ss:HRef=\"" + imagen + "\">");
                                        texto.append("<Data ss:Type=\"String\">");
                                        texto.append(imagen);
                                        break;
                                    case 25: // Se incluye la foto con hipervínculo
                                        imagen = def.getFoto2();
                                        //texto.append("<Cell ss:StyleID=\"s102\" ss:HRef=\"Fotos\\" + imagen + "\">");
                                        texto.append("<Cell ss:StyleID=\"s102\" ss:HRef=\"" + imagen + "\">");
                                        texto.append("<Data ss:Type=\"String\">");
                                        texto.append(imagen);
                                        break;
                                    default: // Por defecto se incluye el valor recogido en la BDD
                                        texto.append("<Cell ss:StyleID=\"s79\">");
                                        texto.append("<Data ss:Type=\"String\">");
                                        texto.append(cEquipos.getString(j));
                                        break;
                                }
                                texto.append("</Data>");
                                texto.append("</Cell>\n");
                            }
                            texto.append("</Row>\n");
                        }
                // Si además tiene medida se incluye también
                        boolean hayMedida = (!def.getMedida().equals(""));
                        if (hayMedida) {
                            boolean hayRc = false;
                            texto.append("<Row>\n");
                            for (int j=1; j<=28; j++) {
                                String fecha, imagen;
                                switch (j){
                                    case 4: // Si es CT no se pone el tramo
                                        texto.append("<Cell ss:StyleID=\"s79\">");
                                        texto.append("<Data ss:Type=\"String\">");
                                        if (tipo.equals("Z")) {
                                            texto.append("");
                                        } else {
                                            texto.append(cEquipos.getString(j));
                                        }
                                        break;
                                    case 8: // Se corrige la fecha para que tenga el formato pedido por EDE
                                        texto.append("<Cell ss:StyleID=\"s79\">");
                                        texto.append("<Data ss:Type=\"String\">");
                                        fecha = cEquipos.getString(j);
                                        texto.append(corregirFecha(fecha));
                                        break;
                                    case 9: // Se incluye el codigo de la medida en lugar del codigo del defecto
                                        texto.append("<Cell ss:StyleID=\"s79\">");
                                        texto.append("<Data ss:Type=\"String\">");
                                        texto.append(equivalenciaMedidaCodigo(def.getCodigoDefecto(), def.getPatUnidas()));
                                        if (def.getCodigoDefecto().equals("T55D") || def.getCodigoDefecto().equals("T55C")) {
                                            hayRc = true;
                                        }
                                        break;
                                    case 12: // Se incluye la medida en lugar de las ocurrencias
                                        texto.append("<Cell ss:StyleID=\"s79\">");
                                        texto.append("<Data ss:Type=\"String\">");
                                        texto.append(adaptarMedida(def.getMedida()));
                                        break;
                                    case 23: // Se incluyen las observaciones del defecto además de las del equipo
                                        texto.append("<Cell ss:StyleID=\"s79\">");
                                        texto.append("<Data ss:Type=\"String\">");
                                        texto.append("TR1");
                                        if (!def.getObservaciones().equals("")) {
                                            texto.append(": " + def.getObservaciones());
                                        }
                                        break;
                                    case 24: // Se incluye también el vínculo a la foto en las medidas
                                        imagen = def.getFoto1();
                                        //texto.append("<Cell ss:StyleID=\"s102\" ss:HRef=\"Fotos\\" + imagen + "\">");
                                        texto.append("<Cell ss:StyleID=\"s102\" ss:HRef=\"" + imagen + "\">");
                                        texto.append("<Data ss:Type=\"String\">");
                                        texto.append(imagen);
                                        break;
                                    case 25: // Se incluye también el vínculo a la foto en las medidas
                                        imagen = def.getFoto2();
                                        //texto.append("<Cell ss:StyleID=\"s102\" ss:HRef=\"Fotos\\" + imagen + "\">");
                                        texto.append("<Cell ss:StyleID=\"s102\" ss:HRef=\"" + imagen + "\">");
                                        texto.append("<Data ss:Type=\"String\">");
                                        texto.append(imagen);
                                        break;
                                    default:
                                        texto.append("<Cell ss:StyleID=\"s79\">");
                                        texto.append("<Data ss:Type=\"String\">");
                                        texto.append(cEquipos.getString(j));
                                        break;
                                }
                                texto.append("</Data>");
                                texto.append("</Cell>\n");
                            }
                            texto.append("</Row>\n");
                            if (hayRc) {
                                incluirRc(texto, cEquipos, def, "TR1");
                            }
                        }
                        // Si además tiene medida de Tr2 se incluye también
                        boolean hayMedidaTr2 = (!def.getMedidaTr2().equals(""));
                        if (hayMedidaTr2) {
                            boolean hayRc = false;
                            texto.append("<Row>\n");
                            for (int j=1; j<=28; j++) {
                                String fecha, imagen;
                                switch (j){
                                    case 4: // Si es CT no se pone el tramo
                                        texto.append("<Cell ss:StyleID=\"s79\">");
                                        texto.append("<Data ss:Type=\"String\">");
                                        if (tipo.equals("Z")) {
                                            texto.append("");
                                        } else {
                                            texto.append(cEquipos.getString(j));
                                        }
                                        break;
                                    case 8: // Se corrige la fecha para que tenga el formato pedido por EDE
                                        texto.append("<Cell ss:StyleID=\"s79\">");
                                        texto.append("<Data ss:Type=\"String\">");
                                        fecha = cEquipos.getString(j);
                                        texto.append(corregirFecha(fecha));
                                        break;
                                    case 9: // Se incluye el codigo de la medida en lugar del codigo del defecto
                                        texto.append("<Cell ss:StyleID=\"s79\">");
                                        texto.append("<Data ss:Type=\"String\">");
                                        texto.append(equivalenciaMedidaCodigo(def.getCodigoDefecto(), def.getPatUnidas()));
                                        if (def.getCodigoDefecto().equals("T55D") || def.getCodigoDefecto().equals("T55C")) {
                                            hayRc = true;
                                        }

                                        break;
                                    case 12: // Se incluye la medida en lugar de las ocurrencias
                                        texto.append("<Cell ss:StyleID=\"s79\">");
                                        texto.append("<Data ss:Type=\"String\">");
                                        texto.append(adaptarMedida(def.getMedidaTr2()));
                                        break;
                                    case 23: // Se incluyen las observaciones del defecto además de las del equipo
                                        texto.append("<Cell ss:StyleID=\"s79\">");
                                        texto.append("<Data ss:Type=\"String\">");
                                        texto.append("TR2");
                                        if (!def.getObservaciones().equals("")) {
                                            texto.append(": " + def.getObservaciones());
                                        }
                                        break;
                                    case 24: // Se incluye también el vínculo a la foto en las medidas
                                        imagen = def.getFoto1();
                                        //texto.append("<Cell ss:StyleID=\"s102\" ss:HRef=\"Fotos\\" + imagen + "\">");
                                        texto.append("<Cell ss:StyleID=\"s102\" ss:HRef=\"" + imagen + "\">");
                                        texto.append("<Data ss:Type=\"String\">");
                                        texto.append(imagen);
                                        break;
                                    case 25: // Se incluye también el vínculo a la foto en las medidas
                                        imagen = def.getFoto2();
                                        //texto.append("<Cell ss:StyleID=\"s102\" ss:HRef=\"Fotos\\" + imagen + "\">");
                                        texto.append("<Cell ss:StyleID=\"s102\" ss:HRef=\"" + imagen + "\">");
                                        texto.append("<Data ss:Type=\"String\">");
                                        texto.append(imagen);
                                        break;
                                    default:
                                        texto.append("<Cell ss:StyleID=\"s79\">");
                                        texto.append("<Data ss:Type=\"String\">");
                                        texto.append(cEquipos.getString(j));
                                        break;
                                }
                                texto.append("</Data>");
                                texto.append("</Cell>\n");
                            }
                            texto.append("</Row>\n");
                            if(hayRc) {
                                incluirRc(texto, cEquipos, def, "TR2");
                            }
                        }
                        // Si además tiene medida de Tr3 se incluye también
                        boolean hayMedidaTr3 = (!def.getMedidaTr3().equals(""));
                        if (hayMedidaTr3) {
                            boolean hayRc = false;
                            texto.append("<Row>\n");
                            for (int j=1; j<=28; j++) {
                                String fecha, imagen;
                                switch (j){
                                    case 4: // Si es CT no se pone el tramo
                                        texto.append("<Cell ss:StyleID=\"s79\">");
                                        texto.append("<Data ss:Type=\"String\">");
                                        if (tipo.equals("Z")) {
                                            texto.append("");
                                        } else {
                                            texto.append(cEquipos.getString(j));
                                        }
                                        break;
                                    case 8: // Se corrige la fecha para que tenga el formato pedido por EDE
                                        texto.append("<Cell ss:StyleID=\"s79\">");
                                        texto.append("<Data ss:Type=\"String\">");
                                        fecha = cEquipos.getString(j);
                                        texto.append(corregirFecha(fecha));
                                        break;
                                    case 9:// Se incluye el codigo de la medida en lugar del codigo del defecto
                                        texto.append("<Cell ss:StyleID=\"s79\">");
                                        texto.append("<Data ss:Type=\"String\">");
                                        texto.append(equivalenciaMedidaCodigo(def.getCodigoDefecto(), def.getPatUnidas()));
                                        if (def.getCodigoDefecto().equals("T55D") || def.getCodigoDefecto().equals("T55C")) {
                                            hayRc = true;
                                        }
                                        break;
                                    case 12: // Se incluye la medida en lugar de las ocurrencias
                                        texto.append("<Cell ss:StyleID=\"s79\">");
                                        texto.append("<Data ss:Type=\"String\">");
                                        texto.append(adaptarMedida(def.getMedidaTr3()));
                                        break;
                                    case 23: // Se incluyen las observaciones del defecto además de las del equipo
                                        texto.append("<Cell ss:StyleID=\"s79\">");
                                        texto.append("<Data ss:Type=\"String\">");
                                        texto.append("TR3");
                                        if (!def.getObservaciones().equals("")) {
                                            texto.append(": " + def.getObservaciones());
                                        }
                                        break;
                                    case 24: // Se incluye también el vínculo a la foto en las medidas
                                        imagen = def.getFoto1();
                                        //texto.append("<Cell ss:StyleID=\"s102\" ss:HRef=\"Fotos\\" + imagen + "\">");
                                        texto.append("<Cell ss:StyleID=\"s102\" ss:HRef=\"" + imagen + "\">");
                                        texto.append("<Data ss:Type=\"String\">");
                                        texto.append(imagen);
                                        break;
                                    case 25: // Se incluye también el vínculo a la foto en las medidas
                                        imagen = def.getFoto2();
                                        //texto.append("<Cell ss:StyleID=\"s102\" ss:HRef=\"Fotos\\" + imagen + "\">");
                                        texto.append("<Cell ss:StyleID=\"s102\" ss:HRef=\"" + imagen + "\">");
                                        texto.append("<Data ss:Type=\"String\">");
                                        texto.append(imagen);
                                        break;
                                    default:
                                        texto.append("<Cell ss:StyleID=\"s79\">");
                                        texto.append("<Data ss:Type=\"String\">");
                                        texto.append(cEquipos.getString(j));
                                        break;
                                }
                                texto.append("</Data>");
                                texto.append("</Cell>\n");
                            }
                            texto.append("</Row>\n");
                            if (hayRc) {
                                incluirRc(texto, cEquipos, def, "TR3");
                            }
                        }
                    }
                }
            } while (cEquipos.moveToNext());
        }
        texto.append("</Table>\n");
        texto.append("</Worksheet>\n");
        cEquipos.close();

        // Hoja 2: Apoyos (Características Apoyos)
        Cursor cApoyos = dbRevisiones.solicitarDatosTodosApoyos(revision);
        texto.append(Auxiliar.ENCABEZADO_XML_HOJA2_A);
        texto.append(revision);
        texto.append(Auxiliar.ENCABEZADO_XML_HOJA2_B);
        if (cApoyos != null && cApoyos.moveToFirst()) {
            do {
                texto.append("<Row>\n");
                texto.append("<Cell ss:StyleID=\"s79\"><Data ss:Type=\"String\"></Data></Cell>\n");
                for (int i=1; i<=16; i++) {
                    texto.append("<Cell ss:StyleID=\"s79\">");
                    texto.append("<Data ss:Type=\"String\">");
                    switch (i) {
                        case 2: // Se aprovecha el campo para guardar las observaciones del kml de entrada y no se han de imprimir en el excel de salida
                            texto.append("");
                            break;
                        case 5: // Se toma una columna menos pues la primera columna se genera vacía
                            int col = cApoyos.getColumnIndex("TipoInstalacion");
                            String tipo = cApoyos.getString(col);
                            if (tipo.equals("Z")) {
                                texto.append("");
                            } else {
                                texto.append(cApoyos.getString(i));
                            }
                            break;
                        default:
                            texto.append(cApoyos.getString(i));
                            break;
                    }
                    texto.append("</Data>");
                    texto.append("</Cell>\n");
                }
                texto.append("</Row>\n");
            } while (cApoyos.moveToNext());
        }
        texto.append("</Table>\n");
        texto.append("</Worksheet>\n");
        cApoyos.close();

        // Hoja 3: Equipos no revisable
        Cursor cEquiposNoRevisables = dbRevisiones.solicitarDatosEquiposNoRevisables(revision);
        Revision rev = dbRevisiones.solicitarRevision(revision);
        texto.append("<Worksheet ss:Name=\"UNIDADES_REV_DATOS_REV_1.xls\">\n");
        texto.append("<Table>\n");
        texto.append(Auxiliar.ENCABEZADO_XML_HOJA3_A);
        // Datos cabecera hoja 3
        texto.append("<Row>\n<Cell ss:Index=\"2\" ss:MergeAcross=\"4\" ss:StyleID=\"m14596960\">" +
                "<Data ss:Type=\"String\">Metodología Utilizada:</Data></Cell>\n");
        texto.append("<Cell ss:StyleID=\"s79\"><Data ss:Type=\"String\">" +
                rev.getMetodologia() + "</Data></Cell>\n</Row>\n");
        texto.append("<Row>\n<Cell ss:Index=\"2\" ss:MergeAcross=\"4\" ss:StyleID=\"m14596960\">" +
                "<Data ss:Type=\"String\">NIF, Nombre y Apellidos de los Revisores:</Data></Cell>\n");
        texto.append("<Cell ss:StyleID=\"s79\"><Data ss:Type=\"String\">" +
                rev.getInspector1() + ", " + rev.getInspector2() + "</Data></Cell>\n</Row>\n");
        texto.append("<Row>\n<Cell ss:Index=\"2\" ss:MergeAcross=\"4\" ss:StyleID=\"m14596960\">" +
                "<Data ss:Type=\"String\">Nombre y apellidos del Colegiado que firmará la inspección:</Data></Cell>\n");
        texto.append("<Cell ss:StyleID=\"s79\"><Data ss:Type=\"String\">" +
                rev.getColegiado() + "</Data></Cell>\n</Row>\n");
        texto.append("<Row>\n<Cell ss:Index=\"2\" ss:MergeAcross=\"4\" ss:StyleID=\"m14596960\">" +
                "<Data ss:Type=\"String\">Equipos utilizados en la inspección (marca y modelo):</Data></Cell>\n");
        texto.append("<Cell ss:StyleID=\"s79\"><Data ss:Type=\"String\">" +
                rev.getEquiposUsados() + "</Data></Cell>\n</Row>\n");
        texto.append("<Row>\n<Cell ss:Index=\"2\" ss:MergeAcross=\"4\" ss:StyleID=\"m14596960\">" +
                "<Data ss:Type=\"String\">Código de Inspección: </Data></Cell>\n");
        texto.append("<Cell ss:StyleID=\"s79\"><Data ss:Type=\"String\">" +
                rev.getCodigoInspeccion() + "</Data></Cell>\n</Row>\n");
        texto.append("<Row></Row>\n<Row></Row>\n");
        texto.append(Auxiliar.ENCABEZADO_XML_HOJA3_B);
        // Datos cuerpo hoja 3
        if (cEquiposNoRevisables != null && cEquiposNoRevisables.moveToFirst()) {
            do {
                texto.append("<Row>\n");
                for (int i=1; i<=2; i++) {
                    texto.append("<Cell ss:StyleID=\"s79\">");
                    texto.append("<Data ss:Type=\"String\">");
                    texto.append(cEquiposNoRevisables.getString(i));
                    texto.append("</Data>");
                    texto.append("</Cell>\n");
                }
                texto.append("</Row>\n");
            } while (cEquiposNoRevisables.moveToNext());
        }
        texto.append("</Table>\n");
        texto.append("</Worksheet>\n");
        cEquiposNoRevisables.close();

        // Fin del archivo
        texto.append("</Workbook>");
        return texto.toString();
    }

    public static String incluirRc(StringBuffer texto, Cursor cursor, Defecto def, String trafo) {
        // TODO: En versiones antiguas quitar si da problemas o genera archivos erróneos
        texto.append("<Row>\n");
        for (int j=1; j<=28; j++) {
            String fecha, imagen;
            switch (j){
                case 4: // Como es CT no se pone el tramo
                    texto.append("<Cell ss:StyleID=\"s79\">");
                    texto.append("<Data ss:Type=\"String\">");
                    texto.append("");
                    break;
                case 8: // Se corrige la fecha para que tenga el formato pedido por EDE
                    texto.append("<Cell ss:StyleID=\"s79\">");
                    texto.append("<Data ss:Type=\"String\">");
                    fecha = cursor.getString(j);
                    texto.append(corregirFecha(fecha));
                    break;
                case 9: // Se incluye el codigo de la medida en lugar del codigo del defecto
                    texto.append("<Cell ss:StyleID=\"s79\">");
                    texto.append("<Data ss:Type=\"String\">");
                    texto.append("1013");
                    break;
                case 12: // Se incluye la medida en lugar de las ocurrencias
                    texto.append("<Cell ss:StyleID=\"s79\">");
                    texto.append("<Data ss:Type=\"String\">");
                    String rc;
                    if (trafo.equals("TR1")) {
                        rc = def.getRc1();
                    } else if (trafo.equals("TR2")) {
                        rc = def.getRc2();
                    } else {
                        rc = def.getRc3();
                    }
                    texto.append(rc);
                    break;
                case 23: // Se incluyen las observaciones del defecto además de las del equipo
                    texto.append("<Cell ss:StyleID=\"s79\">");
                    texto.append("<Data ss:Type=\"String\">");
                    texto.append("TR1");
                    if (!def.getObservaciones().equals("")) {
                        texto.append(": " + def.getObservaciones());
                    }
                    break;
                case 24: // Se incluye también el vínculo a la foto en las medidas
                    imagen = def.getFoto1();
                    texto.append("<Cell ss:StyleID=\"s102\" ss:HRef=\"" + imagen + "\">");
                    texto.append("<Data ss:Type=\"String\">");
                    texto.append(imagen);
                    break;
                case 25: // Se incluye también el vínculo a la foto en las medidas
                    imagen = def.getFoto2();
                    texto.append("<Cell ss:StyleID=\"s102\" ss:HRef=\"" + imagen + "\">");
                    texto.append("<Data ss:Type=\"String\">");
                    texto.append(imagen);
                    break;
                default:
                    texto.append("<Cell ss:StyleID=\"s79\">");
                    texto.append("<Data ss:Type=\"String\">");
                    texto.append(cursor.getString(j));
                    break;
            }
            texto.append("</Data>");
            texto.append("</Cell>\n");
        }
        texto.append("</Row>\n");

        return "";
    }

    /**
     * Genera el archivo KML con los datos recogidos hasta ese momento
     *
     * @param revision
     */
    public static void generarKML(String revision){
        String nombreArchivo = revision + ".kml";
        File archivo = crearArchivoSalida(revision, nombreArchivo);

        try {
            FileOutputStream fos = new FileOutputStream(archivo);
            fos.write(generarCuerpoKML(revision).getBytes());
            fos.close();
        } catch (Exception e) {
            Log.e ("Error RPR: ", e.toString());
        }

    }

    /**
     * Genera el cuerpo del KML
     *
     * @param revision
     * @return
     */
    public static String generarCuerpoKML(String revision) {
        StringBuffer texto = new StringBuffer();
        DBRevisiones dbRevisiones = new DBRevisiones(contexto);

        // Encabezado
        texto.append(Auxiliar.ENCABEZADO_KML);
        texto.append("<Document>\n"); // Apertura documento
        texto.append("<name>" + revision + "</name>\n");

        // Corrección inmediata
        texto.append("<open>1</open>\n<Folder>\n<name>" +
                "CORRECCIÓN INMEDIATA</name>\n<open>1</open>\n"); // Apertura carpeta "Corrección inmediata"
        texto.append("<Folder>\n<open>0</open>\n<name>CDs</name>\n"); // Apertura carpeta CDs
        texto.append(incluirCDsCorreccionInmediata(revision));
        texto.append("</Folder>\n<Folder>\n<open>0</open>\n<name>Apoyos</name>\n"); // Cierre carpeta CDs y apertura carpeta Apoyos
        texto.append(incluirApoyosCorreccionInmediata(revision));
        texto.append("</Folder>\n"); // Cierre carpeta Apoyos
        texto.append("</Folder>\n"); // Cierre carpeta "Corrección inmediata"

        // Defectos estratégicos
        texto.append("<open>1</open>\n<Folder>\n<name>" +
                "DEFECTOS ESTRATÉGICOS</name>\n<open>1</open>\n"); // Apertura carpeta "Defectos estratégicos"
        texto.append("<Folder>\n<open>0</open>\n<name>CDs</name>\n"); // Apertura carpeta CDs
        texto.append(incluirCDsDefectosEstrategicos(revision));
        texto.append("</Folder>\n<Folder>\n<open>0</open>\n<name>Apoyos</name>\n"); // Cierre carpeta CDs y apertura carpeta Apoyos
        texto.append(incluirApoyosDefectosEstrategicos(revision));
        texto.append("</Folder>\n"); // Cierre carpeta Apoyos
        texto.append("</Folder>\n"); // Cierre carpeta "Defectos estratégicos"

        // Defectos NO estratégicos
        texto.append("<open>1</open>\n<Folder>\n<name>" +
                "DEFECTOS NO ESTRATÉGICOS</name>\n<open>1</open>\n"); // Apertura carpeta "Defectos NO estratégicos"
        texto.append("<Folder>\n<open>0</open>\n<name>CDs</name>\n"); // Apertura carpeta CDs
        texto.append(incluirCDsDefectosNoEstrategicos(revision));
        texto.append("</Folder>\n<Folder>\n<open>0</open>\n<name>Apoyos</name>\n"); // Cierre carpeta CDs y apertura carpeta Apoyos
        texto.append(incluirApoyosDefectosNoEstrategicos(revision));
        texto.append("</Folder>\n"); // Cierre carpeta Apoyos
        texto.append("</Folder>\n"); // Cierre carpeta "Defectos NO estratégicos"

        //Posición elementos
        texto.append("<open>1</open>\n<Folder>\n<name>" +
                        "Posicion Elementos</name>\n<open>1</open>\n"); // Apertura carpeta "Posicion elementos"
        texto.append("<Folder>\n<open>0</open>\n<name>CDs</name>\n"); // Apertura carpeta CDs
        texto.append(incluirCDsKML(revision));
        texto.append("</Folder>\n<Folder>\n<open>0</open>\n<name>Apoyos</name>\n"); // Cierre carpeta CDs y apertura carpeta Apoyos
        texto.append(incluirApoyosKML(revision));
        texto.append("</Folder>\n"); // Cierre carpeta apoyos

        texto.append("</Folder>\n"); // Cierre carpeta "Posición elementos"
        // Cierre archivo KML
        texto.append("</Document>");
        texto.append("</kml>");

        return texto.toString();
    }

    /**
     * Se incluyen los CDs con defectos de corrección inmediata en el cuerpo del KML
     * @param revision
     * @return
     */
    public static String incluirCDsCorreccionInmediata (String revision) {
        StringBuffer texto = new StringBuffer();
        DBRevisiones dbRevisiones = new DBRevisiones(contexto);
        Vector<Apoyo> listaCDs = dbRevisiones.solicitarCDsCorreccionInmediata(revision);

        if (listaCDs != null) {
            for (int i=0; i<listaCDs.size(); i++) {
                Apoyo ct = listaCDs.elementAt(i);
                texto.append("<Placemark>\n<visibility>1</visibility>\n"); // Apertura equipo
                texto.append("<name>" + ct.getNombreEquipo() + "</name>\n"); // Nombre equipo
                texto.append(incluirDescripcion(ct));
                texto.append("<Style>\n<IconStyle>\n<color>ff0000ff</color>\n<scale>1.1</scale>\n" +
                        "<Icon>\n<href>http://maps.google.com/mapfiles/kml/paddle/C.png</href>\n</Icon>\n</IconStyle>\n" +
                        "<ListStyle>\n<ItemIcon>\n<href>http://maps.google.com/mapfiles/kml/paddle/C-lv.png</href>\n" +
                        "</ItemIcon>\n</ListStyle>\n</Style>\n"); // Estilo
                texto.append("<Point>\n<gx:drawOrder>1</gx:drawOrder>\n<coordinates>" +
                        ct.getLongitud() + "," + ct.getLatitud() + ",0</coordinates>\n</Point>\n"); //Coordenadas
                texto.append("</Placemark>\n"); // Cierre equipo
            }
        }

        return texto.toString();
    }

    /**
     * Se incluyen los apoyos con defectos de corrección inmediata en el cuerpo del KML
     * @param revision
     * @return
     */
    public static String incluirApoyosCorreccionInmediata (String revision) {
        StringBuffer texto = new StringBuffer();
        DBRevisiones dbRevisiones = new DBRevisiones(contexto);
        Vector<Apoyo> listaApoyos = dbRevisiones.solicitarApoyosCorreccionInmediata(revision);

        if (listaApoyos != null) {
            for (int i=0; i<listaApoyos.size(); i++) {
                Apoyo apoyo = listaApoyos.elementAt(i);
                Equipo equipo = dbRevisiones.solicitarEquipo(apoyo.getNombreRevision(),
                                            apoyo.getNombreEquipo(), apoyo.getCodigoTramo());
                texto.append("<Placemark>\n<visibility>1</visibility>\n"); // Apertura equipo
                texto.append("<name>" + apoyo.getNombreEquipo() + "</name>\n"); // Nombre equipo
                texto.append(incluirDescripcion(apoyo));
                texto.append("<Style>\n<IconStyle>\n<color>ff0000ff</color>\n<scale>1.1</scale>\n<Icon>\n" +
                        "<href>http://maps.google.com/mapfiles/kml/paddle/A.png</href>\n</Icon>\n</IconStyle>\n" +
                        "<ListStyle>\n<ItemIcon>\n<href>http://maps.google.com/mapfiles/kml/paddle/A-lv.png</href>\n" +
                        "</ItemIcon>\n</ListStyle>\n</Style>\n"); // Estilo
                texto.append("<Point>\n<gx:drawOrder>1</gx:drawOrder>\n<coordinates>" +
                        apoyo.getLongitud() + "," + apoyo.getLatitud() + ",0</coordinates>\n</Point>\n"); //Coordenadas
                texto.append("</Placemark>\n"); // Cierre equipo
            }
        }

        return texto.toString();
    }

    /**
     * Se incluyen los CDs con defectos estrategicos en el cuerpo del KML
     * @param revision
     * @return
     */
    public static String incluirCDsDefectosEstrategicos (String revision) {
        StringBuffer texto = new StringBuffer();
        DBRevisiones dbRevisiones = new DBRevisiones(contexto);
        Vector<Apoyo> listaCDs = dbRevisiones.solicitarCDsDefectosEstrategicos(revision);

        if (listaCDs != null) {
            for (int i=0; i<listaCDs.size(); i++) {
                Apoyo ct = listaCDs.elementAt(i);
                texto.append("<Placemark>\n<visibility>1</visibility>\n"); // Apertura equipo
                texto.append("<name>" + ct.getNombreEquipo() + "</name>\n"); // Nombre equipo
                texto.append(incluirDescripcion(ct));
                //texto.append("<description>" + ct.getObservaciones() + "</description>"); // Descripción
                texto.append("<Style>\n<IconStyle>\n<color>ff00ffff</color>\n<scale>1.1</scale>\n" +
                        "<Icon>\n<href>Iconos/ct_yellow.png</href>\n</Icon>\n</IconStyle>\n" +
                        "<ListStyle>\n<ItemIcon>\n<href>http://maps.google.com/mapfiles/kml/paddle/C-lv.png</href>\n" +
                        "</ItemIcon>\n</ListStyle>\n</Style>\n"); // Estilo
                texto.append("<Point>\n<gx:drawOrder>1</gx:drawOrder>\n<coordinates>" +
                        ct.getLongitud() + "," + ct.getLatitud() + ",0</coordinates>\n</Point>\n"); //Coordenadas
                texto.append("</Placemark>\n"); // Cierre equipo
            }
        }

        return texto.toString();
    }

    /**
     * Se incluyen los apoyos con defectos estrategicos en el cuerpo del KML
     * @param revision
     * @return
     */
    public static String incluirApoyosDefectosEstrategicos (String revision) {
        StringBuffer texto = new StringBuffer();
        DBRevisiones dbRevisiones = new DBRevisiones(contexto);
        Vector<Apoyo> listaApoyos = dbRevisiones.solicitarApoyosDefectosEstrategicos(revision);

        if (listaApoyos != null) {
            for (int i=0; i<listaApoyos.size(); i++) {
                Apoyo apoyo = listaApoyos.elementAt(i);
                Equipo equipo = dbRevisiones.solicitarEquipo(apoyo.getNombreRevision(),
                        apoyo.getNombreEquipo(), apoyo.getCodigoTramo());
                texto.append("<Placemark>\n<visibility>1</visibility>\n"); // Apertura equipo
                texto.append("<name>" + apoyo.getNombreEquipo() + "</name>\n"); // Nombre equipo
                texto.append(incluirDescripcion(apoyo));
/*
                texto.append("<description>Material: " + apoyo.getMaterial() +
                        "\nTraza/Tramo: " + apoyo.getCodigoTramo() + "\nObservaciones: " +
                        equipo.getObservaciones() + "</description>\n"); // Descripción
*/
                texto.append("<Style>\n<IconStyle>\n<color>ff00ffff</color>\n<scale>1.1</scale>\n<Icon>\n" +
                        "<href>Iconos/apoyo_yellow.png</href>\n</Icon>\n</IconStyle>\n" +
                        "<ListStyle>\n<ItemIcon>\n<href>http://maps.google.com/mapfiles/kml/paddle/A-lv.png</href>\n" +
                        "</ItemIcon>\n</ListStyle>\n</Style>\n"); // Estilo
                texto.append("<Point>\n<gx:drawOrder>1</gx:drawOrder>\n<coordinates>" +
                        apoyo.getLongitud() + "," + apoyo.getLatitud() + ",0</coordinates>\n</Point>\n"); //Coordenadas
                texto.append("</Placemark>\n"); // Cierre equipo
            }
        }

        return texto.toString();
    }

    /**
     * Se incluyen los CDs con defectos NO estrategicos en el cuerpo del KML
     * @param revision
     * @return
     */
    public static String incluirCDsDefectosNoEstrategicos (String revision) {
        StringBuffer texto = new StringBuffer();
        DBRevisiones dbRevisiones = new DBRevisiones(contexto);
        Vector<Apoyo> listaCDs = dbRevisiones.solicitarCDsDefectosNoEstrategicos(revision);

        if (listaCDs != null) {
            for (int i=0; i<listaCDs.size(); i++) {
                Apoyo ct = listaCDs.elementAt(i);
                texto.append("<Placemark>\n<visibility>1</visibility>\n"); // Apertura equipo
                texto.append("<name>" + ct.getNombreEquipo() + "</name>\n"); // Nombre equipo
                texto.append(incluirDescripcion(ct));
                //texto.append("<description>" + ct.getObservaciones() + "</description>"); // Descripción
                texto.append("<Style>\n<IconStyle>\n<color>ffff0000</color>\n<scale>1.1</scale>\n" +
                        "<Icon>\n<href>Iconos/ct_blue.png</href>\n</Icon>\n</IconStyle>\n" +
                        "<ListStyle>\n<ItemIcon>\n<href>http://maps.google.com/mapfiles/kml/paddle/C-lv.png</href>\n" +
                        "</ItemIcon>\n</ListStyle>\n</Style>\n"); // Estilo
                texto.append("<Point>\n<gx:drawOrder>1</gx:drawOrder>\n<coordinates>" +
                        ct.getLongitud() + "," + ct.getLatitud() + ",0</coordinates>\n</Point>\n"); //Coordenadas
                texto.append("</Placemark>\n"); // Cierre equipo
            }
        }

        return texto.toString();
    }

    /**
     * Se incluyen los apoyos con defectos NO estrategicos en el cuerpo del KML
     * @param revision
     * @return
     */
    public static String incluirApoyosDefectosNoEstrategicos (String revision) {
        StringBuffer texto = new StringBuffer();
        DBRevisiones dbRevisiones = new DBRevisiones(contexto);
        Vector<Apoyo> listaApoyos = dbRevisiones.solicitarApoyosDefectosNoEstrategicos(revision);

        if (listaApoyos != null) {
            for (int i=0; i<listaApoyos.size(); i++) {
                Apoyo apoyo = listaApoyos.elementAt(i);
                Equipo equipo = dbRevisiones.solicitarEquipo(apoyo.getNombreRevision(),
                        apoyo.getNombreEquipo(), apoyo.getCodigoTramo());
                texto.append("<Placemark>\n<visibility>1</visibility>\n"); // Apertura equipo
                texto.append("<name>" + apoyo.getNombreEquipo() + "</name>\n"); // Nombre equipo
                texto.append(incluirDescripcion(apoyo));
/*
                texto.append("<description>Material: " + apoyo.getMaterial() +
                        "\nTraza/Tramo: " + apoyo.getCodigoTramo() + "\nObservaciones: " +
                        equipo.getObservaciones() + "</description>\n"); // Descripción
*/
                texto.append("<Style>\n<IconStyle>\n<color>ffff0000</color>\n<scale>1.1</scale>\n<Icon>\n" +
                        "<href>Iconos/apoyo_blue.png</href>\n</Icon>\n</IconStyle>\n" +
                        "<ListStyle>\n<ItemIcon>\n<href>http://maps.google.com/mapfiles/kml/paddle/A-lv.png</href>\n" +
                        "</ItemIcon>\n</ListStyle>\n</Style>\n"); // Estilo
                texto.append("<Point>\n<gx:drawOrder>1</gx:drawOrder>\n<coordinates>" +
                        apoyo.getLongitud() + "," + apoyo.getLatitud() + ",0</coordinates>\n</Point>\n"); //Coordenadas
                texto.append("</Placemark>\n"); // Cierre equipo
            }
        }

        return texto.toString();
    }

    /**
     * Se incluyen los CDs en el cuerpo del KML
     * @param revision
     * @return
     */
    public static String incluirCDsKML (String revision) {
        StringBuffer texto = new StringBuffer();
        DBRevisiones dbRevisiones = new DBRevisiones(contexto);
        Vector<Apoyo> listaCDs = dbRevisiones.solicitarDatosCDs(revision);

        if (listaCDs != null) {
            for (int i=0; i<listaCDs.size(); i++) {
                Apoyo ct = listaCDs.elementAt(i);
                texto.append("<Placemark>\n<visibility>0</visibility>\n"); // Apertura equipo
                texto.append("<name>" + ct.getNombreEquipo() + "</name>\n"); // Nombre equipo
//                Equipo equipo = dbRevisiones.solicitarEquipo(revision, ct.getNombreEquipo(), ct.getCodigoTramo());
//                if (equipo != null) {
                    texto.append(incluirDescripcion(ct));
//                }
                texto.append("<Style>\n<IconStyle>\n<color>ff00ff00</color>\n<scale>1.1</scale>\n" +
                        "<Icon>\n<href>http://maps.google.com/mapfiles/kml/paddle/C.png</href>\n</Icon>\n</IconStyle>\n" +
                        "<ListStyle>\n<ItemIcon>\n<href>http://maps.google.com/mapfiles/kml/paddle/C-lv.png</href>\n" +
                        "</ItemIcon>\n</ListStyle>\n</Style>\n"); // Estilo
                texto.append("<Point>\n<gx:drawOrder>1</gx:drawOrder>\n<coordinates>" +
                        ct.getLongitud() + "," + ct.getLatitud() + ",0</coordinates>\n</Point>\n"); //Coordenadas
                texto.append("</Placemark>\n"); // Cierre equipo
            }
        }

        return texto.toString();
    }

    /**
     * Se incluyen los apoyos en el cuerpo del KML
     *
     * @param revision
     * @return
     */
    public static String incluirApoyosKML(String revision) {
        StringBuffer texto = new StringBuffer();
        DBRevisiones dbRevisiones = new DBRevisiones(contexto);
        Vector<Apoyo> listaApoyos = dbRevisiones.solicitarDatosApoyos(revision);

        if (listaApoyos != null) {
            for (int i=0; i<listaApoyos.size(); i++) {
                Apoyo apoyo = listaApoyos.elementAt(i);
//                Equipo equipo = dbRevisiones.solicitarEquipo(apoyo.getNombreRevision(),
//                        apoyo.getNombreEquipo(), apoyo.getCodigoTramo());
//                if (equipo != null) {
                    texto.append("<Placemark>\n<visibility>0</visibility>\n"); // Apertura apoyo
                    texto.append("<name>" + apoyo.getNombreEquipo() + "</name>\n"); // Nombre apoyo
                    texto.append(incluirDescripcion(apoyo));
                    texto.append("<Style>\n<IconStyle>\n<color>ff00ff00</color>\n<scale>1.1</scale>\n<Icon>\n" +
                            "<href>http://maps.google.com/mapfiles/kml/paddle/A.png</href>\n</Icon>\n</IconStyle>\n" +
                            "<ListStyle>\n<ItemIcon>\n<href>http://maps.google.com/mapfiles/kml/paddle/A-lv.png</href>\n" +
                            "</ItemIcon>\n</ListStyle>\n</Style>\n"); // Estilo
                    texto.append("<Point>\n<gx:drawOrder>1</gx:drawOrder>\n<coordinates>" + apoyo.getLongitud() + "," +
                            apoyo.getLatitud() + ",0 </coordinates>\n</Point>\n");
                    texto.append("</Placemark>\n"); // Cierre equipo
//                }
            }
        }

        return texto.toString();
    }

    /**
     * Se incluye la descripción y fotos (si las hubiera) en el equipo
     * @param apoyo
     * @return
     */
    public static String incluirDescripcion(Apoyo apoyo){
        StringBuffer texto = new StringBuffer();
        DBRevisiones dbRevisiones = new DBRevisiones(contexto);
        Equipo equipo = dbRevisiones.solicitarEquipo(apoyo.getNombreRevision(),
                apoyo.getNombreEquipo(), apoyo.getCodigoTramo());

        // TODO: Modificar observaciones en función del tipo de apoyo
        // Observaciones incluye los datos a mostrar separados por "---";
        String datosObs = apoyo.getObservaciones();
        String obs, foto1, foto2;
        if (datosObs.contains("---")) {
            obs = datosObs.substring(0, datosObs.indexOf("---"));
            foto1 = datosObs.substring((datosObs.indexOf("---") + 3), datosObs.lastIndexOf("---"));
            foto2 = datosObs.substring(datosObs.lastIndexOf("---") + 3);
        } else {
            obs = datosObs;
            foto1 = "";
            foto2 = "";
        }
        texto.append("<description>\n<![CDATA[<div align=\"left\">\n<p>"
                + obs + "\n</p>\n"); // Se incluyen las observaciones
        // Se incluyen las fotos si hay
        if (!foto1.equals("")) {
            texto.append("<table>\n<tr>\n<td>\n");
            texto.append("<img src=\"" + foto1); // Se asocia la foto
            texto.append("\" width=\"" + ANCHOFOTO + "\" height=\"" + ALTOFOTO + "\">"); // Se asigna alto y ancho a la foto
            if (!foto2.equals("")) {
                texto.append("<td>\n");
                texto.append("<img src=\"" + foto2); // Se asocia la foto
                texto.append("\" width=\"" + ANCHOFOTO + "\" height=\"" + ALTOFOTO + "\">"); // Se asigna alto y ancho a la foto
            }
            texto.append("</td>\n</tr>\n</table>\n");
        }
        texto.append("</div>]]>\n</description>\n");

        return texto.toString();
    }

    /**
     * Genera el archivo defectos.txt con los datos recogidos hasta ese momento
     *
     * @param revision
     */
    public static void generarArchivoDefectosTXT(String revision){
        String nombreArchivo = revision + "_defectos.txt";
        File archivo = crearArchivoSalida(revision, nombreArchivo);

        try {
            FileOutputStream fos = new FileOutputStream(archivo);
            fos.write(generarCuerpoDefectosTXT(revision).getBytes());
            fos.close();
        } catch (Exception e) {
            Log.e ("Error RPR: ", e.toString());
            //Aplicacion.print("ErrorRPR: Error al generar el archivo de defectos " + e.toString());
        }

    }

    /**
     * Genera el cuerpo del archivo defectos.txt
     *
     * @param revision
     * @return
     */
    public static String generarCuerpoDefectosTXT(String revision) {
        StringBuffer texto = new StringBuffer();
        DBRevisiones dbRevisiones = new DBRevisiones(contexto);
        Cursor cDefectos = dbRevisiones.solicitarDatosDefectosPorRevision(revision);

        if (cDefectos != null && cDefectos.getCount()>0) {
            cDefectos.moveToFirst();
            do {
                String nombreEquipo = cDefectos.getString(1);
                Cursor cUTM = dbRevisiones.solicitarUTMEquipo(revision, nombreEquipo);
                texto.append(cDefectos.getString(0) + ";"); // NombreRevision
                texto.append(nombreEquipo + ";"); // NombreEquipo
                texto.append(cDefectos.getString(2) + ";"); // CodigoDefecto
                if (cUTM != null && cUTM.moveToFirst()) {
                    for (int i=0; i<cUTM.getColumnCount(); i++) {
                        String s = cUTM.getString(i);
                        if (s.contains(",")) {
                            s = s.substring(0, s.lastIndexOf(","));
                        }
                        texto.append(s + ";");
                    }
                    cUTM.close();
                }
                for (int j=3; j<(cDefectos.getColumnCount()-1); j++) {
                    texto.append(cDefectos.getString(j) + ";");
                }
                texto.append("\\n");
            } while (cDefectos.moveToNext());
            cDefectos.close();
        }

        return texto.toString();
    }

    /**
     * Genera el archivo equipos.txt con los datos recogidos hasta ese momento
     *
     * @param revision
     */
    public static void generarArchivoEquiposTXT(String revision) {
        String nombreArchivo = revision + "_equipos.txt";
        File archivo = crearArchivoSalida(revision, nombreArchivo);

        try {
            FileOutputStream fos = new FileOutputStream(archivo);
            fos.write(generarCuerpoEquiposTXT(revision).getBytes());
            fos.close();
        } catch (Exception e) {
            Log.e ("Error RPR: ", e.toString());
            Aplicacion.print("ErrorRPR: Error al generar el archivo de equipos " + e.toString());
        }

    }

    /**
     * Genera el cuerpo del archivo equipos.txt
     *
     * @param revision
     * @return
     */
    public static String generarCuerpoEquiposTXT (String revision) {
        StringBuffer texto = new StringBuffer();
        DBRevisiones dbRevisiones = new DBRevisiones(contexto);
        Vector<Apoyo> listaApoyos = dbRevisiones.solicitarListaApoyos(revision);

        if (listaApoyos != null) {
            for (int i=0; i<listaApoyos.size(); i++) {
                Apoyo apoyo = listaApoyos.elementAt(i);
                texto.append(apoyo.getNombreRevision() + ";");
                texto.append(apoyo.getNombreEquipo() + ";");
                texto.append(apoyo.getHusoApoyo() + ";");
                texto.append(apoyo.getCoordenadaXUTMApoyo() + ";");
                texto.append(apoyo.getCoordenadaYUTMApoyo() + ";");
                texto.append(apoyo.getLongitud() + ";");
                texto.append(apoyo.getLatitud() + ";");
                Cursor cFoto = dbRevisiones.solicitarItem(DBRevisiones.TABLA_EQUIPOS,
                        "DocumentosAsociar", "NombreEquipo", apoyo.getNombreEquipo());
                 if (cFoto != null && cFoto.moveToFirst()) {
                     String foto = cFoto.getString(0);
                    texto.append(foto + ";");
                    cFoto.close();
                 }
                texto.append("\\n");
            }
        }

        return texto.toString();
    }

    public static void moverFotos(Revision revision){
        String rutaEntrada = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/";
        File f = recuperarArchivo(rutaEntrada, revision.getNombre());

        if (f != null) {
            rutaEntrada = rutaEntrada + revision.getNombre();
            String nombreRevision = revision.getNombre();
            moverFotosDelDirectorio(rutaEntrada, nombreRevision);
            try {
                // Despues de mover las fotos se borran los directorios
                f.delete();
            } catch (Exception e) {
                Log.e(Aplicacion.TAG, "Error al mover las fotos: " + e.toString());
            }
        }

    }

    /**
     * Metodo recursivo para mover todas las fotos de la ruta dada
     * @param ruta
     */
    public static void moverFotosDelDirectorio (String ruta, String nombreRevision) {
        File f = new File(ruta);
        File[] files = f.listFiles();
        if (files != null) {
            for (int i=0; i<files.length; i++) {
                File archivo = files[i];
                if(archivo.isDirectory()) {
                    try {
                        File[] fDir = archivo.listFiles();
                        if (fDir.length == 0) {
                            archivo.delete();
                        } else {
                            moverFotosDelDirectorio(archivo.getPath(), nombreRevision);
                            archivo.delete();
                        }
                    } catch (Exception e) {

                    }
                } else {
                    String rutaDest = Environment.getExternalStoragePublicDirectory
                                        (Environment.DIRECTORY_DOWNLOADS) + DIRECTORIO_SALIDA_BD +
                                            nombreRevision +
                                            "/";
                                            //"/Fotos/";
                    File dirDest = new File(rutaDest);
                    if (!dirDest.exists()) {
                        dirDest.mkdirs();
                    }
                    File fDest = new File(rutaDest + archivo.getName());
                    archivo.renameTo(fDest);
                }
            }
        }
    }

    public static void borrarFotos(Revision revision) {
        String ruta = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/";
        File f = recuperarArchivo(ruta, revision.getNombre());

        if (f != null) {
            borrarDirectorio(ruta + "/" + revision.getNombre() + "/");
            try {
                boolean borrado = f.delete();
                if (borrado) Aplicacion.print("Borrado");
            } catch (Exception e) {
                Log.e(Aplicacion.TAG, "Error al borrar las fotos: " + e.toString());
            }
        }

    }

    /**
     * Metodo recursivo para borrar todas las fotos de la ruta dada
     * @param ruta
     */
    public static void borrarDirectorio (String ruta) {
        File f = new File(ruta);
        File[] files = f.listFiles();
        if (files != null) {
            for (int i=0; i<files.length; i++) {
                File archivo = files[i];
                if(archivo.isDirectory()) {
                    try {
                        File[] fDir = archivo.listFiles();
                        if (fDir.length == 0) {
                            archivo.delete();
                        } else {
                            borrarDirectorio(archivo.getPath());
                            archivo.delete();
                        }
                    } catch (Exception e) {

                    }
                } else {
                    archivo.delete();
                }
            }
        }
    }

    public static String equivalenciaMedidaCodigo (String codigoDefecto, String patUnidas) {
        String medida;
        switch (codigoDefecto){
            case "T22B":
                medida = "1000";
                break;
            case "T53D":
                if (patUnidas.equals(Aplicacion.SI)) {
                    medida = "1010";
                } else {
                    medida = "1012";
                }
                break;
            case "T55D":
                medida = "1014";
//                medida = "1013";
                break;
            case "T62D":
                medida = "1011";
                break;
            case "T53C":
                medida = "1012";
                break;
            case "T55C":
                medida = "1014";
//                medida = "1013";
                break;
            case "T62C":
                medida = "1011";
                break;
            default:
                medida = "";
                break;
        }

        return medida;
    }

    public static String adaptarMedida(String medida) {
        String resultado;
        if (medida.contains(".")) {
            resultado = medida.replace(".", ",");
        } else {
            resultado = medida;
        }

        return resultado;
    }

    public static void convertirCoordenadasApoyos (String revision) {
        DBRevisiones dbRevisiones = new DBRevisiones(contexto);
        Vector<Apoyo> listaApoyos = dbRevisiones.solicitarListaApoyos(revision);

        try {
            for (int i=0; i<listaApoyos.size(); i++) {
                Apoyo apoyo = listaApoyos.elementAt(i);

                // Conversión de las coordenadas
                // ***************************************************************************************
                Double lat, lng;
                try {
                    lat = Double.parseDouble(apoyo.getLatitud());
                    lng = Double.parseDouble(apoyo.getLongitud());
                } catch (NumberFormatException e) {
                    Log.e (TAG, "(Aplicacion.convertirCoordenadas) Error al convertir coordenadas a Double:" +
                            " " + e.toString());
                    lat = 0d;
                    lng = 0d;
                }
                InfoApoyoEndesa puntoGPS84 = new InfoApoyoEndesa();

                puntoGPS84.setDatum(InfoApoyoEndesa.WGS84);
                puntoGPS84.setLatitud(lat);
                puntoGPS84.setLongitud(lng);

                // se convierten las coordenadas de WGS84 a ED50
                InfoApoyoEndesa puntoGPS50 = InfoApoyoEndesa.converToGPS84toGPS50(puntoGPS84);
                puntoGPS50.setDatum(InfoApoyoEndesa.INTERNATIONAL11924);

                // Se convierten las coordenadas de ED50 a UTM
                InfoApoyoEndesaUTM puntoUTM = InfoApoyoEndesa.convertToGPSToUTM(puntoGPS50);
                // ***************************************************************************************

                /*Actualizaación de datos en el apoyo correspondiente
                    Se redondean los datos para darle el formato solicitado por Endesa*/
                Long x, y, huso;
                huso = Math.round(puntoUTM.getHuso());
                /*En caso de que las coordenadas sean (0, 0) no se convierten a UTM al entenderse que
                    nos e han recogido coordenadas y evitar así que se haga una conversión incoherente*/
                if((lat == 0) && (lng == 0)) {
                    x = 0L;
                    y = 0L;
                } else { // Si las coordenadas son diferentes de (0, 0) se realiza la conversión a UTM
                    x = Math.round(puntoUTM.getUTMx());
                    y = Math.round(puntoUTM.getUTMy());
                }

                // Se guardan las coordenadas en la BDD
                dbRevisiones.actualizarItemEquipoApoyo(DBRevisiones.TABLA_APOYOS, "CoordenadaXUTMApoyo",
                        x.toString(), apoyo.getNombreRevision(),
                        apoyo.getNombreEquipo(), apoyo.getCodigoTramo());
                dbRevisiones.actualizarItemEquipoApoyo(DBRevisiones.TABLA_APOYOS, "CoordenadaYUTMApoyo",
                        y.toString(), apoyo.getNombreRevision(),
                        apoyo.getNombreEquipo(), apoyo.getCodigoTramo());
                dbRevisiones.actualizarItemEquipoApoyo(DBRevisiones.TABLA_APOYOS, "HusoApoyo", huso.toString(),
                        apoyo.getNombreRevision(), apoyo.getNombreEquipo(), apoyo.getCodigoTramo());
            }
        } catch (Exception e) {
            Log.e (TAG, "(convertirCoordenadas) Error al convertir coordenadas: " + e.toString());
        }

    }

}
