package com.proyecto.drones.infraestructura.persistencia;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Singleton de conexión JDBC a PostgreSQL.
 *
 * <p>A diferencia de una fábrica de conexiones, esta clase conserva una única
 * instancia de {@link Connection}. Si PostgreSQL cierra la conexión, el método
 * {@link #getConnection()} la reconstruye y mantiene la misma instancia del
 * Singleton.</p>
 */
public final class PostgresConnection {
    private static PostgresConnection instancia;

    private final String url;
    private final String usuario;
    private final String password;
    private Connection conexion;

    private PostgresConnection() throws SQLException {
        ConfiguracionEnv env = ConfiguracionEnv.cargar();
        this.url = env.requerido("DB_URL");
        this.usuario = env.requerido("DB_USER");
        this.password = env.requerido("DB_PASSWORD");
        this.conexion = abrirConexion();
    }

    /**
     * Retorna la única instancia del Singleton.
     *
     * @return instancia única de conexión PostgreSQL
     */
    public static synchronized PostgresConnection getInstance() {
        if (instancia == null) {
            try {
                instancia = new PostgresConnection();
            } catch (SQLException e) {
                throw new IllegalStateException("No fue posible conectar con PostgreSQL.", e);
            }
        }
        return instancia;
    }

    /**
     * Retorna la conexión persistente administrada por el Singleton.
     * Si se encuentra cerrada, crea una nueva conexión JDBC antes de retornarla.
     *
     * @return conexión JDBC activa
     * @throws SQLException cuando no es posible conectarse a PostgreSQL
     */
    public synchronized Connection getConnection() throws SQLException {
        if (conexion == null || conexion.isClosed()) {
            conexion = abrirConexion();
        }
        return conexion;
    }

    private Connection abrirConexion() throws SQLException {
        return DriverManager.getConnection(url, usuario, password);
    }
}
