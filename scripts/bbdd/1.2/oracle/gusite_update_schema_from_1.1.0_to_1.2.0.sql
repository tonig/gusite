
-- Eliminar funcionalitats de banners 
DELETE FROM GUS_TIPSER WHERE TPS_CODI = 3;

DROP TABLE gus_banidi cascade constraints;
DROP TABLE gus_banner cascade constraints;

-- Quitamos el servicio 3 de los servicios ofrecidos de los microsites
UPDATE gus_micros
  SET MIC_SEROFR = SUBSTR(MIC_SEROFR, 1, INSTR(MIC_SEROFR,'3;')-1) || SUBSTR(MIC_SEROFR, INSTR(MIC_SEROFR,'3;')+2)
WHERE
  INSTR(MIC_SEROFR,'3;') != 0;
  
-- Eliminar funcionalitat de procediments
DELETE FROM GUS_TIPSER WHERE TPS_CODI = 6;

DROP TABLE gus_micpro cascade constraints;

-- Quitamos el servicio 6 de los servicios ofrecidos de los microsites
UPDATE gus_micros
  SET MIC_SEROFR = SUBSTR(MIC_SEROFR, 1, INSTR(MIC_SEROFR,'6;')-1) || SUBSTR(MIC_SEROFR, INSTR(MIC_SEROFR,'6;')+2)
WHERE
  INSTR(MIC_SEROFR,'6;') != 0;
  
  
-- Un bug en las versiones anteriores hacía que el servicio 100 se repitiese como 9 veces
UPDATE gus_micros
  SET MIC_SEROFR = SUBSTR(MIC_SEROFR, 1, INSTR(MIC_SEROFR,'100;')-1) || SUBSTR(MIC_SEROFR, INSTR(MIC_SEROFR,'100;')+4)
WHERE
  LENGTH(MIC_SEROFR) - LENGTH(REPLACE(MIC_SEROFR, '100', '10')) > 1
  AND
  INSTR(MIC_SEROFR,'100;') != 0;
UPDATE gus_micros
  SET MIC_SEROFR = SUBSTR(MIC_SEROFR, 1, INSTR(MIC_SEROFR,'100;')-1) || SUBSTR(MIC_SEROFR, INSTR(MIC_SEROFR,'100;')+4)
WHERE
  LENGTH(MIC_SEROFR) - LENGTH(REPLACE(MIC_SEROFR, '100', '10')) > 1
  AND
  INSTR(MIC_SEROFR,'100;') != 0;
UPDATE gus_micros
  SET MIC_SEROFR = SUBSTR(MIC_SEROFR, 1, INSTR(MIC_SEROFR,'100;')-1) || SUBSTR(MIC_SEROFR, INSTR(MIC_SEROFR,'100;')+4)
WHERE
  LENGTH(MIC_SEROFR) - LENGTH(REPLACE(MIC_SEROFR, '100', '10')) > 1
  AND
  INSTR(MIC_SEROFR,'100;') != 0;
UPDATE gus_micros
  SET MIC_SEROFR = SUBSTR(MIC_SEROFR, 1, INSTR(MIC_SEROFR,'100;')-1) || SUBSTR(MIC_SEROFR, INSTR(MIC_SEROFR,'100;')+4)
WHERE
  LENGTH(MIC_SEROFR) - LENGTH(REPLACE(MIC_SEROFR, '100', '10')) > 1
  AND
  INSTR(MIC_SEROFR,'100;') != 0;
UPDATE gus_micros
  SET MIC_SEROFR = SUBSTR(MIC_SEROFR, 1, INSTR(MIC_SEROFR,'100;')-1) || SUBSTR(MIC_SEROFR, INSTR(MIC_SEROFR,'100;')+4)
WHERE
  LENGTH(MIC_SEROFR) - LENGTH(REPLACE(MIC_SEROFR, '100', '10')) > 1
  AND
  INSTR(MIC_SEROFR,'100;') != 0;
UPDATE gus_micros
  SET MIC_SEROFR = SUBSTR(MIC_SEROFR, 1, INSTR(MIC_SEROFR,'100;')-1) || SUBSTR(MIC_SEROFR, INSTR(MIC_SEROFR,'100;')+4)
WHERE
  LENGTH(MIC_SEROFR) - LENGTH(REPLACE(MIC_SEROFR, '100', '10')) > 1
  AND
  INSTR(MIC_SEROFR,'100;') != 0;
UPDATE gus_micros
  SET MIC_SEROFR = SUBSTR(MIC_SEROFR, 1, INSTR(MIC_SEROFR,'100;')-1) || SUBSTR(MIC_SEROFR, INSTR(MIC_SEROFR,'100;')+4)
