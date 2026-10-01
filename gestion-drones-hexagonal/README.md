# Gestión de Drones - Arquitectura Hexagonal

Proyecto académico en **Java 17**, **JavaFX**, **Maven** y **PostgreSQL** reorganizado con Arquitectura Hexagonal (Ports and Adapters). La estructura sigue el mismo enfoque del proyecto de referencia `examen2_Soto`: un puerto de entrada y un servicio por caso de uso, un puerto de salida para persistencia, wiring manual en `App` y una UI JavaFX tratada como adaptador de entrada.

## Reglas de esta versión

Los únicos patrones de diseño intencionales son:

- **Adapter**: `DronPostgresAdapter` implementa `DronRepository` y adapta los casos de persistencia a JDBC/PostgreSQL.
- **Singleton**: `PostgresConnection` conserva una única instancia de la conexión JDBC y la reconstruye si PostgreSQL la cierra.

No se utilizan Builder, Factory Method, Prototype, Bridge, Decorator, Composite, Facade ni Proxy.

## Cinco paquetes principales

```text
com.proyecto.drones
├── aplicacion
│   ├── puerto
│   │   ├── entrada
│   │   │   ├── CrearDronUseCase.java
│   │   │   ├── ActualizarDronUseCase.java
│   │   │   ├── ListarDronUseCase.java
│   │   │   ├── ListarDronesUseCase.java
│   │   │   └── EliminarDronUseCase.java
│   │   └── salida
│   │       └── DronRepository.java
│   └── servicio
│       ├── CrearDronServicio.java
│       ├── ActualizarDronServicio.java
│       ├── ListarDronServicio.java
│       ├── ListarDronesServicio.java
│       └── EliminarDronServicio.java
├── dominio
│   └── modelo
│       ├── Dron.java
│       ├── Sensor.java
│       ├── Mision.java
│       ├── Piloto.java
│       ├── Agricultura.java
│       └── Vigilancia.java
├── infraestructura
│   └── persistencia
│       ├── ConfiguracionEnv.java
│       ├── PostgresConnection.java
│       └── DronPostgresAdapter.java
├── ui
│   └── DronController.java
└── vista
    └── App.java
```

**Importante:** `dominio/modelo` contiene únicamente las seis clases solicitadas.

## Arquitectura y dirección de dependencias

```text
Usuario
  |
  v
JavaFX / DronController                 <- driving adapter
  |
  +--> CrearDronUseCase
  +--> ActualizarDronUseCase
  +--> ListarDronUseCase
  +--> ListarDronesUseCase
  +--> EliminarDronUseCase              <- puertos de entrada
             |
             v
Servicios de aplicación                 <- un servicio por caso de uso
             |
             v
DronRepository                          <- puerto de salida
             ^
             |
DronPostgresAdapter                     <- driven adapter / Adapter
             |
             v
PostgresConnection                      <- Singleton de Connection
             |
             v
PostgreSQL
```

El dominio no conoce JavaFX, JDBC ni PostgreSQL. Los servicios tampoco conocen la implementación concreta de persistencia: dependen de `DronRepository`.

## Casos de uso

Cada caso de uso tiene su propio puerto de entrada y su propio servicio:

1. `CrearDronUseCase` -> `CrearDronServicio`
2. `ActualizarDronUseCase` -> `ActualizarDronServicio`
3. `ListarDronUseCase` -> `ListarDronServicio`
4. `ListarDronesUseCase` -> `ListarDronesServicio`
5. `EliminarDronUseCase` -> `EliminarDronServicio`

Todos los servicios reciben un `DronRepository` por constructor.

## Wiring manual en App

`com.proyecto.drones.vista.App` es el **composition root** y es el único punto que conoce todas las zonas necesarias para ensamblar la aplicación.

El orden de composición es:

```text
1. new DronPostgresAdapter()
2. new CrearDronServicio(repository)
   new ActualizarDronServicio(repository)
   new ListarDronServicio(repository)
   new ListarDronesServicio(repository)
   new EliminarDronServicio(repository)
3. FXMLLoader carga dron-view.fxml
4. loader.getController()
5. controller.setUseCases(...)
6. controller.cargarInicial()
7. mostrar Stage
```

