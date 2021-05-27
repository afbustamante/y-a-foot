SET SEARCH_PATH TO yafoot;

CREATE TABLE t_sport
(
    spt_id   smallint     NOT NULL,
    spt_code varchar(50)  NOT NULL,
    spt_name varchar(100) NOT NULL
);

CREATE UNIQUE INDEX i_sport_id ON t_sport (spt_id);
CREATE UNIQUE INDEX i_sport_code ON t_sport (spt_code);

ALTER TABLE t_sport ADD CONSTRAINT pk_sport PRIMARY KEY USING INDEX i_sport_id;

INSERT INTO t_sport (spt_id, spt_code, spt_name) VALUES (1, 'FOOTBALL', 'Football');
INSERT INTO t_sport (spt_id, spt_code, spt_name) VALUES (2, 'RUGBY', 'Rugby');
INSERT INTO t_sport (spt_id, spt_code, spt_name) VALUES (3, 'BASKETBALL', 'Basketball');
INSERT INTO t_sport (spt_id, spt_code, spt_name) VALUES (4, 'VOLLEYBALL', 'Volleyball');
INSERT INTO t_sport (spt_id, spt_code, spt_name) VALUES (5, 'HANDBALL', 'Handball');
INSERT INTO t_sport (spt_id, spt_code, spt_name) VALUES (6, 'TENNIS', 'Tennis');
INSERT INTO t_sport (spt_id, spt_code, spt_name) VALUES (7, 'HOCKEY', 'Hockey');
INSERT INTO t_sport (spt_id, spt_code, spt_name) VALUES (8, 'BASEBALL', 'Baseball');
INSERT INTO t_sport (spt_id, spt_code, spt_name) VALUES (9, 'HIKING', 'Hiking');
INSERT INTO t_sport (spt_id, spt_code, spt_name) VALUES (99, 'OTHER', 'Another sport');

COMMIT;