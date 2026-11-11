package com.proyecto.drones.infraestructura.weather;

import java.net.URI;
import java.util.Locale;

/** Cliente HTTP de bajo nivel para Open-Meteo Forecast API. */
public class OpenMeteoClient {

    private static final String ENDPOINT = "https://api.open-meteo.com/v1/forecast";
    private static final String SERVICIO = "la API de clima";

    private final HttpGetClient httpGetClient;

    /** Crea el cliente utilizando la configuración HTTP compartida. */
    public OpenMeteoClient() {
        this.httpGetClient = new HttpGetClient();
    }

    /**
     * Consulta las condiciones meteorológicas actuales de unas coordenadas.
     *
     * @param latitude latitud WGS84
     * @param longitude longitud WGS84
     * @return respuesta JSON de Open-Meteo
     */
    public String consultar(double latitude, double longitude) {
        String lat = String.format(Locale.US, "%.6f", latitude);
        String lon = String.format(Locale.US, "%.6f", longitude);
        URI uri = URI.create(ENDPOINT
                + "?latitude=" + lat
                + "&longitude=" + lon
                + "&current=temperature_2m,relative_humidity_2m,wind_speed_10m,pressure_msl,visibility"
                + "&wind_speed_unit=kmh&timezone=auto");
        return httpGetClient.get(uri, SERVICIO);
    }
}
