package com.emisora.model;

import jakarta.persistence.*;
import java.util.List;

@Entity
public class Persona {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;
    private String correo;

    @ElementCollection
    @CollectionTable(name = "persona_rol", joinColumns = @JoinColumn(name = "persona_id"))
    @Column(name = "rol_tipo")
    private List<String> roles;

    public Persona() {}

    public Persona(String nombre, String correo, List<String> roles) {
        this.nombre = nombre;
        this.correo = correo;
        this.roles = roles;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getCorreo() { return correo; }
    public void setCorreo(String correo) { this.correo = correo; }
    public List<String> getRoles() { return roles; }
    public void setRoles(List<String> roles) { this.roles = roles; }

}
