package com.proyecto.drones.dominio.modelo.strategy;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class StrategyConsumoTest {
    @Test
    void aplicaLosTresFactoresSolicitados() {
        double distancia = 20.0;
        assertEquals(10.0, new AhorroStrategy().calcularConsumo(distancia), 0.000001);
        assertEquals(20.0, new NormalStrategy().calcularConsumo(distancia), 0.000001);
        assertEquals(30.0, new AltoRendimientoStrategy().calcularConsumo(distancia), 0.000001);
    }
}
