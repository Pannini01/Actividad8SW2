package com.proyecto.drones.dominio.modelo.strategy;

/** Modo ahorro: distancia en kilómetros multiplicada por 0.5. */
public class AhorroStrategy implements ConsumoStrategy {
    @Override
    public double calcularConsumo(double distanciaKm) {
        return distanciaKm * 0.5d;
    }
}
