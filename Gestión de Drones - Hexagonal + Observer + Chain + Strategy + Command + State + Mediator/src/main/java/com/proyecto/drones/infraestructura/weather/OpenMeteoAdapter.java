package com.proyecto.drones.infraestructura.weather;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.proyecto.drones.aplicacion.puerto.salida.weather.WeatherApiPort;
import com.proyecto.drones.dominio.modelo.weather.Weather;

/**
 * Adaptador que transforma la respuesta JSON de Open-Meteo al objeto de dominio
 * {@link Weather} y normaliza la visibilidad de metros a kilómetros.
 */
public class OpenMeteoAdapter implements WeatherApiPort {

    private static final Pattern CURRENT = Pattern.compile("\\\"current\\\"\\s*:\\s*\\{([^}]*)}");

    private final OpenMeteoClient client;

    public OpenMeteoAdapter(OpenMeteoClient client) {
        this.client = client;
    }

    @Override
    public Weather obtenerClimaActual(double latitude, double longitude) {
        String json = client.consultar(latitude, longitude);
        Matcher currentMatcher = CURRENT.matcher(json);
        if (!currentMatcher.find()) {
            throw new IllegalStateException("Open-Meteo no devolvió el bloque current esperado.");
        }

        String current = currentMatcher.group(1);
        double temperatura = numero(current, "temperature_2m");
        int humedad = (int) Math.round(numero(current, "relative_humidity_2m"));
        double viento = numero(current, "wind_speed_10m");
        int presion = (int) Math.round(numero(current, "pressure_msl"));
        double visibilidadKm = numero(current, "visibility") / 1000.0;

        return new Weather(temperatura, humedad, viento, presion, visibilidadKm);
    }

    private double numero(String json, String campo) {
        Pattern patron = Pattern.compile("\\\"" + Pattern.quote(campo)
                + "\\\"\\s*:\\s*(-?\\d+(?:\\.\\d+)?)");
        Matcher matcher = patron.matcher(json);
        if (!matcher.find()) {
            throw new IllegalStateException("La respuesta climática no contiene el campo " + campo + ".");
        }
        return Double.parseDouble(matcher.group(1));
    }
}
