# Arquitectura Hexagonal - versión Strategy + Command

La aplicación conserva los cinco paquetes principales: `aplicacion`, `dominio`, `infraestructura`, `ui` y `vista`.

## Dominio

Las seis entidades originales no se modifican. Se conservan `modelo.weather` y `modelo.chain` y se agregan:

- `modelo.strategy`: `PuntoGeografico`, `ConsumoStrategy`, `AhorroStrategy`, `NormalStrategy`, `AltoRendimientoStrategy`.
- `modelo.command`: `DroneCommand`, `TomarFotografiaCommand`, `EscanearZonaCommand`, `MedirTemperaturaCommand`.

## Strategy

`CalcularConsumoService` actúa como Context del patrón Strategy. Recibe un dron seleccionado, un archivo JSON de ruta y una implementación de `ConsumoStrategy`.

`JsonRutaLoader` obtiene origen y destino. `HaversineUtil` calcula la distancia geográfica en kilómetros. La estrategia seleccionada calcula el consumo.

## Command

`DroneCommand` declara únicamente `ejecutar()` y no recibe `Dron`.

Los comandos concretos son wrappers de `DroneOperacionService`, que actúa como Receiver y conserva el dron seleccionado.

`ProgramadorTareas` es el Invoker: registra `DroneCommand` y los ejecuta en orden.

## UI

`DronController` continúa siendo el Driving Adapter JavaFX. Además de CRUD y clima, invoca `CalcularConsumoUseCase` y `ProgramarTareasUseCase`.

## Composition Root

`vista.App` crea y conecta todas las implementaciones: persistencia, clima, Chain, Observer, Strategy, loader JSON, Haversine, Receiver, Commands e Invoker.
