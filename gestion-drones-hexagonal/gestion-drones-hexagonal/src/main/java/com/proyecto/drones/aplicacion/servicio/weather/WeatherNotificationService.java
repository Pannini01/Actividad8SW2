package com.proyecto.drones.aplicacion.servicio.weather;

import java.util.Set;

import com.proyecto.drones.aplicacion.dto.weather.Coordinates;
import com.proyecto.drones.aplicacion.dto.weather.WeatherResult;
import com.proyecto.drones.aplicacion.puerto.entrada.weather.GetWeatherByCityUseCase;
import com.proyecto.drones.aplicacion.puerto.entrada.weather.WeatherSubscriptionUseCase;
import com.proyecto.drones.aplicacion.puerto.salida.weather.GeoApiPort;
import com.proyecto.drones.aplicacion.puerto.salida.weather.WeatherApiPort;
import com.proyecto.drones.dominio.modelo.Dron;
import com.proyecto.drones.dominio.modelo.chain.WeatherHandler;
import com.proyecto.drones.dominio.modelo.weather.Weather;
import com.proyecto.drones.dominio.modelo.weather.WeatherObserver;

/**
 * Servicio de aplicación que orquesta APIs, Observer y Chain of Responsibility.
 */
public class WeatherNotificationService
        implements GetWeatherByCityUseCase, WeatherSubscriptionUseCase {

    private static final Set<String> CIUDADES_PERMITIDAS = Set.of(
            "Bogotá", "Medellín", "Cali", "Barranquilla", "Cartagena");

    private final GeoApiPort geoApiPort;
    private final WeatherApiPort weatherApiPort;
    private final WeatherObserver weatherObserver;
    private final WeatherHandler chainInicial;

    public WeatherNotificationService(GeoApiPort geoApiPort,
            WeatherApiPort weatherApiPort,
            WeatherObserver weatherObserver,
            WeatherHandler chainInicial) {
        this.geoApiPort = geoApiPort;
        this.weatherApiPort = weatherApiPort;
        this.weatherObserver = weatherObserver;
        this.chainInicial = chainInicial;
    }

    @Override
    public WeatherResult consultar(String ciudad) {
        if (ciudad == null || !CIUDADES_PERMITIDAS.contains(ciudad)) {
            throw new IllegalArgumentException("Seleccione una de las cinco ciudades permitidas.");
        }

        Coordinates coordenadas = geoApiPort.buscarCiudad(ciudad);
        Weather weather = weatherApiPort.obtenerClimaActual(
                coordenadas.latitude(), coordenadas.longitude());

        String resultado = chainInicial.handle(weather);
        weatherObserver.notifyObservers(weather);

        return new WeatherResult(ciudad, weather, resultado);
    }

    @Override
    public void suscribir(Dron dron) {
        if (dron == null) {
            throw new IllegalArgumentException("Seleccione un dron para suscribirlo.");
        }
        weatherObserver.addObserver(dron);
    }

    @Override
    public void desuscribir(Dron dron) {
        if (dron == null) {
            throw new IllegalArgumentException("Seleccione un dron para desuscribirlo.");
        }
        weatherObserver.removeObserver(dron);
    }
}
