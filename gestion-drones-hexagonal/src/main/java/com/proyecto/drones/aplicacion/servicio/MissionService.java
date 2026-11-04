package com.proyecto.drones.aplicacion.servicio;

import java.util.Objects;

import com.proyecto.drones.aplicacion.mediator.DroneMediator;
import com.proyecto.drones.aplicacion.puerto.entrada.ExecuteMissionUseCase;

/** Servicio de aplicación que expone la coordinación Mediator como caso de uso. */
public class MissionService implements ExecuteMissionUseCase {
    private final DroneMediator mediator;

    /** @param mediator mediador encargado de coordinar los componentes */
    public MissionService(DroneMediator mediator) {
        this.mediator = Objects.requireNonNull(mediator);
    }

    @Override
    public String ejecutarMisionVigilancia() {
        return mediator.iniciarOperacionVigilancia();
    }
}
