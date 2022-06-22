ALTER TABLE t_voiture RENAME COLUMN voi_id TO car_id;
ALTER TABLE t_voiture RENAME COLUMN voi_nom TO car_name;
ALTER TABLE t_voiture RENAME COLUMN voi_num_places TO car_num_seats;
ALTER TABLE t_voiture RENAME COLUMN voi_chauffeur_fk TO car_driver_id;

ALTER TABLE t_voiture RENAME TO t_car;
