package com.proyecto.drones.aplicacion.dto.weather;

import com.proyecto.drones.dominio.modelo.weather.Weather;

/** Resultado que la capa de aplicación entrega al adaptador JavaFX. */
public record WeatherResult(String ciudad, Weather weather, String resultadoValidacion) {
}
