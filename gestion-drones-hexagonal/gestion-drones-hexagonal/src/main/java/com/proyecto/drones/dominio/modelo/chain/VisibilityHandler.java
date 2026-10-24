package com.proyecto.drones.dominio.modelo.chain;

import com.proyecto.drones.dominio.modelo.weather.Weather;

/** Valida que la visibilidad sea de al menos 5 km. */
public class VisibilityHandler extends WeatherHandler {

    @Override
    public String handle(Weather weather) {
        if (weather.getVisibilidad() < 5) {
            return "CLIMA NO APTO PARA OPERACIÓN DE DRONES. Motivo: "
                    + "visibilidad inferior a 5 km.";
        }
        return handleNext(weather);
    }
}
