package com.proyecto.drones.infraestructura;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.jupiter.api.Test;

class JsonRutaLoaderTest {
    @Test
    void cargaOrigenYDestino() throws Exception {
        Path archivo = Files.createTempFile("ruta-test", ".json");
        Files.writeString(archivo, """
                {
                  \"origen\": {\"nombre\": \"A\", \"latitud\": 4.6, \"longitud\": -74.0},
                  \"destino\": {\"nombre\": \"B\", \"latitud\": 4.7, \"longitud\": -74.1}
                }
                """);

        var ruta = new JsonRutaLoader().cargarRuta(archivo);
        assertEquals("A", ruta[0].getNombre());
        assertEquals("B", ruta[1].getNombre());
    }
}
