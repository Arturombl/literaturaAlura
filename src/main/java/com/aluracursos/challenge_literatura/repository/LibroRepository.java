package com.aluracursos.challenge_literatura.repository;

import com.aluracursos.challenge_literatura.model.Idioma;
import com.aluracursos.challenge_literatura.model.Libro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LibroRepository extends JpaRepository<Libro, Long> {

    @Query("SELECT l FROM Libro l WHERE l.idioma = :idioma")
    List<Libro> buscarLibrosPorIdioma(@Param("idioma") Idioma idioma);

    @Query("SELECT l FROM Libro l WHERE l.titulo = :nombre")
    Optional<Libro> comprobarLibroBD(@Param("nombre") String nombre);

}
