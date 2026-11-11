package com.proyecto.drones.dominio.modelo.command;

import com.proyecto.drones.aplicacion.servicio.DroneOperacionService;

/** Wrapper Command para la operación de escanear una zona. */
public class EscanearZonaCommand implements DroneCommand {
    private final DroneOperacionService receiver;

    public EscanearZonaCommand(DroneOperacionService receiver) {
        this.receiver = receiver;
    }

    @Override
    public String ejecutar() {
        return receiver.escanearZona();
    }
}
