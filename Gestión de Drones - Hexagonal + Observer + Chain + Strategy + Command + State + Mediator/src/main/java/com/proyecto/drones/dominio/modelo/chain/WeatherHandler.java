package com.proyecto.drones.dominio.modelo.chain;

import com.proyecto.drones.dominio.modelo.weather.Weather;

/**
 * Handler base del patrón Chain of Responsibility para validación climática.
 */
public abstract class WeatherHandler {

    protected WeatherHandler nextHandler;

    /**
     * Establece el siguiente elemento de la cadena.
     *
     * @param next siguiente handler
     */
    public void setNext(WeatherHandler next) {
        this.nextHandler = next;
    }

    /**
     * Evalúa la regla propia del handler.
     *
     * @param weather clima a validar
     * @return mensaje final de aptitud o rechazo
     */
    public abstract String handle(Weather weather);

    /**
     * Continúa la cadena o genera el mensaje de aprobación al llegar al final.
     *
     * @param weather clima a validar
     * @return resultado de la cadena
     */
    protected String handleNext(Weather weather) {
        if (nextHandler == null) {
            return "CLIMA APTO PARA OPERACIÓN DE DRONES";
        }
        return nextHandler.handle(weather);
    }
}
