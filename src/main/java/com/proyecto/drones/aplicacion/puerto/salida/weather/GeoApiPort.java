package com.proyecto.drones.aplicacion.puerto.salida.weather;

import com.proyecto.drones.aplicacion.dto.weather.Coordinates;

/** Puerto de salida para resolver una ciudad a coordenadas. */
public interface GeoApiPort {

    Coordinates buscarCiudad(String ciudad);
}
