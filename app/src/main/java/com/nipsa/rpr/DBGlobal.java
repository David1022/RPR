package com.nipsa.rpr;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.Vector;

public class DBGlobal extends SQLiteOpenHelper {

    public static String DATABASE_NAME = "DBGlobal";
    public static String TABLA_DEFECTOS = "Defectos";

    public DBGlobal(Context contexto) {
        super(contexto, DATABASE_NAME, null, 1);
    }

    /**
     * Llamado al crear la BDD (sólo se llama si la BDD aún no ha sido creada
     *
     * @param db
     */
    @Override
    public void onCreate(SQLiteDatabase db) {

        try {
            String instruccionCrearTablaDefectos = "CREATE TABLE " + TABLA_DEFECTOS +
                    " (InstalacionTipo TEXT, CodigoEndesa2010 TEXT, CodigoEndesa2012 PRIMARY KEY NOT NULL TEXT," +
                    " CriticidadCatalunya TEXT, CodigoDecreto328 TEXT, DescripcionEndesa TEXT, Observaciones TEXT," +
                    " CorreccioInmediata TEXT, DefectesEstrategics TEXT, DefectesMajorsMenors TEXT)";
            db.execSQL(instruccionCrearTablaDefectos);

        }catch (Exception e){
            Log.e("ERRORRPR: ", "Error al crear la base de datos: " + e.toString());
        }
    }

    /**
     *
     * @param db
     * @param oldVersion
     * @param newVersion
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    /**
     * Devuelve los defectos según el tipo equipo (LAMT, CT, PT) y el grupo de defecto
     * dado (Aislamiento, Conductor,...)
     *
     * @param tipo
     * @param grupo
     * @return Lista con los defectos de un grupo dado
     */
    public Vector<ListaDef> solicitarListaDef (String tipo, String grupo) {

        Vector<String> datosFila = new Vector<String>();
        Vector<ListaDef> resultado = new Vector<ListaDef>();
        Cursor cursor = null;
        String instruccion = "SELECT * FROM " + TABLA_DEFECTOS + " WHERE InstalacionTipo = '" + tipo + "' AND " +
                                    "CodigoEndesa2012 LIKE '" + grupo + "%'";

        try {
            SQLiteDatabase db = getReadableDatabase();
            cursor = db.rawQuery(instruccion, null);
            while (cursor.moveToNext()) {
                datosFila.clear();
                for (int i = 0; i < cursor.getColumnCount(); i++) {
                    datosFila.add(cursor.getString(i));
                }
                resultado.add(new ListaDef(datosFila));
            }
            cursor.close();
        } catch (Exception e) {
            Log.e(Aplicacion.TAG, e.toString());
            Aplicacion.print("ERRORRPR: al solicitar lista defectos" + e.toString());
        }

        return resultado;
    }

    /**
     * Devuelve una lista con todos los codigos de defectos
     * @param tipo
     * @return Lista con todos los codigos de defectos
     */
    public Vector<ListaDef> solicitarListaDef (String tipo) {
        Vector<String> datosFila = new Vector<String>();
        Vector<ListaDef> resultado = new Vector<ListaDef>();
        Cursor cursor = null;
        String instruccion = "SELECT * FROM " + TABLA_DEFECTOS + " WHERE InstalacionTipo = '" + tipo + "'";

        try {
            SQLiteDatabase db = getReadableDatabase();
            cursor = db.rawQuery(instruccion, null);
            while (cursor.moveToNext()) {
                datosFila.clear();
                for (int i = 0; i < cursor.getColumnCount(); i++) {
                    datosFila.add(cursor.getString(i));
                }
                resultado.add(new ListaDef(datosFila));
            }
            cursor.close();
        } catch (Exception e) {
            Log.e("ERRORRPR: ", e.toString());
            Aplicacion.print("ERRORRPR: al solicitar lista defectos" + e.toString());
        }

        return resultado;
    }

    /**
     * Devuelve la descripción del defecto pasado por parámetro
     *
     * @param codigo
     * @return descripción del defecto o null si no se encuentra el codigo
     */
    public String solicitarDescripcionPorCodigo (String codigo) {
        String descripcion;
        String instruccion = "SELECT DescripcionEndesa FROM " + TABLA_DEFECTOS + " WHERE CodigoEndesa2012 = '" + codigo + "'";
        try {
            SQLiteDatabase db = getReadableDatabase();
            Cursor c = db.rawQuery(instruccion, null);
            c.moveToFirst();
            descripcion = c.getString(0);
        } catch (Exception e) {
            return null;
        }

        return descripcion;
    }

    public String solicitarItem(String codigoDefecto, String columna) {
        String resultado;
        String instruccion = "SELECT " + columna + " FROM " + TABLA_DEFECTOS + " WHERE CodigoEndesa2012 = ?";
        String[] args = {codigoDefecto};
        try {
            SQLiteDatabase db = getReadableDatabase();
            Cursor c = db.rawQuery(instruccion, args);
            c.moveToFirst();
            resultado = c.getString(0);
        } catch (Exception e) {
            return null;
        }

        return resultado;
    }

    public boolean esNoEstrategico (String codigoDefecto) {
        boolean esNoEstrategico = false;
        String instruccion = "SELECT * FROM Defectos WHERE CodigoEndesa2012 = '" + codigoDefecto +
                                "' AND CorreccioInmediata = '' AND DefectesEstrategics = ''";
        try {
            SQLiteDatabase db = getReadableDatabase();
            Cursor c = db.rawQuery(instruccion, null);
            if (c.getCount()>0) {
                esNoEstrategico = true;
            }
        } catch (Exception e) {
            Log.e(Aplicacion.TAG, "Error al leer error NO estrategico " + e.toString());
        }finally {
            return esNoEstrategico;
        }
    }

}