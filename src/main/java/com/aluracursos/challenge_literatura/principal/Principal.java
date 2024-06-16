package com.aluracursos.challenge_literatura.principal;

import com.aluracursos.challenge_literatura.model.*;
import com.aluracursos.challenge_literatura.repository.AutorRepository;
import com.aluracursos.challenge_literatura.repository.LibroRepository;
import com.aluracursos.challenge_literatura.service.ConsumoApi;
import com.aluracursos.challenge_literatura.service.ConvierteDatos;

import java.util.*;
import java.util.stream.Collectors;

public class Principal {
    private static final String URL_BASE = "https://gutendex.com/books/?search=";
    private ConsumoApi consumoApi = new ConsumoApi();
    private ConvierteDatos conversor = new ConvierteDatos();
    private Scanner teclado = new Scanner(System.in);
    private AutorRepository autorRepository;
    private LibroRepository libroRepository;
    private Optional <DatosLibro> libroBuscado;
    private List<Autor> autor;
    private List<Libro> libro;
    List<Libro> libros;
    List<Autor> autores;
    Autor autorApi;

    public Principal(AutorRepository autorRepository, LibroRepository libroRepository) {
        this.autorRepository = autorRepository;
        this.libroRepository = libroRepository;
    }

    public void muestraElMenu() {
        var opcion = -1;
        while (opcion != 0) {
            try{
                var menu = """
                    >>>>>>>>>>>>LITERATURA ONE<<<<<<<<<<<<
                    1 - Buscar libro por titulo
                    2 - Buscar Autor por nombre
                    3 - Listar libros registrados
                    4 - listar autores registrados
                    5 - listar autores vivos en un determinado año
                    6 - listar libros por idioma
                    7 - Top 10 Libros mas Descargados
                    8 - Estadisticas
                    
                    0 - Salir
                    >>>>>>>>>>>>>>>>>>><<<<<<<<<<<<<<<<<<<
                    Selecciona la opcion deseada....
                    """;
                System.out.println(menu);
                opcion = teclado.nextInt();
                teclado.nextLine();
            }catch (InputMismatchException e){
                opcion = 9;
                teclado.nextLine();
            }


            switch (opcion) {
                case 1:
                    buscarLibroPorTitulo();
                    break;
                case 2:
                    buscarLibroPorAutor();
                    break;
                case 3:
                    listarLibrosRegistrados();
                    break;
                case 4:
                    listarAutoresRegistrados();
                    break;
                case 5:
                    listarAutoresVivosPorAnos();
                    break;
                case 6:
                    listarLibrosPorIdioma();
                    break;
                case 7:
                    break;
                case 0:
                    System.out.println("Cerrando la aplicación...");
                    break;
                default:
                    System.out.println("Opción del menu inválida");
            }

        }
    }

