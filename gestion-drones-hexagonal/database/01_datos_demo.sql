-- Datos mínimos para demostrar los cinco casos de uso sin implementar "crear" en la UI.
INSERT INTO piloto (id, nombre, licencia, telefono) VALUES
    ('PIL-001', 'Laura Gómez', 'LIC-1001', '3000000001'),
    ('PIL-002', 'Carlos Ruiz', 'LIC-1002', '3000000002')
ON CONFLICT (id) DO NOTHING;

INSERT INTO dron (id, serial, modelo, fabricante, peso, tipo, capacidad_tanque, deteccion_termica, piloto_id) VALUES
    ('DR-AGR-001', 'AGR-001', 'Agro X1', 'DJI', 5.20, 'AGRICULTURA', 12.50, NULL, 'PIL-001'),
    ('DR-VIG-001', 'VIG-001', 'Guardian T', 'Autel', 2.80, 'VIGILANCIA', NULL, TRUE, 'PIL-002')
ON CONFLICT (id) DO NOTHING;
