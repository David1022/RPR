package com.nipsa.rpr.ExcelHandler;

import android.os.Environment;
import android.util.Log;

import com.nipsa.rpr.Aplicacion;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Locale;

import jxl.Workbook;
import jxl.WorkbookSettings;
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

public class ExcelWriter {

    public static final String LANGUAGE = "en";
    public static final String COUNTRY = "EN";
    public static final String OUTPUT_PATH = "/RPR/OUTPUT/";

    public ExcelWriter() {
    }

    public void generateExampleExcelFile() {
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
            sheet = addDataExample(sheet);
            workbook.write();
            workbook.close();
        } catch (NullPointerException npe) {
            Log.e(Aplicacion.TAG, "ExcelWriter.generateExcelFile: " + npe.toString());
        } catch (MalformedURLException mue) {
            Log.e(Aplicacion.TAG, "ExcelWriter.generateExcelFile: " + mue.toString());
        } catch (IOException ioe) {
            Log.e(Aplicacion.TAG, "ExcelWriter.generateExcelFile: " + ioe.toString());
        } catch (WriteException we) {
            Log.e(Aplicacion.TAG, "ExcelWriter.generateExcelFile: " + we.toString());
        }
    }

    private WritableSheet addDataExample(WritableSheet sheetToFill) throws WriteException, MalformedURLException {
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

    public void generateRevisionExcelFile(String excelFileName) {
        String destPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + OUTPUT_PATH;
        String firstSheetName = "Hoja de equipos";
        String secondSheetName = "Hoja de apoyos";
        String thirdSheetName = "Hoja de apoyos no revisables";

        File directory = new File(destPath);
        // Create the directory if not exists
        if (!directory.exists()) {
            directory.mkdirs();
        }

        try {
            File xlsFile = new File(directory, excelFileName);
            // Generate a workbook in excel file
            WorkbookSettings workbookSettings = new WorkbookSettings();
            workbookSettings.setLocale(new Locale(LANGUAGE, COUNTRY));
            WritableWorkbook workbook = Workbook.createWorkbook(xlsFile, workbookSettings);
            // Generate a worksheet
            WritableSheet firstSheet = workbook.createSheet("Hoja de equipos", 0);
            firstSheet = addDataExample(firstSheet);
//            firstSheet = addFirstSheetData(firstSheet);
            workbook.write();
            workbook.close();
        } catch (NullPointerException npe) {
            Log.e(Aplicacion.TAG, "ExcelWriter.generateExcelFile: " + npe.toString());
        } catch (MalformedURLException mue) {
            Log.e(Aplicacion.TAG, "ExcelWriter.generateExcelFile: " + mue.toString());
        } catch (IOException ioe) {
            Log.e(Aplicacion.TAG, "ExcelWriter.generateExcelFile: " + ioe.toString());
        } catch (WriteException we) {
            Log.e(Aplicacion.TAG, "ExcelWriter.generateExcelFile: " + we.toString());
        }

    }

    private WritableSheet addFirstSheetData (WritableSheet fSheet){

        return fSheet;
    }

}