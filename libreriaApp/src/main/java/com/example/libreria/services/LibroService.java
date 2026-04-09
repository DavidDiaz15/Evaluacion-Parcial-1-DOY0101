package com.example.libreria.services;


import com.example.libreria.model.Libro;
import com.example.libreria.repository.LibroRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LibroService {
    @Autowired
    private LibroRepository libroRepository;

    public List<Libro> getLibros() {
        return libroRepository.obtenerLibros();


    }

    public Libro saveLibro(Libro libro) {
        return libroRepository.guardarLibro(libro);

    }

    public Libro getLibroNombre(String nombre) {
        return libroRepository.buscarPorNombre(nombre);

    }

    // Nueva feature
    public List<Libro> getLibrosPorCategoria(String categoria) {
        return libroRepository.buscarPorCategoria(categoria);

    public String deleteLibro(String nombre) {
        libroRepository.eliminar(nombre);
        return "Producto eliminado";
    }
}
