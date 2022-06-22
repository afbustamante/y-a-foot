ALTER TABLE t_joueur RENAME COLUMN jou_id TO ply_id;
ALTER TABLE t_joueur RENAME COLUMN jou_nom TO ply_surname;
ALTER TABLE t_joueur RENAME COLUMN jou_prenom TO ply_first_name;
ALTER TABLE t_joueur RENAME COLUMN jou_email TO ply_email;
ALTER TABLE t_joueur RENAME COLUMN jou_telephone TO ply_phone_number;
ALTER TABLE t_joueur RENAME COLUMN jou_date_creation TO ply_created_at;
ALTER TABLE t_joueur RENAME COLUMN jou_date_derniere_maj TO ply_last_updated;
ALTER TABLE t_joueur RENAME COLUMN jou_actif TO ply_active;

ALTER TABLE t_joueur RENAME TO t_player;
