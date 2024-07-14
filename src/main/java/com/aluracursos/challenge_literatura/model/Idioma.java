package com.aluracursos.challenge_literatura.model;

public enum Idioma {
    ES("es", "Español"),
    FR("fr" , "Frances"),
    EN("en", "Ingles"),
    PT("pt", "Protuges"),
    JP("jp", "Japones");

    private String idioma;
    private String nombreIdiomaCompleto;

    Idioma(String idioma, String nombreIdiomaCompleto) {
        this.idioma = idioma;
        this.nombreIdiomaCompleto = nombreIdiomaCompleto;
    }

    public String getIdioma(){
        return this.idioma;
    }

    public static Idioma fromString(String text) {
        for (Idioma idioma : Idioma.values()) {
            if (idioma.idioma.equalsIgnoreCase(text)) {
                return idioma;
            }
        }
        throw new IllegalArgumentException("No constant with text " + text + " found");
    }

    public static Idioma fromCompleto(String text) {
        for (Idioma idioma : Idioma.values()) {
            if (idioma.nombreIdiomaCompleto.equalsIgnoreCase(text)) {
                return idioma;
            }
        }
        throw new IllegalArgumentException("No constant with text " + text + " found");
    }
}
