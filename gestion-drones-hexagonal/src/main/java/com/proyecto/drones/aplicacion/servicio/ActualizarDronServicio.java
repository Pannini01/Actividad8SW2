package com.proyecto.drones.aplicacion.servicio;

import java.util.Objects;

import com.proyecto.drones.aplicacion.puerto.entrada.ActualizarDronUseCase;
import com.proyecto.drones.aplicacion.puerto.salida.DronRepository;
import com.proyecto.drones.dominio.modelo.Agricultura;
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
        validarDron(dron);
        return repository.actualizar(dron);
    }

    private void validarDron(Dron dron) {
        Objects.requireNonNull(dron, "El dron es obligatorio.");
        textoObligatorio(dron.getId(), "El id es obligatorio.");
        textoObligatorio(dron.getSerial(), "El serial es obligatorio.");
        textoObligatorio(dron.getModelo(), "El modelo es obligatorio.");
        textoObligatorio(dron.getFabricante(), "El fabricante es obligatorio.");
        if (dron.getPeso() <= 0) {
            throw new IllegalArgumentException("El peso debe ser mayor que cero.");
        }
        if (dron instanceof Agricultura agricultura && agricultura.getCapacidadTanque() <= 0) {
            throw new IllegalArgumentException("La capacidad del tanque debe ser mayor que cero.");
        }
    }

    private String textoObligatorio(String valor, String mensaje) {
        if (valor == null || valor.isBlank()) {
            throw new IllegalArgumentException(mensaje);
        }
        return valor.trim();
    }
}
