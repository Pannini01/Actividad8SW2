package com.proyecto.drones.vista;

import com.proyecto.drones.aplicacion.puerto.entrada.ActualizarDronUseCase;
import com.proyecto.drones.aplicacion.puerto.entrada.CrearDronUseCase;
import com.proyecto.drones.aplicacion.puerto.entrada.EliminarDronUseCase;
import com.proyecto.drones.aplicacion.puerto.entrada.ListarDronUseCase;
import com.proyecto.drones.aplicacion.puerto.entrada.ListarDronesUseCase;
import com.proyecto.drones.aplicacion.puerto.entrada.ProgramarTareasUseCase;
import com.proyecto.drones.aplicacion.puerto.entrada.CalcularConsumoUseCase;
import com.proyecto.drones.aplicacion.puerto.entrada.weather.GetWeatherByCityUseCase;
import com.proyecto.drones.aplicacion.puerto.entrada.weather.WeatherSubscriptionUseCase;
import com.proyecto.drones.aplicacion.puerto.salida.DronRepository;
import com.proyecto.drones.aplicacion.puerto.salida.weather.GeoApiPort;
import com.proyecto.drones.aplicacion.puerto.salida.weather.WeatherApiPort;
import com.proyecto.drones.aplicacion.servicio.ActualizarDronServicio;
import com.proyecto.drones.aplicacion.servicio.CrearDronServicio;
import com.proyecto.drones.aplicacion.servicio.EliminarDronServicio;
import com.proyecto.drones.aplicacion.servicio.ListarDronServicio;
import com.proyecto.drones.aplicacion.servicio.ListarDronesServicio;
import com.proyecto.drones.aplicacion.servicio.DroneOperacionService;
import com.proyecto.drones.aplicacion.servicio.ProgramadorTareas;
import com.proyecto.drones.aplicacion.servicio.CalcularConsumoService;
import com.proyecto.drones.aplicacion.servicio.WeatherNotificationService;
import com.proyecto.drones.dominio.modelo.chain.HumidityHandler;
import com.proyecto.drones.dominio.modelo.chain.PressureHandler;
import com.proyecto.drones.dominio.modelo.chain.TemperatureHandler;
import com.proyecto.drones.dominio.modelo.chain.VisibilityHandler;
import com.proyecto.drones.dominio.modelo.chain.WeatherHandler;
import com.proyecto.drones.dominio.modelo.chain.WindHandler;
import com.proyecto.drones.dominio.modelo.command.DroneCommand;
import com.proyecto.drones.dominio.modelo.command.EscanearZonaCommand;
import com.proyecto.drones.dominio.modelo.command.MedirTemperaturaCommand;
import com.proyecto.drones.dominio.modelo.command.TomarFotografiaCommand;
import com.proyecto.drones.dominio.modelo.weather.WeatherObserver;
import com.proyecto.drones.infraestructura.JsonRutaLoader;
import com.proyecto.drones.infraestructura.persistencia.DronPostgresAdapter;
import com.proyecto.drones.infraestructura.HaversineUtil;
import com.proyecto.drones.infraestructura.weather.GeoApiAdapter;
import com.proyecto.drones.infraestructura.weather.GeoApiClient;
import com.proyecto.drones.infraestructura.weather.OpenMeteoAdapter;
import com.proyecto.drones.infraestructura.weather.OpenMeteoClient;
import com.proyecto.drones.ui.DronController;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/** Punto de entrada JavaFX y Composition Root de la aplicación. */
public class App extends Application {

    public App() {
    }

    @Override
    public void start(Stage stage) throws Exception {
        // Persistencia PostgreSQL existente.
        DronRepository dronRepository = new DronPostgresAdapter();

        CrearDronUseCase crearUC = new CrearDronServicio(dronRepository);
        ActualizarDronUseCase actualizarUC = new ActualizarDronServicio(dronRepository);
        ListarDronUseCase listarUnoUC = new ListarDronServicio(dronRepository);
        ListarDronesUseCase listarMuchosUC = new ListarDronesServicio(dronRepository);
        EliminarDronUseCase eliminarUC = new EliminarDronServicio(dronRepository);

        // Observer + Chain + APIs de clima existentes.
        GeoApiPort geoApiPort = new GeoApiAdapter(new GeoApiClient());
        WeatherApiPort weatherApiPort = new OpenMeteoAdapter(new OpenMeteoClient());
        WeatherObserver weatherObserver = new WeatherObserver();

        WeatherHandler temperatura = new TemperatureHandler();
        WeatherHandler humedad = new HumidityHandler();
        WeatherHandler viento = new WindHandler();
        WeatherHandler presion = new PressureHandler();
        WeatherHandler visibilidad = new VisibilityHandler();
        temperatura.setNext(humedad);
        humedad.setNext(viento);
        viento.setNext(presion);
        presion.setNext(visibilidad);

        WeatherNotificationService weatherService = new WeatherNotificationService(
                geoApiPort, weatherApiPort, weatherObserver, temperatura);
        GetWeatherByCityUseCase consultarClimaUC = weatherService;
        WeatherSubscriptionUseCase suscripcionClimaUC = weatherService;

        // Strategy: JSON -> Haversine -> estrategia de consumo.
        JsonRutaLoader jsonRutaLoader = new JsonRutaLoader();
        HaversineUtil haversineUtil = new HaversineUtil();
        CalcularConsumoUseCase calcularConsumoUC = new CalcularConsumoService(
                jsonRutaLoader, haversineUtil);

        // Command: comandos wrapper -> Receiver; ProgramadorTareas es el Invoker.
        DroneOperacionService droneOperacionService = new DroneOperacionService();
        DroneCommand tomarFotografiaCommand = new TomarFotografiaCommand(droneOperacionService);
        DroneCommand escanearZonaCommand = new EscanearZonaCommand(droneOperacionService);
        DroneCommand medirTemperaturaCommand = new MedirTemperaturaCommand(droneOperacionService);
        ProgramarTareasUseCase programarTareasUC = new ProgramadorTareas(droneOperacionService);

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/dron-view.fxml"));
        Parent root = loader.load();

        DronController controlador = loader.getController();
        controlador.setUseCases(crearUC, actualizarUC, listarUnoUC, listarMuchosUC, eliminarUC);
        controlador.setWeatherUseCases(consultarClimaUC, suscripcionClimaUC);
        controlador.setStrategyUseCase(calcularConsumoUC);
        controlador.setCommandUseCase(programarTareasUC,
                tomarFotografiaCommand, escanearZonaCommand, medirTemperaturaCommand);
        controlador.cargarInicial();

        Scene scene = new Scene(root, 1540, 820);
        scene.getStylesheets().add(getClass().getResource("/css/app.css").toExternalForm());
        stage.setTitle("Gestión de Drones - Hexagonal + Observer + Chain + Strategy + Command");
        stage.setMinWidth(1280);
        stage.setMinHeight(760);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
