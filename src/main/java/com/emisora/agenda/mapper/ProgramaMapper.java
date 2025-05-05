package com.emisora.agenda.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import com.emisora.agenda.dto.ProgramaDTO;
import com.emisora.agenda.model.Programa;


@Mapper
public interface ProgramaMapper {

    ProgramaMapper INSTANCE = Mappers.getMapper(ProgramaMapper.class);

    ProgramaDTO toDto(Programa programa);
    Programa toEntity(ProgramaDTO programaDTO);

}
