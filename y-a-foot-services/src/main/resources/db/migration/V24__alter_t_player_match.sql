SET SEARCH_PATH TO yafoot;

ALTER TABLE t_player_match ADD COLUMN pma_created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP;
ALTER TABLE t_player_match ADD COLUMN pma_last_update TIMESTAMP;
