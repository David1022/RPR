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

public class ExcelReader {

    public static final int MARK_FIRST_SHEET_INIT_ROW = 10;
    public static final int MARK_SECOND_SHEET_INIT_ROW = 11;
    public static final int MARK_THIRD_SHEET_INIT_ROW = 12;
    public static final int FIRST_SHEET_NUM_COLUMNS = 28;
    public static final int SECOND_SHEET_NUM_COLUMNS = 17;
    public static final int THIRD_SHEET_NUM_COLUMNS = 2;
    public static final String SPACE = " ";
    public static final int REVISION_NAME_COL = 0;
    public static final int REVISION_NAME_ROW = 2;

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
            ioe.printStackTrace();
            Log.e(Aplicacion.TAG, "ExcelReader.readExcelFile: " + ioe.toString());
        } catch (BiffException be) {
            be.printStackTrace();
            Log.e(Aplicacion.TAG, "ExcelReader.readExcelFile: " + be.toString());
        } catch (IllegalArgumentException iae) { // Exception caused by the expected excel file format
            iae.printStackTrace();
            Log.e(Aplicacion.TAG, "Error en el formato del excel" + iae.toString());
            throw new IllegalArgumentException(iae.toString());
        }
    }

    private void readFirstSheet(Sheet sheet) throws IllegalArgumentException {
        Vector<String> rowData = new Vector<>();
        int totalRows = sheet.getRows();
        int totalCol = sheet.getColumns();

        readRevisionName(sheet);

        if ((totalRows - MARK_FIRST_SHEET_INIT_ROW) > 0) {
            if ((totalCol - FIRST_SHEET_NUM_COLUMNS) >= 0) {
                for (int row = MARK_FIRST_SHEET_INIT_ROW; row < totalRows; row++) { // get all the rows
                    rowData.clear();
                    for (int col = 0; col < FIRST_SHEET_NUM_COLUMNS; col++) { // for each row, get all the columns (cells)
                        rowData.add(sheet.getCell(col, row).getContents());
                    }
                    mDBRevisiones.incluirEquipo(rowData);
                }
            } else {
                throw new IllegalArgumentException(("Illegal number of columns in sheet 1"));
            }
        } else {
            throw new IllegalArgumentException("Illegal number of rows in sheet 1");
        }

    }

    private void readSecondSheet(Sheet sheet) {
        Vector<String> rowData = new Vector<>();
        int totalRows = sheet.getRows();
        int totalCol = sheet.getColumns();

        // TODO: Hacer comprobaciones para los casos en que los excel estén mal formados
        if ((totalRows - MARK_SECOND_SHEET_INIT_ROW) > 0) {
            if ((totalCol - SECOND_SHEET_NUM_COLUMNS) >= 0) {
                for (int row = MARK_SECOND_SHEET_INIT_ROW; row < totalRows; row++) { // get all the rows
                    rowData.clear();
                    for (int col = 1; col < SECOND_SHEET_NUM_COLUMNS; col++) { // for each row, get all the columns (cells)
                        rowData.add(sheet.getCell(col, row).getContents());
                    }
                    mDBRevisiones.incluirApoyo(rowData);
                }
            } else {
                throw new IllegalArgumentException(("Illegal number of columns in sheet 2"));
            }
        } else {
            throw new IllegalArgumentException("Illegal number of rows in sheet 2");
        }
    }

    private void readThirdSheet(Sheet sheet) {
        Vector<String> rowData = new Vector<>();
        int totalRows = sheet.getRows();
        int totalCol = sheet.getColumns();

        // TODO: Hacer comprobaciones para los casos en que los excel estén mal formados
        if ((totalRows - MARK_THIRD_SHEET_INIT_ROW) >= 0) {
            if ((totalCol - THIRD_SHEET_NUM_COLUMNS) >= 0) {
                for (int row = MARK_THIRD_SHEET_INIT_ROW; row < totalRows; row++) { // get all the rows
                    rowData.clear();
                    for (int col = 0; col < THIRD_SHEET_NUM_COLUMNS; col++) { // for each row, get all the columns (cells)
                        rowData.add(sheet.getCell(col, row).getContents());
                    }
                    mDBRevisiones.incluirApoyoNoRevisable(rowData);
                }
            } else {
                throw new IllegalArgumentException(("Illegal number of columns in sheet 3"));
            }
        } else {
            throw new IllegalArgumentException("Illegal number of rows in sheet 3");
        }

    }

    private void readRevisionName(Sheet sheet) throws IllegalArgumentException {
        String revisionName = sheet.getCell(REVISION_NAME_COL, REVISION_NAME_ROW).getContents();

        if (revisionName != null && revisionName.contains((SPACE))) {
            revisionName = revisionName.substring(revisionName.lastIndexOf(SPACE) + 1);
        } else {
            throw new IllegalArgumentException("Error in the title cell");
        }

        Aplicacion.tituloRevision = revisionName;

    }
}
