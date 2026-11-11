package com.proyecto.drones.aplicacion.puerto.entrada;

import com.proyecto.drones.dominio.modelo.Dron;

/**
 * Puerto de entrada para el caso de uso de creación de drones.
 *
 * <p>El adaptador de entrada JavaFX invoca este contrato sin conocer
 * la implementación del servicio ni la tecnología de persistencia.</p>
 */
public interface CrearDronUseCase {
    /**
     * Registra un nuevo dron.
     *
     * @param dron entidad que se desea registrar
     * @return dron persistido
     */
    Dron crear(Dron dron);
}
