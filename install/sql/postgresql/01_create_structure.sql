--SET SEARCH_PATH TO yafoot;

CREATE SEQUENCE s_joueur INCREMENT BY 1 MINVALUE 1 NO MAXVALUE NO CYCLE;
CREATE SEQUENCE s_match INCREMENT BY 1 MINVALUE 1 NO MAXVALUE NO CYCLE;
CREATE SEQUENCE s_site INCREMENT BY 1 MINVALUE 1 NO MAXVALUE NO CYCLE;
CREATE SEQUENCE s_voiture INCREMENT BY 1 MINVALUE 1 NO MAXVALUE NO CYCLE;

CREATE TABLE t_joueur (
  jou_id        INT4         NOT NULL,
  jou_nom       VARCHAR(255) NOT NULL,
  jou_prenom    VARCHAR(255) NOT NULL,
  jou_telephone VARCHAR(16),
  jou_email     VARCHAR(255) NOT NULL,
  jou_mdp       VARCHAR(64),
  PRIMARY KEY (jou_id)
);

CREATE TABLE t_joueur_match (
  jma_match_fk   INT4 NOT NULL,
  jma_joueur_fk  INT4 NOT NULL,
  jma_voiture_fk INT4,
  PRIMARY KEY (jma_match_fk, jma_joueur_fk)
);

CREATE TABLE t_match (
  mat_id              INT4        NOT NULL,
  mat_date            DATE        NOT NULL,
  mat_code            VARCHAR(12) NOT NULL,
  mat_description     TEXT,
  mat_num_joueurs_min INT4,
  mat_num_joueurs_max INT4,
  mat_site_fk         INT4        NOT NULL,
  PRIMARY KEY (mat_id)
);

CREATE TABLE t_site (
  sit_id        INT4 NOT NULL,
  sit_nom       VARCHAR(255),
  sit_adresse   VARCHAR(255),
  sit_telephone VARCHAR(16),
  PRIMARY KEY (sit_id)
);

CREATE TABLE t_voiture (
  voi_id           INT4 NOT NULL,
  voi_nom          VARCHAR(255),
  voi_num_places   INT4,
  voi_chauffeur_fk INT4 NOT NULL,
  PRIMARY KEY (voi_id)
);

CREATE UNIQUE INDEX i_joueur_email
  ON t_joueur (jou_email);

CREATE UNIQUE INDEX i_match_code
  ON t_match (mat_code);

ALTER TABLE t_joueur_match
  ADD CONSTRAINT FKt_joueur_m340160 FOREIGN KEY (jma_voiture_fk) REFERENCES t_voiture (voi_id);
ALTER TABLE t_joueur_match
  ADD CONSTRAINT FKt_joueur_m371597 FOREIGN KEY (jma_joueur_fk) REFERENCES t_joueur (jou_id);
ALTER TABLE t_joueur_match
  ADD CONSTRAINT FKt_joueur_m36050 FOREIGN KEY (jma_match_fk) REFERENCES t_match (mat_id);
ALTER TABLE t_match
  ADD CONSTRAINT FKt_match630262 FOREIGN KEY (mat_site_fk) REFERENCES t_site (sit_id);
ALTER TABLE t_voiture
  ADD CONSTRAINT FKt_voiture48620 FOREIGN KEY (voi_chauffeur_fk) REFERENCES t_joueur (jou_id);
