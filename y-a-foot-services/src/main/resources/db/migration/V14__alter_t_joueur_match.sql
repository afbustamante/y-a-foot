SET SEARCH_PATH TO yafoot;

ALTER TABLE t_joueur_match RENAME COLUMN jma_joueur_fk TO pma_player_id;
ALTER TABLE t_joueur_match RENAME COLUMN jma_match_fk TO pma_match_id;
ALTER TABLE t_joueur_match RENAME COLUMN jma_voiture_fk TO pma_car_id;

ALTER TABLE t_joueur_match RENAME TO t_player_match;

