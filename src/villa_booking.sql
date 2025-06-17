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
(1, 'Villa 1', 'Description of Villa 1', 'Address 1'),
(2, 'Villa 2', 'Description of Villa 2', 'Address 2'),
(3, 'Villa 3', 'Description of Villa 3', 'Address 3'),
(4, 'Villa 4', 'Description of Villa 4', 'Address 4'),
(5, 'Villa 5', 'Description of Villa 5', 'Address 5'),
(6, 'Villa 6', 'Description of Villa 6', 'Address 6'),
(7, 'Villa 7', 'Description of Villa 7', 'Address 7'),
(8, 'Villa 8', 'Description of Villa 8', 'Address 8'),
(9, 'Villa 9', 'Description of Villa 9', 'Address 9'),
(10, 'Villa 10', 'Description of Villa 10', 'Address 10');

INSERT INTO room_types (id, villa, name, quantity, capacity, price, bed_size, has_desk, has_ac, has_tv, has_wifi, has_shower, has_hotwater, has_fridge) VALUES 
(1, 6, 'Family', 5, 2, 818358, 'king', 1, 0, 0, 0, 1, 1, 0),
(2, 10, 'Economy', 3, 1, 1441470, 'king', 0, 0, 0, 0, 1, 1, 0),
(3, 9, 'Suite', 3, 3, 1135127, 'queen', 1, 1, 1, 1, 0, 1, 1),
(4, 1, 'Suite', 4, 1, 1113372, 'queen', 1, 0, 0, 1, 1, 1, 0),
(5, 10, 'Economy', 1, 3, 1466359, 'queen', 0, 0, 1, 1, 0, 1, 1),
(6, 3, 'Standard', 1, 1, 1149800, 'king', 0, 1, 1, 1, 0, 0, 0),
(7, 7, 'Standard', 5, 2, 637247, 'queen', 0, 0, 1, 1, 0, 0, 0),
(8, 3, 'Standard', 1, 1, 458340, 'king', 1, 0, 1, 0, 1, 1, 1),
(9, 2, 'Economy', 5, 4, 846297, 'king', 1, 1, 1, 0, 1, 1, 1),
(10, 10, 'Family', 1, 3, 1189699, 'queen', 1, 0, 1, 1, 0, 0, 1);

INSERT INTO customers (id, name, email, phone) VALUES 
(1, 'Customer 1', 'customer1@mail.com', '0812300001'),
(2, 'Customer 2', 'customer2@mail.com', '0812300002'),
(3, 'Customer 3', 'customer3@mail.com', '0812300003'),
(4, 'Customer 4', 'customer4@mail.com', '0812300004'),
(5, 'Customer 5', 'customer5@mail.com', '0812300005'),
(6, 'Customer 6', 'customer6@mail.com', '0812300006'),
(7, 'Customer 7', 'customer7@mail.com', '0812300007'),
(8, 'Customer 8', 'customer8@mail.com', '0812300008'),
(9, 'Customer 9', 'customer9@mail.com', '0812300009'),
(10, 'Customer 10', 'customer10@mail.com', '0812300010');

INSERT INTO bookings (id, customer, room_type, checkin_date, checkout_date, price, voucher, final_price, payment_status, has_checkedin, has_checkedout) VALUES 
(1, 1, 2, '2024-11-12 16:13:56', '2025-12-02 11:38:27', 1117504, 9, 418095, 'waiting', 1, 0),
(2, 10, 9, '2024-07-24 14:27:48', '2025-01-14 13:45:52', 1184763, 10, 815732, 'success', 0, 1),
(3, 3, 2, '2024-06-30 16:27:24', '2024-09-08 13:38:06', 1256176, 7, 456393, 'waiting', 0, 1),
(4, 3, 4, '2024-07-04 12:44:47', '2024-09-21 17:47:29', 539649, 5, 983701, 'failed', 1, 1),
(5, 2, 7, '2024-06-23 10:16:00', '2025-10-25 15:21:26', 872447, 6, 1226515, 'failed', 0, 0),
(6, 2, 2, '2025-01-03 03:53:04', '2025-04-14 06:50:10', 1317606, 6, 840972, 'waiting', 1, 1),
(7, 2, 1, '2024-09-04 20:19:53', '2025-09-04 02:48:56', 1151432, 7, 333337, 'success', 0, 1),
(8, 4, 7, '2024-11-06 06:04:47', '2025-09-06 11:48:34', 1455730, 2, 846409, 'waiting', 0, 0),
(9, 2, 6, '2024-07-19 06:18:38', '2025-11-18 17:40:35', 1017510, 6, 911314, 'success', 1, 0),
(10, 2, 5, '2024-10-08 21:08:58', '2025-09-26 03:02:45', 978130, 6, 933019, 'waiting', 0, 1);

INSERT INTO reviews (booking, star, title, content) VALUES 
(1, 4, 'Review 1', 'Content for review 1'),
(2, 2, 'Review 2', 'Content for review 2'),
(3, 1, 'Review 3', 'Content for review 3'),
(4, 4, 'Review 4', 'Content for review 4'),
(5, 4, 'Review 5', 'Content for review 5'),
(6, 1, 'Review 6', 'Content for review 6'),
(7, 2, 'Review 7', 'Content for review 7'),
(8, 4, 'Review 8', 'Content for review 8'),
(9, 4, 'Review 9', 'Content for review 9'),
(10, 3, 'Review 10', 'Content for review 10');

INSERT INTO vouchers (id, code, description, discount, start_date, end_date) VALUES 
(1, 'DISC001', 'Voucher 1', 47.11, '2024-07-08 13:08:55', '2025-05-20 08:03:38'),
(2, 'DISC002', 'Voucher 2', 43.9, '2024-07-30 15:04:55', '2025-05-06 21:41:35'),
(3, 'DISC003', 'Voucher 3', 14.38, '2024-07-25 00:53:49', '2025-01-08 01:42:14'),
(4, 'DISC004', 'Voucher 4', 48.26, '2024-09-11 01:08:38', '2026-05-23 13:09:23'),
(5, 'DISC005', 'Voucher 5', 39.86, '2024-08-24 15:24:12', '2025-04-04 18:53:32'),
(6, 'DISC006', 'Voucher 6', 5.74, '2024-06-20 16:23:26', '2025-11-15 22:02:47'),
(7, 'DISC007', 'Voucher 7', 42.42, '2024-08-02 14:34:55', '2026-03-28 15:42:56'),
(8, 'DISC008', 'Voucher 8', 15.57, '2024-06-27 17:19:01', '2025-12-27 01:45:36'),
(9, 'DISC009', 'Voucher 9', 19.41, '2024-08-13 01:04:14', '2026-05-06 00:20:23'),
(10, 'DISC010', 'Voucher 10', 21.92, '2024-09-03 03:55:56', '2025-11-14 21:47:14'),
(11, 'DISC011', 'Voucher 11', 22.92, '2024-09-03 04:55:56', '2025-11-14 22:47:14');
