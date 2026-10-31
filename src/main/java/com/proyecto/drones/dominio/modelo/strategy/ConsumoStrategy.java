package com.proyecto.drones.dominio.modelo.strategy;

/** Estrategia para estimar el consumo de batería según una distancia. */
public interface ConsumoStrategy {
    /**
     * Calcula el consumo estimado para una distancia dada.
     *
     * @param distanciaKm distancia de la ruta en kilómetros
     * @return consumo estimado de batería
     */
    double calcularConsumo(double distanciaKm);
}
