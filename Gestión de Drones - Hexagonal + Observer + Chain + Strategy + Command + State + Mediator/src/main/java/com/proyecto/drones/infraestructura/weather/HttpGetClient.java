package com.proyecto.drones.infraestructura.weather;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;

/**
 * Cliente HTTP GET compartido por los adaptadores de infraestructura climática.
 *
 * <p>Concentra la configuración de timeouts, la lectura UTF-8, la validación
 * del código HTTP y el tratamiento uniforme de errores de red.</p>
 */
final class HttpGetClient {

    private static final Duration CONNECT_TIMEOUT = Duration.ofSeconds(10);
    private static final Duration REQUEST_TIMEOUT = Duration.ofSeconds(15);

    private final HttpClient httpClient = HttpClient.newBuilder()
            .connectTimeout(CONNECT_TIMEOUT)
            .build();

    /**
     * Ejecuta una solicitud GET y devuelve el cuerpo cuando la respuesta es 200.
     *
     * @param uri destino de la consulta
     * @param servicio nombre legible del servicio para mensajes de error
     * @return cuerpo UTF-8 de la respuesta
     */
    String get(URI uri, String servicio) {
        HttpRequest request = HttpRequest.newBuilder(uri)
                .timeout(REQUEST_TIMEOUT)
                .GET()
                .build();
        try {
            HttpResponse<String> response = httpClient.send(
                    request, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));
            validarRespuesta(response, servicio);
            return response.body();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new IllegalStateException("La consulta a " + servicio + " fue interrumpida.", e);
        } catch (IOException e) {
            throw new IllegalStateException("No fue posible conectar con " + servicio + ".", e);
        }
    }

    private void validarRespuesta(HttpResponse<String> response, String servicio) {
        if (response.statusCode() != 200) {
            throw new IllegalStateException(
                    servicio + " respondió HTTP " + response.statusCode() + ".");
        }
    }
}
