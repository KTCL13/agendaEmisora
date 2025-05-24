package com.emisora.agenda.model;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

import com.emisora.agenda.model.personas.Persona;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "episodios")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Episodio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nombre;

    @Lob 
    private String descripcion;

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "episodio_fechas_emision", joinColumns = @JoinColumn(name = "episodio_id"))
    @Column(name = "fecha_emision")
    private List<LocalDate> fechasEmitidas; 

    private String duracion; 

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "productor_id")
    private Persona productor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "locutor_id")
    private Persona locutor;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "episodio_invitados",
        joinColumns = @JoinColumn(name = "episodio_id"),
        inverseJoinColumns = @JoinColumn(name = "persona_id")
    )
    private Set<Persona> invitados; 

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "episodio_canciones",
        joinColumns = @JoinColumn(name = "episodio_id"),
        inverseJoinColumns = @JoinColumn(name = "cancion_id")
    )
    private Set<Cancion> canciones;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "programa_id", nullable = false)
    private Programa programa; 
}