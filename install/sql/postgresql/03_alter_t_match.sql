SET SEARCH_PATH TO yafoot;

ALTER TABLE t_match ADD COLUMN mat_date_creation TIMESTAMP;
ALTER TABLE t_match ADD COLUMN mat_date_derniere_maj TIMESTAMP;
