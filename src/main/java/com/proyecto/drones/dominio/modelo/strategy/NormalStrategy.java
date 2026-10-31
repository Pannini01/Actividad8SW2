package com.proyecto.drones.dominio.modelo.strategy;

/** Modo normal: distancia en kilómetros multiplicada por 1.0. */
public class NormalStrategy implements ConsumoStrategy {
    @Override
    public double calcularConsumo(double distanciaKm) {
        return distanciaKm;
    }
}
