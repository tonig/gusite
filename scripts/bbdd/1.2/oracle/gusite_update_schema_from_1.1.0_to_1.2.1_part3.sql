-----------------------------------
-- INICIO DEPENDENCIA 1 PARTE 2-3

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
  
-- FIN DEPENDENCIA 1 PARTE 2-3  
--------------------------------

  
  
----------------------------------------
--INICIO DEPENDENCIA 2 PARTE 1-2-3
  
ALTER table "GUS_CONIDI" MODIFY (
    "CID_URI" NOT NULL
);
   
ALTER table "GUS_ENCIDI" MODIFY (
    "EID_URI" NOT NULL
);

ALTER table "GUS_MICROS" MODIFY (
    "MIC_URI" NOT NULL CONSTRAINT UQ_GUS_MICROS_2 UNIQUE
);
    
ALTER table "GUS_NOTIDI" MODIFY (
    "NID_URI" NOT NULL
);
        
ALTER table "GUS_TPNIDI" MODIFY (
    "TPI_URI" NOT NULL
);    
    
-- Create Indexes section

Create Index "GUS_CIDURI_I" ON "GUS_CONIDI" ("CID_CODIDI","CID_URI"); 

Create Index "GUS_EIDURI_I" ON "GUS_ENCIDI" ("EID_CODIDI","EID_URI"); 

Create Index "GUS_NIDURI_I" ON "GUS_NOTIDI" ("NID_CODIDI","NID_URI"); 

Create Index "GUS_TPNURI_I" ON "GUS_TPNIDI" ("TPI_CODIDI","TPI_URI");    
    
-- FIN DEPENDENCIA 2 PARTE 1-2-3
-------------------------------------    
    
    
-------------------------------------------------
-- redmine #874 Persistencia de los temas y extensiones por sitio web.
------------------------------------------------

ALTER table "GUS_MICROS" ADD (
	"MIC_FTECOD" Number(19,0),
	"MIC_DESARROLLO" Varchar2 (1) Default 'N' NOT NULL 
);

ALTER table "GUS_COMPOS" ADD  (
	"CMP_PPLCOD" Number(19,0)
); 

ALTER table "GUS_CONTEN" ADD (
	"CON_PPLCOD" Number(19,0)
); 

ALTER table "GUS_TPNOTI" ADD (
	"TPN_PPLCOD" Number(19,0)
); 

Create table "GUS_FR_TEMA" (
	"FTE_CODI" Number(19,0) NOT NULL ,
	"FTE_VERSION" Varchar2 (2) NOT NULL ,
	"FTE_TEMA_PADRE" Number(19,0) ,
	"FTE_NOMBRE" Varchar2 (255) NOT NULL  UNIQUE ,
	"FTE_CSS" Number,
	"FTE_ACTUALIZACION" Date Default SYSDATE NOT NULL ,
	"FTE_URI" Varchar2 (32 CHAR) NOT NULL  UNIQUE ,
 Constraint "GUS_FTE_PK" primary key ("FTE_CODI") 
); 


Create table "GUS_FR_PLANTILLA" (
	"PLA_CODI" Number(19,0) NOT NULL ,
	"PLA_VERSION" Varchar2 (2) NOT NULL ,
	"PLA_NOMBRE" Varchar2 (255) NOT NULL  UNIQUE ,
	"PLA_DESCRIPCION" Clob,
	"PLA_TITULO" VARCHAR2(255) NOT NULL,
 Constraint "GUS_PLA_PK" primary key ("PLA_CODI") 
); 

Create table "GUS_FR_VERSION" (
	"FVE_VERSION" Varchar2 (2) NOT NULL ,
	"FVE_NOMBRE" Varchar2 (255) NOT NULL  UNIQUE ,
 Constraint "GUS_FVE_PK" primary key ("FVE_VERSION") 
); 

Create table "GUS_FR_PERPLA" (
	"PPL_CODI" Number(19,0) NOT NULL ,
	"PPL_TITULO" Varchar2 (255) NOT NULL ,
	"PPL_PLACOD" Number(19,0) NOT NULL ,
	"PPL_ORDEN" Integer,
	"PPL_FTECOD" Number(19,0),
	"PPL_MICCOD" Number,
	"PPL_CONTENIDO" Clob,
 Constraint "GUS_PLT_PK" primary key ("PPL_CODI") 
); 

