package com.proyecto.drones.aplicacion.servicio;

import java.util.Objects;

import com.proyecto.drones.aplicacion.puerto.entrada.CrearDronUseCase;
import com.proyecto.drones.aplicacion.puerto.salida.DronRepository;
import com.proyecto.drones.dominio.modelo.Dron;

/**
 * Servicio de aplicación que implementa el caso de uso de creación.
 *
 * <p>Valida las reglas necesarias mediante el validador compartido y delega
 * la persistencia al puerto de salida {@link DronRepository}.</p>
 */
public final class CrearDronServicio implements CrearDronUseCase {
    private final DronRepository repository;

    /**
     * Crea el servicio con el puerto de persistencia requerido.
     *
     * @param repository puerto de salida de persistencia
     */
    public CrearDronServicio(DronRepository repository) {
        this.repository = Objects.requireNonNull(repository, "El repositorio es obligatorio.");
    }

    @Override
    public Dron crear(Dron dron) {
        DronValidator.validar(dron);
        return repository.crear(dron);
    }
}
