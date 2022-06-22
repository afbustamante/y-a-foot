ALTER TABLE t_match DROP CONSTRAINT IF EXISTS t_match_pkey CASCADE;
CREATE UNIQUE INDEX i_match_id ON t_match (mat_id);
ALTER TABLE t_match ADD CONSTRAINT pk_match PRIMARY KEY USING INDEX i_match_id;

ALTER TABLE t_match ADD CONSTRAINT fk_match_creator FOREIGN KEY (mat_creator_id) REFERENCES t_player (ply_id);
ALTER TABLE t_match ADD CONSTRAINT fk_match_site FOREIGN KEY (mat_site_id) REFERENCES t_site (sit_id);