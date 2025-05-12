package com.emisora.agenda.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "funcionarios")
public class Funcionario extends Persona {

    private String departamento;
    private String cargo;
    private String codigoUniversidad;

}
