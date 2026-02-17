-- 1. Таблица пользователей (под Spring Security в будущем)
CREATE TABLE IF NOT EXISTS users (
                                     id BIGSERIAL PRIMARY KEY,
                                     username VARCHAR(255) NOT NULL UNIQUE,
                                     password VARCHAR(255) NOT NULL,
                                     email VARCHAR(255) NOT NULL UNIQUE,
                                     created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 2. Таблица сущностей для бронирования (например, Отели/Номера)
CREATE TABLE IF NOT EXISTS rooms (
                                     id BIGSERIAL PRIMARY KEY,
                                     name VARCHAR(255) NOT NULL,
                                     description TEXT,
                                     price_per_night DECIMAL(19, 2) NOT NULL,
                                     is_available BOOLEAN DEFAULT TRUE
);

-- 3. Таблица бронирований
CREATE TABLE IF NOT EXISTS bookings (
                                        id BIGSERIAL PRIMARY KEY,
                                        user_id BIGINT REFERENCES users(id),
                                        room_id BIGINT REFERENCES rooms(id),
                                        check_in TIMESTAMP NOT NULL,
                                        check_out TIMESTAMP NOT NULL,
                                        status VARCHAR(50) DEFAULT 'PENDING'
);

-- Пример того, как должна выглядеть таблица
CREATE TABLE booking_history (
                                 id BIGSERIAL PRIMARY KEY,
                                 booking_id BIGINT NOT NULL,
                                 action_timestamp TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP, -- Вот этой колонки не хватает
                                 message TEXT
);