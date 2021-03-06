SET SEARCH_PATH TO yafoot;

ALTER TABLE t_car DROP CONSTRAINT IF EXISTS t_voiture_pkey CASCADE;
CREATE UNIQUE INDEX i_car_id ON t_car (car_id);
ALTER TABLE t_car ADD CONSTRAINT pk_car PRIMARY KEY USING INDEX i_car_id;

ALTER TABLE t_car DROP CONSTRAINT IF EXISTS fkt_voiture48620;
ALTER TABLE t_car ADD CONSTRAINT fk_car_driver FOREIGN KEY (car_driver_id) REFERENCES t_player (ply_id);