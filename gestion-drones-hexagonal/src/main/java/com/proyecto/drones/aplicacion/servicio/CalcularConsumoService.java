package com.proyecto.drones.aplicacion.servicio;

import java.nio.file.Path;

import com.proyecto.drones.aplicacion.dto.ConsumoResult;
import com.proyecto.drones.aplicacion.puerto.entrada.CalcularConsumoUseCase;
import com.proyecto.drones.aplicacion.puerto.salida.DistanciaGeograficaPort;
import com.proyecto.drones.aplicacion.puerto.salida.RutaLoaderPort;
import com.proyecto.drones.dominio.modelo.Dron;
import com.proyecto.drones.dominio.modelo.strategy.AhorroStrategy;
import com.proyecto.drones.dominio.modelo.strategy.AltoRendimientoStrategy;
import com.proyecto.drones.dominio.modelo.strategy.ConsumoStrategy;
import com.proyecto.drones.dominio.modelo.strategy.NormalStrategy;
import com.proyecto.drones.dominio.modelo.strategy.PuntoGeografico;

/** Servicio/Context que carga la ruta, calcula distancia y aplica Strategy. */
public class CalcularConsumoService implements CalcularConsumoUseCase {
    private final RutaLoaderPort jsonRutaLoader;
    private final DistanciaGeograficaPort haversineUtil;

    public CalcularConsumoService(RutaLoaderPort jsonRutaLoader, DistanciaGeograficaPort haversineUtil) {
        this.jsonRutaLoader = jsonRutaLoader;
        this.haversineUtil = haversineUtil;
    }

    @Override
    public ConsumoResult calcularConsumo(Dron dron, Path archivoRuta, ConsumoStrategy estrategia) {
        if (dron == null) {
            throw new IllegalArgumentException("Seleccione un dron antes de calcular el consumo.");
        }
        if (estrategia == null) {
            throw new IllegalArgumentException("Seleccione una estrategia de consumo.");
        }

        PuntoGeografico[] ruta = jsonRutaLoader.cargarRuta(archivoRuta);
        PuntoGeografico origen = ruta[0];
        PuntoGeografico destino = ruta[1];
        double distanciaKm = haversineUtil.calcularDistancia(origen, destino);
        double consumoEstimado = estrategia.calcularConsumo(distanciaKm);

        return new ConsumoResult(
                dron.getId(),
                dron.getSerial(),
                origen,
                destino,
                distanciaKm,
                consumoEstimado,
                nombreEstrategia(estrategia));
    }

    private String nombreEstrategia(ConsumoStrategy estrategia) {
        if (estrategia instanceof AhorroStrategy) {
            return "Modo Ahorro";
        }
        if (estrategia instanceof AltoRendimientoStrategy) {
            return "Modo Alto Rendimiento";
        }
        if (estrategia instanceof NormalStrategy) {
            return "Modo Normal";
        }
        return estrategia.getClass().getSimpleName();
    }
}
