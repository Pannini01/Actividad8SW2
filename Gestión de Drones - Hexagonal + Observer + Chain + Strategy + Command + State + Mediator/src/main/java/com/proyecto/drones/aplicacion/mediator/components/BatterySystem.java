package com.proyecto.drones.aplicacion.mediator.components;

/** Componente que verifica la disponibilidad energética de la operación. */
public class BatterySystem {
    private boolean bateriaDisponible;

    /** Crea el sistema con batería disponible. */
    public BatterySystem() {
        this(true);
    }

    /**
     * Crea el componente con la disponibilidad indicada, útil para pruebas.
     *
     * @param bateriaDisponible disponibilidad energética simulada
     */
    public BatterySystem(boolean bateriaDisponible) {
        this.bateriaDisponible = bateriaDisponible;
    }

    /** @return {@code true} cuando existe energía suficiente para iniciar */
    public boolean verificarBateria() {
        return bateriaDisponible;
    }

    /** Permite simular disponibilidad energética sin usar el CRUD ni persistencia. */
    public void setBateriaDisponible(boolean bateriaDisponible) {
        this.bateriaDisponible = bateriaDisponible;
    }
}
