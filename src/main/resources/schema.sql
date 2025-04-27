-- Location table
CREATE TABLE Location (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    unLocode VARCHAR(255) NOT NULL UNIQUE,
    name VARCHAR(255) NOT NULL
);

-- Voyage table
CREATE TABLE Voyage (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    voyage_number VARCHAR(255) NOT NULL UNIQUE
);

-- CarrierMovement table
CREATE TABLE CarrierMovement (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    arrival_location_id BIGINT NOT NULL,
    departure_location_id BIGINT NOT NULL,
    arrival_time TIMESTAMP NOT NULL,
    departure_time TIMESTAMP NOT NULL,
    voyage_id BIGINT,
    FOREIGN KEY (arrival_location_id) REFERENCES Location(id),
    FOREIGN KEY (departure_location_id) REFERENCES Location(id),
    FOREIGN KEY (voyage_id) REFERENCES Voyage(id)
);

-- Cargo table (including embedded RouteSpecification and Delivery)
CREATE TABLE Cargo (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    tracking_id VARCHAR(255) UNIQUE NOT NULL,
    origin_id BIGINT,
    -- Route Specification fields
    spec_origin_id BIGINT,
    spec_destination_id BIGINT,
    spec_arrival_deadline TIMESTAMP,
    -- Delivery fields
    transport_status VARCHAR(50),
    routing_status VARCHAR(50),
    last_known_location_id BIGINT,
    current_voyage_id BIGINT,
    last_handling_event_id BIGINT,
    last_handling_event_type VARCHAR(50),
    last_handling_event_location_id BIGINT,
    last_handling_event_voyage_id BIGINT,
    -- Foreign key constraints
    FOREIGN KEY (origin_id) REFERENCES Location(id),
    FOREIGN KEY (spec_origin_id) REFERENCES Location(id),
    FOREIGN KEY (spec_destination_id) REFERENCES Location(id),
    FOREIGN KEY (last_known_location_id) REFERENCES Location(id),
    FOREIGN KEY (current_voyage_id) REFERENCES Voyage(id),
    FOREIGN KEY (last_handling_event_location_id) REFERENCES Location(id),
    FOREIGN KEY (last_handling_event_voyage_id) REFERENCES Voyage(id)
);

-- Leg table
CREATE TABLE Leg (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    voyage_id BIGINT,
    load_location_id BIGINT NOT NULL,
    unload_location_id BIGINT NOT NULL,
    load_time TIMESTAMP NOT NULL,
    unload_time TIMESTAMP NOT NULL,
    cargo_id BIGINT,
    FOREIGN KEY (voyage_id) REFERENCES Voyage(id),
    FOREIGN KEY (load_location_id) REFERENCES Location(id),
    FOREIGN KEY (unload_location_id) REFERENCES Location(id),
    FOREIGN KEY (cargo_id) REFERENCES Cargo(id)
);

-- HandlingEvent table
CREATE TABLE HandlingEvent (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    type VARCHAR(50) NOT NULL,
    completion_time TIMESTAMP NOT NULL,
    registration_time TIMESTAMP NOT NULL,
    voyage_id BIGINT,
    location_id BIGINT NOT NULL,
    cargo_id BIGINT NOT NULL,
    FOREIGN KEY (voyage_id) REFERENCES Voyage(id),
    FOREIGN KEY (location_id) REFERENCES Location(id),
    FOREIGN KEY (cargo_id) REFERENCES Cargo(id)
);

-- Add indexes for frequently queried columns
CREATE INDEX idx_cargo_tracking_id ON Cargo(tracking_id);
CREATE INDEX idx_voyage_number ON Voyage(voyage_number);
CREATE INDEX idx_location_unlocode ON Location(unLocode);
CREATE INDEX idx_handling_event_cargo ON HandlingEvent(cargo_id);
CREATE INDEX idx_handling_event_completion ON HandlingEvent(completion_time);
CREATE INDEX idx_carrier_movement_voyage ON CarrierMovement(voyage_id);
CREATE INDEX idx_leg_cargo ON Leg(cargo_id); 