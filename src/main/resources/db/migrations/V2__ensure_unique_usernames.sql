DROP TRIGGER IF EXISTS check_admin_username ON Admin;
DROP TRIGGER IF EXISTS check_instructor_username ON Instructor;
DROP TRIGGER IF EXISTS check_client_username ON Client;
DROP FUNCTION IF EXISTS check_username_uniqueness();

CREATE OR REPLACE FUNCTION check_username_uniqueness()
RETURNS TRIGGER AS $$
DECLARE
    username_to_check VARCHAR;
    current_id INT;
BEGIN
    CASE TG_TABLE_NAME
        WHEN 'admin' THEN 
            username_to_check := NEW.A_USERNAME;
            current_id := NEW.A_ID;
        WHEN 'instructor' THEN 
            username_to_check := NEW.I_USERNAME;
            current_id := NEW.I_ID;
        WHEN 'client' THEN 
            username_to_check := NEW.C_USERNAME;
            current_id := NEW.C_ID;
    END CASE;

    -- Check if username exists in Admin table
    IF EXISTS (
        SELECT 1 FROM Admin 
        WHERE A_USERNAME = username_to_check
        AND (TG_TABLE_NAME != 'admin' OR A_ID != current_id) -- Allow updates when the current user id is the same as the one being checked
    ) THEN
        RAISE EXCEPTION 'Username % already exists in Admin table', username_to_check
        USING ERRCODE = 'U0001';
    END IF;

    -- Check if username exists in Instructor table
    IF EXISTS (
        SELECT 1 FROM Instructor 
        WHERE I_USERNAME = username_to_check
        AND (TG_TABLE_NAME != 'instructor' OR I_ID != current_id)
    ) THEN
        RAISE EXCEPTION 'Username % already exists in Instructor table', username_to_check
        USING ERRCODE = 'U0001';
    END IF;

    -- Check if username exists in Client table
    IF EXISTS (
        SELECT 1 FROM Client 
        WHERE C_USERNAME = username_to_check
        AND (TG_TABLE_NAME != 'client' OR C_ID != current_id)
    ) THEN
        RAISE EXCEPTION 'Username % already exists in Client table', username_to_check
        USING ERRCODE = 'U0001';
    END IF;

    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER check_admin_username
BEFORE INSERT OR UPDATE ON Admin
FOR EACH ROW
EXECUTE FUNCTION check_username_uniqueness();

CREATE TRIGGER check_instructor_username
BEFORE INSERT OR UPDATE ON Instructor
FOR EACH ROW
EXECUTE FUNCTION check_username_uniqueness();

CREATE TRIGGER check_client_username
BEFORE INSERT OR UPDATE ON Client
FOR EACH ROW
EXECUTE FUNCTION check_username_uniqueness();