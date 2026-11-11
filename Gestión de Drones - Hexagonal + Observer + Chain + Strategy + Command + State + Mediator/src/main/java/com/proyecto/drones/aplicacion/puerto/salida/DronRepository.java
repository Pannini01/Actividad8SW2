package com.proyecto.drones.aplicacion.puerto.salida;

import java.util.List;
import java.util.Optional;

import com.proyecto.drones.dominio.modelo.Dron;

/**
 * Puerto de salida de persistencia para la entidad {@link Dron}.
 *
 * <p>La capa de aplicación depende de esta abstracción. La infraestructura
 * la implementa mediante un Adapter PostgreSQL.</p>
 */
public interface DronRepository {
    /**
     * Persiste un nuevo dron.
     *
     * @param dron entidad nueva
     * @return entidad persistida
     */
    Dron crear(Dron dron);

    /**
     * Actualiza un dron existente.
     *
     * @param dron entidad actualizada
     * @return {@code true} si el registro fue actualizado
     */
    boolean actualizar(Dron dron);

    /**
     * Recupera un dron por su identificador.
     *
     * @param id identificador
     * @return dron encontrado o vacío
     */
    Optional<Dron> listarUno(String id);

    /**
     * Recupera todos los drones.
     *
     * @return drones persistidos
     */
    List<Dron> listarMuchos();

    /**
     * Elimina un dron por su identificador.
     *
     * @param id identificador
     * @return {@code true} si el registro fue eliminado
     */
    boolean eliminar(String id);
}
