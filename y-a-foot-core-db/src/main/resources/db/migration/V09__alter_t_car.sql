ALTER TABLE t_car ADD COLUMN car_active BOOL;

UPDATE t_car SET car_active = true;

ALTER TABLE t_car ALTER COLUMN car_active SET NOT NULL;
