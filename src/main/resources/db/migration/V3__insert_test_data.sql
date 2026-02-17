-- ==========================================================
-- 1. НАПОЛНЕНИЕ ГОРОДОВ (УЗБЕКИСТАН)
-- ==========================================================

INSERT INTO cities (name, country, description, timezone) VALUES
                                                              ('Ташкент', 'Узбекистан', 'Столица и крупнейший мегаполис.', 'Asia/Tashkent'),
                                                              ('Самарканд', 'Узбекистан', 'Исторический центр Великого шелкового пути.', 'Asia/Tashkent'),
                                                              ('Бухара', 'Узбекистан', 'Древний город с уникальной исламской архитектурой.', 'Asia/Tashkent'),
                                                              ('Хива', 'Узбекистан', 'Город-крепость Ичан-Кала.', 'Asia/Tashkent'),
                                                              ('Нукус', 'Узбекистан', 'Центр искусств и ворот к Аральскому морю.', 'Asia/Tashkent');

-- ==========================================================
-- 2. НАПОЛНЕНИЕ ОТЕЛЕЙ
-- ==========================================================

INSERT INTO hotels (name, address, description, city_id, accommodation_type, hotel_brand, average_rating, reviews_count) VALUES
                                                                                                                             ('Hyatt Regency Tashkent', 'Улица Навои, 1А', 'Премиальный отель в центре.', (SELECT id FROM cities WHERE name = 'Ташкент'), 'HOTEL', 'CHAIN', 4.8, 120),
                                                                                                                             ('Hilton Tashkent City', 'Tashkent City Block 1', 'Современный отель бизнес-класса.', (SELECT id FROM cities WHERE name = 'Ташкент'), 'HOTEL', 'CHAIN', 4.9, 85),
                                                                                                                             ('Registan Plaza', 'Улица Шохрух, 53', 'Отель рядом с площадью Регистан.', (SELECT id FROM cities WHERE name = 'Самарканд'), 'HOTEL', 'INDEPENDENT', 4.5, 95),
                                                                                                                             ('Savitsky Plaza', 'Silk Road Samarkand', 'Арт-отель в новом комплексе.', (SELECT id FROM cities WHERE name = 'Самарканд'), 'HOTEL', 'INDEPENDENT', 4.7, 42),
                                                                                                                             ('Boutique Hotel Minzifa', 'Старый город', 'Традиционный узбекский стиль.', (SELECT id FROM cities WHERE name = 'Бухара'), 'HOTEL', 'INDEPENDENT', 4.9, 110);

-- ==========================================================
-- 3. УДОБСТВА ОТЕЛЕЙ (Set<Amenity>)
-- ==========================================================

INSERT INTO hotel_amenities (hotel_id, amenity) VALUES
                                                    ((SELECT id FROM hotels WHERE name = 'Hyatt Regency Tashkent'), 'POOL'),
                                                    ((SELECT id FROM hotels WHERE name = 'Hyatt Regency Tashkent'), 'SPA'),
                                                    ((SELECT id FROM hotels WHERE name = 'Hyatt Regency Tashkent'), 'GYM'),
                                                    ((SELECT id FROM hotels WHERE name = 'Registan Plaza'), 'WIFI'),
                                                    ((SELECT id FROM hotels WHERE name = 'Registan Plaza'), 'RESTAURANT');

-- ==========================================================
-- 4. НОМЕРА (ROOMS)
-- ==========================================================

INSERT INTO rooms (room_number, price, capacity, hotel_id, board_basis, cancellation_policy_type, room_availability_status, room_type) VALUES
                                                                                                                                           ('101', 1500000.00, 2, (SELECT id FROM hotels WHERE name = 'Hyatt Regency Tashkent'), 'BB', 'FREE_CANCELLATION', 'AVAILABLE', 'STANDARD'),
                                                                                                                                           ('102', 1500000.00, 2, (SELECT id FROM hotels WHERE name = 'Hyatt Regency Tashkent'), 'BB', 'FREE_CANCELLATION', 'AVAILABLE', 'STANDARD'),
                                                                                                                                           ('505', 2800000.00, 2, (SELECT id FROM hotels WHERE name = 'Hilton Tashkent City'), 'HB', 'NON_REFUNDABLE', 'AVAILABLE', 'DELUXE'),
                                                                                                                                           ('201', 900000.00, 2, (SELECT id FROM hotels WHERE name = 'Registan Plaza'), 'BB', 'FREE_CANCELLATION', 'AVAILABLE', 'STANDARD');

-- ==========================================================
-- 5. БРОНИРОВАНИЯ (BOOKINGS)
-- ==========================================================

-- Создаем бронь на 101 номер в Ташкенте (Подтверждено)
INSERT INTO bookings (room_id, user_id, check_in_date, check_out_date, booking_status, created_at) VALUES
    ((SELECT id FROM rooms WHERE room_number = '101' AND hotel_id = (SELECT id FROM hotels WHERE name = 'Hyatt Regency Tashkent')),
     1001, '2026-03-01', '2026-03-05', 'CONFIRMED', CURRENT_TIMESTAMP);

-- Создаем временное удержание (HOLD) на номер в Hilton
INSERT INTO bookings (room_id, user_id, check_in_date, check_out_date, hold_until, booking_status, created_at) VALUES
    ((SELECT id FROM rooms WHERE room_number = '505'),
     1002, '2026-03-10', '2026-03-12', CURRENT_TIMESTAMP + INTERVAL '15 minutes', 'HOLD', CURRENT_TIMESTAMP);

-- ==========================================================
-- 6. ИСТОРИЯ И ОПЛАТА
-- ==========================================================

-- История для первого бронирования
INSERT INTO booking_history (booking_id, history_action_type, booking_status, details) VALUES
    ((SELECT id FROM bookings WHERE user_id = 1001), 'CREATE', 'CONFIRMED', 'Бронирование создано через веб-интерфейс');

-- Имитация успешного платежа для брони в Самарканде (если бы она была PAID)
INSERT INTO payments (booking_id, amount, transaction_id, payment_type, payment_status, payment_date) VALUES
    ((SELECT id FROM bookings WHERE user_id = 1001), 6000000.00, 'TXN-ABC-123', 'PREPAYMENT', 'SUCCESS', CURRENT_TIMESTAMP);