SET SEARCH_PATH TO yafoot;

ALTER TABLE t_match
  ADD COLUMN mat_covoiturage_actif BOOLEAN NOT NULL DEFAULT true;
ALTER TABLE t_match
  ADD COLUMN mat_partage_actif BOOLEAN NOT NULL DEFAULT true;
