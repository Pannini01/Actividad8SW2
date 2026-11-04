package com.proyecto.drones.dominio.modelo.state;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import com.proyecto.drones.dominio.modelo.Agricultura;
import com.proyecto.drones.dominio.modelo.Dron;

/** Pruebas unitarias del patrón State implementado como wrapper de Dron. */
@Tag("unit")
class DroneStateTest {

    @Test
    void dronNuevoIniciaApagadoYCompletaElCicloValido() {
        Dron dron = nuevoDron("D-STATE-1");
        DroneState estado = new OffState(dron);

        assertEquals("APAGADO", estado.getNombre());
        assertSame(dron, estado.getDron());

        estado.encender();
        estado = estado.getEstadoResultante();
        assertEquals("EN ESPERA", estado.getNombre());

        estado.despegar();
        estado = estado.getEstadoResultante();
        assertEquals("VOLANDO", estado.getNombre());

        estado.iniciarAterrizaje();
        estado = estado.getEstadoResultante();
        assertEquals("ATERRIZANDO", estado.getNombre());

        estado.finalizarAterrizaje();
        estado = estado.getEstadoResultante();
        assertEquals("EN ESPERA", estado.getNombre());

        estado.apagar();
        estado = estado.getEstadoResultante();
        assertEquals("APAGADO", estado.getNombre());

        assertSame(dron, estado.getDron());
    }

    @Test
    void rechazaTransicionNoPermitidaSinCambiarEstado() {
        DroneState estado = new OffState(nuevoDron("D-STATE-2"));

        String resultado = estado.despegar();
        DroneState resultante = estado.getEstadoResultante();

        assertSame(estado, resultante);
        assertEquals("APAGADO", resultante.getNombre());
        assertTrue(resultado.contains("no puede despegar"));
    }

    @Test
    void cadaEstadoConservaElMismoDronEnvueltoDuranteLasTransiciones() {
        Dron dron = nuevoDron("D-STATE-3");
        DroneState estado = new OffState(dron);

        estado.encender();
        DroneState ready = estado.getEstadoResultante();
        ready.despegar();
        DroneState flying = ready.getEstadoResultante();

        assertSame(dron, ready.getDron());
        assertSame(dron, flying.getDron());
    }

    private Dron nuevoDron(String id) {
        return new Agricultura(id, "SER-" + id, "Modelo", "Fabricante", 2.5, 10.0);
    }
}
