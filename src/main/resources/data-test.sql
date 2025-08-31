-- John Doe → password123
-- Emily Clark → mypassword
-- Alice Smith → alice123
-- Bob Brown → bob123
-- Clara Johnson → clara123

INSERT INTO abstract_user (first_name, last_name, email, password, role) VALUES
('John', 'Doe', 'john@example.com', '$2a$10$cma2qhSEV1j.Xkf9UO.9Je8N2M0bWMH7Ml1jyj4wKIawZQ.yTTHoe', 'ROLE_PET_OWNER'),
('Emily', 'Clark', 'emily@example.com', '$2a$10$JQlWK4m4qVAHjrRZ9.oQLO01gvmibRPLrmFlMeXGY1qHufTdWF8/C', 'ROLE_PET_OWNER'),
('Alice', 'Smith', 'alice@vet.com', '$2a$10$xpW6bYZ4yIrPJ1OaHoTG/unEGvUM1h/V7gv6epGNK3aJps0.Nzb/S', 'ROLE_DOCTOR'),
('Bob', 'Brown', 'bob@vet.com', '$2a$10$mwsBzJFZ5Ad3Ly3Up9hUfeRoB/YpmzjM5n0J8SvZyCzHwxXjEW6ga', 'ROLE_DOCTOR'),
('Clara', 'Johnson', 'clara@vet.com', '$2a$10$OJXbdZiCkFGvGbe1TPY9BuwDyYTWc/mIyQUE4XIR.i8UzC8zmb9Vq', 'ROLE_DOCTOR');

INSERT INTO pet_owner (id)
SELECT id FROM abstract_user WHERE email='john@example.com';
INSERT INTO pet_owner (id)
SELECT id FROM abstract_user WHERE email='emily@example.com';

INSERT INTO doctor (id, specialization)
SELECT id, 'CARDIOLOGY' FROM abstract_user WHERE email='alice@vet.com';
INSERT INTO doctor (id, specialization)
SELECT id, 'DERMATOLOGY' FROM abstract_user WHERE email='bob@vet.com';
INSERT INTO doctor (id, specialization)
SELECT id, 'SURGERY' FROM abstract_user WHERE email='clara@vet.com';

INSERT INTO pet (name, species, gender, birth_date, owner_id)
SELECT 'Fluffy', 'CAT', 'FEMALE', '2020-05-12', id FROM abstract_user WHERE email='john@example.com';
INSERT INTO pet (name, species, gender, birth_date, owner_id)
SELECT 'Bella', 'DOG', 'FEMALE', '2019-03-07', id FROM abstract_user WHERE email='emily@example.com';
INSERT INTO pet (name, species, gender, birth_date, owner_id)
SELECT 'Rocky', 'DOG', 'MALE', '2018-11-22', id FROM abstract_user WHERE email='john@example.com';
INSERT INTO pet (name, species, gender, birth_date, owner_id)
SELECT 'Milo', 'BIRD', 'MALE', '2021-09-10', id FROM abstract_user WHERE email='emily@example.com';

INSERT INTO medical_record (diagnosis, treatment, record_date, pet_id, doctor_id)
SELECT 'Cold', 'Rest and vitamins', CURRENT_DATE - INTERVAL '5' DAY, p.id, d.id
FROM pet p, abstract_user d
WHERE p.name='Fluffy' AND d.email='alice@vet.com';

INSERT INTO medical_record (diagnosis, treatment, record_date, pet_id, doctor_id)
SELECT 'Skin allergy', 'Special shampoo', CURRENT_DATE - INTERVAL '10' DAY, p.id, d.id
FROM pet p, abstract_user d
WHERE p.name='Bella' AND d.email='bob@vet.com';

INSERT INTO medical_record (diagnosis, treatment, record_date, pet_id, doctor_id)
SELECT 'Broken wing', 'Bandage + antibiotics', CURRENT_DATE - INTERVAL '2' DAY, p.id, d.id
FROM pet p, abstract_user d
WHERE p.name='Milo' AND d.email='clara@vet.com';

INSERT INTO available_slot (slot_time, booked, doctor_id)
SELECT CURRENT_TIMESTAMP + INTERVAL '1' DAY + INTERVAL '10' HOUR, true, id
FROM abstract_user WHERE email='alice@vet.com';

INSERT INTO available_slot (slot_time, booked, doctor_id)
SELECT CURRENT_TIMESTAMP + INTERVAL '2' DAY + INTERVAL '11' HOUR, false, id
FROM abstract_user WHERE email='alice@vet.com';

INSERT INTO available_slot (slot_time, booked, doctor_id)
SELECT CURRENT_TIMESTAMP + INTERVAL '3' DAY + INTERVAL '9' HOUR + INTERVAL '30' MINUTE, true, id
FROM abstract_user WHERE email='bob@vet.com';

INSERT INTO available_slot (slot_time, booked, doctor_id)
SELECT CURRENT_TIMESTAMP + INTERVAL '4' DAY + INTERVAL '15' HOUR, false, id
FROM abstract_user WHERE email='clara@vet.com';

INSERT INTO appointment (reason, pet_id, doctor_id, slot_id)
SELECT 'Regular checkup', p.id, d.id, s.id
FROM pet p, abstract_user d, available_slot s
WHERE p.name='Fluffy' AND d.email='alice@vet.com' AND s.doctor_id=d.id AND s.booked=true;

INSERT INTO appointment (reason, pet_id, doctor_id, slot_id)
SELECT 'Skin irritation follow-up', p.id, d.id, s.id
FROM pet p, abstract_user d, available_slot s
WHERE p.name='Bella' AND d.email='bob@vet.com' AND s.doctor_id=d.id AND s.booked=true;
