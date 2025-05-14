package com.emisora.agenda.mapper;

import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import com.emisora.agenda.dto.PersonaDTO;
import com.emisora.agenda.model.Estudiante;
import com.emisora.agenda.model.Persona;

@Mapper(componentModel = "spring")
public interface PersonaMapper {


    PersonaDTO toDto(Persona persona);

       @AfterMapping
    default void setTipoYCamposEspecificos(Persona persona, @MappingTarget PersonaDTO dto) {
        if (persona instanceof Estudiante) {
            dto.tipo = "ESTUDIANTE"; 
        }
    }
}
   
