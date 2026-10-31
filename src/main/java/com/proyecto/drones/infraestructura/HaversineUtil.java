package com.proyecto.drones.infraestructura;

import com.proyecto.drones.aplicacion.puerto.salida.DistanciaGeograficaPort;
import com.proyecto.drones.dominio.modelo.strategy.PuntoGeografico;

/** Utilidad para calcular distancia geodésica mediante la fórmula Haversine. */
public class HaversineUtil implements DistanciaGeograficaPort {
    private static final double RADIO_TIERRA_KM = 6371.0088d;

    /**
     * Calcula la distancia en kilómetros entre dos puntos geográficos.
     */
    @Override
    public double calcularDistancia(PuntoGeografico origen, PuntoGeografico destino) {
        if (origen == null || destino == null) {
            throw new IllegalArgumentException("Origen y destino son obligatorios.");
        }

        double lat1 = Math.toRadians(origen.getLatitud());
        double lat2 = Math.toRadians(destino.getLatitud());
        double deltaLat = Math.toRadians(destino.getLatitud() - origen.getLatitud());
        double deltaLon = Math.toRadians(destino.getLongitud() - origen.getLongitud());

        double a = Math.sin(deltaLat / 2) * Math.sin(deltaLat / 2)
                + Math.cos(lat1) * Math.cos(lat2)
                * Math.sin(deltaLon / 2) * Math.sin(deltaLon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return RADIO_TIERRA_KM * c;
    }
}
