-- 1. Пользователи (Обязательно создаем первыми)
CREATE TABLE IF NOT EXISTS users (
                                     id BIGSERIAL PRIMARY KEY,
                                     first_name VARCHAR(255),
                                     last_name VARCHAR(255),
                                     email VARCHAR(255) UNIQUE NOT NULL,
                                     phone_number VARCHAR(50),
                                     password VARCHAR(255),
                                     role VARCHAR(50)
);

-- 2. Города
CREATE TABLE IF NOT EXISTS cities (
                                      id BIGSERIAL PRIMARY KEY,
                                      name VARCHAR(255) NOT NULL,
                                      country VARCHAR(255),
                                      description TEXT,
                                      timezone VARCHAR(50)
);

-- 3. Отели
CREATE TABLE IF NOT EXISTS hotels (
                                      id BIGSERIAL PRIMARY KEY,
                                      name VARCHAR(255) NOT NULL,
                                      address VARCHAR(255),
                                      description TEXT,
                                      city_id BIGINT REFERENCES cities(id),
                                      accommodation_type VARCHAR(50),
                                      average_rating DOUBLE PRECISION DEFAULT 0.0,
                                      reviews_count INTEGER DEFAULT 0
);

-- 4. Комнаты
CREATE TABLE IF NOT EXISTS rooms (
                                     id BIGSERIAL PRIMARY KEY,
                                     room_number VARCHAR(50),
                                     price DECIMAL(19, 2) NOT NULL,
                                     capacity INTEGER,
                                     hotel_id BIGINT REFERENCES hotels(id) ON DELETE CASCADE,
                                     board_basis VARCHAR(50),
                                     cancellation_policy_type VARCHAR(50),
                                     room_availability_status VARCHAR(50),
                                     room_type VARCHAR(50)
);

-- 5. Бронирования
CREATE TABLE IF NOT EXISTS bookings (
                                        id BIGSERIAL PRIMARY KEY,
                                        room_id BIGINT REFERENCES rooms(id),
                                        user_id BIGINT REFERENCES users(id),
                                        check_in_date DATE NOT NULL,
                                        check_out_date DATE NOT NULL,
                                        hold_until TIMESTAMP,
                                        booking_status VARCHAR(50),
                                        created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 6. Платежи
CREATE TABLE IF NOT EXISTS payments (
                                        id BIGSERIAL PRIMARY KEY,
                                        booking_id BIGINT UNIQUE REFERENCES bookings(id) ON DELETE CASCADE,
                                        amount DECIMAL(19, 2) NOT NULL,
                                        transaction_id VARCHAR(255) UNIQUE,
                                        payment_type VARCHAR(50),
                                        payment_status VARCHAR(50),
                                        payment_date TIMESTAMP
);