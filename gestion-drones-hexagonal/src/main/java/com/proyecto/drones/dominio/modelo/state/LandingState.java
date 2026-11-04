package com.proyecto.drones.dominio.modelo.state;

import com.proyecto.drones.dominio.modelo.Dron;

/** Estado ATERRIZANDO: únicamente permite finalizar el aterrizaje. */
public class LandingState extends DroneState {

    /**
     * Crea el estado ATERRIZANDO para el dron indicado.
     *
     * @param dron dron envuelto
     */
    public LandingState(Dron dron) {
        super(dron);
    }

    @Override
    public String encender() {
        return permanecer("Operación no permitida: el dron está ATERRIZANDO.");
    }

    @Override
    public String apagar() {
        return permanecer("Operación no permitida: no se puede apagar mientras está ATERRIZANDO.");
    }

    @Override
    public String despegar() {
        return permanecer("Operación no permitida: no se puede iniciar un vuelo mientras está ATERRIZANDO.");
    }

    @Override
    public String iniciarAterrizaje() {
        return permanecer("Operación no permitida: el dron ya está ATERRIZANDO.");
    }

    @Override
    public String finalizarAterrizaje() {
        return transicionarA(
                new ReadyState(dron),
                "Aterrizaje finalizado. Estado actual: EN ESPERA.");
    }

    @Override
    public String getNombre() {
        return "ATERRIZANDO";
    }
}
