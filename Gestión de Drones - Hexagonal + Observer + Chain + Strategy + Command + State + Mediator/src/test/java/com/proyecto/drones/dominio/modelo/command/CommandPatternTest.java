package com.proyecto.drones.dominio.modelo.command;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import java.lang.reflect.Method;
import java.util.List;

import org.junit.jupiter.api.Test;

import com.proyecto.drones.aplicacion.dto.ResultadoTarea;
import com.proyecto.drones.aplicacion.servicio.DroneOperacionService;
import com.proyecto.drones.aplicacion.servicio.ProgramadorTareas;
import com.proyecto.drones.dominio.modelo.Agricultura;
import com.proyecto.drones.dominio.modelo.Dron;

class CommandPatternTest {
    @Test
    void commandNoRecibeDronYProgramadorRespetaOrden() throws Exception {
        Method ejecutar = DroneCommand.class.getMethod("ejecutar");
        assertEquals(0, ejecutar.getParameterCount());
        for (Method method : DroneCommand.class.getDeclaredMethods()) {
            assertFalse(List.of(method.getParameterTypes()).contains(Dron.class));
        }

        Dron dron = new Agricultura("D1", "SER-1", "M1", "FAB", 2.5, 10.0);
        DroneOperacionService receiver = new DroneOperacionService();
        ProgramadorTareas invoker = new ProgramadorTareas(receiver);
        invoker.seleccionarDron(dron);
        invoker.agregarTarea(new TomarFotografiaCommand(receiver));
        invoker.agregarTarea(new EscanearZonaCommand(receiver));
        invoker.agregarTarea(new MedirTemperaturaCommand(receiver));

        List<ResultadoTarea> resultados = invoker.ejecutarTareas();
        assertEquals("Tomar fotografía", resultados.get(0).tarea());
        assertEquals("Escanear zona", resultados.get(1).tarea());
        assertEquals("Medir temperatura", resultados.get(2).tarea());
    }
}
