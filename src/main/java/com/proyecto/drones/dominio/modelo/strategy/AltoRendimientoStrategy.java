package com.proyecto.drones.dominio.modelo.strategy;

/** Modo alto rendimiento: distancia en kilómetros multiplicada por 1.5. */
public class AltoRendimientoStrategy implements ConsumoStrategy {
    @Override
    public double calcularConsumo(double distanciaKm) {
        return distanciaKm * 1.5d;
    }
}
