INSERT INTO
    City (CI_NAME, CI_PROVINCE)
VALUES
    ('Toronto', 'Ontario'),
    ('Vancouver', 'British Columbia'),
    ('Montreal', 'Quebec'),
    ('Quebec City', 'Quebec');

INSERT INTO
    Location (L_FACILITY, L_ROOM_NAME, L_TYPE, CI_ID)
VALUES
    ('YMCA', 'Room 1', 'Room', 3),
    ('YMCA', 'Room 2', 'Room', 3),
    ('YMCA', 'Room 3', 'Room', 3),
    ('YMCA', 'Gym 1', 'Gym', 3),
    ('YMCA', 'Gym 2', 'Gym', 3),
    ('Boy''s and Girl''s Club', 'Gym 1', 'Gym', 1),
    ('Boy''s and Girl''s Club', 'Room L2', 'Room', 1),
    ('Aqua Park', 'Beginner Pool', 'Pool', 1),
    ('Aqua Park', 'Beginner Pool', 'Pool', 3),
    ('Aqua Park', 'Deep End', 'Pool', 3);

INSERT INTO
    Admin (A_NAME, A_USERNAME, A_PASSWORD)
VALUES
    ('Admin', 'admin', 'SuperSecretPassword'),
    ('', 'adminWithoutName', 'SuperSecretPassword') ON CONFLICT (A_USERNAME) DO NOTHING;

INSERT INTO
    Client (
        C_NAME,
        C_USERNAME,
        C_PASSWORD,
        C_AGE,
        GUARDIAN_ID
    )
VALUES
    ('Bob', 'bob', 'password', 35, NULL),
    ('Alice', 'alice', 'password', 10, NULL),
    ('Mary', 'mary', 'password', 12, 1),
    ('Steve', 'steve', 'password', 18, NULL) ON CONFLICT (C_USERNAME) DO NOTHING;

INSERT INTO
    Instructor (
        I_NAME,
        I_USERNAME,
        I_PASSWORD,
        I_PHONE -- No country code due to only offering service in Canada
    )
VALUES
    ('John', 'john', 'password', '123-456-7890'),
    ('Nathan', 'nathan', 'password', '987-654-3210'),
    ('Mark', 'mark', 'password', '321-456-7890') ON CONFLICT (I_USERNAME) DO NOTHING;

INSERT INTO
    Instructor_City (I_ID, CI_ID)
VALUES
    (1, 1),
    (2, 1),
    (2, 2),
    (2, 3),
    (3, 3);

INSERT INTO
    Specialization (S_NAME)
VALUES
    ('Swimming'),
    ('Yoga'),
    ('Weightlifting'),
    ('Cardio'),
    ('Pilates') ON CONFLICT (S_NAME) DO NOTHING;

INSERT INTO
    Instructor_Specialization (I_ID, S_ID)
VALUES
    (1, 1),
    (1, 3),
    (2, 2),
    (2, 4),
    (2, 5),
    (3, 2),
    (3, 5);

INSERT INTO
    Schedule (SC_START_DATE, SC_END_DATE)
VALUES
    ('2024-12-01', '2024-12-10'),
    ('2024-11-04', '2024-12-2'),
    ('2025-01-01', '2025-01-31');

INSERT INTO
    Time_Slot (TS_DAY, TS_START_TIME, TS_END_TIME, SC_ID)
VALUES
    ('Monday', '20:30', '22:00', 1),
    ('Wednesday', '20:30', '22:00', 1),
    ('Friday', '20:30', '22:00', 1),
    ('Saturday', '9:00', '10:30', 2),
    ('Sunday', '9:00', '10:30', 2),
    ('Monday', '19:00', '21:00', 3),
    ('Tuesday', '19:00', '21:00', 3),
    ('Wednesday', '19:00', '21:00', 3),
    ('Thursday', '19:00', '21:00', 3),
    ('Friday', '19:00', '21:00', 3),
    ('Saturday', '8:30', '11:00', 3),
    ('Sunday', '8:30', '11:00', 3);

INSERT INTO
    Offering (O_LESSON, O_MAX_CAPACITY, I_ID, L_ID, SC_ID)
VALUES
    ('Swimming Lessons', 10, 1, 8, 1),
    ('Swimming Lessons', 1, 1, 8, 1),
    ('Yoga Lessons', 1, 2, 3, 2),
    ('Yoga Lessons', 5, 3, 1, 2),
    ('Weightlifting Class', 3, 1, 6, 3);

INSERT INTO
    Booking (C_ID, O_ID)
VALUES
    (1, 1),
    (1, 3),
    (2, 4),
    (2, 1),
    (3, 4),
    (4, 5);