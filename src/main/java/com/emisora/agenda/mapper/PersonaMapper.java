package com.emisora.agenda.mapper;
import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import org.mapstruct.Mappings;

import com.emisora.agenda.dto.PersonaDTO;
import com.emisora.agenda.model.personas.Persona;


@Mapper(componentModel = "spring", uses = {RolInstitucionalMapper.class}) 
public interface PersonaMapper {

   
    PersonaDTO toDto(Persona persona);

    @Mappings({
        @Mapping(target = "idPersona", ignore = true),
        @Mapping(target = "rolesInstitucionales", ignore = true),
    })
    Persona dtoToPersona(PersonaDTO dto);

    List<PersonaDTO> toDtoList(List<Persona> personas);
}
   
