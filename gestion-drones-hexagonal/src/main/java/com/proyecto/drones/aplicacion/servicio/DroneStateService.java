package com.proyecto.drones.aplicacion.servicio;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import com.proyecto.drones.aplicacion.puerto.entrada.ManageDroneStateUseCase;
import com.proyecto.drones.dominio.modelo.Dron;
import com.proyecto.drones.dominio.modelo.state.DroneState;
import com.proyecto.drones.dominio.modelo.state.OffState;

/**
 * Servicio de aplicación que conserva el State wrapper actual de cada dron.
 *
 * <p>La entidad {@link Dron} no se modifica. Cuando un dron participa por
 * primera vez en esta funcionalidad se crea un {@link OffState} que envuelve
 * directamente al dron, cumpliendo la regla de inicio en APAGADO.</p>
 */
public class DroneStateService implements ManageDroneStateUseCase {

    private final Map<String, DroneState> estados = new HashMap<>();

    @Override
    public String obtenerEstado(Dron dron) {
        return estadoActual(dron).getNombre();
    }

    @Override
    public String encender(Dron dron) {
        return ejecutar(dron, DroneState::encender);
    }

    @Override
    public String apagar(Dron dron) {
        return ejecutar(dron, DroneState::apagar);
    }

    @Override
    public String despegar(Dron dron) {
        return ejecutar(dron, DroneState::despegar);
    }

    @Override
    public String iniciarAterrizaje(Dron dron) {
        return ejecutar(dron, DroneState::iniciarAterrizaje);
    }

    @Override
    public String finalizarAterrizaje(Dron dron) {
        return ejecutar(dron, DroneState::finalizarAterrizaje);
    }

    private String ejecutar(Dron dron, Function<DroneState, String> operacion) {
        DroneState actual = estadoActual(dron);
        String resultado = operacion.apply(actual);
        estados.put(clave(dron), actual.getEstadoResultante());
        return resultado;
    }

    private DroneState estadoActual(Dron dron) {
        String id = clave(dron);
        return estados.computeIfAbsent(id, clave -> new OffState(dron));
    }

    private String clave(Dron dron) {
        if (dron == null) {
            throw new IllegalArgumentException("Seleccione un dron antes de administrar su estado.");
        }
        String id = dron.getId();
        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException("El dron debe tener un id para administrar su estado.");
        }
        return id;
    }
}
