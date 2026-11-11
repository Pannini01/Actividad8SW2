package com.proyecto.drones.integracion;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import com.proyecto.drones.aplicacion.puerto.entrada.ManageDroneStateUseCase;
import com.proyecto.drones.aplicacion.servicio.DroneStateService;
import com.proyecto.drones.dominio.modelo.Agricultura;
import com.proyecto.drones.dominio.modelo.Dron;

/** Integración del puerto State, servicio y wrappers concretos. */
@Tag("integration")
class DroneStateServiceIT {

    @Test
    void integraUseCaseServicioYWrappersManteniendoEstadosIndependientes() {
        ManageDroneStateUseCase useCase = new DroneStateService();
        Dron dron1 = new Agricultura("IT-S1", "SER-1", "M1", "FAB", 2.0, 8.0);
        Dron dron2 = new Agricultura("IT-S2", "SER-2", "M2", "FAB", 2.2, 9.0);

        assertEquals("APAGADO", useCase.obtenerEstado(dron1));
        assertEquals("APAGADO", useCase.obtenerEstado(dron2));

        useCase.encender(dron1);
        useCase.despegar(dron1);

        assertEquals("VOLANDO", useCase.obtenerEstado(dron1));
        assertEquals("APAGADO", useCase.obtenerEstado(dron2));
    }

    @Test
    void integraRechazoDeOperacionInvalidaSinAlterarEstado() {
        ManageDroneStateUseCase useCase = new DroneStateService();
        Dron dron = new Agricultura("IT-S3", "SER-3", "M3", "FAB", 2.1, 8.5);

        String resultado = useCase.despegar(dron);

        assertTrue(resultado.contains("no puede despegar"));
        assertEquals("APAGADO", useCase.obtenerEstado(dron));
    }
}
