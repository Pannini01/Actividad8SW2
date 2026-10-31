SELECT id, serial, modelo, fabricante, peso, tipo, capacidad_tanque, deteccion_termica
FROM dron
ORDER BY serial;

SELECT COUNT(*) AS total_drones FROM dron;
