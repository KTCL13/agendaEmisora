package com.emisora.agenda.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.emisora.agenda.dto.PersonaDTO;
import com.emisora.agenda.model.Persona;

@Mapper(componentModel = "spring")
public interface PersonaMapper {
    
    @Mapping(target = "cargo", source = "cargo")
    @Mapping(target = "carrera", source = "carrera")
    @Mapping(target = "codigoUniversidad", source = "codigoUniversidad")
    @Mapping(target = "departamento", source = "departamento")
    @Mapping(target = "facultad", source = "facultad")
    @Mapping(target = "ocupacion", source = "ocupacion")
    @Mapping(target = "semestre", source = "semestre")
    @Mapping(target = "tipo", source = "tipo")
    PersonaDTO toDto(Persona persona);

} 