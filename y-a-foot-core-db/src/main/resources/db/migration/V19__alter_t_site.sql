ALTER TABLE t_site DROP CONSTRAINT IF EXISTS t_site_pkey CASCADE;
CREATE UNIQUE INDEX i_site_id ON t_site (sit_id);
ALTER TABLE t_site ADD CONSTRAINT pk_site PRIMARY KEY USING INDEX i_site_id;

ALTER TABLE t_site ADD CONSTRAINT fk_site_creator FOREIGN KEY (sit_creator_id) REFERENCES t_player (ply_id);