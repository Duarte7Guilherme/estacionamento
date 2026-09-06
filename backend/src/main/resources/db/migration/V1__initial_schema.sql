-- Extensão para UUID (caso prefira IDs randômicos em vez de sequenciais, ou podemos usar BIGSERIAL)
CREATE TABLE users (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(150) NOT NULL,
    email VARCHAR(150) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(30) NOT NULL DEFAULT 'ROLE_DRIVER',
    created_at TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP NOT NULL
);

CREATE TABLE vehicles (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL,
    license_plate VARCHAR(10) NOT NULL UNIQUE,
    model VARCHAR(80) NOT NULL,
    color VARCHAR(40),
    created_at TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP NOT NULL,
    CONSTRAINT fk_vehicles_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

CREATE TABLE parking_zones (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL UNIQUE,
    hourly_rate NUMERIC(10, 2) NOT NULL,
    total_spots INT NOT NULL CHECK (total_spots > 0),
    occupied_spots INT NOT NULL DEFAULT 0 CHECK (occupied_spots >= 0),
    active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP NOT NULL,
    CONSTRAINT chk_spots_capacity CHECK (occupied_spots <= total_spots)
);

CREATE TABLE tickets (
    id BIGSERIAL PRIMARY KEY,
    vehicle_id BIGINT NOT NULL,
    zone_id BIGINT NOT NULL,
    entry_time TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    expected_exit_time TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    actual_exit_time TIMESTAMP WITHOUT TIME ZONE,
    status VARCHAR(30) NOT NULL DEFAULT 'ACTIVE',
    total_amount NUMERIC(10, 2),
    created_at TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP NOT NULL,
    CONSTRAINT fk_tickets_vehicle FOREIGN KEY (vehicle_id) REFERENCES vehicles(id),
    CONSTRAINT fk_tickets_zone FOREIGN KEY (zone_id) REFERENCES parking_zones(id)
);

CREATE TABLE payments (
    id BIGSERIAL PRIMARY KEY,
    ticket_id BIGINT NOT NULL UNIQUE,
    amount NUMERIC(10, 2) NOT NULL,
    payment_method VARCHAR(50) NOT NULL,
    status VARCHAR(30) NOT NULL DEFAULT 'PENDING',
    paid_at TIMESTAMP WITHOUT TIME ZONE,
    created_at TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP NOT NULL,
    CONSTRAINT fk_payments_ticket FOREIGN KEY (ticket_id) REFERENCES tickets(id) ON DELETE RESTRICT
);

-- Índices essenciais para consultas de alta performance
CREATE INDEX idx_tickets_vehicle_status ON tickets(vehicle_id, status);
CREATE INDEX idx_tickets_entry_time ON tickets(entry_time);
CREATE INDEX idx_vehicles_license_plate ON vehicles(license_plate);