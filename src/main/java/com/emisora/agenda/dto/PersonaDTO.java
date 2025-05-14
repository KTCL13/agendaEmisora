package com.emisora.agenda.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PersonaDTO {

    private Long id;
    public String nombre;
    public String apellido;
    public String correo;
    public String cedula;
    public String tipo; 

    // Campos opcionales según el tipo
    public String codigoUniversidad;
    public String carrera;
    public String cargo;
    public String facultad;
    public String ocupacion;
    public Integer semestre;
    public String departamento;
}