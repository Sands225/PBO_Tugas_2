# Villa Booking API - Tugas 2 PBO
Hai! Selamat datang di program Sederhana kami.

---

## Pembuatan API Pemesanan Villa Sederhana Berbasis Java
<div align="justify">Proyek ini merupakan API Pemesanan Villa Sederhana Berbasis Java yang kami buat dengan tujuan untuk menguasai dasar-dasar pemrograman berorientasi objek, meningkatkan keterampilan pemrograman dengan Java, melatih kerja sama tim, serta memenuhi syarat Tugas 2 pada Mata Kuliah Pemrograman Berorientasi Objek. API digunakan untuk melakukan manipulasi data pada tiap entitas dari database dan mengatur GET, POST, PUT, DELETE. Response yang diberikan oleh server API menggunakan format JSON serta data disimpan pada Database SQLite. Untuk pengujian aplikasi dilakukan pada aplikasi Postman.

---

## Identitas Kontributor
**Mata Kuliah** : Pemrograman Berbasis Obyek E 
- I Gusti Bagus Eri Widura  ( 2405551054 ) 
- I Made Sandika Wijaya	    ( 2405551082 ) 
- I Gede Puterayasa	        ( 2405551088 ) 
- I Wayan Arya Wikananda    ( 2405551090 )

---

## Struktur Program
Program ini memiliki beberapa package yang digunakan untuk memisahkan class, diantaranya
- models      : Berisi class entitas utama, seperti `Villa`, `Room`, `Customer`, `Booking`,`Voucher`, dan `Review`.
- http        : Berisi konfigurasi HTTP server.
- db          : Mengelola koneksi dan utilitas database SQLite.
- handlers    : Menjalankan query database seperti SELECT, INSERT, dan DELETE.
- routes      : Menangani request dari client dan mengatur logika endpoint.
- utils       : Berisi fungsi bantu, seperti `sendResponse`.
- validations : Menyediakan fungsi validasi data dari client.
- exceptions  : Menyimpan custom exception untuk penanganan error.

---

## Panduan Program
### 1. Persiapan
Sebelum menjalankan program, pastikan komputer telah menginstal:
- Java JDK versi 11 atau lebih tinggi
- SQLite (untuk inisialisasi database)
- IDE seperti IntelliJ IDEA atau VS Code, atau dapat juga di run pada terminal
- Postman (untuk pengujian API)

### 2. Cara Menjalankan Program
#### 2.1 Inisialisasi Database
- Buka terminal dan arahkan ke direktori utama proyek, lalu jalankan perintah berikut: \
  ```sqlite3 src/vbook.db < src/villa_booking.sql``` \
  Perintah tersebut akan membuat file `vbook.db` dan mengisi struktur tabel serta dummy data dari file SQL
