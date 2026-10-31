package com.proyecto.drones.dominio.modelo;

import java.util.Objects;

/** Piloto responsable de operar uno o varios drones. */
public class Piloto {
    private String id;
    private String nombre;
    private String licencia;
    private String telefono;

    /** Construye un piloto sin datos iniciales. */
    public Piloto() {
    }

    /**
     * Crea un piloto.
     *
     * @param id identificador
     * @param nombre nombre completo
     * @param licencia licencia
     * @param telefono teléfono
     */
    public Piloto(String id, String nombre, String licencia, String telefono) {
        this.id = id;
        this.nombre = nombre;
        this.licencia = licencia;
        this.telefono = telefono;
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
     * @return nombre completo
     */
    public String getNombre() { return nombre; }
    /**
     * Cambia el nombre.
     * @param nombre nuevo nombre
     */
    public void setNombre(String nombre) { this.nombre = nombre; }
    /**
     * Obtiene la licencia.
     * @return licencia del piloto
     */
    public String getLicencia() { return licencia; }
    /**
     * Cambia la licencia.
     * @param licencia nueva licencia
     */
    public void setLicencia(String licencia) { this.licencia = licencia; }
    /**
     * Obtiene el teléfono.
     * @return teléfono
     */
    public String getTelefono() { return telefono; }
    /**
     * Cambia el teléfono.
     * @param telefono nuevo teléfono
     */
    public void setTelefono(String telefono) { this.telefono = telefono; }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof Piloto otro && Objects.equals(id, otro.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
