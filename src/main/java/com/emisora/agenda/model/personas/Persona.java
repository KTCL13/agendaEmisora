package com.emisora.agenda.model.personas;

import java.util.ArrayList;
import java.util.List;

import com.emisora.agenda.enums.TipoId;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "personas")
public class Persona {

    @Id
    @Column(name = "persona_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombres;
    private String apellidos;
    private String telefono;
    @Enumerated(EnumType.STRING)
    private TipoId tipoId;
    private String correo;
    private String numeroId;

    @OneToMany(mappedBy = "persona", cascade = CascadeType.ALL, fetch = FetchType.LAZY) 
    private List<Rol> roles = new ArrayList<>();
    

    public void addRol(Rol rol) {
        this.roles.add(rol);
        rol.setPersona(this);
    }

    public void removeRol(Rol rol) {
        this.roles.remove(rol);
        rol.setPersona(null);
    }

}
