package com.nipsa.rpr;
/**
 * Abrir terminal e ir a la carpeta
 *              C:\Program Files\Java\jdk1.8.0_111\bin
 * Ejecutar el comando
 *              keytool -list -v -alias "Key alias" -keystore "Key store path" -storepass "Key store password" -keypass "Key password"
 *              EJEMPLO: keytool -list -v -alias RPR -keystore "C:/Users/david.mendano/Desktop/APP RPR/AndroidStudioProjects/Nipsa.jks" -storepass nipsa2017 -keypass nipsarpr
 * Copiar huella deigital
 *              8D:BD:18:00:27:32:EE:AE:B3:F3:9F:9A:D0:71:93:17:92:EF:A9:89
 * La nueva clave se debe poner en el archivo correspondiente segun la huella digital (debug o release)
 *              C:\Users\david.mendano\Desktop\APP RPR\AndroidStudioProjects\RPR\app\src\----DEBUG----\res\values
 *              C:\Users\david.mendano\Desktop\APP RPR\AndroidStudioProjects\RPR\app\src\----RELEASE----\res\values
 */

import android.app.Application;
import android.content.Context;
import android.database.Cursor;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Vector;

/* TODO:
 * Mover fotos
 *
 * Marcar como defecto los valores de PaT
 * Mostrar las rutas
 * Si no se encuentran equipos no trasladar las coordenadas del KML de lectura
 */
public class Aplicacion extends Application {

