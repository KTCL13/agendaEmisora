package com.emisora.agenda.model;

import jakarta.persistence.*;
import java.util.List;

@Entity
public class Persona {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @NotBlank(message = "El nombre es obligatorio")
    private String nombre;

    @Column(nullable = false)
    @NotBlank(message = "El correo es obligatorio")
    @Email(message = "Debe ser un correo válido")
    private String correo;

    @Column(unique = true, nullable = false)
    @NotBlank(message = "El username es obligatorio")
    @Size(min = 4, max = 20, message = "El username debe tener entre 4 y 20 caracteres")
    private String username;

    @Column(nullable = false)
    @NotBlank(message = "La contraseña es obligatoria")
    @Size(min = 6, message = "La contraseña debe tener al menos 6 caracteres")
    private String password;

    // Lista de roles permitidos: FUNCIONARIO, PROFESOR, ESTUDIANTE, INVITADO, ROLE_ADMIN
    @ElementCollection
    @CollectionTable(name = "persona_rol", joinColumns = @JoinColumn(name = "persona_id"))
    @Column(name = "rol_tipo", nullable = false)
    @Size(min = 1, message = "Debe asignar al menos un rol")
    private List<String> roles;

    public Persona() {}

    public Persona(String nombre, String correo, String username, String password, List<String> roles) {
        this.nombre = nombre;
        this.correo = correo;
        this.username = username;
        this.password = password;
        this.roles = roles;
    }

    // Getters y Setters

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getCorreo() { return correo; }
    public void setCorreo(String correo) { this.correo = correo; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public List<String> getRoles() { return roles; }
    public void setRoles(List<String> roles) { this.roles = roles; }
}
