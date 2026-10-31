package com.proyecto.drones.ui;

import java.io.File;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import com.proyecto.drones.aplicacion.dto.ResultadoTarea;
import com.proyecto.drones.aplicacion.dto.ConsumoResult;
import com.proyecto.drones.aplicacion.dto.weather.WeatherResult;
import com.proyecto.drones.aplicacion.puerto.entrada.ActualizarDronUseCase;
import com.proyecto.drones.aplicacion.puerto.entrada.CrearDronUseCase;
import com.proyecto.drones.aplicacion.puerto.entrada.EliminarDronUseCase;
import com.proyecto.drones.aplicacion.puerto.entrada.ListarDronUseCase;
import com.proyecto.drones.aplicacion.puerto.entrada.ListarDronesUseCase;
import com.proyecto.drones.aplicacion.puerto.entrada.ProgramarTareasUseCase;
import com.proyecto.drones.aplicacion.puerto.entrada.CalcularConsumoUseCase;
import com.proyecto.drones.aplicacion.puerto.entrada.weather.GetWeatherByCityUseCase;
import com.proyecto.drones.aplicacion.puerto.entrada.weather.WeatherSubscriptionUseCase;
import com.proyecto.drones.dominio.modelo.Agricultura;
import com.proyecto.drones.dominio.modelo.Dron;
import com.proyecto.drones.dominio.modelo.Vigilancia;
import com.proyecto.drones.dominio.modelo.command.DroneCommand;
import com.proyecto.drones.dominio.modelo.strategy.AhorroStrategy;
import com.proyecto.drones.dominio.modelo.strategy.AltoRendimientoStrategy;
import com.proyecto.drones.dominio.modelo.strategy.ConsumoStrategy;
import com.proyecto.drones.dominio.modelo.strategy.NormalStrategy;
import com.proyecto.drones.dominio.modelo.weather.Weather;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;

/**
 * Driving Adapter JavaFX de la arquitectura hexagonal.
 *
 * <p>Conserva el CRUD y expone las funcionalidades Observer + Chain, Strategy
 * y Command exclusivamente a través de casos de uso de aplicación.</p>
 */
public class DronController {

    private CrearDronUseCase crearDronUseCase;
    private ActualizarDronUseCase actualizarDronUseCase;
    private ListarDronUseCase listarDronUseCase;
    private ListarDronesUseCase listarDronesUseCase;
    private EliminarDronUseCase eliminarDronUseCase;

    private GetWeatherByCityUseCase getWeatherByCityUseCase;
    private WeatherSubscriptionUseCase weatherSubscriptionUseCase;
    private CalcularConsumoUseCase calcularConsumoUseCase;
    private ProgramarTareasUseCase programarTareasUseCase;

    private DroneCommand tomarFotografiaCommand;
    private DroneCommand escanearZonaCommand;
    private DroneCommand medirTemperaturaCommand;

    private final ObservableList<Dron> datos = FXCollections.observableArrayList();
    private final Set<String> idsSuscritos = new HashSet<>();
    private Path archivoRutaSeleccionado;

    @FXML private TextField txtId;
    @FXML private TextField txtSerial;
    @FXML private TextField txtModelo;
    @FXML private TextField txtFabricante;
    @FXML private TextField txtPeso;
    @FXML private TextField txtCapacidadTanque;
    @FXML private ComboBox<String> cmbTipo;
    @FXML private CheckBox chkDeteccionTermica;
    @FXML private TableView<Dron> tablaDrones;
    @FXML private TableColumn<Dron, String> colId;
    @FXML private TableColumn<Dron, String> colSerial;
    @FXML private TableColumn<Dron, String> colModelo;
    @FXML private TableColumn<Dron, String> colFabricante;
    @FXML private TableColumn<Dron, Number> colPeso;
    @FXML private TableColumn<Dron, String> colTipo;
    @FXML private TableColumn<Dron, String> colDetalle;
    @FXML private Label lblEstado;

