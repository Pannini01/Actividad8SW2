package com.proyecto.drones.aplicacion.puerto.entrada.weather;

import com.proyecto.drones.aplicacion.dto.weather.WeatherResult;

/** Puerto de entrada para consultar y validar el clima de una ciudad. */
public interface GetWeatherByCityUseCase {

    /**
     * Consulta el clima actual, ejecuta la cadena y notifica a los drones
     * suscritos.
     *
     * @param ciudad ciudad permitida por la aplicación
     * @return clima y resultado de validación
     */
    WeatherResult consultar(String ciudad);
}
