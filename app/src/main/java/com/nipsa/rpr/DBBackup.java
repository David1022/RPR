package com.nipsa.rpr;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.Vector;

/**
 * Created by David.Mendano on 28/08/2017.
 */

public class DBBackup extends SQLiteOpenHelper{

    public Context contexto;

    public static String DATABASE_NAME = "DBBackup";
    public static final String TABLA_REVISIONES = "Revisiones";
    public static final String TABLA_EQUIPOS = "Equipos";
    public static final String TABLA_APOYOS = "Apoyos";
    public static final String TABLA_NO_REVISABLE = "EquiposNoRevisables";
    public static final String TABLA_DEFECTOS = "Defectos";
    public static final String TABLA_TRAMOS = "Tramos";
    public static final String DIRECTORIO_SALIDA_BD = "/RPR/OUTPUT/";

    public DBRevisiones dbRevisiones;

    private final String[] COL_TABLA_REVISIONES = {"_id" , "Nombre", "Estado", "Inspector1", "Inspector2",
            "Colegiado", "EquipoUsado", "Metodologia", "CodigoNipsa", "NumeroTrabajo",
            "CodigoInspeccion"};

    private final String[] COL_TABLA_EQUIPOS = {"_id", "TipoInstalacion", "CodigoBDE","DescripcionInstalacion", "PosicionTramo",
            "CodigoTramo", "DescripcionTramo", "EquipoApoyo", "FechaInspeccion", "DefectoMedida",
            "DescDefectoMedida", "Crit", "OcurrenciasMedida", "EstadoInst", "TrabajoInspeccion",
            "Valoracion", "Importe", "LímiteCorreccion", "TrabajoCorreccion", "FechaCorreccion",
            "D", "C", "CodigoInspeccion", "Observaciones", "DocumentosAsociar",
            "DescripcionDocumentos", "FechaAlta", "TPL", "KmAéreos", "Estado",
            "NombreRevision", "NombreEquipo", "HayCruces", "HayManiobra", "HayConversion",
            "TipoEquipo"};

    private final String[] COL_TABLA_APOYOS = {"_id", "CodigoApoyo", "Observaciones", "MaxTension", "ComboMaxTension",
            "Material", "ComboMaterial", "Estructura", "ComboEstructura", "AlturaApoyo",
            "HusoApoyo", "HusoCombo", "CoordenadaXUTMApoyo", "CoordenadaYUTMApoyo", "TipoInstalacion",
            "NombreInstalacion", "CodigoTramo", "Latitud", "Longitud", "NombreRevision",
            "NombreEquipo"};

    private final String[] COL_TABLA_APOYO_NO_REVISABLE = {"_id", "CodigoApoyoCT", "Motivo", "NombreRevision", "Tramo"};

    private final String[] COL_TABLA_DEFECTOS = {"_id", "NombreEquipo", "NombreRevision", "CodigoDefecto", "Foto1",
            "Foto2", "Medida", "Observaciones", "Ocurrencias", "Latitud",
            "Longitud", "EsDefecto", "Corregido", "FechaCorreccion", "Tramo",
            "MedidaTr2", "MedidaTr3"};

    private final String[] COL_TABLA_TRAMOS = {"_id", "NombreRevision", "NombreTramo", "Longitud", "Latitud"};

