package com.emisora.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Estudiante implements Rol {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String carrera;
    private int semestre;

    public Estudiante() {}

    public Estudiante(String carrera, int semestre) {
        this.carrera = carrera;
        this.semestre = semestre;
    }

    @Override
    public String getDescripcion() {
        return "Carrera: " + carrera + " Semestre: " + semestre;
    }


    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getCarrera() {
        return carrera;
    }
    public void setCarrera(String carrera) {
        this.carrera = carrera;
    }
    public int getSemestre() {
        return semestre;
    }
    public void setSemestre(int semestre) {
        this.semestre = semestre;
    }

}
