--users
INSERT INTO users (id, first_name, last_name, email, phone_number, password) VALUES
                                                                                 (gen_random_uuid(), 'Ivan', 'Ivanov', 'ivan.ivanov@example.com', '+998901234567', 'password123'),
                                                                                 (gen_random_uuid(), 'Anna', 'Smit', 'anna.smit@example.com', '+998901234568', 'password123'),
                                                                                 (gen_random_uuid(), 'Bekzod', 'Abduvakhidov', 'bekzod.a@example.com', '+998901234569', 'password123'),
                                                                                 (gen_random_uuid(), 'Elena', 'Petrova', 'elena.p@example.com', '+998901234570', 'password123'),
                                                                                 (gen_random_uuid(), 'Dmitry', 'Sokolov', 'dmitry.s@example.com', '+998901234571', 'password123'),
                                                                                 (gen_random_uuid(), 'Nigora', 'Umarova', 'nigora.u@example.com', '+998901234572', 'password123'),
                                                                                 (gen_random_uuid(), 'Rustam', 'Karimov', 'rustam.k@example.com', '+998901234573', 'password123'),
                                                                                 (gen_random_uuid(), 'Maria', 'Volkova', 'maria.v@example.com', '+998901234574', 'password123'),
                                                                                 (gen_random_uuid(), 'Alexey', 'Morozov', 'alexey.m@example.com', '+998901234575', 'password123'),
                                                                                 (gen_random_uuid(), 'Sardor', 'Alimov', 'sardor.a@example.com', '+998901234576', 'password123'),
                                                                                 (gen_random_uuid(), 'Olga', 'Kuznetsova', 'olga.k@example.com', '+998901234577', 'password123'),
                                                                                 (gen_random_uuid(), 'Aziz', 'Tulyaganov', 'aziz.t@example.com', '+998901234578', 'password123'),
                                                                                 (gen_random_uuid(), 'Tatiana', 'Popova', 'tatiana.p@example.com', '+998901234579', 'password123'),
                                                                                 (gen_random_uuid(), 'Igor', 'Lebedev', 'igor.l@example.com', '+998901234580', 'password123'),
                                                                                 (gen_random_uuid(), 'Lola', 'Saidova', 'lola.s@example.com', '+998901234581', 'password123'),
                                                                                 (gen_random_uuid(), 'Victor', 'Kozlov', 'victor.k@example.com', '+998901234582', 'password123'),
                                                                                 (gen_random_uuid(), 'Zebo', 'Sharipova', 'zebo.sh@example.com', '+998901234583', 'password123'),
                                                                                 (gen_random_uuid(), 'Oleg', 'Novikov', 'oleg.n@example.com', '+998901234584', 'password123'),
                                                                                 (gen_random_uuid(), 'Svetlana', 'Egorova', 'svetlana.e@example.com', '+998901234585', 'password123'),
                                                                                 (gen_random_uuid(), 'Jasur', 'Khodjayev', 'jasur.kh@example.com', '+998901234586', 'password123');
