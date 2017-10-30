package com.nipsa.rpr.ExcelHandler;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.nipsa.rpr.Aplicacion;

import java.io.File;
import java.io.IOException;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

/**
 * Created by david on 30/10/17.
 */

public class ExcelReader {

    private File mExcelToRead;
    private Context mContext;

    public ExcelReader(Context context, File excelToRead) {
        this.mContext = context;
        this.mExcelToRead = excelToRead;
    }

    public void readExcelFile() {
        Workbook workbook;
        try {
            workbook = Workbook.getWorkbook(mExcelToRead);
            Sheet sheet = workbook.getSheet(0);
            int numRows = sheet.getRows();
            sheet.getColumns();
            String s = "";
            for (int i = 0; i<numRows; i++){
                Cell[] cells = sheet.getRow(i);
                int numCells = cells.length;
                for (int j = 0; j<numCells; j++) {
                    s += cells[j].getContents() + ", ";
                }
            }
            Toast.makeText(mContext, s, Toast.LENGTH_LONG).show();
        } catch (IOException ioe) {
            Log.e(Aplicacion.TAG, "ExcelReader.readExcelFile: " + ioe.toString());
        } catch (BiffException be) {
            Log.e(Aplicacion.TAG, "ExcelReader.readExcelFile: " + be.toString());
        }
    }
}
