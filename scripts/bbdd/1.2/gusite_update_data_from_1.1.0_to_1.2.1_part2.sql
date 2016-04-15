
-- Eliminar funcionalitats de banners 
DELETE FROM GUS_TIPSER WHERE TPS_CODI = 3;

-- Quitamos el servicio 3 de los servicios ofrecidos de los microsites
UPDATE gus_micros
  SET MIC_SEROFR = SUBSTR(MIC_SEROFR, 1, INSTR(MIC_SEROFR,'3;')-1) || SUBSTR(MIC_SEROFR, INSTR(MIC_SEROFR,'3;')+2)
WHERE
  INSTR(MIC_SEROFR,'3;') != 0;
  
-- Eliminar funcionalitat de procediments
DELETE FROM GUS_TIPSER WHERE TPS_CODI = 6;

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
  
  
-------------------------------------------------
-- redmine #647 Migración a JPA: Foreign keys corregidas
------------------------------------------------

----------------------------------------
-- INICIO DEPENDENCIA 1 PARTE 2-3 
  
-- Eliminamos previamente las tuplas huérfanas ()
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
-- FIN DEPENDENCIA 1 PARTE 2-3  
----------------------------------------
  


----------------------------------------
--INICIO DEPENDENCIA 2 PARTE 1-2-3

UPDATE GUS_CONIDI SET CID_URI = 
  CASE 
    WHEN CID_TITULO IS NULL THEN CID_CONCOD  || ''
  ELSE
    GUS_STRING2URI_F(CID_TITULO) || '-' || CID_CONCOD  
  END;

UPDATE GUS_ENCIDI SET EID_URI = 
  CASE 
    WHEN EID_TITULO IS NULL THEN EID_ENCCOD  || ''
  ELSE
    GUS_STRING2URI_F(EID_TITULO) || '-' || EID_ENCCOD  
  END;
  
UPDATE GUS_MICROS SET MIC_URI = MIC_CLAVE;


UPDATE GUS_NOTIDI SET NID_URI = 
  CASE 
    WHEN NID_TITULO IS NULL THEN NID_NOTCOD  || ''
  ELSE
    GUS_STRING2URI_F(NID_TITULO) || '-' || NID_NOTCOD  
  END;

  
UPDATE GUS_TPNIDI SET TPI_URI = 
  CASE 
    WHEN TPI_NOMBRE IS NULL THEN TPI_TIPCOD  || ''
  ELSE
    GUS_STRING2URI_F(TPI_NOMBRE) || '-' || TPI_TIPCOD  
  END;  
  
-- FIN DEPENDENCIA 2 PARTE 1-2-3
-------------------------------------

  

-- Actualizar los Roles
UPDATE GUS_MUSUAR SET msu_perfil = REPLACE(msu_perfil, 'sacsuper', 'gussuper');

UPDATE GUS_MUSUAR SET msu_perfil = REPLACE(msu_perfil, 'sacoper', 'gusoper');

UPDATE GUS_MUSUAR SET msu_perfil = REPLACE(msu_perfil, 'sacadmin', 'gusadmin');

UPDATE GUS_MUSUAR SET msu_perfil = REPLACE(msu_perfil, 'sacsystem', 'gussystem'); 



-------------------------------------
-- INICIO DEPENDENCIA 3 PARTE 1-2

-- REALIZAMOS EL BACKUP
DELETE FROM GUS_MICROS_BACKUP;
INSERT INTO GUS_MICROS_BACKUP (SELECT MIC_CODI, MIC_RESTRI FROM GUS_MICROS);

-- Se comenta porque se prevee un pase progresivo de los sites a la nueva versión del frontal
--UPDATE GUS_MICROS SET MIC_RESTRI = '5' WHERE MIC_RESTRI = '2' AND MIC_CSS is null;

-- Fijar los valores iniciales de los sitios antiguos
UPDATE GUS_MICROS SET MIC_VERSION = 'v1', MIC_TIPO_ACCESO = 'P' WHERE MIC_RESTRI = 'N';
UPDATE GUS_MICROS SET MIC_VERSION = 'IN', MIC_TIPO_ACCESO = 'R' WHERE MIC_RESTRI = 'S';
UPDATE GUS_MICROS SET MIC_VERSION = 'v4', MIC_TIPO_ACCESO = 'P' WHERE MIC_RESTRI = '2';
UPDATE GUS_MICROS SET MIC_VERSION = 'v5', MIC_TIPO_ACCESO = 'P' WHERE MIC_RESTRI = '5';

UPDATE GUS_MICROS SET MIC_TIPO_ACCESO = 'M' WHERE MIC_ROL IS NOT NULL;


-- FIN DEPENDENCIA 3 PARTE 1-2
-------------------------------------
 