WHERE
  LENGTH(MIC_SEROFR) - LENGTH(REPLACE(MIC_SEROFR, '100', '10')) > 1
  AND
  INSTR(MIC_SEROFR,'100;') != 0;
UPDATE gus_micros
  SET MIC_SEROFR = SUBSTR(MIC_SEROFR, 1, INSTR(MIC_SEROFR,'100;')-1) || SUBSTR(MIC_SEROFR, INSTR(MIC_SEROFR,'100;')+4)
WHERE
  LENGTH(MIC_SEROFR) - LENGTH(REPLACE(MIC_SEROFR, '100', '10')) > 1
  AND
  INSTR(MIC_SEROFR,'100;') != 0;
UPDATE gus_micros
  SET MIC_SEROFR = SUBSTR(MIC_SEROFR, 1, INSTR(MIC_SEROFR,'100;')-1) || SUBSTR(MIC_SEROFR, INSTR(MIC_SEROFR,'100;')+4)
WHERE
  LENGTH(MIC_SEROFR) - LENGTH(REPLACE(MIC_SEROFR, '100', '10')) > 1
  AND
  INSTR(MIC_SEROFR,'100;') != 0;
UPDATE gus_micros
  SET MIC_SEROFR = SUBSTR(MIC_SEROFR, 1, INSTR(MIC_SEROFR,'100;')-1) || SUBSTR(MIC_SEROFR, INSTR(MIC_SEROFR,'100;')+4)
WHERE
  LENGTH(MIC_SEROFR) - LENGTH(REPLACE(MIC_SEROFR, '100', '10')) > 1
  AND
  INSTR(MIC_SEROFR,'100;') != 0;
UPDATE gus_micros
  SET MIC_SEROFR = SUBSTR(MIC_SEROFR, 1, INSTR(MIC_SEROFR,'100;')-1) || SUBSTR(MIC_SEROFR, INSTR(MIC_SEROFR,'100;')+4)
WHERE
  LENGTH(MIC_SEROFR) - LENGTH(REPLACE(MIC_SEROFR, '100', '10')) > 1
  AND
  INSTR(MIC_SEROFR,'100;') != 0;
UPDATE gus_micros
  SET MIC_SEROFR = SUBSTR(MIC_SEROFR, 1, INSTR(MIC_SEROFR,'100;')-1) || SUBSTR(MIC_SEROFR, INSTR(MIC_SEROFR,'100;')+4)
WHERE
  LENGTH(MIC_SEROFR) - LENGTH(REPLACE(MIC_SEROFR, '100', '10')) > 1
  AND
  INSTR(MIC_SEROFR,'100;') != 0;
  

-- Corregimos algunos "not null" de versiones anteriores 
 ALTER TABLE GUS_ACTIVI modify (ACT_MICCOD not null);
 ALTER TABLE GUS_AGENDA modify (AGE_MICCOD not null);
 ALTER TABLE GUS_COMPOS modify (CMP_MICCOD not null);
 ALTER TABLE GUS_CONVOCATORIA modify (MICROSITE_ID not null);
 ALTER TABLE GUS_ENCUST modify (ENC_MICCOD not null);
 ALTER TABLE GUS_FAQ modify (FAQ_MICCOD not null);
 ALTER TABLE GUS_LDISTRIBUCION modify (MICROSITE_ID not null);
 ALTER TABLE GUS_MENU modify (MNU_MICCOD not null);
 ALTER TABLE GUS_MICUSU modify (MIU_CODMIC not null);
 ALTER TABLE GUS_NOTICS modify (NOT_MICCOD not null);
 ALTER TABLE GUS_TEMAS modify (TEM_MICCOD not null);
 
-------------------------------------------------
-- redmine #647 Migración a JPA: Foreign keys corregidas
------------------------------------------------

