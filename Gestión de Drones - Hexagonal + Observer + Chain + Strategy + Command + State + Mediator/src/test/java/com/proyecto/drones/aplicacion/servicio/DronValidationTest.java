package com.proyecto.drones.aplicacion.servicio;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import com.proyecto.drones.aplicacion.puerto.salida.DronRepository;
import com.proyecto.drones.dominio.modelo.Agricultura;
import com.proyecto.drones.dominio.modelo.Dron;

/** Pruebas de regresión para la validación compartida de crear y actualizar. */
@Tag("unit")
class DronValidationTest {

    @Test
    void crearYActualizarReutilizanLasMismasReglasSinDuplicarlas() {
        RepositoryStub repository = new RepositoryStub();
        CrearDronServicio crear = new CrearDronServicio(repository);
        ActualizarDronServicio actualizar = new ActualizarDronServicio(repository);
        Dron dron = nuevoDron("D-VAL-1");

        assertEquals(dron, crear.crear(dron));
        assertTrue(actualizar.actualizar(dron));
        assertEquals(2, repository.operaciones);
    }

    @Test
    void crearYActualizarRechazanElMismoDronInvalido() {
        RepositoryStub repository = new RepositoryStub();
        CrearDronServicio crear = new CrearDronServicio(repository);
        ActualizarDronServicio actualizar = new ActualizarDronServicio(repository);
        Dron invalido = new Agricultura("D-VAL-2", "SER-2", "Modelo", "Fabricante", 0, 10);

        assertThrows(IllegalArgumentException.class, () -> crear.crear(invalido));
        assertThrows(IllegalArgumentException.class, () -> actualizar.actualizar(invalido));
        assertEquals(0, repository.operaciones);
    }

    private Dron nuevoDron(String id) {
        return new Agricultura(id, "SER-" + id, "Modelo", "Fabricante", 2.5, 10.0);
    }

    private static final class RepositoryStub implements DronRepository {
        private int operaciones;

        @Override
        public Dron crear(Dron dron) {
            operaciones++;
            return dron;
        }

        @Override
        public boolean actualizar(Dron dron) {
            operaciones++;
            return true;
        }

        @Override
        public Optional<Dron> listarUno(String id) {
            return Optional.empty();
        }

        @Override
        public List<Dron> listarMuchos() {
            return List.of();
        }

        @Override
        public boolean eliminar(String id) {
            return false;
        }
    }
}
