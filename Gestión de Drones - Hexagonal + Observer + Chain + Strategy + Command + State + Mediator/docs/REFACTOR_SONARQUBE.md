# Refactor para reducir código duplicado en SonarQube

## Objetivo

Reducir el porcentaje de código duplicado reportado por SonarQube sin eliminar funcionalidad, sin romper la arquitectura hexagonal y sin modificar las seis entidades principales del dominio.

## Cambios realizados

### Validación CRUD

`CrearDronServicio` y `ActualizarDronServicio` contenían el mismo bloque de reglas para ID, serial, modelo, fabricante, peso y capacidad del tanque. Las reglas se trasladaron a `DronValidator`, que ahora es reutilizado por ambos servicios.

La validación/normalización de ID usada por `ListarDronServicio` y `EliminarDronServicio` también reutiliza este componente.

### Clientes HTTP

`GeoApiClient` y `OpenMeteoClient` repetían la configuración de `HttpClient`, timeouts, ejecución GET, lectura UTF-8 y tratamiento de errores. Esa infraestructura quedó centralizada en `HttpGetClient`.

Cada cliente conserva lo que sí es diferente: endpoint, parámetros y construcción de la URI.

## Elementos preservados

No se modificaron `Dron`, `Agricultura`, `Vigilancia`, `Sensor`, `Mision` ni `Piloto`. Tampoco se eliminaron clases de State, Command, Chain, Strategy o Mediator, porque forman parte de los requisitos académicos y de la demostración de los patrones.

## Validación

- Compilación completa con `javac --release 17`: OK.
- Compilación de fuentes JUnit: OK.
- Smoke test de Crear/Actualizar, State y Mediator: OK.
- Javadoc regenerado.
- Revisión estática interna: sin coincidencias normalizadas de 70 o más tokens entre archivos de producción.

El porcentaje oficial debe confirmarse repitiendo el análisis en la misma instancia de SonarQube, ya que ese valor lo calcula el servidor con su versión y configuración concretas.
