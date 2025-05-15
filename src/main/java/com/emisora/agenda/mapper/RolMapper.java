package com.emisora.agenda.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.Named;

import com.emisora.agenda.dto.RolDTO;
import com.emisora.agenda.enums.CarreraEnum;
import com.emisora.agenda.model.personas.EstudianteRol;
import com.emisora.agenda.model.personas.FuncionarioRol;
import com.emisora.agenda.model.personas.InvitadoRol;
import com.emisora.agenda.model.personas.ProfesorRol;
import com.emisora.agenda.model.personas.Rol;

@Mapper(componentModel = "spring")
public interface RolMapper {


   @Mappings({
        @Mapping(target = "rolId", ignore = true), // rolId es generado por la BD
        @Mapping(target = "persona", ignore = true), // persona se establece en la lógica de servicio
        // Mapeos explícitos para los campos de EstudianteRol desde RolDTO:
        @Mapping(source = "carrera", target = "carrera", qualifiedByName = "stringToCarreraEnumInternal"),
        @Mapping(source = "semestre", target = "semestre"),
        @Mapping(source = "codigoUniversidad", target = "codigoUniversidad"),
    })
    EstudianteRol dtoToEstudianteRol(RolDTO dto);

    @Mappings({
        @Mapping(target = "rolId", ignore = true),
        @Mapping(target = "persona", ignore = true),
   
        @Mapping(source = "carrera", target = "carrera", qualifiedByName = "stringToCarreraEnumInternal"),

        @Mapping(source = "codigoUniversidad", target = "codigoUniversidad"),
        @Mapping(source = "facultad", target = "facultad")
    })
    ProfesorRol dtoToProfesorRol(RolDTO dto);

    @Mappings({
        @Mapping(target = "rolId", ignore = true),
        @Mapping(target = "persona", ignore = true),
        @Mapping(source = "ocupacion", target = "ocupacion")
    })
    InvitadoRol dtoToInvitadoRol(RolDTO dto);

    @Mappings({
        @Mapping(target = "rolId", ignore = true),
        @Mapping(target = "persona", ignore = true),
        @Mapping(source = "cargo", target = "cargo"),
        @Mapping(source = "departamento", target = "departamento"),
        @Mapping(source = "codigoUniversidad", target = "codigoUniversidad")
    })
    FuncionarioRol dtoToFuncionarioRol(RolDTO dto);

    // --- Entidades de Rol a DTO de Respuesta ---


     default RolDTO rolToRolDTO(Rol rol) { // Cambiado el nombre para consistencia si este es el método principal
        if (rol == null) {
            System.out.println("DEBUG: Rol es nulo, retornando null"); // DEBUG
            return null;
        }
        // Delega a métodos de mapeo específicos basados en el tipo de instancia
        if (rol instanceof EstudianteRol) {
            System.out.println("DEBUG: Mapeando EstudianteRol con ID: " + rol.getRolId()); // DEBUG
            return estudianteRolToDTO((EstudianteRol) rol);
        } else if (rol instanceof ProfesorRol) {
            System.out.println("DEBUG: Mapeando ProfesorRol con ID: " + rol.getRolId()); // DEBUG
            return profesorRolToDTO((ProfesorRol) rol);
        } else if (rol instanceof InvitadoRol) {
            System.out.println("DEBUG: Mapeando InvitadoRol con ID: " + rol.getRolId()); // DEBUG
            return invitadoRolToDTO((InvitadoRol) rol);
        } else if (rol instanceof FuncionarioRol) {
            System.out.println("DEBUG: Mapeando FuncionarioRol con ID: " + rol.getRolId()); // DEBUG
            return funcionarioRolToDTO((FuncionarioRol) rol);
        }
        // Esto no debería ocurrir si todas tus subclases de Rol están cubiertas.
        throw new IllegalArgumentException("Tipo de Rol no mapeado a DTO: " + rol.getClass().getName());
    }

    // Para cada tipo concreto de Rol a RolResponseDTO
    @Mappings({
    @Mapping(target = "tipoRol", constant = "ESTUDIANTE"),
    @Mapping(target = "semestre", source = "semestre"),
    @Mapping(source = "carrera", target = "carrera", qualifiedByName = "carreraEnumToStringInternal"), 
    @Mapping(target = "codigoUniversidad", source = "codigoUniversidad"),
    @Mapping(target = "cargo", ignore = true),
    @Mapping(target = "departamento", ignore = true),
    @Mapping(target = "facultad", ignore = true),
    @Mapping(target = "ocupacion", ignore = true)
    })
    RolDTO estudianteRolToDTO(EstudianteRol estudianteRol);

    @Mappings({
    @Mapping(target = "tipoRol", constant = "PROFESOR"),
    @Mapping(target = "codigoUniversidad", source = "codigoUniversidad"),
    @Mapping(source = "carrera", target = "carrera", qualifiedByName = "carreraEnumToStringInternal"), 
    @Mapping(target = "facultad", source = "facultad"),
    @Mapping(target = "cargo", ignore = true),
    @Mapping(target = "departamento", ignore = true),
    @Mapping(target = "semestre", ignore = true),
    @Mapping(target = "ocupacion", ignore = true)
    })
    RolDTO profesorRolToDTO(ProfesorRol profesorRol);

    @Mappings({
    @Mapping(target = "tipoRol", constant = "INVITADO"),
    @Mapping(target = "ocupacion", source = "ocupacion"),
    @Mapping(target = "cargo", ignore = true),
    @Mapping(target = "carrera", ignore = true),
    @Mapping(target = "codigoUniversidad", ignore = true),
    @Mapping(target = "departamento", ignore = true),
    @Mapping(target = "facultad", ignore = true),
    @Mapping(target = "semestre", ignore = true)
    })
    RolDTO invitadoRolToDTO(InvitadoRol invitadoRol);

    @Mappings({
    @Mapping(target = "tipoRol", constant = "FUNCIONARIO"),
    @Mapping(target = "codigoUniversidad", source = "codigoUniversidad"),
    @Mapping(target = "cargo", source = "cargo"),
    @Mapping(target = "departamento", source = "departamento"),
    })
    RolDTO funcionarioRolToDTO(FuncionarioRol funcionarioRol);


    @Named("stringToCarreraEnumInternal")
    default CarreraEnum stringToCarreraEnum(String carreraStr) {
        if (carreraStr == null || carreraStr.trim().isEmpty()) {
            return null;
        }
        try {
            // Ajusta esta lógica según sea necesario (ej. insensibilidad a mayúsculas/minúsculas)
            return CarreraEnum.valueOf(carreraStr.toUpperCase().replace(" ", "_"));
        } catch (IllegalArgumentException e) {
            System.err.println("Valor de carrera no válido en DTO para Enum: '" + carreraStr + "'. Error: " + e.getMessage());
            // Considera lanzar una excepción si el campo es mandatorio y el valor es inválido.
            // throw new IllegalArgumentException("Valor de carrera inválido: " + carreraStr, e);
            return null; // O un valor por defecto si es apropiado
        }
    }

    @Named("carreraEnumToStringInternal")
    default String carreraEnumToString(CarreraEnum carreraEnum) {
        if (carreraEnum == null) {
            return null;
        }
        // return carreraEnum.getDescripcion(); // Si quieres usar una descripción amigable
        return carreraEnum.name(); // Por defecto, retorna el nombre de la constante del enum
    }
}