    public static final String ENCABEZADO_XML_ARCHIVO =
            "<?xml version=\"1.0\"?>\n"+
                    "<?mso-application progid=\"Excel.Sheet\"?>\n"+
                    "<Workbook xmlns=\"urn:schemas-microsoft-com:office:spreadsheet\"\n"+
                    " xmlns:o=\"urn:schemas-microsoft-com:office:office\"\n"+
                    " xmlns:x=\"urn:schemas-microsoft-com:office:excel\"\n"+
                    " xmlns:ss=\"urn:schemas-microsoft-com:office:spreadsheet\"\n"+
                    " xmlns:html=\"http://www.w3.org/TR/REC-html40\">\n"+
                    " <DocumentProperties xmlns=\"urn:schemas-microsoft-com:office:office\">\n"+
                    "  <Author>GOM</Author>\n"+
                    "  <LastAuthor>david.mendano</LastAuthor>\n"+
                    "  <Created>2005-04-04T10:00:56Z</Created>\n"+
                    "  <LastSaved>2017-07-27T08:30:40Z</LastSaved>\n"+
                    "  <Company>Grupo Endesa</Company>\n"+
                    "  <Version>12.00</Version>\n"+
                    " </DocumentProperties>\n"+
                    " <ExcelWorkbook xmlns=\"urn:schemas-microsoft-com:office:excel\">\n"+
                    "  <SupBook>\n"+
                    "   <Path>P:\\ge\\gom\\capture\\Inet\\Exe\\MacrosGOM.xls</Path>\n"+
                    "   <SheetName>MacrosGOM</SheetName>\n"+
                    "   <ExternName>\n"+
                    "    <Name>AniadirFila</Name>\n"+
                    "   </ExternName>\n"+
                    "   <ExternName>\n"+
                    "    <Name>EliminarFila</Name>\n"+
                    "   </ExternName>\n"+
                    "   <ExternName>\n"+
                    "    <Name>ModificarrFila</Name>\n"+
                    "   </ExternName>\n"+
                    "   <ExternName>\n"+
                    "    <Name>Mostrar_Ocultar_Columnas</Name>\n"+
                    "   </ExternName>\n"+
                    "  </SupBook>\n"+
                    "  <WindowHeight>10830</WindowHeight>\n"+
                    "  <WindowWidth>13935</WindowWidth>\n"+
                    "  <WindowTopX>240</WindowTopX>\n"+
                    "  <WindowTopY>45</WindowTopY>\n"+
                    "  <ProtectStructure>False</ProtectStructure>\n"+
                    "  <ProtectWindows>False</ProtectWindows>\n"+
                    " </ExcelWorkbook>\n"+
                    " <Styles>\n"+
                    "  <Style ss:ID=\"Default\" ss:Name=\"Normal\">\n"+
                    "   <Alignment ss:Vertical=\"Bottom\"/>\n"+
                    "   <Borders/>\n"+
                    "   <Font ss:FontName=\"Arial\"/>\n"+
                    "   <Interior/>\n"+
                    "   <NumberFormat/>\n"+
                    "   <Protection/>\n"+
                    "  </Style>\n"+
                    "  <Style ss:ID=\"m14596084\">\n"+
                    "   <Alignment ss:Horizontal=\"Center\" ss:Vertical=\"Bottom\"/>\n"+
                    "   <Borders>\n"+
                    "    <Border ss:Position=\"Bottom\" ss:LineStyle=\"Continuous\" ss:Weight=\"1\"/>\n"+
                    "    <Border ss:Position=\"Left\" ss:LineStyle=\"Continuous\" ss:Weight=\"1\"/>\n"+
                    "    <Border ss:Position=\"Right\" ss:LineStyle=\"Continuous\" ss:Weight=\"1\"\n"+
                    "     ss:Color=\"#000000\"/>\n"+
                    "    <Border ss:Position=\"Top\" ss:LineStyle=\"Continuous\" ss:Weight=\"1\"/>\n"+
                    "   </Borders>\n"+
                    "   <Font ss:FontName=\"Arial\" x:Family=\"Swiss\" ss:Size=\"8\" ss:Bold=\"1\"/>\n"+
                    "   <Interior ss:Color=\"#99CCFF\" ss:Pattern=\"Solid\"/>\n"+
                    "  </Style>\n"+
                    "  <Style ss:ID=\"m14596960\">\n"+
                    "   <Alignment ss:Horizontal=\"Right\" ss:Vertical=\"Bottom\" ss:WrapText=\"1\"/>\n"+
                    "   <Borders>\n"+
                    "    <Border ss:Position=\"Bottom\" ss:LineStyle=\"Continuous\" ss:Weight=\"1\"/>\n"+
                    "    <Border ss:Position=\"Left\" ss:LineStyle=\"Continuous\" ss:Weight=\"1\"/>\n"+
                    "    <Border ss:Position=\"Right\" ss:LineStyle=\"Continuous\" ss:Weight=\"1\"\n"+
                    "     ss:Color=\"#000000\"/>\n"+
                    "    <Border ss:Position=\"Top\" ss:LineStyle=\"Continuous\" ss:Weight=\"1\"/>\n"+
                    "   </Borders>\n"+
                    "   <Font ss:FontName=\"Arial\" x:Family=\"Swiss\" ss:Size=\"8\" ss:Bold=\"1\"/>\n"+
                    "   <Interior ss:Color=\"#99CCFF\" ss:Pattern=\"Solid\"/>\n"+
                    "  </Style>\n"+
                    "  <Style ss:ID=\"m14596980\">\n"+
                    "   <Alignment ss:Horizontal=\"Right\" ss:Vertical=\"Bottom\" ss:WrapText=\"1\"/>\n"+
                    "   <Borders>\n"+
                    "    <Border ss:Position=\"Bottom\" ss:LineStyle=\"Continuous\" ss:Weight=\"1\"/>\n"+
                    "    <Border ss:Position=\"Left\" ss:LineStyle=\"Continuous\" ss:Weight=\"1\"/>\n"+
                    "    <Border ss:Position=\"Right\" ss:LineStyle=\"Continuous\" ss:Weight=\"1\"\n"+
                    "     ss:Color=\"#000000\"/>\n"+
                    "    <Border ss:Position=\"Top\" ss:LineStyle=\"Continuous\" ss:Weight=\"1\"/>\n"+
                    "   </Borders>\n"+
                    "   <Font ss:FontName=\"Arial\" x:Family=\"Swiss\" ss:Size=\"8\" ss:Bold=\"1\"/>\n"+
                    "   <Interior ss:Color=\"#99CCFF\" ss:Pattern=\"Solid\"/>\n"+
                    "  </Style>\n"+
                    "  <Style ss:ID=\"m14597000\">\n"+
                    "   <Alignment ss:Horizontal=\"Right\" ss:Vertical=\"Bottom\" ss:WrapText=\"1\"/>\n"+
                    "   <Borders>\n"+
                    "    <Border ss:Position=\"Bottom\" ss:LineStyle=\"Continuous\" ss:Weight=\"1\"/>\n"+
                    "    <Border ss:Position=\"Left\" ss:LineStyle=\"Continuous\" ss:Weight=\"1\"/>\n"+
                    "    <Border ss:Position=\"Right\" ss:LineStyle=\"Continuous\" ss:Weight=\"1\"\n"+
                    "     ss:Color=\"#000000\"/>\n"+
                    "    <Border ss:Position=\"Top\" ss:LineStyle=\"Continuous\" ss:Weight=\"1\"/>\n"+
                    "   </Borders>\n"+
                    "   <Font ss:FontName=\"Arial\" x:Family=\"Swiss\" ss:Size=\"8\" ss:Bold=\"1\"/>\n"+
                    "   <Interior ss:Color=\"#99CCFF\" ss:Pattern=\"Solid\"/>\n"+
                    "  </Style>\n"+
                    "  <Style ss:ID=\"m14597020\">\n"+
                    "   <Alignment ss:Horizontal=\"Right\" ss:Vertical=\"Bottom\" ss:WrapText=\"1\"/>\n"+
                    "   <Borders>\n"+
                    "    <Border ss:Position=\"Bottom\" ss:LineStyle=\"Continuous\" ss:Weight=\"1\"/>\n"+
                    "    <Border ss:Position=\"Left\" ss:LineStyle=\"Continuous\" ss:Weight=\"1\"/>\n"+
                    "    <Border ss:Position=\"Right\" ss:LineStyle=\"Continuous\" ss:Weight=\"1\"\n"+
                    "     ss:Color=\"#000000\"/>\n"+
                    "    <Border ss:Position=\"Top\" ss:LineStyle=\"Continuous\" ss:Weight=\"1\"/>\n"+
                    "   </Borders>\n"+
                    "   <Font ss:FontName=\"Arial\" x:Family=\"Swiss\" ss:Size=\"8\" ss:Bold=\"1\"/>\n"+
                    "   <Interior ss:Color=\"#99CCFF\" ss:Pattern=\"Solid\"/>\n"+
                    "  </Style>\n"+
                    "  <Style ss:ID=\"m14597040\">\n"+
                    "   <Alignment ss:Horizontal=\"Right\" ss:Vertical=\"Bottom\" ss:WrapText=\"1\"/>\n"+
                    "   <Borders>\n"+
                    "    <Border ss:Position=\"Bottom\" ss:LineStyle=\"Continuous\" ss:Weight=\"1\"/>\n"+
                    "    <Border ss:Position=\"Left\" ss:LineStyle=\"Continuous\" ss:Weight=\"1\"/>\n"+
                    "    <Border ss:Position=\"Right\" ss:LineStyle=\"Continuous\" ss:Weight=\"1\"\n"+
                    "     ss:Color=\"#000000\"/>\n"+
                    "    <Border ss:Position=\"Top\" ss:LineStyle=\"Continuous\" ss:Weight=\"1\"/>\n"+
                    "   </Borders>\n"+
                    "   <Font ss:FontName=\"Arial\" x:Family=\"Swiss\" ss:Size=\"8\" ss:Bold=\"1\"/>\n"+
                    "   <Interior ss:Color=\"#99CCFF\" ss:Pattern=\"Solid\"/>\n"+
                    "  </Style>\n"+
                    "  <Style ss:ID=\"s15\">\n"+
                    "   <Font ss:FontName=\"Arial\"/>\n"+
                    "  </Style>\n"+
                    "  <Style ss:ID=\"s64\">\n"+
                    "   <Font ss:FontName=\"Arial\" ss:Size=\"8\"/>\n"+
                    "  </Style>\n"+
                    "  <Style ss:ID=\"s65\">\n"+
                    "   <Font ss:FontName=\"Arial\" ss:Size=\"8\"/>\n"+
                    "   <NumberFormat ss:Format=\"@\"/>\n"+
                    "  </Style>\n"+
                    "  <Style ss:ID=\"s66\">\n"+
                    "   <Font ss:FontName=\"Comic Sans MS\" x:Family=\"Script\" ss:Size=\"14\" ss:Bold=\"1\"\n"+
                    "    ss:Underline=\"Single\"/>\n"+
                    "  </Style>\n"+
                    "  <Style ss:ID=\"s68\">\n"+
                    "   <Font ss:FontName=\"Arial\" x:Family=\"Swiss\" ss:Size=\"8\" ss:Color=\"#FFFFFF\"/>\n"+
                    "  </Style>\n"+
                    "  <Style ss:ID=\"s69\">\n"+
                    "   <Font ss:FontName=\"Arial\" x:Family=\"Swiss\" ss:Size=\"8\"/>\n"+
                    "  </Style>\n"+
                    "  <Style ss:ID=\"s70\">\n"+
                    "   <Font ss:FontName=\"Arial\" x:Family=\"Swiss\" ss:Size=\"8\"/>\n"+
                    "   <NumberFormat ss:Format=\"@\"/>\n"+
                    "  </Style>\n"+
                    "  <Style ss:ID=\"s71\">\n"+
                    "   <Font ss:FontName=\"Arial\" x:Family=\"Swiss\" ss:Size=\"8\"/>\n"+
                    "   <NumberFormat/>\n"+
                    "  </Style>\n"+
                    "  <Style ss:ID=\"s72\">\n"+
                    "   <Font ss:FontName=\"Arial\" x:Family=\"Swiss\" ss:Size=\"14\"/>\n"+
                    "  </Style>\n"+
                    "  <Style ss:ID=\"s73\">\n"+
                    "   <Alignment ss:Horizontal=\"Center\" ss:Vertical=\"Bottom\"/>\n"+
                    "   <Font ss:FontName=\"Comic Sans MS\" x:Family=\"Script\" ss:Size=\"12\" ss:Bold=\"1\"/>\n"+
                    "  </Style>\n"+
                    "  <Style ss:ID=\"s74\">\n"+
                    "   <Alignment ss:Horizontal=\"Center\" ss:Vertical=\"Bottom\"/>\n"+
                    "   <Font ss:FontName=\"Arial\" x:Family=\"Swiss\" ss:Size=\"12\" ss:Bold=\"1\"/>\n"+
                    "  </Style>\n"+
                    "  <Style ss:ID=\"s75\">\n"+
                    "   <Alignment ss:Horizontal=\"Center\" ss:Vertical=\"Bottom\"/>\n"+
                    "   <Font ss:FontName=\"Arial\" x:Family=\"Swiss\" ss:Size=\"12\" ss:Bold=\"1\"/>\n"+
                    "   <NumberFormat/>\n"+
                    "  </Style>\n"+
                    "  <Style ss:ID=\"s76\">\n"+
                    "   <Alignment ss:Horizontal=\"Center\" ss:Vertical=\"Bottom\" ss:WrapText=\"1\"/>\n"+
                    "   <Borders>\n"+
                    "    <Border ss:Position=\"Bottom\" ss:LineStyle=\"Continuous\" ss:Weight=\"1\"/>\n"+
                    "    <Border ss:Position=\"Left\" ss:LineStyle=\"Continuous\" ss:Weight=\"1\"/>\n"+
                    "    <Border ss:Position=\"Right\" ss:LineStyle=\"Continuous\" ss:Weight=\"1\"/>\n"+
                    "    <Border ss:Position=\"Top\" ss:LineStyle=\"Continuous\" ss:Weight=\"1\"/>\n"+
                    "   </Borders>\n"+
                    "   <Font ss:FontName=\"Arial\" x:Family=\"Swiss\" ss:Size=\"8\" ss:Bold=\"1\"/>\n"+
                    "   <Interior ss:Color=\"#99CCFF\" ss:Pattern=\"Solid\"/>\n"+
                    "  </Style>\n"+
                    "  <Style ss:ID=\"s77\">\n"+
                    "   <Alignment ss:Horizontal=\"Center\" ss:Vertical=\"Bottom\" ss:WrapText=\"1\"/>\n"+
                    "   <Borders>\n"+
                    "    <Border ss:Position=\"Bottom\" ss:LineStyle=\"Continuous\" ss:Weight=\"1\"/>\n"+
                    "    <Border ss:Position=\"Right\" ss:LineStyle=\"Continuous\" ss:Weight=\"1\"/>\n"+
                    "    <Border ss:Position=\"Top\" ss:LineStyle=\"Continuous\" ss:Weight=\"1\"/>\n"+
                    "   </Borders>\n"+
                    "   <Font ss:FontName=\"Arial\" x:Family=\"Swiss\" ss:Size=\"8\" ss:Bold=\"1\"/>\n"+
                    "   <Interior ss:Color=\"#99CCFF\" ss:Pattern=\"Solid\"/>\n"+
                    "  </Style>\n"+
                    "  <Style ss:ID=\"s78\">\n"+
                    "   <Alignment ss:Horizontal=\"Center\" ss:Vertical=\"Bottom\"/>\n"+
                    "   <Borders>\n"+
                    "    <Border ss:Position=\"Bottom\" ss:LineStyle=\"Continuous\" ss:Weight=\"1\"/>\n"+
                    "    <Border ss:Position=\"Left\" ss:LineStyle=\"Continuous\" ss:Weight=\"1\"/>\n"+
                    "    <Border ss:Position=\"Right\" ss:LineStyle=\"Continuous\" ss:Weight=\"1\"/>\n"+
                    "   </Borders>\n"+
                    "   <Font ss:FontName=\"Arial\" ss:Size=\"8\"/>\n"+
                    "  </Style>\n"+
                    "  <Style ss:ID=\"s79\">\n"+
                    "   <Alignment ss:Horizontal=\"Center\" ss:Vertical=\"Bottom\"/>\n"+
                    "   <Borders>\n"+
                    "    <Border ss:Position=\"Bottom\" ss:LineStyle=\"Continuous\" ss:Weight=\"1\"/>\n"+
                    "    <Border ss:Position=\"Right\" ss:LineStyle=\"Continuous\" ss:Weight=\"1\"/>\n"+
                    "    <Border ss:Position=\"Top\" ss:LineStyle=\"Continuous\" ss:Weight=\"1\"/>\n"+
                    "   </Borders>\n"+
                    "   <Font ss:FontName=\"Arial\" ss:Size=\"8\"/>\n"+
                    "  </Style>\n"+
                    "  <Style ss:ID=\"s80\">\n"+
                    "   <Alignment ss:Horizontal=\"Left\" ss:Vertical=\"Bottom\"/>\n"+
                    "   <Borders>\n"+
                    "    <Border ss:Position=\"Bottom\" ss:LineStyle=\"Continuous\" ss:Weight=\"1\"/>\n"+
                    "   </Borders>\n"+
                    "   <Font ss:FontName=\"Arial\" ss:Size=\"8\"/>\n"+
                    "  </Style>\n"+
                    "  <Style ss:ID=\"s81\">\n"+
                    "   <Alignment ss:Horizontal=\"Left\" ss:Vertical=\"Bottom\"/>\n"+
                    "   <Borders>\n"+
                    "    <Border ss:Position=\"Bottom\" ss:LineStyle=\"Continuous\" ss:Weight=\"1\"/>\n"+
                    "   </Borders>\n"+
                    "   <Font ss:FontName=\"Arial\" x:Family=\"Swiss\" ss:Size=\"8\"/>\n"+
                    "  </Style>\n"+
                    "  <Style ss:ID=\"s82\">\n"+
                    "   <Borders>\n"+
                    "    <Border ss:Position=\"Bottom\" ss:LineStyle=\"Continuous\" ss:Weight=\"1\"/>\n"+
                    "   </Borders>\n"+
                    "   <Font ss:FontName=\"Arial\" ss:Size=\"8\"/>\n"+
                    "  </Style>\n"+
                    "  <Style ss:ID=\"s83\">\n"+
                    "   <Borders>\n"+
                    "    <Border ss:Position=\"Bottom\" ss:LineStyle=\"Continuous\" ss:Weight=\"1\"/>\n"+
                    "   </Borders>\n"+
                    "   <Font ss:FontName=\"Arial\" ss:Size=\"8\"/>\n"+
                    "   <NumberFormat ss:Format=\"@\"/>\n"+
                    "  </Style>\n"+
                    "  <Style ss:ID=\"s84\">\n"+
                    "   <Alignment ss:Horizontal=\"Center\" ss:Vertical=\"Bottom\"/>\n"+
                    "   <Borders>\n"+
                    "    <Border ss:Position=\"Bottom\" ss:LineStyle=\"Continuous\" ss:Weight=\"1\"/>\n"+
                    "   </Borders>\n"+
                    "   <Font ss:FontName=\"Arial\" ss:Size=\"8\"/>\n"+
                    "  </Style>\n"+
                    "  <Style ss:ID=\"s85\">\n"+
                    "   <Alignment ss:Horizontal=\"Right\" ss:Vertical=\"Bottom\"/>\n"+
                    "   <Borders>\n"+
                    "    <Border ss:Position=\"Bottom\" ss:LineStyle=\"Continuous\" ss:Weight=\"1\"/>\n"+
                    "   </Borders>\n"+
                    "   <Font ss:FontName=\"Arial\" ss:Size=\"8\"/>\n"+
                    "  </Style>\n"+
                    "  <Style ss:ID=\"s86\">\n"+
                    "   <Alignment ss:Horizontal=\"Center\" ss:Vertical=\"Bottom\" ss:WrapText=\"1\"/>\n"+
                    "   <Borders>\n"+
                    "    <Border ss:Position=\"Bottom\" ss:LineStyle=\"Continuous\" ss:Weight=\"1\"/>\n"+
                    "    <Border ss:Position=\"Left\" ss:LineStyle=\"Continuous\" ss:Weight=\"1\"/>\n"+
                    "    <Border ss:Position=\"Right\" ss:LineStyle=\"Continuous\" ss:Weight=\"1\"/>\n"+
                    "   </Borders>\n"+
                    "   <Font ss:FontName=\"Arial\" x:Family=\"Swiss\" ss:Size=\"8\" ss:Bold=\"1\"/>\n"+
                    "   <Interior ss:Color=\"#99CCFF\" ss:Pattern=\"Solid\"/>\n"+
                    "  </Style>\n"+
                    "  <Style ss:ID=\"s87\">\n"+
                    "   <Alignment ss:Horizontal=\"Center\" ss:Vertical=\"Bottom\" ss:WrapText=\"1\"/>\n"+
                    "   <Borders>\n"+
                    "    <Border ss:Position=\"Bottom\" ss:LineStyle=\"Continuous\" ss:Weight=\"1\"/>\n"+
                    "    <Border ss:Position=\"Right\" ss:LineStyle=\"Continuous\" ss:Weight=\"1\"/>\n"+
                    "   </Borders>\n"+
                    "   <Font ss:FontName=\"Arial\" x:Family=\"Swiss\" ss:Size=\"8\" ss:Bold=\"1\"/>\n"+
                    "   <Interior ss:Color=\"#99CCFF\" ss:Pattern=\"Solid\"/>\n"+
                    "  </Style>\n"+
                    "  <Style ss:ID=\"s88\">\n"+
                    "   <Alignment ss:Horizontal=\"Left\" ss:Vertical=\"Bottom\" ss:WrapText=\"1\"/>\n"+
                    "   <Borders>\n"+
                    "    <Border ss:Position=\"Bottom\" ss:LineStyle=\"Continuous\" ss:Weight=\"1\"/>\n"+
                    "    <Border ss:Position=\"Right\" ss:LineStyle=\"Continuous\" ss:Weight=\"1\"/>\n"+
                    "   </Borders>\n"+
                    "   <Font ss:FontName=\"Arial\" x:Family=\"Swiss\" ss:Size=\"8\" ss:Bold=\"1\"/>\n"+
                    "   <Interior ss:Color=\"#99CCFF\" ss:Pattern=\"Solid\"/>\n"+
                    "  </Style>\n"+
                    "  <Style ss:ID=\"s89\">\n"+
                    "   <Alignment ss:Vertical=\"Bottom\" ss:WrapText=\"1\"/>\n"+
                    "   <Borders>\n"+
                    "    <Border ss:Position=\"Bottom\" ss:LineStyle=\"Continuous\" ss:Weight=\"1\"/>\n"+
                    "    <Border ss:Position=\"Right\" ss:LineStyle=\"Continuous\" ss:Weight=\"1\"/>\n"+
                    "   </Borders>\n"+
                    "   <Font ss:FontName=\"Arial\" x:Family=\"Swiss\" ss:Size=\"8\" ss:Bold=\"1\"/>\n"+
                    "   <Interior ss:Color=\"#99CCFF\" ss:Pattern=\"Solid\"/>\n"+
                    "  </Style>\n"+
                    "  <Style ss:ID=\"s90\">\n"+
                    "   <Borders>\n"+
                    "    <Border ss:Position=\"Bottom\" ss:LineStyle=\"Continuous\" ss:Weight=\"1\"/>\n"+
                    "    <Border ss:Position=\"Right\" ss:LineStyle=\"Continuous\" ss:Weight=\"1\"/>\n"+
                    "   </Borders>\n"+
                    "   <Font ss:FontName=\"Arial\" x:Family=\"Swiss\" ss:Size=\"8\" ss:Bold=\"1\"/>\n"+
                    "   <Interior ss:Color=\"#99CCFF\" ss:Pattern=\"Solid\"/>\n"+
                    "  </Style>\n"+
                    "  <Style ss:ID=\"s91\">\n"+
                    "   <Alignment ss:Horizontal=\"Center\" ss:Vertical=\"Bottom\"/>\n"+
                    "   <Borders>\n"+
                    "    <Border ss:Position=\"Bottom\" ss:LineStyle=\"Continuous\" ss:Weight=\"1\"/>\n"+
                    "    <Border ss:Position=\"Right\" ss:LineStyle=\"Continuous\" ss:Weight=\"1\"/>\n"+
                    "   </Borders>\n"+
                    "   <Font ss:FontName=\"Arial\" x:Family=\"Swiss\" ss:Size=\"8\" ss:Bold=\"1\"/>\n"+
                    "   <Interior ss:Color=\"#99CCFF\" ss:Pattern=\"Solid\"/>\n"+
                    "  </Style>\n"+
                    "  <Style ss:ID=\"s92\">\n"+
                    "   <Alignment ss:Horizontal=\"Center\" ss:Vertical=\"Bottom\" ss:WrapText=\"1\"/>\n"+
                    "   <Borders>\n"+
                    "    <Border ss:Position=\"Bottom\" ss:LineStyle=\"Continuous\" ss:Weight=\"1\"/>\n"+
                    "    <Border ss:Position=\"Right\" ss:LineStyle=\"Continuous\" ss:Weight=\"1\"/>\n"+
                    "   </Borders>\n"+
                    "   <Font ss:FontName=\"Arial\" x:Family=\"Swiss\" ss:Size=\"8\" ss:Bold=\"1\"/>\n"+
                    "   <Interior ss:Color=\"#99CCFF\" ss:Pattern=\"Solid\"/>\n"+
                    "   <NumberFormat ss:Format=\"@\"/>\n"+
                    "  </Style>\n"+
                    "  <Style ss:ID=\"s93\">\n"+
                    "   <Alignment ss:Horizontal=\"Center\" ss:Vertical=\"Bottom\"/>\n"+
                    "   <Borders>\n"+
                    "    <Border ss:Position=\"Bottom\" ss:LineStyle=\"Continuous\" ss:Weight=\"1\"/>\n"+
                    "    <Border ss:Position=\"Right\" ss:LineStyle=\"Continuous\" ss:Weight=\"1\"/>\n"+
                    "   </Borders>\n"+
                    "   <Font ss:FontName=\"Arial\" x:Family=\"Swiss\" ss:Size=\"8\" ss:Bold=\"1\"/>\n"+
                    "   <Interior ss:Color=\"#99CCFF\" ss:Pattern=\"Solid\"/>\n"+
                    "   <NumberFormat ss:Format=\"@\"/>\n"+
                    "  </Style>\n"+
                    "  <Style ss:ID=\"s94\">\n"+
                    "   <Alignment ss:Horizontal=\"Left\" ss:Vertical=\"Bottom\"/>\n"+
                    "   <Borders>\n"+
                    "    <Border ss:Position=\"Bottom\" ss:LineStyle=\"Continuous\" ss:Weight=\"1\"/>\n"+
                    "    <Border ss:Position=\"Left\" ss:LineStyle=\"Continuous\" ss:Weight=\"1\"/>\n"+
                    "    <Border ss:Position=\"Right\" ss:LineStyle=\"Continuous\" ss:Weight=\"1\"/>\n"+
                    "   </Borders>\n"+
                    "   <Font ss:FontName=\"Arial\" ss:Size=\"8\"/>\n"+
                    "  </Style>\n"+
                    "  <Style ss:ID=\"s95\">\n"+
                    "   <Alignment ss:Horizontal=\"Left\" ss:Vertical=\"Bottom\"/>\n"+
                    "   <Borders>\n"+
                    "    <Border ss:Position=\"Bottom\" ss:LineStyle=\"Continuous\" ss:Weight=\"1\"/>\n"+
                    "    <Border ss:Position=\"Right\" ss:LineStyle=\"Continuous\" ss:Weight=\"1\"/>\n"+
                    "   </Borders>\n"+
                    "   <Font ss:FontName=\"Arial\" ss:Size=\"8\"/>\n"+
                    "   <NumberFormat ss:Format=\"@\"/>\n"+
                    "  </Style>\n"+
                    "  <Style ss:ID=\"s96\">\n"+
                    "   <Alignment ss:Horizontal=\"Left\" ss:Vertical=\"Bottom\"/>\n"+
                    "   <Borders>\n"+
                    "    <Border ss:Position=\"Bottom\" ss:LineStyle=\"Continuous\" ss:Weight=\"1\"/>\n"+
                    "    <Border ss:Position=\"Right\" ss:LineStyle=\"Continuous\" ss:Weight=\"1\"/>\n"+
                    "   </Borders>\n"+
                    "   <Font ss:FontName=\"Arial\" x:Family=\"Swiss\" ss:Size=\"8\"/>\n"+
                    "  </Style>\n"+
                    "  <Style ss:ID=\"s97\">\n"+
                    "   <Borders>\n"+
                    "    <Border ss:Position=\"Bottom\" ss:LineStyle=\"Continuous\" ss:Weight=\"1\"/>\n"+
                    "    <Border ss:Position=\"Right\" ss:LineStyle=\"Continuous\" ss:Weight=\"1\"/>\n"+
                    "   </Borders>\n"+
                    "   <Font ss:FontName=\"Arial\" ss:Size=\"8\"/>\n"+
                    "  </Style>\n"+
                    "  <Style ss:ID=\"s98\">\n"+
                    "   <Borders>\n"+
                    "    <Border ss:Position=\"Bottom\" ss:LineStyle=\"Continuous\" ss:Weight=\"1\"/>\n"+
                    "    <Border ss:Position=\"Right\" ss:LineStyle=\"Continuous\" ss:Weight=\"1\"/>\n"+
                    "   </Borders>\n"+
                    "   <Font ss:FontName=\"Arial\" ss:Size=\"8\"/>\n"+
                    "   <NumberFormat ss:Format=\"@\"/>\n"+
                    "  </Style>\n"+
                    "  <Style ss:ID=\"s99\">\n"+
                    "   <Borders>\n"+
                    "    <Border ss:Position=\"Bottom\" ss:LineStyle=\"Continuous\" ss:Weight=\"1\"/>\n"+
                    "    <Border ss:Position=\"Right\" ss:LineStyle=\"Continuous\" ss:Weight=\"1\"/>\n"+
                    "   </Borders>\n"+
                    "   <Font ss:FontName=\"Arial\" ss:Size=\"8\"/>\n"+
                    "   <NumberFormat ss:Format=\"0\"/>\n"+
                    "  </Style>\n"+
                    "  <Style ss:ID=\"s100\">\n"+
                    "   <Alignment ss:Horizontal=\"Center\" ss:Vertical=\"Bottom\"/>\n"+
                    "   <Borders>\n"+
                    "    <Border ss:Position=\"Bottom\" ss:LineStyle=\"Continuous\" ss:Weight=\"1\"/>\n"+
                    "    <Border ss:Position=\"Right\" ss:LineStyle=\"Continuous\" ss:Weight=\"1\"/>\n"+
                    "   </Borders>\n"+
                    "   <Font ss:FontName=\"Arial\" x:Family=\"Swiss\" ss:Size=\"8\" ss:Bold=\"1\"/>\n"+
                    "  </Style>\n"+
                    "  <Style ss:ID=\"s101\">\n"+
                    "   <Alignment ss:Horizontal=\"Right\" ss:Vertical=\"Bottom\"/>\n"+
                    "   <Borders>\n"+
                    "    <Border ss:Position=\"Bottom\" ss:LineStyle=\"Continuous\" ss:Weight=\"1\"/>\n"+
                    "    <Border ss:Position=\"Right\" ss:LineStyle=\"Continuous\" ss:Weight=\"1\"/>\n"+
                    "   </Borders>\n"+
                    "   <Font ss:FontName=\"Arial\" ss:Size=\"8\"/>\n"+
                    "  </Style>\n"+
                    "  <Style ss:ID=\"s104\">\n"+
                    "   <Alignment ss:Horizontal=\"Center\" ss:Vertical=\"Bottom\"/>\n"+
                    "   <Font ss:FontName=\"Comic Sans MS\" x:Family=\"Script\" ss:Size=\"12\" ss:Bold=\"1\"/>\n"+
                    "   <NumberFormat/>\n"+
                    "  </Style>\n"+
                    "  <Style ss:ID=\"s105\">\n"+
                    "   <Alignment ss:Horizontal=\"Center\" ss:Vertical=\"Bottom\"/>\n"+
                    "   <Font ss:FontName=\"Comic Sans MS\" x:Family=\"Script\" ss:Size=\"12\" ss:Bold=\"1\"/>\n"+
                    "   <NumberFormat ss:Format=\"@\"/>\n"+
                    "  </Style>\n"+
                    "  <Style ss:ID=\"s106\">\n"+
                    "   <Alignment ss:Horizontal=\"Center\" ss:Vertical=\"Bottom\"/>\n"+
                    "   <Font ss:FontName=\"Arial\" x:Family=\"Swiss\" ss:Size=\"12\" ss:Bold=\"1\"/>\n"+
                    "   <NumberFormat ss:Format=\"@\"/>\n"+
                    "  </Style>\n"+
                    "  <Style ss:ID=\"s107\">\n"+
                    "   <Alignment ss:Horizontal=\"Left\" ss:Vertical=\"Bottom\"/>\n"+
                    "   <Font ss:FontName=\"Arial\" ss:Size=\"8\"/>\n"+
                    "  </Style>\n"+
                    "  <Style ss:ID=\"s108\">\n"+
                    "   <Alignment ss:Horizontal=\"Left\" ss:Vertical=\"Bottom\"/>\n"+
                    "   <Font ss:FontName=\"Arial\" x:Family=\"Swiss\" ss:Size=\"8\"/>\n"+
                    "  </Style>\n"+
                    "  <Style ss:ID=\"s109\">\n"+
                    "   <Font ss:FontName=\"Arial\" x:Family=\"Swiss\" ss:Italic=\"1\"/>\n"+
                    "  </Style>\n"+
                    "  <Style ss:ID=\"s110\">\n"+
                    "   <Font ss:FontName=\"Arial\" x:Family=\"Swiss\"/>\n"+
                    "  </Style>\n"+
                    " </Styles>\n";
    public static final String ENCABEZADO_XML_HOJA1 =
                    "   <Column ss:StyleID=\"s64\" ss:AutoFitWidth=\"0\" ss:Width=\"51\"/>\n"+
                    "   <Column ss:StyleID=\"s64\" ss:AutoFitWidth=\"0\" ss:Width=\"68.25\"/>\n"+
                    "   <Column ss:StyleID=\"s64\" ss:AutoFitWidth=\"0\" ss:Width=\"126.75\"/>\n"+
                    "   <Column ss:StyleID=\"s64\" ss:AutoFitWidth=\"0\" ss:Width=\"56.25\"/>\n"+
                    "   <Column ss:StyleID=\"s64\" ss:AutoFitWidth=\"0\" ss:Width=\"114.75\"/>\n"+
                    "   <Column ss:StyleID=\"s64\" ss:Hidden=\"1\" ss:AutoFitWidth=\"0\" ss:Width=\"124.5\"/>\n"+
                    "   <Column ss:StyleID=\"s64\" ss:AutoFitWidth=\"0\" ss:Width=\"81\"/>\n"+
                    "   <Column ss:StyleID=\"s64\" ss:AutoFitWidth=\"0\" ss:Width=\"48.75\"/>\n"+
                    "   <Column ss:StyleID=\"s64\" ss:AutoFitWidth=\"0\" ss:Width=\"45.75\"/>\n"+
                    "   <Column ss:StyleID=\"s65\" ss:Hidden=\"1\" ss:AutoFitWidth=\"0\" ss:Width=\"102.75\"/>\n"+
                    "   <Column ss:StyleID=\"s65\" ss:AutoFitWidth=\"0\" ss:Width=\"19.5\"/>\n"+
                    "   <Column ss:StyleID=\"s65\" ss:AutoFitWidth=\"0\" ss:Width=\"54.75\"/>\n"+
                    "   <Column ss:StyleID=\"s65\" ss:Hidden=\"1\" ss:AutoFitWidth=\"0\" ss:Width=\"38.25\"/>\n"+
                    "   <Column ss:StyleID=\"s64\" ss:AutoFitWidth=\"0\" ss:Width=\"49.5\"/>\n"+
                    "   <Column ss:StyleID=\"s64\" ss:Hidden=\"1\" ss:AutoFitWidth=\"0\" ss:Width=\"55.5\"/>\n"+
                    "   <Column ss:StyleID=\"s64\" ss:Hidden=\"1\" ss:AutoFitWidth=\"0\" ss:Width=\"48.75\"/>\n"+
                    "   <Column ss:StyleID=\"s64\" ss:Hidden=\"1\" ss:AutoFitWidth=\"0\" ss:Width=\"52.5\"/>\n"+
                    "   <Column ss:StyleID=\"s64\" ss:Hidden=\"1\" ss:AutoFitWidth=\"0\" ss:Width=\"57\"/>\n"+
                    "   <Column ss:StyleID=\"s64\" ss:AutoFitWidth=\"0\" ss:Width=\"56.25\"/>\n"+
                    "   <Column ss:StyleID=\"s64\" ss:Hidden=\"1\" ss:AutoFitWidth=\"0\" ss:Width=\"10.5\"/>\n"+
                    "   <Column ss:StyleID=\"s64\" ss:Hidden=\"1\" ss:AutoFitWidth=\"0\" ss:Width=\"12\"/>\n"+
                    "   <Column ss:StyleID=\"s64\" ss:AutoFitWidth=\"0\" ss:Width=\"50.25\"/>\n"+
                    "   <Column ss:StyleID=\"s64\" ss:AutoFitWidth=\"0\" ss:Width=\"85.5\" ss:Span=\"2\"/>\n"+
                    "   <Column ss:Index=\"26\" ss:StyleID=\"s64\" ss:AutoFitWidth=\"0\" ss:Width=\"50.25\"/>\n"+
                    "   <Column ss:StyleID=\"s64\" ss:Hidden=\"1\" ss:AutoFitWidth=\"0\" ss:Width=\"48.75\"/>\n"+
                    "   <Column ss:StyleID=\"s64\" ss:AutoFitWidth=\"0\" ss:Width=\"58.5\"/>\n"+
                    "   <Column ss:StyleID=\"s64\" ss:AutoFitWidth=\"0\" ss:Width=\"66.75\"/>\n"+
                    "   <Column ss:StyleID=\"s64\" ss:AutoFitWidth=\"0\" ss:Width=\"59.25\"/>\n"+
                    "   <Column ss:StyleID=\"s64\" ss:AutoFitWidth=\"0\" ss:Width=\"222\"/>\n"+
                    "   <Column ss:StyleID=\"s64\" ss:AutoFitWidth=\"0\" ss:Width=\"128.25\"/>\n"+
                    "   <Row ss:Height=\"22.5\">\n"+
                    "    <Cell ss:StyleID=\"s66\"><Data ss:Type=\"String\">Carga y Modificación de Inspecciones</Data></Cell>\n"+
                    "    <Cell ss:StyleID=\"s66\"/>\n"+
                    "    <Cell ss:StyleID=\"s66\"/>\n"+
                    "    <Cell ss:StyleID=\"s66\"/>\n"+
                    "    <Cell ss:StyleID=\"s15\"/>\n"+
                    "    <Cell ss:Index=\"8\" ss:StyleID=\"s68\"><Data ss:Type=\"String\">OK</Data></Cell>\n"+
                    "   </Row>\n"+
                    "   <Row ss:AutoFitHeight=\"0\" ss:Height=\"13.5\" ss:StyleID=\"s69\">\n"+
                    "    <Cell ss:Index=\"10\" ss:StyleID=\"s71\"/>\n"+
                    "    <Cell ss:StyleID=\"s71\"/>\n"+
                    "    <Cell ss:StyleID=\"s71\"/>\n"+
                    "    <Cell ss:StyleID=\"s71\"/>\n"+
                    "   </Row>\n"+
                    "   <Row ss:AutoFitHeight=\"0\" ss:Height=\"17.25\" ss:StyleID=\"s72\">\n"+
                    "    <Cell ss:MergeAcross=\"25\" ss:StyleID=\"s73\"><Data ss:Type=\"String\">CARGA DE INSPECCIONES DE TRAZAS Y CD´s FVC187</Data></Cell>\n"+
                    "    <Cell ss:StyleID=\"s73\"/>\n"+
                    "    <Cell ss:StyleID=\"s73\"/>\n"+
                    "   </Row>\n"+
                    "   <Row ss:AutoFitHeight=\"0\" ss:Height=\"12\">\n"+
                    "    <Cell ss:StyleID=\"s74\"/>\n"+
                    "    <Cell ss:StyleID=\"s74\"/>\n"+
                    "    <Cell ss:StyleID=\"s74\"/>\n"+
                    "    <Cell ss:StyleID=\"s74\"/>\n"+
                    "    <Cell ss:StyleID=\"s74\"/>\n"+
                    "    <Cell ss:StyleID=\"s74\"/>\n"+
                    "    <Cell ss:StyleID=\"s74\"/>\n"+
                    "    <Cell ss:StyleID=\"s74\"/>\n"+
                    "    <Cell ss:StyleID=\"s74\"/>\n"+
                    "    <Cell ss:StyleID=\"s75\"/>\n"+
                    "    <Cell ss:StyleID=\"s75\"/>\n"+
                    "    <Cell ss:StyleID=\"s75\"/>\n"+
                    "    <Cell ss:StyleID=\"s75\"/>\n"+
                    "    <Cell ss:StyleID=\"s74\"/>\n"+
                    "    <Cell ss:StyleID=\"s74\"/>\n"+
                    "    <Cell ss:StyleID=\"s74\"/>\n"+
                    "    <Cell ss:StyleID=\"s74\"/>\n"+
                    "   </Row>\n"+
                    "   <Row ss:AutoFitHeight=\"0\" ss:Height=\"26.25\">\n"+
                    "    <Cell ss:StyleID=\"s76\"><Data ss:Type=\"String\">Tipo de Inspección</Data></Cell>\n"+
                    "    <Cell ss:StyleID=\"s77\"><Data ss:Type=\"String\">Territorio</Data></Cell>\n"+
                    "    <Cell ss:StyleID=\"s77\"><Data ss:Type=\"String\">Nombre Territorio</Data></Cell>\n"+
                    "    <Cell ss:StyleID=\"s74\"/>\n"+
                    "    <Cell ss:StyleID=\"s74\"/>\n"+
                    "    <Cell ss:StyleID=\"s74\"/>\n"+
                    "    <Cell ss:StyleID=\"s74\"/>\n"+
                    "    <Cell ss:StyleID=\"s74\"/>\n"+
                    "    <Cell ss:StyleID=\"s74\"/>\n"+
                    "    <Cell ss:StyleID=\"s75\"/>\n"+
                    "    <Cell ss:StyleID=\"s75\"/>\n"+
                    "    <Cell ss:StyleID=\"s75\"/>\n"+
                    "    <Cell ss:StyleID=\"s75\"/>\n"+
                    "    <Cell ss:StyleID=\"s74\"/>\n"+
                    "    <Cell ss:StyleID=\"s74\"/>\n"+
                    "    <Cell ss:StyleID=\"s74\"/>\n"+
                    "    <Cell ss:StyleID=\"s74\"/>\n"+
                    "   </Row>\n"+
                    "   <Row>\n"+
                    "    <Cell ss:StyleID=\"s78\"><Data ss:Type=\"String\">L</Data></Cell>\n"+
                    "    <Cell ss:StyleID=\"s79\"><Data ss:Type=\"String\">CAT</Data></Cell>\n"+
                    "    <Cell ss:StyleID=\"s79\"><Data ss:Type=\"String\">Cataluña</Data></Cell>\n"+
                    "   </Row>\n"+
                    "   <Row ss:Index=\"8\">\n"+
                    "    <Cell ss:StyleID=\"s80\"/>\n"+
                    "    <Cell ss:StyleID=\"s80\"/>\n"+
                    "    <Cell ss:StyleID=\"s80\"/>\n"+
                    "    <Cell ss:StyleID=\"s80\"/>\n"+
                    "    <Cell ss:StyleID=\"s80\"/>\n"+
                    "    <Cell ss:StyleID=\"s81\"/>\n"+
                    "    <Cell ss:StyleID=\"s81\"/>\n"+
                    "    <Cell ss:StyleID=\"s82\"/>\n"+
                    "    <Cell ss:StyleID=\"s82\"/>\n"+
                    "    <Cell ss:StyleID=\"s83\"/>\n"+
                    "    <Cell ss:StyleID=\"s83\"/>\n"+
                    "    <Cell ss:StyleID=\"s83\"/>\n"+
                    "    <Cell ss:StyleID=\"s83\"/>\n"+
                    "    <Cell ss:StyleID=\"s82\"/>\n"+
                    "    <Cell ss:StyleID=\"s82\"/>\n"+
                    "    <Cell ss:StyleID=\"s82\"/>\n"+
                    "    <Cell ss:StyleID=\"s82\"/>\n"+
                    "    <Cell ss:StyleID=\"s82\"/>\n"+
                    "    <Cell ss:StyleID=\"s82\"/>\n"+
                    "    <Cell ss:MergeAcross=\"1\" ss:StyleID=\"m14596084\"><Data ss:Type=\"String\">I.Ofic</Data></Cell>\n"+
                    "    <Cell ss:StyleID=\"s82\"/>\n"+
                    "    <Cell ss:StyleID=\"s82\"/>\n"+
                    "    <Cell ss:StyleID=\"s82\"/>\n"+
                    "    <Cell ss:StyleID=\"s82\"/>\n"+
                    "    <Cell ss:StyleID=\"s84\"/>\n"+
                    "    <Cell ss:StyleID=\"s82\"/>\n"+
                    "    <Cell ss:StyleID=\"s85\"/>\n"+
                    "   </Row>\n"+
                    "   <Row ss:AutoFitHeight=\"0\" ss:Height=\"35.25\">\n"+
                    "    <Cell ss:StyleID=\"s86\"><Data ss:Type=\"String\">Tipo Instalación</Data></Cell>\n"+
                    "    <Cell ss:StyleID=\"s87\"><Data ss:Type=\"String\">Cód. BDE Instalación</Data></Cell>\n"+
                    "    <Cell ss:StyleID=\"s88\"><Data ss:Type=\"String\">Descripción Instalación</Data></Cell>\n"+
                    "    <Cell ss:StyleID=\"s87\"><Data ss:Type=\"String\">Posición, Tramo o Localización</Data></Cell>\n"+
                    "    <Cell ss:StyleID=\"s89\"><Data ss:Type=\"String\">Nombre de Tramo/&#10;Código de Tramo : Nombre de Tramo del CD</Data></Cell>\n"+
                    "    <Cell ss:StyleID=\"s90\"><Data ss:Type=\"String\">Descripción Tramo</Data></Cell>\n"+
                    "    <Cell ss:StyleID=\"s91\"><Data ss:Type=\"String\">Equipo o Apoyo</Data></Cell>\n"+
                    "    <Cell ss:StyleID=\"s87\"><Data ss:Type=\"String\">Fecha de Inspección</Data></Cell>\n"+
                    "    <Cell ss:StyleID=\"s87\"><Data ss:Type=\"String\">Defecto o Medida</Data></Cell>\n"+
                    "    <Cell ss:StyleID=\"s92\"><Data ss:Type=\"String\">Descripción Defecto/Medida</Data></Cell>\n"+
                    "    <Cell ss:StyleID=\"s93\"><Data ss:Type=\"String\">Crit</Data></Cell>\n"+
                    "    <Cell ss:StyleID=\"s92\"><Data ss:Type=\"String\">Ocurrencias o Medida</Data></Cell>\n"+
                    "    <Cell ss:StyleID=\"s92\"><Data ss:Type=\"String\">Estado Inst</Data></Cell>\n"+
                    "    <Cell ss:StyleID=\"s87\"><Data ss:Type=\"String\">Trabajo de Inspección</Data></Cell>\n"+
                    "    <Cell ss:StyleID=\"s91\"><Data ss:Type=\"String\">Valoración</Data></Cell>\n"+
                    "    <Cell ss:StyleID=\"s91\"><Data ss:Type=\"String\">Importe</Data></Cell>\n"+
                    "    <Cell ss:StyleID=\"s87\"><Data ss:Type=\"String\">Límite de Corrección</Data></Cell>\n"+
                    "    <Cell ss:StyleID=\"s87\"><Data ss:Type=\"String\">Trabajo de corrección</Data></Cell>\n"+
                    "    <Cell ss:StyleID=\"s87\"><Data ss:Type=\"String\">Fecha de Corrección</Data></Cell>\n"+
                    "    <Cell ss:StyleID=\"s90\"><Data ss:Type=\"String\">D</Data></Cell>\n"+
                    "    <Cell ss:StyleID=\"s90\"><Data ss:Type=\"String\">C</Data></Cell>\n"+
                    "    <Cell ss:StyleID=\"s87\"><Data ss:Type=\"String\">Código de Inspección</Data></Cell>\n"+
                    "    <Cell ss:StyleID=\"s90\"><Data ss:Type=\"String\">Observaciones</Data></Cell>\n"+
                    "    <Cell ss:StyleID=\"s87\"><Data ss:Type=\"String\">Documento/s a asociar</Data></Cell>\n"+
                    "    <Cell ss:StyleID=\"s87\"><Data ss:Type=\"String\">Descripción documento/s</Data></Cell>\n"+
                    "    <Cell ss:StyleID=\"s87\"><Data ss:Type=\"String\">Fecha de Alta</Data></Cell>\n"+
                    "    <Cell ss:StyleID=\"s91\"><Data ss:Type=\"String\">TPL</Data></Cell>\n"+
                    "    <Cell ss:StyleID=\"s91\"><Data ss:Type=\"String\">Km. Aéreos</Data></Cell>\n"+
                    "   </Row>\n";
    public static final String ENCABEZADO_XML_HOJA2 =
            " <Worksheet ss:Name=\"UNIDADES_REV_APOYO_1.xls\">\n" +
                    "  <Table ss:ExpandedColumnCount=\"33\" " +
                    //"ss:ExpandedRowCount=\"43\" " +
                    "x:FullColumns=\"1\"\n" +
                    "   x:FullRows=\"1\" ss:StyleID=\"s64\" ss:DefaultColumnWidth=\"60\"\n" +
                    "   ss:DefaultRowHeight=\"11.25\">\n" +
                    "   <Column ss:StyleID=\"s64\" ss:AutoFitWidth=\"0\" ss:Width=\"57.75\"/>\n" +
                    "   <Column ss:StyleID=\"s64\" ss:AutoFitWidth=\"0\" ss:Width=\"113.25\"/>\n" +
                    "   <Column ss:StyleID=\"s64\" ss:AutoFitWidth=\"0\" ss:Width=\"126.75\"/>\n" +
                    "   <Column ss:StyleID=\"s64\" ss:AutoFitWidth=\"0\" ss:Width=\"56.25\"/>\n" +
                    "   <Column ss:StyleID=\"s64\" ss:Hidden=\"1\" ss:AutoFitWidth=\"0\" ss:Width=\"37.5\"/>\n" +
                    "   <Column ss:StyleID=\"s64\" ss:AutoFitWidth=\"0\" ss:Width=\"102.75\"/>\n" +
                    "   <Column ss:StyleID=\"s64\" ss:Hidden=\"1\" ss:AutoFitWidth=\"0\" ss:Width=\"57.75\"/>\n" +
                    "   <Column ss:StyleID=\"s64\" ss:AutoFitWidth=\"0\" ss:Width=\"116.25\"/>\n" +
                    "   <Column ss:StyleID=\"s64\" ss:Hidden=\"1\" ss:AutoFitWidth=\"0\" ss:Width=\"54.75\"/>\n" +
                    "   <Column ss:StyleID=\"s65\" ss:AutoFitWidth=\"0\" ss:Width=\"46.5\"/>\n" +
                    "   <Column ss:StyleID=\"s65\" ss:AutoFitWidth=\"0\" ss:Width=\"44.25\"/>\n" +
                    "   <Column ss:StyleID=\"s65\" ss:Hidden=\"1\" ss:AutoFitWidth=\"0\" ss:Width=\"33\"/>\n" +
                    "   <Column ss:StyleID=\"s65\" ss:AutoFitWidth=\"0\" ss:Width=\"65.25\"/>\n" +
                    "   <Column ss:StyleID=\"s65\" ss:AutoFitWidth=\"0\" ss:Width=\"63\"/>\n" +
                    "   <Column ss:StyleID=\"s64\" ss:AutoFitWidth=\"0\" ss:Width=\"50.25\"/>\n" +
                    "   <Column ss:Index=\"17\" ss:StyleID=\"s65\" ss:AutoFitWidth=\"0\" ss:Width=\"86.25\"/>\n" +
                    "   <Column ss:StyleID=\"s64\" ss:AutoFitWidth=\"0\" ss:Width=\"24\"/>\n" +
                    "   <Column ss:StyleID=\"s64\" ss:AutoFitWidth=\"0\" ss:Width=\"30.75\"/>\n" +
                    "   <Column ss:StyleID=\"s64\" ss:AutoFitWidth=\"0\" ss:Width=\"56.25\"/>\n" +
                    "   <Column ss:StyleID=\"s64\" ss:AutoFitWidth=\"0\" ss:Width=\"22.5\"/>\n" +
                    "   <Column ss:StyleID=\"s64\" ss:AutoFitWidth=\"0\" ss:Width=\"35.25\"/>\n" +
                    "   <Column ss:StyleID=\"s64\" ss:AutoFitWidth=\"0\" ss:Width=\"50.25\"/>\n" +
                    "   <Column ss:StyleID=\"s64\" ss:AutoFitWidth=\"0\" ss:Width=\"85.5\" ss:Span=\"2\"/>\n" +
                    "   <Column ss:Index=\"27\" ss:StyleID=\"s64\" ss:AutoFitWidth=\"0\" ss:Width=\"50.25\"/>\n" +
                    "   <Column ss:StyleID=\"s64\" ss:Hidden=\"1\" ss:AutoFitWidth=\"0\" ss:Width=\"48.75\"/>\n" +
                    "   <Column ss:StyleID=\"s64\" ss:AutoFitWidth=\"0\" ss:Width=\"58.5\"/>\n" +
                    "   <Column ss:StyleID=\"s64\" ss:AutoFitWidth=\"0\" ss:Width=\"66.75\"/>\n" +
                    "   <Column ss:StyleID=\"s64\" ss:AutoFitWidth=\"0\" ss:Width=\"59.25\"/>\n" +
                    "   <Column ss:StyleID=\"s64\" ss:AutoFitWidth=\"0\" ss:Width=\"222\"/>\n" +
                    "   <Column ss:StyleID=\"s64\" ss:AutoFitWidth=\"0\" ss:Width=\"128.25\"/>\n" +
                    "   <Row ss:Height=\"22.5\">\n" +
                    "    <Cell ss:StyleID=\"s66\"><Data ss:Type=\"String\">Carga y Modificación de Apoyos de Inspecciones</Data></Cell>\n" +
                    "    <Cell ss:StyleID=\"s66\"/>\n" +
                    "    <Cell ss:StyleID=\"s66\"/>\n" +
                    "    <Cell ss:StyleID=\"s66\"/>\n" +
                    "    <Cell ss:StyleID=\"s66\"/>\n" +
                    "    <Cell ss:Index=\"8\" ss:StyleID=\"s68\"><Data ss:Type=\"String\">OK</Data></Cell>\n" +
                    "   </Row>\n" +
                    "   <Row ss:AutoFitHeight=\"0\" ss:Height=\"13.5\" ss:StyleID=\"s69\">\n" +
                    "    <Cell ss:Index=\"10\" ss:StyleID=\"s71\"/>\n" +
                    "    <Cell ss:StyleID=\"s71\"/>\n" +
                    "    <Cell ss:StyleID=\"s71\"/>\n" +
                    "    <Cell ss:StyleID=\"s71\"/>\n" +
                    "    <Cell ss:StyleID=\"s71\"/>\n" +
                    "    <Cell ss:Index=\"17\" ss:StyleID=\"s71\"/>\n" +
                    "    <Cell ss:StyleID=\"s70\"/>\n" +
                    "   </Row>\n" +
                    "   <Row ss:AutoFitHeight=\"0\" ss:Height=\"17.25\" ss:StyleID=\"s72\">\n" +
                    "    <Cell ss:Index=\"2\" ss:StyleID=\"s73\"/>\n" +
                    "    <Cell ss:StyleID=\"s73\"/>\n" +
                    "    <Cell ss:StyleID=\"s73\"/>\n" +
                    "    <Cell ss:Index=\"6\" ss:StyleID=\"s73\"><Data ss:Type=\"String\">CARGA DE APOYOS DE INSPECCIONES DE TRAZAS Y CD´s FVC187</Data></Cell>\n" +
                    "    <Cell ss:StyleID=\"s73\"/>\n" +
                    "    <Cell ss:StyleID=\"s73\"/>\n" +
                    "    <Cell ss:StyleID=\"s73\"/>\n" +
                    "    <Cell ss:StyleID=\"s104\"/>\n" +
                    "    <Cell ss:StyleID=\"s104\"/>\n" +
                    "    <Cell ss:StyleID=\"s104\"/>\n" +
                    "    <Cell ss:StyleID=\"s104\"/>\n" +
                    "    <Cell ss:StyleID=\"s104\"/>\n" +
                    "    <Cell ss:StyleID=\"s73\"/>\n" +
                    "    <Cell ss:StyleID=\"s73\"/>\n" +
                    "    <Cell ss:StyleID=\"s105\"/>\n" +
                    "    <Cell ss:StyleID=\"s73\"/>\n" +
                    "    <Cell ss:StyleID=\"s73\"/>\n" +
                    "    <Cell ss:StyleID=\"s73\"/>\n" +
                    "    <Cell ss:StyleID=\"s73\"/>\n" +
                    "    <Cell ss:StyleID=\"s73\"/>\n" +
                    "    <Cell ss:StyleID=\"s73\"/>\n" +
                    "    <Cell ss:StyleID=\"s73\"/>\n" +
                    "    <Cell ss:StyleID=\"s73\"/>\n" +
                    "    <Cell ss:StyleID=\"s73\"/>\n" +
                    "    <Cell ss:StyleID=\"s73\"/>\n" +
                    "    <Cell ss:StyleID=\"s73\"/>\n" +
                    "    <Cell ss:StyleID=\"s73\"/>\n" +
                    "   </Row>\n" +
                    "   <Row ss:AutoFitHeight=\"0\" ss:Height=\"12\">\n" +
                    "    <Cell ss:StyleID=\"s74\"/>\n" +
                    "    <Cell ss:StyleID=\"s74\"/>\n" +
                    "    <Cell ss:StyleID=\"s74\"/>\n" +
                    "    <Cell ss:StyleID=\"s74\"/>\n" +
                    "    <Cell ss:StyleID=\"s74\"/>\n" +
                    "    <Cell ss:StyleID=\"s74\"/>\n" +
                    "    <Cell ss:StyleID=\"s74\"/>\n" +
                    "    <Cell ss:StyleID=\"s74\"/>\n" +
                    "    <Cell ss:StyleID=\"s74\"/>\n" +
                    "    <Cell ss:StyleID=\"s75\"/>\n" +
                    "    <Cell ss:StyleID=\"s75\"/>\n" +
                    "    <Cell ss:StyleID=\"s75\"/>\n" +
                    "    <Cell ss:StyleID=\"s75\"/>\n" +
                    "    <Cell ss:StyleID=\"s75\"/>\n" +
                    "    <Cell ss:StyleID=\"s74\"/>\n" +
                    "    <Cell ss:StyleID=\"s74\"/>\n" +
                    "    <Cell ss:StyleID=\"s106\"/>\n" +
                    "    <Cell ss:StyleID=\"s74\"/>\n" +
                    "   </Row>\n" +
                    "   <Row ss:AutoFitHeight=\"0\" ss:Height=\"26.25\">\n" +
                    "    <Cell ss:StyleID=\"s76\"><Data ss:Type=\"String\">Tipo de Inspección</Data></Cell>\n" +
                    "    <Cell ss:StyleID=\"s74\"/>\n" +
                    "    <Cell ss:StyleID=\"s74\"/>\n" +
                    "    <Cell ss:StyleID=\"s74\"/>\n" +
                    "    <Cell ss:StyleID=\"s74\"/>\n" +
                    "    <Cell ss:StyleID=\"s74\"/>\n" +
                    "    <Cell ss:StyleID=\"s74\"/>\n" +
                    "    <Cell ss:StyleID=\"s74\"/>\n" +
                    "    <Cell ss:StyleID=\"s74\"/>\n" +
                    "    <Cell ss:StyleID=\"s75\"/>\n" +
                    "    <Cell ss:StyleID=\"s75\"/>\n" +
                    "    <Cell ss:StyleID=\"s75\"/>\n" +
                    "    <Cell ss:StyleID=\"s75\"/>\n" +
                    "    <Cell ss:StyleID=\"s75\"/>\n" +
                    "    <Cell ss:StyleID=\"s74\"/>\n" +
                    "    <Cell ss:StyleID=\"s74\"/>\n" +
                    "    <Cell ss:StyleID=\"s106\"/>\n" +
                    "    <Cell ss:StyleID=\"s74\"/>\n" +
                    "   </Row>\n" +
                    "   <Row>\n" +
                    "    <Cell ss:StyleID=\"s78\"><Data ss:Type=\"String\">L</Data></Cell>\n" +
                    "   </Row>\n" +
                    "   <Row ss:Index=\"9\">\n" +
                    "    <Cell ss:StyleID=\"s80\"/>\n" +
                    "    <Cell ss:StyleID=\"s80\"/>\n" +
                    "    <Cell ss:StyleID=\"s80\"/>\n" +
                    "    <Cell ss:StyleID=\"s80\"/>\n" +
                    "    <Cell ss:StyleID=\"s80\"/>\n" +
                    "    <Cell ss:StyleID=\"s81\"/>\n" +
                    "    <Cell ss:StyleID=\"s81\"/>\n" +
                    "    <Cell ss:StyleID=\"s82\"/>\n" +
                    "    <Cell ss:StyleID=\"s82\"/>\n" +
                    "    <Cell ss:StyleID=\"s83\"/>\n" +
                    "    <Cell ss:StyleID=\"s83\"/>\n" +
                    "    <Cell ss:StyleID=\"s83\"/>\n" +
                    "    <Cell ss:StyleID=\"s83\"/>\n" +
                    "    <Cell ss:StyleID=\"s83\"/>\n" +
                    "    <Cell ss:StyleID=\"s82\"/>\n" +
                    "    <Cell ss:StyleID=\"s82\"/>\n" +
                    "    <Cell ss:StyleID=\"s83\"/>\n" +
                    "   </Row>\n" +
                    "   <Row ss:AutoFitHeight=\"0\" ss:Height=\"35.25\">\n" +
                    "    <Cell ss:StyleID=\"s86\"><Data ss:Type=\"String\">Acción</Data></Cell>\n" +
                    "    <Cell ss:StyleID=\"s87\"><Data ss:Type=\"String\">Cód. Apoyo/Rótulo</Data></Cell>\n" +
                    "    <Cell ss:StyleID=\"s87\"><Data ss:Type=\"String\">Observaciones</Data></Cell>\n" +
                    "    <Cell ss:StyleID=\"s87\"><Data ss:Type=\"String\">Máx. Tens. Redes Soportado</Data></Cell>\n" +
                    "    <Cell ss:StyleID=\"s89\"><Data ss:Type=\"String\">Oculto Combo Máx. Tens.</Data></Cell>\n" +
                    "    <Cell ss:StyleID=\"s91\"><Data ss:Type=\"String\">Material</Data></Cell>\n" +
                    "    <Cell ss:StyleID=\"s87\"><Data ss:Type=\"String\">Oculto Combo Material</Data></Cell>\n" +
                    "    <Cell ss:StyleID=\"s87\"><Data ss:Type=\"String\">Estructura</Data></Cell>\n" +
                    "    <Cell ss:StyleID=\"s87\"><Data ss:Type=\"String\">Oculto Combo Estructura</Data></Cell>\n" +
                    "    <Cell ss:StyleID=\"s92\"><Data ss:Type=\"String\">Altura Apoyo</Data></Cell>\n" +
                    "    <Cell ss:StyleID=\"s92\"><Data ss:Type=\"String\">Huso Apoyo</Data></Cell>\n" +
                    "    <Cell ss:StyleID=\"s92\"><Data ss:Type=\"String\">Huso Combo</Data></Cell>\n" +
                    "    <Cell ss:StyleID=\"s92\"><Data ss:Type=\"String\">Coordenada X utm  Apoyo</Data></Cell>\n" +
                    "    <Cell ss:StyleID=\"s92\"><Data ss:Type=\"String\">Coordenada Y utm  Apoyo</Data></Cell>\n" +
                    "    <Cell ss:StyleID=\"s87\"><Data ss:Type=\"String\">Tipo de Instalación</Data></Cell>\n" +
                    "    <Cell ss:StyleID=\"s87\"><Data ss:Type=\"String\">Nombre Instalación</Data></Cell>\n" +
                    "    <Cell ss:StyleID=\"s92\"><Data ss:Type=\"String\">Código de Tramo</Data></Cell>\n" +
                    "   </Row>";
    public static final String ENCABEZADO_XML_HOJA3_A =
            "<Row>\n<Cell ss:StyleID=\"s66\"><Data ss:Type=\"String\">Carga datos de revisión desde Tablet" +
            "</Data></Cell>\n</Row>\n<Row></Row>\n" +
            "<Row><Cell ss:Index=\"6\" ss:StyleID=\"s73\"><Data ss:Type=\"String\">" +
            "DATOS REVISIÓN</Data></Cell>\n</Row>\n";
    public static final String ENCABEZADO_XML_HOJA3_B =
            "<Row>\n<Cell ss:StyleID=\"s76\"><Data ss:Type=\"String\">Cód. Apoyo / CD</Data></Cell>\n" +
            "<Cell ss:StyleID=\"s76\"><Data ss:Type=\"String\">Motivo por el que no se ha revisado</Data></Cell>\n</Row>\n";

