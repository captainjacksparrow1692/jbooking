--отели
CREATE TABLE hotels (
                        id BIGSERIAL PRIMARY KEY,
                        name VARCHAR(255) NOT NULL,
                        address TEXT,
                        rating DECIMAL(3, 2),
                        created_at TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

--комнаты
CREATE TABLE rooms (
                       id BIGSERIAL PRIMARY KEY,
                       hotel_id BIGINT,
                       room_number VARCHAR(50) NOT NULL,
                       type VARCHAR(50), -- например: DELUXE, STANDARD
                       price_per_night DECIMAL(12, 2) NOT NULL,
                       is_available BOOLEAN DEFAULT TRUE,
                       CONSTRAINT fk_rooms_hotel FOREIGN KEY (hotel_id) REFERENCES hotels (id)
);

--бронирования
CREATE TABLE bookings (
                          id BIGSERIAL PRIMARY KEY,
                          user_id BIGINT NOT NULL,
                          room_id BIGINT NOT NULL,
                          check_in_date DATE NOT NULL,
                          check_out_date DATE NOT NULL,
                          total_price DECIMAL(12, 2) NOT NULL,
                          status VARCHAR(50) NOT NULL DEFAULT 'PENDING',
                          created_at TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP,
                          updated_at TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP,

                          CONSTRAINT fk_bookings_room FOREIGN KEY (room_id) REFERENCES rooms (id)
);

--история
CREATE TABLE booking_history (
                                 id BIGSERIAL PRIMARY KEY,
                                 booking_id BIGINT NOT NULL,
                                 action_type VARCHAR(50) NOT NULL, -- CREATED, STATUS_CHANGED, etc.
                                 previous_status VARCHAR(50),
                                 new_status VARCHAR(50),
                                 comment TEXT,
                                 created_at TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP,

                                 CONSTRAINT fk_booking_history_booking
                                     FOREIGN KEY (booking_id)
                                         REFERENCES bookings (id)
                                         ON DELETE CASCADE
);

--индексы для производительности
CREATE INDEX idx_bookings_user_id ON bookings(user_id);
CREATE INDEX idx_rooms_hotel_id ON rooms(hotel_id);
CREATE INDEX idx_booking_history_booking_id ON booking_history(booking_id);