-- Eliminamos previamente las tuplas huérfanas
DELETE FROM GUS_ACTIDI WHERE ATI_CODIDI NOT IN (SELECT IDI_CODI FROM GUS_IDIOMA);
DELETE FROM GUS_AGEIDI WHERE AID_CODIDI NOT IN (SELECT IDI_CODI FROM GUS_IDIOMA);
DELETE FROM GUS_CMPIDI WHERE CPI_CODIDI NOT IN (SELECT IDI_CODI FROM GUS_IDIOMA);
DELETE FROM GUS_CONIDI WHERE CID_CODIDI NOT IN (SELECT IDI_CODI FROM GUS_IDIOMA);
DELETE FROM GUS_ENCIDI WHERE EID_CODIDI NOT IN (SELECT IDI_CODI FROM GUS_IDIOMA);
DELETE FROM GUS_FAQIDI WHERE FID_CODIDI NOT IN (SELECT IDI_CODI FROM GUS_IDIOMA);
DELETE FROM GUS_FRMIDI WHERE RID_CODIDI NOT IN (SELECT IDI_CODI FROM GUS_IDIOMA);
DELETE FROM GUS_FRQIDI WHERE FQI_CODIDI NOT IN (SELECT IDI_CODI FROM GUS_IDIOMA);
DELETE FROM GUS_MICIDI WHERE MID_CODIDI NOT IN (SELECT IDI_CODI FROM GUS_IDIOMA);
DELETE FROM GUS_MNUIDI WHERE MDI_CODIDI NOT IN (SELECT IDI_CODI FROM GUS_IDIOMA);
DELETE FROM GUS_NOTIDI WHERE NID_CODIDI NOT IN (SELECT IDI_CODI FROM GUS_IDIOMA);
DELETE FROM GUS_PREIDI WHERE PID_CODIDI NOT IN (SELECT IDI_CODI FROM GUS_IDIOMA);
DELETE FROM GUS_RESIDI WHERE REI_CODIDI NOT IN (SELECT IDI_CODI FROM GUS_IDIOMA);
DELETE FROM GUS_TEMIDI WHERE TID_CODIDI NOT IN (SELECT IDI_CODI FROM GUS_IDIOMA);
DELETE FROM GUS_TPNIDI WHERE TPI_CODIDI NOT IN (SELECT IDI_CODI FROM GUS_IDIOMA);

 ALTER TABLE GUS_ACTIDI ADD (
  CONSTRAINT GUS_ATIIDI_FK 
  FOREIGN KEY (ATI_CODIDI) 
  REFERENCES GUS_IDIOMA (IDI_CODI)
  ENABLE VALIDATE);

ALTER TABLE GUS_AGEIDI ADD (
  CONSTRAINT GUS_AIDIDI_FK 
  FOREIGN KEY (AID_CODIDI) 
  REFERENCES GUS_IDIOMA (IDI_CODI)
  ENABLE VALIDATE);
  
ALTER TABLE GUS_CMPIDI ADD (
  CONSTRAINT GUS_CPIIDI_FK 
  FOREIGN KEY (CPI_CODIDI) 
  REFERENCES GUS_IDIOMA (IDI_CODI)
  ENABLE VALIDATE);
  
ALTER TABLE GUS_CONIDI ADD (
  CONSTRAINT GUS_CIDIDI_FK 
  FOREIGN KEY (CID_CODIDI) 
  REFERENCES GUS_IDIOMA (IDI_CODI)
  ENABLE VALIDATE);
  
ALTER TABLE GUS_ENCIDI ADD (
  CONSTRAINT GUS_EIDIDI_FK 
  FOREIGN KEY (EID_CODIDI) 
  REFERENCES GUS_IDIOMA (IDI_CODI)
  ENABLE VALIDATE);
  
ALTER TABLE GUS_FAQIDI ADD (
  CONSTRAINT GUS_FIDIDI_FK 
  FOREIGN KEY (FID_CODIDI) 
  REFERENCES GUS_IDIOMA (IDI_CODI)
  ENABLE VALIDATE);

ALTER TABLE GUS_FRMIDI ADD (
  CONSTRAINT GUS_RIDIDI_FK 
  FOREIGN KEY (RID_CODIDI) 
  REFERENCES GUS_IDIOMA (IDI_CODI)
 ENABLE VALIDATE);
  
ALTER TABLE GUS_FRQIDI ADD (
  CONSTRAINT GUS_FQIIDI_FK 
  FOREIGN KEY (FQI_CODIDI) 
  REFERENCES GUS_IDIOMA (IDI_CODI)
  ENABLE VALIDATE);
  
ALTER TABLE GUS_MICIDI ADD (
  CONSTRAINT GUS_MIDIDI_FK 
  FOREIGN KEY (MID_CODIDI) 
  REFERENCES GUS_IDIOMA (IDI_CODI)
  ENABLE VALIDATE);
  
ALTER TABLE GUS_MNUIDI ADD (
  CONSTRAINT GUS_MDIIDI_FK 
  FOREIGN KEY (MDI_CODIDI) 
  REFERENCES GUS_IDIOMA (IDI_CODI)
  ENABLE VALIDATE);
  
ALTER TABLE GUS_NOTIDI ADD (
  CONSTRAINT GUS_NIDIDI_FK 
  FOREIGN KEY (NID_CODIDI) 
  REFERENCES GUS_IDIOMA (IDI_CODI)
  ENABLE VALIDATE);
  
