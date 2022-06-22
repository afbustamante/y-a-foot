ALTER TABLE t_match ADD COLUMN mat_sport_id SMALLINT NOT NULL DEFAULT 1;

ALTER TABLE t_match ADD CONSTRAINT fk_match_sport FOREIGN KEY (mat_sport_id) REFERENCES t_sport (spt_id);