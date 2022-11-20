-- Add auditing missing audit columns
ALTER TABLE t_site ADD COLUMN sit_created_at TIMESTAMP;
ALTER TABLE t_site ADD COLUMN sit_updated_at TIMESTAMP;
ALTER TABLE t_car ADD COLUMN car_created_at TIMESTAMP;
ALTER TABLE t_car ADD COLUMN car_updated_at TIMESTAMP;

UPDATE t_site SET sit_created_at = CURRENT_TIMESTAMP;
UPDATE t_car SET car_created_at = CURRENT_TIMESTAMP;

ALTER TABLE t_site ALTER COLUMN sit_created_at SET NOT NULL;
ALTER TABLE t_car ALTER COLUMN car_created_at SET NOT NULL;
