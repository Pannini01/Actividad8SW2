package com.proyecto.drones.infraestructura;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.proyecto.drones.aplicacion.puerto.salida.RutaLoaderPort;
import com.proyecto.drones.dominio.modelo.strategy.PuntoGeografico;

/**
 * Carga desde JSON los puntos de origen y destino requeridos por Strategy.
 *
 * <p>El formato esperado contiene los objetos {@code origen} y
 * {@code destino}, cada uno con nombre, latitud y longitud.</p>
 */
public class JsonRutaLoader implements RutaLoaderPort {
    private static final Pattern BLOQUE = Pattern.compile(
            "\\\"%s\\\"\\s*:\\s*\\{(.*?)\\}", Pattern.DOTALL);
    private static final Pattern NOMBRE = Pattern.compile(
            "\\\"nombre\\\"\\s*:\\s*\\\"([^\\\"]+)\\\"");
    private static final Pattern LATITUD = Pattern.compile(
            "\\\"latitud\\\"\\s*:\\s*(-?\\d+(?:\\.\\d+)?)");
    private static final Pattern LONGITUD = Pattern.compile(
            "\\\"longitud\\\"\\s*:\\s*(-?\\d+(?:\\.\\d+)?)");

    /**
     * Lee un archivo JSON y devuelve origen en posición 0 y destino en 1.
     */
    @Override
    public PuntoGeografico[] cargarRuta(Path rutaArchivo) {
        if (rutaArchivo == null) {
            throw new IllegalArgumentException("Debe seleccionar un archivo JSON de ruta.");
        }
        try {
            String json = Files.readString(rutaArchivo, StandardCharsets.UTF_8);
            PuntoGeografico origen = leerPunto(json, "origen");
            PuntoGeografico destino = leerPunto(json, "destino");
            return new PuntoGeografico[] { origen, destino };
        } catch (IOException ex) {
            throw new IllegalStateException("No fue posible leer el archivo JSON: " + ex.getMessage(), ex);
        }
    }

    private PuntoGeografico leerPunto(String json, String clave) {
        Pattern patronBloque = Pattern.compile(String.format(BLOQUE.pattern(), Pattern.quote(clave)), Pattern.DOTALL);
        Matcher bloque = patronBloque.matcher(json);
        if (!bloque.find()) {
            throw new IllegalArgumentException("El JSON no contiene el objeto obligatorio: " + clave);
        }
        String contenido = bloque.group(1);
        String nombre = extraerTexto(NOMBRE, contenido, clave + ".nombre");
        double latitud = extraerNumero(LATITUD, contenido, clave + ".latitud");
        double longitud = extraerNumero(LONGITUD, contenido, clave + ".longitud");
        validarCoordenadas(latitud, longitud, clave);
        return new PuntoGeografico(nombre, latitud, longitud);
    }

    private String extraerTexto(Pattern pattern, String contenido, String campo) {
        Matcher matcher = pattern.matcher(contenido);
        if (!matcher.find()) {
            throw new IllegalArgumentException("Falta el campo obligatorio " + campo + ".");
        }
        return matcher.group(1).trim();
    }

    private double extraerNumero(Pattern pattern, String contenido, String campo) {
        Matcher matcher = pattern.matcher(contenido);
        if (!matcher.find()) {
            throw new IllegalArgumentException("Falta el campo obligatorio " + campo + ".");
        }
        return Double.parseDouble(matcher.group(1));
    }

    private void validarCoordenadas(double latitud, double longitud, String clave) {
        if (latitud < -90 || latitud > 90) {
            throw new IllegalArgumentException("Latitud inválida en " + clave + ".");
        }
        if (longitud < -180 || longitud > 180) {
            throw new IllegalArgumentException("Longitud inválida en " + clave + ".");
        }
    }
}
