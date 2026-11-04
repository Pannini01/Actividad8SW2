# Gestión de Drones - Arquitectura Hexagonal + Patrones GoF

Proyecto académico desarrollado en **Java 17**, **JavaFX**, **Maven** y **PostgreSQL**. Conserva las funcionalidades de entregas anteriores e incorpora en esta entrega los patrones **State** y **Mediator**, además de pruebas JUnit unitarias y de integración.

Patrones actualmente presentes:

- Observer
- Chain of Responsibility
- Strategy
- Command
- State
- Mediator

## Restricción principal del modelo

Las seis clases principales permanecen **sin modificaciones**:

- `Dron`
- `Agricultura`
- `Vigilancia`
- `Piloto`
- `Sensor`
- `Mision`

La entrega actual únicamente agrega el subpaquete `state` dentro de `dominio/modelo`. El patrón Mediator se ubica en la capa de aplicación, como exige el requerimiento.

```text
dominio/modelo
├── Dron.java                 (sin modificar)
├── Agricultura.java          (sin modificar)
├── Vigilancia.java           (sin modificar)
├── Piloto.java               (sin modificar)
├── Sensor.java               (sin modificar)
├── Mision.java               (sin modificar)
├── weather/                  (entrega anterior)
├── chain/                    (entrega anterior)
├── strategy/                 (entrega anterior)
├── command/                  (entrega anterior)
└── state/                    (NUEVO)
    ├── DroneState.java
    ├── OffState.java
    ├── ReadyState.java
    ├── FlyingState.java
    └── LandingState.java
```

## A. State - estados operativos del dron

`DroneState` funciona como **wrapper abstracto de `Dron`**. Cada estado concreto recibe el mismo `Dron` en su constructor y aplica sus propias reglas de transición, sin modificar `Dron.java` y sin crear `DroneStateContext` ni un `Context` interno.

```text
Dron
 ▲
 │ envuelve
 │
DroneState
- dron : Dron
     ▲
     ├── OffState
     ├── ReadyState
     ├── FlyingState
     └── LandingState
```

La primera vez que `DroneStateService` administra un dron crea `new OffState(dron)`, por lo que todo dron inicia en `APAGADO`.

Ciclo permitido:

```text
APAGADO --encender--> EN ESPERA --despegar--> VOLANDO
   ▲                                           │
   │                                           │ iniciar aterrizaje
   └-------------- apagar ----------------     ▼
                                           ATERRIZANDO
                                                │
                                                │ finalizar aterrizaje
                                                ▼
                                            EN ESPERA
```

Las transiciones no contempladas se rechazan dentro de cada estado concreto, sin utilizar un `if-else` o `switch` extenso para controlar el ciclo.

### Puerto y servicio State

```text
aplicacion/puerto/entrada/ManageDroneStateUseCase.java
aplicacion/servicio/DroneStateService.java
```

`DroneStateService` conserva el `DroneState` wrapper actual por id de dron y **no consulta repositorios ni PostgreSQL** para determinar el estado operativo.

### Criterios de aceptación cubiertos

1. Dron nuevo: `APAGADO`.
2. `APAGADO -> EN ESPERA` mediante encendido.
3. `EN ESPERA -> VOLANDO` mediante despegue.
4. `VOLANDO -> ATERRIZANDO` mediante inicio de aterrizaje.
5. `ATERRIZANDO -> EN ESPERA` al finalizar aterrizaje.
6. Transiciones inválidas rechazadas por cada estado.
7. JavaFX muestra el estado actualizado después de cada operación.

## B. Mediator - coordinación de operación de vigilancia

Mediator se encuentra en la capa de aplicación:

```text
aplicacion
├── mediator/
│   ├── DroneMediator.java
│   ├── MissionCoordinator.java
│   └── components/
│       ├── NavigationSystem.java
│       ├── SensorSystem.java
│       ├── BatterySystem.java
│       └── CommunicationSystem.java
├── puerto/entrada/
│   └── ExecuteMissionUseCase.java
└── servicio/
    └── MissionService.java
```

`MissionCoordinator` es el **Concrete Mediator**. Los componentes no se conocen entre sí y no contienen referencias mutuas.

