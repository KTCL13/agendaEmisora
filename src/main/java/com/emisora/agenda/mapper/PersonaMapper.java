package com.emisora.agenda.mapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import org.mapstruct.Mappings;

import com.emisora.agenda.dto.PersonaDTO;
import com.emisora.agenda.model.personas.Persona;


@Mapper(componentModel = "spring", uses = {RolMapper.class}) 
public interface PersonaMapper {


    PersonaDTO toDto(Persona persona);

    @Mappings({
        @Mapping(target = "id", ignore = true),
        @Mapping(target = "roles", ignore = true)
    })

   
    Persona dtoToPersona(PersonaDTO dto);
}
   
