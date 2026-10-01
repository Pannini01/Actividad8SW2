package com.proyecto.drones.aplicacion.servicio;

import java.util.Objects;

import com.proyecto.drones.aplicacion.puerto.entrada.EliminarDronUseCase;
import com.proyecto.drones.aplicacion.puerto.salida.DronRepository;

/** Servicio de aplicación que implementa el caso de uso de eliminación. */
public final class EliminarDronServicio implements EliminarDronUseCase {
    private final DronRepository repository;

    /**
     * Crea el servicio con el puerto de persistencia requerido.
     *
     * @param repository puerto de salida de persistencia
     */
    public EliminarDronServicio(DronRepository repository) {
        this.repository = Objects.requireNonNull(repository, "El repositorio es obligatorio.");
    }

    @Override
    public boolean eliminar(String id) {
        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException("Ingrese el id del dron.");
        }
        return repository.eliminar(id.trim());
    }
}
