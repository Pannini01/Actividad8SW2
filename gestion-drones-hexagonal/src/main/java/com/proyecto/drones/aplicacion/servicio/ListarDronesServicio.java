package com.proyecto.drones.aplicacion.servicio;

import java.util.List;
import java.util.Objects;

import com.proyecto.drones.aplicacion.puerto.entrada.ListarDronesUseCase;
import com.proyecto.drones.aplicacion.puerto.salida.DronRepository;
import com.proyecto.drones.dominio.modelo.Dron;

/** Servicio de aplicación que implementa el caso de uso de listar varios drones. */
public final class ListarDronesServicio implements ListarDronesUseCase {
    private final DronRepository repository;

    /**
     * Crea el servicio con el puerto de persistencia requerido.
     *
     * @param repository puerto de salida de persistencia
     */
    public ListarDronesServicio(DronRepository repository) {
        this.repository = Objects.requireNonNull(repository, "El repositorio es obligatorio.");
    }

    @Override
    public List<Dron> listarMuchos() {
        return repository.listarMuchos();
    }
}
