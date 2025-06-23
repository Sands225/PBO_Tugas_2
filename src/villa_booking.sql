/* Cara membuat db dari terminal: sqlite3 vbook.db < villa_booking.sql */

CREATE TABLE `villas` (
  `id` INTEGER PRIMARY KEY,
  `name` TEXT NOT NULL,
  `description` text NOT NULL,
  `address` text NOT NULL
);

CREATE TABLE `room_types` (
  `id` INTEGER PRIMARY KEY,
  `villa` INTEGER NOT NULL,
  `name` TEXT NOT NULL,
  `quantity` INTEGER DEFAULT 1,
  `capacity` INTEGER DEFAULT 1,
  `price` INTEGER NOT NULL,
  `bed_size` TEXT NOT NULL /* hanya bernilai: double, queen, king */,
  `has_desk` INTEGER DEFAULT 0 /* hanya bernilai 0 dan 1 (boolean) */,
  `has_ac` INTEGER DEFAULT 0 /* hanya bernilai 0 dan 1 (boolean) */,
  `has_tv` INTEGER DEFAULT 0 /* hanya bernilai 0 dan 1 (boolean) */,
  `has_wifi` INTEGER DEFAULT 0 /* hanya bernilai 0 dan 1 (boolean) */,
  `has_shower` INTEGER DEFAULT 0 /* hanya bernilai 0 dan 1 (boolean) */,
  `has_hotwater` INTEGER DEFAULT 0 /* hanya bernilai 0 dan 1 (boolean) */,
  `has_fridge` INTEGER DEFAULT 0 /* hanya bernilai 0 dan 1 (boolean) */
);

CREATE TABLE `customers` (
  `id` INTEGER PRIMARY KEY,
  `name` TEXT NOT NULL,
  `email` TEXT NOT NULL,
  `phone` TEXT
);

CREATE TABLE `bookings` (
  `id` INTEGER PRIMARY KEY,
  `customer` INTEGER,
  `room_type` INTEGER,
  `checkin_date` TEXT NOT NULL, /* timestamp dalam format YYYY-MM-DD hh:mm:ss */
  `checkout_date` TEXT NOT NULL, /* timestamp dalam format YYYY-MM-DD hh:mm:ss */
  `price` INTEGER,
  `voucher` INTEGER,
  `final_price` INTEGER,
  `payment_status` TEXT DEFAULT 'waiting' /* hanya bernilai: waiting, failed, success */,
  `has_checkedin` INTEGER DEFAULT 0 /* hanya bernilai 0 dan 1 (boolean) */,
  `has_checkedout` INTEGER DEFAULT 0 /* hanya bernilai 0 dan 1 (boolean) */
);

CREATE TABLE `reviews` (
  `booking` INTEGER PRIMARY KEY,
  `star` INTEGER NOT NULL,
  `title` TEXT NOT NULL,
  `content` TEXT NOT NULL
);

CREATE TABLE `vouchers` (
  `id` INTEGER PRIMARY KEY,
  `code` TEXT NOT NULL,
  `description` TEXT NOT NULL,
  `discount` REAL NOT NULL,
  `start_date` TEXT NOT NULL, /* timestamp dalam format YYYY-MM-DD hh:mm:ss */
  `end_date` TEXT NOT NULL /* timestamp dalam format YYYY-MM-DD hh:mm:ss */
);

-- INSERT DATA
INSERT INTO villas (id, name, description, address) VALUES
(1, 'Villa Ubud', 'Cozy jungle retreat in Ubud', 'Jl. Monkey Forest, Ubud, Bali'),
(2, 'Villa Seminyak', 'Modern beachfront villa', 'Jl. Dhyana Pura, Seminyak, Bali'),
(3, 'Villa Canggu', 'Stylish surfside villa', 'Jl. Batu Bolong, Canggu, Bali'),
(4, 'Villa Sanur', 'Relaxing stay by the beach', 'Jl. Danau Tamblingan, Sanur, Bali'),
(5, 'Villa Jimbaran', 'Luxury villa near seafood spots', 'Jl. Uluwatu, Jimbaran, Bali'),
(6, 'Villa Nusa Dua', 'Elegant villa with pool', 'Jl. Pantai Mengiat, Nusa Dua, Bali'),
(7, 'Villa Uluwatu', 'Clifftop views with sunset', 'Jl. Labuan Sait, Uluwatu, Bali'),
(8, 'Villa Denpasar', 'Urban villa in city center', 'Jl. Teuku Umar, Denpasar, Bali'),
(9, 'Villa Lovina', 'North Bali beachfront villa', 'Jl. Seririt, Lovina, Bali'),
(10, 'Villa Amed', 'Tranquil villa near diving spots', 'Jl. I Ketut Natih, Amed, Bali');

