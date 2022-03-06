SET SEARCH_PATH TO yafoot;

ALTER TABLE t_site
  ADD COLUMN sit_latitude NUMERIC(8, 5);
ALTER TABLE t_site
  ADD COLUMN sit_longitude NUMERIC(8, 5);