Esto evita que el controlador JavaFX cree repositorios, conexiones o servicios por su cuenta.

## Adaptador de entrada - JavaFX UI

`ui.DronController` es el **driving adapter**. Solo depende de los cinco puertos de entrada y recibe sus implementaciones mediante `setUseCases(...)` después de que `FXMLLoader` crea el controlador.

El método `initialize()` configura controles JavaFX, pero no consulta la base de datos porque todavía no se han inyectado dependencias. Después del wiring, `App` llama a `cargarInicial()`.

## Puerto y Adapter de salida

`DronRepository` define las operaciones que la aplicación necesita de la persistencia:

- `crear`
- `actualizar`
- `listarUno`
- `listarMuchos`
- `eliminar`

`DronPostgresAdapter implements DronRepository` traduce ese contrato a SQL y JDBC.

## Singleton de conexión

`PostgresConnection` implementa un Singleton lazy:

```text
PostgresConnection.getInstance()
        |
        v
misma instancia Singleton
        |
        v
Connection conexion
```

La clase conserva una única `Connection`. `getConnection()` verifica si está cerrada y la reconstruye cuando sea necesario.

El Adapter cierra `PreparedStatement` y `ResultSet`, pero no cierra la conexión Singleton en cada operación.

## Modelo de dominio

`Dron` es abstracta y sus dos especializaciones son:

- `Agricultura`: agrega `capacidadTanque`.
- `Vigilancia`: agrega `deteccionTermica`.

También forman parte del dominio `Sensor`, `Mision` y `Piloto`. No existe una séptima clase de modelo para representar el tipo; se usan `Dron.TIPO_AGRICULTURA` y `Dron.TIPO_VIGILANCIA`.

## Base de datos

No fue necesario cambiar el esquema por esta reorganización arquitectónica.

Para una base nueva:

1. Ejecute `database/00_crear_base.sql`.
2. Conéctese a `drones_db` en pgAdmin.
3. Ejecute `database/schema.sql`.
4. Opcional: ejecute `database/01_datos_demo.sql`.

La aplicación también permite crear drones desde la interfaz.

## Configuración

Copie `.env.example` como `.env` y configure:

```env
DB_URL=jdbc:postgresql://localhost:5432/drones_db
DB_USER=postgres
DB_PASSWORD=su_clave
```

`.env` está excluido de Git.

## Abrir en Eclipse

1. Descomprima el proyecto.
2. En Eclipse: `File > Import > Maven > Existing Maven Projects`.
3. Seleccione la carpeta que contiene `pom.xml`.
4. Si es necesario: clic derecho al proyecto > `Maven > Update Project`.
5. Configure `.env` en la raíz.
6. Ejecute `com.proyecto.drones.vista.App` como Java Application o use Maven.

Con Maven:

```bash
mvn clean javafx:run
```

## Javadoc

El código público incluye Javadoc para dominio, puertos, servicios, adaptadores, Singleton, UI y composition root.

Generación mediante Maven:

```bash
mvn javadoc:javadoc
```

También puede usar:

- Windows: `generar-javadoc.bat`
- Linux/macOS: `./generar-javadoc.sh`

La salida de Maven queda en:

```text
target/site/apidocs/index.html
```

El ZIP también incluye una copia previamente generada en:

```text
docs/javadoc/index.html
```

## Diagramas

En `docs/diagramas/` se incluyen:

- `Diagrama_Arquitectura_Hexagonal.pdf`: arquitectura actualizada con cinco puertos de entrada, cinco servicios, wiring en `App`, driving adapter JavaFX, Adapter PostgreSQL y Singleton de conexión.
- `Diagrama_Clases_Dominio.pdf`: únicamente las seis clases de `dominio/modelo`.
- `Diagramas_Arquitectura_y_Dominio.pdf`: ambos diagramas unidos.
- versiones PNG/SVG y fuentes DOT.
