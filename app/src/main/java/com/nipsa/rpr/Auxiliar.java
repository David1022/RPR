package com.nipsa.rpr;

public class Auxiliar {

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
                    "  <Style ss:ID=\"s62\" ss:Name=\"Hipervínculo\">\n" +
                    "   <Font ss:FontName=\"Arial\" ss:Color=\"#0000FF\" ss:Underline=\"Single\"/>\n" +
                    "  </Style>\n" +
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
                    "  <Style ss:ID=\"s102\" ss:Parent=\"s62\">\n" +
                    "   <Alignment ss:Horizontal=\"Center\" ss:Vertical=\"Bottom\"/>\n" +
                    "   <Borders>\n" +
                    "    <Border ss:Position=\"Bottom\" ss:LineStyle=\"Continuous\" ss:Weight=\"1\"/>\n" +
                    "    <Border ss:Position=\"Right\" ss:LineStyle=\"Continuous\" ss:Weight=\"1\"/>\n" +
                    "    <Border ss:Position=\"Top\" ss:LineStyle=\"Continuous\" ss:Weight=\"1\"/>\n" +
                    "   </Borders>\n" +
                    "   <Protection/>\n" +
                    "  </Style>\n" +
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
    public static final String ENCABEZADO_XML_HOJA1_A =
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
                    "    <Cell ss:MergeAcross=\"25\" ss:StyleID=\"s73\"><Data ss:Type=\"String\">CARGA DE INSPECCIONES DE TRAZAS Y CD´s ";
    public static final String ENCABEZADO_XML_HOJA1_B =
                    "</Data></Cell>\n"+
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
    public static final String ENCABEZADO_XML_HOJA2_A =
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
                    "    <Cell ss:Index=\"6\" ss:StyleID=\"s73\"><Data ss:Type=\"String\">CARGA DE APOYOS DE INSPECCIONES DE TRAZAS Y CD´s ";
    public static final String ENCABEZADO_XML_HOJA2_B =
                    "</Data></Cell>\n" +
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

    public Auxiliar() {}
}
