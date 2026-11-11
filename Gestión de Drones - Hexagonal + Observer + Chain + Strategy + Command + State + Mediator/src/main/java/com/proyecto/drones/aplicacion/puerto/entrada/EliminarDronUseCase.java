package com.proyecto.drones.aplicacion.puerto.entrada;

/** Puerto de entrada para el caso de uso de eliminación de un dron. */
public interface EliminarDronUseCase {
    /**
     * Elimina un dron por su identificador.
     *
     * @param id identificador del dron
     * @return {@code true} si el dron existía y fue eliminado
     */
    boolean eliminar(String id);
}
