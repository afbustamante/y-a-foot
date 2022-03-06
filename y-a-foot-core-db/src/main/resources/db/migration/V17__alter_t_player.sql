SET SEARCH_PATH TO yafoot;

ALTER TABLE t_player DROP CONSTRAINT IF EXISTS t_joueur_pkey CASCADE;
CREATE UNIQUE INDEX i_player_id ON t_player (ply_id);
ALTER TABLE t_player ADD CONSTRAINT pk_player PRIMARY KEY USING INDEX i_player_id;
ALTER INDEX i_joueur_email RENAME TO i_player_email;