package com.proyecto.drones.dominio.modelo;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/** Misión planificada que puede involucrar uno o varios drones. */
public class Mision {
    private String id;
    private String nombre;
    private String ubicacion;
    private LocalDate fecha;
    private List<Dron> drones = new ArrayList<>();

    /** Construye una misión sin datos iniciales. */
    public Mision() {
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
     * Obtiene el nombre.
     * @return nombre de la misión
     */
    public String getNombre() { return nombre; }
    /**
     * Cambia el nombre.
     * @param nombre nuevo nombre
     */
    public void setNombre(String nombre) { this.nombre = nombre; }
    /**
     * Obtiene la ubicación.
     * @return ubicación
     */
    public String getUbicacion() { return ubicacion; }
    /**
     * Cambia la ubicación.
     * @param ubicacion nueva ubicación
     */
    public void setUbicacion(String ubicacion) { this.ubicacion = ubicacion; }
    /**
     * Obtiene la fecha.
     * @return fecha programada
     */
    public LocalDate getFecha() { return fecha; }
    /**
     * Cambia la fecha.
     * @param fecha nueva fecha
     */
    public void setFecha(LocalDate fecha) { this.fecha = fecha; }
    /**
     * Obtiene una copia de los drones asignados.
     * @return drones asignados
     */
    public List<Dron> getDrones() { return new ArrayList<>(drones); }
    /**
     * Reemplaza los drones asignados.
     * @param drones nueva colección de drones
     */
    public void setDrones(List<Dron> drones) {
        this.drones = drones == null ? new ArrayList<>() : new ArrayList<>(drones);
    }
}
