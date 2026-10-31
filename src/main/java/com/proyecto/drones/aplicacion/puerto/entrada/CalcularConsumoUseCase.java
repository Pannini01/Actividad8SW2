package com.proyecto.drones.aplicacion.puerto.entrada;

import java.nio.file.Path;

import com.proyecto.drones.aplicacion.dto.ConsumoResult;
import com.proyecto.drones.dominio.modelo.Dron;
import com.proyecto.drones.dominio.modelo.strategy.ConsumoStrategy;

/** Caso de uso Context del patrón Strategy para estimar consumo energético. */
public interface CalcularConsumoUseCase {
    ConsumoResult calcularConsumo(Dron dron, Path archivoRuta, ConsumoStrategy estrategia);
}
