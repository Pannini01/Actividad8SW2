package com.proyecto.drones.dominio.modelo;

/** Sensor físico que puede asociarse a un dron. */
public class Sensor {
    private String id;
    private String tipo;
    private String fabricante;

    /** Construye un sensor sin datos iniciales. */
    public Sensor() {
    }

    /**
     * Crea un sensor.
     *
     * @param id identificador
     * @param tipo tipo de sensor
     * @param fabricante fabricante
     */
    public Sensor(String id, String tipo, String fabricante) {
        this.id = id;
        this.tipo = tipo;
        this.fabricante = fabricante;
    }

    /**
     * Obtiene el identificador.
     * @return identificador
     */
    public String getId() { return id; }
    /**
     * Cambia el identificador.
     * @param id nuevo identificador
     */
    public void setId(String id) { this.id = id; }
    /**
     * Obtiene el tipo de sensor.
     * @return tipo
     */
    public String getTipo() { return tipo; }
    /**
     * Cambia el tipo de sensor.
     * @param tipo nuevo tipo
     */
    public void setTipo(String tipo) { this.tipo = tipo; }
    /**
     * Obtiene el fabricante.
     * @return fabricante
     */
    public String getFabricante() { return fabricante; }
    /**
     * Cambia el fabricante.
     * @param fabricante nuevo fabricante
     */
    public void setFabricante(String fabricante) { this.fabricante = fabricante; }
}
