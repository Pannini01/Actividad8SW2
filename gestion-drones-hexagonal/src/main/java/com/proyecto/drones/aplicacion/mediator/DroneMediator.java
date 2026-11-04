package com.proyecto.drones.aplicacion.mediator;

/** Contrato Mediator para coordinar una operación de vigilancia. */
public interface DroneMediator {
    /**
     * Coordina la preparación de la misión sin comunicación directa entre componentes.
     *
     * @return bitácora con el resultado de la coordinación
     */
    String iniciarOperacionVigilancia();
}
