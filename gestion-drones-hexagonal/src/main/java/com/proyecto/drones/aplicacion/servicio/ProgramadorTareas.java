package com.proyecto.drones.aplicacion.servicio;

import java.util.ArrayList;
import java.util.List;

import com.proyecto.drones.aplicacion.dto.ResultadoTarea;
import com.proyecto.drones.aplicacion.puerto.entrada.ProgramarTareasUseCase;
import com.proyecto.drones.dominio.modelo.Dron;
import com.proyecto.drones.dominio.modelo.command.DroneCommand;

/** Invoker del patrón Command. Conserva y ejecuta tareas en orden de registro. */
public class ProgramadorTareas implements ProgramarTareasUseCase {
    private final List<DroneCommand> tareas = new ArrayList<>();
    private final DroneOperacionService receiver;

    public ProgramadorTareas(DroneOperacionService receiver) {
        this.receiver = receiver;
    }

    @Override
    public void seleccionarDron(Dron dron) {
        receiver.seleccionarDron(dron);
    }

    @Override
    public void agregarTarea(DroneCommand comando) {
        if (comando == null) {
            throw new IllegalArgumentException("La tarea no puede ser nula.");
        }
        tareas.add(comando);
    }

    @Override
    public List<String> listarTareas() {
        return tareas.stream().map(this::nombreTarea).toList();
    }

    @Override
    public List<ResultadoTarea> ejecutarTareas() {
        if (receiver.getDron() == null) {
            throw new IllegalStateException("Seleccione un dron antes de ejecutar las tareas.");
        }
        if (tareas.isEmpty()) {
            throw new IllegalStateException("Agregue al menos una tarea a la misión.");
        }

        List<ResultadoTarea> resultados = new ArrayList<>();
        for (DroneCommand tarea : tareas) {
            resultados.add(new ResultadoTarea(nombreTarea(tarea), tarea.ejecutar()));
        }
        return resultados;
    }

    @Override
    public void limpiarTareas() {
        tareas.clear();
    }

    private String nombreTarea(DroneCommand comando) {
        String nombre = comando.getClass().getSimpleName();
        return switch (nombre) {
            case "TomarFotografiaCommand" -> "Tomar fotografía";
            case "EscanearZonaCommand" -> "Escanear zona";
            case "MedirTemperaturaCommand" -> "Medir temperatura";
            default -> nombre;
        };
    }
}
