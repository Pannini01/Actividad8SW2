package com.proyecto.drones.infraestructura.weather;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.proyecto.drones.aplicacion.dto.weather.Coordinates;
import com.proyecto.drones.aplicacion.puerto.salida.weather.GeoApiPort;

/** Adaptador de Open-Meteo Geocoding al puerto de salida de la aplicación. */
public class GeoApiAdapter implements GeoApiPort {

    private static final Pattern LATITUDE = Pattern.compile("\\\"latitude\\\"\\s*:\\s*(-?\\d+(?:\\.\\d+)?)");
    private static final Pattern LONGITUDE = Pattern.compile("\\\"longitude\\\"\\s*:\\s*(-?\\d+(?:\\.\\d+)?)");

    private final GeoApiClient client;

    public GeoApiAdapter(GeoApiClient client) {
        this.client = client;
    }

    @Override
    public Coordinates buscarCiudad(String ciudad) {
        String json = client.buscar(ciudad);
        Matcher lat = LATITUDE.matcher(json);
        Matcher lon = LONGITUDE.matcher(json);
        if (!lat.find() || !lon.find()) {
            throw new IllegalStateException("Open-Meteo no devolvió coordenadas para " + ciudad + ".");
        }
        return new Coordinates(Double.parseDouble(lat.group(1)), Double.parseDouble(lon.group(1)));
    }
}
