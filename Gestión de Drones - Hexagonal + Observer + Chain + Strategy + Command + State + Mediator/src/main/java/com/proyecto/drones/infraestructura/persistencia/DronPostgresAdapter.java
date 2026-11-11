package com.proyecto.drones.infraestructura.persistencia;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import com.proyecto.drones.aplicacion.puerto.salida.DronRepository;
import com.proyecto.drones.dominio.modelo.Agricultura;
import com.proyecto.drones.dominio.modelo.Dron;
import com.proyecto.drones.dominio.modelo.Vigilancia;

/**
 * Adaptador de salida para PostgreSQL.
 *
 * <p>Implementa el puerto {@link DronRepository} y traduce las operaciones
 * definidas por la aplicación a sentencias JDBC/PostgreSQL. Es el punto de
 * contacto entre la arquitectura hexagonal y la base de datos.</p>
 */
public final class DronPostgresAdapter implements DronRepository {
    private final PostgresConnection postgres;

    /** Construye el Adapter usando el Singleton PostgreSQL. */
    public DronPostgresAdapter() {
        this(PostgresConnection.getInstance());
    }

    /**
     * Permite inyectar explícitamente el Singleton de conexión.
     *
     * @param postgres administrador Singleton de la conexión
     */
    public DronPostgresAdapter(PostgresConnection postgres) {
        this.postgres = Objects.requireNonNull(postgres, "La conexión PostgreSQL es obligatoria.");
    }

    @Override
    public Dron crear(Dron dron) {
        String sql = """
                INSERT INTO dron (id, serial, modelo, fabricante, peso, tipo,
                                  capacidad_tanque, deteccion_termica)
                VALUES (?, ?, ?, ?, ?, ?, ?, ?)
                """;
        try {
            Connection c = postgres.getConnection();
            try (PreparedStatement ps = c.prepareStatement(sql)) {
                ps.setString(1, dron.getId());
                ps.setString(2, dron.getSerial());
                ps.setString(3, dron.getModelo());
                ps.setString(4, dron.getFabricante());
                ps.setDouble(5, dron.getPeso());
                ps.setString(6, dron.getTipo());
                asignarCamposEspecificos(ps, dron, 7, 8);
                ps.executeUpdate();
                return dron;
            }
        } catch (SQLException e) {
            throw errorPersistencia("crear el dron", e);
        }
    }

    @Override
    public boolean actualizar(Dron dron) {
        String sql = """
                UPDATE dron
                SET serial = ?, modelo = ?, fabricante = ?, peso = ?, tipo = ?,
                    capacidad_tanque = ?, deteccion_termica = ?
                WHERE id = ?
                """;
        try {
            Connection c = postgres.getConnection();
            try (PreparedStatement ps = c.prepareStatement(sql)) {
                ps.setString(1, dron.getSerial());
                ps.setString(2, dron.getModelo());
                ps.setString(3, dron.getFabricante());
                ps.setDouble(4, dron.getPeso());
                ps.setString(5, dron.getTipo());
                asignarCamposEspecificos(ps, dron, 6, 7);
                ps.setString(8, dron.getId());
                return ps.executeUpdate() > 0;
            }
        } catch (SQLException e) {
            throw errorPersistencia("actualizar el dron", e);
        }
    }

    @Override
    public Optional<Dron> listarUno(String id) {
        String sql = """
                SELECT id, serial, modelo, fabricante, peso, tipo,
                       capacidad_tanque, deteccion_termica
                FROM dron
                WHERE id = ?
                """;
        try {
            Connection c = postgres.getConnection();
            try (PreparedStatement ps = c.prepareStatement(sql)) {
                ps.setString(1, id);
                try (ResultSet rs = ps.executeQuery()) {
                    return rs.next() ? Optional.of(mapear(rs)) : Optional.empty();
                }
            }
        } catch (SQLException e) {
            throw errorPersistencia("listar un dron", e);
        }
    }

    @Override
    public List<Dron> listarMuchos() {
        String sql = """
                SELECT id, serial, modelo, fabricante, peso, tipo,
                       capacidad_tanque, deteccion_termica
                FROM dron
                ORDER BY serial
                """;
        try {
            Connection c = postgres.getConnection();
            try (PreparedStatement ps = c.prepareStatement(sql)) {
                return consultarLista(ps);
            }
        } catch (SQLException e) {
            throw errorPersistencia("listar los drones", e);
        }
    }

    @Override
    public boolean eliminar(String id) {
        String sql = "DELETE FROM dron WHERE id = ?";
        try {
            Connection c = postgres.getConnection();
            try (PreparedStatement ps = c.prepareStatement(sql)) {
                ps.setString(1, id);
                return ps.executeUpdate() > 0;
            }
        } catch (SQLException e) {
            throw errorPersistencia("eliminar el dron", e);
        }
    }

    private void asignarCamposEspecificos(PreparedStatement ps, Dron dron,
            int indiceCapacidad, int indiceTermica) throws SQLException {
        if (dron instanceof Agricultura agricultura) {
            ps.setDouble(indiceCapacidad, agricultura.getCapacidadTanque());
            ps.setNull(indiceTermica, Types.BOOLEAN);
        } else if (dron instanceof Vigilancia vigilancia) {
            ps.setNull(indiceCapacidad, Types.NUMERIC);
            ps.setBoolean(indiceTermica, vigilancia.isDeteccionTermica());
        } else {
            throw new IllegalArgumentException("Tipo de dron no soportado.");
        }
    }

    private List<Dron> consultarLista(PreparedStatement ps) throws SQLException {
        List<Dron> resultado = new ArrayList<>();
        try (ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                resultado.add(mapear(rs));
            }
        }
        return resultado;
    }

    private Dron mapear(ResultSet rs) throws SQLException {
        String tipo = rs.getString("tipo");
        if (Dron.TIPO_AGRICULTURA.equalsIgnoreCase(tipo)) {
            return new Agricultura(
                    rs.getString("id"),
                    rs.getString("serial"),
                    rs.getString("modelo"),
                    rs.getString("fabricante"),
                    rs.getDouble("peso"),
                    rs.getDouble("capacidad_tanque"));
        }
        if (Dron.TIPO_VIGILANCIA.equalsIgnoreCase(tipo)) {
            return new Vigilancia(
                    rs.getString("id"),
                    rs.getString("serial"),
                    rs.getString("modelo"),
                    rs.getString("fabricante"),
                    rs.getDouble("peso"),
                    rs.getBoolean("deteccion_termica"));
        }
        throw new IllegalStateException("Tipo de dron desconocido en base de datos: " + tipo);
    }

    private IllegalStateException errorPersistencia(String operacion, SQLException causa) {
        return new IllegalStateException("No fue posible " + operacion + ".", causa);
    }
}
