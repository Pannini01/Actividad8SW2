package com.proyecto.drones.dominio.modelo.chain;

import com.proyecto.drones.dominio.modelo.weather.Weather;

/** Valida una presión atmosférica entre 950 y 1050 hPa, inclusive. */
public class PressureHandler extends WeatherHandler {

    @Override
    public String handle(Weather weather) {
        int valor = weather.getPresionAtmosferica();
        if (valor < 950 || valor > 1050) {
            return "CLIMA NO APTO PARA OPERACIÓN DE DRONES. Motivo: "
                    + "presión atmosférica fuera del rango permitido de 950 a 1050 hPa.";
        }
        return handleNext(weather);
    }
}
