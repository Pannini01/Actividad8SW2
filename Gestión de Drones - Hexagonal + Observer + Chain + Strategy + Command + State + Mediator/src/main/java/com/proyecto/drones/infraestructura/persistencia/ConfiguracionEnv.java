package com.proyecto.drones.infraestructura.persistencia;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

/**
 * Lee la configuración local desde variables de entorno, propiedades Java
 * o desde un archivo {@code .env} ubicado en la raíz del proyecto.
 */
public final class ConfiguracionEnv {
    private final Map<String, String> valores;

    private ConfiguracionEnv(Map<String, String> valores) {
        this.valores = valores;
    }

    /**
     * Carga la configuración disponible.
     *
     * @return configuración cargada
     */
    public static ConfiguracionEnv cargar() {
        Map<String, String> valores = new HashMap<>();
        Path archivo = Path.of(".env");
        if (Files.exists(archivo)) {
            try {
                for (String linea : Files.readAllLines(archivo)) {
                    String limpia = linea.trim();
                    if (limpia.isEmpty() || limpia.startsWith("#") || !limpia.contains("=")) {
                        continue;
                    }
                    int pos = limpia.indexOf('=');
                    valores.put(limpia.substring(0, pos).trim(),
                            quitarComillas(limpia.substring(pos + 1).trim()));
                }
            } catch (IOException e) {
                throw new IllegalStateException("No fue posible leer el archivo .env.", e);
            }
        }
        return new ConfiguracionEnv(valores);
    }

    /**
     * Recupera una configuración obligatoria.
     *
     * @param clave nombre de la configuración
     * @return valor configurado
     */
    public String requerido(String clave) {
        String valor = System.getenv(clave);
        if (valor == null || valor.isBlank()) {
            valor = System.getProperty(clave);
        }
        if (valor == null || valor.isBlank()) {
            valor = valores.get(clave);
        }
        if (valor == null || valor.isBlank()) {
            throw new IllegalStateException("Falta la configuración obligatoria: " + clave);
        }
        return valor;
    }

    private static String quitarComillas(String valor) {
        if (valor.length() >= 2) {
            char primero = valor.charAt(0);
            char ultimo = valor.charAt(valor.length() - 1);
            if ((primero == '"' && ultimo == '"') || (primero == '\'' && ultimo == '\'')) {
                return valor.substring(1, valor.length() - 1);
            }
        }
        return valor;
    }
}
