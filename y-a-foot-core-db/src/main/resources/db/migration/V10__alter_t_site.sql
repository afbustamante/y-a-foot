ALTER TABLE t_site RENAME COLUMN sit_nom TO sit_name;
ALTER TABLE t_site RENAME COLUMN sit_adresse TO sit_address;
ALTER TABLE t_site RENAME COLUMN sit_telephone TO sit_phone_number;

ALTER TABLE t_site ADD COLUMN sit_creator_id INTEGER;

ALTER TABLE t_site ADD CONSTRAINT site_creator_fk FOREIGN KEY (sit_creator_id) REFERENCES t_joueur (jou_id);