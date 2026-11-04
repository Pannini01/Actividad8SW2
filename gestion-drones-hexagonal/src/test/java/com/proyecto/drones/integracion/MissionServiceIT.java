package com.proyecto.drones.integracion;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import com.proyecto.drones.aplicacion.mediator.DroneMediator;
import com.proyecto.drones.aplicacion.mediator.MissionCoordinator;
import com.proyecto.drones.aplicacion.mediator.components.BatterySystem;
import com.proyecto.drones.aplicacion.mediator.components.CommunicationSystem;
import com.proyecto.drones.aplicacion.mediator.components.NavigationSystem;
import com.proyecto.drones.aplicacion.mediator.components.SensorSystem;
import com.proyecto.drones.aplicacion.puerto.entrada.ExecuteMissionUseCase;
import com.proyecto.drones.aplicacion.servicio.MissionService;

/** Integración entre puerto de entrada, servicio, Mediator y componentes reales. */
@Tag("integration")
class MissionServiceIT {

    @Test
    void integraCasoDeUsoServicioMediatorYComponentesSinCrud() {
        DroneMediator mediator = new MissionCoordinator(
                new NavigationSystem(), new SensorSystem(), new BatterySystem(), new CommunicationSystem());
        ExecuteMissionUseCase useCase = new MissionService(mediator);

        String resultado = useCase.ejecutarMisionVigilancia();

        assertTrue(resultado.contains("Navegación:"));
        assertTrue(resultado.contains("Sensores:"));
        assertTrue(resultado.contains("Batería:"));
        assertTrue(resultado.contains("Comunicación:"));
        assertTrue(resultado.contains("MISIÓN AUTORIZADA"));
    }
}
