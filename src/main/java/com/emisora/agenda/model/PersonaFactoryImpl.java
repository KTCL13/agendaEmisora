package com.emisora.agenda.model;

import org.springframework.stereotype.Component;

import com.emisora.agenda.dto.PersonaDTO;

@Component
public class PersonaFactoryImpl implements PersonaFactory {

    @Override
    public Persona crearDesdeDTO(PersonaDTO dto) {
        switch (dto.tipo.toLowerCase()) {
            case "estudiante":
                Estudiante e = new Estudiante();
                e.setNombre(dto.nombre);
                e.setApellido(dto.apellido);
                e.setCorreo(dto.correo);
                e.setCedula(dto.cedula);
                e.setCodigoUniversidad(dto.codigoUniversidad);
                e.setCarrera(CarreraEnum.valueOf(dto.carrera.toUpperCase()));
                e.setSemestre(dto.semestre);
                return e;

            case "funcionario":
                Funcionario f = new Funcionario();
                f.setNombre(dto.nombre);
                f.setApellido(dto.apellido);
                f.setCorreo(dto.correo);
                f.setCedula(dto.cedula);
                f.setCargo(dto.cargo);
                f.setDepartamento(dto.departamento);
                f.setCodigoUniversidad(dto.codigoUniversidad);
                return f;

            case "profesor":
                Profesor p = new Profesor();
                p.setNombre(dto.nombre);
                p.setApellido(dto.apellido);
                p.setCorreo(dto.correo);
                p.setCedula(dto.cedula);
                p.setFacultad(dto.facultad);
                p.setCarrera(CarreraEnum.valueOf(dto.carrera.toUpperCase()));
                p.setCodigoUniversidad(dto.codigoUniversidad);
                return p;

            case "invitado":
                Invitado i = new Invitado();
                i.setNombre(dto.nombre);
                i.setApellido(dto.apellido);
                i.setCorreo(dto.correo);
                i.setCedula(dto.cedula);
                i.setOcupación(dto.ocupacion);
                return i;

            default:
                throw new IllegalArgumentException("Tipo de persona no válido: " + dto.tipo);
        }
    }


}
