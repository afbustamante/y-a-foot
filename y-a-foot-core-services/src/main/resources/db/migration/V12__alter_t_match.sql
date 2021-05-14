SET SEARCH_PATH TO yafoot;

ALTER TABLE t_match RENAME COLUMN mat_num_joueurs_min TO mat_num_players_min;
ALTER TABLE t_match RENAME COLUMN mat_num_joueurs_max TO mat_num_players_max;
ALTER TABLE t_match RENAME COLUMN mat_site_fk TO mat_site_id;
ALTER TABLE t_match RENAME COLUMN mat_createur_fk TO mat_creator_id;
ALTER TABLE t_match RENAME COLUMN mat_date_creation TO mat_created_at;
ALTER TABLE t_match RENAME COLUMN mat_date_derniere_maj TO mat_last_update;
ALTER TABLE t_match RENAME COLUMN mat_covoiturage_actif TO mat_carpooling_enabled;
ALTER TABLE t_match RENAME COLUMN mat_partage_actif TO mat_code_sharing_enabled;
ALTER TABLE t_match RENAME COLUMN mat_num_joueurs_inscrits TO mat_num_registered_players;
