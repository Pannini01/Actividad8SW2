package com.proyecto.drones.aplicacion.puerto.salida.weather;

import com.proyecto.drones.dominio.modelo.weather.Weather;

/** Puerto de salida para recuperar la lectura climática actual. */
public interface WeatherApiPort {

    Weather obtenerClimaActual(double latitude, double longitude);
}
