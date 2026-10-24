package com.proyecto.drones.infraestructura.weather;

import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;

/** Cliente HTTP de bajo nivel para Open-Meteo Geocoding API. */
public class GeoApiClient {

    private static final String ENDPOINT = "https://geocoding-api.open-meteo.com/v1/search";

    private final HttpClient httpClient = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(10))
            .build();

    public String buscar(String ciudad) {
        try {
            String nombre = URLEncoder.encode(ciudad, StandardCharsets.UTF_8);
            URI uri = URI.create(ENDPOINT
                    + "?name=" + nombre
                    + "&count=1&language=es&countryCode=CO&format=json");

            HttpRequest request = HttpRequest.newBuilder(uri)
                    .timeout(Duration.ofSeconds(15))
                    .GET()
                    .build();

            HttpResponse<String> response = httpClient.send(
                    request, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));

            if (response.statusCode() != 200) {
                throw new IllegalStateException(
                        "La API de geolocalización respondió HTTP " + response.statusCode() + ".");
            }
            return response.body();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new IllegalStateException("La consulta de geolocalización fue interrumpida.", e);
        } catch (IOException e) {
            throw new IllegalStateException("No fue posible conectar con la API de geolocalización.", e);
        }
    }
}
