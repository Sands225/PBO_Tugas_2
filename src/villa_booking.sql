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

INSERT INTO villas (id, name, description, address) VALUES
(1, 'Villa Tabanan', 'Hidden gem among rice fields', 'Jl. Tanah Lot, Tabanan, Bali'),
(2, 'Villa Bedugul', 'Cool mountain retreat', 'Jl. Raya Bedugul, Bali');

INSERT INTO customers (id, name, email, phone) VALUES
(1, 'Ayu Kartika', 'ayu.kartika@mail.com', '0812300111'),
(2, 'Dewa Mahendra', 'dewa.mahendra@mail.com', '0812300112');

INSERT INTO vouchers (id, code, description, discount, start_date, end_date) VALUES
(1, 'TABANANDEAL', 'Discount for Tabanan area', 12.0, '2025-07-01', '2025-12-31'),
(2, 'BEDUGUL10', '10% off for Bedugul villas', 10.0, '2025-06-15', '2025-09-30');

INSERT INTO room_types (id, villa, name, quantity, capacity, price, bed_size, has_desk, has_ac, has_tv, has_wifi, has_shower, has_hotwater, has_fridge) VALUES
(2, 1, 'Ricefield View Room', 2, 2, 1050000, 'queen', 1, 1, 1, 1, 1, 1, 1),
(3, 2, 'Mountain Chill Room', 2, 2, 980000, 'king', 1, 1, 1, 1, 1, 1, 0),
(4, 1, 'Private Pool Suite', 1, 2, 1600000, 'king', 1, 1, 1, 1, 1, 1, 1),
(5, 2, 'Lakeside Cottage', 1, 3, 1200000, 'queen', 0, 1, 1, 1, 1, 1, 1);

INSERT INTO bookings (id, customer, room_type, checkin_date, checkout_date, price, voucher, final_price, payment_status, has_checkedin, has_checkedout) VALUES
(1, 1, 2, '2025-08-05 14:00:00', '2025-08-08 12:00:00', 1050000, 11, 924000, 'success', 1, 0),
(2, 2, 3, '2025-09-01 15:00:00', '2025-09-04 11:00:00', 980000, 12, 882000, 'waiting', 0, 0),
(3, 1, 4, '2025-10-10 13:00:00', '2025-10-12 12:00:00', 3200000, 11, 2816000, 'success', 1, 1),
(4, 2, 5, '2025-11-20 14:00:00', '2025-11-22 12:00:00', 1200000, 12, 1080000, 'success', 1, 1);

INSERT INTO reviews (booking, star, title, content) VALUES
(1, 5, 'Loved the view!', 'Really peaceful in the rice fields.'),
(2, 4, 'Cool air and comfy bed', 'Perfect escape from Bali heat.'),
(3, 5, 'Private pool was amazing!', 'Would stay again for sure.'),
(4, 3, 'Nice but no fridge', 'Lake view was great but room lacked minibar.');
