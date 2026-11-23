ALTER TABLE users
    ADD COLUMN email    VARCHAR(255) NOT NULL,
    ADD COLUMN password VARCHAR(255) NOT NULL;

UPDATE users
SET email = 'legacy-' || id || '@proexpense.invalid',
    password = '!'
WHERE email IS NULL;

ALTER TABLE users
    ALTER COLUMN email    SET NOT NULL,
    ALTER COLUMN password SET NOT NULL,
    ADD CONSTRAINT uk_users_email UNIQUE (email);