--city
INSERT INTO cities (id, name, country, description, timezone) VALUES
                                                                  (gen_random_uuid(), 'Tashkent', 'Uzbekistan', 'The capital of Uzbekistan, known for its mix of modern and Soviet-era architecture.', 'Asia/Tashkent'),
                                                                  (gen_random_uuid(), 'Samarkand', 'Uzbekistan', 'One of the oldest continuously inhabited cities in Central Asia, a key point on the Silk Road.', 'Asia/Samarkand'),
                                                                  (gen_random_uuid(), 'Bukhara', 'Uzbekistan', 'A city-museum with more than 140 architectural monuments.', 'Asia/Samarkand'),
                                                                  (gen_random_uuid(), 'Dubai', 'UAE', 'Famous for luxury shopping, ultramodern architecture and a lively nightlife scene.', 'Asia/Dubai'),
                                                                  (gen_random_uuid(), 'Istanbul', 'Turkey', 'A major city in Turkey that straddles Europe and Asia across the Bosphorus Strait.', 'Europe/Istanbul'),
                                                                  (gen_random_uuid(), 'Antalya', 'Turkey', 'A Turkish resort city with a yacht-filled Old Harbor and beaches flanked by large hotels.', 'Europe/Istanbul'),
                                                                  (gen_random_uuid(), 'London', 'UK', 'The capital of England and the United Kingdom, a 21st-century city with history stretching back to Roman times.', 'Europe/London'),
                                                                  (gen_random_uuid(), 'Paris', 'France', 'A major European city and a global center for art, fashion, gastronomy and culture.', 'Europe/Paris'),
                                                                  (gen_random_uuid(), 'Rome', 'Italy', 'Italy’s capital, a sprawling, cosmopolitan city with nearly 3,000 years of globally influential art, architecture and culture.', 'Europe/Rome'),
                                                                  (gen_random_uuid(), 'Barcelona', 'Spain', 'The cosmopolitan capital of Spain’s Catalonia region, known for its art and architecture.', 'Europe/Madrid'),
                                                                  (gen_random_uuid(), 'New York', 'USA', 'Comprising 5 boroughs sitting where the Hudson River meets the Atlantic Ocean.', 'America/New_York'),
                                                                  (gen_random_uuid(), 'Tokyo', 'Japan', 'Japan’s busy capital, mixes the ultramodern and the traditional, from neon-lit skyscrapers to historic temples.', 'Asia/Tokyo'),
                                                                  (gen_random_uuid(), 'Seoul', 'South Korea', 'A huge metropolis where modern skyscrapers, high-tech subways and pop culture meet Buddhist temples.', 'Asia/Seoul'),
                                                                  (gen_random_uuid(), 'Bangkok', 'Thailand', 'Thailand’s capital, a large city known for ornate shrines and vibrant street life.', 'Asia/Bangkok'),
                                                                  (gen_random_uuid(), 'Singapore', 'Singapore', 'A sunny, tropical island city-state in Southeast Asia, off the southern tip of the Malay Peninsula.', 'Asia/Singapore'),
                                                                  (gen_random_uuid(), 'Berlin', 'Germany', 'Germany’s capital, dates to the 13th century.', 'Europe/Berlin'),
                                                                  (gen_random_uuid(), 'Prague', 'Czech Republic', 'Capital city of the Czech Republic, bisected by the Vltava River.', 'Europe/Prague'),
                                                                  (gen_random_uuid(), 'Vienna', 'Austria', 'Lies in the country’s east on the Danube River.', 'Europe/Vienna'),
                                                                  (gen_random_uuid(), 'Almaty', 'Kazakhstan', 'Kazakhstan''s largest metropolis, set in the foothills of the Trans-Ili Alatau mountains.', 'Asia/Almaty'),
                                                                  (gen_random_uuid(), 'Astana', 'Kazakhstan', 'The capital city of Kazakhstan, located on the banks of the Ishim River.', 'Asia/Almaty');

