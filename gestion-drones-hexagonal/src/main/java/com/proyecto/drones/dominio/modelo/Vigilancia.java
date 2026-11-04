package com.proyecto.drones.dominio.modelo;

/** Dron especializado en labores de vigilancia y monitoreo. */
public class Vigilancia extends Dron {
    private boolean deteccionTermica;

    /** Construye un dron de vigilancia sin datos iniciales. */
    public Vigilancia() {
    }

    /**
     * Crea un dron de vigilancia completo.
     *
     * @param id identificador
     * @param serial serial
     * @param modelo modelo
     * @param fabricante fabricante
     * @param peso peso en kilogramos
     * @param deteccionTermica disponibilidad de detección térmica
     */
    public Vigilancia(String id, String serial, String modelo, String fabricante,
            double peso, boolean deteccionTermica) {
        super(id, serial, modelo, fabricante, peso);
        this.deteccionTermica = deteccionTermica;
    }

    @Override
    public String getTipo() {
        return TIPO_VIGILANCIA;
    }

    /**
     * Indica si dispone de detección térmica.
     * @return estado de detección térmica
     */
    public boolean isDeteccionTermica() { return deteccionTermica; }
    /**
     * Obtiene el estado de detección térmica.
     * @return estado de detección térmica
     */
    public boolean getDeteccionTermica() { return deteccionTermica; }
    /**
     * Cambia la disponibilidad térmica.
     * @param deteccionTermica nuevo estado
     */
    public void setDeteccionTermica(boolean deteccionTermica) { this.deteccionTermica = deteccionTermica; }
}
