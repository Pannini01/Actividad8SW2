package com.proyecto.drones.aplicacion.servicio;

import java.util.Objects;

import com.proyecto.drones.dominio.modelo.Agricultura;
import com.proyecto.drones.dominio.modelo.Dron;

/**
 * Validador compartido de las reglas comunes de alta y actualización de drones.
 *
 * <p>Centraliza las reglas para evitar que los casos de uso de creación y
 * actualización mantengan bloques idénticos de validación.</p>
 */
final class DronValidator {

    private DronValidator() {
        // Utilidad sin instancias.
    }

    /**
     * Valida los datos obligatorios y las restricciones numéricas del dron.
     *
     * @param dron dron que se desea crear o actualizar
     * @throws NullPointerException si el dron es nulo
     * @throws IllegalArgumentException si algún dato incumple las reglas
     */
    static void validar(Dron dron) {
        Objects.requireNonNull(dron, "El dron es obligatorio.");
        textoObligatorio(dron.getId(), "El id es obligatorio.");
        textoObligatorio(dron.getSerial(), "El serial es obligatorio.");
        textoObligatorio(dron.getModelo(), "El modelo es obligatorio.");
        textoObligatorio(dron.getFabricante(), "El fabricante es obligatorio.");
        if (dron.getPeso() <= 0) {
            throw new IllegalArgumentException("El peso debe ser mayor que cero.");
        }
        if (dron instanceof Agricultura agricultura && agricultura.getCapacidadTanque() <= 0) {
            throw new IllegalArgumentException("La capacidad del tanque debe ser mayor que cero.");
        }
    }

    /**
     * Valida y normaliza un identificador recibido por los casos de uso.
     *
     * @param id identificador a validar
     * @param mensaje mensaje a usar cuando no existe un valor válido
     * @return identificador sin espacios laterales
     */
    static String validarId(String id, String mensaje) {
        return textoObligatorio(id, mensaje);
    }

    private static String textoObligatorio(String valor, String mensaje) {
        if (valor == null || valor.isBlank()) {
            throw new IllegalArgumentException(mensaje);
        }
        return valor.trim();
    }
}