    // Observer + Chain
    @FXML private ComboBox<String> cmbCiudad;
    @FXML private Label lblTemperatura;
    @FXML private Label lblHumedad;
    @FXML private Label lblViento;
    @FXML private Label lblPresion;
    @FXML private Label lblVisibilidad;
    @FXML private Label lblClimaResultado;
    @FXML private Label lblSuscripcion;

    // Strategy
    @FXML private ComboBox<String> cmbEstrategia;
    @FXML private Label lblDronRuta;
    @FXML private Label lblArchivoRuta;
    @FXML private Label lblOrigenRuta;
    @FXML private Label lblDestinoRuta;
    @FXML private Label lblDistanciaRuta;
    @FXML private Label lblEstrategiaRuta;
    @FXML private Label lblConsumoRuta;

    // Command
    @FXML private ComboBox<String> cmbTareaCommand;
    @FXML private Label lblDronMision;
    @FXML private ListView<String> listaTareasCommand;
    @FXML private TextArea txtResultadosCommand;

    /** Constructor vacío requerido por FXMLLoader. */
    public DronController() {
    }

    public void setUseCases(CrearDronUseCase crear,
            ActualizarDronUseCase actualizar,
            ListarDronUseCase listarUno,
            ListarDronesUseCase listarMuchos,
            EliminarDronUseCase eliminar) {
        this.crearDronUseCase = crear;
        this.actualizarDronUseCase = actualizar;
        this.listarDronUseCase = listarUno;
        this.listarDronesUseCase = listarMuchos;
        this.eliminarDronUseCase = eliminar;
    }

    public void setWeatherUseCases(GetWeatherByCityUseCase consultarClima,
            WeatherSubscriptionUseCase suscripciones) {
        this.getWeatherByCityUseCase = consultarClima;
        this.weatherSubscriptionUseCase = suscripciones;
    }

    public void setStrategyUseCase(CalcularConsumoUseCase calcularConsumoUseCase) {
        this.calcularConsumoUseCase = calcularConsumoUseCase;
    }

    public void setCommandUseCase(ProgramarTareasUseCase programarTareasUseCase,
            DroneCommand tomarFotografiaCommand,
            DroneCommand escanearZonaCommand,
            DroneCommand medirTemperaturaCommand) {
        this.programarTareasUseCase = programarTareasUseCase;
        this.tomarFotografiaCommand = tomarFotografiaCommand;
        this.escanearZonaCommand = escanearZonaCommand;
        this.medirTemperaturaCommand = medirTemperaturaCommand;
    }