    private void buscarLibroPorTitulo() {

            System.out.println("Escribe el nombre del libro que deseas buscar");
            var nombreLibro = teclado.nextLine();
            var json = consumoApi.obtenerDatos(URL_BASE + nombreLibro.replace(" ", "+"));
            var datos = conversor.obtenerDatos(json, Datos.class);
            libroBuscado = datos.libros().stream()
                    .findFirst();

            if (libroBuscado.isPresent()) {
                var mensaje = "<<<<<<<<<<<<<<<<<LIBRO ENCONTRADO>>>>>>>>>>>>>>>>>\n" +
                        "Titulo: " + libroBuscado.get().titulo() +
                        "\n Autor: " + libroBuscado.get().autor() +
                        "\n <<<<<<<<<<<<<<<<<<<<<<<< >>>>>>>>>>>>>>>>>>>>>>>>";
                System.out.println(mensaje);

                List<Libro> libroEncontrado = libroBuscado.stream().map(Libro::new).collect(Collectors.toList());
                try{
                    autorApi = libroBuscado.stream().
                            flatMap(l -> l.autor().stream()
                                    .map(Autor::new))
                            .collect(Collectors.toList()).stream().findFirst().get();
                }catch (NoSuchElementException e){
                    DatosAutor datosAutor = new DatosAutor("Desconocido","0000", "0000");
                    autorApi = new Autor(datosAutor);
                }

                Optional<Autor> autorBuscado = autorRepository.comprobarAutorBD(libroBuscado.get().autor().stream()
                        .map(DatosAutor::nombre)
                        .collect(Collectors.joining()));
                Optional<Libro> libroBuscadoBD = libroRepository.comprobarLibroBD(String.valueOf(datos.libros().get(0).titulo()));
                if (libroBuscadoBD.isPresent()) {
                    System.out.println("""
                            <<<<<<<<<<<<<<<<<<<<>>>>>>>>>>>>>>>>>>>
                            Libro ya registrado en la base de datos
                            <<<<<<<<<<<<<<<<<<<<>>>>>>>>>>>>>>>>>>>
                            
                            """);
                }else {
                    Autor autor;
                    if (autorBuscado.isPresent()) {
                        autor = autorBuscado.get();
                        System.out.println("""
                                <<<<<<<<<<<<<<<<<<<<>>>>>>>>>>>>>>>>>>>
                                EL autor ya esta guardado en la BD
                                <<<<<<<<<<<<<<<<<<<<>>>>>>>>>>>>>>>>>>>
                                
                                """);
                    }else {
                        autor = autorApi;
                        autorRepository.save(autor);
                    }
                    autor.setLibro(libroEncontrado);
                    System.out.println();
                    autorRepository.save(autor);
                }
            } else {
                System.out.println("\n<<<<<<<<<LIBRO NO ENCONTRADO>>>>>>>>>");
                System.out.println("Intentelo de nuevo");
                System.out.println("<<<<<<<<<<<<<<<<<< >>>>>>>>>>>>>>>>>>\n");
            }

        }

    private void buscarLibroPorAutor(){
        System.out.println("""
                <<<<<<<<<<<<<<<<<< >>>>>>>>>>>>>>>>>>
                Ingresa el nombre del Autor""");
        String autorBuscado = teclado.nextLine();
        Optional<Autor> autor = autorRepository.buscaAutorBD(autorBuscado);
        if (autor.isPresent()){
            System.out.println("<<<<<<<<<<<<AUTOR ENCONTRADO>>>>>>>>>>>>");
            System.out.println("Autor: " + autor.get().getNombre());
            System.out.println("<<<<<<<<<<<<<<<<<<<<>>>>>>>>>>>>>>>>>>>>>\n");
        }else {
            System.out.println("Autor no encontrado");
        }
    }

    private void listarLibrosRegistrados() {
        libros = libroRepository.findAll();
        System.out.println("<<<<<<<<<<<<<LIBROS REGISTRADOS>>>>>>>>>>>>>");
        for (Libro libro : libros) {
            System.out.println("Título: " + libro.getTitulo());
        }
        System.out.println("\n<<<<<<<<<<<<<<<<<<<<< >>>>>>>>>>>>>>>>>>>>>\n");
    }

    private void listarAutoresRegistrados() {
        autores = autorRepository.findAll();
        System.out.println("************AUTORES REGISTRADOS************");
        for (Autor autor : autores) {
            System.out.println("Nombre: " + autor.getNombre());
        }
        System.out.println("\n ****************************************\n");
    }

    private void listarAutoresVivosPorAnos(){
        System.out.println("Favor ingrese el año que desea buscar");
        var busqueda = teclado.nextLine();
        autores = autorRepository.autorVivoEnDeterminadoAnio(busqueda);

        if (autores != null){
            System.out.println("************AUTORES VIVOS************");
            for (Autor autor : autores) {
                System.out.println("Nombre: " + autor.getNombre());
            }
            System.out.println("\n ***************************************\n");
        }else {
            System.out.println("no se encuentra ningun Autor Vivo de Ese año");
        }
    }

    private void listarLibrosPorIdioma(){
        System.out.println("""
                
                """);
    }

}
