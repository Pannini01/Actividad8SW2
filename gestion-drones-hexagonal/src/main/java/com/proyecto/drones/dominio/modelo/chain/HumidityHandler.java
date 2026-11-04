package com.proyecto.drones.dominio.modelo.chain;

import com.proyecto.drones.dominio.modelo.weather.Weather;

/** Valida que la humedad no supere el 85 %. */
public class HumidityHandler extends WeatherHandler {

    @Override
    public String handle(Weather weather) {
        if (weather.getHumedad() > 85) {
            return "CLIMA NO APTO PARA OPERACIÓN DE DRONES. Motivo: "
                    + "humedad superior al 85 % permitido.";
        }
        return handleNext(weather);
    }
}
