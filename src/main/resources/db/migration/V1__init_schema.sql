--города
CREATE TABLE IF NOT EXISTS cities (
                                      id BIGSERIAL PRIMARY KEY,
                                      name VARCHAR(255) NOT NULL,
                                      country VARCHAR(255),
                                      description TEXT,
                                      timezone VARCHAR(50)
);

--отели
CREATE TABLE IF NOT EXISTS hotels (
                                      id BIGSERIAL PRIMARY KEY,
                                      name VARCHAR(255) NOT NULL,
                                      address VARCHAR(255),
                                      description TEXT,
                                      city_id BIGINT REFERENCES cities(id),
                                      accommodation_type VARCHAR(50),
                                      hotel_brand VARCHAR(50),
                                      average_rating DOUBLE PRECISION DEFAULT 0.0,
                                      reviews_count INTEGER DEFAULT 0
);

--удобства отеля
CREATE TABLE IF NOT EXISTS hotel_amenities (
                                               hotel_id BIGINT REFERENCES hotels(id),
                                               amenity VARCHAR(100)
);

--комнаты
DROP TABLE IF EXISTS rooms CASCADE;
CREATE TABLE rooms (
                       id BIGSERIAL PRIMARY KEY,
                       room_number VARCHAR(50),
                       price DECIMAL(19, 2) NOT NULL,
                       capacity INTEGER,
                       hotel_id BIGINT REFERENCES hotels(id),
                       board_basis VARCHAR(50),
                       cancellation_policy_type VARCHAR(50),
                       room_availability_status VARCHAR(50),
                       room_type VARCHAR(50)
);

--букинг
DROP TABLE IF EXISTS bookings CASCADE;
CREATE TABLE bookings (
                          id BIGSERIAL PRIMARY KEY,
                          room_id BIGINT REFERENCES rooms(id),
                          user_id BIGINT, -- Если нет таблицы users, оставьте пока просто BIGINT
                          check_in_date DATE NOT NULL,
                          check_out_date DATE NOT NULL,
                          hold_until TIMESTAMP,
                          booking_status VARCHAR(50),
                          created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

--история бронирований
CREATE TABLE IF NOT EXISTS booking_history (
                                               id BIGSERIAL PRIMARY KEY,
                                               booking_id BIGINT REFERENCES bookings(id),
                                               history_action_type VARCHAR(50),
                                               booking_status VARCHAR(50),
                                               action_timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                               details TEXT
);

-- Платежи
CREATE TABLE IF NOT EXISTS payments (
                                        id BIGSERIAL PRIMARY KEY,
                                        booking_id BIGINT UNIQUE REFERENCES bookings(id),
                                        amount DECIMAL(19, 2) NOT NULL,
                                        transaction_id VARCHAR(255) UNIQUE,
                                        payment_type VARCHAR(50),
                                        payment_status VARCHAR(50),
                                        payment_date TIMESTAMP
);

-- Политики отмены
CREATE TABLE IF NOT EXISTS cancellation_policy (
                                                   id BIGSERIAL PRIMARY KEY,
                                                   penalty_type VARCHAR(50),
                                                   penalty_value DECIMAL(19, 2),
                                                   days_before_cancel INTEGER
);

-- Отзывы
CREATE TABLE IF NOT EXISTS hotel_reviews (
                                             id BIGSERIAL PRIMARY KEY,
                                             hotel_id BIGINT NOT NULL REFERENCES hotels(id),
                                             user_id BIGINT NOT NULL,
                                             comment VARCHAR(1000),
                                             created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);