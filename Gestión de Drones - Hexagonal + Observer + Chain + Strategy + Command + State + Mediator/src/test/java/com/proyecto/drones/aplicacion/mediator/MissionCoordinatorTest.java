package com.proyecto.drones.aplicacion.mediator;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import com.proyecto.drones.aplicacion.mediator.components.BatterySystem;
import com.proyecto.drones.aplicacion.mediator.components.CommunicationSystem;
import com.proyecto.drones.aplicacion.mediator.components.NavigationSystem;
import com.proyecto.drones.aplicacion.mediator.components.SensorSystem;

/** Pruebas unitarias del Concrete Mediator. */
@Tag("unit")
class MissionCoordinatorTest {

    @Test
    void coordinaTodosLosComponentesCuandoLaBateriaEstaDisponible() {
        DroneMediator mediator = new MissionCoordinator(
                new NavigationSystem(), new SensorSystem(), new BatterySystem(true), new CommunicationSystem());

        String resultado = mediator.iniciarOperacionVigilancia();

        assertTrue(resultado.contains("disponibilidad energética verificada"));
        assertTrue(resultado.contains("ruta de vigilancia preparada"));
        assertTrue(resultado.contains("sensores de vigilancia activados"));
        assertTrue(resultado.contains("enlace de misión establecido"));
        assertTrue(resultado.contains("MISIÓN AUTORIZADA"));
    }

    @Test
    void detieneLaCoordinacionCuandoNoHayBateria() {
        DroneMediator mediator = new MissionCoordinator(
                new NavigationSystem(), new SensorSystem(), new BatterySystem(false), new CommunicationSystem());

        String resultado = mediator.iniciarOperacionVigilancia();

        assertTrue(resultado.contains("MISIÓN NO AUTORIZADA"));
        assertFalse(resultado.contains("ruta de vigilancia preparada"));
        assertFalse(resultado.contains("sensores de vigilancia activados"));
        assertFalse(resultado.contains("enlace de misión establecido"));
    }
}