    public static final String ENCABEZADO_KML =
            "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
            "<kml xmlns=\"http://earth.google.com/kml/2.1\">\n";

    public static final String DIRECTORIO_SALIDA_BD = "/RPR/OUTPUT/";
    public static final String DIRECTORIO_ENTRADA_BD = "/RPR/INPUT/";

    public static final String ESTADO_PENDIENTE = "Pendiente";
    public static final String ESTADO_EN_CURSO = "En curso";
    public static final String ESTADO_FINALIZADA = "Finalizada";
    public static final String LAMT = "LAMT";
    public static final String CT = "CT";
    public static final String PT = "PT";
    public static final String SI = "Si";
    public static final String NO = "No";
    public static final String TAG = "ErrorRPR: ";
    public static final String TIPO = "Tipo";
    public static final String EQUIPO = "Equipo";
    public static final String DEFECTO = "Defecto";
    public static String revisionActual;
    public static String equipoActual;
    public static String tipoActual;
    public static String defectoActual;
    public static String tramoActual;
    private static Context contexto;

    public static String[] listaTitulosRevisiones = {"Nombre", "Estado", "Inspector1", "Inspector2", "Colegiado", "Equipo usado",
            "Metodologia", "Código Nipsa", "Numero de Trabajo", "Código Inspección"};
    public static String[] listaTitulosEquipos = {"Tipo Instalación", "Cód. BDE Instalación", "Descripción Instalación",
            "Posición, Tramo o Localización", "Nombre de Tramo/Código de Tramo : Nombre de Tramo del CD",
            "Descripción Tramo", "Equipo o Apoyo", "Fecha de Inspección",
            "Defecto o Medida", "Descripción Defecto/Medida", "Crit", "Ocurrencias o Medida",
            "Estado Inst", "Trabajo de Inspección", "Valoración", "Importe", "Límite de Corrección",
            "Trabajo de corrección", "Fecha de Corrección", "D", "C", "Código de Inspección",
            "Observaciones", "Documento/s a asociar", "Descripción documento/s", "Fecha de Alta",
            "TPL", "Km. Aéreos"};

