package com.proyecto.drones.aplicacion.puerto.entrada;

import java.util.Optional;

import com.proyecto.drones.dominio.modelo.Dron;

/** Puerto de entrada para el caso de uso de listar un único dron. */
public interface ListarDronUseCase {
    /**
     * Recupera un dron por su identificador.
     *
     * @param id identificador del dron
     * @return dron encontrado o vacío cuando no existe
     */
    Optional<Dron> listarUno(String id);
}
