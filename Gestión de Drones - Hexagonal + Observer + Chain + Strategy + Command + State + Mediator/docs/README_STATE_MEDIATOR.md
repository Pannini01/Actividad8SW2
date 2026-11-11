# Entrega State + Mediator

## State

La entidad `Dron` no se modifica. `DroneState` es ahora una **clase abstracta wrapper** que recibe directamente un `Dron`.

```text
Dron
 ▲
 │ envuelve
DroneState
 ├── OffState
 ├── ReadyState
 ├── FlyingState
 └── LandingState
```

- `OffState`: APAGADO; permite encender.
- `ReadyState`: EN ESPERA; permite despegar o apagar.
- `FlyingState`: VOLANDO; permite iniciar aterrizaje.
- `LandingState`: ATERRIZANDO; permite finalizar aterrizaje.
- No existe `DroneStateContext`.
- No existe `Context` interno.
- Las operaciones inválidas conservan el estado actual y devuelven el motivo.
- `DroneStateService` conserva el wrapper activo por id de dron.

## Mediator

Mediator permanece en la capa de aplicación:

```text
aplicacion/mediator
├── DroneMediator
├── MissionCoordinator
└── components
    ├── NavigationSystem
    ├── SensorSystem
    ├── BatterySystem
    └── CommunicationSystem
```

`MissionCoordinator` centraliza la coordinación. Los componentes no se comunican directamente entre sí y la demostración no usa CRUD de drones ni PostgreSQL.

## Pruebas

Unitarias:

- `DroneStateTest`
- `MissionCoordinatorTest`

Integración:

- `DroneStateServiceIT`
- `MissionServiceIT`

`mvn test` ejecuta pruebas unitarias y `mvn verify` incluye las pruebas de integración configuradas con Failsafe.

## Ajuste de interfaz y visualización de Mediator

- Todos los botones JavaFX se muestran con peso de fuente normal, sin negrilla.
- Para ejecutar la demostración Mediator se requiere seleccionar un dron de la tabla.
- El dron seleccionado se usa únicamente como contexto visual de la operación; el Mediator no consulta repositorios ni ejecuta operaciones CRUD.
- Al final del resultado de coordinación se muestran serial, ID y tipo del dron seleccionado.
