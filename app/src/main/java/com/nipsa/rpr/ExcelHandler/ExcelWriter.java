package com.nipsa.rpr.ExcelHandler;

import android.content.Context;
import android.database.Cursor;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.util.Log;

import com.nipsa.rpr.Aplicacion;
import com.nipsa.rpr.DBRevisiones;
import com.nipsa.rpr.Defecto;
import com.nipsa.rpr.Revision;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Locale;
import java.util.Vector;

import jxl.CellView;
import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.format.Alignment;
import jxl.format.Border;
import jxl.format.BorderLineStyle;
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

    private static final String LANGUAGE = "en";
    private static final String COUNTRY = "EN";
    private static final String OUTPUT_PATH = "/RPR/OUTPUT/";

    private static final String FIRST_SHEET_TITLE = "Carga y Modificación de Inspecciones";
    private static final String FIRST_SHEET_SUBTITLE = "CARGA DE INSPECCIONES DE TRAZAS Y CD´s ";
    private static final String[] FIRST_SHEET_HEADER = {"Tipo de Inspección", "Territorio", "Nombre Territorio"};
    private static final String[] FIRST_SHEET_SECOND_HEADER = {"Tipo Instalación", "Cód. BDE Instalación",
            "Descripción Instalación", "Posición, Tramo o Localización", "Nombre de Tramo/Código de Tramo : Nombre de Tramo del CD",
            "Descripción Tramo", "Equipo o Apoyo", "Fecha de Inspección", "Defecto o Medida", "Descripción Defecto/Medida",
            "Crit", "Ocurrencias o Medida", "Estado Inst", "Trabajo de Inspección", "Valoración", "Importe", "Límite de Corrección",
            "Trabajo de corrección", "Fecha de Corrección", "D", "C", "Código de Inspección", "Observaciones", "Documento/s a asociar",
            "Descripción documento/s", "Fecha de Alta", "TPL", "Km. Aéreos"};
    public static final String EMPTY_STRING = "";
    public static final String SECOND_SHEET_TITLE = "Carga y Modificación de Apoyos de Inspecciones";
    private static final String SECOND_SHEET_SUBTITLE = "CARGA DE APOYOS DE INSPECCIONES DE TRAZAS Y CD´s ";
    private static final String SECOND_SHEET_HEADER = "Tipo de Inspección";
    private static final String[] SECOND_SHEET_SECOND_HEADER = {"Acción", "Cód. Apoyo/Rótulo", "Observaciones", "Máx. Tens. Redes Soportado", "Oculto Combo Máx. Tens.",
            "Material", "Oculto Combo Material", "Estructura", "Oculto Combo Estructura", "Altura Apoyo", "Huso Apoyo",
            "Huso Combo", "Coordenada X utm  Apoyo", "Coordenada Y utm  Apoyo", "Tipo de Instalación", "Nombre Instalación",
            "Código de Tramo"};
    public static final String THIRD_SHEET_TITLE = "Carga datos de revisión desde Tablet";
    private static final String THIRD_SHEET_SUBTITLE = "DATOS REVISIÓN ";
    private static final String[] THIRD_SHEET_HEADER = {"Metodología Utilizada:", "NIF, Nombre y Apellidos de los Revisores:",
            "Nombre y apellidos del Colegiado que firmará la inspección:", "Equipos utilizados en la inspección (marca y modelo):",
            "Código de Inspección:"};
    private static final String[] THIRD_SHEET_SECOND_HEADER = {"Cód. Apoyo / CD", "Motivo por el que no se ha revisado"};

    private DBRevisiones dbRevisiones;
    private String revision;
    private Context mContext;

    public ExcelWriter(String revision, Context context) {
        this.mContext = context;
        dbRevisiones = new DBRevisiones(mContext);
        this.revision = revision;
    }

    public void generateExampleExcelFile() {
        String destPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + OUTPUT_PATH;
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
            workbookSettings.setLocale(new Locale(LANGUAGE, COUNTRY));
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
        wcf.setBackground(Colour.SKY_BLUE);
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
        String firstSheetName = "UNIDADES_REVISION_" + revision + "_1.xls";
        String secondSheetName = "UNIDADES_REV_APOYO_1.xls";
        String thirdSheetName = "UNIDADES_REV_DATOS_REV_1.xls";

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

            // Generate and fill first sheet
            WritableSheet firstSheet = workbook.createSheet(firstSheetName, 0);
            firstSheet.mergeCells(0, 2, 25, 2);
            addFirstSheetData(firstSheet);

            // Generate and fill second sheet
            WritableSheet secondSheet = workbook.createSheet(secondSheetName, 1);
            addSecondSheetData(secondSheet);

            // Generate and fill third sheet
            WritableSheet thirdSheet = workbook.createSheet(thirdSheetName, 2);
            for (int i = 4; i <= 8; i++) {
                thirdSheet.mergeCells(1, i, 5, i);
            }
            addThirdSheetData(thirdSheet);

            setAutosizeColumns(workbook);
            hideColumns(workbook);

            workbook.write();
            workbook.close();
        } catch (NullPointerException npe) {
            npe.printStackTrace();
            Log.e(Aplicacion.TAG, "ExcelWriter.generateExcelFile: " + npe.toString());
        } catch (MalformedURLException mue) {
            mue.printStackTrace();
            Log.e(Aplicacion.TAG, "ExcelWriter.generateExcelFile: " + mue.toString());
        } catch (IOException ioe) {
            ioe.printStackTrace();
            Log.e(Aplicacion.TAG, "ExcelWriter.generateExcelFile: " + ioe.toString());
        } catch (WriteException we) {
            we.printStackTrace();
            Log.e(Aplicacion.TAG, "ExcelWriter.generateExcelFile: " + we.toString());
        }

    }

    private void hideColumns(WritableWorkbook workbook) {
        CellView cellView = new CellView();
        cellView.setHidden(true);
        int[] hiddenColumnsSheet0 = {4, 9, 12, 14, 15, 16, 17, 19, 20, 26};
        int[] hiddenColumnsSheet1 = {4, 6, 8, 11};

        hideColumns(cellView, hiddenColumnsSheet0, workbook.getSheet(0));
        hideColumns(cellView, hiddenColumnsSheet1, workbook.getSheet(1));
    }

    private void hideColumns(CellView cellView, int[] hiddenColumnsSheet, WritableSheet ws) {
        for (int i = 0; i < hiddenColumnsSheet.length; i++) {
            ws.setColumnView(hiddenColumnsSheet[i], cellView);
        }
    }

    private void setAutosizeColumns(WritableWorkbook workbook) {
        CellView cellView = new CellView();
        cellView.setAutosize(true);
        int numberOfSheets = workbook.getNumberOfSheets();

        for (int i = 0; i < numberOfSheets; i++) {
            WritableSheet ws = workbook.getSheet(i);
            int columns = ws.getColumns();
            for (int col = 0; col < columns; col++) {
                ws.setColumnView(col, cellView);
            }
        }
    }

    private void addFirstSheetData(WritableSheet sheet) throws WriteException {
        fillFirstSheetHeader(sheet);
        fillFirstSheetData(sheet);
    }

    private void fillFirstSheetHeader(WritableSheet sheet) throws WriteException {
        String revisionSubtitle = FIRST_SHEET_SUBTITLE + this.revision;

        sheet.addCell(new Label(0, 0, FIRST_SHEET_TITLE, setupTitleCell()));
        sheet.addCell(new Label(0, 2, revisionSubtitle, setupSubtitleCell()));

        for (int i = 0; i < FIRST_SHEET_HEADER.length; i++) {
            sheet.addCell(new Label(i, 4, FIRST_SHEET_HEADER[i], setupDataCell(Colour.SKY_BLUE, Alignment.CENTRE)));
        }

        WritableCellFormat wcf = setupDataCell(Colour.WHITE, Alignment.CENTRE);
        sheet.addCell(new Label(0, 5, "L", wcf));
        sheet.addCell(new Label(1, 5, "CAT", wcf));
        sheet.addCell(new Label(2, 5, "Cataluña", wcf));

        for (int i = 0; i < FIRST_SHEET_SECOND_HEADER.length; i++) {
            sheet.addCell(new Label(i, (ExcelReader.MARK_FIRST_SHEET_INIT_ROW - 2),
                    FIRST_SHEET_SECOND_HEADER[i], setupDataCell(Colour.SKY_BLUE, Alignment.CENTRE)));
        }
    }

    @NonNull
    private WritableCellFormat setupTitleCell() throws WriteException {
        WritableFont wfTitle = new WritableFont(WritableFont.ARIAL, 14, WritableFont.BOLD, false, UnderlineStyle.SINGLE);
        WritableCellFormat wcfTitle = new WritableCellFormat(wfTitle);
        wcfTitle.setAlignment(Alignment.LEFT);
        return wcfTitle;
    }

    @NonNull
    private WritableCellFormat setupSubtitleCell() throws WriteException {
        WritableFont wfSubtitle = new WritableFont(WritableFont.ARIAL, 12, WritableFont.BOLD, false, UnderlineStyle.NO_UNDERLINE);
        WritableCellFormat wcfSubtitle = new WritableCellFormat(wfSubtitle);
        wcfSubtitle.setAlignment(Alignment.CENTRE);
        return wcfSubtitle;
    }

    @NonNull
    private WritableCellFormat setupDataCell(Colour backgroundCell, Alignment alignment) throws WriteException {
        WritableFont wfHeader = new WritableFont(WritableFont.ARIAL, 8, WritableFont.BOLD, false, UnderlineStyle.NO_UNDERLINE);
        WritableCellFormat wcfHeaderBlue = new WritableCellFormat(wfHeader);
        wcfHeaderBlue.setAlignment(alignment);
        wcfHeaderBlue.setBackground(backgroundCell);
        wcfHeaderBlue.setBorder(Border.ALL, BorderLineStyle.THIN);
        return wcfHeaderBlue;
    }

    private void fillFirstSheetData(WritableSheet sheet) throws WriteException {
        WritableCellFormat wcfData = setupDataCell(Colour.WHITE, Alignment.LEFT);
        Cursor cEquipos = dbRevisiones.solicitarDatosEquipos(revision);

        if ((cEquipos != null) && (cEquipos.moveToFirst())) {
            int row = ExcelReader.MARK_FIRST_SHEET_INIT_ROW - 1;
            do {
                String nombreEquipo = cEquipos.getString(cEquipos.getColumnIndex("NombreEquipo"));
                String tramo = cEquipos.getString(cEquipos.getColumnIndex("PosicionTramo"));
                String tipo = cEquipos.getString(cEquipos.getColumnIndex("TipoInstalacion"));
                Vector<Defecto> listaDefectos = dbRevisiones.solicitarDefectosMedidas(revision, nombreEquipo, tramo);

                for (int col = 0; col < ExcelReader.FIRST_SHEET_NUM_COLUMNS; col++) {
                    switch (col) {
                        case 3:
                            String data;
                            data = tipo.equals("Z") ? EMPTY_STRING : cEquipos.getString(col + 1);
                            sheet.addCell(new Label(col, row, data, wcfData));
                            break;
                        case 7:
                            sheet.addCell(new Label(col, row, corregirFecha(cEquipos.getString(col + 1)), wcfData));
                            break;
                        case 18:
                            sheet.addCell(new Label(col, row, corregirFecha(cEquipos.getString(col + 1)), wcfData));
                            break;
                        case 23:
                            String imagen1 = cEquipos.getString(col + 1);
                            if (!imagen1.equals(EMPTY_STRING)) {
                                File fileImagen1 = new File("/" + imagen1);
                                sheet.addCell(new Label(col, row, imagen1, wcfData));
                                sheet.addHyperlink(new WritableHyperlink(col, row, fileImagen1));
                            } else {
                                sheet.addCell(new Label(col, row, EMPTY_STRING, wcfData));
                            }
                            break;
                        case 24:
                            String imagen2 = cEquipos.getString(col + 1);
                            if (!imagen2.equals(EMPTY_STRING)) {
                                File fileImagen2 = new File("/" + imagen2);
                                sheet.addCell(new Label(col, row, imagen2, wcfData));
                                sheet.addHyperlink(new WritableHyperlink(col, row, fileImagen2));
                            } else {
                                sheet.addCell(new Label(col, row, EMPTY_STRING, wcfData));
                            }
                            break;
                        default:
                            sheet.addCell(new Label(col, row, cEquipos.getString(col + 1), wcfData));
                            break;
                    }
                }
                row++;
                row = addDefectoData(sheet, listaDefectos, tipo, cEquipos, row);
            } while (cEquipos.moveToNext());
            cEquipos.close();
        }
    }

    private int addDefectoData(WritableSheet sheet, Vector<Defecto> listaDefectos, String tipo,
                               Cursor cEquipos, int row) throws WriteException {
        WritableCellFormat wcfData = setupDataCell(Colour.WHITE, Alignment.LEFT);

        if ((listaDefectos != null) && (listaDefectos.size() > 0)) {
            for (int i = 0; i < listaDefectos.size(); i++) {
                Defecto def = listaDefectos.elementAt(i);
                if (def.getEsDefecto().equals(Aplicacion.SI)) {
                    for (int col = 0; col < ExcelReader.FIRST_SHEET_NUM_COLUMNS; col++) {
                        String data;
                        switch (col) {
                            case 3:
                                data = tipo.equals("Z") ? EMPTY_STRING : cEquipos.getString(col + 1);
                                sheet.addCell(new Label(col, row, data, wcfData));
                                break;
                            case 7:
                                sheet.addCell(new Label(col, row, corregirFecha(cEquipos.getString(col + 1)), wcfData));
                                break;
                            case 8: // Se incluye el codigo del defecto
                                sheet.addCell(new Label(col, row, def.getCodigoDefecto(), wcfData));
                                break;
                            case 11: // Se incluye el número de ocurrencias del defecto
                                sheet.addCell(new Label(col, row, def.getOcurrencias(), wcfData));
                                break;
                            case 18: // Si el defecto se ha corregido se incluye la fecha de corrección
                                data = def.getCorregido().equals(Aplicacion.SI) ?
                                        corregirFecha(def.getFechaCorreccion()) : cEquipos.getString(col + 1);
                                sheet.addCell(new Label(col, row, data, wcfData));
                                break;
                            case 22: // Se incluyen las observaciones del defecto además de las del equipo
                                sheet.addCell(new Label(col, row, def.getObservaciones(), wcfData));
                                break;
                            case 23: // Se incluye la foto con hipervínculo
                                String imagen1 = cEquipos.getString(col + 1);
                                if (!imagen1.equals(EMPTY_STRING)) {
                                    File fileImagen1 = new File(imagen1);
                                    sheet.addCell(new Label(col, row, imagen1, wcfData));
                                    sheet.addHyperlink(new WritableHyperlink(col, row, fileImagen1));
                                } else {
                                    sheet.addCell(new Label(col, row, EMPTY_STRING, wcfData));
                                }
                                break;
                            case 24: // Se incluye la foto con hipervínculo
                                String imagen2 = cEquipos.getString(col + 1);
                                if (!imagen2.equals(EMPTY_STRING)) {
                                    File fileImagen2 = new File(imagen2);
                                    sheet.addCell(new Label(col, row, imagen2, wcfData));
                                    sheet.addHyperlink(new WritableHyperlink(col, row, fileImagen2));
                                } else {
                                    sheet.addCell(new Label(col, row, EMPTY_STRING, wcfData));
                                }
                                break;
                            default: // Por defecto se incluye el valor recogido en la BDD
                                sheet.addCell(new Label(col, row, cEquipos.getString(col + 1), wcfData));
                                break;
                        }
                    }
                }
                row++;
                row = addMeasurementDataTr1(sheet, def, tipo, cEquipos, row);
            }
        }
        return row;
    }

    private int addMeasurementDataTr1(WritableSheet sheet, Defecto def, String tipo,
                                      Cursor cEquipos, int row) throws WriteException {
        WritableCellFormat wcfData = setupDataCell(Colour.WHITE, Alignment.LEFT);
        boolean hayMedida = (!def.getMedida().equals(EMPTY_STRING));

        if (hayMedida) {
            boolean hayRmn = false;
            for (int col = 0; col < ExcelReader.FIRST_SHEET_NUM_COLUMNS; col++) {
                String data;
                switch (col) {
                    case 3: // Si es CT no se pone el tramo
                        data = tipo.equals("Z") ? EMPTY_STRING : cEquipos.getString(col + 1);
                        sheet.addCell(new Label(col, row, data, wcfData));
                        break;
                    case 7: // Se corrige la fecha para que tenga el formato pedido por EDE
                        sheet.addCell(new Label(col, row, corregirFecha(cEquipos.getString(col + 1)), wcfData));
                        break;
                    case 8: // Se incluye el codigo de la medida en lugar del codigo del defecto
                        sheet.addCell(new Label(col, row,
                                equivalenciaMedidaCodigo(def.getCodigoDefecto(), def.getPatUnidas()), wcfData));
                        if (def.getCodigoDefecto().equals("T55D") || def.getCodigoDefecto().equals("T55C")) {
                            hayRmn = true;
                            def = calculosPreviosRmnRc(def, "TR1");
                        }
                        break;
                    case 11: // Se incluye la medida en lugar de las ocurrencias
                        sheet.addCell(new Label(col, row, adaptarMedida(def.getMedida()), wcfData));
                        break;
                    case 22: // Se incluyen las observaciones del defecto además de las del equipo
                        data = "TR1";
                        if (!def.getObservaciones().equals(EMPTY_STRING)) {
                            data += ": " + def.getObservaciones();
                        }
                        sheet.addCell(new Label(col, row, data, wcfData));
                        break;
                    case 23: // Se incluye también el vínculo a la foto en las medidas
                        String imagen1 = def.getFoto1();
                        if (!imagen1.equals(EMPTY_STRING)) {
                            File fileImagen1 = new File(imagen1);
                            sheet.addCell(new Label(col, row, imagen1, wcfData));
                            sheet.addHyperlink(new WritableHyperlink(col - 1, row, fileImagen1));
                        } else {
                            sheet.addCell(new Label(col, row, EMPTY_STRING, wcfData));
                        }
                        break;
                    case 24: // Se incluye también el vínculo a la foto en las medidas
                        String imagen2 = def.getFoto2();
                        if (!imagen2.equals(EMPTY_STRING)) {
                            File fileImagen2 = new File(imagen2);
                            sheet.addCell(new Label(col, row, imagen2, wcfData));
                            sheet.addHyperlink(new WritableHyperlink(col - 1, row, fileImagen2));
                        } else {
                            sheet.addCell(new Label(col, row, EMPTY_STRING, wcfData));
                        }
                        break;
                    default:
                        sheet.addCell(new Label(col, row, cEquipos.getString(col + 1), wcfData));
                        break;
                }
            }
            if (hayRmn) {
                row = incluirRc(sheet, cEquipos, def, "TR1", row);
            }
            row++;
            row = addMeasurementDataTr2(sheet, def, tipo, cEquipos, row);
        }
        return row;
    }

    private int addMeasurementDataTr2(WritableSheet sheet, Defecto def, String tipo,
                                      Cursor cEquipos, int row) throws WriteException {
        WritableCellFormat wcfData = setupDataCell(Colour.WHITE, Alignment.LEFT);
        boolean hayMedidaTr2 = (!def.getMedidaTr2().equals(EMPTY_STRING)); // Si además tiene medida de Tr2 se incluye también

        if (hayMedidaTr2) {
            boolean hayRc = false;

            for (int col = 0; col < ExcelReader.FIRST_SHEET_NUM_COLUMNS; col++) {
                String data;
                switch (col) {
                    case 3: // Si es CT no se pone el tramo
                        data = tipo.equals("Z") ? EMPTY_STRING : cEquipos.getString(col + 1);
                        sheet.addCell(new Label(col, row, data, wcfData));
                        break;
                    case 7: // Se corrige la fecha para que tenga el formato pedido por EDE
                        sheet.addCell(new Label(col, row, corregirFecha(cEquipos.getString(col + 1)), wcfData));
                        break;
                    case 8: // Se incluye el codigo de la medida en lugar del codigo del defecto
                        sheet.addCell(new Label(col, row,
                                equivalenciaMedidaCodigo(def.getCodigoDefecto(), def.getPatUnidas()), wcfData));
                        if (def.getCodigoDefecto().equals("T55D") || def.getCodigoDefecto().equals("T55C")) {
                            hayRc = true;
                            def = calculosPreviosRmnRc(def, "TR2");
                        }
                        break;
                    case 11: // Se incluye la medida en lugar de las ocurrencias
                        sheet.addCell(new Label(col, row, adaptarMedida(def.getMedidaTr2()), wcfData));
                        break;
                    case 22: // Se incluyen las observaciones del defecto además de las del equipo
                        data = "TR2";
                        if (!def.getObservaciones().equals(EMPTY_STRING)) {
                            data += ": " + def.getObservaciones();
                        }
                        sheet.addCell(new Label(col, row, data, wcfData));
                        break;
                    case 23: // Se incluye también el vínculo a la foto en las medidas
                        String imagen1 = def.getFoto1();
                        if (!imagen1.equals(EMPTY_STRING)) {
                            File fileImagen1 = new File(imagen1);
                            sheet.addCell(new Label(col, row, imagen1, wcfData));
                            sheet.addHyperlink(new WritableHyperlink(col - 1, row, fileImagen1));
                        } else {
                            sheet.addCell(new Label(col, row, EMPTY_STRING, wcfData));
                        }
                        break;
                    case 24: // Se incluye también el vínculo a la foto en las medidas
                        String imagen2 = def.getFoto2();
                        if (!imagen2.equals(EMPTY_STRING)) {
                            File fileImagen2 = new File(imagen2);
                            sheet.addCell(new Label(col, row, imagen2, wcfData));
                            sheet.addHyperlink(new WritableHyperlink(col - 1, row, fileImagen2));
                        } else {
                            sheet.addCell(new Label(col, row, EMPTY_STRING, wcfData));
                        }
                        break;
                    default:
                        sheet.addCell(new Label(col, row, cEquipos.getString(col + 1), wcfData));
                        break;
                }
            }
            if (hayRc) {
                row = incluirRc(sheet, cEquipos, def, "TR2", row);
            }
            row++;
            row = addMeasurementDataTr3(sheet, def, tipo, cEquipos, row);
        }
        return row;
    }

    private int addMeasurementDataTr3(WritableSheet sheet, Defecto def, String tipo,
                                      Cursor cEquipos, int row) throws WriteException {
        WritableCellFormat wcfData = setupDataCell(Colour.WHITE, Alignment.LEFT);
        boolean hayMedidaTr3 = (!def.getMedidaTr3().equals(EMPTY_STRING));

        if (hayMedidaTr3) {
            boolean hayRc = false;

            for (int col = 0; col < ExcelReader.FIRST_SHEET_NUM_COLUMNS; col++) {
                String data;
                switch (col) {
                    case 3: // Si es CT no se pone el tramo
                        data = tipo.equals("Z") ? EMPTY_STRING : cEquipos.getString(col + 1);
                        sheet.addCell(new Label(col, row, data, wcfData));
                        break;
                    case 7: // Se corrige la fecha para que tenga el formato pedido por EDE
                        sheet.addCell(new Label(col, row, corregirFecha(cEquipos.getString(col + 1)), wcfData));
                        break;
                    case 8:// Se incluye el codigo de la medida en lugar del codigo del defecto
                        sheet.addCell(new Label(col, row,
                                equivalenciaMedidaCodigo(def.getCodigoDefecto(), def.getPatUnidas()), wcfData));
                        if (def.getCodigoDefecto().equals("T55D") || def.getCodigoDefecto().equals("T55C")) {
                            hayRc = true;
                            def = calculosPreviosRmnRc(def, "TR3");
                        }
                        break;
                    case 11: // Se incluye la medida en lugar de las ocurrencias
                        sheet.addCell(new Label(col, row, adaptarMedida(def.getMedidaTr3()), wcfData));
                        break;
                    case 22: // Se incluyen las observaciones del defecto además de las del equipo
                        data = "TR3";
                        if (!def.getObservaciones().equals(EMPTY_STRING)) {
                            data += ": " + def.getObservaciones();
                        }
                        sheet.addCell(new Label(col, row, data, wcfData));
                        break;
                    case 23: // Se incluye también el vínculo a la foto en las medidas
                        String imagen1 = def.getFoto1();
                        if (!imagen1.equals(EMPTY_STRING)) {
                            File fileImagen1 = new File(imagen1);
                            sheet.addCell(new Label(col, row, imagen1, wcfData));
                            sheet.addHyperlink(new WritableHyperlink(col - 1, row, fileImagen1));
                        } else {
                            sheet.addCell(new Label(col, row, EMPTY_STRING, wcfData));
                        }
                        break;
                    case 24: // Se incluye también el vínculo a la foto en las medidas
                        String imagen2 = def.getFoto2();
                        if (!imagen2.equals(EMPTY_STRING)) {
                            File fileImagen2 = new File(imagen2);
                            sheet.addCell(new Label(col, row, imagen2, wcfData));
                            sheet.addHyperlink(new WritableHyperlink(col - 1, row, fileImagen2));
                        } else {
                            sheet.addCell(new Label(col, row, EMPTY_STRING, wcfData));
                        }
                        break;
                    default:
                        sheet.addCell(new Label(col, row, cEquipos.getString(col + 1), wcfData));
                        break;
                }
            }
            if (hayRc) {
                row = incluirRc(sheet, cEquipos, def, "TR3", row);
            }
            row++;
        }
        return row;
    }

    private void addSecondSheetData(WritableSheet sheet) throws WriteException {
        fillSecondSheetHeader(sheet);
        fillSecondSheetData(sheet);
    }

    private void fillSecondSheetHeader(WritableSheet sheet) throws WriteException {
        String revisionSubtitle = SECOND_SHEET_SUBTITLE + this.revision;

        sheet.addCell(new Label(0, 0, SECOND_SHEET_TITLE, setupTitleCell()));
        sheet.addCell(new Label(5, 2, revisionSubtitle, setupSubtitleCell()));
        sheet.addCell(new Label(0, 4, SECOND_SHEET_HEADER, setupDataCell(Colour.SKY_BLUE, Alignment.CENTRE)));
        sheet.addCell(new Label(0, 5, "L", setupDataCell(Colour.WHITE, Alignment.CENTRE)));

        for (int col = 0; col < SECOND_SHEET_SECOND_HEADER.length; col++) {
            sheet.addCell(new Label(col, (ExcelReader.MARK_SECOND_SHEET_INIT_ROW - 2),
                    SECOND_SHEET_SECOND_HEADER[col], setupDataCell(Colour.SKY_BLUE, Alignment.CENTRE)));
        }

    }

    private void fillSecondSheetData(WritableSheet sheet) throws WriteException {
        WritableCellFormat wcfData = setupDataCell(Colour.WHITE, Alignment.LEFT);
        Cursor cApoyos = dbRevisiones.solicitarDatosTodosApoyos(revision);
        int offset = ExcelReader.MARK_SECOND_SHEET_INIT_ROW - 1;

        if ((cApoyos != null) && (cApoyos.moveToFirst())) {
            do {
                int row = cApoyos.getPosition() + offset;
                for (int col = 0; col < ExcelReader.SECOND_SHEET_NUM_COLUMNS + 2; col++) {
                    switch (col) {
                        case 0:
                        case 2: // Se aprovecha el campo para guardar las observaciones del kml de entrada y no se han de imprimir en el excel de salida
                            sheet.addCell(new Label(col, row, EMPTY_STRING, wcfData));
                            break;
                        case 5: // Se toma una columna menos pues la primera columna se genera vacía
                            String tipo = cApoyos.getString(cApoyos.getColumnIndex("TipoInstalacion"));
                            if (tipo.equals("Z")) {
                                sheet.addCell(new Label(col, row, EMPTY_STRING, wcfData));
                            } else {
                                sheet.addCell(new Label(col, row, cApoyos.getString(col), wcfData));
                            }
                            break;
                        default:
                            sheet.addCell(new Label(col, row, cApoyos.getString(col), wcfData));
                            break;
                    }
                }
            } while (cApoyos.moveToNext());
        }
        cApoyos.close();
    }

    private void addThirdSheetData(WritableSheet sheet) throws WriteException {
        fillThirdSheetHeader(sheet);
        fillThirdSheetData(sheet);
    }

    private void fillThirdSheetHeader(WritableSheet sheet) throws WriteException {
        String revisionSubtitle = THIRD_SHEET_SUBTITLE + this.revision;
        Revision rev = dbRevisiones.solicitarRevision(revision);

        sheet.addCell(new Label(0, 0, THIRD_SHEET_TITLE, setupTitleCell()));
        sheet.addCell(new Label(5, 2, revisionSubtitle, setupSubtitleCell()));

        int offset = 4;
        for (int i = 0; i < THIRD_SHEET_HEADER.length; i++) {
            sheet.addCell(new Label(1, offset + i, THIRD_SHEET_HEADER[i], setupDataCell(Colour.SKY_BLUE, Alignment.RIGHT)));
        }
        offset = 4;
        WritableCellFormat wcf = setupDataCell(Colour.WHITE, Alignment.CENTRE);
        sheet.addCell(new Label(6, offset, rev.getMetodologia(), wcf));
        sheet.addCell(new Label(6, offset + 1, rev.getInspector1() + ", " + rev.getInspector2(), wcf));
        sheet.addCell(new Label(6, offset + 2, rev.getColegiado(), wcf));
        sheet.addCell(new Label(6, offset + 3, rev.getEquiposUsados(), wcf));
        sheet.addCell(new Label(6, offset + 4, rev.getCodigoInspeccion(), wcf));

        for (int i = 0; i < THIRD_SHEET_SECOND_HEADER.length; i++) {
            sheet.addCell(new Label(i, (ExcelReader.MARK_THIRD_SHEET_INIT_ROW - 2),
                    THIRD_SHEET_SECOND_HEADER[i], setupDataCell(Colour.SKY_BLUE, Alignment.CENTRE)));
        }

    }

    private void fillThirdSheetData(WritableSheet sheet) throws WriteException {
        WritableCellFormat wcfData = setupDataCell(Colour.WHITE, Alignment.LEFT);
        Cursor cEquiposNoRevisables = dbRevisiones.solicitarDatosEquiposNoRevisables(revision);
        int offset = ExcelReader.MARK_THIRD_SHEET_INIT_ROW - 1;

        if ((cEquiposNoRevisables != null) && (cEquiposNoRevisables.moveToFirst())) {
            do {
                int row = cEquiposNoRevisables.getPosition() + offset;
                for (int col = 0; col < ExcelReader.THIRD_SHEET_NUM_COLUMNS; col++) {
                    sheet.addCell(new Label(col, row, cEquiposNoRevisables.getString(col + 1), wcfData));
                }
            } while (cEquiposNoRevisables.moveToNext());
        }
        cEquiposNoRevisables.close();
    }

    /**
     * @param sheet
     * @param cEquipos
     * @param def
     * @param trafo
     * @return
     */
    public int incluirRc(WritableSheet sheet, Cursor cEquipos, Defecto def, String trafo, int row) throws WriteException {
        WritableCellFormat wcfData = setupDataCell(Colour.WHITE, Alignment.LEFT);

        for (int col = 1; col < ExcelReader.FIRST_SHEET_NUM_COLUMNS + 1; col++) {
            String data;
            switch (col) {
                case 3: // Como es CT no se pone el tramo
                    sheet.addCell(new Label(col, row, EMPTY_STRING, wcfData));
                    break;
                case 7: // Se corrige la fecha para que tenga el formato pedido por EDE
                    sheet.addCell(new Label(col, row, corregirFecha(cEquipos.getString(col)), wcfData));
                    break;
                case 8: // Se incluye el codigo de la medida en lugar del codigo del defecto
                    sheet.addCell(new Label(col, row, "1013", wcfData));
                    break;
                case 11: // Se incluye la medida en lugar de las ocurrencias
                    String rc;
                    if (trafo.equals("TR1")) {
                        rc = def.getRc1();
                    } else if (trafo.equals("TR2")) {
                        rc = def.getRc2();
                    } else {
                        rc = def.getRc3();
                    }
                    sheet.addCell(new Label(col, row, rc, wcfData));
                    break;
                case 22: // Se incluyen las observaciones del defecto además de las del equipo
                    data = "TR1";
                    if (!def.getObservaciones().equals("")) {
                        data += ": " + def.getObservaciones();
                    }
                    sheet.addCell(new Label(col, row, data, wcfData));
                    break;
                case 23: // Se incluye también el vínculo a la foto en las medidas
                    String imagen1 = def.getFoto1();
                    if (!imagen1.equals(EMPTY_STRING)) {
                        File fileImagen1 = new File(imagen1);
                        sheet.addCell(new Label(col, row, imagen1, wcfData));
                        sheet.addHyperlink(new WritableHyperlink(col - 1, row, fileImagen1));
                    } else {
                        sheet.addCell(new Label(col, row, EMPTY_STRING, wcfData));
                    }
                    break;
                case 24: // Se incluye también el vínculo a la foto en las medidas
                    String imagen2 = def.getFoto2();
                    if (!imagen2.equals(EMPTY_STRING)) {
                        File fileImagen2 = new File("\"" + imagen2 + "\"");
                        sheet.addHyperlink(new WritableHyperlink(col - 1, row, fileImagen2));
                        sheet.addCell(new Label(col, row, imagen2, wcfData));
                    } else {
                        sheet.addCell(new Label(col, row, EMPTY_STRING, wcfData));
                    }
                    break;
                default:
                    sheet.addCell(new Label(col, row, cEquipos.getString(col), wcfData));
                    break;
            }
            row++;
        }
        return row;
    }

    /**
     * Corrige la fecha para darle un formato dd/mm/yyyy
     *
     * @param fecha
     * @return
     */
    public static String corregirFecha(String fecha) {
        String fechaCorregida = "";
        if (fecha != null) {
            if (!fecha.equals("")) {
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
                } catch (Exception e) {
                } finally {
                    return fechaCorregida;
                }
            } else {
                return "";
            }
        } else {
            return null;
        }
    }

    public static String equivalenciaMedidaCodigo(String codigoDefecto, String patUnidas) {
        String medida;
        switch (codigoDefecto) {
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

    /**
     * Método para arreglar Rmn y Rc débido a las modificaciones introducidas en las primeras versiones:
     * Caso 1: Si no hay Rc significa que la tablet ha recogido el valor de Rc en el campo de Rmn (caso de las
     * primeras versiones de la app), por lo tanto hay que hacer el cálculo inverso para tomar la Rmn y
     * "imprimir" Rmn y Rc
     * Caso 2: Si hay Rc significa que la tablet ha recogido los datos de Rmn y de Rc de forma independiente y
     * sólo hay que "imprimir" Rmn y Rc en el .xml. En este caso no se realiza ninguna acción en este método
     *
     * @param defecto
     * @param trafo
     */
    public Defecto calculosPreviosRmnRc(Defecto defecto, String trafo) {
        boolean hayRc;
        Double rm, rn, rmn, rc;
        Defecto defRm, defRn;
//        DBRevisiones dbRevisiones = new DBRevisiones();

        // Se recuperan los valores de Rm y Rn para realizar el cálculo de Rmn
        switch (trafo) {
            case "TR1":
                try {
                    // Si se produce excepción significa que no hay ningún valor guardado en Rc o que
                    // el valor guardado no es correcto. Entonces se calcula Rc y sustituye en el registro (defecto)
                    rc = Double.parseDouble(defecto.getRc1());
                } catch (Exception e) {
                    // Se recuperan los registros (defectos) que contienen los valores Rm, Rn del equipo
                    String codDef = defecto.getCodigoDefecto();
                    codDef = codDef.replace("55", "53");
                    defRm = dbRevisiones.solicitarDefecto(defecto.getNombreRevision(), defecto.getNombreEquipo(),
                            codDef, defecto.getTramo());
                    codDef = codDef.replace("53", "62");
                    defRn = dbRevisiones.solicitarDefecto(defecto.getNombreRevision(), defecto.getNombreEquipo(),
                            codDef, defecto.getTramo());
                    try {
                        rm = Double.parseDouble(defRm.getMedida());
                        rn = Double.parseDouble(defRn.getMedida());
                        rc = Double.parseDouble(defecto.getMedida());
                        rmn = (rm + rn - (2 * rc));
                        rmn = redondearA2Decimales(rmn);
                        defecto.setMedida(rmn.toString());
                        defecto.setRc1(rc.toString());
                        dbRevisiones.actualizarItemDefecto(defecto.getNombreRevision(), defecto.getNombreEquipo(),
                                defecto.getTramo(), defecto.getCodigoDefecto(), "Medida", rmn.toString());
                        dbRevisiones.actualizarItemDefecto(defecto.getNombreRevision(), defecto.getNombreEquipo(),
                                defecto.getTramo(), defecto.getCodigoDefecto(), "Rc1", rc.toString());
                    } catch (Exception ex) {

                    }
                }
                break;
            case "TR2":
                try {
                    // Si se produce excepción significa que no hay ningún valor guardado en Rc o que
                    // el valor guardado no es correcto. Entonces se calcula Rc y sustituye en el registro (defecto)
                    rc = Double.parseDouble(defecto.getRc2());
                } catch (Exception e) {
                    // Se recuperan los registros (defectos) que contienen los valores Rm, Rn del equipo
                    String codDef = defecto.getCodigoDefecto();
                    codDef = codDef.replace("55", "53");
                    defRm = dbRevisiones.solicitarDefecto(defecto.getNombreRevision(), defecto.getNombreEquipo(),
                            codDef, defecto.getTramo());
                    codDef = codDef.replace("53", "62");
                    defRn = dbRevisiones.solicitarDefecto(defecto.getNombreRevision(), defecto.getNombreEquipo(),
                            codDef, defecto.getTramo());
                    try {
                        rm = Double.parseDouble(defRm.getMedidaTr2());
                        rn = Double.parseDouble(defRn.getMedidaTr2());
                        rc = Double.parseDouble(defecto.getMedidaTr2());
                        rmn = (rm + rn - (2 * rc));
                        rmn = redondearA2Decimales(rmn);
                        defecto.setMedidaTr2(rmn.toString());
                        defecto.setRc2(rc.toString());
                        dbRevisiones.actualizarItemDefecto(defecto.getNombreRevision(), defecto.getNombreEquipo(),
                                defecto.getTramo(), defecto.getCodigoDefecto(), "MedidaTr2", rmn.toString());
                        dbRevisiones.actualizarItemDefecto(defecto.getNombreRevision(), defecto.getNombreEquipo(),
                                defecto.getTramo(), defecto.getCodigoDefecto(), "Rc2", rc.toString());
                    } catch (Exception ex) {

                    }
                }
                break;
            case "TR3":
                try {
                    // Si se produce excepción significa que no hay ningún valor guardado en Rc o que
                    // el valor guardado no es correcto. Entonces se calcula Rc y sustituye en el registro (defecto)
                    rc = Double.parseDouble(defecto.getRc3());
                } catch (Exception e) {
                    // Se recuperan los registros (defectos) que contienen los valores Rm, Rn del equipo
                    String codDef = defecto.getCodigoDefecto();
                    codDef = codDef.replace("55", "53");
                    defRm = dbRevisiones.solicitarDefecto(defecto.getNombreRevision(), defecto.getNombreEquipo(),
                            codDef, defecto.getTramo());
                    codDef = codDef.replace("53", "62");
                    defRn = dbRevisiones.solicitarDefecto(defecto.getNombreRevision(), defecto.getNombreEquipo(),
                            codDef, defecto.getTramo());
                    try {
                        rm = Double.parseDouble(defRm.getMedidaTr3());
                        rn = Double.parseDouble(defRn.getMedidaTr3());
                        rc = Double.parseDouble(defecto.getMedidaTr3());
                        rmn = (rm + rn - (2 * rc));
                        rmn = redondearA2Decimales(rmn);
                        defecto.setMedidaTr3(rmn.toString());
                        defecto.setRc3(rc.toString());
                        dbRevisiones.actualizarItemDefecto(defecto.getNombreRevision(), defecto.getNombreEquipo(),
                                defecto.getTramo(), defecto.getCodigoDefecto(), "MedidaTr3", rmn.toString());
                        dbRevisiones.actualizarItemDefecto(defecto.getNombreRevision(), defecto.getNombreEquipo(),
                                defecto.getTramo(), defecto.getCodigoDefecto(), "Rc3", rc.toString());
                    } catch (Exception ex) {

                    }
                }
                break;
            default:
                break;
        }

        return defecto;
    }

    public static Double redondearA2Decimales(Double valor) {
        valor = valor * 100;
        double valorRedondeado = Math.round(valor);
        valor = valorRedondeado / 100;

        return valor;
    }

}