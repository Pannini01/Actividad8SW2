package com.proyecto.drones.aplicacion.puerto.salida;

import com.proyecto.drones.dominio.modelo.strategy.PuntoGeografico;

/** Puerto de salida para calcular la distancia geográfica de una ruta. */
public interface DistanciaGeograficaPort {
    double calcularDistancia(PuntoGeografico origen, PuntoGeografico destino);
}
