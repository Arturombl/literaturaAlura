package com.aluracursos.challenge_literatura.model;

import jakarta.persistence.*;

import java.util.OptionalDouble;
import java.util.stream.Collectors;

@Entity
@Table(name = "libros")
public class Libro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String titulo;
    @Enumerated(EnumType.STRING)
    private Idioma idioma;
    private Double numeroDescargas;
    @ManyToOne
    @JoinColumn(name = "autor_id")
    private Autor autor;

    public Libro(){}

public Libro(DatosLibro datosLibro){
    this.titulo = datosLibro.titulo();
    this.idioma = Idioma.fromString(datosLibro.idioma().stream()
            .limit(1).collect(Collectors.joining()));
    this.numeroDescargas = OptionalDouble.of(Double.valueOf(datosLibro.numeroDeDescargas())).orElse(0);
}

    @Override
    public String toString() {
        return
                ", titulo='" + titulo + '\'' +
                ", Autor" + autor.getNombre() +
                ", idioma='" + idioma + '\'' +
                ", numeroDescargas=" + numeroDescargas;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public Autor getAutor() {
        return autor;
    }

    public void setAutor(Autor autor) {
        this.autor = autor;
    }

    public Idioma getIdioma() {
        return idioma;
    }

    public void setIdioma(Idioma idioma) {
        this.idioma = idioma;
    }

    public Double getNumeroDescargas() {
        return numeroDescargas;
    }

    public void setNumeroDescargas(Long numeroDescargas) {
        this.numeroDescargas = Double.valueOf(numeroDescargas);
    }



}
