package com.proyecto.drones.aplicacion.puerto.entrada;

import java.util.List;

import com.proyecto.drones.aplicacion.dto.ResultadoTarea;
import com.proyecto.drones.dominio.modelo.Dron;
import com.proyecto.drones.dominio.modelo.command.DroneCommand;

/** Puerto de entrada para programar y ejecutar comandos en orden. */
public interface ProgramarTareasUseCase {
    void seleccionarDron(Dron dron);
    void agregarTarea(DroneCommand comando);
    List<String> listarTareas();
    List<ResultadoTarea> ejecutarTareas();
    void limpiarTareas();
}
