package com.proyecto.drones.dominio.modelo.state;

import com.proyecto.drones.dominio.modelo.Dron;

/** Estado VOLANDO: únicamente permite iniciar el aterrizaje. */
public class FlyingState extends DroneState {

    /**
     * Crea el estado VOLANDO para el dron indicado.
     *
     * @param dron dron envuelto
     */
    public FlyingState(Dron dron) {
        super(dron);
    }

    @Override
    public String encender() {
        return permanecer("Operación no permitida: el dron ya está VOLANDO.");
    }

    @Override
    public String apagar() {
        return permanecer("Operación no permitida: un dron VOLANDO no puede apagarse.");
    }

    @Override
    public String despegar() {
        return permanecer("Operación no permitida: el dron ya se encuentra VOLANDO.");
    }

    @Override
    public String iniciarAterrizaje() {
        return transicionarA(
                new LandingState(dron),
                "Proceso de aterrizaje iniciado. Estado actual: ATERRIZANDO.");
    }

    @Override
    public String finalizarAterrizaje() {
        return permanecer("Operación no permitida: primero debe iniciar el aterrizaje.");
    }

    @Override
    public String getNombre() {
        return "VOLANDO";
    }
}
