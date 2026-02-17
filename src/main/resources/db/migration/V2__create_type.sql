CREATE TABLE review_ratings (
                                review_id BIGINT NOT NULL REFERENCES hotel_reviews(id),
                                rating_type VARCHAR(50) NOT NULL,
                                score INTEGER NOT NULL,
                                PRIMARY KEY (review_id, rating_type)
);

CREATE TABLE hotel_amenities (
                                 hotel_id BIGINT NOT NULL REFERENCES hotels(id),
                                 amenity VARCHAR(50) NOT NULL
);

CREATE TABLE payments (
                          id BIGSERIAL PRIMARY KEY,
                          booking_id BIGINT UNIQUE REFERENCES bookings(id),
                          amount DECIMAL(19, 2) NOT NULL,
                          transaction_id VARCHAR(255) UNIQUE,
                          payment_type VARCHAR(50),
                          payment_status VARCHAR(50),
                          payment_date TIMESTAMP
);