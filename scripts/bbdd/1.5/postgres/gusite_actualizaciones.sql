ALTER TABLE gus_micros ADD COLUMN mic_indexado numeric(1,0) DEFAULT 0;
ALTER TABLE gus_micros ADD COLUMN mic_idxcor numeric(2,0) DEFAULT -1;
ALTER TABLE gus_micros ADD COLUMN mic_menori varchar2(1)  DEFAULT 'C';
ALTER TABLE gus_conten ADD COLUMN con_plantilla numeric DEFAULT -1;
INSERT INTO gus_musuar (msu_codi, msu_userna, msu_passwo, msu_nombre, msu_observ, msu_perfil) VALUES (1000001, 'admin', 'admin', 'Usuario administrador', 'admin@gusite.es', 'gusadmin');