package com.proyecto.drones.infraestructura.weather;

import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

/** Cliente HTTP de bajo nivel para Open-Meteo Geocoding API. */
public class GeoApiClient {

    private static final String ENDPOINT = "https://geocoding-api.open-meteo.com/v1/search";
    private static final String SERVICIO = "la API de geolocalización";

    private final HttpGetClient httpGetClient;

    /** Crea el cliente utilizando la configuración HTTP compartida. */
    public GeoApiClient() {
        this.httpGetClient = new HttpGetClient();
    }

    /**
     * Busca la primera coincidencia colombiana para una ciudad.
     *
     * @param ciudad nombre de la ciudad
     * @return respuesta JSON de Open-Meteo
     */
    public String buscar(String ciudad) {
        String nombre = URLEncoder.encode(ciudad, StandardCharsets.UTF_8);
        URI uri = URI.create(ENDPOINT
                + "?name=" + nombre
                + "&count=1&language=es&countryCode=CO&format=json");
        return httpGetClient.get(uri, SERVICIO);
    }
}
