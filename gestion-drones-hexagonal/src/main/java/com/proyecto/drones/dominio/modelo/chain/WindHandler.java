package com.proyecto.drones.dominio.modelo.chain;

import com.proyecto.drones.dominio.modelo.weather.Weather;

/** Valida que el viento no supere 40 km/h. */
public class WindHandler extends WeatherHandler {

    @Override
    public String handle(Weather weather) {
        if (weather.getVelocidadViento() > 40) {
            return "CLIMA NO APTO PARA OPERACIÓN DE DRONES. Motivo: "
                    + "velocidad del viento superior a 40 km/h.";
        }
        return handleNext(weather);
    }
}
