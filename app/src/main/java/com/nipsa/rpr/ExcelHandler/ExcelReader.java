package com.nipsa.rpr.ExcelHandler;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.nipsa.rpr.Aplicacion;
import com.nipsa.rpr.DBRevisiones;
import com.nipsa.rpr.MostrarRevisiones;

import java.io.File;
import java.io.IOException;
import java.util.Vector;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

/**
 * Created by david on 30/10/17.
 */

public class ExcelReader {

    public static final String MARK_FIRST_SHEET = "Tipo Instalación";
    public static final String MARK_SECOND_SHEET = "Acción";
    public static final String MARK_THIRD_SHEET = "Cód. Apoyo / CD";
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
        Vector<String> rowData = new Vector<String>();

        readRevisionName(sheet);

        int totalRows = sheet.getRows();
        Cell cell = sheet.getCell(MARK_FIRST_SHEET);
        int firstRow = cell.getRow() + 1;
        for (int i = firstRow; i < totalRows; i++) { // get all the rows
            rowData.clear();
            for (int j = 0; j < sheet.getColumns(); j++) { // for each rows, get all the columns (cells)
                rowData.add(sheet.getCell(i, j).getContents());
            }
            mDBRevisiones.incluirEquipo(rowData);
        }

    }

    private void readSecondSheet(Sheet sheet) {
        Vector<String> rowData = new Vector<String>();

        int totalRows = sheet.getRows();
        Cell cell = sheet.getCell(MARK_SECOND_SHEET);
        int firstRow = cell.getRow() + 1;
        for (int i = firstRow; i < totalRows; i++) { // get all the rows
            rowData.clear();
            for (int j = 1; j < sheet.getColumns(); j++) { // for each rows, get all the columns (cells)
                rowData.add(sheet.getCell(i, j).getContents());
            }
            mDBRevisiones.incluirApoyo(rowData);
        }

    }

    private void readThirdSheet(Sheet sheet) {
        Vector<String> rowData = new Vector<String>();

        int totalRows = sheet.getRows();
        Cell cell = sheet.getCell(MARK_THIRD_SHEET);
        int firstRow = cell.getRow() + 1;

        for (int i = firstRow; i < totalRows; i++) { // get all the rows
            rowData.clear();
            for (int j = 1; j < sheet.getColumns(); j++) { // for each rows, get all the columns (cells)
                rowData.add(sheet.getCell(i, j).getContents());
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