--hotels
INSERT INTO hotels (id, name, address, description, city_id, accommodation_type, brand, amenities, average_rating, reviews_count) VALUES
                                                                                                                                      (gen_random_uuid(), 'Hyatt Regency Tashkent', 'Navoi Avenue 1A', 'Luxury 5-star hotel in the heart of Tashkent.', (SELECT id FROM cities WHERE name = 'Tashkent' LIMIT 1), 'HOTEL', 'Hyatt', 'Pool, Gym, WiFi, Spa', 4.8, 1250),
                                                                                                                                      (gen_random_uuid(), 'Hilton Tashkent City', 'Islam Karimov Street 2', 'Modern hotel with a panoramic view of the city.', (SELECT id FROM cities WHERE name = 'Tashkent' LIMIT 1), 'HOTEL', 'Hilton', 'Rooftop Bar, Pool, WiFi', 4.7, 890),
                                                                                                                                      (gen_random_uuid(), 'Burj Al Arab', 'Jumeirah St', 'The world''s most luxurious hotel.', (SELECT id FROM cities WHERE name = 'Dubai' LIMIT 1), 'RESORT', 'Jumeirah', 'Private Beach, Butler Service, Helipad', 4.9, 5400),
                                                                                                                                      (gen_random_uuid(), 'Atlantis The Palm', 'Crescent Rd', 'Ocean-themed resort on the Palm Jumeirah.', (SELECT id FROM cities WHERE name = 'Dubai' LIMIT 1), 'RESORT', 'Atlantis', 'Waterpark, Aquarium, Spa', 4.6, 12000),
                                                                                                                                      (gen_random_uuid(), 'Ritz-Carlton Paris', '15 Place Vendôme', 'A legendary hotel in the center of Paris.', (SELECT id FROM cities WHERE name = 'Paris' LIMIT 1), 'HOTEL', 'Ritz-Carlton', 'Fine Dining, Garden, WiFi', 4.9, 3100),
                                                                                                                                      (gen_random_uuid(), 'The Savoy', 'Strand, London', 'An iconic luxury hotel on the North Bank of the Thames.', (SELECT id FROM cities WHERE name = 'London' LIMIT 1), 'HOTEL', 'Fairmont', 'Traditional Tea, WiFi, Gym', 4.8, 4500),
                                                                                                                                      (gen_random_uuid(), 'Registan Plaza', 'Shohruh Str. 53', 'Classic hotel near the historical center of Samarkand.', (SELECT id FROM cities WHERE name = 'Samarkand' LIMIT 1), 'HOTEL', 'Independent', 'WiFi, Breakfast, Parking', 4.2, 320),
                                                                                                                                      (gen_random_uuid(), 'Bukhara Palace', 'Navoi Str. 8', 'Grand hotel reflecting the traditional Bukhara style.', (SELECT id FROM cities WHERE name = 'Bukhara' LIMIT 1), 'HOTEL', 'Independent', 'WiFi, Pool, Traditional Courtyard', 4.1, 210),
                                                                                                                                      (gen_random_uuid(), 'Ciragan Palace Kempinski', 'Ciragan Cd. No:32', 'Ottoman Imperial Palace and Hotel on the Bosphorus.', (SELECT id FROM cities WHERE name = 'Istanbul' LIMIT 1), 'HOTEL', 'Kempinski', 'Infinity Pool, Spa, Luxury Dining', 4.8, 2800),
                                                                                                                                      (gen_random_uuid(), 'Hotel Arts Barcelona', 'Marina 19-21', 'A luxury landmark on the coast of Barcelona.', (SELECT id FROM cities WHERE name = 'Barcelona' LIMIT 1), 'HOTEL', 'Ritz-Carlton', 'Beach View, Spa, Michelin Stars', 4.7, 1950),
                                                                                                                                      (gen_random_uuid(), 'The Peninsula Tokyo', '1-8-1 Yurakucho', 'Stunning views of the Imperial Palace gardens.', (SELECT id FROM cities WHERE name = 'Tokyo' LIMIT 1), 'HOTEL', 'Peninsula', 'Gym, Luxury Cars, WiFi', 4.9, 1400),
                                                                                                                                      (gen_random_uuid(), 'Waldorf Astoria New York', '301 Park Ave', 'A world-famous landmark in Manhattan.', (SELECT id FROM cities WHERE name = 'New York' LIMIT 1), 'HOTEL', 'Hilton', 'Historical Tours, WiFi, Fine Dining', 4.5, 6200),
                                                                                                                                      (gen_random_uuid(), 'Hotel Sacher Wien', 'Philharmoniker Str. 4', 'Known for its Sacher Torte and traditional charm.', (SELECT id FROM cities WHERE name = 'Vienna' LIMIT 1), 'HOTEL', 'Independent', 'Spa, Famous Cafe, WiFi', 4.8, 2300),
                                                                                                                                      (gen_random_uuid(), 'Rixos Premium Almaty', 'Kabanbai Batyr St 506', 'Premium hotel in the center of Almaty.', (SELECT id FROM cities WHERE name = 'Almaty' LIMIT 1), 'HOTEL', 'Rixos', 'Luxury Spa, Pool, WiFi', 4.7, 1100),
                                                                                                                                      (gen_random_uuid(), 'Sheraton Astana', 'Syganak Str 60/1', 'Modern skyscraper hotel in the capital of Kazakhstan.', (SELECT id FROM cities WHERE name = 'Astana' LIMIT 1), 'HOTEL', 'Marriott', 'Business Lounge, Gym, Pool', 4.6, 750),
                                                                                                                                      (gen_random_uuid(), 'Four Seasons George V', '31 Av. George V', 'Art-deco landmark built in 1928.', (SELECT id FROM cities WHERE name = 'Paris' LIMIT 1), 'HOTEL', 'Four Seasons', 'Flower Displays, Michelin Food, Spa', 4.9, 2900),
                                                                                                                                      (gen_random_uuid(), 'Mandarin Oriental Bangkok', '48 Oriental Ave', 'Overlooking the Chao Phraya River.', (SELECT id FROM cities WHERE name = 'Bangkok' LIMIT 1), 'RESORT', 'Mandarin Oriental', 'River View, Spa, Thai Cooking Classes', 4.9, 3800),
                                                                                                                                      (gen_random_uuid(), 'Marina Bay Sands', '10 Bayfront Ave', 'Integrated resort known for its infinity pool.', (SELECT id FROM cities WHERE name = 'Singapore' LIMIT 1), 'RESORT', 'Independent', 'Infinity Pool, Casino, Mall', 4.7, 45000),
                                                                                                                                      (gen_random_uuid(), 'St. Regis Rome', 'Via Vittorio Emanuele Orlando 3', 'Palatial hotel near Termini station.', (SELECT id FROM cities WHERE name = 'Rome' LIMIT 1), 'HOTEL', 'Marriott', 'Butler Service, Fine Art, WiFi', 4.8, 1600),
                                                                                                                                      (gen_random_uuid(), 'Adlon Kempinski Berlin', 'Unter den Linden 77', 'Historic luxury hotel next to Brandenburg Gate.', (SELECT id FROM cities WHERE name = 'Berlin' LIMIT 1), 'HOTEL', 'Kempinski', 'History, Pool, Fine Dining', 4.8, 2200);

