package com.proyecto.drones.vista;

import com.proyecto.drones.aplicacion.puerto.entrada.ActualizarDronUseCase;
import com.proyecto.drones.aplicacion.puerto.entrada.CrearDronUseCase;
import com.proyecto.drones.aplicacion.puerto.entrada.EliminarDronUseCase;
import com.proyecto.drones.aplicacion.puerto.entrada.ListarDronUseCase;
import com.proyecto.drones.aplicacion.puerto.entrada.ListarDronesUseCase;
import com.proyecto.drones.aplicacion.puerto.salida.DronRepository;
import com.proyecto.drones.aplicacion.servicio.ActualizarDronServicio;
import com.proyecto.drones.aplicacion.servicio.CrearDronServicio;
import com.proyecto.drones.aplicacion.servicio.EliminarDronServicio;
import com.proyecto.drones.aplicacion.servicio.ListarDronServicio;
import com.proyecto.drones.aplicacion.servicio.ListarDronesServicio;
import com.proyecto.drones.infraestructura.persistencia.DronPostgresAdapter;
import com.proyecto.drones.ui.DronController;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Punto de entrada JavaFX y composition root de la aplicación.
 *
 * <p>Al igual que en una arquitectura hexagonal estricta, esta es la única
 * clase que conoce simultáneamente el adaptador de salida, los servicios de
 * aplicación y el adaptador de entrada JavaFX. Aquí se realiza el wiring
 * manual de dependencias.</p>
 */
public class App extends Application {

    /** Construye la aplicación JavaFX. */
    public App() {
    }

    @Override
    public void start(Stage stage) throws Exception {
        // 1. Adaptador de salida: PostgreSQL implementa el puerto de persistencia.
        DronRepository dronRepository = new DronPostgresAdapter();

        // 2. Servicios: cada caso de uso recibe únicamente el puerto de salida.
        CrearDronUseCase crearUC = new CrearDronServicio(dronRepository);
        ActualizarDronUseCase actualizarUC = new ActualizarDronServicio(dronRepository);
        ListarDronUseCase listarUnoUC = new ListarDronServicio(dronRepository);
        ListarDronesUseCase listarMuchosUC = new ListarDronesServicio(dronRepository);
        EliminarDronUseCase eliminarUC = new EliminarDronServicio(dronRepository);

        // 3. Cargar el adaptador de entrada JavaFX definido por el FXML.
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/dron-view.fxml"));
        Parent root = loader.load();

        // 4. Inyectar los puertos de entrada en el driving adapter.
        DronController controlador = loader.getController();
        controlador.setUseCases(crearUC, actualizarUC, listarUnoUC, listarMuchosUC, eliminarUC);
        controlador.cargarInicial();

        // 5. Mostrar la vista.
        Scene scene = new Scene(root, 1080, 680);
        scene.getStylesheets().add(getClass().getResource("/css/app.css").toExternalForm());
        stage.setTitle("Gestión de Drones - Arquitectura Hexagonal");
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Inicia JavaFX.
     *
     * @param args argumentos de línea de comandos
     */
    public static void main(String[] args) {
        launch(args);
    }
}
