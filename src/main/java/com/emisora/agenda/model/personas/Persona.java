package com.emisora.agenda.model.personas;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


import com.emisora.agenda.enums.EstadoPersona;
import com.emisora.agenda.enums.TipoId;

import com.emisora.agenda.model.Episodio;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "personas")
public class Persona {

    @Id
    @Column(name = "persona_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idPersona;

    private String nombresPersona;
    private String apellidosPersona;
    private String telefonoPersona;
    @Enumerated(EnumType.STRING)
    private TipoId tipoId;
    private String correo;
    private String numeroId;
    private EstadoPersona estado;
    private LocalDateTime fechaCreacionPersona;
    private LocalDateTime fechaModificacionPersona;

    @OneToMany(mappedBy = "persona", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private List<RolInstitucional> rolesInstitucionales = new ArrayList<>();
    

    public void addRol(RolInstitucional rol) {
        this.rolesInstitucionales.add(rol);
        rol.setPersona(this);
    }

    public void removeRol(RolInstitucional rol) {
        this.rolesInstitucionales.remove(rol);
        rol.setPersona(null);
    }

    @OneToMany(mappedBy = "productor")
    private List<Episodio> productos = new ArrayList<>();

    @OneToMany(mappedBy = "locutor")
    private List<Episodio> locuciones = new ArrayList<>();

    @ManyToMany(mappedBy = "invitados")
    private Set<Episodio> episodiosDondeEsInvitado = new HashSet<>();

}
