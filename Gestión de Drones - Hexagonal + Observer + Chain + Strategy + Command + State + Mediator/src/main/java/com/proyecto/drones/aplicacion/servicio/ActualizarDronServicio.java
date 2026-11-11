package com.proyecto.drones.aplicacion.servicio;

import java.util.Objects;

import com.proyecto.drones.aplicacion.puerto.entrada.ActualizarDronUseCase;
import com.proyecto.drones.aplicacion.puerto.salida.DronRepository;
import com.proyecto.drones.dominio.modelo.Dron;

/** Servicio de aplicación que implementa el caso de uso de actualización. */
public final class ActualizarDronServicio implements ActualizarDronUseCase {
    private final DronRepository repository;

    /**
     * Crea el servicio con el puerto de persistencia requerido.
     *
     * @param repository puerto de salida de persistencia
     */
    public ActualizarDronServicio(DronRepository repository) {
        this.repository = Objects.requireNonNull(repository, "El repositorio es obligatorio.");
    }

    @Override
    public boolean actualizar(Dron dron) {
        DronValidator.validar(dron);
        return repository.actualizar(dron);
    }
}
