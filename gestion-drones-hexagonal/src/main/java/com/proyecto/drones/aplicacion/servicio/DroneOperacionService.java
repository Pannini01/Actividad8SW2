package com.proyecto.drones.aplicacion.servicio;

import com.proyecto.drones.dominio.modelo.Dron;

/**
 * Receiver del patrón Command.
 *
 * <p>Es la única pieza de Command que mantiene el dron seleccionado. Los
 * comandos concretos son wrappers y delegan aquí sus operaciones.</p>
 */
public class DroneOperacionService {
    private Dron dron;

    public void seleccionarDron(Dron dron) {
        if (dron == null) {
            throw new IllegalArgumentException("Seleccione un dron para la misión.");
        }
        this.dron = dron;
    }

    public Dron getDron() {
        return dron;
    }

    public String tomarFotografia() {
        return "Fotografía tomada correctamente por el dron " + serialSeleccionado() + ".";
    }

    public String escanearZona() {
        return "Zona escaneada correctamente por el dron " + serialSeleccionado() + ".";
    }

    public String medirTemperatura() {
        return "Temperatura medida correctamente por el dron " + serialSeleccionado() + ".";
    }

    private String serialSeleccionado() {
        if (dron == null) {
            throw new IllegalStateException("Debe seleccionar un dron antes de ejecutar la misión.");
        }
        return dron.getSerial();
    }
}
