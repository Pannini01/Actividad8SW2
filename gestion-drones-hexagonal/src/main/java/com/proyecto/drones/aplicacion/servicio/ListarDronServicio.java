package com.proyecto.drones.aplicacion.servicio;

import java.util.Objects;
import java.util.Optional;

import com.proyecto.drones.aplicacion.puerto.entrada.ListarDronUseCase;
import com.proyecto.drones.aplicacion.puerto.salida.DronRepository;
import com.proyecto.drones.dominio.modelo.Dron;

/** Servicio de aplicación que implementa el caso de uso de listar un dron. */
public final class ListarDronServicio implements ListarDronUseCase {
    private final DronRepository repository;

    /**
     * Crea el servicio con el puerto de persistencia requerido.
     *
     * @param repository puerto de salida de persistencia
     */
    public ListarDronServicio(DronRepository repository) {
        this.repository = Objects.requireNonNull(repository, "El repositorio es obligatorio.");
    }

    @Override
    public Optional<Dron> listarUno(String id) {
        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException("Ingrese el id del dron.");
        }
        return repository.listarUno(id.trim());
    }
}
