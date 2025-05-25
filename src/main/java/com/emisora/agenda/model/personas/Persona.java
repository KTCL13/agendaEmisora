package com.emisora.agenda.model.personas;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.cglib.core.Local;

import com.emisora.agenda.enums.EstadoPersona;
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
    private EstadoPersona estado;
    private LocalDateTime fechaCreacion;
    private LocalDateTime fechaModificacion;

    @OneToMany(mappedBy = "persona", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private List<RolInstitucional> roles = new ArrayList<>();
    

    public void addRol(RolInstitucional rol) {
        this.roles.add(rol);
        rol.setPersona(this);
    }

    public void removeRol(RolInstitucional rol) {
        this.roles.remove(rol);
        rol.setPersona(null);
    }

}
