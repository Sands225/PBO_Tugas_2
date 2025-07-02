# PBO_Tugas_2
Hai! Selamat datang di program Sederhana kami.

# Pembuatan API Pemesanan Villa Sederhana Berbasis Java
<div align="justify">Proyek ini merupakan API Pemesanan Villa Sederhana Berbasis Java yang kami buat dengan tujuan untuk menguasai dasar-dasar pemrograman berorientasi objek, meningkatkan keterampilan pemrograman dengan Java, melatih kerja sama tim, serta memenuhi syarat Tugas 2 pada Mata Kuliah Pemrograman Berorientasi Objek. API digunakan untuk melakukan manipulasi data pada tiap entitas dari database dan mengatur GET, POST, PUT, DELETE. Response yang diberikan oleh server API menggunakan format JSON serta data disimpan pada Database SQLite. Untuk pengujian aplikasi dilakukan pada aplikasi Postman.</div>

# Identitas Kontributor
Mata Kuliah : Pemrograman Berbasis Obyek E 
- I Gusti Bagus Eri Widura  ( 2405551054 ) 
- I Made Sandika Wijaya	    ( 2405551082 ) 
- I Gede Puterayasa	        ( 2405551088 ) 
- I Wayan Arya Wikananda    ( 2405551090 )

# Panduan Sederhana
Program ini memiliki 3 type kelas, yakni class untuk pertama yaitu class untuk masing-masing Entitas yang terletak pada package model, class untuk keperluan API dan HTTP Server pada package httpserver dan class untuk keperluan database pada package ___.

# GET
Untuk mendapatkan record Seluruh Villa

![villa - get all villa](https://github.com/user-attachments/assets/c294f6ab-e504-40ca-b612-1ce398929faf)

Untuk mendapatkan record Villa berdasarkan ID

![villa - get villa](https://github.com/user-attachments/assets/fbb18580-a900-416a-b2d3-9b05e2f111c3)

Untuk mendapatkan record Villa available
![villa - get available villas](https://github.com/user-attachments/assets/b80c872e-73cc-4b8a-a57e-e839ca452ba9)

Untuk mendapatkan record review Villa

![villa - get review](https://github.com/user-attachments/assets/ea5b69e0-7e84-4a68-a92d-6c60e6aebff2)

Untuk mendapatkan record booking Villa

![villa - get booking](https://github.com/user-attachments/assets/cd14660a-e6d5-4bb9-b454-789faebc570b)

Untuk mendapatkan record room Villa

![villa - get room](https://github.com/user-attachments/assets/150bb4a4-db00-4d6f-a05e-11f1272c256b)

Untuk mendapatkan record Seluruh Customer

![customer - get all customer](https://github.com/user-attachments/assets/91d026c2-107f-47f8-97d5-6d5bb1250386)

Untuk mendapatkan record Customer berdasarkan ID

![customer - get customer](https://github.com/user-attachments/assets/9989a560-60bf-48c1-a4c3-b75997d72418)

Untuk mendapatkan record booking Customer

![customer - get booking](https://github.com/user-attachments/assets/c7587214-de5a-40e0-b2d0-bd912d4bdc28)

Untuk mendapatkan record review Customer

![customer - get review](https://github.com/user-attachments/assets/9f1e245f-bd73-45aa-9513-a5f16a8a3310)

Untuk mendapatkan record seluruh Voucher

![voucher - get all vouchers](https://github.com/user-attachments/assets/98f1d078-0e50-4ae3-b951-3dd7dd49ea39)

Untuk mendapatkan record Voucher berdasarkan ID

![voucher - get voucher](https://github.com/user-attachments/assets/78bfcccf-8f66-4bac-83c4-ee6a0b22e00b)


# POST
Menambahkan data baru pada Villa
![villa - add villa](https://github.com/user-attachments/assets/b324fc91-89c3-420f-af5a-3585b1bbfa40)

Data baru berhasil ditambahkan, berikut merupakan data dari tabel Villa


Menambahkan data baru pada Room
![villa - add room](https://github.com/user-attachments/assets/a7771291-db33-4db3-98c1-835ba7da3c53)

Data baru berhasil ditambahkan, berikut merupakan data dari tabel Room


Menambahkan data baru pada Customer
![customer - add customer](https://github.com/user-attachments/assets/27b2ba73-daf0-4149-ac26-dc13dbac4f1f)

Data baru berhasil ditambahkan, berikut merupakan data dari tabel Customer


Menambahkan data Booking baru oleh Customer
![customer - add booking](https://github.com/user-attachments/assets/6768b765-14c2-4964-b6b7-992a39ed4c6e)

Data baru berhasil ditambahkan, berikut merupakan data dari tabel Booking


Menambahkan data baru pada Review
![customer - add review](https://github.com/user-attachments/assets/2dc58f34-ebee-4dfd-89c2-83007c1f2626)

Data baru berhasil ditambahkan, berikut merupakan data dari tabel Review


Menambahkan data baru pada Voucher
![voucher - add voucher](https://github.com/user-attachments/assets/032c1a64-e093-4391-96ea-b78d189d9af2)

Data baru berhasil ditambahkan, berikut merupakan data dari tabel Voucher


# PUT
Mengupdate data yang sudah ada pada Villa berdasarkan ID 
![update villa](https://github.com/user-attachments/assets/803d8ae7-03d3-4283-9dd7-5231ded72c20)


Berikut merupakan hasil data yang sudah di update dengan Villa ID

Mengupdate data yang sudah ada pada Customer berdasarkan ID
![customer - update customer](https://github.com/user-attachments/assets/979fad1d-ea1f-478a-b39e-751c5988a376)

Berikut merupakan hasil data yang sudah di update dengan customer ID

Mengupdate data Room Villa yang sudah ada pada Villa berdasarkan Room ID
![update room](https://github.com/user-attachments/assets/17fff1eb-9fdb-419c-8840-b4856378b546)


Berikut merupakan hasil data yang sudah di update dengan Room ID 

Mengupdate data yang sudah ada pada Voucher berdasarkan ID
![voucher - update voucher](https://github.com/user-attachments/assets/ff3280a5-ea06-427d-a508-38389478f5b7)

Berikut merupakan hasil data yang sudah di update dengan Voucher ID

# DELETE

# EROR
