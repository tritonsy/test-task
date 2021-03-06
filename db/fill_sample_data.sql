-- patients table

INSERT INTO T_PATIENT(NAME, SURNAME, PATRONYMIC, PHONE_NUMBER) VALUES
('Сергей', 'Александр', 'Антонович', '89273225323');
INSERT INTO T_PATIENT(NAME, SURNAME, PATRONYMIC, PHONE_NUMBER) VALUES
('Иван', 'Петров', 'Сергеевич', '89271235323');
INSERT INTO T_PATIENT(NAME, SURNAME, PATRONYMIC, PHONE_NUMBER) VALUES
('Кларисса', 'Старлинг', 'М.', '89279123452');
INSERT INTO T_PATIENT(NAME, SURNAME, PATRONYMIC, PHONE_NUMBER) VALUES
('Александр', 'Луканов', 'Сергеевич', '89279813254');
INSERT INTO T_PATIENT(NAME, SURNAME, PATRONYMIC, PHONE_NUMBER) VALUES
('Анатолий', 'Степанов', 'Николаевич', '89279182343');
INSERT INTO T_PATIENT(NAME, SURNAME, PATRONYMIC, PHONE_NUMBER) VALUES
('Рената', 'Филатова', 'Александровна', '89279182343');

-- doctors table

INSERT INTO T_DOCTOR(NAME, SURNAME, PATRONYMIC, SPECIALIZATION) VALUES
('Сергей', 'Боткин', 'Петрович', 'Терапевт');
INSERT INTO T_DOCTOR(NAME, SURNAME, PATRONYMIC, SPECIALIZATION) VALUES
('Николай', 'Склифосовкий', 'Васильевич', 'Хирург');
INSERT INTO T_DOCTOR(NAME, SURNAME, PATRONYMIC, SPECIALIZATION) VALUES
('Иван', 'Павлов', 'Петрович', 'Физиолог');
INSERT INTO T_DOCTOR(NAME, SURNAME, PATRONYMIC, SPECIALIZATION) VALUES
('Александр', 'Николаевич', 'Бакулев', 'Хирург');
INSERT INTO T_DOCTOR(NAME, SURNAME, PATRONYMIC, SPECIALIZATION) VALUES
('Владимир', 'Бехтерев', 'Михайлович', 'Хирург');
INSERT INTO T_DOCTOR(NAME, SURNAME, PATRONYMIC, SPECIALIZATION) VALUES
('Владимир', 'Филатов', 'Петрович', 'Офтальмолог');
INSERT INTO T_DOCTOR(NAME, SURNAME, PATRONYMIC, SPECIALIZATION) VALUES
('Ганнибал', 'Лектер', 'Диттман', 'Психиатр');

-- receipts table

INSERT INTO T_RECEIPT(DESCRIPTION, PATIENT_ID, DOCTOR_ID, CREATION_DATE, EXPIRE_DATE, PRIORITY) VALUES
('Туда сюда', 2, 4, '2019-07-09', '2019-09-09', 'Нормальный');
INSERT INTO T_RECEIPT(DESCRIPTION, PATIENT_ID, DOCTOR_ID, CREATION_DATE, EXPIRE_DATE, PRIORITY) VALUES
('Логика человеческого восприятия до безобразия изменчива', 3, 7, '1991-02-13', '1991-03-13', 'Срочный');
INSERT INTO T_RECEIPT(DESCRIPTION, PATIENT_ID, DOCTOR_ID, CREATION_DATE, EXPIRE_DATE, PRIORITY) VALUES
('Пятое десятое', 5, 4, '2019-07-12', '2019-08-12', 'Немедленный');
INSERT INTO T_RECEIPT(DESCRIPTION, PATIENT_ID, DOCTOR_ID, CREATION_DATE, EXPIRE_DATE, PRIORITY) VALUES
('Тут покрутить', 6, 1, '2019-07-12', '2019-08-12', 'Немедленный');
