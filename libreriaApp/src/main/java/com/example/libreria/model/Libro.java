package com.example.libreria.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class Libro {

    private String nombre;
    private int precio;
    private String categoria;

}
