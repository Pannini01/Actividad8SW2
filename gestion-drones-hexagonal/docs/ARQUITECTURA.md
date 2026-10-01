# Arquitectura Hexagonal

La aplicación usa cinco paquetes principales: `aplicacion`, `dominio`, `infraestructura`, `ui` y `vista`.

## Entrada

`DronController` es el driving adapter JavaFX. Recibe cinco puertos de entrada independientes: crear, actualizar, listar uno, listar muchos y eliminar.

## Aplicación

Cada puerto de entrada tiene un servicio propio. Los servicios dependen exclusivamente de `DronRepository`.

## Salida

`DronRepository` es el puerto de salida. `DronPostgresAdapter` lo implementa con JDBC/PostgreSQL.

## Wiring

`vista.App` es el composition root. Crea el Adapter PostgreSQL, construye los cinco servicios, carga el FXML y los inyecta en `DronController`.

## Singleton

`PostgresConnection` conserva una instancia única del administrador y una única `Connection` activa. Si la conexión se cierra, se vuelve a abrir dentro de la misma abstracción Singleton.