ALTER TABLE GUS_PREIDI ADD (
  CONSTRAINT GUS_PIDIDI_FK 
  FOREIGN KEY (PID_CODIDI) 
  REFERENCES GUS_IDIOMA (IDI_CODI)
  ENABLE VALIDATE);

ALTER TABLE GUS_RESIDI ADD (
  CONSTRAINT GUS_REIIDI_FK 
  FOREIGN KEY (REI_CODIDI) 
  REFERENCES GUS_IDIOMA (IDI_CODI)
  ENABLE VALIDATE);
  
ALTER TABLE GUS_TEMIDI ADD (
  CONSTRAINT GUS_TIDIDI_FK 
  FOREIGN KEY (TID_CODIDI) 
  REFERENCES GUS_IDIOMA (IDI_CODI)
  ENABLE VALIDATE);

ALTER TABLE GUS_TPNIDI ADD (
  CONSTRAINT GUS_TPIIDI_FK 
  FOREIGN KEY (TPI_CODIDI) 
  REFERENCES GUS_IDIOMA (IDI_CODI)
  ENABLE VALIDATE);

-------------------------------------------------
-- redmine #659 Cambios auditoria
------------------------------------------------

Create table "GUS_AUDITORIA" (
	"AUD_CODI" Number(19,0) NOT NULL ,
	"AUD_MICCOD" Number,
	"AUD_USUARI" Varchar2 (256 CHAR),
	"AUD_FECHA" Date,
	"AUD_OPERACION" Number(1,0),
	"AUD_ENTIDAD" Varchar2 (50 CHAR) NOT NULL ,
	"AUD_INFORMACION" Clob,
	"AUD_ID_ENTIDAD" Varchar2 (255 CHAR),
primary key ("AUD_CODI") 
) 
STORAGE(
	INITIAL 5888K
	MAXEXTENTS 2147483645
	BUFFER_POOL DEFAULT
	)
LOGGING

/

Alter table "GUS_AUDITORIA" add  foreign key ("AUD_MICCOD") references "GUS_MICROS" ("MIC_CODI")  on delete set null
/

Create Index "GUS_AUDMIC_I" ON "GUS_AUDITORIA" ("AUD_MICCOD") 
/

CREATE SEQUENCE "GUS_SEQAUD"
/


-------------------------------------------------
-- redmine #655 Cambios SEO
------------------------------------------------

--Convierte una cadena a un formato utilizable en url.
--Por ejemplo, convertiría:
--    "Perfil del t(u)rista britànic 2002´''"
--     a:
--     Perfil_del_turista_britanic_2002

CREATE OR REPLACE Function GUS_STRING2URI_F ( nombre IN varchar2 ) RETURN  varchar2
IS
 tmp VARCHAR2(1000);
 uri VARCHAR2(1000);
 origin VARCHAR2(1000);
 dest VARCHAR2(1000);
 validAbc VARCHAR2(1000);
 charAtI VARCHAR2(1000);

BEGIN

  origin     := 'ÁÉÍÓÚÀÈÌÒÙÄËÏÖÜÂÊÎÔÛÃÕÑÝÇáéíóúàèìòùäëïöüâêîôûãõñýç ';
  dest       := 'AEIOUAEIOUAEIOUAEIOUAONYCaeiouaeiouaeiouaeiouaonyc_';
  validAbc   := 'ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789_-';

  IF (nombre IS null) THEN
    RETURN '';
  END IF;

  tmp := lower(translate(
                    nombre,
                    origin,
                    dest)
                    );

  uri := '';
  FOR i IN 1..length(tmp) LOOP
    -- filtramos los caracteres no validos
    charAtI := substr(tmp, i, 1);
    IF INSTR (validAbc, charAtI) != 0 THEN
      uri := uri || substr(tmp, i, 1);
    END IF;
  END LOOP;

RETURN uri;

END;
/

ALTER table "GUS_CONIDI" ADD (
    "CID_URI" Varchar2 (270 CHAR)
    )
/

UPDATE GUS_CONIDI SET CID_URI = 
  CASE 
    WHEN CID_TITULO IS NULL THEN CID_CONCOD  || ''
  ELSE
    GUS_STRING2URI_F(CID_TITULO) || '-' || CID_CONCOD  
  END;
/  

ALTER table "GUS_CONIDI" MODIFY (
    "CID_URI" NOT NULL
    )
/

ALTER table "GUS_ENCIDI" ADD (
	"EID_URI" Varchar2 (270 CHAR)
	) 