INSERT INTO room_types (id, villa, name, quantity, capacity, price, bed_size, has_desk, has_ac, has_tv, has_wifi, has_shower, has_hotwater, has_fridge) VALUES
(1, 6, 'Family Room Garden View', 3, 4, 1000000, 'king', 1, 1, 1, 1, 1, 1, 1),
(2, 10, 'Bungalow with Sea View', 2, 2, 1400000, 'queen', 1, 1, 1, 1, 1, 1, 1),
(3, 9, 'Deluxe Suite', 3, 3, 1200000, 'king', 1, 1, 1, 1, 1, 1, 1),
(4, 1, 'Jungle View Suite', 2, 2, 1350000, 'queen', 1, 1, 1, 1, 1, 1, 0),
(5, 2, 'Oceanfront Standard', 4, 2, 1100000, 'queen', 1, 1, 1, 1, 1, 0, 1),
(6, 3, 'Terrace Room', 1, 2, 950000, 'queen', 1, 1, 1, 1, 1, 0, 0),
(7, 5, 'Cliffside Family Room', 2, 5, 1800000, 'king', 1, 1, 1, 1, 1, 1, 1),
(8, 4, 'Beach Hut', 1, 2, 850000, 'queen', 0, 1, 1, 1, 1, 1, 0),
(9, 7, 'Sunset Suite', 2, 3, 1600000, 'king', 1, 1, 1, 1, 1, 1, 1),
(10, 8, 'City Room', 4, 2, 700000, 'queen', 1, 1, 1, 1, 1, 0, 1),
(11, 1, 'Exclusive View Suite', 2, 2, 1350000, 'king', 1, 1, 1, 1, 1, 1, 0);

INSERT INTO customers (id, name, email, phone) VALUES
(1, 'Made Wirawan', 'made@mail.com', '0812300101'),
(2, 'Ni Luh Ayu', 'luh.ayu@mail.com', '0812300102'),
(3, 'Kadek Surya', 'kadek@mail.com', '0812300103'),
(4, 'Gede Putra', 'gede@mail.com', '0812300104'),
(5, 'Komang Diah', 'komang@mail.com', '0812300105'),
(6, 'Wayan Eka', 'wayan@mail.com', '0812300106'),
(7, 'I Gusti Rai', 'gusti@mail.com', '0812300107'),
(8, 'Desak Ayu', 'desak@mail.com', '0812300108'),
(9, 'Putu Lestari', 'putu@mail.com', '0812300109'),
(10, 'Ketut Adi', 'ketut@mail.com', '0812300110');

INSERT INTO bookings (id, customer, room_type, checkin_date, checkout_date, price, voucher, final_price, payment_status, has_checkedin, has_checkedout) VALUES
(1, 1, 1, '2025-07-01', '2025-07-05', 950000, 1, 751000, 'success', 1, 1),
(2, 2, 2, '2025-08-10', '2025-08-12', 2700000, 2, 1510000, 'waiting', 0, 0),
(3, 3, 3, '2025-06-20', '2025-06-25', 1200000, 3, 1028000, 'success', 1, 1),
(4, 4, 4, '2025-09-15', '2025-09-18', 800000, 4, 414000, 'success', 1, 1),
(5, 5, 5, '2025-10-01', '2025-10-03', 1800000, 5, 1080000, 'waiting', 0, 0),
(6, 6, 6, '2025-11-11', '2025-11-14', 1400000, 6, 1310000, 'failed', 0, 0),
(7, 7, 7, '2025-12-01', '2025-12-05', 1550000, 7, 893000, 'success', 1, 1),
(8, 8, 8, '2025-06-10', '2025-06-15', 700000, 8, 590000, 'waiting', 0, 0),
(9, 9, 9, '2025-07-25', '2025-07-28', 1250000, 9, 1008000, 'success', 1, 0),
(10, 10, 10, '2025-08-05', '2025-08-07', 900000, 10, 702000, 'failed', 0, 1);

INSERT INTO reviews (booking, star, title, content) VALUES
(1, 5, 'Amazing stay in Ubud', 'Tranquil and comfortable!'),
(2, 4, 'Good beach experience', 'Loved the location in Seminyak.'),
(3, 3, 'Cool surfer vibe', 'Perfect for solo travelers!'),
(4, 5, 'Nice garden view', 'Peaceful and cozy in Sanur.'),
(5, 2, 'Too hot inside', 'No AC made it a bit tough.'),
(6, 1, 'Didn’t enjoy', 'Too noisy and service was slow.'),
(7, 5, 'Perfect sunset spot', 'Stunning views in Uluwatu.'),
(8, 3, 'Simple room', 'Good enough for business trip.'),
(9, 4, 'Beach access was a plus', 'Very nice staff in Lovina.'),
(10, 4, 'Perfect for diving', 'Will come again to Amed!');

INSERT INTO vouchers (id, code, description, discount, start_date, end_date) VALUES
(1, 'BALI10', '10% off for Bali summer', 10.0, '2025-06-01', '2025-08-31'),
(2, 'BALI15', '15% off weekday stay', 15.0, '2025-07-01', '2025-09-30'),
(3, 'DIVESPOT', 'Special discount for divers', 14.0, '2025-06-15', '2025-12-31'),
(4, 'BEACHLOVER', 'Beach villas only promo', 48.0, '2025-07-01', '2025-10-01'),
(5, 'ROMANCE25', 'Couple getaway deal', 25.0, '2025-06-01', '2025-09-01'),
(6, 'EARLYBIRD', 'Early booking discount', 5.0, '2025-05-01', '2025-12-31'),
(7, 'SUNSET20', 'Uluwatu sunset special', 20.0, '2025-08-01', '2025-10-31'),
(8, 'CITYSTAY', 'Denpasar promo', 15.0, '2025-06-01', '2025-11-30'),
(9, 'LOVINABREEZE', 'North Bali deal', 19.0, '2025-06-01', '2025-10-30'),
(10, 'AMEDADVENTURE', 'Diving special', 22.0, '2025-07-15', '2025-12-31');