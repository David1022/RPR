package com.nipsa.rpr.ExcelHandler;

import android.content.Context;
import android.util.Log;

import com.nipsa.rpr.Aplicacion;
import com.nipsa.rpr.DBRevisiones;

import java.io.File;
import java.io.IOException;
import java.util.Vector;

import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

/**
 * Created by david on 30/10/17.
 */

public class ExcelReader {

    public static final int MARK_FIRST_SHEET_INIT_ROW = 10;
    public static final int MARK_SECOND_SHEET_INIT_ROW = 11;
    public static final int MARK_THIRD_SHEET_INIT_ROW = 12;
    public static final int FIRST_SHEET_NUM_COLUMNS = 28;
    public static final int SECOND_SHEET_NUM_COLUMNS = 17;
    public static final int THIRD_SHEET_NUM_COLUMNS = 2;
    public static final String SPACE = " ";

    private File mExcelToRead;
    private Context mContext;
    private DBRevisiones mDBRevisiones;

    public ExcelReader(Context context, File excelToRead) {
        this.mContext = context;
        this.mExcelToRead = excelToRead;
        mDBRevisiones = new DBRevisiones(mContext);
    }

    public void readExcelFile() {
        Workbook workbook;
        try {
            workbook = Workbook.getWorkbook(mExcelToRead);
            readFirstSheet(workbook.getSheet(0));
            readSecondSheet(workbook.getSheet(1));
            readThirdSheet(workbook.getSheet(2));
        } catch (IOException ioe) {
            Log.e(Aplicacion.TAG, "ExcelReader.readExcelFile: " + ioe.toString());
        } catch (BiffException be) {
            Log.e(Aplicacion.TAG, "ExcelReader.readExcelFile: " + be.toString());
        }
    }

    private void readFirstSheet(Sheet sheet) {
        Vector<String> rowData = new Vector<>();
        int totalRows = sheet.getRows();
        int totalCol = FIRST_SHEET_NUM_COLUMNS;

        readRevisionName(sheet);

        for (int row = MARK_FIRST_SHEET_INIT_ROW; row < totalRows; row++) { // get all the rows
            rowData.clear();
            for (int col = 0; col < totalCol; col++) { // for each rows, get all the columns (cells)
                rowData.add(sheet.getCell(col, row).getContents());
            }
            mDBRevisiones.incluirEquipo(rowData);
        }

    }

    private void readSecondSheet(Sheet sheet) {
        Vector<String> rowData = new Vector<>();
        int totalRows = sheet.getRows();
        int totalCol = SECOND_SHEET_NUM_COLUMNS;

        // TODO: Comprobar si lee y guarda bien los apoyos

        for (int row = MARK_SECOND_SHEET_INIT_ROW; row < totalRows; row++) { // get all the rows
            rowData.clear();
            for (int col = 1; col < totalCol; col++) { // for each rows, get all the columns (cells)
                rowData.add(sheet.getCell(col, row).getContents());
            }
            mDBRevisiones.incluirApoyo(rowData);
        }

    }

    private void readThirdSheet(Sheet sheet) {
        Vector<String> rowData = new Vector<>();
        int totalRows = sheet.getRows();
        int totalCol = THIRD_SHEET_NUM_COLUMNS;

        // TODO: Comprobar si lee y guarda bien los apoyos

        for (int row = MARK_THIRD_SHEET_INIT_ROW; row < totalRows; row++) { // get all the rows
            rowData.clear();
            for (int col = 0; col < totalCol; col++) { // for each rows, get all the columns (cells)
                rowData.add(sheet.getCell(col, row).getContents());
            }
            mDBRevisiones.incluirApoyoNoRevisable(rowData);
        }

    }

    private void readRevisionName(Sheet sheet) {
        String revisionName = sheet.getCell(2, 0).getContents();

        if (revisionName != null) {
            revisionName = revisionName.substring(revisionName.lastIndexOf(SPACE) + 1);
        }

        Aplicacion.tituloRevision = revisionName;

    }
}