    @FXML
    public void initialize() {
        cmbTipo.setItems(FXCollections.observableArrayList(
                Dron.TIPO_AGRICULTURA, Dron.TIPO_VIGILANCIA));
        cmbTipo.getSelectionModel().select(Dron.TIPO_AGRICULTURA);
        cmbTipo.valueProperty().addListener((obs, anterior, actual) -> actualizarCamposTipo());

        cmbCiudad.setItems(FXCollections.observableArrayList(
                "Bogotá", "Medellín", "Cali", "Barranquilla", "Cartagena"));
        cmbCiudad.getSelectionModel().select("Bogotá");

        cmbEstrategia.setItems(FXCollections.observableArrayList(
                "Modo Ahorro", "Modo Normal", "Modo Alto Rendimiento"));
        cmbEstrategia.getSelectionModel().select("Modo Normal");

        cmbTareaCommand.setItems(FXCollections.observableArrayList(
                "Tomar fotografía", "Escanear zona", "Medir temperatura"));
        cmbTareaCommand.getSelectionModel().selectFirst();

        colId.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getId()));
        colSerial.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getSerial()));
        colModelo.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getModelo()));
        colFabricante.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getFabricante()));
        colPeso.setCellValueFactory(c -> new SimpleDoubleProperty(c.getValue().getPeso()));
        colTipo.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getTipo()));
        colDetalle.setCellValueFactory(c -> new SimpleStringProperty(detalle(c.getValue())));

        tablaDrones.setItems(datos);
        tablaDrones.getSelectionModel().selectedItemProperty()
                .addListener((obs, anterior, actual) -> {
                    cargarFormulario(actual);
                    lblDronRuta.setText(actual == null ? "Dron: --"
                            : "Dron: " + actual.getSerial() + " (" + actual.getId() + ")");
                });

        actualizarCamposTipo();
        limpiarClima();
        limpiarConsumo();
        limpiarCommandVisual();
        actualizarEstadoSuscripciones();
    }

    public void cargarInicial() {
        validarUseCases();
        listarMuchos();
    }

    @FXML
    private void crear() {
        ejecutar(() -> {
            validarUseCases();
            Dron dron = construirDesdeFormulario();
            Dron creado = crearDronUseCase.crear(dron);
            limpiarFormulario();
            actualizarTabla();
            lblEstado.setText("Dron creado correctamente: " + creado.getId());
        });
    }

    @FXML
    private void listarUno() {
        ejecutar(() -> {
            validarUseCases();
            Optional<Dron> encontrado = listarDronUseCase.listarUno(txtId.getText());
            if (encontrado.isPresent()) {
                mostrarLista(List.of(encontrado.get()));
                cargarFormulario(encontrado.get());
                lblEstado.setText("Listar uno: dron encontrado.");
            } else {
                datos.clear();
                lblEstado.setText("Listar uno: no existe un dron con ese id.");
            }
        });
    }

    @FXML
    private void listarMuchos() {
        ejecutar(() -> {
            validarUseCases();
            actualizarTabla();
            lblEstado.setText("Listar muchos: " + datos.size() + " dron(es).");
        });
    }

    @FXML
    private void actualizar() {
        ejecutar(() -> {
            validarUseCases();
            Dron dron = construirDesdeFormulario();
            if (!actualizarDronUseCase.actualizar(dron)) {
                throw new IllegalArgumentException("No existe un dron con el id indicado.");
            }
            actualizarTabla();
            lblEstado.setText("Dron actualizado correctamente.");
        });
    }

    @FXML
    private void eliminar() {
        String id = txtId.getText() == null ? "" : txtId.getText().trim();
        Dron seleccionado = tablaDrones.getSelectionModel().getSelectedItem();
        if (id.isEmpty() && seleccionado != null) {
            id = seleccionado.getId();
        }
        final String idFinal = id;

        ejecutar(() -> {
            validarUseCases();
            if (idFinal.isBlank()) {
                throw new IllegalArgumentException("Seleccione un dron o ingrese su id.");
            }

            Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION,
                    "¿Desea eliminar el dron " + idFinal + "?", ButtonType.OK, ButtonType.CANCEL);
            confirmacion.setHeaderText("Confirmar eliminación");
            if (confirmacion.showAndWait().orElse(ButtonType.CANCEL) != ButtonType.OK) {
                return;
            }

            if (seleccionado != null && idFinal.equals(seleccionado.getId())
                    && idsSuscritos.contains(idFinal) && weatherSubscriptionUseCase != null) {
                weatherSubscriptionUseCase.desuscribir(seleccionado);
                idsSuscritos.remove(idFinal);
                actualizarEstadoSuscripciones();
            }

            if (!eliminarDronUseCase.eliminar(idFinal)) {
                throw new IllegalArgumentException("No existe un dron con ese id.");
            }
            limpiarFormulario();
            actualizarTabla();
            lblEstado.setText("Dron eliminado correctamente.");
        });
    }

    // ---------------- Observer + Chain ----------------

    @FXML
    private void suscribirDron() {
        ejecutar(() -> {
            validarWeatherUseCases();
            Dron dron = dronSeleccionado();
            weatherSubscriptionUseCase.suscribir(dron);
            idsSuscritos.add(dron.getId());
            actualizarEstadoSuscripciones();
            lblEstado.setText("Dron " + dron.getId() + " suscrito a notificaciones climáticas.");
        });
    }

    @FXML
    private void desuscribirDron() {
        ejecutar(() -> {
            validarWeatherUseCases();
            Dron dron = dronSeleccionado();
            weatherSubscriptionUseCase.desuscribir(dron);
            idsSuscritos.remove(dron.getId());
            actualizarEstadoSuscripciones();
            lblEstado.setText("Dron " + dron.getId() + " desuscrito de notificaciones climáticas.");
        });
    }

    @FXML
    private void consultarClima() {
        ejecutar(() -> {
            validarWeatherUseCases();
            String ciudad = cmbCiudad.getValue();
            lblClimaResultado.setText("Consultando clima...");

            WeatherResult resultado = getWeatherByCityUseCase.consultar(ciudad);
            mostrarClima(resultado);
            lblEstado.setText("Clima de " + ciudad + " consultado. Notificación enviada a "
                    + idsSuscritos.size() + " dron(es) suscrito(s).");
        });
    }

    // ---------------- Strategy ----------------

    @FXML
    private void seleccionarArchivoRuta() {
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Seleccionar archivo JSON de ruta");
        chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Archivos JSON", "*.json"));
        File archivo = chooser.showOpenDialog(tablaDrones.getScene().getWindow());
        if (archivo != null) {
            archivoRutaSeleccionado = archivo.toPath();
            lblArchivoRuta.setText(archivo.getName());
            lblEstado.setText("Ruta JSON seleccionada: " + archivo.getName());
        }
    }

    @FXML
    private void calcularConsumo() {
        ejecutar(() -> {
            validarStrategyUseCase();
            Dron dron = dronSeleccionado();
            if (archivoRutaSeleccionado == null) {
                throw new IllegalArgumentException("Seleccione primero un archivo JSON de ruta.");
            }
            ConsumoStrategy estrategia = estrategiaSeleccionada();
            ConsumoResult resultado = calcularConsumoUseCase.calcularConsumo(
                    dron, archivoRutaSeleccionado, estrategia);
            mostrarConsumo(resultado);
            lblEstado.setText("Consumo energético calculado para el dron " + dron.getSerial() + ".");
        });
    }

    private ConsumoStrategy estrategiaSeleccionada() {
        return switch (cmbEstrategia.getValue()) {
            case "Modo Ahorro" -> new AhorroStrategy();
            case "Modo Alto Rendimiento" -> new AltoRendimientoStrategy();
            case "Modo Normal" -> new NormalStrategy();
            default -> throw new IllegalArgumentException("Seleccione una estrategia de consumo.");
        };
    }

    private void mostrarConsumo(ConsumoResult resultado) {
        lblDronRuta.setText("Dron: " + resultado.dronSerial() + " (" + resultado.dronId() + ")");
        lblOrigenRuta.setText(String.format("%s (%.5f, %.5f)",
                resultado.origen().getNombre(), resultado.origen().getLatitud(), resultado.origen().getLongitud()));
        lblDestinoRuta.setText(String.format("%s (%.5f, %.5f)",
                resultado.destino().getNombre(), resultado.destino().getLatitud(), resultado.destino().getLongitud()));
        lblDistanciaRuta.setText(String.format("%.3f km", resultado.distanciaKm()));
        lblEstrategiaRuta.setText(resultado.estrategia());
        lblConsumoRuta.setText(String.format("%.3f unidades de batería", resultado.consumoEstimado()));
    }

    // ---------------- Command ----------------

    @FXML
    private void seleccionarDronMision() {
        ejecutar(() -> {
            validarCommandUseCase();
            Dron dron = dronSeleccionado();
            programarTareasUseCase.seleccionarDron(dron);
            lblDronMision.setText("Dron de misión: " + dron.getSerial() + " (" + dron.getId() + ")");
            lblEstado.setText("Dron seleccionado para la misión: " + dron.getSerial());
        });
    }

    @FXML
    private void agregarTareaCommand() {
        ejecutar(() -> {
            validarCommandUseCase();
            DroneCommand comando = comandoSeleccionado();
            programarTareasUseCase.agregarTarea(comando);
            refrescarListaTareas();
            lblEstado.setText("Tarea agregada a la misión: " + cmbTareaCommand.getValue());
        });
    }

    @FXML
    private void ejecutarTareasCommand() {
        ejecutar(() -> {
            validarCommandUseCase();
            List<ResultadoTarea> resultados = programarTareasUseCase.ejecutarTareas();
            StringBuilder texto = new StringBuilder();
            int indice = 1;
            for (ResultadoTarea resultado : resultados) {
                texto.append(indice++)
                        .append(". ")
                        .append(resultado.tarea())
                        .append(" → ")
                        .append(resultado.resultado())
                        .append(System.lineSeparator());
            }
            txtResultadosCommand.setText(texto.toString());
            lblEstado.setText("Misión ejecutada: " + resultados.size() + " tarea(s) en orden.");
        });
    }

    @FXML
    private void limpiarTareasCommand() {
        ejecutar(() -> {
            validarCommandUseCase();
            programarTareasUseCase.limpiarTareas();
            refrescarListaTareas();
            txtResultadosCommand.clear();
            lblEstado.setText("Secuencia de tareas limpiada.");
        });
    }

    private DroneCommand comandoSeleccionado() {
        return switch (cmbTareaCommand.getValue()) {
            case "Tomar fotografía" -> tomarFotografiaCommand;
            case "Escanear zona" -> escanearZonaCommand;
            case "Medir temperatura" -> medirTemperaturaCommand;
            default -> throw new IllegalArgumentException("Seleccione una tarea.");
        };
    }

    private void refrescarListaTareas() {
        listaTareasCommand.setItems(FXCollections.observableArrayList(programarTareasUseCase.listarTareas()));
    }

    // ---------------- Helpers ----------------

    private Dron dronSeleccionado() {
        Dron dron = tablaDrones.getSelectionModel().getSelectedItem();
        if (dron == null) {
            throw new IllegalArgumentException("Seleccione un dron en la tabla.");
        }
        return dron;
    }

    private void mostrarClima(WeatherResult resultado) {
        Weather weather = resultado.weather();
        lblTemperatura.setText(String.format("%.1f °C", weather.getTemperatura()));
        lblHumedad.setText(weather.getHumedad() + " %");
        lblViento.setText(String.format("%.1f km/h", weather.getVelocidadViento()));
        lblPresion.setText(weather.getPresionAtmosferica() + " hPa");
        lblVisibilidad.setText(String.format("%.1f km", weather.getVisibilidad()));
        lblClimaResultado.setText(resultado.resultadoValidacion());
    }

    private void limpiarClima() {
        lblTemperatura.setText("--");
        lblHumedad.setText("--");
        lblViento.setText("--");
        lblPresion.setText("--");
        lblVisibilidad.setText("--");
        lblClimaResultado.setText("Seleccione una ciudad y consulte el clima.");
    }

    private void limpiarConsumo() {
        lblDronRuta.setText("Dron: --");
        lblArchivoRuta.setText("Ningún archivo seleccionado");
        lblOrigenRuta.setText("--");
        lblDestinoRuta.setText("--");
        lblDistanciaRuta.setText("--");
        lblEstrategiaRuta.setText("--");
        lblConsumoRuta.setText("--");
    }

    private void limpiarCommandVisual() {
        lblDronMision.setText("Dron de misión: --");
        if (listaTareasCommand != null) {
            listaTareasCommand.getItems().clear();
        }
        if (txtResultadosCommand != null) {
            txtResultadosCommand.clear();
        }
    }

    private void actualizarEstadoSuscripciones() {
        lblSuscripcion.setText("Drones suscritos: " + idsSuscritos.size());
    }

    private void actualizarTabla() {
        mostrarLista(listarDronesUseCase.listarMuchos());
    }

    private void validarUseCases() {
        if (crearDronUseCase == null
                || actualizarDronUseCase == null
                || listarDronUseCase == null
                || listarDronesUseCase == null
                || eliminarDronUseCase == null) {
            throw new IllegalStateException("Los casos de uso CRUD aún no fueron inyectados por App.");
        }
    }

    private void validarWeatherUseCases() {
        if (getWeatherByCityUseCase == null || weatherSubscriptionUseCase == null) {
            throw new IllegalStateException("Los casos de uso de clima aún no fueron inyectados por App.");
        }
    }

    private void validarStrategyUseCase() {
        if (calcularConsumoUseCase == null) {
            throw new IllegalStateException("El caso de uso Strategy aún no fue inyectado por App.");
        }
    }

    private void validarCommandUseCase() {
        if (programarTareasUseCase == null
                || tomarFotografiaCommand == null
                || escanearZonaCommand == null
                || medirTemperaturaCommand == null) {
            throw new IllegalStateException("Command aún no fue configurado por App.");
        }
    }

    private Dron construirDesdeFormulario() {
        String tipo = cmbTipo.getValue();
        double peso = Double.parseDouble(txtPeso.getText().trim());
        if (Dron.TIPO_AGRICULTURA.equals(tipo)) {
            double capacidad = Double.parseDouble(txtCapacidadTanque.getText().trim());
            return new Agricultura(txtId.getText().trim(), txtSerial.getText().trim(),
                    txtModelo.getText().trim(), txtFabricante.getText().trim(), peso, capacidad);
        }
        return new Vigilancia(txtId.getText().trim(), txtSerial.getText().trim(),
                txtModelo.getText().trim(), txtFabricante.getText().trim(), peso,
                chkDeteccionTermica.isSelected());
    }

    private void cargarFormulario(Dron dron) {
        if (dron == null) {
            return;
        }
        txtId.setText(dron.getId());
        txtSerial.setText(dron.getSerial());
        txtModelo.setText(dron.getModelo());
        txtFabricante.setText(dron.getFabricante());
        txtPeso.setText(Double.toString(dron.getPeso()));
        cmbTipo.setValue(dron.getTipo());
        if (dron instanceof Agricultura agricultura) {
            txtCapacidadTanque.setText(Double.toString(agricultura.getCapacidadTanque()));
            chkDeteccionTermica.setSelected(false);
        } else if (dron instanceof Vigilancia vigilancia) {
            txtCapacidadTanque.clear();
            chkDeteccionTermica.setSelected(vigilancia.isDeteccionTermica());
        }
        actualizarCamposTipo();
    }

    private void actualizarCamposTipo() {
        boolean agricola = Dron.TIPO_AGRICULTURA.equals(cmbTipo.getValue());
        txtCapacidadTanque.setDisable(!agricola);
        chkDeteccionTermica.setDisable(agricola);
    }

    private String detalle(Dron dron) {
        if (dron instanceof Agricultura agricultura) {
            return "Tanque: " + agricultura.getCapacidadTanque() + " L";
        }
        if (dron instanceof Vigilancia vigilancia) {
            return vigilancia.isDeteccionTermica() ? "Térmica: sí" : "Térmica: no";
        }
        return "";
    }

    private void mostrarLista(List<Dron> drones) {
        datos.setAll(drones);
    }

    private void limpiarFormulario() {
        txtId.clear();
        txtSerial.clear();
        txtModelo.clear();
        txtFabricante.clear();
        txtPeso.clear();
        txtCapacidadTanque.clear();
        chkDeteccionTermica.setSelected(false);
    }

    private void ejecutar(Runnable operacion) {
        try {
            operacion.run();
        } catch (NumberFormatException e) {
            mostrarError("Peso y capacidad deben ser valores numéricos.");
        } catch (IllegalArgumentException | IllegalStateException e) {
            mostrarError(e.getMessage());
        } catch (RuntimeException e) {
            mostrarError("Ocurrió un error inesperado: " + e.getMessage());
        }
    }

    private void mostrarError(String mensaje) {
        lblEstado.setText(mensaje);
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Gestión de drones");
        alert.setHeaderText("No fue posible completar la operación");
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}
