package com.emisora.agenda.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.emisora.agenda.dto.ProgramaDTO;
import com.emisora.agenda.model.Programa;



@Mapper(componentModel = "spring" )
public interface ProgramaMapper {

    @Mapping(target = "fechaCreacion", source = "fechaCreacion", dateFormat = "yyyy-MM-dd")
    ProgramaDTO toDto(Programa programa);
    
    @Mapping(target = "fechaCreacion", source = "fechaCreacion", dateFormat = "yyyy-MM-dd")
    Programa toEntity(ProgramaDTO programaDTO); 
    
}


