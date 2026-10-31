package com.proyecto.drones.dominio.modelo.command;

import com.proyecto.drones.aplicacion.servicio.DroneOperacionService;

/** Wrapper Command para la operación de medir temperatura. */
public class MedirTemperaturaCommand implements DroneCommand {
    private final DroneOperacionService receiver;

    public MedirTemperaturaCommand(DroneOperacionService receiver) {
        this.receiver = receiver;
    }

    @Override
    public String ejecutar() {
        return receiver.medirTemperatura();
    }
}