/

UPDATE GUS_ENCIDI SET EID_URI = 
  CASE 
    WHEN EID_TITULO IS NULL THEN EID_ENCCOD  || ''
  ELSE
    GUS_STRING2URI_F(EID_TITULO) || '-' || EID_ENCCOD  
  END;
/  

ALTER table "GUS_ENCIDI" MODIFY (
    "EID_URI" NOT NULL
    )
/



ALTER table "GUS_MICROS" ADD (
	"MIC_URI" Varchar2 (32 CHAR),
	"MIC_ANALYTICS" Varchar2 (60 CHAR)
) 
/

UPDATE GUS_MICROS SET MIC_URI = MIC_CLAVE;

ALTER table "GUS_MICROS" MODIFY (
    "MIC_URI" NOT NULL CONSTRAINT UQ_GUS_MICROS_2 UNIQUE
    )
/



ALTER table "GUS_MICIDI" ADD (
	"MID_KEYWORDS" Varchar2 (1000 CHAR),
	"MID_DESCRIPTION" Varchar2 (4000 CHAR)
) 
/

ALTER table "GUS_NOTIDI" ADD (
	"NID_URI" Varchar2 (530 CHAR)
) 
/

UPDATE GUS_NOTIDI SET NID_URI = 
  CASE 
    WHEN NID_TITULO IS NULL THEN NID_NOTCOD  || ''
  ELSE
    GUS_STRING2URI_F(NID_TITULO) || '-' || NID_NOTCOD  
  END;
/  

ALTER table "GUS_NOTIDI" MODIFY (
    "NID_URI" NOT NULL
    )
/

ALTER table "GUS_TPNIDI" ADD (
	"TPI_URI" Varchar2 (125 CHAR)
) 
/

UPDATE GUS_TPNIDI SET TPI_URI = 
  CASE 
    WHEN TPI_NOMBRE IS NULL THEN TPI_TIPCOD  || ''
  ELSE
    GUS_STRING2URI_F(TPI_NOMBRE) || '-' || TPI_TIPCOD  
  END;
/  

ALTER table "GUS_TPNIDI" MODIFY (
    "TPI_URI" NOT NULL
    )
/


-- Create Indexes section

Create UNIQUE Index "GUS_CIDURI_I" ON "GUS_CONIDI" ("CID_CODIDI","CID_URI") 
/
Create UNIQUE Index "GUS_EIDURI_I" ON "GUS_ENCIDI" ("EID_CODIDI","EID_URI") 
/
Create UNIQUE Index "GUS_NIDURI_I" ON "GUS_NOTIDI" ("NID_CODIDI","NID_URI") 
/
Create UNIQUE Index "GUS_TPNURI_I" ON "GUS_TPNIDI" ("TPI_CODIDI","TPI_URI") 
/

-- Actualizar los Roles
UPDATE GUS_MUSUAR SET msu_perfil = REPLACE(msu_perfil, 'sacsuper', 'gussuper');

UPDATE GUS_MUSUAR SET msu_perfil = REPLACE(msu_perfil, 'sacoper', 'gusoper');

UPDATE GUS_MUSUAR SET msu_perfil = REPLACE(msu_perfil, 'sacadmin', 'gusadmin');

UPDATE GUS_MUSUAR SET msu_perfil = REPLACE(msu_perfil, 'sacsystem', 'gussystem');

-- Añadir nuevos campos en el microsite para el control de las versiones
ALTER TABLE GUS_MICROS ADD MIC_VERSION VARCHAR2(2);

ALTER TABLE GUS_MICROS ADD MIC_TIPO_ACCESO VARCHAR2(1);

-- Actualizamos a versión 5 los microsites antiguos sin css personalizado
UPDATE GUS_MICROS SET MIC_RESTRI = '5' WHERE MIC_RESTRI = '2' AND MIC_CSS is null;

-- Fijar los valores iniciales de los sitios antiguos
UPDATE GUS_MICROS SET MIC_VERSION = 'v1', MIC_TIPO_ACCESO = 'P' WHERE MIC_RESTRI = 'N';
UPDATE GUS_MICROS SET MIC_VERSION = 'v1', MIC_TIPO_ACCESO = 'R' WHERE MIC_RESTRI = 'S';
UPDATE GUS_MICROS SET MIC_VERSION = 'v4', MIC_TIPO_ACCESO = 'P' WHERE MIC_RESTRI = '2';
UPDATE GUS_MICROS SET MIC_VERSION = 'v5', MIC_TIPO_ACCESO = 'P' WHERE MIC_RESTRI = '5';

