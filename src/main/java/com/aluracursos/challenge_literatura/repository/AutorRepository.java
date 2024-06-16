package com.aluracursos.challenge_literatura.repository;

import com.aluracursos.challenge_literatura.model.Autor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AutorRepository extends JpaRepository<Autor, Long> {


    @Query("SELECT a FROM Autor a WHERE UPPER(a.nombre) LIKE UPPER(CONCAT('%', :nombre, '%'))")
    Optional<Autor> buscaAutorBD(@Param("nombre") String nombre);

    @Query("SELECT a FROM Autor a WHERE a.nombre = :nombre")
    Optional<Autor> comprobarAutorBD(@Param("nombre") String nombre);


    @Query("SELECT a FROM Autor a WHERE a.fechaNacimiento <= :fecha AND a.fechaDefuncion >= :fecha")
    List<Autor> autorVivoEnDeterminadoAnio(@Param("fecha") String fecha);

}
