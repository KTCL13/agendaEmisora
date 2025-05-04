package com.emisora.agenda.dto;

import java.util.List;

public class PersonaDTO {
    private String nombre;
    private String correo;
    private String username;
    private String password;
    private List<RolDTO> roles;

    public PersonaDTO() {}

    public PersonaDTO(String nombre, String correo, String username, String password, List<RolDTO> roles) {
        this.nombre = nombre;
        this.correo = correo;
        this.username = username;
        this.password = password;
        this.roles = roles;
    }

    // Getters y setters
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getCorreo() { return correo; }
    public void setCorreo(String correo) { this.correo = correo; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public List<RolDTO> getRoles() { return roles; }
    public void setRoles(List<RolDTO> roles) { this.roles = roles; }
}