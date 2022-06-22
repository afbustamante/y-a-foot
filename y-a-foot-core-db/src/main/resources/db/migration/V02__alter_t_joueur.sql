ALTER TABLE t_joueur ADD COLUMN jou_date_creation TIMESTAMP;
ALTER TABLE t_joueur ADD COLUMN jou_date_derniere_maj TIMESTAMP;
ALTER TABLE t_joueur DROP COLUMN jou_mdp;