--rooms
-- Добавляем комнаты для Hyatt Regency Tashkent
INSERT INTO rooms (id, room_number, price, capacity, hotel_id, board_basis, cancellation_policy_type, room_availability_status, room_type)
VALUES
    (gen_random_uuid(), 'H-101', 150.00, 2, (SELECT id FROM hotels WHERE name = 'Hyatt Regency Tashkent'), 'BREAKFAST_INCLUDED', 'FREE_CANCELLATION', 'AVAILABLE', 'STANDARD'),
    (gen_random_uuid(), 'H-202', 250.00, 2, (SELECT id FROM hotels WHERE name = 'Hyatt Regency Tashkent'), 'BREAKFAST_INCLUDED', 'NON_REFUNDABLE', 'AVAILABLE', 'DELUXE'),
    (gen_random_uuid(), 'H-303', 500.00, 4, (SELECT id FROM hotels WHERE name = 'Hyatt Regency Tashkent'), 'ALL_INCLUSIVE', 'FREE_CANCELLATION', 'AVAILABLE', 'SUITE');

-- Добавляем комнаты для Burj Al Arab
INSERT INTO rooms (id, room_number, price, capacity, hotel_id, board_basis, cancellation_policy_type, room_availability_status, room_type)
VALUES
    (gen_random_uuid(), 'B-777', 1500.00, 2, (SELECT id FROM hotels WHERE name = 'Burj Al Arab'), 'BREAKFAST_INCLUDED', 'NON_REFUNDABLE', 'AVAILABLE', 'DELUXE'),
    (gen_random_uuid(), 'B-888', 3500.00, 3, (SELECT id FROM hotels WHERE name = 'Burj Al Arab'), 'ALL_INCLUSIVE', 'FREE_CANCELLATION', 'AVAILABLE', 'SUITE');

-- Добавляем комнаты для Hilton Tashkent City
INSERT INTO rooms (id, room_number, price, capacity, hotel_id, board_basis, cancellation_policy_type, room_availability_status, room_type)
VALUES
    (gen_random_uuid(), 'HT-501', 180.00, 2, (SELECT id FROM hotels WHERE name = 'Hilton Tashkent City'), 'BREAKFAST_INCLUDED', 'FREE_CANCELLATION', 'AVAILABLE', 'STANDARD'),
    (gen_random_uuid(), 'HT-606', 300.00, 2, (SELECT id FROM hotels WHERE name = 'Hilton Tashkent City'), 'BREAKFAST_INCLUDED', 'FREE_CANCELLATION', 'AVAILABLE', 'DELUXE');

-- Добавляем комнаты для Marina Bay Sands
INSERT INTO rooms (id, room_number, price, capacity, hotel_id, board_basis, cancellation_policy_type, room_availability_status, room_type)
VALUES
    (gen_random_uuid(), 'MBS-10', 600.00, 2, (SELECT id FROM hotels WHERE name = 'Marina Bay Sands'), 'ROOM_ONLY', 'NON_REFUNDABLE', 'AVAILABLE', 'STANDARD'),
    (gen_random_uuid(), 'MBS-50', 1200.00, 2, (SELECT id FROM hotels WHERE name = 'Marina Bay Sands'), 'BREAKFAST_INCLUDED', 'FREE_CANCELLATION', 'AVAILABLE', 'DELUXE');

-- Массовая вставка стандартных комнат для остальных отелей (для тестов)
INSERT INTO rooms (id, room_number, price, capacity, hotel_id, board_basis, cancellation_policy_type, room_availability_status, room_type)
SELECT
    gen_random_uuid(),
    'STD-' || floor(random() * 900 + 100)::text,
    100.00 + (random() * 200),
    2,
    id,
    'BREAKFAST_INCLUDED',
    'FREE_CANCELLATION',
    'AVAILABLE',
    'STANDARD'
FROM hotels
WHERE name NOT IN ('Hyatt Regency Tashkent', 'Burj Al Arab', 'Hilton Tashkent City', 'Marina Bay Sands');