    /**
     *
     */
    @Override
    public void onCreate() {
        super.onCreate();
        contexto = this;
    }

    /**
     * Método para generar un Toast e imprimirlo por pantalla
     *
     * @param texto
     */
    public static void print (String texto) {
        Toast.makeText(contexto, texto, Toast.LENGTH_LONG).show();
    }

    /**
     * Genera todos los archivos de salida
     *
     * @param revision
     */
    public static void backup (String revision) {
        HiloBackup hilo = new HiloBackup();
        hilo.execute(revision);

    }

    /**
     * Metodo para crear una copia exacta de la base de datos en un directorio accesible externamente
     */
    public static void backupBaseDatos() {
        String timeStamp = fechaHoraActual();

        // Ruta fichero interno BD
        final String inFileName = "/data/data/com.nipsa.rpr/databases/" + DBRevisiones.DATABASE_NAME;
        try {
            File dbFile = new File(inFileName);
            FileInputStream fis = new FileInputStream(dbFile);
            // Ruta fichero salida BD
            String directorio = Environment.getExternalStoragePublicDirectory
                    (Environment.DIRECTORY_DOWNLOADS) + DIRECTORIO_SALIDA_BD;

            File d = new File(directorio);
            if (!d.exists()) {
                d.mkdirs();
            }
            // Nombre fichero salida BD
            String outFileName = directorio + "/" + DBRevisiones.DATABASE_NAME +
                                                        // "_" + timeStamp +
                                                        ".sqlite";

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
            //Aplicacion.print(e.toString());
        }

    }

