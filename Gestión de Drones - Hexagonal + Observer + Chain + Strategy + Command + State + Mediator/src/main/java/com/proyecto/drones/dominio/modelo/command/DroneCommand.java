package com.proyecto.drones.dominio.modelo.command;

/**
 * Command base para una tarea de misión.
 *
 * <p>El comando funciona únicamente como wrapper: no recibe ni conoce un
 * {@code Dron}. Cada implementación delega en el Receiver configurado.</p>
 */
public interface DroneCommand {
    /** Ejecuta la operación encapsulada. */
    String ejecutar();
}
