package com.emisora.agenda.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.emisora.agenda.dto.ProgramaDTO;
import com.emisora.agenda.model.Programa;



@Mapper(componentModel = "spring")
public interface ProgramaMapper {
    
    // Eliminar esta línea - no es necesaria cuando usas componentModel = "spring"
    // ProgramaMapper INSTANCE = Mappers.getMapper(ProgramaMapper.class);

    @Mapping(target = "fechaCreacion", source = "fechaCreacion", dateFormat = "yyyy-MM-dd")
    ProgramaDTO toDto(Programa programa);
    
    @Mapping(target = "fechaCreacion", source = "fechaCreacion", dateFormat = "yyyy-MM-dd")
    Programa toEntity(ProgramaDTO programaDTO);
    
    // Los métodos de conversión personalizados son opcionales cuando usas dateFormat arriba
    // Pero si prefieres mantenerlos, asegúrate de que manejen valores nulos:
    
    default Date map(String value) {
        if (value == null) {
            return null; // Maneja el caso nulo explícitamente
        }
        try {
            return new SimpleDateFormat("yyyy-MM-dd").parse(value);
        } catch (ParseException e) {
            throw new RuntimeException("Invalid date format: " + value, e);
        }
    }

    default String map(Date value) {
        if (value == null) {
            return null; // Maneja el caso nulo explícitamente
        }
        return new SimpleDateFormat("yyyy-MM-dd").format(value);
    }
}


