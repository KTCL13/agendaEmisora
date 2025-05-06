package com.emisora.agenda.dto;

import java.util.List;
import jakarta.validation.constraints.*;

public class PersonaDTO {

    @NotBlank(message = "El nombre es obligatorio")
    private String nombre;

    @NotBlank(message = "El correo es obligatorio")
    @Email(message = "Debe ser un correo válido")
    private String correo;

    @NotBlank(message = "El nombre de usuario es obligatorio")
    @Size(min = 4, max = 20, message = "El username debe tener entre 4 y 20 caracteres")
    private String username;

    @NotBlank(message = "La contraseña es obligatoria")
    @Size(min = 6, message = "La contraseña debe tener al menos 6 caracteres")
    private String password;

    @NotNull(message = "Debe asignar al menos un rol")
    @Size(min = 1, message = "Debe seleccionar al menos un rol")
    private List<RolDTO> roles;

    public PersonaDTO() {}

    /**
     *Constructor completo
     */
    public PersonaDTO(String nombre, String correo, String username, String password, List<RolDTO> roles) {
        this.nombre = nombre;
        this.correo = correo;
        this.username = username;
        this.password = password;
        this.roles = roles;
    }

    /**
     *Constructor sin password
     */
    public PersonaDTO(String nombre, String correo, List<RolDTO> roles) {
        this.nombre = nombre;
        this.correo = correo;
        this.roles = roles;
    }

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