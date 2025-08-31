# 🐾 PetCare

**PetCare** is a **web application** for managing pets, veterinary appointments, and medical records.
The application supports two user roles: **Pet Owner** and **Doctor**.

---

## ✨ Features

### 🔐 Users

* **Pet Owners**: can register, log in, manage their pets and appointments, and view medical records.
* **Doctors**: (created by an admin in the database), can log in, manage their available appointment slots, create and view pet medical records, and view their patients.

### 🐶 Pet Owners can:

* CRUD for pets (add, edit, delete, view)
* Book appointments with doctors
* Cancel appointments
* View medical records of their pets

### 🩺 Doctors can:

* View their patients (only pets with scheduled appointments)
* View all appointments
* Create and view medical records of pets
* Manage their available slots (add/delete)

---

## 🛠️ Tech Stack

**Backend:**

* Java 17+
* Spring Boot (Web, Security, Data JPA)
* Hibernate ORM

**Frontend:**

* Thymeleaf templates
* HTML, CSS, JavaScript

**Database:**

* H2 (test mode)
* PostgreSQL (dev mode, via Docker Compose)

**Other:**

* Maven
* JUnit + Spring Test
* Docker Compose (for PostgreSQL)

---

## 🚀 Getting Started

### 1. Prerequisites

* JDK 17+
* Maven 3+
* Docker & Docker Compose (for dev mode)

### 2. Test Mode (H2)

```bash
mvn spring-boot:run -Dspring-boot.run.profiles=test
```

Open: [http://localhost:8080](http://localhost:8080)

### 3. Development Mode (PostgreSQL)

1. Start PostgreSQL via Docker Compose:

```bash
docker-compose up -d
```

2. Run the app:

```bash
mvn spring-boot:run -Dspring-boot.run.profiles=dev
```

### 4. Run Tests

* Unit tests only: `mvn test`
* Integration tests: `mvn verify`

---

## 📂 Project Structure

```
src/
 ├── main/
 │   ├── java/com/example/petcare   # Controllers, services, entities, repositories
 │   ├── resources/
 │   │   ├── templates/             # Thymeleaf HTML templates
 │   │   ├── static/                # CSS, JS
 │   │   ├── application.properties
 │   │   └── application-{profile}.properties
 ├── test/                          # Unit & integration tests
```

---

## 🌐 Endpoints

### For all users

* `GET /` – Dashboard or login page
* `GET /login` – Login page
* `POST /login` – Authenticate user
* `GET /dashboard` – Dashboard after login

### For Pet Owners

* `GET /register` – Registration page
* `POST /register` – Register a new owner
* `GET /pets` – List of pets
* `POST /pets` – Add a new pet
* `PUT /pets/{id}` – Update pet
* `DELETE /pets/{id}` – Delete pet
* `GET /appointments/available-doctors` – List available doctors and slots
* `GET /appointments` – List appointments
* `POST /appointments` – Book an appointment
* `DELETE /appointments/{id}` – Cancel appointment
* `GET /pets/medical-records/{petId}` – View medical records

### For Doctors

* `GET /pets` – View pets they have appointments with
* `GET /appointments` – View all appointments
* `GET /pets/medical-records/{petId}` – View medical records
* `POST /pets/medical-records/{petId}` – Create medical record
* `GET /doctor-slots` – View their available slots
* `POST /doctor-slots` – Add a free slot
* `DELETE /doctor-slots/{id}` – Remove a free slot

> All endpoints are role-protected: **ROLE\_PET\_OWNER** and **ROLE\_DOCTOR**.
> Pet Owners can only access their own data. Doctors can manage only their slots and view medical records for pets associated with their appointments.

## 📖 Notes 

- In **test mode**, demo data (users, pets, doctors, slots, appointments, medical records) is auto-generated.
- In **dev mode**, the database starts empty (manual data entry required). 

---