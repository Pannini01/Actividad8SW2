package com.proyecto.drones.dominio.modelo.command;

import com.proyecto.drones.aplicacion.servicio.DroneOperacionService;

/** Wrapper Command para la operación de tomar una fotografía. */
public class TomarFotografiaCommand implements DroneCommand {
    private final DroneOperacionService receiver;

    public TomarFotografiaCommand(DroneOperacionService receiver) {
        this.receiver = receiver;
    }

    @Override
    public String ejecutar() {
        return receiver.tomarFotografia();
    }
}
