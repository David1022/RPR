package com.nipsa.rpr;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.Vector;
import java.util.concurrent.Exchanger;

public class DBRevisiones extends SQLiteOpenHelper {

    public Context contexto;

    public static final String DATABASE_NAME = "dbrpr";
    public static final String TABLA_REVISIONES = "Revisiones";
    public static final String TABLA_EQUIPOS = "Equipos";
    public static final String TABLA_APOYOS = "Apoyos";
    public static final String TABLA_NO_REVISABLE = "EquiposNoRevisables";
    public static final String TABLA_DEFECTOS = "Defectos";
    public static final String TABLA_TRAMOS = "Tramos";
    private final int NUM_COL_EQUIPOS = 28;
    private final int NUM_COL_APOYOS = 18;
    private final int NUM_COL_APOYOS_NO_REVISABLES = 2;

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
                                                    "MedidaTr2", "MedidaTr3", "PaTUnidas", "Rc1", "Rc2", "Rc3"};

    private final String[] COL_TABLA_TRAMOS = {"_id", "Orden", "NombreRevision", "NombreTramo", "Longitud", "Latitud"};

    public DBRevisiones(Context contexto) {
        super(contexto, DATABASE_NAME, null, 1);
        this.contexto = contexto;
        //dbBackup = new DBBackup(contexto);
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
                    + (" (_id INTEGER PRIMARY KEY AUTOINCREMENT, Orden INTEGER"));
            for (int i=2; i<COL_TABLA_TRAMOS.length; i++) {
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

    /**
     *
     * @param nombreRevision
     * @return true si la revision ya se ha incluido en la BDD
     */
    public boolean existeRevision(String nombreRevision) {

        if ((nombreRevision.contains(".xml")) || (nombreRevision.contains(".XML"))) {
            nombreRevision = nombreRevision.substring(0, nombreRevision.lastIndexOf("."));
        }
        Revision revision = solicitarRevision(nombreRevision);

        return (revision != null);
/*
        boolean existeRevision = false;
        String instruccion = "SELECT Nombre FROM '" + TABLA_REVISIONES + "' WHERE Nombre = '" + nombreRevision + "'";

        try {
            SQLiteDatabase db = getReadableDatabase();
            Cursor cursor = db.rawQuery(instruccion, null);
            if ((cursor !=null) && (cursor.getCount() > 0)) {
                existeRevision = true;
            }
            cursor.close();
        }catch (Exception e){
            Log.e(Aplicacion.TAG, "Error al comprobar si existe Revisión: " + e.toString());
            Aplicacion.print(Aplicacion.TAG + "Error al comprobar si existe Revisión: " + e.toString());
        }

        return existeRevision;
*/

    }

    /**
     *
     * @param revision
     * @param estado
     */
    public void incluirRevision (String revision, String estado) {
        String instruccion = "INSERT INTO " + TABLA_REVISIONES +
                " VALUES ( null, '" + revision + "', '" + estado + "', '', '', '', '', '', '', '', '')";
        try {
            SQLiteDatabase db = getWritableDatabase();
            db.execSQL(instruccion);
        }catch (Exception e){
            Log.e(Aplicacion.TAG, "Error al introducir Revisión: " + e.toString());
            Aplicacion.print(Aplicacion.TAG + "Error al incluir revisión: " + e.toString());
        }
    }

    /**
     * Se incluye un nuevo equipo desde el archivo Excel
     * @param datos
     */
    public void incluirEquipo (Vector<String> datos){
        try {
            boolean hayDatos = false;
            StringBuffer instruccion = new StringBuffer("INSERT INTO " + TABLA_EQUIPOS +
                    " VALUES ( null");
            int numDatos = datos.size();
            int diferencia = NUM_COL_EQUIPOS - numDatos;
            for (int i=0; i<diferencia; i++) {
                datos.add("");
            }
            for (int i=0; i<datos.size(); i++){
                instruccion.append(", '" + datos.get(i) + "'");
            }
            instruccion.append(", '" + Aplicacion.ESTADO_PENDIENTE + "'");
            instruccion.append(", '" + Aplicacion.revisionActual + "'");

            // Se determina cual será el nombre del apoyo en función de si es Apoyo o CT
            String nombreApoyo = "";
            String d = datos.get(0).toString();
            String tipo;
            switch (d){
                case "L": // Si es LAMT
                    nombreApoyo = datos.get(6); // Se asigna el nombre de la columna EquipoApoyo
                    if (!nombreApoyo.equals("")) hayDatos = true;
                    tipo = Aplicacion.LAMT;
                    break;
                case "Z": // Si es CT
                    nombreApoyo = datos.get(1); // Se asigna el nombre de la columna CodigoBDE (CT)
                    if (!nombreApoyo.equals("")) hayDatos = true;
                    tipo = Aplicacion.CT;
                    break;
                default:
                    tipo = "";
                    break;
            }
            if (hayDatos) {
                instruccion.append(", '" + nombreApoyo + "', '', '', '', '" + tipo + "')");
                SQLiteDatabase db = getWritableDatabase();
                db.execSQL(instruccion.toString());
            }
        } catch (Exception e) {
            Log.e(Aplicacion.TAG, "Error al introducir Equipo: " + e.toString());
        }

    }

    /**
     * Se incluye un nuevo equipo desde el Dialogo de Nuevo Equipo
     * @param nombreRevision
     * @param nombreEquipo
     * @param tipoInstalacion
     */
    public void incluirNuevoEquipo (String nombreRevision, String nombreEquipo, String tipoInstalacion, String tramo) {
        int id = crearEquipoVacio(); // Se crea un equipo vacio
        // Se actualizan los campos necesarios inicialmente
        actualizarItemEquipoById("NombreRevision", nombreRevision, id);
        actualizarItemEquipoById("NombreEquipo", nombreEquipo, id);
        actualizarItemEquipoById("EquipoApoyo", nombreEquipo, id);
        actualizarItemEquipoById("CodigoBDE", nombreEquipo, id);
        actualizarItemEquipoById("Estado", Aplicacion.ESTADO_PENDIENTE, id);
        actualizarItemEquipoById("TipoInstalacion", tipoInstalacion, id);
        actualizarItemEquipoById("CodigoTramo", tramo, id);
        actualizarItemEquipoById("PosicionTramo", recuperarNumDeTramo(tramo), id);
        id = crearApoyoVacio();
        actualizarItemApoyoById("NombreRevision", nombreRevision, id);
        actualizarItemApoyoById("NombreEquipo", nombreEquipo, id);
        actualizarItemApoyoById("CodigoTramo", tramo, id);
    }

    /**
     * Se incluye un tramo leido del archivo KML
     * @param revision
     * @param tramo
     * @param lng
     * @param lat
     */
    public void incluirTramo (String orden, String revision, String tramo, String lng, String lat) {
        String instruccion = "INSERT INTO " + TABLA_TRAMOS + " VALUES ( null, '" + orden + "', '" + revision +
                                "', '" + tramo + "', '" + lng + "', '" + lat + "')";
        try {
            SQLiteDatabase db = getWritableDatabase();
            db.execSQL(instruccion);
        }catch (Exception e){
            Log.e(Aplicacion.TAG, "Error al introducir tramo: " + e.toString());
        }

    }

    /**
     * Crea un nuevo equipo con todos los campos vacíos excepto el campo id.
     * @return id del registro creado
     */
    public Integer crearEquipoVacio () {
        Cursor c = solicitarDatosEquipos(Aplicacion.revisionActual);
        int id;
        int numCol = c.getColumnCount();
        StringBuffer instruccion = new StringBuffer("INSERT INTO " + TABLA_EQUIPOS +
                " VALUES ( null");
        for (int i=1; i<(numCol-1); i++){
            instruccion.append(", ''");
        }
        instruccion.append(", '')");

        try {
            SQLiteDatabase db = getWritableDatabase();
            db.execSQL(instruccion.toString());

            instruccion.setLength(0);
            instruccion.append("SELECT MAX(_id) FROM " + TABLA_EQUIPOS);
            c = db.rawQuery(instruccion.toString(), null);
            c.moveToFirst();
            id = c.getInt(0);
            c.close();
        } catch (Exception e){
            return null;
        }

        return id;
    }

    /**
     * Crea un nuevo equipo con todos los campos vacíos excepto el campo id.
     * @return id del registro creado
     */
    public Integer crearApoyoVacio () {
        Cursor c = solicitarDatosTodosApoyos(Aplicacion.revisionActual);
        int id;
        int numCol = c.getColumnCount();
        StringBuffer instruccion = new StringBuffer("INSERT INTO " + TABLA_APOYOS +
                " VALUES ( null");
        for (int i=1; i<(numCol-1); i++){
            instruccion.append(", ''");
        }
        instruccion.append(", '')");

        try {
            SQLiteDatabase db = getWritableDatabase();
            db.execSQL(instruccion.toString());

            instruccion.setLength(0);
            instruccion.append("SELECT MAX(_id) FROM " + TABLA_APOYOS);
            c = db.rawQuery(instruccion.toString(), null);
            c.moveToFirst();
            id = c.getInt(0);
            c.close();
        } catch (Exception e){
            return null;
        }

        return id;
    }

    /**
     *
     * @param datos
     */
    public void incluirApoyo (Vector<String> datos){
        try {
            boolean hayDatos = false;
            StringBuffer instruccion = new StringBuffer("INSERT INTO " + TABLA_APOYOS +
                    " VALUES ( null");
            int numDatos = datos.size();
            int diferencia = NUM_COL_APOYOS - numDatos;
            for (int i=0; i<diferencia; i++) {
                datos.add("");
            }
            for (int i=0; i<datos.size(); i++){
                instruccion.append(", '" + datos.get(i) + "'");
            }
            instruccion.append(", '" + Aplicacion.revisionActual + "'");
            // Se determina cual será el nombre del apoyo en función de si es Apoyo o CT
            String nombreApoyo = "";
            String d = datos.get(13).toString();
            switch (d){
                case "L": // Si es LAMT
                    nombreApoyo = datos.get(0); // Se asigna el nombre de la columna EquipoApoyo
                    if (!nombreApoyo.equals("")) hayDatos = true;
                    break;
                case "Z": // Si es CT
                    nombreApoyo = datos.get(14); // Se asigna el nombre de la columna CodigoBDE (CT)
                    if (!nombreApoyo.equals("")) hayDatos = true;
                    break;
                default:
                    break;
            }
            if (hayDatos) {
                instruccion.append(", '" + nombreApoyo + "')");
                SQLiteDatabase db = getWritableDatabase();
                db.execSQL(instruccion.toString());
            } else {
                hayDatos = false;
            }
        } catch (Exception e) {
            Log.e(Aplicacion.TAG, "Error al introducir Apoyo: " + e.toString());
        }

    }

    /**
     *
     * @param datos
     */
    public void incluirApoyoNoRevisable (Vector<String> datos) {
        try {
            boolean hayDatos = false;
            StringBuffer instruccion = new StringBuffer("INSERT INTO " + TABLA_NO_REVISABLE +
                    " VALUES ( null");
            int numDatos = datos.size();
            int diferencia = NUM_COL_APOYOS_NO_REVISABLES - numDatos;
            for (int i=0; i<datos.size(); i++){
                String dato = datos.get(i);
                instruccion.append(", '" + dato + "'");
                if (!hayDatos && !dato.equals("")) {
                    hayDatos = true;
                }
            }
            if (hayDatos) {
                for (int i = 0; i < diferencia; i++) {
                    instruccion.append(", ''");
                }
                instruccion.append(", '" + Aplicacion.revisionActual + ", '', ')");
                SQLiteDatabase db = getWritableDatabase();
                db.execSQL(instruccion.toString());
            }
        } catch (Exception e) {
            Log.e(Aplicacion.TAG, "Error al introducir nuevo Apoyo No Revisable: " + e.toString());
        }

    }

    /**
     *
     * @param nombreRevision
     * @param nombreEquipo
     * @param motivo
     * @param tramo
     */
    public void incluirApoyoNoRevisable (String nombreRevision, String nombreEquipo, String motivo, String tramo) {
        try {
            StringBuffer instruccion = new StringBuffer("INSERT INTO " + TABLA_NO_REVISABLE + " VALUES ( null");
            instruccion.append(", '" + nombreEquipo + "'");
            instruccion.append(", '" + motivo + "'");
            instruccion.append(", '" + nombreRevision + "'");
            instruccion.append(", '" + tramo + "')");
            SQLiteDatabase db = getWritableDatabase();
            db.execSQL(instruccion.toString());
        } catch (Exception e) {
            Log.e(Aplicacion.TAG, "Error al introducir nuevo Apoyo: " + e.toString());
            Aplicacion.print(Aplicacion.TAG + "Error al introducir nuevo Apoyo: " + e.toString());
        }

    }

    /**
     *
     * @param nombreRevision
     * @param nombreEquipo
     * @param codigoDefecto
     * @param tramo
     */
    public void incluirDefecto (String nombreRevision, String nombreEquipo, String codigoDefecto, String tramo) {
        try {
            String instruccion = "INSERT INTO " + TABLA_DEFECTOS +
                                    " VALUES ( null, '" + nombreEquipo + "', '" + nombreRevision + "', '"
                                    + codigoDefecto + "', '', '' , '', '', '', '', '', '', '', '', '" + tramo +
                                    "', '', '', '', '', '', '')";
            SQLiteDatabase db = getWritableDatabase();
            db.execSQL(instruccion);
        } catch (Exception e) {
            Log.e(Aplicacion.TAG, "Error al introducir nuevo Defecto: " + e.toString());
            Aplicacion.print(Aplicacion.TAG + "Error al introducir nuevo Defecto: " + e.toString());
        }
    }

    /**
     *
     * @param nombreRevision
     * @return la revisión solicitada (o null si no se encuentra)
     */
    public Revision solicitarRevision(String nombreRevision) {
        Revision revision = null;
        Vector <String> datosRevision = new Vector<String>();
        String instruccion = "SELECT * FROM '" + TABLA_REVISIONES + "' WHERE Nombre = '" + nombreRevision + "'";
        try {
            SQLiteDatabase db = getReadableDatabase();
            Cursor cursor = db.rawQuery(instruccion, null);
            if ((cursor != null) && (cursor.moveToFirst())){
                for (int i=1; i<cursor.getColumnCount(); i++){
                    datosRevision.add(cursor.getString(i));
                }
                revision = new Revision(cursor.getInt(0), datosRevision);
            }
            cursor.close();
        } catch (Exception e) {
            return null;
        }

        return revision;
    }

    /**
     *
     * @param nombreRevision
     * @param nombreEquipo
     * @param codigoTramo
     * @return el equipo solicitado (o null si no se encuentra)
     */
    public Equipo solicitarEquipo(String nombreRevision, String nombreEquipo, String codigoTramo) {
        Equipo equipo;
        Vector <String> datosEquipo = new Vector<String>();
        String tramo = recuperarNumDeTramo(codigoTramo);
        String instruccion = "SELECT * FROM '" + TABLA_EQUIPOS + "' WHERE NombreRevision = '" + nombreRevision
                                    + "' AND NombreEquipo = '" + nombreEquipo + "' AND CodigoTramo LIKE '%" + tramo + "%'";
        try {
            SQLiteDatabase db = getReadableDatabase();
            Cursor cursor = db.rawQuery(instruccion, null);
            cursor.moveToFirst();
            for (int i=1; i<cursor.getColumnCount(); i++){
                datosEquipo.add(cursor.getString(i));
            }
            equipo = new Equipo(cursor.getInt(0), datosEquipo);
            cursor.close();
        } catch (Exception e) {
            return null;
        }

        return equipo;
    }

    /**
     *
     * @param nombreRevision
     * @param nombreApoyo
     * @param codigoTramo se tratará para recuperar sólo el número de tramo
     * @return el apoyo solicitado (o  null si no lo encuentra)
     */
    public Apoyo solicitarApoyo(String nombreRevision, String nombreApoyo, String codigoTramo) {
        Apoyo apoyo;
        Vector <String> datosApoyo = new Vector<String>();
        // Se trata el dato recibido para recuperar únicamente el número de tramo
        String tramo = recuperarNumDeTramo(codigoTramo);
        String instruccion = "SELECT * FROM '" + TABLA_APOYOS + "' WHERE NombreRevision = '" + nombreRevision
                                    + "' AND NombreEquipo = '" + nombreApoyo + "' AND CodigoTramo LIKE '%" + tramo + "%'";
        try {
            SQLiteDatabase db = getReadableDatabase();
            Cursor cursor = db.rawQuery(instruccion, null);
            cursor.moveToFirst();
            for (int i=1; i<cursor.getColumnCount(); i++){
                datosApoyo.add(cursor.getString(i));
            }
            apoyo = new Apoyo(cursor.getInt(0), datosApoyo);
            cursor.close();
        } catch (Exception e) {
            return null;
        }

        return apoyo;
    }

    /**
     * Solicita el defecto equipo/codigo pasados por parametro
     * @param nombreEquipo
     * @param codigDefecto
     * @param codigoTramo
     * @return el primer defecto encontrado con esos datos (solo se debería encontrar uno que cumpla los requisitos)
     *          Devuelve null si no encuentra ninguno
     */
    public Defecto solicitarDefecto (String nombreEquipo, String codigDefecto, String codigoTramo) {
        Defecto defecto = null;
        Vector <String> datosDefecto = new Vector<String>();
        String tramo = recuperarNumDeTramo(codigoTramo);
        String instruccion = "SELECT * FROM '" + TABLA_DEFECTOS + "' WHERE  NombreRevision = '" +
                                Aplicacion.revisionActual + "' AND NombreEquipo = '" + nombreEquipo +
                                "' AND CodigoDefecto = '" + codigDefecto + "' AND Tramo LIKE '%" + tramo + "%'";
        try {
            SQLiteDatabase db = getReadableDatabase();
            Cursor cursor = db.rawQuery(instruccion, null);
            if ((cursor != null) && (cursor.getCount() > 0)) {
                cursor.moveToFirst();
                for (int i=1; i<cursor.getColumnCount(); i++){
                    datosDefecto.add(cursor.getString(i));
                }
                defecto = new Defecto(cursor.getInt(0), datosDefecto);
            }
            cursor.close();
        } catch (Exception e) {
             return null;
        }

        return defecto;
    }

    public Defecto solicitarDefecto (String revision, String nombreEquipo, String codigDefecto, String codigoTramo) {
        Defecto defecto = null;
        Vector <String> datosDefecto = new Vector<String>();
        String tramo = recuperarNumDeTramo(codigoTramo);
        String instruccion = "SELECT * FROM '" + TABLA_DEFECTOS + "' WHERE  NombreRevision = '" +
                revision + "' AND NombreEquipo = '" + nombreEquipo +
                "' AND CodigoDefecto = '" + codigDefecto + "' AND Tramo LIKE '%" + tramo + "%'";
        try {
            SQLiteDatabase db = getReadableDatabase();
            Cursor cursor = db.rawQuery(instruccion, null);
            if ((cursor != null) && (cursor.getCount() > 0)) {
                cursor.moveToFirst();
                for (int i=1; i<cursor.getColumnCount(); i++){
                    datosDefecto.add(cursor.getString(i));
                }
                defecto = new Defecto(cursor.getInt(0), datosDefecto);
            }
            cursor.close();
        } catch (Exception e) {
            return null;
        }

        return defecto;
    }

    /**
     *
     * @param tabla
     * @param columna
     * @param columnaWhere
     * @param valorWhere
     * @return
     */
    public Cursor solicitarItem(String tabla, String columna, String columnaWhere, String valorWhere) {

        Cursor cursor = null;
        String instruccion = "SELECT " + columna + " FROM " + tabla +
                                " WHERE " + columnaWhere + " = '" + valorWhere + "'";
        try {
            SQLiteDatabase db = getReadableDatabase();
            cursor = db.rawQuery(instruccion, null);
        } catch (Exception e) {
            return null;
        }

        return cursor;
    }

    public boolean marcadoNoRevisable(String revision, String equipo, String tramo) {
        Cursor cursor = null;
        boolean marcadoNoRevisable = false;
        String instruccion = "SELECT * FROM " + TABLA_NO_REVISABLE + " WHERE NombreRevision = '" + revision +
                "' AND CodigoApoyoCT = '" + equipo + "' AND Tramo = '" + tramo + "'";
        try {
            SQLiteDatabase db = getReadableDatabase();
            cursor = db.rawQuery(instruccion, null);
            if (cursor.getCount() > 0) {
                marcadoNoRevisable = true;
            }
            cursor.close();
        } catch (Exception e) {
            Log.e(Aplicacion.TAG, "Error al solicitar MarcadoNoRevisable: " + e.toString());
            return marcadoNoRevisable;
        }

        return marcadoNoRevisable;
    }

    /**
     *
     * @param tabla
     * @param columna
     * @param nombreEquipo
     * @param codigoTramo
     * @return
     */
    public Cursor solicitarFotoEquipo(String tabla, String columna, String nombreRevision,
                                      String nombreEquipo, String codigoTramo) {
        Cursor cursor = null;
        String tramo = recuperarNumDeTramo(codigoTramo);
        String instruccion = "SELECT " + columna + " FROM " + tabla + " WHERE NombreRevision = '" +
                                nombreRevision + "' AND NombreEquipo = '" + nombreEquipo +
                                "' AND CodigoTramo LIKE '%" + tramo + "%'";
        try {
            SQLiteDatabase db = getReadableDatabase();
            cursor = db.rawQuery(instruccion, null);
        } catch (Exception e) {
            return null;
        }

        return cursor;

    }

    /**
     *
     * @param tabla
     * @param columna
     * @param nombreEquipo
     * @param codigoDefecto
     * @param codigoTramo
     * @return
     */
    public Cursor solicitarFotoDefecto(String tabla, String columna, String nombreEquipo,
                                        String codigoDefecto, String codigoTramo) {

        Cursor cursor = null;
        String tramo = recuperarNumDeTramo(codigoTramo);
        String instruccion = "SELECT " + columna + " FROM " + tabla +
                " WHERE NombreEquipo = '" + nombreEquipo + "' AND CodigoDefecto = '" + codigoDefecto + "'" +
                " AND Tramo LIKE '%" + tramo + "%'";
        try {
            SQLiteDatabase db = getReadableDatabase();
            cursor = db.rawQuery(instruccion, null);
        } catch (Exception e) {
            return null;
        }

        return cursor;
    }

    /**
     *
     * @return lista de revisiones existentes en la BDD
     */
    public Vector<Revision> solicitarListaRevisiones () {
        Vector<Revision> resultado = new Vector<Revision>();
        Vector<String> datosFila = new Vector<String>();

        resultado.clear();
        String instruccion = "SELECT * FROM " + TABLA_REVISIONES + " ORDER BY Nombre";
        try{
            SQLiteDatabase db = getReadableDatabase();
            Cursor cursor = db.rawQuery(instruccion, null);
            if(cursor != null) {
                if (cursor.moveToFirst()) {
                    do{
                        datosFila.clear();
                        for (int i=1; i<cursor.getColumnCount(); i++){
                            datosFila.add(cursor.getString(i));
                        }
                        resultado.add(new Revision(cursor.getInt(0), datosFila));
                    } while (cursor.moveToNext());
                }
                cursor.close();
            }
        } catch (Exception e) {
            resultado = null;
        } finally {
            return resultado;
        }

    }

    /**
     *
     * @param nombreRevision
     * @return lista de equipos pertenecientes a la revisión pasada por parámetro
     */
    public Vector<Equipo> solicitarListaEquipos (String nombreRevision) {
        Vector<Equipo> resultado = new Vector<Equipo>();
        Vector<String> datosFila = new Vector<String>();

        resultado.clear();
        String instruccion = "SELECT * FROM " + TABLA_EQUIPOS +
                " WHERE NombreRevision = '" + nombreRevision +
                "' ORDER BY TipoInstalacion, CodigoTramo, NombreEquipo";
        try {
            SQLiteDatabase db = getReadableDatabase();
            Cursor cursor = db.rawQuery(instruccion, null);
            if ((cursor != null) && (cursor.moveToFirst())) {
                do {
                    datosFila.clear();
                    for (int i = 1; i < cursor.getColumnCount(); i++) {
                        datosFila.add(cursor.getString(i));
                    }
                    Equipo e = new Equipo(cursor.getInt(0), datosFila);
                    resultado.add(e);
                } while (cursor.moveToNext());
            }
            cursor.close();
        } catch (Exception e) {
            resultado = null;
        } finally {
            return resultado;
        }
    }

    /**
     * Devuelve el número de equipos con el mismo nombre en cualquier revisión
     * @param nombreEquipo
     * @return
     */
    public int numEquiposIguales (String nombreEquipo) {
        int equiposIguales = 0;
        String instruccion = "SELECT NombreEquipo FROM " + TABLA_EQUIPOS + " WHERE NombreEquipo = '" + nombreEquipo + "'";

        try {
            SQLiteDatabase db = getReadableDatabase();
            Cursor cursor = db.rawQuery(instruccion, null);
            equiposIguales = cursor.getCount();
            cursor.close();
        } catch (Exception e) {
            Log.e (Aplicacion.TAG, "Error al solicitar el número de equipos iguales: " + e.toString());
            return -1;
        }

        return equiposIguales;
    }

    /**
     * Devuelve el número de equipos con el mismo nombre en la misma revisión
     * @param nombreRevision
     * @param nombreEquipo
     * @return
     */
    public int numEquiposIguales (String nombreRevision, String nombreEquipo) {
        int equiposIguales = 0;
        String instruccion = "SELECT NombreEquipo FROM " + TABLA_EQUIPOS + " WHERE " +
                "NombreRevision = '" + nombreRevision + "' AND NombreEquipo = '" + nombreEquipo + "'";

        try {
            SQLiteDatabase db = getReadableDatabase();
            Cursor cursor = db.rawQuery(instruccion, null);
            equiposIguales = cursor.getCount();
            cursor.close();
        } catch (Exception e) {
            Log.e (Aplicacion.TAG, "Error al solicitar el número de equipos iguales: " + e.toString());
            return -1;
        }

        return equiposIguales;
    }

    public Vector<Equipo> solicitarListaEquiposIguales (String nombreEquipo) {
        Vector<Equipo> listaEquipos = new Vector<Equipo>();
        Vector<String> datosFila = new Vector<String>();
        String instruccion = "SELECT * FROM " + TABLA_EQUIPOS + " WHERE NombreEquipo = '" + nombreEquipo + "' " +
                                    "ORDER BY NombreRevision, CodigoTramo";

        try {
            SQLiteDatabase db = getReadableDatabase();
            Cursor cursor = db.rawQuery(instruccion, null);

            cursor.moveToFirst();
            do {
                datosFila.clear();
                for (int i = 1; i < cursor.getColumnCount(); i++) {
                    datosFila.add(cursor.getString(i));
                }
                Equipo e = new Equipo(cursor.getInt(0), datosFila);
                listaEquipos.add(e);
            } while (cursor.moveToNext());
            cursor.close();
        } catch (Exception e) {
            Log.e(Aplicacion.TAG, "Error al solicitar lista de equipos: " + e.toString());
            return null;
        }

        return listaEquipos;
    }

    /**
     *
     * @param revision
     * @return lista de apoyos pertenecientes a la revisión pasada por parámetro
     */
    public Vector<Apoyo> solicitarListaApoyos (String revision) {
        Vector<Apoyo> resultado = new Vector<Apoyo>();
        Vector<String> datosFila = new Vector<String>();

        resultado.clear();
        String instruccion = "SELECT * FROM " + TABLA_APOYOS + " WHERE NombreRevision = '" + revision + "'";
        try {
            SQLiteDatabase db = getReadableDatabase();
            Cursor cursor = db.rawQuery(instruccion, null);
            while (cursor.moveToNext()) {
                datosFila.clear();
                for (int i = 1; i < cursor.getColumnCount(); i++) {
                    datosFila.add(cursor.getString(i));
                }
                Apoyo apoyo = new Apoyo(cursor.getInt(0), datosFila);
                resultado.add(apoyo);
            }
            cursor.close();
        } catch (Exception e) {
            return null;
        }

        return resultado;
    }

    /**
     *
     * @param revision
     * @return lista de coordenadas de tramos pertenecientes a la revisión pasada por parámetro
     */
    public Vector<Tramo> solicitarListaTramosCoord (String revision, Integer orden) {
        Vector<Tramo> resultado = new Vector<Tramo>();
        Vector<String> datosFila = new Vector<String>();
        Cursor cursor = null;
        String instruccion = "SELECT * FROM Tramos WHERE NombreRevision = '" + revision + "' " +
                                "AND Orden = '" + orden + "' ORDER BY _id ASC";
        try {
            SQLiteDatabase db = getReadableDatabase();
            cursor = db.rawQuery(instruccion, null);
            if((cursor != null) && (cursor.moveToFirst())) {
                do{
                    datosFila.clear();
                    Tramo tramo = new Tramo(cursor.getInt(1), cursor.getString(2),
                            cursor.getString(3), cursor.getString(4), cursor.getString(5));
                    resultado.add(tramo);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.e(Aplicacion.TAG, "Error al solicitar tramo: " + e.toString());
        } finally {
            if(cursor != null) {
                cursor.close();
            }
        }

        return resultado;
    }

/*
    public String solicitarCodigoTramo (String revision, Integer orden) {
        Cursor cursor = null;
        String instruccion = "SELECT "
    }
*/

    public Integer solicitarNumTramos (String revision) {
        Cursor cursor = null;
        Integer maxOrden = 0;
        String instruccion = "SELECT MAX(Orden) FROM Tramos WHERE NombreRevision = '" + revision + "'";

        try {
            SQLiteDatabase db = getReadableDatabase();
            cursor = db.rawQuery(instruccion, null);
            cursor.moveToFirst();
            maxOrden = cursor.getInt(0);
        } catch (Exception e) {
            Log.e(Aplicacion.TAG, "Error al convertir numero máximo tramos: " + e.toString());
            maxOrden = 0;
        } finally {
            return maxOrden;
        }
    }

    /**
     * Solicita lista de defectos de un equipo concreto marcados como "SI es defecto"
     * @param nombreRevision
     * @param nombreEquipo
     * @return
     */
    public Vector<String> solicitarDefectosAsociadosPorEquipo (String nombreRevision,
                                                                    String nombreEquipo, String codigoTramo) {
        Vector<String> listaDefectos = new Vector<String>();
        String tramo = recuperarNumDeTramo(codigoTramo);
        listaDefectos.clear();
        String instruccion = "SELECT * FROM " + TABLA_DEFECTOS + " WHERE NombreRevision = '" + nombreRevision +
                                "' AND NombreEquipo = '" + nombreEquipo + "' AND Tramo LIKE '%" + tramo + "%'";
        try {
            SQLiteDatabase db = getReadableDatabase();
            Cursor cursor = db.rawQuery(instruccion, null);
            while (cursor.moveToNext()) {
                String esDefecto = cursor.getString(11);
                if (esDefecto.equals(Aplicacion.SI)) {
                    listaDefectos.add(cursor.getString(3));
                }
            }
            cursor.close();
        } catch (Exception e) {
            Log.e(Aplicacion.TAG, "Error al solicitar lista defectos: " + e.toString());
            Aplicacion.print(Aplicacion.TAG + "Error al solicitar lista defectos: " + e.toString());
            return null;
        }

        return listaDefectos;
    }

    /**
     * Solicita lista de todos los defectos de un equipo concreto marcados como ya sean defecto o no
     * @param nombreRevision
     * @param nombreEquipo
     * @param codigoTramo
     * @return
     */
    public Vector<Defecto> solicitarDefectosRegistrados (String nombreRevision, String nombreEquipo, String codigoTramo) {
        Vector<Defecto> resultado = new Vector<Defecto>();
        Vector<String> datosFila = new Vector<String>();
        resultado.clear();
        String tramo = recuperarNumDeTramo(codigoTramo);

        String instruccion = "SELECT * FROM " + TABLA_DEFECTOS + " WHERE NombreRevision = '" + nombreRevision +
                                "' AND NombreEquipo = '" + nombreEquipo + "' AND Tramo LIKE '%" + tramo + "%'";
        try {
            SQLiteDatabase db = getReadableDatabase();
            Cursor cursor = db.rawQuery(instruccion, null);
            while (cursor.moveToNext()) {
                datosFila.clear();
                for (int i = 1; i < cursor.getColumnCount(); i++) {
                    datosFila.add(cursor.getString(i));
                }
                Defecto defecto = new Defecto(cursor.getInt(0), datosFila);
                resultado.add(defecto);
            }
            cursor.close();
        } catch (Exception e) {
            Log.e(Aplicacion.TAG, "Error al solicitar lista equipos: " + e.toString());
            return null;
        }

        return resultado;

    }

    /**
     * Solicita todos los tramos que hay en una revisión para poder seleccionar a que tramo pertenece
     * un nuevo equipo
     * @param nombreRevision
     * @return
     */
    public Vector<String> solicitarTramos (String nombreRevision) {
        Vector<String> listaTramos = new Vector<String>();
        String instruccion = "SELECT DISTINCT CodigoTramo FROM " + TABLA_EQUIPOS +
                                " WHERE NombreRevision = '" + nombreRevision + "'";
        try {
            SQLiteDatabase db = getReadableDatabase();
            Cursor c = db.rawQuery(instruccion, null);
            while (c.moveToNext()) {
                listaTramos.add(c.getString(0));
            }
            c.close();
        } catch (Exception e) {
            Log.e (Aplicacion.TAG, "Error al solicitar lista de tramos");
            Aplicacion.print(Aplicacion.TAG + "Error al solicitar lista de tramos");
            return  null;
        }

        return listaTramos;
    }

    /**
     * Actualiza los datos iniciales de una revisión
     * @param nombreRevision
     * @param inspector1
     * @param inspector2
     * @param colegiado
     * @param equiposUsados
     * @param metodologia
     * @param codigoNipsa
     * @param trabajo
     * @param codigoInspeccion
     */
    public void actualizarRevision (String nombreRevision, String inspector1, String inspector2, String colegiado,
                                             String equiposUsados, String metodologia, String codigoNipsa,
                                             String trabajo, String codigoInspeccion) {
        try {
            SQLiteDatabase db = getWritableDatabase();
            ContentValues valores = new ContentValues();
            String clausulaWhere;

            valores.put("Estado", Aplicacion.ESTADO_EN_CURSO);
            valores.put("Inspector1", inspector1);
            valores.put("Inspector2", inspector2);
            valores.put("Colegiado", colegiado);
            valores.put("EquipoUsado", equiposUsados);
            valores.put("Metodologia", metodologia);
            valores.put("codigoNipsa", codigoNipsa);
            valores.put("NumeroTrabajo", trabajo);
            valores.put("CodigoInspeccion", codigoInspeccion);

            clausulaWhere = "Nombre = '" + nombreRevision + "'";
            db.update(TABLA_REVISIONES, valores, clausulaWhere, null);

        }catch (Exception e) {
            Log.e (Aplicacion.TAG, "Error al actualizar apoyo: " + e.toString());
            Aplicacion.print("Error al actualizar apoyo: " + Aplicacion.revisionActual);
        }
    }

    /**
     * Actualiza un item de los datos iniciales de una revisión
     * @param nombreRevision
     * @param columna
     * @param dato
     */
    public void actualizarItemRevision(String nombreRevision, String columna, String dato) {
        try {
            SQLiteDatabase db = getWritableDatabase();
            ContentValues valor = new ContentValues();
            String clausulaWhere = "Nombre = '" + nombreRevision + "'";

            valor.put(columna, dato);
            db.update(TABLA_REVISIONES, valor, clausulaWhere, null);
            String instruccion = "SELECT Estado FROM " + TABLA_REVISIONES + " WHERE Nombre = '" + nombreRevision + "'";
            Cursor c = db.rawQuery(instruccion, null);
            c.moveToFirst();
            String s = c.getString(0);
            c.close();
        } catch (Exception e) {
            Log.e(Aplicacion.TAG, e.toString());
            Aplicacion.print(Aplicacion.TAG + "Error al actualizar revision: " + Aplicacion.equipoActual);
        }
    }


    /**
     * Se actualiza la columna pasada por parámetro del equipo actual
     * @param tabla
     * @param columna
     * @param dato
     */
    public void actualizarItemEquipoApoyoActual(String tabla, String columna, String dato) {
        try {
            SQLiteDatabase db = getWritableDatabase();
            ContentValues valor = new ContentValues();
            String tramo = recuperarNumDeTramo(Aplicacion.tramoActual);
            String clausulaWhere = "NombreEquipo = '" + Aplicacion.equipoActual +
                                    "' AND CodigoTramo LIKE '%" + tramo + "%'";

            valor.put(columna, dato);
            db.update(tabla, valor, clausulaWhere, null);
        } catch (Exception e) {
            Log.e(Aplicacion.TAG, e.toString());
            Aplicacion.print(Aplicacion.TAG + "Error al actualizar apoyo: " + Aplicacion.equipoActual);
        }

    }

    /**
     * Se actualiza la columna pasada por parámetro del equipo actual
     * @param tabla
     * @param columna
     * @param dato
     * @param revision
     * @param equipo
     * @param codigoTramo
     */
    public void actualizarItemEquipoApoyo(String tabla, String columna, String dato,
                                                    String revision, String equipo, String codigoTramo) {
        try {
            SQLiteDatabase db = getWritableDatabase();
            ContentValues valor = new ContentValues();
            String tramo = recuperarNumDeTramo(codigoTramo);
            String clausulaWhere = "NombreRevision = '" + revision + "' AND NombreEquipo = '" + equipo +
                    "' AND CodigoTramo LIKE '%" + tramo + "%'";

            valor.put(columna, dato);
            db.update(tabla, valor, clausulaWhere, null);
        } catch (Exception e) {
            Log.e(Aplicacion.TAG, e.toString());
            Aplicacion.print(Aplicacion.TAG + "Error al actualizar apoyo: " + Aplicacion.equipoActual);
        }

    }

    /**
     * Se actualiza la columna del equipo con el dato pasado
     * @param tabla
     * @param columna
     * @param dato
     * @param nombreEquipo
     */
    public void actualizarItemEquipoApoyo(String tabla, String columna, String dato, String nombreEquipo) {
        try {
            SQLiteDatabase db = getWritableDatabase();
            ContentValues valor = new ContentValues();
            String clausulaWhere = "NombreEquipo = '" + nombreEquipo + "'";

            valor.put(columna, dato);
            db.update(tabla, valor, clausulaWhere, null);
        } catch (Exception e) {
            Log.e(Aplicacion.TAG, e.toString());
            Aplicacion.print(Aplicacion.TAG + "Error al actualizar apoyo: " + nombreEquipo);
        }

    }

    public void actualizarFotoEquipo (String revision, String equipo, String tramo, String columna, String nombreFoto) {
        try {
            SQLiteDatabase db = getWritableDatabase();
            ContentValues valor = new ContentValues();
            String clausulaWhere = "NombreRevision = '" + revision + "' AND NombreEquipo = '" + equipo +
                                        "' AND CodigoTramo LIKE '%" + tramo + "%'";

            valor.put(columna, nombreFoto);
            db.update(TABLA_EQUIPOS, valor, clausulaWhere, null);
        } catch (Exception e) {
            Log.e(Aplicacion.TAG, "Error al actualizar la foto: " + e.toString());
        }
    }

    /**
     * Se utiliza para actualizar los datos de un nuevo equipo introducido en el dialogo nuevo equipo
     * @param columna
     * @param dato
     * @param id
     */
    public void actualizarItemEquipoById (String columna, String dato, int id) {
        try {
            SQLiteDatabase db = getWritableDatabase();
            ContentValues valor = new ContentValues();
            String clausulaWhere;

            valor.put(columna, dato);
            clausulaWhere = "_id = '" + id + "'";
            db.update(TABLA_EQUIPOS, valor, clausulaWhere, null);
        } catch (Exception e) {
            Log.e(Aplicacion.TAG, e.toString());
            Aplicacion.print(Aplicacion.TAG + "Error al actualizar equipo: " + Aplicacion.equipoActual);
        }

    }

    /**
     * Se utiliza para actualizar los datos de un nuevo apoyo introducido en el dialogo nuevo equipo
     * @param columna
     * @param dato
     * @param id
     */
    public void actualizarItemApoyoById (String columna, String dato, int id) {
        try {
            SQLiteDatabase db = getWritableDatabase();
            ContentValues valor = new ContentValues();
            String clausulaWhere;

            valor.put(columna, dato);
            clausulaWhere = "_id = '" + id + "'";
            db.update(TABLA_APOYOS, valor, clausulaWhere, null);
        } catch (Exception e) {
            Log.e(Aplicacion.TAG, e.toString());
            Aplicacion.print(Aplicacion.TAG + "Error al actualizar apoyo: " + Aplicacion.equipoActual);
        }

    }

    /**
     * Actualiza un valor del defecto actual
     * @param codigoDefecto
     * @param columna
     * @param dato
     */
    public void actualizarItemDefecto (String codigoDefecto, String columna, String dato) {
        try {
            SQLiteDatabase db = getWritableDatabase();
            ContentValues valor = new ContentValues();
            String clausulaWhere;
            String tramo = recuperarNumDeTramo(Aplicacion.tramoActual);

            valor.put(columna, dato);
            clausulaWhere = "NombreRevision = '" + Aplicacion.revisionActual + "' AND NombreEquipo = '"
                                + Aplicacion.equipoActual + "' AND CodigoDefecto = '" + codigoDefecto +
                                "' AND Tramo LIKE '%" + tramo + "%'";
            db.update(TABLA_DEFECTOS, valor, clausulaWhere, null);
        } catch (Exception e) {
            Log.e(Aplicacion.TAG, e.toString());
            Aplicacion.print(Aplicacion.TAG + "Error al actualizar defecto: " + Aplicacion.defectoActual);
        }

    }

    /**
     *
     * @param revision
     * @return cursor con todos los equipos de una revisión
     */
    public Cursor solicitarDatosEquipos (String revision){
        Cursor cursor = null;

        String instruccion = "SELECT * FROM " + TABLA_EQUIPOS + " WHERE NombreRevision = '" + revision + "'";
        try {
            SQLiteDatabase db = getReadableDatabase();
            cursor = db.rawQuery(instruccion, null);
        } catch (Exception e) {
            Log.e(Aplicacion.TAG, "Error al solicitar lista equipos: " + e.toString());
            return null;
        }

        return cursor;
    }

    /**
     *
     * @param revision
     * @param equipo
     * @param tramo
     * @return Lista de los defectos marcados como "Es Defecto" o que tienen alguna medida
     */
    public Vector<Defecto> solicitarDefectosMedidas (String revision, String equipo, String tramo) {
        Vector<Defecto> resultado = new Vector<Defecto> ();
        Vector<String> datosFila = new Vector<String>();
        String instruccion = "SELECT * FROM " + TABLA_DEFECTOS + " WHERE NombreRevision = '" + revision +
                                "' AND NombreEquipo = '" + equipo + "' AND Tramo LIKE '%" + tramo + "%' AND " +
                                " (EsDefecto = '" + Aplicacion.SI + "' OR Medida != '')";

        try {
            SQLiteDatabase db = getReadableDatabase();
            Cursor cursor = db.rawQuery(instruccion, null);
            if (cursor.moveToFirst()) {
                do{
                    datosFila.clear();
                    for (int i = 1; i < cursor.getColumnCount(); i++) {
                        datosFila.add(cursor.getString(i));
                    }
                    Defecto defecto = new Defecto(cursor.getInt(0), datosFila);
                    resultado.add(defecto);
                } while (cursor.moveToNext());
            } {
            }
            cursor.close();
        } catch (Exception e) {
            Log.e(Aplicacion.TAG, "Error al solicitar lista equipos: " + e.toString());
            return null;
        }

        return resultado;

    }

    /**
     *
     * @param revision
     * @return cursor con todos los apoyos de una revisión
     */
    public Cursor solicitarDatosTodosApoyos (String revision){
        Cursor cursor = null;
        String instruccion = "SELECT * FROM " + TABLA_APOYOS + " WHERE NombreRevision = '" + revision + "'";

        try {
            SQLiteDatabase db = getReadableDatabase();
            cursor = db.rawQuery(instruccion, null);
        } catch (Exception e) {
            Log.e(Aplicacion.TAG, "Error al solicitar lista apoyos: " + e.toString());
            return null;
        }

        return cursor;
    }

    /**
     * Solicita los defectos marcados como "SI es defecto" de una revisión
     * @param revision
     * @return cursor con todos los defectos de una revisión
     */
    public Cursor solicitarDatosDefectosPorRevision (String revision) {
        Cursor cursor = null;
        String instruccion = "SELECT NombreRevision, NombreEquipo, CodigoDefecto, Longitud, Latitud, Tramo " +
                                "FROM " + TABLA_DEFECTOS + " WHERE NombreRevision = '" + revision + "'" +
                                " AND EsDefecto = '" + Aplicacion.SI + "'";

        try {
            SQLiteDatabase db = getReadableDatabase();
            cursor = db.rawQuery(instruccion, null);
        } catch (Exception e) {
            return null;
        }

        return cursor;
    }

    /**
     * Solicita las UTM de un equipo (no se distingue por tramo puesto que las coordenadas son las mismas
     * en los equipos con el mismo nombre)
     * @param revision
     * @param equipo
     * @return cursor con las coordenadas x, y de las UTM de un equipo
     */
    public Cursor solicitarUTMEquipo (String revision, String equipo) {
        Cursor cursor = null;
        String instruccion = "SELECT HusoApoyo, CoordenadaXUTMApoyo, CoordenadaYUTMApoyo FROM " + TABLA_APOYOS +
                                " WHERE NombreRevision = '" + revision + "' AND NombreEquipo = '" + equipo + "'";
        try {
            SQLiteDatabase db = getReadableDatabase();
            cursor = db.rawQuery(instruccion, null);
        } catch (Exception e) {
            return null;
        }

        return cursor;
    }

    public Cursor solicitarDatosApoyosPorRevision (String revision) {
        Cursor cursor = null;
        String instruccion = "SELECT NombreRevision, NombreEquipo, HusoApoyo, CoordenadaXUTMApoyo, CoordenadaYUTMApoyo, " +
                                "Latitud, Longitud FROM " + TABLA_APOYOS + " WHERE NombreRevision = '" + revision + "'";

        try {
            SQLiteDatabase db = getReadableDatabase();
            cursor = db.rawQuery(instruccion, null);
        } catch (Exception e) {
            return null;
        }

        return cursor;
    }

    /**
     * Solicita una lista con los equipos marcados como No Revisables
     * @param revision
     * @return cursor (lista) con los equipos marcados como No Revisables
     */
    public Cursor solicitarDatosEquiposNoRevisables (String revision) {
        Cursor cursor = null;
        String instruccion = "SELECT * FROM " + TABLA_NO_REVISABLE + " WHERE NombreRevision = '" + revision + "'";

        try {
            SQLiteDatabase db = getReadableDatabase();
            cursor = db.rawQuery(instruccion, null);
        } catch (Exception e) {
            return null;
        }

        return cursor;
    }

    /**
     *
     * @param revision
     * @return Lista de CDs con defectos de corrección inmediata
     */
    public Vector<Apoyo> solicitarCDsCorreccionInmediata (String revision) {
        Vector<Apoyo> listaApoyos = new Vector<Apoyo>();
        String instruccion = "";
        try {
            SQLiteDatabase db = getReadableDatabase();
            Cursor cursor = solicitarDatosDefectosPorRevision(revision);
            if ((cursor != null) && cursor.moveToFirst()) {
                do {
                    String equipo = cursor.getString(1);
                    String codigoDefecto = cursor.getString(2);
                    String tramo = cursor.getString(5);
                    Apoyo apoyo = solicitarApoyo(revision, equipo, tramo);
                    if (apoyo.getTipoInstalacion().equals("Z")) {
                        DBGlobal dbGlobal = new DBGlobal(contexto);
                        String tipoDef = dbGlobal.solicitarItem(codigoDefecto, "CorreccioInmediata");
                        if (tipoDef.equalsIgnoreCase(Aplicacion.SI)) {
                            Defecto def = solicitarDefecto(revision, equipo, codigoDefecto, tramo);
                            // En el campo observaciones del apoyo se incluyen los datos que se quieren mostrar:
                            // CodigoDefecto, DescripcionCodigoDefecto y FotosDefecto
                            String obs = codigoDefecto + ": " + def.getObservaciones() +
                                    "---" + def.getFoto1() + "---" + def.getFoto2();
                            apoyo.setObservaciones(obs);
//                            if (!apoyoEstaEnVector(apoyo, listaApoyos)) {
                            listaApoyos.add(apoyo);
//                            }
                        }
                    }
                } while (cursor.moveToNext());
            }
            cursor.close();
        } catch (Exception e) {
            Log.e(Aplicacion.TAG, "Error al solicitar CDs Correccion Inmediata " + e.toString());
            return null;
        }

        return listaApoyos;
    }

    /**
     *
     * @param revision
     * @return Lista de apoyos con defectos de corrección inmediata
     */
    public Vector<Apoyo> solicitarApoyosCorreccionInmediata (String revision) {
        Vector<Apoyo> listaApoyos = new Vector<Apoyo>();
        try {
            Cursor cursor = solicitarDatosDefectosPorRevision(revision);
            if ((cursor != null) && cursor.moveToFirst()) {
                do {
                    String equipo = cursor.getString(1);
                    String codigoDefecto = cursor.getString(2);
                    String tramo = cursor.getString(5);
                    Apoyo apoyo = solicitarApoyo(revision, equipo, tramo);
                    if (apoyo.getTipoInstalacion().equals("L")) {
                        DBGlobal dbGlobal = new DBGlobal(contexto);
                        String tipoDef = dbGlobal.solicitarItem(codigoDefecto, "CorreccioInmediata");
                        if (tipoDef.equalsIgnoreCase(Aplicacion.SI)) {
                            Defecto def = solicitarDefecto(revision, equipo, codigoDefecto, tramo);
                            // En el campo observaciones del apoyo se incluyen los datos que se quieren mostrar:
                            // CodigoDefecto, DescripcionCodigoDefecto y FotosDefecto
                            String obs = codigoDefecto + ": " + def.getObservaciones() +
                                    "---" + def.getFoto1() + "---" + def.getFoto2();
                            apoyo.setObservaciones(obs);
//                            if (!apoyoEstaEnVector(apoyo, listaApoyos)) {
                            listaApoyos.add(apoyo);
//                            }
                        }
                    }
                } while (cursor.moveToNext());
            }
            cursor.close();
        } catch (Exception e) {
            Log.e(Aplicacion.TAG, "Error al solicitar apoyos Correccion Inmediata " + e.toString());
            return null;
        }

        return listaApoyos;
    }

    /**
     *
     * @param revision
     * @return Lista de CDs con defectos estrategicos
     */
    public Vector<Apoyo> solicitarCDsDefectosEstrategicos (String revision) {
        Vector<Apoyo> listaApoyos = new Vector<Apoyo>();
        try {
            Cursor cursor = solicitarDatosDefectosPorRevision(revision);
            if ((cursor != null) && cursor.moveToFirst()) {
                do {
                    String equipo = cursor.getString(1);
                    String codigoDefecto = cursor.getString(2);
                    String tramo = cursor.getString(5);
                    Apoyo apoyo = solicitarApoyo(revision, equipo, tramo);
                    if (apoyo.getTipoInstalacion().equals("Z")) {
                        DBGlobal dbGlobal = new DBGlobal(contexto);
                        String tipoDef = dbGlobal.solicitarItem(codigoDefecto, "DefectesEstrategics");
                        if (tipoDef.equalsIgnoreCase(Aplicacion.SI)) {
                            Defecto def = solicitarDefecto(revision, equipo, codigoDefecto, tramo);
                            // En el campo observaciones del apoyo se incluyen los datos que se quieren mostrar:
                            // CodigoDefecto, DescripcionCodigoDefecto y FotosDefecto
                            String obs = codigoDefecto + ": " + def.getObservaciones() +
                                    "---" + def.getFoto1() + "---" + def.getFoto2();
                            apoyo.setObservaciones(obs);
//                            if (!apoyoEstaEnVector(apoyo, listaApoyos)) {
                            listaApoyos.add(apoyo);
//                            }
                        }
                    }
                } while (cursor.moveToNext());
            }
            cursor.close();
        } catch (Exception e) {
            Log.e(Aplicacion.TAG, "Error al solicitar CDs Defectos estrategicos " + e.toString());
            return null;
        }

        return listaApoyos;
    }

    /**
     *
     * @param revision
     * @return Lista de apoyos con defectos estrategicos
     */
    public Vector<Apoyo> solicitarApoyosDefectosEstrategicos (String revision) {
        Vector<Apoyo> listaApoyos = new Vector<Apoyo>();
        try {
            Cursor cursor = solicitarDatosDefectosPorRevision(revision);
            if ((cursor != null) && cursor.moveToFirst()) {
                do {
                    String equipo = cursor.getString(1);
                    String codigoDefecto = cursor.getString(2);
                    String tramo = cursor.getString(5);
                    Apoyo apoyo = solicitarApoyo(revision, equipo, tramo);
                    if (apoyo.getTipoInstalacion().equals("L")) {
                        DBGlobal dbGlobal = new DBGlobal(contexto);
                        String tipoDef = dbGlobal.solicitarItem(codigoDefecto, "DefectesEstrategics");
                        if (tipoDef.equalsIgnoreCase(Aplicacion.SI)) {
                            Defecto def = solicitarDefecto(revision, equipo, codigoDefecto, tramo);
                            // En el campo observaciones del apoyo se incluyen los datos que se quieren mostrar:
                            // CodigoDefecto, DescripcionCodigoDefecto y FotosDefecto
                            String obs = codigoDefecto + ": " + def.getObservaciones() +
                                    "---" + def.getFoto1() + "---" + def.getFoto2();
                            apoyo.setObservaciones(obs);
//                            if (!apoyoEstaEnVector(apoyo, listaApoyos)) {
                            listaApoyos.add(apoyo);
//                            }
                        }
                    }
                } while (cursor.moveToNext());
            }
            cursor.close();
        } catch (Exception e) {
            Log.e(Aplicacion.TAG, "Error al solicitar apoyos Defectos Estrategicos " + e.toString());
            return null;
        }

        return listaApoyos;
    }

    /**
     *
     * @param revision
     * @return Lista de CDs con defectos NO estrategicos
     */
    public Vector<Apoyo> solicitarCDsDefectosNoEstrategicos (String revision) {
        Vector<Apoyo> listaApoyos = new Vector<Apoyo>();
        try {
            Cursor cursor = solicitarDatosDefectosPorRevision(revision);
            if ((cursor != null) && cursor.moveToFirst()) {
                do {
                    String equipo = cursor.getString(1);
                    String codigoDefecto = cursor.getString(2);
                    String tramo = cursor.getString(5);
                    Apoyo apoyo = solicitarApoyo(revision, equipo, tramo);
                    if (apoyo.getTipoInstalacion().equals("Z")) {
                        DBGlobal dbGlobal = new DBGlobal(contexto);
                        if (dbGlobal.esNoEstrategico(codigoDefecto)) {
                            Defecto def = solicitarDefecto(revision, equipo, codigoDefecto, tramo);
                            // En el campo observaciones del apoyo se incluyen los datos que se quieren mostrar:
                            // CodigoDefecto, DescripcionCodigoDefecto y FotosDefecto
                            String obs = codigoDefecto + ": " + def.getObservaciones() +
                                    "---" + def.getFoto1() + "---" + def.getFoto2();
                            apoyo.setObservaciones(obs);
//                            if (!apoyoEstaEnVector(apoyo, listaApoyos)) {
                                listaApoyos.add(apoyo);
//                            }
                        }
                    }
                } while (cursor.moveToNext());
            }
            cursor.close();
        } catch (Exception e) {
            Log.e(Aplicacion.TAG, "Error al solicitar CDs Defectos estrategicos " + e.toString());
            return null;
        }

        return listaApoyos;
    }

    /**
     *
     * @param revision
     * @return Lista de apoyos con defectos NO estrategicos
     */
    public Vector<Apoyo> solicitarApoyosDefectosNoEstrategicos (String revision) {
        Vector<Apoyo> listaApoyos = new Vector<Apoyo>();
        try {
            Cursor cursor = solicitarDatosDefectosPorRevision(revision);
            if ((cursor != null) && cursor.moveToFirst()) {
                do {
                    String equipo = cursor.getString(1);
                    String codigoDefecto = cursor.getString(2);
                    String tramo = cursor.getString(5);
                    Apoyo apoyo = solicitarApoyo(revision, equipo, tramo);
                    if (apoyo.getTipoInstalacion().equals("L")) {
                        DBGlobal dbGlobal = new DBGlobal(contexto);
                        if (dbGlobal.esNoEstrategico(codigoDefecto)) {
                            Defecto def = solicitarDefecto(revision, equipo, codigoDefecto, tramo);
                            // En el campo observaciones del apoyo se incluyen los datos que se quieren mostrar:
                            // CodigoDefecto, DescripcionCodigoDefecto y FotosDefecto
                            String obs = codigoDefecto + ": " + def.getObservaciones() +
                                    "---" + def.getFoto1() + "---" + def.getFoto2();
                            apoyo.setObservaciones(obs);
//                            if (!apoyoEstaEnVector(apoyo, listaApoyos)) {
                            listaApoyos.add(apoyo);
//                            }
                        }
                    }
                } while (cursor.moveToNext());
            }
            cursor.close();
        } catch (Exception e) {
            Log.e(Aplicacion.TAG, "Error al solicitar apoyos Defectos estrategicos " + e.toString());
            return null;
        }

        return listaApoyos;
    }

/*
    private boolean apoyoEstaEnVector(Apoyo apoyo, Vector<Apoyo> lista) {
        boolean estaEnVector = false;
        for (int i=0; i<lista.size(); i++) {
            String nombreEquipo = lista.elementAt(i).getNombreEquipo();
            if (nombreEquipo.equals(apoyo.getNombreEquipo())) {
                estaEnVector = true;
                break;
            }
        }

        return estaEnVector;
    }
*/

    /**
     * Solicita una lista (vector) con todos los CTs de una revisión (excluye apoyos)
     * @param revision
     * @return vector con la lista de apoyos
     */
    public Vector<Apoyo> solicitarDatosCDs (String revision) {
        Vector<Apoyo> resultado = new Vector<Apoyo>();
        Vector<String> datosFila = new Vector<String>();

        resultado.clear();
        String instruccion = "SELECT * FROM " + TABLA_APOYOS + " WHERE NombreRevision = '" + revision + "'" +
                                " AND TipoInstalacion = 'Z'";
        try {
            SQLiteDatabase db = getReadableDatabase();
            Cursor cursor = db.rawQuery(instruccion, null);
            while (cursor.moveToNext()) {
                datosFila.clear();
                for (int i = 1; i < cursor.getColumnCount(); i++) {
                    datosFila.add(cursor.getString(i));
                }
                Apoyo apoyo = new Apoyo(cursor.getInt(0), datosFila);
                Equipo equipo = solicitarEquipo(apoyo.getNombreRevision(), apoyo.getNombreEquipo(),
                                    apoyo.getCodigoTramo());
                String obs = apoyo.getObservaciones() + "---" + equipo.getDocumentosAsociar() + "---"
                                + equipo.getDescripcionDocumentos();
                apoyo.setObservaciones(obs);
                resultado.add(apoyo);
            }
            cursor.close();
        } catch (Exception e) {
            Log.e(Aplicacion.TAG, "Error al solicitar lista equipos: " + e.toString());
            return null;
        }

        return resultado;
    }

    /**
     * Solicita una lista (vector) con todos los apoyos de una revisión (excluye los CDs)
     * @param revision
     * @return vector con la lista de apoyos
     */
    public Vector<Apoyo> solicitarDatosApoyos (String revision) {
        Vector<Apoyo> resultado = new Vector<Apoyo>();
        Vector<String> datosFila = new Vector<String>();

        resultado.clear();
        String instruccion = "SELECT * FROM " + TABLA_APOYOS + " WHERE NombreRevision = '" + revision + "'" +
                " AND TipoInstalacion = 'L'";
        try {
            SQLiteDatabase db = getReadableDatabase();
            Cursor cursor = db.rawQuery(instruccion, null);
            while (cursor.moveToNext()) {
                datosFila.clear();
                for (int i = 1; i < cursor.getColumnCount(); i++) {
                    datosFila.add(cursor.getString(i));
                }
                Apoyo apoyo = new Apoyo(cursor.getInt(0), datosFila);
                Equipo equipo = solicitarEquipo(apoyo.getNombreRevision(), apoyo.getNombreEquipo(),
                        apoyo.getCodigoTramo());
                String obs = apoyo.getObservaciones() + "---" + equipo.getDocumentosAsociar() + "---"
                        + equipo.getDescripcionDocumentos();
                apoyo.setObservaciones(obs);
                resultado.add(apoyo);
            }
            cursor.close();
        } catch (Exception e) {
            // Log.e(Aplicacion.TAG, "Error al solicitar lista equipos: " + e.toString());
            // Aplicacion.print(Aplicacion.TAG + "Error al solicitar lista equipos: " + e.toString());
            return null;
        }

        return resultado;
    }

    public boolean equiposFinalizados(String revision) {
        boolean finalizados = false;
        String instruccion = "SELECT NombreEquipo FROM " + TABLA_EQUIPOS + " WHERE NombreRevision = '" +
                revision + "' AND Estado != '" + Aplicacion.ESTADO_FINALIZADA + "'";

        try {
            SQLiteDatabase db = getReadableDatabase();
            Cursor cursor = db.rawQuery(instruccion,  null);
            if (!(cursor.getCount() > 0)) {
                finalizados = true;
            }
            cursor.close();
        } catch (Exception e) {
            Log.e(Aplicacion.TAG, "Error al comprobar los equipos finalizados " + e.toString());
        } finally {
            return finalizados;
        }
    }

    /**
     * Borrar un registro de la tabla de defectos
     * @param nombreRevision
     * @param nombreEquipo
     */
    public void borrarDefectosEquipo (String nombreRevision, String nombreEquipo, String codigoTramo) {
        String tramo = recuperarNumDeTramo(codigoTramo);
        String instruccion = "DELETE FROM " + TABLA_DEFECTOS + " WHERE NombreRevision = '" + nombreRevision +
                                "' AND NombreEquipo = '" + nombreEquipo + "' AND Tramo LIKE '%" + tramo + "%'";
        try {
            SQLiteDatabase db = getWritableDatabase();
            db.execSQL(instruccion);
        } catch (Exception e) {
            Log.e(Aplicacion.TAG, "Error al borrar defectos: " + e.toString());
            Aplicacion.print(Aplicacion.TAG + "Error al borrar defectos: " + e.toString());
        }
    }

    /**
     * Borrar un registro de la tabla de apoyos no revisables
     *
     * @param nombreRevision
     * @param nombreEquipo
     */
    public void borrarNoRevisable (String nombreRevision, String nombreEquipo, String codigoTramo){
        String tramo = recuperarNumDeTramo(codigoTramo);
        String instruccion = "DELETE FROM " + TABLA_NO_REVISABLE + " WHERE NombreRevision = '" + nombreRevision +
                                "' AND CodigoApoyoCT = '" + nombreEquipo + "' AND Tramo LIKE '%" + tramo + "%'";
        try {
            SQLiteDatabase db = getReadableDatabase();
            db.execSQL(instruccion);
        } catch (Exception e) {
            Log.e(Aplicacion.TAG, "Error al borrar apoyo no revisable: " + e.toString());
            Aplicacion.print(Aplicacion.TAG + "Error al borrar apoyo no revisable: " + e.toString());
        }
    }

    /**
     * Elimina todos los registro de la revisión dada de la BDD
     *
     * @param revision a eliminar
     */
    public void borrarRevision (Revision revision) {
        String sRevision = revision.getNombre();
        String clausulaWhere;
        String[] args = {sRevision};
        try {
            SQLiteDatabase db = getWritableDatabase();
            clausulaWhere = "Nombre = ?";
            db.delete(TABLA_REVISIONES, clausulaWhere, args);
            clausulaWhere = "NombreRevision = ?";
            db.delete(TABLA_EQUIPOS, clausulaWhere, args);
            db.delete(TABLA_APOYOS, clausulaWhere, args);
            db.delete(TABLA_DEFECTOS, clausulaWhere, args);
            db.delete(TABLA_NO_REVISABLE, clausulaWhere, args);
            db.delete(TABLA_TRAMOS, clausulaWhere, args);
        } catch (Exception e) {
            Log.e(Aplicacion.TAG, "Error al borrar la revision: " + e.toString());
        }
    }

    /**
     * Recupera el número de tramo de un texto dado (puede ser del formato aaa-000-bbbbbbb o bien 000)
     *
     * @param texto
     * @return el número de tramo
     */
    public static String recuperarNumDeTramo (String texto) {
        String tramo = texto;

        if (tramo != null) {
            if (tramo.contains("-")) {
                tramo = tramo.substring(tramo.indexOf("-") + 1);
                if (tramo.contains("-")) {
                    tramo = tramo.substring(0, tramo.indexOf("-"));
                }
            }
        }

        return tramo;
    }

    /**
     * Devuelve un backup de la tabla y revisión recibidos
     * @param revision
     * @param tabla
     * @return
     */
    public Cursor solicitarBackup (String revision, String tabla) {
        Cursor cursor = null;

        String instruccion = "SELECT * FROM " + tabla + " WHERE NombreRevision = '" + revision + "'";
        try {
            SQLiteDatabase db = getReadableDatabase();
            cursor = db.rawQuery(instruccion, null);
        } catch (Exception e) {
            Log.e(Aplicacion.TAG, "Error al solicitar lista equipos: " + e.toString());
            return null;
        }

        return cursor;

    }

    /**
     * Incluye la revisión seleccionada en la BDD
     * @param nombreRevision
     */
    public void importarRevision(String nombreRevision) {
        //Aplicacion.print(nombreRevision);
        if (!existeRevision(nombreRevision)) {
            incluirNuevaRevision(nombreRevision);
        } else {
            //fusionarDB(nombreRevision);
        }
    }

    /**
     * Incluye una nueva revisión leida de un backup
     * @param nombreRevision
     */
    private void incluirNuevaRevision (String nombreRevision) {
            incluirRevision(nombreRevision);
            incluirElementos(nombreRevision, TABLA_EQUIPOS);
            incluirElementos(nombreRevision, TABLA_APOYOS);
            incluirElementos(nombreRevision, TABLA_NO_REVISABLE);
            //incluirElementos(nombreRevision, TABLA_TRAMOS);
            incluirElementos(nombreRevision, TABLA_DEFECTOS);

    }

    /**
     * Incluye los datos de la tabla Revisión
     * @param nombreRevision
     */
    private void incluirRevision(String nombreRevision) {
        DBBackup dbBackup = new DBBackup(contexto);
        Revision revision = dbBackup.solicitarRevision(nombreRevision);
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

    /**
     * Incluye los datos de la tabla recibida por parámetro
     * @param nombreRevision
     * @param tabla
     */
    private void incluirElementos(String nombreRevision, String tabla) {
        DBBackup dbBackup = new DBBackup(contexto);
        Cursor cursor = dbBackup.solicitarBackup(nombreRevision, tabla);
        if ((cursor != null) && (cursor.moveToFirst())){
            do {
                StringBuffer inst = new StringBuffer();
                inst.append("INSERT INTO " + tabla + " VALUES (null");
                for (int i=1; i<cursor.getColumnCount(); i++) {
                    inst.append(", '" + cursor.getString(i) + "'");
                }
                // TODO: Eliminar para versiones nuevas, solo sirve para tablas viejas que no tienen el campo PaTUnidas y Rc
/*
                if (tabla.equals(TABLA_DEFECTOS)) {
//                    inst.append(", ''"); // Sólo medidaPaT
                    inst.append(", '', '', ''"); // Con medidas Rc guardadas en BDD
                }
*/
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
     * Fusiona la BDD existente con la BDD del backup leido. Sólo fusionará los equipos que ya estén
     * finalizados en la BDD esclava
     * @param nombreRevision
     */
    private void fusionarDB (String nombreRevision) {
        DBBackup dbBackup = new DBBackup(contexto);
        Vector<Equipo> listaEquiposEsclavo = dbBackup.solicitarEquiposFinalizados(nombreRevision);
        if (listaEquiposEsclavo != null) {
            for (int i=0; i<listaEquiposEsclavo.size(); i++) {
                Equipo equipoEscalvo = listaEquiposEsclavo.elementAt(i);
                Equipo equipoMaster = solicitarEquipo(equipoEscalvo.getNombreRevision(), equipoEscalvo.getNombreEquipo(),
                                                        equipoEscalvo.getCodigoTramo());
                if (!equipoMaster.getEstado().equals(Aplicacion.ESTADO_FINALIZADA)) {
                    fusionarEquipos(equipoEscalvo);
                    fusionarApoyos(equipoEscalvo);
                    fusionarNoRevisables(equipoEscalvo);
//                    fusionarDefectos(equipoEscalvo);
                }
            }
        }
    }

    /**
     * Se borra en la BDD Master el equipo que coincide con el esclavo recibido para sobreescribirlo
     * con este último
     * @param equipoEsclavo
     */
    private void fusionarEquipos (Equipo equipoEsclavo) {
        DBBackup dbBackup = new DBBackup(contexto);
        Cursor cursorMaster = dbBackup.solicitarEquipo(equipoEsclavo);
        if((cursorMaster != null) && (cursorMaster.moveToFirst())) {
            do {
                // Se borra el elemento existente en la BDD Master
                SQLiteDatabase db = getWritableDatabase();
                String clausulaWhere = "NombreRevision = '" + equipoEsclavo.getNombreRevision() +
                                        "' AND NombreEquipo = '" + equipoEsclavo.getNombreEquipo() +
                                        "' AND CodigoTramo LIKE '%" + equipoEsclavo.getCodigoTramo() + "%'";
                db.delete(TABLA_EQUIPOS, clausulaWhere, null);
                // Se incluye el registro del equipo de la BDD Esclava
/*
                Vector<String> v = new Vector<String>();
                for (int i=0; i<cursorMaster.getColumnCount(); i++) {
                    v.add(cursorMaster.getString(i));
                }
*/
                incluirRegistro(cursorMaster, TABLA_EQUIPOS);
            } while (cursorMaster.moveToNext());
        }
        cursorMaster.close();
    }

    /**
     * Incluye el registro pasado en el cursor en la tabla pasada por parámetro
     * @param cRegistro
     * @param tabla
     */
    private void incluirRegistro (Cursor cRegistro, String tabla) {
        if((cRegistro != null) && cRegistro.moveToFirst()) {
            StringBuffer inst = new StringBuffer();
            inst.append("INSERT INTO " + tabla + " VALUES (null");
            for (int i=1; i<cRegistro.getColumnCount(); i++) {
                inst.append(", '" + cRegistro.getString(i) + "'");
            }
            inst.append(")");
            SQLiteDatabase db = getWritableDatabase();
            try {
                db.execSQL(inst.toString());
            } catch (SQLException e) {
                Log.e(Aplicacion.TAG, "Error al incluir registro: " + e.toString());
            }
        }
    }

    /**
     * Se borra en la BDD Master el apoyo que coincide con el esclavo recibido para sobreescribirlo
     * con este último
     * @param equipoEsclavo
     */
    private void fusionarApoyos (Equipo equipoEsclavo) {
        DBBackup dbBackup = new DBBackup(contexto);
        Cursor cursorMaster = dbBackup.solicitarApoyo(equipoEsclavo);
        if((cursorMaster != null) && (cursorMaster.moveToFirst())) {
            do {
                // Se borra el elemento existente en la BDD Master
                SQLiteDatabase db = getWritableDatabase();
                String clausulaWhere = "NombreRevision = '" + equipoEsclavo.getNombreRevision() +
                        "' AND NombreEquipo = '" + equipoEsclavo.getNombreEquipo() +
                        "' AND CodigoTramo LIKE '%" + equipoEsclavo.getCodigoTramo() + "%'";
                db.delete(TABLA_APOYOS, clausulaWhere, null);
                // Se incluye el registro del apoyo de la BDD Esclava
                incluirRegistro(cursorMaster, TABLA_APOYOS);
            } while (cursorMaster.moveToNext());
        }
        cursorMaster.close();
    }

    /**
     * Se borra en la BDD Master el apoyo no revisable que coincide con el esclavo recibido para sobreescribirlo
     * con este último
     * @param equipoEsclavo
     */
    private void fusionarNoRevisables (Equipo equipoEsclavo) {
        DBBackup dbBackup = new DBBackup(contexto);
        Cursor cursorMaster = dbBackup.solicitarNoRevisable(equipoEsclavo);
        if((cursorMaster != null) && (cursorMaster.moveToFirst())) {
            do {
                // Se borra el elemento existente en la BDD Master
                SQLiteDatabase db = getWritableDatabase();
                String clausulaWhere = "NombreRevision = '" + equipoEsclavo.getNombreRevision() +
                        "' AND CodigoApoyoCT = '" + equipoEsclavo.getNombreEquipo() +
                        "' AND Tramo LIKE '%" + equipoEsclavo.getCodigoTramo() + "%'";
                db.delete(TABLA_NO_REVISABLE, clausulaWhere, null);
                // Se incluye el registro del apoyo no revisable de la BDD Esclava
                incluirRegistro(cursorMaster, TABLA_NO_REVISABLE);
            } while (cursorMaster.moveToNext());
        }
        cursorMaster.close();
    }

    /**
     * Se borra en la BDD Master el apoyo no revisable que coincide con el esclavo recibido para sobreescribirlo
     * con este último.
     * @param equipoEsclavo
     */
    private void fusionarDefectos (Equipo equipoEsclavo) {
        DBBackup dbBackup = new DBBackup(contexto);
        Cursor cursorMaster = dbBackup.solicitarDefectos(equipoEsclavo);
        if((cursorMaster != null) && (cursorMaster.moveToFirst())) {
            do {
                // Se borra el elemento existente en la BDD Master
                SQLiteDatabase db = getWritableDatabase();
                String clausulaWhere = "NombreRevision = '" + equipoEsclavo.getNombreRevision() +
                        "' AND NombreEquipo = '" + equipoEsclavo.getNombreEquipo() +
                        "' AND Tramo LIKE '%" + equipoEsclavo.getCodigoTramo() + "%'";
                db.delete(TABLA_DEFECTOS, clausulaWhere, null);
                // Se incluye el registro del apoyo no revisable de la BDD Esclava
                incluirRegistro(cursorMaster, TABLA_DEFECTOS);
            } while (cursorMaster.moveToNext());
        }
        cursorMaster.close();
    }

}