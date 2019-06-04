SET SEARCH_PATH TO yafoot;

ALTER TABLE t_match ALTER COLUMN mat_num_joueurs_min TYPE SMALLINT;
ALTER TABLE t_match ALTER COLUMN mat_num_joueurs_max TYPE SMALLINT;
ALTER TABLE t_match ADD COLUMN mat_num_joueurs_inscrits SMALLINT DEFAULT 0;