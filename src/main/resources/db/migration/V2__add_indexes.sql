-- 1. Индекс для проверки доступности номеров
CREATE INDEX IF NOT EXISTS idx_bookings_room_dates
    ON bookings (room_id, check_in_date, check_out_date);

-- 2. Индекс для поиска отелей по городу
CREATE INDEX IF NOT EXISTS idx_hotels_city
    ON hotels (city_id);

-- 3. Индекс для получения всех номеров конкретного отеля
CREATE INDEX IF NOT EXISTS idx_rooms_hotel
    ON rooms (hotel_id);