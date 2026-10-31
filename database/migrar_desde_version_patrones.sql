-- Migración OPCIONAL para una base creada con las actividades anteriores.
-- El nuevo código no utiliza estos campos/tablas, por lo que eliminarlos es
-- únicamente una limpieza para que la base refleje la nueva arquitectura.
BEGIN;
DROP TABLE IF EXISTS sensor_composite CASCADE;
ALTER TABLE dron DROP CONSTRAINT IF EXISTS ck_dron_modo_control;
ALTER TABLE dron DROP COLUMN IF EXISTS bateria_adicional;
ALTER TABLE dron DROP COLUMN IF EXISTS modo_control;
COMMIT;
