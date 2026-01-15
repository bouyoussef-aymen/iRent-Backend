-- Create agency table
-- EPIC B2: Agency Management
CREATE TABLE agency (
    id BIGINT PRIMARY KEY IDENTITY(1,1),
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    phone_number VARCHAR(20),
    city VARCHAR(100),
    user_id BIGINT NOT NULL UNIQUE,
    created_at DATETIME2 NOT NULL DEFAULT GETDATE(),
    updated_at DATETIME2 NOT NULL DEFAULT GETDATE(),
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    INDEX idx_user_id (user_id),
    INDEX idx_email (email)
);

-- Create cars table
-- EPIC B3: Car Fleet Management
CREATE TABLE cars (
    id BIGINT PRIMARY KEY IDENTITY(1,1),
    brand VARCHAR(100) NOT NULL,
    model VARCHAR(100) NOT NULL,
    year INT NOT NULL,
    price_per_day DECIMAL(10,2) NOT NULL,
    transmission VARCHAR(50),
    fuel_type VARCHAR(50),
    seating_capacity INT,
    color VARCHAR(50),
    license_plate VARCHAR(50) UNIQUE,
    range_in_miles DECIMAL(10,2),
    description TEXT,
    is_available BIT DEFAULT 1,
    city VARCHAR(100),
    agency_id BIGINT NOT NULL,
    created_at DATETIME2 NOT NULL DEFAULT GETDATE(),
    updated_at DATETIME2 NOT NULL DEFAULT GETDATE(),
    FOREIGN KEY (agency_id) REFERENCES agency(id) ON DELETE CASCADE,
    INDEX idx_agency_id (agency_id),
    INDEX idx_is_available (is_available),
    INDEX idx_city (city)
);

-- Create car_images table for @ElementCollection imageUrls
CREATE TABLE car_images (
    car_id BIGINT NOT NULL,
    image_url VARCHAR(500) NOT NULL,
    FOREIGN KEY (car_id) REFERENCES cars(id) ON DELETE CASCADE,
    PRIMARY KEY (car_id, image_url)
);
