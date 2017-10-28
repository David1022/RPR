package com.nipsa.rpr;

import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Locale;

import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.biff.FontRecord;
import jxl.format.Alignment;
import jxl.format.Colour;
import jxl.format.UnderlineStyle;
import jxl.write.Label;
import jxl.write.WritableCell;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableHyperlink;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;

public class ExcelHandler {

    public ExcelHandler() {
    }

    public void generateExcelFile() {
        String destPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/RPR/OUTPUT/";
        String excelFileName = "excelData.xls";
        File directory = new File(destPath);
        // Create the directory if not exists
        if (!directory.exists()) {
            directory.mkdirs();
        }
        try {
            File xlsFile = new File(directory, excelFileName);
            // Generate a workbook for the excel file
            WorkbookSettings workbookSettings = new WorkbookSettings();
            workbookSettings.setLocale(new Locale("en", "EN"));
            WritableWorkbook workbook = Workbook.createWorkbook(xlsFile, workbookSettings);
            // Generate a worksheet
            WritableSheet sheet = workbook.createSheet("Hoja de equipos", 0);
            sheet = addData(sheet);
            workbook.write();
            workbook.close();
        } catch (NullPointerException npe) {
            Log.e(Aplicacion.TAG, "ExcelHandler.generateExcelFile: " + npe.toString());
        } catch (MalformedURLException mue) {
            Log.e(Aplicacion.TAG, "ExcelHandler.generateExcelFile: " + mue.toString());
        } catch (IOException ioe) {
            Log.e(Aplicacion.TAG, "ExcelHandler.generateExcelFile: " + ioe.toString());
        } catch (WriteException we) {
            Log.e(Aplicacion.TAG, "ExcelHandler.generateExcelFile: " + we.toString());
        }
    }

    public void leerExcel() {
        // TODO: Leer archivo excel
    }
    private WritableSheet addData(WritableSheet sheetToFill) throws WriteException, MalformedURLException {
        WritableFont wf = new WritableFont(WritableFont.ARIAL, 10, WritableFont.BOLD, true, UnderlineStyle.SINGLE);
        URL url = new URL("http://www.google.es");
        WritableHyperlink wh = new WritableHyperlink(1, 1, url);

        WritableCellFormat wcf = new WritableCellFormat(wf);
        wcf.setBackground(Colour.LIGHT_BLUE);
        wcf.setAlignment(Alignment.JUSTIFY);
        WritableCell cell1 = new Label(0, 0, "Celda de texto", wcf);
        WritableCell cell2 = new Label(1, 0, "Celda numerica");
        sheetToFill.addCell(cell1);
        sheetToFill.addCell(cell2);
        sheetToFill.addCell(new Label(0, 1, "Texto"));
        sheetToFill.addHyperlink(wh);
        sheetToFill.addCell(new Label(1, 1, "33", wcf));

        return sheetToFill;
    }

}