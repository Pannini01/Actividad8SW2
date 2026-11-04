package com.proyecto.drones.dominio.modelo.state;

import com.proyecto.drones.dominio.modelo.Dron;

/** Estado EN ESPERA: permite despegar o apagar el dron. */
public class ReadyState extends DroneState {

    /**
     * Crea el estado EN ESPERA para el dron indicado.
     *
     * @param dron dron envuelto
     */
    public ReadyState(Dron dron) {
        super(dron);
    }

    @Override
    public String encender() {
        return permanecer("Operación no permitida: el dron ya está encendido y EN ESPERA.");
    }

    @Override
    public String apagar() {
        return transicionarA(
                new OffState(dron),
                "Dron apagado. Estado actual: APAGADO.");
    }

    @Override
    public String despegar() {
        return transicionarA(
                new FlyingState(dron),
                "Despegue iniciado correctamente. Estado actual: VOLANDO.");
    }

    @Override
    public String iniciarAterrizaje() {
        return permanecer("Operación no permitida: un dron EN ESPERA no está volando.");
    }

    @Override
    public String finalizarAterrizaje() {
        return permanecer("Operación no permitida: el dron EN ESPERA no está aterrizando.");
    }

    @Override
    public String getNombre() {
        return "EN ESPERA";
    }
}
