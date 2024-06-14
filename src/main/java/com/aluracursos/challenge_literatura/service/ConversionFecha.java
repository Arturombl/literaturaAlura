package com.aluracursos.challenge_literatura.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ConversionFecha {
    public static Date convertirStringADate(String anoString) {
        SimpleDateFormat formato = new SimpleDateFormat("yyyy");
        Date fecha = null;
        try {
            fecha = formato.parse(anoString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return fecha;
    }
}
