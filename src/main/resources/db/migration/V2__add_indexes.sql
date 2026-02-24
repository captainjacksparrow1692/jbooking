-- Индексы для поиска отелей и номеров (см. docs/SEARCH_AVAILABLE_HOTELS.md)
CREATE INDEX IF NOT EXISTS idx_bookings_room_dates
    ON bookings (room_id, check_in_date, check_out_date);
CREATE INDEX IF NOT EXISTS idx_hotels_city ON hotels (city_id);
CREATE INDEX IF NOT EXISTS idx_rooms_hotel ON rooms (hotel_id);