Create table "GUS_FR_ARCHIVO" (
	"ARC_CODI" Number NOT NULL ,
	"ARC_FTECOD" Number(19,0) NOT NULL ,
	"ARC_PATH" Varchar2 (256 CHAR),
	"ARC_DOCCOD" Number,
 Constraint "GUS_ARC_PK" primary key ("ARC_CODI") 
); 


Create UNIQUE Index "GUS_ARC_PATH_I" ON "GUS_FR_ARCHIVO" ("ARC_FTECOD","ARC_PATH"); 



-- Create Foreign keys section
Create Index "GUS_FTE_DOC_I" ON "GUS_FR_TEMA" ("FTE_CSS");

Alter table "GUS_FR_TEMA" add Constraint "GUS_FTE_DOC_FK" foreign key ("FTE_CSS") references "GUS_DOCUS" ("DCM_CODI"); 

Create Index "GUS_ARC_DCM_I" ON "GUS_FR_ARCHIVO" ("ARC_DOCCOD");

Alter table "GUS_FR_ARCHIVO" add Constraint "GUS_ARC_DCM_FK" foreign key ("ARC_DOCCOD") references "GUS_DOCUS" ("DCM_CODI"); 

Create Index "GUS_MIC_PLT_I" ON "GUS_FR_PERPLA" ("PPL_MICCOD");

Alter table "GUS_FR_PERPLA" add Constraint "GUS_MIC_PLT_FK" foreign key ("PPL_MICCOD") references "GUS_MICROS" ("MIC_CODI"); 

Create Index "GUS_FTE_FTE_I" ON "GUS_FR_TEMA" ("FTE_TEMA_PADRE");

Alter table "GUS_FR_TEMA" add Constraint "GUS_FTE_FTE_FK" foreign key ("FTE_TEMA_PADRE") references "GUS_FR_TEMA" ("FTE_CODI"); 

Create Index "GUS_FTE_PLT_I" ON "GUS_FR_PERPLA" ("PPL_FTECOD");

Alter table "GUS_FR_PERPLA" add Constraint "GUS_FTE_PLT_FK" foreign key ("PPL_FTECOD") references "GUS_FR_TEMA" ("FTE_CODI"); 

Create Index "GUS_FTE_ARC_I" ON "GUS_FR_ARCHIVO" ("ARC_FTECOD");

Alter table "GUS_FR_ARCHIVO" add Constraint "GUS_FTE_ARC_FK" foreign key ("ARC_FTECOD") references "GUS_FR_TEMA" ("FTE_CODI"); 

Create Index "GUS_FTE_MIC_I" ON "GUS_MICROS" ("MIC_FTECOD");

Alter table "GUS_MICROS" add Constraint "GUS_FTE_MIC_FK" foreign key ("MIC_FTECOD") references "GUS_FR_TEMA" ("FTE_CODI"); 

Create Index "GUS_PLA_PLT_I" ON "GUS_FR_PERPLA" ("PPL_PLACOD");

Alter table "GUS_FR_PERPLA" add Constraint "GUS_PLA_PLT_FK" foreign key ("PPL_PLACOD") references "GUS_FR_PLANTILLA" ("PLA_CODI"); 

Create Index "GUS_FVE_FAR_I" ON "GUS_FR_PLANTILLA" ("PLA_VERSION");

Alter table "GUS_FR_PLANTILLA" add Constraint "GUS_FVE_FAR_FK" foreign key ("PLA_VERSION") references "GUS_FR_VERSION" ("FVE_VERSION"); 

Create Index "GUS_FVE_FTE_I" ON "GUS_FR_TEMA" ("FTE_VERSION");

Alter table "GUS_FR_TEMA" add Constraint "GUS_FVE_FTE_FK" foreign key ("FTE_VERSION") references "GUS_FR_VERSION" ("FVE_VERSION"); 

Create Index "GUS_PPL_CON_I" ON "GUS_CONTEN" ("CON_PPLCOD");

Alter table "GUS_CONTEN" add Constraint "GUS_PPL_CON_FK" foreign key ("CON_PPLCOD") references "GUS_FR_PERPLA" ("PPL_CODI"); 

Create Index "GUS_PPL_TPN_I" ON "GUS_TPNOTI" ("TPN_PPLCOD");

Alter table "GUS_TPNOTI" add Constraint "GUS_PPL_TPN_FK" foreign key ("TPN_PPLCOD") references "GUS_FR_PERPLA" ("PPL_CODI"); 

Create Index "GUS_PPL_CMP_I" ON "GUS_COMPOS" ("CMP_PPLCOD");

