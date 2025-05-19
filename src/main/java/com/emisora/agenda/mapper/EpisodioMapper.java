package com.emisora.agenda.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import com.emisora.agenda.dto.EpisodioDTO;
import com.emisora.agenda.model.Episodio;

@Mapper(componentModel = "spring")
public interface EpisodioMapper {

    EpisodioDTO toDTO(Episodio episodio);

    List<EpisodioDTO> toDTOList(List<Episodio> episodios);

    Episodio toEntity(EpisodioDTO episodioDTO);

    /**
     * Actualiza una entidad Episodio existente a partir de un EpisodioDTO.
     *
     * @param episodioDTO El DTO con los datos actualizados.
     * @param episodio    La entidad a actualizar.
     */
    void updateEntityFromDTO(EpisodioDTO episodioDTO, @MappingTarget Episodio episodio);
}