```text
DronController
      ↓
ExecuteMissionUseCase
      ↓
MissionService
      ↓
DroneMediator
      ↓
MissionCoordinator
   ├── BatterySystem
   ├── NavigationSystem
   ├── SensorSystem
   └── CommunicationSystem
```

La coordinación verifica primero la disponibilidad energética. Si no hay batería suficiente, la misión se detiene. Cuando existe energía, el mediador prepara ruta, activa sensores, establece comunicación y autoriza la operación.

**Restricción cumplida:** esta funcionalidad no utiliza CRUD de drones, `DronRepository`, `DronPostgresAdapter` ni PostgreSQL.

## Funcionalidades anteriores conservadas

### Observer + Chain

- Consulta de clima mediante Open-Meteo.
- Suscripción/desuscripción de drones.
- Validación de temperatura, humedad, viento, presión y visibilidad.
- Resultado `APTO / NO APTO` y motivo del primer rechazo.

### Strategy

- Carga de origen/destino desde JSON.
- Distancia Haversine.
- `AhorroStrategy`: `km * 0.5`.
- `NormalStrategy`: `km * 1`.
- `AltoRendimientoStrategy`: `km * 1.5`.

### Command

- `TomarFotografiaCommand`.
- `EscanearZonaCommand`.
- `MedirTemperaturaCommand`.
- Los Commands son wrappers: `ejecutar()` no recibe `Dron` directamente.
- `DroneOperacionService` funciona como Receiver.
- `ProgramadorTareas` funciona como Invoker.

## Interfaz JavaFX

La interfaz conserva el CRUD y agrega pestañas funcionales para:

- Clima
- Strategy
- Command
- **State**
- **Mediator**

En State se selecciona un dron de la tabla y se pueden ejecutar las operaciones del ciclo operativo. En Mediator también se selecciona previamente un dron de la tabla; la coordinación continúa siendo independiente de las operaciones CRUD y, al final de la bitácora, la interfaz indica el serial, ID y tipo del dron asociado a la demostración.

Todos los botones de la interfaz JavaFX utilizan peso de fuente normal (sin negrilla), independientemente del patrón o funcionalidad a la que pertenezcan.

## Pruebas JUnit

### Pruebas unitarias

Las clases terminadas en `Test` se ejecutan con Surefire:

```text
DroneStateTest
MissionCoordinatorTest
WeatherChainTest
StrategyConsumoTest
CommandPatternTest
JsonRutaLoaderTest
```

Ejecutar:

```bash
mvn test
```

### Pruebas de integración

Las clases terminadas en `IT` se ejecutan mediante Maven Failsafe durante `verify`:

```text
DroneStateServiceIT
MissionServiceIT
```

Estas pruebas integran los puertos de entrada con los servicios y las implementaciones reales de State/Mediator, sin requerir PostgreSQL ni servicios web.

Ejecutar:

```bash
mvn verify
```

## Javadoc

El proyecto incluye comentarios Javadoc y documentación generada en:

```text
docs/javadoc/index.html
```

Para regenerarla con Maven:

```bash
mvn javadoc:javadoc
```

## Ejecutar en Eclipse

1. Descomprima el proyecto.
2. `File > Import > Maven > Existing Maven Projects`.
3. Seleccione la carpeta que contiene `pom.xml`.
4. Configure `.env` para PostgreSQL.
5. Ejecute:

```bash
mvn clean javafx:run
```

## Configuración PostgreSQL

```env
DB_URL=jdbc:postgresql://localhost:5432/drones_db
DB_USER=postgres
DB_PASSWORD=su_clave
```


## Diagramas actualizados

```text
docs/diagramas/Diagrama_Arquitectura_Hexagonal_State_Mediator.png
docs/diagramas/Diagrama_Clases_Dominio_Modelo_State_Mediator.png
```

## Arquitectura

- `App`: Composition Root.
- `DronController`: Driving Adapter JavaFX.
- `aplicacion/puerto/entrada`: contratos de entrada.
- `aplicacion/servicio`: todos los servicios de aplicación.
- `aplicacion/mediator`: Mediator y componentes coordinados.
- `dominio/modelo`: entidades y patrones de dominio.
- `infraestructura`: adaptadores externos y persistencia.

El dominio continúa sin dependencias hacia JavaFX, controladores o infraestructura.