Alter table "GUS_COMPOS" add Constraint "GUS_PPL_CMP_FK" foreign key ("CMP_PPLCOD") references "GUS_FR_PERPLA" ("PPL_CODI");     
    
-- Indices para mejorar el rendimiento #41
CREATE INDEX "GUS_MICROS_IMAGEN_I" ON "GUS_MICROS" ("MIC_IMAGEN");
CREATE INDEX "GUS_MICROS_IMGCAM_I" ON "GUS_MICROS" ("MIC_IMGCAM");
CREATE INDEX "GUS_MICROS_CSS_I"    ON "GUS_MICROS" ("MIC_CSS");
CREATE INDEX "GUS_NOTICS_IMAGEN_I" ON "GUS_NOTICS" ("NOT_IMAGEN");
CREATE INDEX "GUS_PREGUN_IMAGEN_I" ON "GUS_PREGUN" ("PRE_IMAGEN");
CREATE INDEX "GUS_COMPOS_IMGBUL_I" ON "GUS_COMPOS" ("CMP_IMGBUL");
CREATE INDEX "GUS_AGEIDI_DOCU_I"   ON "GUS_AGEIDI" ("AID_DOCU");
CREATE INDEX "GUS_AGEIDI_IMAGEN_I" ON "GUS_AGEIDI" ("AID_IMAGEN");
CREATE INDEX "GUS_MENU_IMGMEN_I"   ON "GUS_MENU"   ("MNU_IMGMEN");
CREATE INDEX "GUS_BANIDI_IMAGEN_I" ON "GUS_BANIDI" ("BID_IMAGEN");


-- Create Table comments section
Comment on table "GUS_FR_TEMA" is 'Tema del front';

Comment on table "GUS_FR_PLANTILLA" is 'Tabla que identifica las plantillas que se pueden implementar (sobreescribir) para una versión determinada de front';

Comment on table "GUS_FR_VERSION" is 'Versión de front / identifica las versiones para su implementación en temas';

Comment on table "GUS_FR_PERPLA" is 'Personalización de una plantilla';

Comment on table "GUS_FR_ARCHIVO" is 'Tabla que contiene los archivos (imágenes, js, etc.) utilizados en los distintos elementos de un tema';


-- Create Attribute comments section

Comment on column "GUS_FR_TEMA"."FTE_NOMBRE" is 'Nom del tema';

Comment on column "GUS_FR_TEMA"."FTE_CSS" is 'CSS General del tema';

Comment on column "GUS_FR_TEMA"."FTE_ACTUALIZACION" is 'Data d''actualització del tema';

Comment on column "GUS_FR_PLANTILLA"."PLA_NOMBRE" is 'Nombre (identificador) de la plantilla';

Comment on column "GUS_FR_PLANTILLA"."PLA_DESCRIPCION" is 'Descripción y documentación de la plantilla';

Comment on column "GUS_FR_PLANTILLA"."PLA_TITULO" is 'Título de la plantilla';

Comment on column "GUS_FR_VERSION"."FVE_VERSION" is 'Versión de GUSITE para la que se ha desarrollado el tema';

Comment on column "GUS_FR_VERSION"."FVE_NOMBRE" is 'Nom del tema';

Comment on column "GUS_FR_PERPLA"."PPL_TITULO" is 'Título de la plantilla';

Comment on column "GUS_FR_PERPLA"."PPL_ORDEN" is 'Número de orden de la personalización de plantilla';

Comment on column "GUS_FR_PERPLA"."PPL_CONTENIDO" is 'Contenido de la plantilla';

Comment on column "GUS_FR_ARCHIVO"."ARC_CODI" is 'Identificador de documento';

Comment on column "GUS_FR_ARCHIVO"."ARC_PATH" is 'Ruta virtual del archivo';

-- SEQUÈNCIES

CREATE SEQUENCE "GUS_SEQFTE";

CREATE SEQUENCE "GUS_SEQPLA";

CREATE SEQUENCE "GUS_SEQFVE";

CREATE SEQUENCE "GUS_SEQPPL";

CREATE SEQUENCE "GUS_SEQARC";

create or replace
Function GUS_NEXTVAL ( secuencia IN varchar2 ) RETURN  varchar2
IS
 dynsql VARCHAR2(1000);
 ret NUMBER;

BEGIN

  dynsql := 'SELECT ' || secuencia || '.NEXTVAL FROM DUAL';
  EXECUTE IMMEDIATE dynsql into ret;

RETURN ret;

END;
/
    
    