package com.proyecto.drones.aplicacion.puerto.salida;

import java.nio.file.Path;

import com.proyecto.drones.dominio.modelo.strategy.PuntoGeografico;

/** Puerto de salida para cargar origen y destino desde una fuente de ruta. */
public interface RutaLoaderPort {
    PuntoGeografico[] cargarRuta(Path rutaArchivo);
}
