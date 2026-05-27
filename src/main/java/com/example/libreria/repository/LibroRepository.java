package com.example.libreria.repository;

import com.example.libreria.model.Libro;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;


@Repository
public class LibroRepository {

        private List<Libro> listaLibros = new ArrayList<>();

        public List<Libro> obtenerLibros() {
            return listaLibros;
        }

        public Libro buscarPorNombre(String nombre) {
            for(Libro libro: listaLibros) {
                if(libro.getNombre().equalsIgnoreCase(nombre)){
                    return libro;
                }
            }
            return null;
        }

        public Libro guardarLibro(Libro lib) {
            listaLibros.add(lib);
            return lib;
        }

        //Mejorado
        public void eliminar(String nombre) {
        listaLibros.removeIf(libro -> libro.getNombre().equalsIgnoreCase(nombre));
        }

        // Metodo que busca libros de una categoria en eespecifico y los retorna en una lista
        public List<Libro> buscarPorCategoria(String categoria) {
            List<Libro> librosPorCategoria = new ArrayList<>();
            for(Libro libro : listaLibros) {
                if(libro.getCategoria().equalsIgnoreCase(categoria)) {
                    librosPorCategoria.add(libro);
                }
            }
            return librosPorCategoria;
        }



}
