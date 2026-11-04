package com.proyecto.drones.dominio.modelo;

/** Dron especializado en actividades agrícolas y equipado con tanque. */
public class Agricultura extends Dron {
    private double capacidadTanque;

    /** Construye un dron agrícola sin datos iniciales. */
    public Agricultura() {
    }

    /**
     * Crea un dron agrícola completo.
     *
     * @param id identificador
     * @param serial serial
     * @param modelo modelo
     * @param fabricante fabricante
     * @param peso peso en kilogramos
     * @param capacidadTanque capacidad del tanque en litros
     */
    public Agricultura(String id, String serial, String modelo, String fabricante,
            double peso, double capacidadTanque) {
        super(id, serial, modelo, fabricante, peso);
        this.capacidadTanque = capacidadTanque;
    }

    @Override
    public String getTipo() {
        return TIPO_AGRICULTURA;
    }

    /**
     * Obtiene la capacidad del tanque.
     * @return capacidad en litros
     */
    public double getCapacidadTanque() { return capacidadTanque; }
    /**
     * Cambia la capacidad del tanque.
     * @param capacidadTanque capacidad en litros
     */
    public void setCapacidadTanque(double capacidadTanque) { this.capacidadTanque = capacidadTanque; }
}
