package com.emisora.agenda.reports;

import com.emisora.agenda.dto.EpisodioReporteDto;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

@Component
public class EpisodioPorPersonaExcelExporter {

    public byte[] generarExcel(List<EpisodioReporteDto> episodios) throws IOException {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Episodios por Persona");

        // Encabezado
        Row headerRow = sheet.createRow(0);
        headerRow.createCell(0).setCellValue("Nombre");
        headerRow.createCell(1).setCellValue("Descripción");
        headerRow.createCell(2).setCellValue("Programa");
        headerRow.createCell(3).setCellValue("Duración");

        // Datos
        int rowNum = 1;
        for (EpisodioReporteDto dto : episodios) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(dto.getNombre());
            row.createCell(1).setCellValue(dto.getDescripcion());
            row.createCell(2).setCellValue(dto.getPrograma());
            row.createCell(3).setCellValue(dto.getDuracion());
        }

        // Guardar archivo en memoria
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        workbook.write(outputStream);
        return outputStream.toByteArray();
    }
}
