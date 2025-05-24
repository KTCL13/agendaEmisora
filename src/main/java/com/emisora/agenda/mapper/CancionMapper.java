package com.emisora.agenda.mapper;

import com.emisora.agenda.dto.CancionDTO;
import com.emisora.agenda.model.Cancion;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface CancionMapper {
    CancionMapper INSTANCE = Mappers.getMapper(CancionMapper.class);

    Cancion toEntity(CancionDTO dto);
    CancionDTO toDto(Cancion cancion);
}
