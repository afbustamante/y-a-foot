ALTER TABLE t_match ADD COLUMN mat_createur_fk INT4;
ALTER TABLE t_match ADD COLUMN mat_date_creation TIMESTAMP;
ALTER TABLE t_match ADD COLUMN mat_date_derniere_maj TIMESTAMP;

ALTER TABLE t_match ADD CONSTRAINT createur_match_fk FOREIGN KEY (mat_createur_fk) REFERENCES t_joueur (jou_id);
