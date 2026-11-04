package com.proyecto.drones.dominio.modelo.chain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.proyecto.drones.dominio.modelo.weather.Weather;

class WeatherChainTest {

    private WeatherHandler cadena;

    @BeforeEach
    void setUp() {
        WeatherHandler temperatura = new TemperatureHandler();
        WeatherHandler humedad = new HumidityHandler();
        WeatherHandler viento = new WindHandler();
        WeatherHandler presion = new PressureHandler();
        WeatherHandler visibilidad = new VisibilityHandler();
        temperatura.setNext(humedad);
        humedad.setNext(viento);
        viento.setNext(presion);
        presion.setNext(visibilidad);
        cadena = temperatura;
    }

    @Test
    void apruebaClimaDentroDeTodosLosRangos() {
        Weather weather = new Weather(22, 70, 18, 1012, 9);
        assertEquals("CLIMA APTO PARA OPERACIÓN DE DRONES", cadena.handle(weather));
    }

    @Test
    void detieneCadenaEnHumedadYMuestraMotivo() {
        Weather weather = new Weather(22, 91, 18, 1012, 9);
        String resultado = cadena.handle(weather);
        assertTrue(resultado.contains("CLIMA NO APTO"));
        assertTrue(resultado.contains("humedad"));
    }
}
