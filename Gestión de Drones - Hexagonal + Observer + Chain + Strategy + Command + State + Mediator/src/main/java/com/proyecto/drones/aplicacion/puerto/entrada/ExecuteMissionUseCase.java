package com.proyecto.drones.aplicacion.puerto.entrada;

/** Puerto de entrada para ejecutar una operación de vigilancia coordinada. */
public interface ExecuteMissionUseCase {
    /**
     * Coordina los subsistemas necesarios para iniciar una misión de vigilancia.
     *
     * @return bitácora de coordinación y resultado final
     */
    String ejecutarMisionVigilancia();
}
