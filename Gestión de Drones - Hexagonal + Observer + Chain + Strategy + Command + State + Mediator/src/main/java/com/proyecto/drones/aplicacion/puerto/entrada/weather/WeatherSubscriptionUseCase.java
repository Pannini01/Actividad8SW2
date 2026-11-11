package com.proyecto.drones.aplicacion.puerto.entrada.weather;

import com.proyecto.drones.dominio.modelo.Dron;

/** Puerto de entrada para gestionar suscripciones al Observer climático. */
public interface WeatherSubscriptionUseCase {

    void suscribir(Dron dron);

    void desuscribir(Dron dron);
}
