package com.proyecto.drones.dominio.modelo;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Entidad abstracta que representa un dron dentro del dominio.
 *
 * <p>Contiene únicamente estado y relaciones de negocio. No depende de
 * JavaFX, JDBC, PostgreSQL ni de clases de infraestructura.</p>
 */
public abstract class Dron {
    /** Identificador persistible del subtipo agrícola. */
    public static final String TIPO_AGRICULTURA = "AGRICULTURA";
    /** Identificador persistible del subtipo de vigilancia. */
    public static final String TIPO_VIGILANCIA = "VIGILANCIA";

    private String id;
    private String serial;
    private String modelo;
    private String fabricante;
    private double peso;
    private Piloto piloto;
    private List<Sensor> sensores = new ArrayList<>();

    /** Construye un dron sin datos iniciales. */
    protected Dron() {
    }

    /**
     * Inicializa los atributos comunes de un dron.
     *
     * @param id identificador único
     * @param serial serial asignado por el fabricante
     * @param modelo modelo comercial
     * @param fabricante nombre del fabricante
     * @param peso peso en kilogramos
     */
    protected Dron(String id, String serial, String modelo, String fabricante, double peso) {
        this.id = id;
        this.serial = serial;
        this.modelo = modelo;
        this.fabricante = fabricante;
        this.peso = peso;
    }

    /**
     * Obtiene el discriminador del subtipo de dron.
     *
     * @return tipo persistible del dron
     */
    public abstract String getTipo();

    /**
     * Obtiene el identificador único.
     * @return identificador único
     */
    public String getId() { return id; }
    /**
     * Cambia el identificador único.
     * @param id nuevo identificador
     */
    public void setId(String id) { this.id = id; }
    /**
     * Obtiene el serial.
     * @return serial del dron
     */
    public String getSerial() { return serial; }
    /**
     * Cambia el serial.
     * @param serial nuevo serial
     */
    public void setSerial(String serial) { this.serial = serial; }
    /**
     * Obtiene el modelo comercial.
     * @return modelo comercial
     */
    public String getModelo() { return modelo; }
    /**
     * Cambia el modelo comercial.
     * @param modelo nuevo modelo
     */
    public void setModelo(String modelo) { this.modelo = modelo; }
    /**
     * Obtiene el fabricante.
     * @return fabricante del dron
     */
    public String getFabricante() { return fabricante; }
    /**
     * Cambia el fabricante.
     * @param fabricante nuevo fabricante
     */
    public void setFabricante(String fabricante) { this.fabricante = fabricante; }
    /**
     * Obtiene el peso.
     * @return peso en kilogramos
     */
    public double getPeso() { return peso; }
    /**
     * Cambia el peso.
     * @param peso nuevo peso en kilogramos
     */
    public void setPeso(double peso) { this.peso = peso; }
    /**
     * Obtiene el piloto asociado.
     * @return piloto o {@code null}
     */
    public Piloto getPiloto() { return piloto; }
    /**
     * Asocia un piloto.
     * @param piloto piloto asociado
     */
    public void setPiloto(Piloto piloto) { this.piloto = piloto; }
    /**
     * Obtiene una copia de los sensores asociados.
     * @return sensores asociados
     */
    public List<Sensor> getSensores() { return new ArrayList<>(sensores); }
    /**
     * Reemplaza los sensores asociados.
     * @param sensores nueva colección de sensores
     */
    public void setSensores(List<Sensor> sensores) {
        this.sensores = sensores == null ? new ArrayList<>() : new ArrayList<>(sensores);
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof Dron otro && Objects.equals(id, otro.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
