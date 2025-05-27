package com.emisora.agenda.mapper;


import java.util.Set;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;


import com.emisora.agenda.dto.EpisodioDTO;
import com.emisora.agenda.dto.EpisodioResponseDTO;
import com.emisora.agenda.model.Cancion;
import com.emisora.agenda.model.Episodio;
import com.emisora.agenda.model.personas.Persona;

@Mapper(componentModel = "spring")
public interface EpisodioMapper {


    @Mapping(source = "productor.idPersona", target = "productorId")
    @Mapping(source = "locutor.idPersona", target = "locutorId")
    @Mapping(source = "programa.id", target = "programaId")
    @Mapping(target = "invitadosIds", expression = "java(mapPersonasToIds(episodio.getInvitados()))")
    @Mapping(target = "cancionIds", expression = "java(mapCancionesToIds(episodio.getCanciones()))")
    EpisodioDTO episodioToEpisodioDTO(Episodio episodio);

    EpisodioResponseDTO episodioToEpisodioResponseDTO(Episodio episodio);


    @Mapping(target = "productor", ignore = true) 
    @Mapping(target = "locutor", ignore = true)
    @Mapping(target = "programa", ignore = true)
    @Mapping(target = "invitados", ignore = true)
    @Mapping(target = "canciones", ignore = true)
    Episodio episodioDTOToEpisodio(EpisodioDTO episodioDTO);

    default Set<Long> mapPersonasToIds(Set<Persona> personas) {
        if (personas == null) return null;
        return personas.stream().map(Persona::getIdPersona).collect(java.util.stream.Collectors.toSet());
    }

    default Set<Long> mapCancionesToIds(Set<Cancion> canciones) {
        // Asumiendo que Cancion tiene un método getId()
        if (canciones == null) return null;
        return canciones.stream().map(Cancion::getId).collect(java.util.stream.Collectors.toSet());
    }

}
