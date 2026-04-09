package com.example.libreria.controller;

import com.example.libreria.model.Libro;
import com.example.libreria.services.LibroService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/productos")

public class LibroController {

    @Autowired
    private LibroService libroService;

    @GetMapping
    public List<Libro> listarLibros() {
        return libroService.getLibros();

    }

    @PostMapping
    public Libro agregarLibro(@RequestBody Libro libro){
        return libroService.saveLibro(libro);

    }

    @GetMapping("/buscar/{nombre}")
    public Libro buscarLibro(@PathVariable String nombre) {
        return libroService.getLibroNombre(nombre);

    }

    // Nueva feature
    @GetMapping("/categoria/{categoria}")
    public List<Libro> listarPorCategoria(@PathVariable String categoria) {
        return libroService.getLibrosPorCategoria(categoria);
    }

    @DeleteMapping("/borrar/{nombre}")
    public String eliminarLbro(@PathVariable String nombre) {
        return  libroService.deleteLibro(nombre);
    }

}
