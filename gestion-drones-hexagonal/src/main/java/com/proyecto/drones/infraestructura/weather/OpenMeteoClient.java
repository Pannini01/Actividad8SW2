package com.proyecto.drones.infraestructura.weather;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Locale;

/** Cliente HTTP de bajo nivel para Open-Meteo Forecast API. */
public class OpenMeteoClient {

    private static final String ENDPOINT = "https://api.open-meteo.com/v1/forecast";

    private final HttpClient httpClient = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(10))
            .build();

    public String consultar(double latitude, double longitude) {
        try {
            String lat = String.format(Locale.US, "%.6f", latitude);
            String lon = String.format(Locale.US, "%.6f", longitude);

            URI uri = URI.create(ENDPOINT
                    + "?latitude=" + lat
                    + "&longitude=" + lon
                    + "&current=temperature_2m,relative_humidity_2m,wind_speed_10m,pressure_msl,visibility"
                    + "&wind_speed_unit=kmh&timezone=auto");

            HttpRequest request = HttpRequest.newBuilder(uri)
                    .timeout(Duration.ofSeconds(15))
                    .GET()
                    .build();

            HttpResponse<String> response = httpClient.send(
                    request, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));

            if (response.statusCode() != 200) {
                throw new IllegalStateException(
                        "La API de clima respondió HTTP " + response.statusCode() + ".");
            }
            return response.body();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new IllegalStateException("La consulta meteorológica fue interrumpida.", e);
        } catch (IOException e) {
            throw new IllegalStateException("No fue posible conectar con la API de clima.", e);
        }
    }
}
