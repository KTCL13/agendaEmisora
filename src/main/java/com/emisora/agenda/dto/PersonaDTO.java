package com.emisora.agenda.dto;

import java.util.List;

public class PersonaDTO {
    private String nombre;
    private String correo;
    private List<RolDTO> roles;

    public PersonaDTO() {}

    public PersonaDTO(String nombre, String correo, List<RolDTO> roles) {
        this.nombre = nombre;
        this.correo = correo;
        this.roles = roles;
    }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getCorreo() { return correo; }
    public void setCorreo(String correo) { this.correo = correo; }
    public List<RolDTO> getRoles() { return roles; }
    public void setRoles(List<RolDTO> roles) { this.roles = roles; }
}