    /**
     * Metodo para generar un String con la fecha actual
     *
     * @return String con la fecha actual
     */
    public static String fechaHoraActual () {
        Date d = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("ddMMyyyy_HHmmss"); // Formato en el que se proporcionaran los datos
        String fechaHoraActual = sdf.format(d); // Se genera un String con el formato indicado
        return fechaHoraActual;
    }

    /**
     * Corrige la fecha para darle un formato dd/mm/yyyy
     * @param fecha
     * @return
     */
    public static String corregirFecha (String fecha){
        String fechaCorregida = "";
        if (fecha != null){
            if (!fecha.equals("")){
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
                } catch (Exception e) {}
                finally {
                    return fechaCorregida;
                }
            } else {
                return "";
            }
        } else {
            return null;
        }
    }

    /**
     * Metodo para leer la BDD con los Defectos
     */
    public static void primeraLecturaBD() {

        // Ruta ubicacion fichero BD
        String outFileName = "/data/data/com.nipsa.rpr/databases/";
        try {
            File dbFile = new File(outFileName);
            if (!dbFile.exists()) {
                dbFile.mkdirs();
            }
            InputStream inputRawFileName = contexto.getResources().openRawResource(R.raw.defectos);
            outFileName = outFileName + DBGlobal.DATABASE_NAME;

            // MostrarRevisiones de la copia
            OutputStream output = new FileOutputStream(outFileName);

            byte[] buffer = new byte[1024];
            int length;
            while ((length = inputRawFileName.read(buffer)) > 0) {
                output.write(buffer, 0, length);
            }

            output.flush();
            output.close();
            inputRawFileName.close();
        } catch (Exception e) {
            Log.e("ErrorRPR: ", e.toString());
            Aplicacion.print(e.toString());
        }

    }

    /**
     * Crea un archivo con un nombre concreto para guardar una imagen tomada con la cámara
     *
     * @return
     * @throws IOException
     */
    public static File crearArchivoImagen() throws IOException {

        String fecha = Aplicacion.fechaHoraActual();
        String nombreFichero = "Imagen_" + fecha;
        File ruta = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES +
                "/" + Aplicacion.revisionActual + "/" + Aplicacion.equipoActual);
        if (!ruta.exists()){
            ruta.mkdirs();
        }

        File imagen = File.createTempFile(nombreFichero, ".jpg", ruta);

        return imagen;
    }

    /**
     * Método que devuelve el archivo con el nombre y ruta recibidos por parámetro.
     * Devolverá "null" si no se encuentra el archivo
     *
     * @param ruta
     * @param nombre
     * @return el archivo solicitado o null si no encuentra el archivo
     */
    public static File recuperarArchivo (String ruta, String nombre) {
        File archivo = null;

        File f = new File(ruta);
        //Se listan los archivos de la carpeta del apoyo
        File[] listaArchivos = f.listFiles();

        if(listaArchivos != null) {
            for (int i=0; i<listaArchivos.length; i++) {
                String nombreArchivo = listaArchivos[i].getName();
                if (nombreArchivo.equalsIgnoreCase(nombre)) {
                    archivo = listaArchivos[i];
                    break;
                }
            }
        }

        return archivo;
    }

    /**
     * Genera el archivo XML con los datos recogidos hasta ese momento
     *
     * @param revision
     */
    public static void generarXML (String revision) {
        String nombreArchivo = revision + ".xml";
        File archivo = crearArchivoSalida(revision, nombreArchivo);

        try {
            FileOutputStream fos = new FileOutputStream(archivo);
            fos.write(ENCABEZADO_XML_ARCHIVO.getBytes());
            fos.write(generarCuerpoXML(revision).getBytes());
            fos.close();
        } catch (Exception e) {
            Log.e ("Error RPR: ", e.toString());
            //Aplicacion.print("ErrorRPR: Error al generar el archivo XML" + e.toString());
        }
    }

    /**
     * Crea un archivo de salida con el nombre recibido por parámetro
     *
     * @param revision
     * @param nombreArchivo
     * @return
     */
    public static File crearArchivoSalida (String revision, String nombreArchivo) {
        File archivoSalida = null;

        // Ruta ubicacion fichero salida
        try {
            File directorio = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS +
                    "/RPR/Output/" + revision + "/");
            if (!directorio.exists()) {
                directorio.mkdirs();
            }
            archivoSalida = new File (directorio, nombreArchivo);
        } catch (Exception e) {
            Log.e ("ErrorRPR: ", "Error al generar el archivo " + e.toString());
            //Aplicacion.print("Error al generar el archivo :" + e.toString());
            return null;
        }

        return archivoSalida;
    }

    /**
     * Genera el cuerpo del XML (la cabecera se incluye de forma estática)
     * @param revision
     * @return
     */
    public static String generarCuerpoXML(String revision) {
        StringBuffer texto = new StringBuffer();
        DBRevisiones dbRevisiones = new DBRevisiones(contexto);
        // Corrección del nombre de la hoja para evitar problemas de compatibilidad con el Excel
        String nombreHoja = "UNIDADES_REVISION_" + revision + "_1.xls";
        nombreHoja = nombreHoja.replace("-", "_");
        if (nombreHoja.length()>30) {
            nombreHoja = nombreHoja.substring(0, 29);
        }

        // Hoja 1: Equipos (Equipos de la revisión)
        Cursor cEquipos = dbRevisiones.solicitarDatosEquipos(revision);
        texto.append(" <Worksheet ss:Name=\"" + nombreHoja + "\">\n");
        texto.append("  <Table ss:ExpandedColumnCount=\"32\" " +
                //"ss:ExpandedRowCount=\"41\" x:FullColumns=\"1\"\n"+
                "   x:FullRows=\"1\" ss:StyleID=\"s64\" ss:DefaultColumnWidth=\"60\"\n"+
                "   ss:DefaultRowHeight=\"11.25\">\n");
        texto.append(ENCABEZADO_XML_HOJA1);
        if (cEquipos != null && cEquipos.moveToFirst()) {
            do {
                // De cada equipo se toman los defectos/medidas asociados
                int col = cEquipos.getColumnIndex("NombreEquipo");
                String nombreEquipo = cEquipos.getString(col);
                col = cEquipos.getColumnIndex("PosicionTramo");
                String tramo = cEquipos.getString(col);
                Vector<Defecto> listaDefectos = dbRevisiones.solicitarDefectosMedidas(revision, nombreEquipo, tramo);
                col = cEquipos.getColumnIndex("TipoInstalacion");
                String tipo = cEquipos.getString(col);

                // Si no hay defectos/medidas asociados se incluye el defecto tal cual
                //if ((listaDefectos == null) || (listaDefectos.size()==0)) {
                // Se incluye el equipo en el Excel
                    texto.append("<Row>\n");
                    for (int i=1; i<=28; i++) {
                        String fecha;
                        texto.append("<Cell ss:StyleID=\"s79\">");
                        texto.append("<Data ss:Type=\"String\">");
                        switch (i) {
                            case 4:
                                if (tipo.equals("Z")) { // En los CTs no debe aparecer la PosiciónTramo
                                    texto.append("");
                                } else {
                                    texto.append(cEquipos.getString(i));
                                }
                                break;
                            case 8:
                                fecha = cEquipos.getString(i);
                                texto.append(corregirFecha(fecha));
                                break;
                            case 19:
                                fecha = cEquipos.getString(i);
                                texto.append(corregirFecha(fecha));
                                break;
                            default:
                                texto.append(cEquipos.getString(i));
                                break;
                        }
                        texto.append("</Data>");
                        texto.append("</Cell>\n");
                    }
                    texto.append("</Row>\n");
                //} else { // Si hay defecto y/o medida se recorren todos los defectos
                if ((listaDefectos != null) && (listaDefectos.size()>0)) {
                    for (int i=0; i<listaDefectos.size(); i++) {
                        Defecto def = listaDefectos.elementAt(i);
                        boolean esDefecto =  def.getEsDefecto().equals(Aplicacion.SI);
                        // Si es defecto se incluye el defecto y el código correspondiente
                        if (esDefecto) {
                            texto.append("<Row>\n");
                            for (int j=1; j<=28; j++) {
                                String fecha;
                                texto.append("<Cell ss:StyleID=\"s79\">");
                                texto.append("<Data ss:Type=\"String\">");
                                switch (j){
                                    case 4:
                                        if (tipo.equals("Z")) {
                                            texto.append("");
                                        } else {
                                            texto.append(cEquipos.getString(j));
                                        }
                                        break;
                                    case 8:
                                        fecha = cEquipos.getString(j);
                                        texto.append(corregirFecha(fecha));
                                        break;
                                    case 9: // Se incluye el codigo del defecto
                                        texto.append(def.getCodigoDefecto());
                                        break;
                                    case 12: // Se incluye el número de ocurrencias del defecto
                                        texto.append(def.getOcurrencias());
                                        break;
                                    case 19: // Si el defecto se ha corregido se incluye la fecha de corrección
                                        if (def.getCorregido().equals(Aplicacion.SI)) {
                                            texto.append(corregirFecha(def.getFechaCorreccion()));
                                        } else {
                                            texto.append(cEquipos.getString(j));
                                        }
                                        break;
                                    case 23: // Se incluyen las observaciones del defecto además de las del equipo
                                        texto.append(def.getObservaciones());
                                        break;
                                    case 24:
                                        texto.append(def.getFoto1());
                                        break;
                                    case 25:
                                        texto.append(def.getFoto2());
                                        break;
                                    default:
                                        texto.append(cEquipos.getString(j));
                                        break;
                                }
                                texto.append("</Data>");
                                texto.append("</Cell>\n");
                            }
                            texto.append("</Row>\n");
                        }
                        // Si además tiene medida se incluye también
                        boolean hayMedida = (!def.getMedida().equals(""));
                        if (hayMedida) {
                            texto.append("<Row>\n");
                            for (int j=1; j<=28; j++) {
                                String fecha;
                                texto.append("<Cell ss:StyleID=\"s79\">");
                                texto.append("<Data ss:Type=\"String\">");
                                switch (j){
                                    case 4:
                                        if (tipo.equals("Z")) {
                                            texto.append("");
                                        } else {
                                            texto.append(cEquipos.getString(j));
                                        }
                                        break;
                                    case 8:
                                        fecha = cEquipos.getString(j);
                                        texto.append(corregirFecha(fecha));
                                        break;
                                    case 9: // Se incluye el codigo de la medida en lugar del codigo del defecto
                                        texto.append(equivalenciaMedidaCodigo(def.getCodigoDefecto()));
                                        break;
                                    case 12: // Se incluye la medida en lugar de las ocurrencias
                                        texto.append(def.getMedida());
                                        break;
                                    case 23: // Se incluyen las observaciones del defecto además de las del equipo
                                        texto.append("TR1");
                                        if (!def.getObservaciones().equals("")) {
                                            texto.append(": " + def.getObservaciones());
                                        }
                                        break;
                                    case 24:
                                        texto.append("");
                                        break;
                                    case 25:
                                        texto.append("");
                                        break;
                                    default:
                                        texto.append(cEquipos.getString(j));
                                        break;
                                }
                                texto.append("</Data>");
                                texto.append("</Cell>\n");
                            }
                            texto.append("</Row>\n");
                        }
                        // Si además tiene medida de Tr2 se incluye también
                        boolean hayMedidaTr2 = (!def.getMedidaTr2().equals(""));
                        if (hayMedidaTr2) {
                            texto.append("<Row>\n");
                            for (int j=1; j<=28; j++) {
                                String fecha;
                                texto.append("<Cell ss:StyleID=\"s79\">");
                                texto.append("<Data ss:Type=\"String\">");
                                switch (j){
                                    case 4:
                                        if (tipo.equals("Z")) {
                                            texto.append("");
                                        } else {
                                            texto.append(cEquipos.getString(j));
                                        }
                                        break;
                                    case 8:
                                        fecha = cEquipos.getString(j);
                                        texto.append(corregirFecha(fecha));
                                        break;
                                    case 9: // Se incluye el codigo de la medida en lugar del codigo del defecto
                                        texto.append(equivalenciaMedidaCodigo(def.getCodigoDefecto()));
                                        break;
                                    case 12: // Se incluye la medida en lugar de las ocurrencias
                                        texto.append(def.getMedidaTr2());
                                        break;
                                    case 23: // Se incluyen las observaciones del defecto además de las del equipo
                                        texto.append("TR2");
                                        if (!def.getObservaciones().equals("")) {
                                            texto.append(": " + def.getObservaciones());
                                        }
                                        break;
                                    case 24:
                                        texto.append("");
                                        break;
                                    case 25:
                                        texto.append("");
                                        break;
                                    default:
                                        texto.append(cEquipos.getString(j));
                                        break;
                                }
                                texto.append("</Data>");
                                texto.append("</Cell>\n");
                            }
                            texto.append("</Row>\n");
                        }
                        // Si además tiene medida de Tr2 se incluye también
                        boolean hayMedidaTr3 = (!def.getMedidaTr3().equals(""));
                        if (hayMedidaTr3) {
                            texto.append("<Row>\n");
                            for (int j=1; j<=28; j++) {
                                String fecha;
                                texto.append("<Cell ss:StyleID=\"s79\">");
                                texto.append("<Data ss:Type=\"String\">");
                                switch (j){
                                    case 4:
                                        if (tipo.equals("Z")) {
                                            texto.append("");
                                        } else {
                                            texto.append(cEquipos.getString(j));
                                        }
                                        break;
                                    case 8:
                                        fecha = cEquipos.getString(j);
                                        texto.append(corregirFecha(fecha));
                                        break;
                                    case 9:// Se incluye el codigo de la medida en lugar del codigo del defecto
                                        texto.append(equivalenciaMedidaCodigo(def.getCodigoDefecto()));
                                        break;
                                    case 12: // Se incluye la medida en lugar de las ocurrencias
                                        texto.append(def.getMedidaTr3());
                                        break;
                                    case 23: // Se incluyen las observaciones del defecto además de las del equipo
                                        texto.append("TR3");
                                        if (!def.getObservaciones().equals("")) {
                                            texto.append(": " + def.getObservaciones());
                                        }
                                        break;
                                    case 24:
                                        texto.append("");
                                        break;
                                    case 25:
                                        texto.append("");
                                        break;
                                    default:
                                        texto.append(cEquipos.getString(j));
                                        break;
                                }
                                texto.append("</Data>");
                                texto.append("</Cell>\n");
                            }
                            texto.append("</Row>\n");
                        }
                    }
                }
            } while (cEquipos.moveToNext());
        }
        texto.append("</Table>\n");
        texto.append("</Worksheet>\n");
        cEquipos.close();

        // Hoja 2: Apoyos (Características Apoyos)
        Cursor cApoyos = dbRevisiones.solicitarDatosTodosApoyos(revision);
        texto.append(ENCABEZADO_XML_HOJA2);
        if (cApoyos != null && cApoyos.moveToFirst()) {
            do {
                texto.append("<Row>\n");
                texto.append("<Cell ss:StyleID=\"s79\"><Data ss:Type=\"String\"></Data></Cell>\n");
                for (int i=1; i<=18; i++) {
                    texto.append("<Cell ss:StyleID=\"s79\">");
                    texto.append("<Data ss:Type=\"String\">");
                    texto.append(cApoyos.getString(i));
                    texto.append("</Data>");
                    texto.append("</Cell>\n");
                }
                texto.append("</Row>\n");
            } while (cApoyos.moveToNext());
        }
        texto.append("</Table>\n");
        texto.append("</Worksheet>\n");
        cApoyos.close();

        // Hoja 3: Equipos no revisable
        Cursor cEquiposNoRevisables = dbRevisiones.solicitarDatosEquiposNoRevisables(revision);
        Revision rev = dbRevisiones.solicitarRevision(revision);
        texto.append("<Worksheet ss:Name=\"UNIDADES_REV_DATOS_REV_1.xls\">\n");
        texto.append("<Table>\n");
        texto.append(ENCABEZADO_XML_HOJA3_A);
        // Datos cabecera hoja 3
        texto.append("<Row>\n<Cell ss:Index=\"2\" ss:MergeAcross=\"4\" ss:StyleID=\"m14596960\">" +
                "<Data ss:Type=\"String\">Metodología Utilizada:</Data></Cell>\n");
        texto.append("<Cell ss:StyleID=\"s79\"><Data ss:Type=\"String\">" +
                rev.getMetodologia() + "</Data></Cell>\n</Row>\n");
        texto.append("<Row>\n<Cell ss:Index=\"2\" ss:MergeAcross=\"4\" ss:StyleID=\"m14596960\">" +
                "<Data ss:Type=\"String\">NIF, Nombre y Apellidos de los Revisores:</Data></Cell>\n");
        texto.append("<Cell ss:StyleID=\"s79\"><Data ss:Type=\"String\">" +
                rev.getInspector1() + ", " + rev.getInspector2() + "</Data></Cell>\n</Row>\n");
        texto.append("<Row>\n<Cell ss:Index=\"2\" ss:MergeAcross=\"4\" ss:StyleID=\"m14596960\">" +
                "<Data ss:Type=\"String\">Nombre y apellidos del Colegiado que firmará la inspección:</Data></Cell>\n");
        texto.append("<Cell ss:StyleID=\"s79\"><Data ss:Type=\"String\">" +
                rev.getColegiado() + "</Data></Cell>\n</Row>\n");
        texto.append("<Row>\n<Cell ss:Index=\"2\" ss:MergeAcross=\"4\" ss:StyleID=\"m14596960\">" +
                "<Data ss:Type=\"String\">Equipos utilizados en la inspección (marca y modelo):</Data></Cell>\n");
        texto.append("<Cell ss:StyleID=\"s79\"><Data ss:Type=\"String\">" +
                rev.getEquiposUsados() + "</Data></Cell>\n</Row>\n");
        texto.append("<Row>\n<Cell ss:Index=\"2\" ss:MergeAcross=\"4\" ss:StyleID=\"m14596960\">" +
                "<Data ss:Type=\"String\">Código de Inspección: </Data></Cell>\n");
        texto.append("<Cell ss:StyleID=\"s79\"><Data ss:Type=\"String\">" +
                rev.getCodigoInspeccion() + "</Data></Cell>\n</Row>\n");
        texto.append("<Row></Row>\n<Row></Row>\n");
        texto.append(ENCABEZADO_XML_HOJA3_B);
        // Datos cuerpo hoja 3
        if (cEquiposNoRevisables != null && cEquiposNoRevisables.moveToFirst()) {
            do {
                texto.append("<Row>\n");
                for (int i=1; i<=2; i++) {
                    texto.append("<Cell ss:StyleID=\"s79\">");
                    texto.append("<Data ss:Type=\"String\">");
                    texto.append(cEquiposNoRevisables.getString(i));
                    texto.append("</Data>");
                    texto.append("</Cell>\n");
                }
                texto.append("</Row>\n");
            } while (cEquiposNoRevisables.moveToNext());
        }
        texto.append("</Table>\n");
        texto.append("</Worksheet>\n");
        cEquiposNoRevisables.close();

        // Fin del archivo
        texto.append("</Workbook>");
        return texto.toString();
    }

    /**
     * Genera el archivo KML con los datos recogidos hasta ese momento
     *
     * @param revision
     */
    public static void generarKML(String revision){
        String nombreArchivo = revision + ".kml";
        File archivo = crearArchivoSalida(revision, nombreArchivo);

        try {
            FileOutputStream fos = new FileOutputStream(archivo);
            fos.write(generarCuerpoKML(revision).getBytes());
            fos.close();
        } catch (Exception e) {
            Log.e ("Error RPR: ", e.toString());
            Aplicacion.print("ErrorRPR: Error al generar el archivo KML" + e.toString());
        }

    }

    /**
     * Genera el cuerpo del KML
     *
     * @param revision
     * @return
     */
    public static String generarCuerpoKML(String revision) {
        StringBuffer texto = new StringBuffer();
        DBRevisiones dbRevisiones = new DBRevisiones(contexto);

        // Encabezado
        texto.append(ENCABEZADO_KML);
        texto.append("<Document>\n"); // Apertura documento
        texto.append("<name>" + revision + "</name>\n");

        // Corrección inmediata
        texto.append("<open>1</open>\n<Folder>\n<name>" +
                "CORRECCIÓN INMEDIATA</name>\n<open>1</open>\n"); // Apertura carpeta "Corrección inmediata"
        texto.append("<Folder>\n<open>0</open>\n<name>CDs</name>\n"); // Apertura carpeta CDs
        texto.append(incluirCDsCorreccionInmediata(revision));
        texto.append("</Folder>\n<Folder>\n<open>0</open>\n<name>Apoyos</name>\n"); // Cierre carpeta CDs y apertura carpeta Apoyos
        texto.append(incluirApoyosCorreccionInmediata(revision));
        texto.append("</Folder>\n"); // Cierre carpeta Apoyos
        texto.append("</Folder>\n"); // Cierre carpeta "Corrección inmediata"

        // Defectos estratégicos
        texto.append("<open>1</open>\n<Folder>\n<name>" +
                "DEFECTOS ESTRATÉGICOS</name>\n<open>1</open>\n"); // Apertura carpeta "Defectos estratégicos"
        texto.append("<Folder>\n<open>0</open>\n<name>CDs</name>\n"); // Apertura carpeta CDs
        texto.append(incluirCDsDefectosEstrategicos(revision));
        texto.append("</Folder>\n<Folder>\n<open>0</open>\n<name>Apoyos</name>\n"); // Cierre carpeta CDs y apertura carpeta Apoyos
        texto.append(incluirApoyosDefectosEstrategicos(revision));
        texto.append("</Folder>\n"); // Cierre carpeta Apoyos
        texto.append("</Folder>\n"); // Cierre carpeta "Defectos estratégicos"

        // Defectos NO estratégicos
        texto.append("<open>1</open>\n<Folder>\n<name>" +
                "DEFECTOS NO ESTRATÉGICOS</name>\n<open>1</open>\n"); // Apertura carpeta "Defectos NO estratégicos"
        texto.append("<Folder>\n<open>0</open>\n<name>CDs</name>\n"); // Apertura carpeta CDs
        texto.append(incluirCDsDefectosNoEstrategicos(revision));
        texto.append("</Folder>\n<Folder>\n<open>0</open>\n<name>Apoyos</name>\n"); // Cierre carpeta CDs y apertura carpeta Apoyos
        texto.append(incluirApoyosDefectosNoEstrategicos(revision));
        texto.append("</Folder>\n"); // Cierre carpeta Apoyos
        texto.append("</Folder>\n"); // Cierre carpeta "Defectos NO estratégicos"

        //Posición elementos
        texto.append("<open>1</open>\n<Folder>\n<name>" +
                        "Posicion Elementos</name>\n<open>1</open>\n"); // Apertura carpeta "Posicion elementos"
        texto.append("<Folder>\n<open>0</open>\n<name>CDs</name>\n"); // Apertura carpeta CDs
        texto.append(incluirCDsKML(revision));
        texto.append("</Folder>\n<Folder>\n<open>0</open>\n<name>Apoyos</name>\n"); // Cierre carpeta CDs y apertura carpeta Apoyos
        texto.append(incluirApoyosKML(revision));
        texto.append("</Folder>\n"); // Cierre carpeta apoyos

        texto.append("</Folder>\n"); // Cierre carpeta "Posición elementos"
        // Trazas
        // TODO Incluir los tramos
        // Cierre archivo KML
        texto.append("</Document>");
        texto.append("</kml>");

        return texto.toString();
    }

    /**
     * Se incluyen los CDs con defectos de corrección inmediata en el cuerpo del KML
     * @param revision
     * @return
     */
    public static String incluirCDsCorreccionInmediata (String revision) {
        String descripción = "Descripcion";
        StringBuffer texto = new StringBuffer();
        DBRevisiones dbRevisiones = new DBRevisiones(contexto);
        Vector<Apoyo> listaCDs = dbRevisiones.solicitarCDsCorreccionInmediata(revision);

        if (listaCDs != null) {
            for (int i=0; i<listaCDs.size(); i++) {
                Apoyo apoyo = listaCDs.elementAt(i);
                texto.append("<Placemark>\n<visibility>1</visibility>\n"); // Apertura equipo
                texto.append("<name>" + apoyo.getNombreEquipo() + "</name>\n"); // Nombre equipo
                // TODO: modificar la descripción según instrucciones
                texto.append("<description>" + descripción + "</description>"); // Descripción

                texto.append("<Style>\n<IconStyle>\n<color>ff0000ff</color>\n<scale>1.1</scale>\n" +
                        "<Icon>\n<href>http://maps.google.com/mapfiles/kml/paddle/C.png</href>\n</Icon>\n</IconStyle>\n" +
                        "<ListStyle>\n<ItemIcon>\n<href>http://maps.google.com/mapfiles/kml/paddle/C-lv.png</href>\n" +
                        "</ItemIcon>\n</ListStyle>\n</Style>\n"); // Estilo
                texto.append("<Point>\n<gx:drawOrder>1</gx:drawOrder>\n<coordinates>" +
                        apoyo.getLongitud() + "," + apoyo.getLatitud() + ",0</coordinates>\n</Point>\n"); //Coordenadas
                texto.append("</Placemark>\n"); // Cierre equipo
            }
        }

        return texto.toString();
    }

    /**
     * Se incluyen los apoyos con defectos de corrección inmediata en el cuerpo del KML
     * @param revision
     * @return
     */
    public static String incluirApoyosCorreccionInmediata (String revision) {
        StringBuffer texto = new StringBuffer();
        DBRevisiones dbRevisiones = new DBRevisiones(contexto);
        Vector<Apoyo> listaApoyos = dbRevisiones.solicitarApoyosCorreccionInmediata(revision);

        if (listaApoyos != null) {
            for (int i=0; i<listaApoyos.size(); i++) {
                Apoyo apoyo = listaApoyos.elementAt(i);
                texto.append("<Placemark>\n<visibility>1</visibility>\n"); // Apertura equipo
                texto.append("<name>" + apoyo.getNombreEquipo() + "</name>\n"); // Nombre equipo
                // TODO: modificar la descripción según instrucciones
                texto.append("<description>Código:" + apoyo.getNombreEquipo() + "\nMaterial: " + apoyo.getMaterial() +
                        "\nTraza/Tramo traza: " + apoyo.getCodigoTramo() + "</description>\n"); // Descripción

                texto.append("<Style>\n<IconStyle>\n<color>ff0000ff</color>\n<scale>1.1</scale>\n<Icon>\n" +
                        "<href>http://maps.google.com/mapfiles/kml/paddle/A.png</href>\n</Icon>\n</IconStyle>\n" +
                        "<ListStyle>\n<ItemIcon>\n<href>http://maps.google.com/mapfiles/kml/paddle/A-lv.png</href>\n" +
                        "</ItemIcon>\n</ListStyle>\n</Style>\n"); // Estilo
                texto.append("<Point>\n<gx:drawOrder>1</gx:drawOrder>\n<coordinates>" +
                        apoyo.getLongitud() + "," + apoyo.getLatitud() + ",0</coordinates>\n</Point>\n"); //Coordenadas
                texto.append("</Placemark>\n"); // Cierre equipo
            }
        }

        return texto.toString();
    }

    /**
     * Se incluyen los CDs con defectos estrategicos en el cuerpo del KML
     * @param revision
     * @return
     */
    public static String incluirCDsDefectosEstrategicos (String revision) {
        String descripción = "Descripcion";
        StringBuffer texto = new StringBuffer();
        DBRevisiones dbRevisiones = new DBRevisiones(contexto);
        Vector<Apoyo> listaCDs = dbRevisiones.solicitarCDsDefectosEstrategicos(revision);

        if (listaCDs != null) {
            for (int i=0; i<listaCDs.size(); i++) {
                Apoyo apoyo = listaCDs.elementAt(i);
                texto.append("<Placemark>\n<visibility>1</visibility>\n"); // Apertura equipo
                texto.append("<name>" + apoyo.getNombreEquipo() + "</name>\n"); // Nombre equipo
                // TODO: modificar la descripción según instrucciones
                texto.append("<description>" + descripción + "</description>"); // Descripción

                texto.append("<Style>\n<IconStyle>\n<color>ff00ffff</color>\n<scale>1.1</scale>\n" +
                        "<Icon>\n<href>http://maps.google.com/mapfiles/kml/paddle/C.png</href>\n</Icon>\n</IconStyle>\n" +
                        "<ListStyle>\n<ItemIcon>\n<href>http://maps.google.com/mapfiles/kml/paddle/C-lv.png</href>\n" +
                        "</ItemIcon>\n</ListStyle>\n</Style>\n"); // Estilo
                texto.append("<Point>\n<gx:drawOrder>1</gx:drawOrder>\n<coordinates>" +
                        apoyo.getLongitud() + "," + apoyo.getLatitud() + ",0</coordinates>\n</Point>\n"); //Coordenadas
                texto.append("</Placemark>\n"); // Cierre equipo
            }
        }

        return texto.toString();
    }

    /**
     * Se incluyen los apoyos con defectos estrategicos en el cuerpo del KML
     * @param revision
     * @return
     */
    public static String incluirApoyosDefectosEstrategicos (String revision) {
        StringBuffer texto = new StringBuffer();
        DBRevisiones dbRevisiones = new DBRevisiones(contexto);
        Vector<Apoyo> listaApoyos = dbRevisiones.solicitarApoyosDefectosEstrategicos(revision);

        if (listaApoyos != null) {
            for (int i=0; i<listaApoyos.size(); i++) {
                Apoyo apoyo = listaApoyos.elementAt(i);
                texto.append("<Placemark>\n<visibility>1</visibility>\n"); // Apertura equipo
                texto.append("<name>" + apoyo.getNombreEquipo() + "</name>\n"); // Nombre equipo
                // TODO: modificar la descripción según instrucciones
                texto.append("<description>Código:" + apoyo.getNombreEquipo() + "\nMaterial: " + apoyo.getMaterial() +
                        "\nTraza/Tramo traza: " + apoyo.getCodigoTramo() + "</description>\n"); // Descripción

                texto.append("<Style>\n<IconStyle>\n<color>ff00ffff</color>\n<scale>1.1</scale>\n<Icon>\n" +
                        "<href>http://maps.google.com/mapfiles/kml/paddle/A.png</href>\n</Icon>\n</IconStyle>\n" +
                        "<ListStyle>\n<ItemIcon>\n<href>http://maps.google.com/mapfiles/kml/paddle/A-lv.png</href>\n" +
                        "</ItemIcon>\n</ListStyle>\n</Style>\n"); // Estilo
                texto.append("<Point>\n<gx:drawOrder>1</gx:drawOrder>\n<coordinates>" +
                        apoyo.getLongitud() + "," + apoyo.getLatitud() + ",0</coordinates>\n</Point>\n"); //Coordenadas
                texto.append("</Placemark>\n"); // Cierre equipo
            }
        }

        return texto.toString();
    }

    /**
     * Se incluyen los CDs con defectos NO estrategicos en el cuerpo del KML
     * @param revision
     * @return
     */
    public static String incluirCDsDefectosNoEstrategicos (String revision) {
        String descripción = "Descripcion";
        StringBuffer texto = new StringBuffer();
        DBRevisiones dbRevisiones = new DBRevisiones(contexto);
        Vector<Apoyo> listaCDs = dbRevisiones.solicitarCDsDefectosNoEstrategicos(revision);

        if (listaCDs != null) {
            for (int i=0; i<listaCDs.size(); i++) {
                Apoyo apoyo = listaCDs.elementAt(i);
                texto.append("<Placemark>\n<visibility>1</visibility>\n"); // Apertura equipo
                texto.append("<name>" + apoyo.getNombreEquipo() + "</name>\n"); // Nombre equipo
                // TODO: modificar la descripción según instrucciones
                texto.append("<description>" + descripción + "</description>"); // Descripción

                texto.append("<Style>\n<IconStyle>\n<color>ffff0000</color>\n<scale>1.1</scale>\n" +
                        "<Icon>\n<href>http://maps.google.com/mapfiles/kml/paddle/C.png</href>\n</Icon>\n</IconStyle>\n" +
                        "<ListStyle>\n<ItemIcon>\n<href>http://maps.google.com/mapfiles/kml/paddle/C-lv.png</href>\n" +
                        "</ItemIcon>\n</ListStyle>\n</Style>\n"); // Estilo
                texto.append("<Point>\n<gx:drawOrder>1</gx:drawOrder>\n<coordinates>" +
                        apoyo.getLongitud() + "," + apoyo.getLatitud() + ",0</coordinates>\n</Point>\n"); //Coordenadas
                texto.append("</Placemark>\n"); // Cierre equipo
            }
        }

        return texto.toString();
    }

    /**
     * Se incluyen los apoyos con defectos NO estrategicos en el cuerpo del KML
     * @param revision
     * @return
     */
    public static String incluirApoyosDefectosNoEstrategicos (String revision) {
        StringBuffer texto = new StringBuffer();
        DBRevisiones dbRevisiones = new DBRevisiones(contexto);
        Vector<Apoyo> listaApoyos = dbRevisiones.solicitarApoyosDefectosNoEstrategicos(revision);

        if (listaApoyos != null) {
            for (int i=0; i<listaApoyos.size(); i++) {
                Apoyo apoyo = listaApoyos.elementAt(i);
                texto.append("<Placemark>\n<visibility>1</visibility>\n"); // Apertura equipo
                texto.append("<name>" + apoyo.getNombreEquipo() + "</name>\n"); // Nombre equipo
                // TODO: modificar la descripción según instrucciones
                texto.append("<description>Código:" + apoyo.getNombreEquipo() + "\nMaterial: " + apoyo.getMaterial() +
                        "\nTraza/Tramo traza: " + apoyo.getCodigoTramo() + "</description>\n"); // Descripción

                texto.append("<Style>\n<IconStyle>\n<color>ffff0000</color>\n<scale>1.1</scale>\n<Icon>\n" +
                        "<href>http://maps.google.com/mapfiles/kml/paddle/A.png</href>\n</Icon>\n</IconStyle>\n" +
                        "<ListStyle>\n<ItemIcon>\n<href>http://maps.google.com/mapfiles/kml/paddle/A-lv.png</href>\n" +
                        "</ItemIcon>\n</ListStyle>\n</Style>\n"); // Estilo
                texto.append("<Point>\n<gx:drawOrder>1</gx:drawOrder>\n<coordinates>" +
                        apoyo.getLongitud() + "," + apoyo.getLatitud() + ",0</coordinates>\n</Point>\n"); //Coordenadas
                texto.append("</Placemark>\n"); // Cierre equipo
            }
        }

        return texto.toString();
    }

    /**
     * Se incluyen los CDs en el cuerpo del KML
     * @param revision
     * @return
     */
    public static String incluirCDsKML (String revision) {
        StringBuffer texto = new StringBuffer();
        DBRevisiones dbRevisiones = new DBRevisiones(contexto);
        Vector<Apoyo> listaCDs = dbRevisiones.solicitarDatosCDs(revision);

        if (listaCDs != null) {
            for (int i=0; i<listaCDs.size(); i++) {
                Apoyo apoyo = listaCDs.elementAt(i);
                texto.append("<Placemark>\n<visibility>0</visibility>\n"); // Apertura equipo
                texto.append("<name>" + apoyo.getNombreEquipo() + "</name>\n"); // Nombre equipo
                Equipo equipo = dbRevisiones.solicitarEquipo(revision, apoyo.getNombreEquipo(), apoyo.getCodigoTramo());
                if (equipo != null) {
                    texto.append("<description>" + equipo.getDescripcionInstalacion() + "</description>"); // Descripción
                }
                texto.append("<Style>\n<IconStyle>\n<color>ff00ff00</color>\n<scale>1.1</scale>\n" +
                        "<Icon>\n<href>http://maps.google.com/mapfiles/kml/paddle/C.png</href>\n</Icon>\n</IconStyle>\n" +
                        "<ListStyle>\n<ItemIcon>\n<href>http://maps.google.com/mapfiles/kml/paddle/C-lv.png</href>\n" +
                        "</ItemIcon>\n</ListStyle>\n</Style>\n"); // Estilo
                texto.append("<Point>\n<gx:drawOrder>1</gx:drawOrder>\n<coordinates>" +
                        apoyo.getLongitud() + "," + apoyo.getLatitud() + ",0</coordinates>\n</Point>\n"); //Coordenadas
                texto.append("</Placemark>\n"); // Cierre equipo
            }
        }

        return texto.toString();
    }

    /**
     * Se incluyen los apoyos en el cuerpo del KML
     *
     * @param revision
     * @return
     */
    public static String incluirApoyosKML(String revision) {
        StringBuffer texto = new StringBuffer();
        DBRevisiones dbRevisiones = new DBRevisiones(contexto);
        Vector<Apoyo> listaApoyos = dbRevisiones.solicitarDatosApoyos(revision);

        if (listaApoyos != null) {
            for (int i=0; i<listaApoyos.size(); i++) {
                Apoyo apoyo = listaApoyos.elementAt(i);
                texto.append("<Placemark>\n<visibility>0</visibility>\n"); // Apertura apoyo
                texto.append("<name>" + apoyo.getNombreEquipo() + "</name>\n"); // Nombre apoyo
                texto.append("<description>Código:" + apoyo.getNombreEquipo() + "\nMaterial: " + apoyo.getMaterial() +
                        "\nTraza/Tramo traza: " + apoyo.getCodigoTramo() + "</description>\n"); // Descripción
                texto.append("<Style>\n<IconStyle>\n<color>ff00ff00</color>\n<scale>1.1</scale>\n<Icon>\n" +
                        "<href>http://maps.google.com/mapfiles/kml/paddle/A.png</href>\n</Icon>\n</IconStyle>\n" +
                        "<ListStyle>\n<ItemIcon>\n<href>http://maps.google.com/mapfiles/kml/paddle/A-lv.png</href>\n" +
                        "</ItemIcon>\n</ListStyle>\n</Style>\n"); // Estilo
                texto.append("<Point>\n<gx:drawOrder>1</gx:drawOrder>\n<coordinates>" + apoyo.getLongitud() + "," +
                        apoyo.getLatitud() + ",0 </coordinates>\n</Point>\n");
                texto.append("</Placemark>\n"); // Cierre equipo
            }
        }

        return texto.toString();
    }

    /**
     * Genera el archivo defectos.txt con los datos recogidos hasta ese momento
     *
     * @param revision
     */
    public static void generarArchivoDefectosTXT(String revision){
        String nombreArchivo = revision + "_defectos.txt";
        File archivo = crearArchivoSalida(revision, nombreArchivo);

        try {
            FileOutputStream fos = new FileOutputStream(archivo);
            fos.write(generarCuerpoDefectosTXT(revision).getBytes());
            fos.close();
        } catch (Exception e) {
            Log.e ("Error RPR: ", e.toString());
            //Aplicacion.print("ErrorRPR: Error al generar el archivo de defectos " + e.toString());
        }

    }

    /**
     * Genera el cuerpo del archivo defectos.txt
     *
     * @param revision
     * @return
     */
    public static String generarCuerpoDefectosTXT(String revision) {
        StringBuffer texto = new StringBuffer();
        DBRevisiones dbRevisiones = new DBRevisiones(contexto);
        Cursor cDefectos = dbRevisiones.solicitarDatosDefectosPorRevision(revision);

        if (cDefectos != null && cDefectos.getCount()>0) {
            cDefectos.moveToFirst();
            do {
                String nombreEquipo = cDefectos.getString(1);
                Cursor cUTM = dbRevisiones.solicitarUTMEquipo(revision, nombreEquipo);
                texto.append(cDefectos.getString(0) + ";"); // NombreRevision
                texto.append(nombreEquipo + ";"); // NombreEquipo
                texto.append(cDefectos.getString(2) + ";"); // CodigoDefecto
                if (cUTM != null && cUTM.moveToFirst()) {
                    for (int i=0; i<cUTM.getColumnCount(); i++) {
                        String s = cUTM.getString(i);
                        if (s.contains(",")) {
                            s = s.substring(0, s.lastIndexOf(","));
                        }
                        texto.append(s + ";");
                    }
                    cUTM.close();
                }
                for (int j=3; j<(cDefectos.getColumnCount()-1); j++) {
                    texto.append(cDefectos.getString(j) + ";");
                }
                texto.append("\\n");
            } while (cDefectos.moveToNext());
            cDefectos.close();
        }

        return texto.toString();
    }

    /**
     * Genera el archivo equipos.txt con los datos recogidos hasta ese momento
     *
     * @param revision
     */
    public static void generarArchivoEquiposTXT(String revision) {
        String nombreArchivo = revision + "_equipos.txt";
        File archivo = crearArchivoSalida(revision, nombreArchivo);

        try {
            FileOutputStream fos = new FileOutputStream(archivo);
            fos.write(generarCuerpoEquiposTXT(revision).getBytes());
            fos.close();
        } catch (Exception e) {
            Log.e ("Error RPR: ", e.toString());
            Aplicacion.print("ErrorRPR: Error al generar el archivo de equipos " + e.toString());
        }

    }

    /**
     * Genera el cuerpo del archivo equipos.txt
     *
     * @param revision
     * @return
     */
    public static String generarCuerpoEquiposTXT (String revision) {
        StringBuffer texto = new StringBuffer();
        DBRevisiones dbRevisiones = new DBRevisiones(contexto);
        Vector<Apoyo> listaApoyos = dbRevisiones.solicitarListaApoyos(revision);

        if (listaApoyos != null) {
            for (int i=0; i<listaApoyos.size(); i++) {
                Apoyo apoyo = listaApoyos.elementAt(i);
                texto.append(apoyo.getNombreRevision() + ";");
                texto.append(apoyo.getNombreEquipo() + ";");
                texto.append(apoyo.getHusoApoyo() + ";");
                texto.append(apoyo.getCoordenadaXUTMApoyo() + ";");
                texto.append(apoyo.getCoordenadaYUTMApoyo() + ";");
                texto.append(apoyo.getLongitud() + ";");
                texto.append(apoyo.getLatitud() + ";");
                Cursor cFoto = dbRevisiones.solicitarItem(DBRevisiones.TABLA_EQUIPOS,
                        "DocumentosAsociar", "NombreEquipo", apoyo.getNombreEquipo());
                 if (cFoto != null && cFoto.moveToFirst()) {
                     String foto = cFoto.getString(0);
                    texto.append(foto + ";\\n");
                    cFoto.close();
                 }
            }
        }

        return texto.toString();
    }

    public static void moverFotos(Revision revision){
        // TODO: Mover fotos

    }

    public static void borrarFotos(Revision revision) {
        String ruta = Environment.getExternalStoragePublicDirectory
                (Environment.DIRECTORY_PICTURES) + "/";
        File f = recuperarArchivo(ruta, revision.getNombre());
        borrarDirectorio(ruta + "/" + revision.getNombre() + "/");

        if (f != null) {
            try {
                boolean deleted = f.delete();
                if (deleted) Aplicacion.print("Borrado");
            } catch (Exception e) {
                Aplicacion.print(Aplicacion.TAG + "Error al borrar las fotos " + e.toString());
            }
        }

    }

    public static void borrarDirectorio (String ruta) {
        File f = new File(ruta);
        File[] files = f.listFiles();
        if (files != null) {
            for (int i=0; i<files.length; i++) {
                File archivo = files[i];
                if(archivo.isDirectory()) {
                    try {
                        File[] fDir = archivo.listFiles();
                        if (fDir.length == 0) {
                            archivo.delete();
                        } else {
                            borrarDirectorio(archivo.getPath());
                            archivo.delete();
                        }
                    } catch (Exception e) {

                    }
                } else {
                    boolean deleted = archivo.delete();
                }
            }
        }
    }

    public static String equivalenciaMedidaCodigo (String defecto) {
        String medida = "";
        switch (defecto){
            case "T22B":
                medida = "1000";
                break;
            case "T53D":
                medida = "1010";
                break;
            case "T55D":
                medida = "1011";
                break;
            case "T62D":
                medida = "1013";
                break;
            default:
                break;
        }

        return medida;
    }

}
