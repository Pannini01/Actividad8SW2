package com.proyecto.drones.dominio.modelo.state;

import com.proyecto.drones.dominio.modelo.Dron;

/** Estado APAGADO: únicamente permite encender el dron. */
public class OffState extends DroneState {

    /**
     * Crea el estado APAGADO para el dron indicado.
     *
     * @param dron dron envuelto
     */
    public OffState(Dron dron) {
        super(dron);
    }

    @Override
    public String encender() {
        return transicionarA(
                new ReadyState(dron),
                "Dron encendido. Estado actual: EN ESPERA.");
    }

    @Override
    public String apagar() {
        return permanecer("Operación no permitida: el dron ya está APAGADO.");
    }

    @Override
    public String despegar() {
        return permanecer("Operación no permitida: un dron APAGADO no puede despegar.");
    }

    @Override
    public String iniciarAterrizaje() {
        return permanecer("Operación no permitida: un dron APAGADO no puede iniciar aterrizaje.");
    }

    @Override
    public String finalizarAterrizaje() {
        return permanecer("Operación no permitida: el dron APAGADO no está aterrizando.");
    }

    @Override
    public String getNombre() {
        return "APAGADO";
    }
}
