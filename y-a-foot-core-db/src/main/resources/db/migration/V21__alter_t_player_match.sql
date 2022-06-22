ALTER TABLE t_player_match DROP CONSTRAINT IF EXISTS t_joueur_match_pkey CASCADE;

CREATE UNIQUE INDEX i_player_match_id ON t_player_match (pma_match_id, pma_player_id);
ALTER TABLE t_player_match ADD CONSTRAINT pk_player_match PRIMARY KEY USING INDEX i_player_match_id;

ALTER TABLE t_player_match ADD CONSTRAINT fk_registration_match FOREIGN KEY (pma_match_id) REFERENCES t_match (mat_id);
ALTER TABLE t_player_match ADD CONSTRAINT fk_registration_player FOREIGN KEY (pma_player_id) REFERENCES t_player (ply_id);
ALTER TABLE t_player_match ADD CONSTRAINT fk_registration_car FOREIGN KEY (pma_car_id) REFERENCES t_car (car_id);