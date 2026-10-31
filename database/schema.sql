-- ============================================================
-- ESQUEMA LIMPIO - ARQUITECTURA HEXAGONAL
-- PostgreSQL / pgAdmin 4
-- ============================================================

BEGIN;

CREATE TABLE IF NOT EXISTS piloto (
    id VARCHAR(36) PRIMARY KEY,
    nombre VARCHAR(120) NOT NULL,
    licencia VARCHAR(80) NOT NULL UNIQUE,
    telefono VARCHAR(30)
);

CREATE TABLE IF NOT EXISTS dron (
    id VARCHAR(36) PRIMARY KEY,
    serial VARCHAR(80) NOT NULL UNIQUE,
    modelo VARCHAR(100) NOT NULL,
    fabricante VARCHAR(100) NOT NULL,
    peso NUMERIC(10, 2) NOT NULL CHECK (peso > 0),
    tipo VARCHAR(20) NOT NULL CHECK (tipo IN ('AGRICULTURA', 'VIGILANCIA')),
    capacidad_tanque NUMERIC(10, 2),
    deteccion_termica BOOLEAN,
    piloto_id VARCHAR(36),
    CONSTRAINT ck_dron_subtipo CHECK (
        (tipo = 'AGRICULTURA' AND capacidad_tanque IS NOT NULL AND capacidad_tanque > 0 AND deteccion_termica IS NULL)
        OR
        (tipo = 'VIGILANCIA' AND capacidad_tanque IS NULL AND deteccion_termica IS NOT NULL)
    ),
    CONSTRAINT fk_dron_piloto FOREIGN KEY (piloto_id)
        REFERENCES piloto(id) ON DELETE SET NULL ON UPDATE CASCADE
);

CREATE TABLE IF NOT EXISTS sensor (
    id VARCHAR(36) PRIMARY KEY,
    tipo VARCHAR(100) NOT NULL,
    fabricante VARCHAR(100) NOT NULL,
    dron_id VARCHAR(36) NOT NULL,
    CONSTRAINT fk_sensor_dron FOREIGN KEY (dron_id)
        REFERENCES dron(id) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE IF NOT EXISTS mision (
    id VARCHAR(36) PRIMARY KEY,
    nombre VARCHAR(120) NOT NULL,
    ubicacion VARCHAR(180) NOT NULL,
    fecha DATE NOT NULL
);

CREATE TABLE IF NOT EXISTS mision_dron (
    mision_id VARCHAR(36) NOT NULL,
    dron_id VARCHAR(36) NOT NULL,
    CONSTRAINT pk_mision_dron PRIMARY KEY (mision_id, dron_id),
    CONSTRAINT fk_mision_dron_mision FOREIGN KEY (mision_id)
        REFERENCES mision(id) ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT fk_mision_dron_dron FOREIGN KEY (dron_id)
        REFERENCES dron(id) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE INDEX IF NOT EXISTS idx_dron_tipo ON dron(tipo);
CREATE INDEX IF NOT EXISTS idx_dron_piloto_id ON dron(piloto_id);
CREATE INDEX IF NOT EXISTS idx_sensor_dron_id ON sensor(dron_id);
CREATE INDEX IF NOT EXISTS idx_mision_dron_dron_id ON mision_dron(dron_id);

COMMIT;
