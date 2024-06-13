package com.aluracursos.challenge_literatura.principal;

import com.aluracursos.challenge_literatura.service.ConsumoApi;
import com.aluracursos.challenge_literatura.service.ConvierteDatos;

import java.util.Scanner;

public class Principal {
    private static final String URL_BASE = "https://gutendex.com/books/";
    private ConsumoApi consumoAPI = new ConsumoApi();
    private ConvierteDatos conversor = new ConvierteDatos();
    private Scanner teclado = new Scanner(System.in);

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
                    System.out.println("En Mantenimiento");
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
                    System.out.println("Opción inválida");
            }

        }
    }
}
