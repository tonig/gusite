--Actualiza el lob a otro tablespace más acorde a los standard.
ALTER TABLE GUS_SOLJOB MOVE LOB(JOB_DESCRI) STORE 			 AS GUS_SOLJOB_DESCRI_LOB (TABLESPACE GUSITE_LOB);
ALTER TABLE GUS_CONVOCATORIA MOVE LOB(TEXTO_MSG) STORE 		 AS GUS_CONVOCATORIA_MSG_LOB (TABLESPACE GUSITE_LOB);
ALTER TABLE GUS_AUDITORIA MOVE LOB(AUD_INFORMACION) STORE 	 AS GUS_AUDITORIA_INFORMACION_LOB (TABLESPACE GUSITE_LOB);
ALTER TABLE GUS_FR_PLANTILLA MOVE LOB(PLA_DESCRIPCION) STORE AS GUS_FR_PLANTILLA_DESCR_LOB (TABLESPACE GUSITE_LOB);
ALTER TABLE GUS_FR_PERPLA MOVE LOB(PPL_CONTENIDO) STORE 	 AS GUS_FR_PERPLA_CONTENIDO_LOB (TABLESPACE GUSITE_LOB);

--Marca la versión como no nulo.
  alter table gus_micros modify mic_version not null;