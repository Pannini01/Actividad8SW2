package com.proyecto.drones.dominio.modelo.state;

import java.util.Objects;

import com.proyecto.drones.dominio.modelo.Dron;

/**
 * Wrapper abstracto del patrón GoF State para el estado operativo de un dron.
 *
 * <p>La clase envuelve directamente una instancia de {@link Dron}, evitando
 * modificar la entidad original y evitando crear una clase adicional de
 * contexto. Cada estado concreto implementa sus propias reglas y decide si la
 * operación mantiene el estado actual o produce una transición válida.</p>
 */
public abstract class DroneState {

    /** Dron envuelto por el estado actual. */
    protected final Dron dron;

    private DroneState estadoResultante;

    /**
     * Crea un estado que envuelve al dron indicado.
     *
     * @param dron dron asociado al estado
     */
    protected DroneState(Dron dron) {
        this.dron = Objects.requireNonNull(dron, "El dron no puede ser nulo.");
        this.estadoResultante = this;
    }

    /** @return dron envuelto por este estado */
    public final Dron getDron() {
        return dron;
    }

    /**
     * Estado resultante de la última operación ejecutada.
     *
     * <p>Este método permite que la capa de aplicación conserve la transición
     * sin utilizar estructuras condicionales extensas.</p>
     *
     * @return estado que debe quedar activo
     */
    public final DroneState getEstadoResultante() {
        return estadoResultante;
    }

    /**
     * Registra una transición válida y devuelve el mensaje de la operación.
     *
     * @param nuevoEstado estado operativo siguiente
     * @param mensaje resultado visible de la operación
     * @return mensaje recibido
     */
    protected final String transicionarA(DroneState nuevoEstado, String mensaje) {
        this.estadoResultante = Objects.requireNonNull(nuevoEstado, "El nuevo estado no puede ser nulo.");
        return mensaje;
    }

    /**
     * Mantiene el estado actual cuando una operación no está permitida.
     *
     * @param mensaje motivo del rechazo
     * @return mensaje recibido
     */
    protected final String permanecer(String mensaje) {
        this.estadoResultante = this;
        return mensaje;
    }

    /** Intenta encender el dron. */
    public abstract String encender();

    /** Intenta apagar el dron. */
    public abstract String apagar();

    /** Intenta iniciar el vuelo. */
    public abstract String despegar();

    /** Intenta iniciar el proceso de aterrizaje. */
    public abstract String iniciarAterrizaje();

    /** Intenta finalizar el proceso de aterrizaje. */
    public abstract String finalizarAterrizaje();

    /**
     * Nombre visible del estado.
     *
     * @return APAGADO, EN ESPERA, VOLANDO o ATERRIZANDO
     */
    public abstract String getNombre();
}
