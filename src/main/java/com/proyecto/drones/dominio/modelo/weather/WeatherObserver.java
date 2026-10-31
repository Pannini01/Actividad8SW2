package com.proyecto.drones.dominio.modelo.weather;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import com.proyecto.drones.dominio.modelo.Dron;

/**
 * Gestiona la suscripción dinámica de drones a las actualizaciones del clima.
 *
 * <p>Por restricción del modelo existente, la clase {@link Dron} no se
 * modifica. Por ello esta clase conserva las referencias de los drones
 * suscritos y realiza el envío de la notificación desde este punto.</p>
 */
public class WeatherObserver {

    private static final Logger LOGGER = Logger.getLogger(WeatherObserver.class.getName());

    private final List<Dron> observers = new ArrayList<>();

    /**
     * Añade un dron a la lista de suscriptores si su id aún no está registrado.
     *
     * @param dron dron que desea recibir notificaciones
     */
    public void addObserver(Dron dron) {
        if (dron == null || dron.getId() == null) {
            return;
        }
        boolean yaExiste = observers.stream()
                .anyMatch(actual -> dron.getId().equals(actual.getId()));
        if (!yaExiste) {
            observers.add(dron);
        }
    }

    /**
     * Elimina de la suscripción el dron que tenga el mismo id.
     *
     * @param dron dron que deja de recibir notificaciones
     */
    public void removeObserver(Dron dron) {
        if (dron == null || dron.getId() == null) {
            return;
        }
        observers.removeIf(actual -> dron.getId().equals(actual.getId()));
    }

    /**
     * Envía la actualización climática a cada dron actualmente suscrito.
     *
     * <p>La notificación no modifica el estado de {@code Dron}; esto mantiene
     * intactas las clases originales del modelo. La capa de aplicación se
     * encarga de presentar la información meteorológica y el resultado de la
     * validación en la vista JavaFX.</p>
     *
     * @param weather lectura climática que se notifica
     */
    public void notifyObservers(Weather weather) {
        if (weather == null) {
            throw new IllegalArgumentException("La información climática no puede ser nula.");
        }
        for (Dron dron : List.copyOf(observers)) {
            LOGGER.info(() -> String.format(
                    "Notificación al dron %s (%s): temperatura %.1f °C, humedad %d %%, "
                            + "viento %.1f km/h, presión %d hPa, visibilidad %.1f km",
                    dron.getId(), dron.getSerial(), weather.getTemperatura(),
                    weather.getHumedad(), weather.getVelocidadViento(),
                    weather.getPresionAtmosferica(), weather.getVisibilidad()));
        }
    }
}