#### 2.2 Menjalankan Program
  
  - Menggunakan IDE (IntelliJ)
    - Buka proyek melalui menu File → Open
    - Buka file Main.java dan jalankan program
    
    ![WhatsApp Image 2025-07-04 at 18 55 35_7ac10eea](https://github.com/user-attachments/assets/1a090ea2-0c0f-4aa3-90e1-e45b10d38f7b)


  - Menggunakan terminal
    - Arahkan terminal pada dimana folder tersimpan, lalu jalankan program dengan menggunakan perintah berikut: \
    - Perintah kompilasi program \
    ```javac -d out -cp "lib/*" src/Main.java src/http/*.java src/routes/*.java src/models/*.java src/handlers/*.java src/db/Database.java src/db/*.java src/exceptions/*.java src/utils/*.java src/validations/*.java``` \
    
    - Perintah menjalankan program \
    ```java -cp "out;lib/*" Main```

    ![Screenshot 2025-07-04 181904](https://github.com/user-attachments/assets/c44f9cdc-1b08-40a2-b67c-19a6cca69a34)

- Jika berhasil, server akan berjalan di http://localhost:8080

### 3. Pengujian API
- Gunakan Postman untuk mengakses dan menguji endpoint API.
- Semua endpoint membutuhkan autentikasi menggunakan API Key:
  - Auth type: API Key
  - Key : X-API-KEY
  - Value : '123-SECRET-KEY'
  - Add to : Header
  // Gambar
  
---

## Dokumentasi Endpoint

### GET

Mendapatkan record Seluruh Villa 
- Method : ```GET``` 
- Endpoint : ```/villas```
  
![villa - get all villa](https://github.com/user-attachments/assets/c294f6ab-e504-40ca-b612-1ce398929faf)

Mendapatkan record Villa berdasarkan ID
- Method : ```GET```
- Endpoint : ```/villas/3```
  
![villa - get villa](https://github.com/user-attachments/assets/fbb18580-a900-416a-b2d3-9b05e2f111c3)

Mendapatkan record Villa yang tersedia dalam rentang waktu tertentu
- Method : ```GET```
- Endpoint :```/villas?ci_date=2020-08-06&co_date=2030-10-31```
  
![villa - get available villas](https://github.com/user-attachments/assets/b80c872e-73cc-4b8a-a57e-e839ca452ba9)

Mendapatkan record review Villa
- Method : ```GET```
- Endpoint : ```/villas/2/reviews```
  
![villa - get review](https://github.com/user-attachments/assets/ea5b69e0-7e84-4a68-a92d-6c60e6aebff2)

mendapatkan record booking Villa
- Method : ```GET```
- Endpoint : ```/villas/2/bookings```
  
![villa - get booking](https://github.com/user-attachments/assets/cd14660a-e6d5-4bb9-b454-789faebc570b)

Mendapatkan record room Villa
- Method : ```GET```
- Endpoint : ```/villas/3/rooms```
  
![villa - get room](https://github.com/user-attachments/assets/150bb4a4-db00-4d6f-a05e-11f1272c256b)

Mendapatkan record seluruh Customer
- Method : ```GET```
- Endpoint : ```/customers```
  
![customer - get all customer](https://github.com/user-attachments/assets/91d026c2-107f-47f8-97d5-6d5bb1250386)

Mendapatkan record Customer berdasarkan ID
- Method : ```GET```
- Endpoint : ```/customers/3```
  
![customer - get customer](https://github.com/user-attachments/assets/9989a560-60bf-48c1-a4c3-b75997d72418)

Mendapatkan record booking Customer
- Method : ```GET```
- Endpoint : ```/customers/3/bookings```
  
![customer - get booking](https://github.com/user-attachments/assets/c7587214-de5a-40e0-b2d0-bd912d4bdc28)

Mendapatkan record review Customer
- Method : ```GET```
- Endpoint : ```/customers/3/reviews```
  
![customer - get review](https://github.com/user-attachments/assets/9f1e245f-bd73-45aa-9513-a5f16a8a3310)

Mendapatkan record seluruh Voucher
- Method : ```GET```
- Endpoint : ```/vouchers```
  
![voucher - get all vouchers](https://github.com/user-attachments/assets/98f1d078-0e50-4ae3-b951-3dd7dd49ea39)

Mendapatkan record Voucher berdasarkan ID
- Method : ```GET```
- Endpoint : ```/vouchers/3```
  
![voucher - get voucher](https://github.com/user-attachments/assets/78bfcccf-8f66-4bac-83c4-ee6a0b22e00b)


### POST
Menambahkan data baru pada Villa
- Method : ```POST```
- Endpoint : ```/villas```
  
![villa - add villa](https://github.com/user-attachments/assets/b324fc91-89c3-420f-af5a-3585b1bbfa40)

Menambahkan data baru pada Room
- Method : ```POST```
- Endpoint : ```/villas/3/rooms```
  
![villa - add room](https://github.com/user-attachments/assets/a7771291-db33-4db3-98c1-835ba7da3c53)

Menambahkan data baru pada Customer
- Method : ```POST```
- Endpoint : ```/customers```
  
![customer - add customer](https://github.com/user-attachments/assets/27b2ba73-daf0-4149-ac26-dc13dbac4f1f)

Menambahkan data Booking baru oleh Customer
- Method : ```POST```
- Endpoint : ```/customers/3/bookings```
  
![customer - add booking](https://github.com/user-attachments/assets/6768b765-14c2-4964-b6b7-992a39ed4c6e)

Menambahkan data baru pada Review
- Method : ```POST```
- Endpoint : ```customers/3/bookings/5/reviews```
  
![customer - add review](https://github.com/user-attachments/assets/2dc58f34-ebee-4dfd-89c2-83007c1f2626)

Menambahkan data baru pada Voucher
- Method : ```POST```
- Endpoint : ```/vouchers```
  
![voucher - add voucher](https://github.com/user-attachments/assets/032c1a64-e093-4391-96ea-b78d189d9af2)


### PUT
Mengubah data yang sudah ada pada Villa berdasarkan ID 
- Method : ```PUT```
- Endpoint : ```/villas/3```
  
![update villa](https://github.com/user-attachments/assets/803d8ae7-03d3-4283-9dd7-5231ded72c20)

Mengubah data yang sudah ada pada Customer berdasarkan ID
- Method : ```PUT```
- Endpoint : ```/customers/3```
  
![customer - update customer](https://github.com/user-attachments/assets/979fad1d-ea1f-478a-b39e-751c5988a376)

Mengubah data Room Villa yang sudah ada pada Villa berdasarkan Room ID
- Method : ```PUT```
- Endpoint : ```/villa```
  
![update room](https://github.com/user-attachments/assets/17fff1eb-9fdb-419c-8840-b4856378b546)

Mengubah data yang sudah ada pada Voucher berdasarkan ID
- Method : ```PUT```
- Endpoint : ```/vouchers```
  
![voucher - update voucher](https://github.com/user-attachments/assets/ff3280a5-ea06-427d-a508-38389478f5b7)

### DELETE
Menghapus data Room Villa yang sudah ada pada Villa berdasarkan Room ID
- Method : ```DELETE```
- Endpoint : ```/villas/3/rooms/5```
  
![update room](https://github.com/user-attachments/assets/36c96b19-0087-40fe-845e-e12055629a94)

Menghapus data yang sudah ada pada Villa berdasarkan Villa ID
- Method : ```DELETE```
- Endpoint : ```/villas/3```
  
![villa - delete villa](https://github.com/user-attachments/assets/53be5e1d-a5e0-4639-ad51-b86e0b899bf7)

Menghapus data yang sudah ada pada Voucher berdasarkan Voucher ID
- Method : ```DELETE```
- Endpoint : ```/vouchers/3```
  
![voucher - delete voucher](https://github.com/user-attachments/assets/d47b4689-bab7-4d93-803f-f6820efd54e7)
</div>

---

### ERROR TESTING

Invalid API Key
- Method : ```GET```
- Endpoint : ```/villas```

![invalid API KEY](https://github.com/user-attachments/assets/40bc27db-72e2-4dc6-871d-14b93ad7a704)

Endpoint Not Found
- Method : ```GET```
- Endpoint : ```/villas/hehe```

![Endpoint Not Found](https://github.com/user-attachments/assets/955a07e0-b075-423f-bb49-32e254b93507)

Erorr Method Not Allowed
- Method : ```PATCH```
- Endpoint : ```/villas```

![Method Not Allowed](https://github.com/user-attachments/assets/0341351f-f418-4eb4-94f9-b073726ffdd7)

