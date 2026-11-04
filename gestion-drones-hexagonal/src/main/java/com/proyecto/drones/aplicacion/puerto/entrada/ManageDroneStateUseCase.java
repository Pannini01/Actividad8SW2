package com.proyecto.drones.aplicacion.puerto.entrada;

import com.proyecto.drones.dominio.modelo.Dron;

/** Puerto de entrada para administrar el ciclo operativo de un dron. */
public interface ManageDroneStateUseCase {
    /** Obtiene el estado actual; un dron no registrado inicia APAGADO. */
    String obtenerEstado(Dron dron);
    /** Solicita encender el dron. */
    String encender(Dron dron);
    /** Solicita apagar el dron. */
    String apagar(Dron dron);
    /** Solicita despegar. */
    String despegar(Dron dron);
    /** Solicita iniciar el aterrizaje. */
    String iniciarAterrizaje(Dron dron);
    /** Solicita finalizar el aterrizaje. */
    String finalizarAterrizaje(Dron dron);
}
