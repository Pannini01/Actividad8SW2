# Gestión de Drones - Arquitectura Hexagonal + Observer + Chain + Strategy + Command

Proyecto académico en **Java 17**, **JavaFX**, **Maven** y **PostgreSQL**. Conserva el CRUD anterior, la consulta climática con Open-Meteo, Observer y Chain of Responsibility, y agrega los patrones **Strategy** y **Command**.

## Restricción principal del modelo

Las seis clases originales permanecen **sin modificaciones**:

- `Dron`
- `Agricultura`
- `Vigilancia`
- `Piloto`
- `Sensor`
- `Mision`

Las nuevas clases del dominio se agregan únicamente en subpaquetes:

```text
dominio/modelo
├── weather     (existente)
├── chain       (existente)
├── strategy    (nuevo)
└── command     (nuevo)
```

## Strategy - consumo energético

Clases del modelo:

```text
modelo.strategy
├── PuntoGeografico
├── ConsumoStrategy
├── AhorroStrategy
├── NormalStrategy
└── AltoRendimientoStrategy
```

Flujo:

```text
Seleccionar dron
      ↓
Cargar JSON de ruta
      ↓
JsonRutaLoader
      ↓
origen + destino
      ↓
HaversineUtil
      ↓
distancia en km
      ↓
ConsumoStrategy
      ↓
Ahorro / Normal / Alto Rendimiento
      ↓
ConsumoResult
```

Fórmulas:

- Modo Ahorro: `distanciaKm * 0.5`
- Modo Normal: `distanciaKm * 1`
- Modo Alto Rendimiento: `distanciaKm * 1.5`

Archivo JSON de ejemplo incluido en:

```text
src/main/resources/rutas/ruta-ejemplo.json
```

## Command - tareas de misión

Clases del modelo:

```text
modelo.command
├── DroneCommand
├── TomarFotografiaCommand
├── EscanearZonaCommand
└── MedirTemperaturaCommand
```

Corrección importante del profesor: **los Commands funcionan únicamente como wrappers y no reciben `Dron` directamente**.

```text
ProgramadorTareas <<Invoker>>
        ↓
DroneCommand.ejecutar()
        ↑
┌────────────┬────────────┬─────────────────┐
TomarFoto    Escanear     MedirTemperatura
     \           |             /
      \          |            /
       └── DroneOperacionService ──┘
              <<Receiver>>
                    ↓
                   Dron
```

`DroneOperacionService` es el Receiver y mantiene el dron seleccionado. Los comandos concretos solo delegan:

```java
public String ejecutar() {
    return receiver.tomarFotografia();
}
```

`ProgramadorTareas` registra los comandos y los ejecuta exactamente en el orden agregado.

## Organización de paquetes para esta entrega

Regla aplicada: **las únicas subcarpetas nuevas de esta entrega están dentro de `dominio/modelo`** (`strategy` y `command`). Fuera del modelo se reutilizan paquetes existentes y no se crean subpaquetes por patrón.

Todos los servicios, sin importar el patrón o modelo al que apoyen, están directamente en:

```text
com.proyecto.drones.aplicacion.servicio
├── CrearDronServicio
├── ActualizarDronServicio
├── ListarDronServicio
├── ListarDronesServicio
├── EliminarDronServicio
├── WeatherNotificationService
├── CalcularConsumoService
├── DroneOperacionService
└── ProgramadorTareas
```

Las clases nuevas de hoy quedan así:

```text
aplicacion
├── dto
│   ├── ConsumoResult
│   └── ResultadoTarea
├── puerto/entrada
│   ├── CalcularConsumoUseCase
│   └── ProgramarTareasUseCase
├── puerto/salida
│   ├── RutaLoaderPort
│   └── DistanciaGeograficaPort
└── servicio
    ├── CalcularConsumoService
    ├── DroneOperacionService
    └── ProgramadorTareas

infraestructura
├── JsonRutaLoader
└── HaversineUtil
```

Los subpaquetes antiguos de `weather`, `chain` y persistencia se conservan porque pertenecen a entregas anteriores.

## Funcionalidades de la interfaz

La interfaz conserva el CRUD y las funciones climáticas. El panel derecho ahora tiene tres pestañas:

- **Clima**: Open-Meteo + Chain + Observer.
- **Strategy**: selección de dron, JSON de ruta, estrategia, distancia y consumo.
- **Command**: selección de dron, programación ordenada de tareas y resultados.

## Ejecutar en Eclipse

1. Descomprima el ZIP.
2. `File > Import > Maven > Existing Maven Projects`.
3. Seleccione la carpeta que contiene `pom.xml`.
4. Copie `.env.example` a `.env` y configure PostgreSQL.
5. Ejecute con Maven:

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
docs/diagramas/Diagrama_Arquitectura_Hexagonal_Strategy_Command.png
docs/diagramas/Diagrama_Clases_Dominio_Modelo_Strategy_Command.png
```

## Pruebas

Se incluyen:

```text
WeatherChainTest
StrategyConsumoTest
CommandPatternTest
JsonRutaLoaderTest
```

`CommandPatternTest` verifica explícitamente que `DroneCommand.ejecutar()` no reciba `Dron`.
