package com.proyecto.drones.aplicacion.mediator;

import java.util.Objects;

import com.proyecto.drones.aplicacion.mediator.components.BatterySystem;
import com.proyecto.drones.aplicacion.mediator.components.CommunicationSystem;
import com.proyecto.drones.aplicacion.mediator.components.NavigationSystem;
import com.proyecto.drones.aplicacion.mediator.components.SensorSystem;

/**
 * Concrete Mediator que centraliza la coordinación de una operación de vigilancia.
 *
 * <p>Los componentes no mantienen referencias entre sí. El coordinador controla
 * la secuencia y detiene la operación si no existe disponibilidad energética.</p>
 */
public class MissionCoordinator implements DroneMediator {
    private final NavigationSystem navigationSystem;
    private final SensorSystem sensorSystem;
    private final BatterySystem batterySystem;
    private final CommunicationSystem communicationSystem;

    /**
     * Crea el coordinador con todos los componentes participantes.
     */
    public MissionCoordinator(NavigationSystem navigationSystem,
            SensorSystem sensorSystem,
            BatterySystem batterySystem,
            CommunicationSystem communicationSystem) {
        this.navigationSystem = Objects.requireNonNull(navigationSystem);
        this.sensorSystem = Objects.requireNonNull(sensorSystem);
        this.batterySystem = Objects.requireNonNull(batterySystem);
        this.communicationSystem = Objects.requireNonNull(communicationSystem);
    }

    @Override
    public String iniciarOperacionVigilancia() {
        StringBuilder resultado = new StringBuilder("COORDINACIÓN DE MISIÓN DE VIGILANCIA\n");

        if (!batterySystem.verificarBateria()) {
            resultado.append("Batería: energía insuficiente.\n")
                    .append("MISIÓN NO AUTORIZADA: batería insuficiente.");
            return resultado.toString();
        }

        resultado.append("Batería: disponibilidad energética verificada.\n");
        resultado.append(navigationSystem.prepararRuta()).append('\n');
        resultado.append(sensorSystem.activarSensores()).append('\n');
        resultado.append(communicationSystem.establecerConexion()).append('\n');
        resultado.append("MISIÓN AUTORIZADA: operación de vigilancia lista para iniciar.");
        return resultado.toString();
    }
}
