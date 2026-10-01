package com.proyecto.drones.aplicacion.puerto.entrada;

import com.proyecto.drones.dominio.modelo.Dron;

/** Puerto de entrada para el caso de uso de actualización de un dron. */
public interface ActualizarDronUseCase {
    /**
     * Actualiza la información de un dron existente.
     *
     * @param dron entidad con los datos actualizados
     * @return {@code true} si el registro existía y fue actualizado
     */
    boolean actualizar(Dron dron);
}
