package com.emisora.agenda.model;

import java.sql.Date;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;

import lombok.NoArgsConstructor;


@Entity
@Data
@Table(name = "programas")
@AllArgsConstructor
@NoArgsConstructor
public class Programa {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String codigo;
    private String titulo;
    private String descripcion;
    private String categoria;
    private Date fechaCreacion;

    @OneToMany(mappedBy = "programa")
    private List<Episodio> episodios;
}
