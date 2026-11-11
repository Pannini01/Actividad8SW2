package com.proyecto.drones.dominio.modelo.chain;

import com.proyecto.drones.dominio.modelo.weather.Weather;

/** Valida que la temperatura esté entre 0 °C y 40 °C, inclusive. */
public class TemperatureHandler extends WeatherHandler {

    @Override
    public String handle(Weather weather) {
        double valor = weather.getTemperatura();
        if (valor < 0 || valor > 40) {
            return "CLIMA NO APTO PARA OPERACIÓN DE DRONES. Motivo: "
                    + "temperatura fuera del rango permitido de 0 °C a 40 °C.";
        }
        return handleNext(weather);
    }
}
