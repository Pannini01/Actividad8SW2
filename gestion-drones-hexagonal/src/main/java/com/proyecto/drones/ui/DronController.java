package com.proyecto.drones.ui;

import java.util.List;
import java.util.Optional;

import com.proyecto.drones.aplicacion.puerto.entrada.ActualizarDronUseCase;
import com.proyecto.drones.aplicacion.puerto.entrada.CrearDronUseCase;
import com.proyecto.drones.aplicacion.puerto.entrada.EliminarDronUseCase;
import com.proyecto.drones.aplicacion.puerto.entrada.ListarDronUseCase;
import com.proyecto.drones.aplicacion.puerto.entrada.ListarDronesUseCase;
import com.proyecto.drones.dominio.modelo.Agricultura;
import com.proyecto.drones.dominio.modelo.Dron;
import com.proyecto.drones.dominio.modelo.Vigilancia;

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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

/**
 * Adaptador de entrada (driving adapter) de la arquitectura hexagonal.
 *
 * <p>Esta clase pertenece a la UI JavaFX y solo conoce puertos de entrada.
 * Nunca accede directamente a {@code DronRepository}, JDBC ni PostgreSQL.
 * Las dependencias se inyectan desde {@code vista.App}, que actúa como
 * composition root o punto de wiring de la aplicación.</p>
 */
public class DronController {

    private CrearDronUseCase crearDronUseCase;
    private ActualizarDronUseCase actualizarDronUseCase;
    private ListarDronUseCase listarDronUseCase;
    private ListarDronesUseCase listarDronesUseCase;
    private EliminarDronUseCase eliminarDronUseCase;

    private final ObservableList<Dron> datos = FXCollections.observableArrayList();

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

    /** Constructor vacío requerido por {@link javafx.fxml.FXMLLoader}. */
    public DronController() {
    }

    /**
     * Inyecta los casos de uso después de que JavaFX carga el FXML.
     *
     * @param crear caso de uso crear
     * @param actualizar caso de uso actualizar
     * @param listarUno caso de uso listar uno
     * @param listarMuchos caso de uso listar muchos
     * @param eliminar caso de uso eliminar
     */
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

    /**
     * Inicializa los controles visuales que no dependen de los casos de uso.
     *
     * <p>FXML ejecuta este método antes de que {@code App} pueda inyectar
     * dependencias, por eso la carga de datos inicial se realiza en
     * {@link #cargarInicial()}.</p>
     */
    @FXML
    public void initialize() {
        cmbTipo.setItems(FXCollections.observableArrayList(
                Dron.TIPO_AGRICULTURA, Dron.TIPO_VIGILANCIA));
        cmbTipo.getSelectionModel().select(Dron.TIPO_AGRICULTURA);
        cmbTipo.valueProperty().addListener((obs, anterior, actual) -> actualizarCamposTipo());

        colId.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getId()));
        colSerial.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getSerial()));
        colModelo.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getModelo()));
        colFabricante.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getFabricante()));
        colPeso.setCellValueFactory(c -> new SimpleDoubleProperty(c.getValue().getPeso()));
        colTipo.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getTipo()));
        colDetalle.setCellValueFactory(c -> new SimpleStringProperty(detalle(c.getValue())));

        tablaDrones.setItems(datos);
        tablaDrones.getSelectionModel().selectedItemProperty()
                .addListener((obs, anterior, actual) -> cargarFormulario(actual));
        actualizarCamposTipo();
    }

    /**
     * Carga la información inicial luego del wiring realizado por {@code App}.
     */
    public void cargarInicial() {
        validarUseCases();
        listarMuchos();
    }

    /** Ejecuta el caso de uso de creación. */
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

    /** Ejecuta el caso de uso de listar un único dron por id. */
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

    /** Ejecuta el caso de uso de listar todos los drones. */
    @FXML
    private void listarMuchos() {
        ejecutar(() -> {
            validarUseCases();
            actualizarTabla();
            lblEstado.setText("Listar muchos: " + datos.size() + " dron(es).");
        });
    }

    /** Ejecuta el caso de uso de actualización. */
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

    /** Ejecuta el caso de uso de eliminación. */
    @FXML
    private void eliminar() {
        String id = txtId.getText() == null ? "" : txtId.getText().trim();
        if (id.isEmpty() && tablaDrones.getSelectionModel().getSelectedItem() != null) {
            id = tablaDrones.getSelectionModel().getSelectedItem().getId();
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

            if (!eliminarDronUseCase.eliminar(idFinal)) {
                throw new IllegalArgumentException("No existe un dron con ese id.");
            }
            limpiarFormulario();
            actualizarTabla();
            lblEstado.setText("Dron eliminado correctamente.");
        });
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
            throw new IllegalStateException("Los casos de uso aún no fueron inyectados por App.");
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
