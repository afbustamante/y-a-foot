CREATE SEQUENCE s_player INCREMENT BY 1 MINVALUE 1 NO MAXVALUE START WITH 1 NO CYCLE;
CREATE SEQUENCE s_match INCREMENT BY 1 MINVALUE 1 NO MAXVALUE START WITH 1 NO CYCLE;
CREATE SEQUENCE s_site INCREMENT BY 1 MINVALUE 1 NO MAXVALUE START WITH 1 NO CYCLE;
CREATE SEQUENCE s_car INCREMENT BY 1 MINVALUE 1 NO MAXVALUE START WITH 1 NO CYCLE;

CREATE TABLE t_car
(
    car_id        integer      NOT NULL,
    car_name      varchar(255) NOT NULL,
    car_num_seats smallint     NOT NULL,
    car_driver_id integer      NOT NULL
);

CREATE TABLE t_match
(
    mat_id                   integer      NOT NULL,
    mat_date                 timestamp    NOT NULL,
    mat_code                 varchar(12)  NOT NULL,
    mat_description          text,
    mat_status               varchar(10)  NOT NULL,
    mat_num_players_min      integer,
    mat_num_players_max      integer,
    mat_site_id              integer      NOT NULL,
    mat_creator_id           integer      NOT NULL,
    mat_created_at           timestamp    NOT NULL,
    mat_updated_at           timestamp,
    mat_carpooling_enabled   bool         NOT NULL,
    mat_code_sharing_enabled bool         NOT NULL
);

CREATE TABLE t_player
(
    ply_id           integer      NOT NULL,
    ply_surname      varchar(255) NOT NULL,
    ply_first_name   varchar(255) NOT NULL,
    ply_phone_number varchar(16),
    ply_email        varchar(255) NOT NULL,
    ply_active       bool         NOT NULL,
    ply_created_at   timestamp    NOT NULL,
    ply_updated_at   timestamp
);

CREATE TABLE t_player_match
(
    pma_match_id         integer   NOT NULL,
    pma_player_id        integer   NOT NULL,
    pma_car_id           integer,
    pma_car_confirmation bool,
    pma_created_at       timestamp NOT NULL,
    pma_updated_at       timestamp
);

CREATE TABLE t_site
(
    sit_id           integer      NOT NULL,
    sit_name         varchar(255) NOT NULL,
    sit_address      varchar(255) NOT NULL,
    sit_phone_number varchar(16),
    sit_longitude    numeric(9, 6),
    sit_latitude     numeric(9, 6),
    sit_creator_id   integer
);

CREATE UNIQUE INDEX i_match_code ON t_match (mat_code);
CREATE UNIQUE INDEX i_player_email ON t_player (ply_email);

ALTER TABLE t_player
    ADD CONSTRAINT pk_player PRIMARY KEY (ply_id);
ALTER TABLE t_site
    ADD CONSTRAINT pk_site PRIMARY KEY (sit_id);
ALTER TABLE t_site
    ADD CONSTRAINT fk_site_creator FOREIGN KEY (sit_creator_id) REFERENCES t_player (ply_id);
ALTER TABLE t_car
    ADD CONSTRAINT pk_car PRIMARY KEY (car_id);
ALTER TABLE t_car
    ADD CONSTRAINT fk_car_driver FOREIGN KEY (car_driver_id) REFERENCES t_player (ply_id);
ALTER TABLE t_match
    ADD CONSTRAINT pk_match PRIMARY KEY (mat_id);
ALTER TABLE t_match
    ADD CONSTRAINT fk_match_creator FOREIGN KEY (mat_creator_id) REFERENCES t_player (ply_id);
ALTER TABLE t_match
    ADD CONSTRAINT fk_match_site FOREIGN KEY (mat_site_id) REFERENCES t_site (sit_id);
ALTER TABLE t_player_match
    ADD CONSTRAINT pk_player_match PRIMARY KEY (pma_match_id, pma_player_id);
ALTER TABLE t_player_match
    ADD CONSTRAINT fk_registration_car FOREIGN KEY (pma_car_id) REFERENCES t_car (car_id);
ALTER TABLE t_player_match
    ADD CONSTRAINT fk_registration_match FOREIGN KEY (pma_match_id) REFERENCES t_match (mat_id);
ALTER TABLE t_player_match
    ADD CONSTRAINT fk_registration_player FOREIGN KEY (pma_player_id) REFERENCES t_player (ply_id);
