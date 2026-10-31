# Nueva entrega: Strategy + Command

## Strategy

- `dominio.modelo.strategy.PuntoGeografico`
- `dominio.modelo.strategy.ConsumoStrategy`
- `dominio.modelo.strategy.AhorroStrategy`
- `dominio.modelo.strategy.NormalStrategy`
- `dominio.modelo.strategy.AltoRendimientoStrategy`
- `aplicacion.puerto.entrada.strategy.CalcularConsumoUseCase`
- `aplicacion.servicio.strategy.CalcularConsumoService`
- `infraestructura.loader.JsonRutaLoader`
- `infraestructura.util.HaversineUtil`

## Command

- `dominio.modelo.command.DroneCommand`
- `dominio.modelo.command.TomarFotografiaCommand`
- `dominio.modelo.command.EscanearZonaCommand`
- `dominio.modelo.command.MedirTemperaturaCommand`
- `aplicacion.puerto.entrada.command.ProgramarTareasUseCase`
- `aplicacion.servicio.command.DroneOperacionService` (Receiver)
- `aplicacion.servicio.command.ProgramadorTareas` (Invoker)

`DroneCommand` expone únicamente `ejecutar()`; no recibe `Dron` directamente.