    public DBBackup(Context contexto) {
        super(contexto, DATABASE_NAME, null, 1);
        this.contexto = contexto;
        this.dbRevisiones = new DBRevisiones(contexto);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        try {
            // Se crea la tabla de revisiones
            StringBuffer instruccionCrearTablaRevisiones = new StringBuffer("CREATE TABLE " + TABLA_REVISIONES
                    + (" (_id INTEGER PRIMARY KEY AUTOINCREMENT"));
            for (int i=1; i<COL_TABLA_REVISIONES.length; i++) {
                instruccionCrearTablaRevisiones.append(", " + COL_TABLA_REVISIONES[i] + " TEXT");
            }
            instruccionCrearTablaRevisiones.append(")");
            db.execSQL(instruccionCrearTablaRevisiones.toString());

            // Se crea la tabla de equipos
            StringBuffer instruccionCrearTablaEquipos = new StringBuffer("CREATE TABLE " + TABLA_EQUIPOS
                    + (" (_id INTEGER PRIMARY KEY AUTOINCREMENT"));
            for (int i=1; i<COL_TABLA_EQUIPOS.length; i++) {
                instruccionCrearTablaEquipos.append(", " + COL_TABLA_EQUIPOS[i] + " TEXT");
            }
            instruccionCrearTablaEquipos.append(")");
            db.execSQL(instruccionCrearTablaEquipos.toString());

            // Se crea la tabla de apoyos
            StringBuffer instruccionCrearTablaApoyos = new StringBuffer("CREATE TABLE " + TABLA_APOYOS
                    + (" (_id INTEGER PRIMARY KEY AUTOINCREMENT"));
            for (int i=1; i<COL_TABLA_APOYOS.length; i++) {
                instruccionCrearTablaApoyos.append(", " + COL_TABLA_APOYOS[i] + " TEXT");
            }
            instruccionCrearTablaApoyos.append(")");
            db.execSQL(instruccionCrearTablaApoyos.toString());

            // Se crea la tabla de nuevos apoyos
            StringBuffer instruccionCrearTablaApoyoNoRevisable = new StringBuffer("CREATE TABLE " + TABLA_NO_REVISABLE
                    + (" (_id INTEGER PRIMARY KEY AUTOINCREMENT"));
            for (int i=1; i<COL_TABLA_APOYO_NO_REVISABLE.length; i++) {
                instruccionCrearTablaApoyoNoRevisable.append(", " + COL_TABLA_APOYO_NO_REVISABLE[i] + " TEXT");
            }
            instruccionCrearTablaApoyoNoRevisable.append(")");
            db.execSQL(instruccionCrearTablaApoyoNoRevisable.toString());

            // Se crea la tabla de defectos
            StringBuffer instruccionCrearTablaDefectos = new StringBuffer("CREATE TABLE " + TABLA_DEFECTOS
                    + (" (_id INTEGER PRIMARY KEY AUTOINCREMENT"));
            for (int i=1; i<COL_TABLA_DEFECTOS.length; i++) {
                instruccionCrearTablaDefectos.append(", " + COL_TABLA_DEFECTOS[i] + " TEXT");
            }
            instruccionCrearTablaDefectos.append(")");
            db.execSQL(instruccionCrearTablaDefectos.toString());

            // Se crea la tabla de tramos
            StringBuffer instruccionCrearTablaTramos = new StringBuffer("CREATE TABLE " + TABLA_TRAMOS
                    + (" (_id INTEGER PRIMARY KEY AUTOINCREMENT"));
            for (int i=1; i<COL_TABLA_TRAMOS.length; i++) {
                instruccionCrearTablaTramos.append(", " + COL_TABLA_TRAMOS[i] + " TEXT");
            }
            instruccionCrearTablaTramos.append(")");
            db.execSQL(instruccionCrearTablaTramos.toString());

        }catch (Exception e){
            Log.e(Aplicacion.TAG, "Error al crear la base de datos: " + e.toString());
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void crearBackup (String revision) {
        borrarElementosTabla(TABLA_REVISIONES);
        borrarElementosTabla(TABLA_EQUIPOS);
        borrarElementosTabla(TABLA_APOYOS);
        borrarElementosTabla(TABLA_NO_REVISABLE);
        borrarElementosTabla(TABLA_TRAMOS);
        borrarElementosTabla(TABLA_DEFECTOS);

        incluirRevision(revision);
        incluirElemento(revision, TABLA_EQUIPOS);
        incluirElemento(revision, TABLA_APOYOS);
        incluirElemento(revision, TABLA_NO_REVISABLE);
        //incluirElemento(revision, TABLA_TRAMOS);
        incluirElemento(revision, TABLA_DEFECTOS);

        crearCopiaDB(revision);
    }

    private void borrarElementosTabla(String tabla) {
        String inst = "DELETE FROM " + tabla;
        SQLiteDatabase db = getWritableDatabase();
        try {
            db.execSQL(inst);
        } catch (SQLException e) {
            Log.e(Aplicacion.TAG, "Error al borrar tabla: " + e.toString());
        }
    }

    private void incluirRevision(String nombreRevision) {
        Revision revision = dbRevisiones.solicitarRevision(nombreRevision);
        Vector<String> lista = revision.getRevisionPorLista();
        StringBuffer inst = new StringBuffer();

        inst.append("INSERT INTO " + TABLA_REVISIONES + " VALUES (null");
        for (int i =0; i<lista.size(); i++) {
            inst.append(", '" + lista.elementAt(i) + "'");
        }
        inst.append(")");

        SQLiteDatabase db = getWritableDatabase();
        try {
            db.execSQL(inst.toString());
        } catch (SQLException e) {
            Log.e(Aplicacion.TAG, "Error al incluir revision: " + e.toString());
        }

    }

    private void incluirElemento(String nombreRevision, String tabla) {
        Cursor cursor = dbRevisiones.solicitarBackup(nombreRevision, tabla);
        if (cursor.moveToFirst()){
            do {
                StringBuffer inst = new StringBuffer();
                inst.append("INSERT INTO " + tabla + " VALUES (null");
                for (int i=1; i<cursor.getColumnCount(); i++) {
                    inst.append(", '" + cursor.getString(i) + "'");
                }
                inst.append(")");
                SQLiteDatabase db = getWritableDatabase();
                try {
                    db.execSQL(inst.toString());
                } catch (SQLException e) {
                    Log.e(Aplicacion.TAG, "Error al incluir elemento: " + e.toString());
                }
            } while (cursor.moveToNext());
        }
        cursor.close();

    }

    /**
     * Metodo para crear una copia exacta de la base de datos en un directorio accesible externamente
     */
    private void crearCopiaDB(String nombreRevision) {
        String timeStamp = Aplicacion.fechaHoraActual();

        // Ruta fichero interno BD
        final String inFileName = "/data/data/com.nipsa.rpr/databases/" + DATABASE_NAME;
        try {
            File dbFile = new File(inFileName);
            FileInputStream fis = new FileInputStream(dbFile);
            // Ruta fichero salida BD
            String directorio = Environment.getExternalStoragePublicDirectory
                    (Environment.DIRECTORY_DOWNLOADS) + DIRECTORIO_SALIDA_BD + nombreRevision + "/";

            File d = new File(directorio);
            if (!d.exists()) {
                d.mkdirs();
            }
            // Nombre fichero salida BD
            String outFileName = directorio + "/" + nombreRevision +
                    // "_" + timeStamp +
                    "_DBBackup.sqlite";

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

}