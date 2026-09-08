-- Inserir um motorista de teste
INSERT INTO users (id, name, email, password, role)
VALUES (1, 'Guilherme Duarte', 'guilherme@teste.com', '$2a$10$e8.Z/yCq7s4QpZ7oI', 'ROLE_DRIVER')
ON CONFLICT (id) DO NOTHING;

-- Inserir um veículo associado ao motorista
INSERT INTO vehicles (id, user_id, license_plate, model, color)
VALUES (1, 1, 'BRA2E19', 'Civic Touring', 'Preto')
ON CONFLICT (id) DO NOTHING;

-- Inserir uma zona de estacionamento (tarifa: R$ 10,00/hora, 5 vagas totais, 0 ocupadas)
INSERT INTO parking_zones (id, name, hourly_rate, total_spots, occupied_spots, active)
VALUES (1, 'Zona Central - Setor A', 10.00, 5, 0, true)
ON CONFLICT (id) DO NOTHING;

-- Sincronizar os auto-incrementos do PostgreSQL após inserção com ID fixo
SELECT setval('users_id_seq', (SELECT MAX(id) FROM users));
SELECT setval('vehicles_id_seq', (SELECT MAX(id) FROM vehicles));
SELECT setval('parking_zones_id_seq', (SELECT MAX(id) FROM parking_zones));