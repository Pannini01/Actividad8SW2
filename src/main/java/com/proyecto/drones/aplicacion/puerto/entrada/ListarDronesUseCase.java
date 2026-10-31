package com.proyecto.drones.aplicacion.puerto.entrada;

import java.util.List;

import com.proyecto.drones.dominio.modelo.Dron;

/** Puerto de entrada para el caso de uso de listar varios drones. */
public interface ListarDronesUseCase {
    /**
     * Recupera todos los drones disponibles.
     *
     * @return lista de drones, posiblemente vacía
     */
    List<Dron> listarMuchos();
}
