package com.emisora.agenda.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;


import com.emisora.agenda.dto.RolInstitucionalDTO;

import com.emisora.agenda.model.personas.EstudianteRol;
import com.emisora.agenda.model.personas.FuncionarioRol;
import com.emisora.agenda.model.personas.InvitadoRol;
import com.emisora.agenda.model.personas.ProfesorRol;
import com.emisora.agenda.model.personas.RolInstitucional;

@Mapper(componentModel = "spring")
public interface RolInstitucionalMapper {


   @Mappings({
        @Mapping(target = "rolInstitucionalId", ignore = true), 
        @Mapping(target = "persona", ignore = true), // persona se establece en la lógica de servicio
        // Mapeos explícitos para los campos de EstudianteRol desde RolDTO:
        @Mapping(source = "carrera", target = "carrera"),
        @Mapping(source = "codigoEstudiante", target = "codigoEstudiante"),
    })
    EstudianteRol dtoToEstudianteRol(RolInstitucionalDTO dto);

    @Mappings({
        @Mapping(target = "rolInstitucionalId", ignore = true),
        @Mapping(target = "persona", ignore = true),
   
        @Mapping(source = "carrera", target = "carrera"),

        @Mapping(source = "facultad", target = "facultad")
    })
    ProfesorRol dtoToProfesorRol(RolInstitucionalDTO dto);

    @Mappings({
        @Mapping(target = "rolInstitucionalId", ignore = true),
        @Mapping(target = "persona", ignore = true),
    })
    InvitadoRol dtoToInvitadoRol(RolInstitucionalDTO dto);

    @Mappings({
        @Mapping(target = "rolInstitucionalId", ignore = true),
        @Mapping(target = "persona", ignore = true),
        @Mapping(source = "dependencia", target = "dependencia")
    })
    FuncionarioRol dtoToFuncionarioRol(RolInstitucionalDTO dto);

    // --- Entidades de Rol a DTO de Respuesta ---


     default RolInstitucionalDTO rolToRolDTO(RolInstitucional rol) { 
        if (rol == null) {
            System.out.println("DEBUG: Rol es nulo, retornando null"); // DEBUG
            return null;
        }
        // Delega a métodos de mapeo específicos basados en el tipo de instancia
        if (rol instanceof EstudianteRol) {
            System.out.println("DEBUG: Mapeando EstudianteRol con ID: " + rol.getRolInstitucionalId()); // DEBUG
            return estudianteRolToDTO((EstudianteRol) rol);
        } else if (rol instanceof ProfesorRol) {
            System.out.println("DEBUG: Mapeando ProfesorRol con ID: " + rol.getRolInstitucionalId()); // DEBUG
            return profesorRolToDTO((ProfesorRol) rol);
        } else if (rol instanceof InvitadoRol) {
            System.out.println("DEBUG: Mapeando InvitadoRol con ID: " + rol.getRolInstitucionalId()); // DEBUG
            return invitadoRolToDTO((InvitadoRol) rol);
        } else if (rol instanceof FuncionarioRol) {
            System.out.println("DEBUG: Mapeando FuncionarioRol con ID: " + rol.getRolInstitucionalId()); // DEBUG
            return funcionarioRolToDTO((FuncionarioRol) rol);
        }
        throw new IllegalArgumentException("Tipo de Rol no mapeado a DTO: " + rol.getClass().getName());
    }

    // Para cada tipo concreto de Rol a RolResponseDTO
    @Mappings({
    @Mapping(target = "tipoRol", constant = "ESTUDIANTE"),
    @Mapping(source = "carrera", target = "carrera"), 
    @Mapping(target = "codigoEstudiante", source = "codigoEstudiante"),
    })
    RolInstitucionalDTO estudianteRolToDTO(EstudianteRol estudianteRol);

    @Mappings({
    @Mapping(target = "tipoRol", constant = "DOCENTE"),
    @Mapping(source = "carrera", target = "carrera"), 
    @Mapping(target = "facultad", source = "facultad"),
    })
    RolInstitucionalDTO profesorRolToDTO(ProfesorRol profesorRol);

    @Mappings({
    @Mapping(target = "tipoRol", constant = "INVITADO"),
    })
    RolInstitucionalDTO invitadoRolToDTO(InvitadoRol invitadoRol);

    @Mappings({
    @Mapping(target = "tipoRol", constant = "FUNCIONARIO"),
    @Mapping(target = "dependencia", source = "dependencia")
    })
    RolInstitucionalDTO funcionarioRolToDTO(FuncionarioRol funcionarioRol);


}
