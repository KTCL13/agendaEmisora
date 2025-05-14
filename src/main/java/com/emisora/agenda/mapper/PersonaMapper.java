package com.emisora.agenda.mapper;

import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import com.emisora.agenda.dto.PersonaDTO;
import com.emisora.agenda.model.Estudiante;
import com.emisora.agenda.model.Funcionario;
import com.emisora.agenda.model.Invitado;
import com.emisora.agenda.model.Persona;
import com.emisora.agenda.model.Profesor;

@Mapper(componentModel = "spring")
public interface PersonaMapper {


    PersonaDTO toDto(Persona persona);

       @AfterMapping
    default void setTipoYCamposEspecificos(Persona persona, @MappingTarget PersonaDTO dto) {
        if (persona instanceof Estudiante) {
            dto.tipo = "ESTUDIANTE";
            dto.carrera = ((Estudiante) persona).getCarrera().toString(); 
            dto.semestre = ((Estudiante) persona).getSemestre();
            dto.codigoUniversidad = ((Estudiante) persona).getCodigoUniversidad();
        }else if (persona instanceof Profesor) {
            dto.tipo = "PROFESOR";
            dto.carrera = ((Profesor) persona).getCarrera().toString();
            dto.codigoUniversidad = ((Profesor) persona).getCodigoUniversidad();
            dto.facultad = ((Profesor) persona).getFacultad().toString();
        } else if (persona instanceof Funcionario) {
            dto.tipo = "FUNCIONARIO";
            dto.cargo = ((Funcionario) persona).getCargo();
            dto.departamento = ((Funcionario) persona).getDepartamento();
            dto.codigoUniversidad = ((Funcionario) persona).getCodigoUniversidad();
        } else  if (persona instanceof Invitado) {
            dto.tipo = "INVITADO";
            dto.ocupacion = ((Invitado) persona).getOcupación();
        } else {
            dto.tipo = "DESCONOCIDO"; 
        }
    }
}
   
