package com.proyecto.drones.aplicacion.dto;

import com.proyecto.drones.dominio.modelo.strategy.PuntoGeografico;

/** Resultado de calcular distancia y consumo energético de una ruta. */
public record ConsumoResult(
        String dronId,
        String dronSerial,
        PuntoGeografico origen,
        PuntoGeografico destino,
        double distanciaKm,
        double consumoEstimado,
        String estrategia) {
}
