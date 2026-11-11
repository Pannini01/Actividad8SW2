package com.proyecto.drones.dominio.modelo.weather;

/**
 * Representa una lectura meteorológica utilizada por el Observer y por la
 * cadena de validación climática.
 *
 * <p>Las unidades manejadas por el dominio son: temperatura en grados
 * Celsius, humedad en porcentaje, velocidad del viento en km/h, presión
 * atmosférica en hPa y visibilidad en kilómetros.</p>
 */
public class Weather {

    private double temperatura;
    private int humedad;
    private double velocidadViento;
    private int presionAtmosferica;
    private double visibilidad;

    /** Constructor vacío para facilitar pruebas y serialización manual. */
    public Weather() {
    }

    /**
     * Construye una lectura climática completa.
     *
     * @param temperatura temperatura en °C
     * @param humedad humedad relativa en %
     * @param velocidadViento velocidad del viento en km/h
     * @param presionAtmosferica presión a nivel del mar en hPa
     * @param visibilidad visibilidad en km
     */
    public Weather(double temperatura, int humedad, double velocidadViento,
            int presionAtmosferica, double visibilidad) {
        this.temperatura = temperatura;
        this.humedad = humedad;
        this.velocidadViento = velocidadViento;
        this.presionAtmosferica = presionAtmosferica;
        this.visibilidad = visibilidad;
    }

    public double getTemperatura() {
        return temperatura;
    }

    public void setTemperatura(double temperatura) {
        this.temperatura = temperatura;
    }

    public int getHumedad() {
        return humedad;
    }

    public void setHumedad(int humedad) {
        this.humedad = humedad;
    }

    public double getVelocidadViento() {
        return velocidadViento;
    }

    public void setVelocidadViento(double velocidadViento) {
        this.velocidadViento = velocidadViento;
    }

    public int getPresionAtmosferica() {
        return presionAtmosferica;
    }

    public void setPresionAtmosferica(int presionAtmosferica) {
        this.presionAtmosferica = presionAtmosferica;
    }

    public double getVisibilidad() {
        return visibilidad;
    }

    public void setVisibilidad(double visibilidad) {
        this.visibilidad = visibilidad;
    }
}
