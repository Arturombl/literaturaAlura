package com.aluracursos.challenge_literatura.principal;

import com.aluracursos.challenge_literatura.model.*;
import com.aluracursos.challenge_literatura.repository.AutorRepository;
import com.aluracursos.challenge_literatura.service.ConsumoApi;
import com.aluracursos.challenge_literatura.service.ConvierteDatos;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Principal {
    private static final String URL_BASE = "https://gutendex.com/books/?search=";
    private ConsumoApi consumoApi = new ConsumoApi();
    private ConvierteDatos conversor = new ConvierteDatos();
    private Scanner teclado = new Scanner(System.in);
    private AutorRepository repositorio;
    private Optional <DatosLibro> libroBuscado;

    public Principal(AutorRepository repository) {
        this.repositorio = repository;
    }

    public void muestraElMenu() {
        var opcion = -1;
        while (opcion != 0) {
            var menu = """
                    1 - Buscar libro por titulo
                    2 - Listar libros registrados
                    3 - listar autores registrados
                    4 - listar autores vivos en un determinado año
                    5 - listar libros por idioma
                                  
                    0 - Salir
                    """;
            System.out.println(menu);
            opcion = teclado.nextInt();
            teclado.nextLine();

            switch (opcion) {
                case 1:
                    buscarLibroPorTitulo();
                    break;
                case 2:
                    System.out.println("En Mantenimiento");
                    break;
                case 3:
                    System.out.println("En Mantenimiento");
                    break;
                case 4:
                    System.out.println("En Mantenimiento");
                    break;
                case 5:
                    System.out.println("En Mantenimiento");
                    break;
                case 0:
                    System.out.println("Cerrando la aplicación...");
                    break;
                default:
                    System.out.println("Opción del menu inválida");
            }

        }
    }

    private Datos getDatosLibro() {
        System.out.println("Escribe el nombre del libro que deseas buscar");
        var nombreLibro = teclado.nextLine();
        var json = consumoApi.obtenerDatos(URL_BASE + nombreLibro.replace(" ", "+"));
        System.out.println(json);

        var datos = conversor.obtenerDatos(json, Datos.class);

        // Process valid data
        libroBuscado = datos.libros().stream()
                .findFirst();
        if (libroBuscado.isPresent()) {
            System.out.println(
                    "*********************************************\n" +
                            "\n------------- LIBRO ENCONTRADO  --------------" +
                            "\nTítulo: " + libroBuscado.get().titulo() +
                            "\nAutor: " + libroBuscado.get().autor().stream()
                            .map(a -> a.nombre()).limit(1).collect(Collectors.joining()) +
                            "\n--------------------------------------\n"
            );
            return datos;
        } else {
            return null;
        }
    }

    private void buscarLibroPorTitulo() {
        Datos datos = getDatosLibro();
        List<Libro> libroEncontrado = libroBuscado.stream().map(a -> new Libro(a)).collect(Collectors.toList());
        Autor autorAPI = libroBuscado.stream().
                flatMap(l -> l.autor().stream()
                        .map(a -> new Autor(a)))
                .collect(Collectors.toList()).stream().findFirst().get();
        Optional<Autor> autorBuscado = repositorio.buscaAutorBD(libroBuscado.get().autor().stream()
                .map(a -> a.nombre())
                .collect(Collectors.joining()));
        Optional<Libro> libroBuscado = repositorio.buscarLibroBD(datos.libros().get(0).titulo());
        if (libroBuscado.isPresent()) {
            System.out.println("Libro ya registrado en la base de datos");
        }else {
            Autor autor;
            if (autorBuscado.isPresent()) {
                autor = autorBuscado.get();
                System.out.println("EL autor ya esta guardado en la BD");
            }else {
                autor = autorAPI;
                repositorio.save(autor);
            }
            autor.setLibro(libroEncontrado);
            System.out.println();
            repositorio.save(autor);
        }
    }

}
