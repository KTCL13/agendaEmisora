package com.emisora.agenda.reports;

import com.emisora.agenda.dto.CancionReporteDto;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

@Component
public class ExcelExporterCancionPorPrograma {

    public byte[] generarExcel(List<CancionReporteDto> canciones) throws IOException {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Canciones del Programa");

        // Fila de encabezados
        Row headerRow = sheet.createRow(0);
        Cell headerCell0 = headerRow.createCell(0);
        headerCell0.setCellValue("Título");
        Cell headerCell1 = headerRow.createCell(1);
        headerCell1.setCellValue("Artista");

        // Datos
        int rowNum = 1;
        for (CancionReporteDto cancion : canciones) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(cancion.getTitulo());
            row.createCell(1).setCellValue(cancion.getArtista());
        }

        // Guardar en memoria
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        workbook.write(outputStream);
        return outputStream.toByteArray();
    }
}
