Create table gus_actidi (
    ati_codidi Varchar (6) NOT NULL ,
    ati_actcod Numeric NOT NULL ,
    ati_nombre Varchar (150),
primary key (ati_codidi,ati_actcod) 
);

Create table gus_activi (
    act_codi Numeric NOT NULL ,
    act_miccod Numeric NOT NULL ,
primary key (act_codi) 
);

Create table gus_ageidi (
    aid_codidi Varchar (6) NOT NULL ,
    aid_agecod Numeric NOT NULL ,
    aid_descri Varchar (4000),
    aid_docu Numeric,
    aid_titulo Varchar (256),
    aid_url Varchar (256),
    aid_imagen Numeric,
    aid_urlnom Varchar (512),
primary key (aid_codidi,aid_agecod) 
);

Create table gus_agenda (
    age_codi Numeric NOT NULL ,
    age_miccod Numeric NOT NULL ,
    age_activi Numeric NOT NULL ,
    age_organi Varchar (256),
    age_inicio timestamp NOT NULL ,
    age_fin timestamp,
    age_visib Varchar (1) Default 'S',
primary key (age_codi) 
);

Create table gus_cmpidi (
    cpi_codidi Varchar (6) NOT NULL ,
    cpi_cmpcod Numeric NOT NULL ,
    cpi_titulo Varchar (256),
primary key (cpi_codidi,cpi_cmpcod) 
);

Create table gus_compos (
    cmp_codi Numeric NOT NULL ,
    cmp_tipo Numeric NOT NULL ,
    cmp_miccod Numeric NOT NULL ,
    cmp_nombre Varchar (256) NOT NULL ,
    cmp_soloim Varchar (1) Default 'N',
    cmp_numele Numeric Default 0,
    cmp_orden Numeric,
    cmp_imgbul Numeric,
    cmp_filas Varchar (1) Default 'S',
    cmp_pplcod Numeric(19,0),
primary key (cmp_codi) 
);

Create table gus_conidi (
    cid_codidi Varchar (6) NOT NULL ,
    cid_concod Numeric NOT NULL ,
    cid_titulo Varchar (256),
    cid_texto Text,
    cid_url Varchar (512),
    cid_txbeta Text,
    cid_uri Varchar (270) NOT NULL ,
primary key (cid_codidi,cid_concod) 
);

Create table gus_conten (
    con_codi Numeric NOT NULL ,
    con_caduca timestamp,
    con_public timestamp NOT NULL ,
    con_visib Varchar (1) Default 'S',
    con_orden Numeric,
    con_mnucod Numeric,
    con_imgmen Numeric,
    con_pplcod Numeric(19,0),
primary key (con_codi) 
);

Create table gus_convocatoria (
    codi Numeric(19,0) NOT NULL ,
    nombre Varchar (255),
    descripcion Varchar (255),
    data_envio timestamp,
    microsite_id Numeric,
    reenvio_error Numeric(1,0),
    encuesta_id Numeric(19,0),
    reenvio_confirmados Numeric(1,0),
    pregunta_correo_id Numeric(19,0),
    respuesta_correo_id Numeric,
    pregunta_confirmacion_id Numeric(19,0),
    respuesta_confirmacion_id Numeric,
    otros_dest Text,
    asunto_msg Varchar (255),
    texto_msg Text,
primary key (codi) 
);

Create table gus_correo (
    correo Varchar (255) NOT NULL ,
    noenviar Numeric(1,0),
    nombre Varchar (255),
    apellidos Varchar (512),
    ultimo_envio timestamp,
    error_envio Varchar (2000),
    intento_envio Numeric,
primary key (correo) 
);

Create table gus_distrib_convocatoria (
    convocatoria_id Numeric(19,0) NOT NULL ,
    distrib_id Numeric(19,0) NOT NULL ,
    ultimo_envio timestamp,
    error_envio Varchar (255),
primary key (convocatoria_id,distrib_id) 
);

Create table gus_docus (
    dcm_codi Numeric NOT NULL ,
    dcm_miccod Numeric,
    dcm_datos bytea,
    dcm_nombre Varchar (256),
    dcm_tipo Varchar (256),
    dcm_tamano Numeric,
    dcm_pagina Numeric,
primary key (dcm_codi) 
);

Create table gus_encidi (
    eid_codidi Varchar (6) NOT NULL ,
    eid_enccod Numeric NOT NULL ,
    eid_titulo Varchar (256),
    eid_uri Varchar (270) NOT NULL ,
primary key (eid_codidi,eid_enccod) 
);

Create table gus_encust (
    enc_codi Numeric NOT NULL ,
    enc_miccod Numeric NOT NULL ,
    enc_public timestamp,
    enc_caduca timestamp,
    enc_visib Varchar (1) Default 'S',
    enc_indiv Varchar (1) Default 'S',
    enc_pagina Numeric,
    enc_muestr Varchar (1) Default 'S',
    enc_identif Varchar (1),
primary key (enc_codi) 
);

Create table gus_encvot (
    vot_codi Numeric NOT NULL ,
    vot_idencu Numeric,
    vot_idpreg Numeric,
    vot_idresp Numeric NOT NULL ,
    vot_input Varchar (2000),
    vot_codusu Numeric(19,0),
primary key (vot_codi) 
);

Create table gus_faq (
    faq_codi Numeric NOT NULL ,
    faq_miccod Numeric NOT NULL ,
    faq_codtem Numeric,
    faq_fecha timestamp,
    faq_visib Varchar (1) Default 'S',
primary key (faq_codi) 
);

Create table gus_faqidi (
    fid_codidi Varchar (6) NOT NULL ,
    fid_faqcod Numeric NOT NULL ,
    fid_pregun Varchar (1024),
    fid_respu Varchar (4000),
    fid_url Varchar (1024),
    fid_urlnom Varchar (512),
primary key (fid_codidi,fid_faqcod) 
);

Create table gus_frmcon (
    frm_codi Numeric NOT NULL ,
    frm_miccod Numeric,
    frm_email Varchar (256),
    frm_visib Varchar (1) Default 'S',
    frm_anexarch Varchar (1),
primary key (frm_codi) 
);

Create table gus_frmidi (
    rid_codidi Varchar (6) NOT NULL ,
    rid_flicod Numeric NOT NULL ,
    rid_texto Varchar (4000),
primary key (rid_codidi,rid_flicod) 
);

Create table gus_frmlin (
    fli_codi Numeric NOT NULL ,
    fli_frmcod Numeric NOT NULL ,
    fli_visib Varchar (1) Default 'S',
    fli_tamano Numeric,
    fli_lineas Numeric,
    fli_orden Numeric,
    fli_obliga Numeric,
    fli_tipo Varchar (1) Default '2'
,
primary key (fli_codi) 
);

Create table gus_frqidi (
    fqi_codidi Varchar (6) NOT NULL ,
    fqi_frqcod Numeric NOT NULL ,
    fqi_nombre Varchar (100),
primary key (fqi_codidi,fqi_frqcod) 
);

Create table gus_frqssi (
    frq_codi Numeric NOT NULL ,
    frq_miccod Numeric,
    frq_centre Varchar (25),
    frq_tpescr Varchar (25),
primary key (frq_codi) 
);

Create table gus_idimic (
    imi_codidi Varchar (6) NOT NULL ,
    imi_miccod Numeric NOT NULL ,
primary key (imi_codidi,imi_miccod) 
);

Create table gus_idioma (
    idi_codi Varchar (2) NOT NULL ,
    idi_orden Numeric(10,0) NOT NULL ,
    idi_codest Varchar (128),
    idi_nombre Varchar (128),
    idi_traductor Varchar (128),
primary key (idi_codi) 
);

Create table gus_ldistribucion (
    codi Numeric(19,0) NOT NULL ,
    nombre Varchar (255),
    descripcion Varchar (255),
    microsite_id Numeric,
    publico Numeric(1,0),
primary key (codi) 
);

Create table gus_ldistribucion_correo (
    listadistrib_id Numeric(19,0) NOT NULL ,
    correo_id Varchar (255) NOT NULL ,
primary key (listadistrib_id,correo_id) 
);

Create table gus_menu (
    mnu_codi Numeric NOT NULL ,
    mnu_miccod Numeric NOT NULL ,
    mnu_orden Numeric Default 0,
    mnu_imgmen Numeric,
    mnu_padre Numeric Default 0                      NOT NULL ,
    mnu_visib Varchar (1) Default 'S',
    mnu_modo Varchar (1) Default 'F'
,
primary key (mnu_codi) 
);

Create table gus_micidi (
    mid_codidi Varchar (6) NOT NULL ,
    mid_miccod Numeric NOT NULL ,
    mid_txtop1 Varchar (64),
    mid_urlop1 Varchar (256),
    mid_txtop2 Varchar (64),
    mid_urlop2 Varchar (256),
    mid_txtop3 Varchar (64),
    mid_urlop3 Varchar (256),
    mid_txtop4 Varchar (64),
    mid_urlop4 Varchar (256),
    mid_txtop5 Varchar (64),
    mid_urlop5 Varchar (256),
    mid_txtop6 Varchar (64),
    mid_urlop6 Varchar (256),
    mid_txtop7 Varchar (64),
    mid_urlop7 Varchar (256),
    mid_titcam Varchar (256),
    mid_subtca Varchar (256),
    mid_cabper Varchar (4000),
    mid_pieper Varchar (4000),
    mid_titulo Varchar (256),
    mid_keywords Varchar (1000),
    mid_description Varchar (4000),
primary key (mid_codidi,mid_miccod) 
);

Create table gus_micros (
    mic_codi Numeric NOT NULL ,
    mic_coduni Numeric,
    mic_fecha timestamp Default now(),
    mic_visib Varchar (1) Default 'S'                    NOT NULL ,
    mic_imagen Numeric,
    mic_menu Varchar (1) Default 'N'                    NOT NULL ,
    mic_planti Varchar (1) Default '1',
    mic_opfaq Varchar (1) Default 'N',
    mic_opmapa Varchar (1) Default 'N',
    mic_opcont Varchar (1) Default 'N',
    mic_opt1 Varchar (1) Default 'N',
    mic_opt2 Varchar (1) Default 'N',
    mic_opt3 Varchar (1) Default 'N',
    mic_opt4 Varchar (1) Default 'N',
    mic_opt5 Varchar (1) Default 'N',
    mic_opt6 Varchar (1) Default 'N',
    mic_opt7 Varchar (1) Default 'N',
    mic_urlhom Varchar (512),
    mic_imgcam Numeric,
    mic_restri Varchar (1) Default 'N',
    mic_rol Varchar (150),
    mic_sersel Varchar (1024),
    mic_urlcam Varchar (512),
    mic_serofr Varchar (1024),
    mic_css Numeric,
    mic_cab Varchar (1),
    mic_pie Varchar (1),
    mic_numnot Numeric,
    mic_busca Varchar (1) Default 'N',
    mic_clave Varchar (150) Constraint uq_gus_micros_1 UNIQUE ,
    mic_mnucrp Varchar (1) Default 'N',
    mic_cssptr Varchar (1),
    mic_domini Varchar (150),
    mic_uri Varchar (32) NOT NULL Constraint uq_gus_micros_2 UNIQUE ,
    mic_analytics Varchar (60),
    mic_version Varchar (2),
    mic_tipo_acceso Varchar (1),
    mic_ftecod Numeric(19,0),
    mic_desarrollo Varchar (1) Default 'N' NOT NULL ,
primary key (mic_codi) 
);

Create table gus_micusu (
    miu_codusu Numeric(19,0) NOT NULL ,
    miu_codmic Numeric NOT NULL ,
primary key (miu_codusu,miu_codmic) 
);

Create table gus_mnuidi (
    mdi_mnucod Numeric NOT NULL ,
    mdi_codidi Varchar (6) NOT NULL ,
    mdi_nombre Varchar (256),
primary key (mdi_mnucod,mdi_codidi) 
);

Create table gus_musuar (
    msu_codi Numeric(19,0) NOT NULL ,
    msu_userna Varchar (128) NOT NULL  Constraint gus_mus_uni UNIQUE ,
    msu_passwo Varchar (128),
    msu_nombre Varchar (256),
    msu_observ Varchar (4000),
    msu_perfil Varchar (64),
    msu_permis Numeric(19,0),
primary key (msu_codi) 
);

Create table gus_notics (
    not_codi Numeric NOT NULL ,
    not_miccod Numeric NOT NULL ,
    not_imagen Numeric,
    not_caduca timestamp,
    not_public timestamp,
    not_visib Varchar (1) Default 'S',
    not_visweb Varchar (1) Default 'N',
    not_tipo Numeric NOT NULL ,
    not_orden Numeric,
    not_latitud Varchar (100),
    not_longitud Varchar (100),
    not_icocolor Varchar (20),
primary key (not_codi) 
);

Create table gus_notidi (
    nid_codidi Varchar (6) NOT NULL ,
    nid_notcod Numeric NOT NULL ,
    nid_titulo Varchar (512),
    nid_subtit Varchar (256),
    nid_fuente Varchar (100),
    nid_url Varchar (512),
    nid_docu Numeric,
    nid_texto Text,
    nid_urlnom Varchar (512),
    nid_uri Varchar (530) NOT NULL ,
primary key (nid_codidi,nid_notcod) 
);

Create table gus_pregun (
    pre_codi Numeric NOT NULL ,
    pre_enccod Numeric,
    pre_imagen Numeric,
    pre_multir Varchar (1) Default 'S',
    pre_viscmp Varchar (1) Default 'S',
    pre_obliga Varchar (1) Default 'S',
    pre_visib Varchar (1) Default 'S',
    pre_orden Numeric,
    pre_nresp Numeric,
    pre_mincont Numeric(10,0),
    pre_maxcont Numeric(10,0),
primary key (pre_codi) 
);

Create table gus_preidi (
    pid_codidi Varchar (6) NOT NULL ,
    pid_precod Numeric NOT NULL ,
    pid_titulo Varchar (256),
primary key (pid_codidi,pid_precod) 
);

Create table gus_residi (
    rei_codidi Varchar (6) NOT NULL ,
    rei_rescod Numeric NOT NULL ,
    rei_titulo Varchar (256),
primary key (rei_codidi,rei_rescod) 
);

Create table gus_respus (
    res_codi Numeric NOT NULL ,
    res_precod Numeric,
    res_entrad Varchar (256),
    res_orden Numeric,
    res_nresp Numeric,
    res_tipo Varchar (1),
primary key (res_codi) 
);

Create table gus_stats (
    sta_codi Numeric NOT NULL ,
    sta_item Numeric NOT NULL ,
    sta_mes Numeric NOT NULL ,
    sta_ref Varchar (5) NOT NULL ,
    sta_nacces Numeric NOT NULL ,
    sta_miccod Numeric NOT NULL ,
    sta_pub Numeric Default 1                      NOT NULL ,
primary key (sta_codi) 
);

Create table gus_temas (
    tem_codi Numeric NOT NULL ,
    tem_miccod Numeric NOT NULL ,
primary key (tem_codi) 
);

Create table gus_temidi (
    tid_codidi Varchar (6) NOT NULL ,
    tid_temcod Numeric NOT NULL ,
    tid_nombre Varchar (100),
primary key (tid_codidi,tid_temcod) 
);

Create table gus_tipser (
    tps_codi Numeric NOT NULL ,
    tps_nombre Varchar (64) NOT NULL ,
    tps_visib Varchar (1) Default 'S'                    NOT NULL ,
    tps_ref Varchar (5) NOT NULL ,
    tps_url Varchar (64),
    tps_tipo Varchar (1),
primary key (tps_codi) 
);

Create table gus_tpnidi (
    tpi_codidi Varchar (6) NOT NULL ,
    tpi_tipcod Numeric NOT NULL ,
    tpi_nombre Varchar (100),
    tpi_uri Varchar (125) NOT NULL ,
primary key (tpi_codidi,tpi_tipcod) 
);

Create table gus_tpnoti (
    tpn_codi Numeric NOT NULL ,
    tpn_miccod Numeric,
    tpn_tampag Numeric Default 0,
    tpn_tippag Varchar (1) Default '0',
    tpn_tpelem Varchar (1) Default '0',
    tpn_buscar Varchar (1) Default 'N',
    tpn_orden Varchar (1) Default '1',
    tpn_xurl Varchar (1000),
    tpn_xjndi Varchar (500),
    tpn_xlocal Varchar (1),
    tpn_xauth Varchar (1),
    tpn_xusr Varchar (50),
    tpn_xpwd Varchar (50),
    tpn_xid Varchar (50),
    tpn_clasif Varchar (100),
    tpn_fotosporfila Numeric Default 3,
    tpn_pplcod Numeric(19,0),
primary key (tpn_codi) 
);

Create table gus_usuarienc (
    use_codi Numeric(19,0) NOT NULL ,
    use_nombre Varchar (256) Default 'anonim'  NOT NULL ,
    use_observ Varchar (4000),
    use_dni Varchar (10),
primary key (use_codi) 
);

Create table gus_usuresp (
    usr_codusu Numeric(19,0) NOT NULL ,
    usr_codresp Numeric NOT NULL ,
primary key (usr_codusu,usr_codresp) 
);

Create table gus_w3c (
    w3c_servic Varchar (25),
    w3c_result Varchar (1),
    w3c_mensa Varchar (2000),
    w3c_mesura Varchar (1),
    w3c_codi Numeric NOT NULL ,
    w3c_iditem Numeric,
    w3c_idioma Varchar (2),
    w3c_miccod Numeric,
    w3c_tawres Numeric,
    w3c_tawmen Varchar (2000)
);

Create table gus_auditoria (
    aud_codi Numeric(19,0) NOT NULL ,
    aud_miccod Numeric,
    aud_usuari Varchar (256),
    aud_fecha timestamp,
    aud_operacion Numeric(1,0),
    aud_entidad Varchar (50) NOT NULL ,
    aud_informacion Text,
    aud_id_entidad Varchar (255),
primary key (aud_codi) 
);

Create table gus_fr_tema (
    fte_codi Numeric(19,0) NOT NULL ,
    fte_version Varchar (2) NOT NULL ,
    fte_tema_padre Numeric(19,0) ,
    fte_nombre Varchar (255) NOT NULL  UNIQUE ,
    fte_css Numeric,
    fte_actualizacion timestamp Default now() NOT NULL ,
    fte_uri Varchar (32) NOT NULL  UNIQUE ,
 Constraint gus_fte_pk primary key (fte_codi) 
);

Create table gus_fr_plantilla (
    pla_codi Numeric(19,0) NOT NULL ,
    pla_version Varchar (2) NOT NULL ,
    pla_nombre Varchar (255) NOT NULL  UNIQUE ,
    pla_descripcion Text,
    pla_titulo Varchar(255) NOT NULL,
 Constraint gus_pla_pk primary key (pla_codi) 
);

Create table gus_fr_version (
    fve_version Varchar (2) NOT NULL ,
    fve_nombre Varchar (255) NOT NULL  UNIQUE ,
 Constraint gus_fve_pk primary key (fve_version) 
);

Create table gus_fr_perpla (
    ppl_codi Numeric(19,0) NOT NULL ,
    ppl_titulo Varchar (255) NOT NULL ,
    ppl_placod Numeric(19,0) NOT NULL ,
    ppl_orden Integer,
    ppl_ftecod Numeric(19,0),
    ppl_miccod Numeric,
    ppl_contenido Text,
 Constraint gus_plt_pk primary key (ppl_codi) 
);

Create table gus_fr_archivo (
    arc_codi Numeric NOT NULL ,
    arc_ftecod Numeric(19,0) NOT NULL ,
    arc_path Varchar (256),
    arc_doccod Numeric,
 Constraint gus_arc_pk primary key (arc_codi) 
);

  
--INTRODUCIR SOLRPD.
CREATE TABLE gus_solrpd
  (
    slp_id     Numeric NOT NULL,
    slp_tipo   Varchar(128),
    slp_idelem Numeric,
    slp_accion Numeric,
    slp_feccre timestamp,
    slp_fecidx timestamp,
    slp_result    Numeric,
    slp_txterr    Varchar(300),
    slp_idarchivo Numeric,
    CONSTRAINT gus_solrpd_pk PRIMARY KEY (slp_id)
  );


--INTRODUCIR SOLR JOB.
CREATE TABLE gus_soljob
  (
    job_id Numeric(19,0) NOT NULL,
    job_fecini timestamp DEFAULT now(),
    job_fecfin timestamp,
    job_tipo   Varchar(20),
    job_descri Text,
    job_idelem Numeric(19,0),
    CONSTRAINT rsc_job_pk PRIMARY KEY (job_id)
  );

CREATE TABLE gus_fcoidi (
    fci_codidi Varchar (6) NOT NULL ,
    fci_fcocod Numeric NOT NULL ,
    fci_nombre Varchar (100),
    fci_uri Varchar (125) NOT NULL ,
primary key (fci_codidi,fci_fcocod) 
);
  
  
-- Create Indexes section
Create Index gus_ciduri_i    ON gus_conidi (cid_codidi,cid_uri) ;
Create Index gus_dcmmcd_fk_i ON gus_docus  (dcm_miccod) ;
Create Index gus_dcmnom_i    ON gus_docus  (dcm_nombre) ;
Create Index gus_eiduri_i    ON gus_encidi (eid_codidi,eid_uri) ;
Create Index gus_niduri_i    ON gus_notidi (nid_codidi,nid_uri) ;
Create Index gus_staitm_i    ON gus_stats  (sta_item) ;
Create Index gus_stamcd_fk_i ON gus_stats  (sta_miccod) ;
Create Index gus_stames_i    ON gus_stats  (sta_mes) ;
Create Index gus_staref_i    ON gus_stats  (sta_ref) ;
Create Index gus_tpnuri_i    ON gus_tpnidi (tpi_codidi,tpi_uri) ;
Create Index gus_audmic_i    ON gus_auditoria (aud_miccod) ;
Create UNIQUE Index gus_arc_path_i ON gus_fr_archivo (arc_ftecod,arc_path) ;
Create Index gus_fcoidi_i    ON gus_fcoidi (fci_codidi,fci_uri) ;


-- Create Foreign keys section
Alter table gus_actidi               add  foreign key (ati_actcod)      references gus_activi (act_codi) ;
Alter table gus_agenda               add  foreign key (age_activi)      references gus_activi (act_codi) ;
Alter table gus_ageidi               add  foreign key (aid_agecod)      references gus_agenda (age_codi) ;
Alter table gus_cmpidi               add  foreign key (cpi_cmpcod)      references gus_compos (cmp_codi) ;
Alter table gus_conidi               add  foreign key (cid_concod)      references gus_conten (con_codi) ;
Alter table gus_ldistribucion_correo add  foreign key (correo_id)       references gus_correo (correo) ;
Alter table gus_ageidi               add  foreign key (aid_docu)        references gus_docus (dcm_codi) ;
Alter table gus_ageidi               add  foreign key (aid_imagen)      references gus_docus (dcm_codi) ;
Alter table gus_compos               add  foreign key (cmp_imgbul)      references gus_docus (dcm_codi) ;
Alter table gus_conten               add  foreign key (con_imgmen)      references gus_docus (dcm_codi) ;
Alter table gus_menu                 add  foreign key (mnu_imgmen)      references gus_docus (dcm_codi) ;
Alter table gus_micros               add  foreign key (mic_imagen)      references gus_docus (dcm_codi) ;
Alter table gus_micros               add  foreign key (mic_imgcam)      references gus_docus (dcm_codi) ;
Alter table gus_micros               add  foreign key (mic_css)         references gus_docus (dcm_codi) ;
Alter table gus_notics               add  foreign key (not_imagen)      references gus_docus (dcm_codi) ;
Alter table gus_notidi               add  foreign key (nid_docu)        references gus_docus (dcm_codi) ;
Alter table gus_pregun               add  foreign key (pre_imagen)      references gus_docus (dcm_codi) ;
Alter table gus_convocatoria         add  foreign key (encuesta_id)     references gus_encust (enc_codi) ;
Alter table gus_encidi               add  foreign key (eid_enccod)      references gus_encust (enc_codi) ;
Alter table gus_pregun               add  foreign key (pre_enccod)      references gus_encust (enc_codi) ;
Alter table gus_faqidi               add  foreign key (fid_faqcod)      references gus_faq (faq_codi) ;
Alter table gus_frmlin               add  foreign key (fli_frmcod)      references gus_frmcon (frm_codi) ;
Alter table gus_frmidi               add  foreign key (rid_flicod)      references gus_frmlin (fli_codi) ;
Alter table gus_frqidi               add  foreign key (fqi_frqcod)      references gus_frqssi (frq_codi) ;
Alter table gus_ldistribucion_correo add  foreign key (listadistrib_id) references gus_ldistribucion (codi) ;
Alter table gus_conten               add  foreign key (con_mnucod)      references gus_menu (mnu_codi) ;
Alter table gus_mnuidi               add  foreign key (mdi_mnucod)      references gus_menu (mnu_codi) ;
Alter table gus_activi               add  foreign key (act_miccod)      references gus_micros (mic_codi) ;
Alter table gus_agenda               add  foreign key (age_miccod)      references gus_micros (mic_codi) ;
Alter table gus_compos               add  foreign key (cmp_miccod)      references gus_micros (mic_codi) ;
Alter table gus_convocatoria         add  foreign key (microsite_id)    references gus_micros (mic_codi) ;
Alter table gus_encust               add  foreign key (enc_miccod)      references gus_micros (mic_codi) ;
Alter table gus_faq                  add  foreign key (faq_miccod)      references gus_micros (mic_codi) ;
Alter table gus_frmcon               add  foreign key (frm_miccod)      references gus_micros (mic_codi) ;
Alter table gus_frqssi               add  foreign key (frq_miccod)      references gus_micros (mic_codi) ;
Alter table gus_idimic               add  foreign key (imi_miccod)      references gus_micros (mic_codi) ;
Alter table gus_ldistribucion        add  foreign key (microsite_id)    references gus_micros (mic_codi) ;
Alter table gus_menu                 add  foreign key (mnu_miccod)      references gus_micros (mic_codi) ;
Alter table gus_micidi               add  foreign key (mid_miccod)      references gus_micros (mic_codi) ;
Alter table gus_micusu               add  foreign key (miu_codmic)      references gus_micros (mic_codi) ;
Alter table gus_notics               add  foreign key (not_miccod)      references gus_micros (mic_codi) ;
Alter table gus_temas                add  foreign key (tem_miccod)      references gus_micros (mic_codi) ;
Alter table gus_tpnoti               add  foreign key (tpn_miccod)      references gus_micros (mic_codi) ;
Alter table gus_auditoria            add  foreign key (aud_miccod)      references gus_micros (mic_codi)  on delete set null;
Alter table gus_micusu               add  foreign key (miu_codusu)      references gus_musuar (msu_codi) ;
Alter table gus_notidi               add  foreign key (nid_notcod)      references gus_notics (not_codi) ;
Alter table gus_convocatoria         add  foreign key (pregunta_confirmacion_id) references gus_pregun (pre_codi) ;
Alter table gus_convocatoria         add  foreign key (pregunta_correo_id) references gus_pregun (pre_codi) ;
Alter table gus_preidi               add  foreign key (pid_precod)          references gus_pregun (pre_codi) ;
Alter table gus_respus               add  foreign key (res_precod)          references gus_pregun (pre_codi) ;
Alter table gus_convocatoria         add  foreign key (respuesta_confirmacion_id) references gus_respus (res_codi) ;
Alter table gus_encvot               add  foreign key (vot_idresp)          references gus_respus (res_codi) ;
Alter table gus_residi               add  foreign key (rei_rescod)          references gus_respus (res_codi) ;
Alter table gus_usuresp              add  foreign key (usr_codresp)         references gus_respus (res_codi) ;
Alter table gus_convocatoria         add  foreign key (respuesta_correo_id) references gus_respus (res_codi) ;
Alter table gus_faq                  add  foreign key (faq_codtem)          references gus_temas (tem_codi) ;
Alter table gus_temidi               add  foreign key (tid_temcod)          references gus_temas (tem_codi) ;
Alter table gus_compos               add  foreign key (cmp_tipo)            references gus_tpnoti (tpn_codi) ;
Alter table gus_notics               add  foreign key (not_tipo)            references gus_tpnoti (tpn_codi) ;
Alter table gus_tpnidi               add  foreign key (tpi_tipcod)          references gus_tpnoti (tpn_codi) ;
Alter table gus_usuresp              add  foreign key (usr_codusu)          references gus_usuarienc (use_codi) ;
Alter table gus_fcoidi               add  foreign key (fci_fcocod)          references gus_frmcon (frm_codi) ;


Create Index gus_fte_doc_i ON gus_fr_tema (fte_css);
Alter table gus_fr_tema add Constraint gus_fte_doc_fk foreign key (fte_css) references gus_docus (dcm_codi) ;
Create Index gus_arc_dcm_i ON gus_fr_archivo (arc_doccod);
Alter table gus_fr_archivo add Constraint gus_arc_dcm_fk foreign key (arc_doccod) references gus_docus (dcm_codi) ;
Create Index gus_mic_plt_i ON gus_fr_perpla (ppl_miccod);
Alter table gus_fr_perpla add Constraint gus_mic_plt_fk foreign key (ppl_miccod) references gus_micros (mic_codi) ;
Create Index gus_fte_fte_i ON gus_fr_tema (fte_tema_padre);
Alter table gus_fr_tema add Constraint gus_fte_fte_fk foreign key (fte_tema_padre) references gus_fr_tema (fte_codi) ;
Create Index gus_fte_plt_i ON gus_fr_perpla (ppl_ftecod);
Alter table gus_fr_perpla add Constraint gus_fte_plt_fk foreign key (ppl_ftecod) references gus_fr_tema (fte_codi) ;
Create Index gus_fte_arc_i ON gus_fr_archivo (arc_ftecod);
Alter table gus_fr_archivo add Constraint gus_fte_arc_fk foreign key (arc_ftecod) references gus_fr_tema (fte_codi) ;
Create Index gus_fte_mic_i ON gus_micros (mic_ftecod);
Alter table gus_micros add Constraint gus_fte_mic_fk foreign key (mic_ftecod) references gus_fr_tema (fte_codi) ;
Create Index gus_pla_plt_i ON gus_fr_perpla (ppl_placod);
Alter table gus_fr_perpla add Constraint gus_pla_plt_fk foreign key (ppl_placod) references gus_fr_plantilla (pla_codi); 
Create Index gus_fve_far_i ON gus_fr_plantilla (pla_version);
Alter table gus_fr_plantilla add Constraint gus_fve_far_fk foreign key (pla_version) references gus_fr_version (fve_version) ;
Create Index gus_fve_fte_i ON gus_fr_tema (fte_version);
Alter table gus_fr_tema add Constraint gus_fve_fte_fk foreign key (fte_version) references gus_fr_version (fve_version) ;
Create Index gus_ppl_con_i ON gus_conten (con_pplcod);
Alter table gus_conten add Constraint gus_ppl_con_fk foreign key (con_pplcod) references gus_fr_perpla (ppl_codi) ;
Create Index gus_ppl_tpn_i ON gus_tpnoti (tpn_pplcod);
Alter table gus_tpnoti add Constraint gus_ppl_tpn_fk foreign key (tpn_pplcod) references gus_fr_perpla (ppl_codi) ;
Create Index gus_ppl_cmp_i ON gus_compos (cmp_pplcod);
Alter table gus_compos add Constraint gus_ppl_cmp_fk foreign key (cmp_pplcod) references gus_fr_perpla (ppl_codi) ;

-- Indices para mejorar el rendimiento #41
CREATE INDEX gus_micros_imagen_i ON gus_micros (mic_imagen);
CREATE INDEX gus_micros_imgcam_i ON gus_micros (mic_imgcam);
CREATE INDEX gus_micros_css_i    ON gus_micros (mic_css);
CREATE INDEX gus_notics_imagen_i ON gus_notics (not_imagen);
CREATE INDEX gus_pregun_imagen_i ON gus_pregun (pre_imagen);
CREATE INDEX gus_compos_imgbul_i ON gus_compos (cmp_imgbul);
CREATE INDEX gus_ageidi_docu_i   ON gus_ageidi (aid_docu);
CREATE INDEX gus_ageidi_imagen_i ON gus_ageidi (aid_imagen);
CREATE INDEX gus_menu_imgmen_i   ON gus_menu   (mnu_imgmen);

-- INDICES SOBRE LAS ENCUESTAS PARA SOLVENTAR PROBLEMAS DE RENDIMIENTO
CREATE INDEX gus_encust_enc_miccod_fk_i ON gus_encust (enc_miccod);
CREATE INDEX gus_pregun_pre_enccod_fk_i ON gus_pregun (pre_enccod);
CREATE INDEX gus_respus_res_precod_fk_i ON gus_respus (res_precod);
CREATE INDEX gus_encvot_vot_idresp_fk_i ON gus_encvot (vot_idresp);
CREATE INDEX gus_encvot_vot_idencu_i ON gus_encvot (vot_idencu);
CREATE INDEX gus_encvot_vot_idpreg_i ON gus_encvot (vot_idpreg);
CREATE INDEX gus_encvot_vot_codusu_i ON gus_encvot (vot_codusu);  


-- Create Functions section

--Convierte una cadena a un formato utilizable en url.
--Por ejemplo, convertiría:
--    "Perfil del t(u)rista británic 2002''"
--     a:
--     Perfil_del_turista_britanic_2002

-- Para que no de error la llamada select gus_sting2uri_f()
CREATE OR REPLACE FUNCTION GUS_STRING2URI_F () RETURNS varchar AS $$
BEGIN
    RETURN '';
END;
$$ LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION GUS_STRING2URI_F (nombre varchar) RETURNS varchar AS $$
DECLARE
    origin varchar := 'ÁÉÍÓÚÀÈÌÒÙÄËÏÖÜÂÊÎÔÛÃÕÑÝÇáéíóúàèìòùäëïöüâêîôûãõñýç ';
    dest varchar := 'AEIOUAEIOUAEIOUAEIOUAONYCaeiouaeiouaeiouaeiouaonyc_';
    validAbc varchar := '[^ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789_-]';
BEGIN
    IF nombre is NULL THEN
        RETURN '';
    END IF;
    RETURN regexp_replace(lower(translate(nombre, origin, dest)), validAbc, '', 'g');
END;
$$ LANGUAGE plpgsql;


-- Nextval portable
CREATE OR REPLACE FUNCTION GUS_NEXTVAL ( secuencia Varchar ) RETURNS Integer AS $$
BEGIN
    RETURN nextval(secuencia);
END;
$$ LANGUAGE plpgsql;


Comment on table gus_actidi is 'Tabla que contiene los atributos dependientes de idioma de los tipos de registros que incluiye una agenda de un microsite.';
Comment on table gus_activi is 'Tabla que contiene los tipos de registros que incluiye una agenda de un microsite.';
Comment on table gus_ageidi is 'Tabla que contiene los atributos dependientes de idioma de los registros que incluiye una agenda de un microsite.';
Comment on table gus_agenda is 'Tabla que contiene los registros de la agenda de un microsite.';
Comment on table gus_cmpidi is 'Tabla que contiene los atributos dependientes de idioma del registro de componentes de un microsite.';
Comment on table gus_compos is 'Tabla que contiene el registro de componentes sobre los listados definidos para un microsite. Son distintas presentaciones para un informe, que pueden incluirse en un contenido.';
Comment on table gus_conidi is 'Tabla que contiene los atributos dependientes de idioma de los contenidos de un microsite.';
Comment on table gus_conten is 'Tabla que contiene los contenidos de un microsite. Son los contenidos estáticos o los enlaces externos asociados al menú.';
Comment on table gus_docus  is 'Tabla que contiene los archivos (imágenes, pdf, doc, etc..) utilizados en los distintos elementos de un microsite.';
Comment on table gus_encidi is 'Tabla que contiene los atributos dependientes de idioma para las encuestas de un microsite.';
Comment on table gus_encust is 'Tabla que contiene el registro de encuestas definidas para un microsite. Se compone de una relación de preguntas y respuestas.';
Comment on table gus_encvot is 'Tabla que contiene los resultados de las contestaciones sobre las preguntas asociadas a una encuesta publicadas para un microsite.';
Comment on table gus_faq    is 'Tabla que contiene el registro de preguntas frecuentes creadas para un microsite.';
Comment on table gus_faqidi is 'Tabla que contiene los atributos dependientes de idioma para las preguntas frecuentes de un microsite.';
Comment on table gus_frmcon is 'Tabla que contiene el registro de los formularios de contacto definidos para un microsite. Se compone de un número de campos que incluirá el formulario.';
Comment on table gus_frmidi is 'Tabla que contiene los atributos dependientes de idioma del registro de campos de un formulario de un microsite.';
Comment on table gus_frmlin is 'Tabla que contiene la relación de campos y su tipo asociados a un formularios de contacto definidos para un microsite.';
Comment on table gus_idioma is 'Tabla que contiene la lista de idiomas gestionados por los microsites. La activación de un nuevo idioma no solo supone el registro de esta tabla.';
Comment on table gus_menu   is 'Tabla que contiene la definición del arbol de menú asociado a un microsite. Define los nodos de menú, su tipo y funcionamiento y enlaza con los contenidos del microsite.';
Comment on table gus_micidi is 'Tabla que contiene los atributos dependientes de idioma para la definición de un elementos de un tipo de listado de un microsite.';
Comment on table gus_micros is 'Tabla que contiene la definición del microsite con todos los atributos de caracter general que determinan su funcionamiento.';
Comment on table gus_micusu is 'Tabla que relaciona un usuario con todos los microsites en los que tiene permiso, y el perfil con el que actual sobre él.';
Comment on table gus_mnuidi is 'Tabla que contiene los atributos dependientes de idioma para el menú de un microsite.';
Comment on table gus_musuar is 'Tabla que contiene la ficha de los usuarios del entorno de los microsite. El alta se realiza coincidir con un usuario de CAIB. Se utiliza para controlar que un usuario CAIB acceda un microsite con un perfil determinado.';
Comment on table gus_notics is 'Tabla que contiene el registro de elemetos a publicar en la lista para un tipo de listado definidos en un microsite.';
Comment on table gus_pregun is 'Tabla que contiene la relación de preguntas asociadas a una encuesta definidas para un microsite.';
Comment on table gus_preidi is 'Tabla que contiene los atributos dependientes de idioma para las preguntas asociadas a una encuesta de un microsite.';
Comment on table gus_residi is 'Tabla que contiene los atributos dependientes de idioma para las respuestas tabuladas de las preguntas asociadas a una encuesta de un microsite.';
Comment on table gus_respus is 'Tabla que contiene la relación de respuestas tabuladas a una pregunta asociadas a una encuesta definidas para un microsite.';
Comment on table gus_stats  is 'Tabla que contiene el registro de las estadísticas de acceso a los distintos elementos de un microsite.';
Comment on table gus_temas  is 'Tabla que contiene el registro de agrupadores definidos para las preguntas frecuentes que se creen para un microsite.';
Comment on table gus_temidi is 'Tabla que contiene los atributos dependientes de idioma para la definición de los agrupadores de preguntas frecuentes de un microsite.';
Comment on table gus_tipser is 'Tabla que contiene el registro de los servicios de un microsite. La creación de un nuevo servicio no solo supone el registro de esta tabla.';
Comment on table gus_tpnidi is 'Tabla que contiene los atributos dependientes de idioma para la definición de un tipo de listado de un microsite.';
Comment on table gus_tpnoti is 'Tabla que contiene el registro de los distintos tipos de listados definidos para un microsite y que se utilizarán para la Publicación de listas de elementos de los distintos tipo posible.';
Comment on table gus_usuarienc is 'Tabla que contiene la ficha de los usuarios de los usuarios que rellenan una encuesta.';
Comment on table gus_usuresp   is 'Tabla que relaciona un usuario que rellena una encuesta con las respuestas.';
Comment on table gus_fr_tema   is 'Tema del front';
Comment on table gus_fr_plantilla is 'Tabla que identifica las plantillas que se pueden implementar (sobreescribir) para una versión determinada de front';
Comment on table gus_fr_version   is 'Versión de front / identifica las versiones para su implementación en temas';
Comment on table gus_fr_perpla    is 'Personalización de una plantilla';
Comment on table gus_fr_archivo   is 'Tabla que contiene los archivos (imágenes, js, etc.) utilizados en los distintos elementos de un tema';
Comment on table gus_fcoidi is 'Tabla que contiene los atributos dependientes de idioma para la definición de un formulario de contacto de un microsite.';

-- Create Attribute comments section

Comment on column gus_actidi.ati_codidi is 'Identificador del idioma ca,es,de,en';
Comment on column gus_actidi.ati_actcod is 'Identificador de la actividad';
Comment on column gus_actidi.ati_nombre is 'Nombre de la actividad';
Comment on column gus_activi.act_codi   is 'Identificador de la Actividad';
Comment on column gus_activi.act_miccod is 'Identificador del microsite';
Comment on column gus_ageidi.aid_codidi is 'Identificador del idioma ca,es,de,en';
Comment on column gus_ageidi.aid_agecod is 'Código Agenda';
Comment on column gus_ageidi.aid_descri is 'Descripción evento de la Agenda';
Comment on column gus_ageidi.aid_docu   is 'Código documento asociado';
Comment on column gus_ageidi.aid_titulo is 'Título del registro';
Comment on column gus_ageidi.aid_url    is 'URL de información asociada al registro';
Comment on column gus_ageidi.aid_imagen is 'Código imagen';
Comment on column gus_ageidi.aid_urlnom is 'Descripción donde se implementará el enlace de la URL';
Comment on column gus_agenda.age_codi   is 'Identificador Registro de Agenda';
Comment on column gus_agenda.age_miccod is 'Código de Microsite';
Comment on column gus_agenda.age_activi is 'Identificador de la actividad';
Comment on column gus_agenda.age_organi is 'Organisnmo del Evento';
Comment on column gus_agenda.age_inicio is 'Fecha Inicio del Evento';
Comment on column gus_agenda.age_fin    is 'Fecha de fin del Evento';
Comment on column gus_agenda.age_visib  is 'Visibilidad del evento';
Comment on column gus_cmpidi.cpi_codidi is 'Identificador del idioma ca,es,de,en';
Comment on column gus_cmpidi.cpi_cmpcod is 'Identificador del componente';
Comment on column gus_cmpidi.cpi_titulo is 'Nombre del componente';
Comment on column gus_compos.cmp_codi   is 'Identificador del componente';
Comment on column gus_compos.cmp_tipo   is 'Tipo de componente';
Comment on column gus_compos.cmp_miccod is 'Identificador de microsite';
Comment on column gus_compos.cmp_nombre is 'titulo del componente';
Comment on column gus_compos.cmp_soloim is 'Indica si es de tipo imagen';
Comment on column gus_compos.cmp_numele is 'Número de elementos';
Comment on column gus_compos.cmp_orden  is 'Orden de aparición';
Comment on column gus_compos.cmp_filas  is 'Número de filas';
Comment on column gus_conidi.cid_codidi is 'Identificador del idioma ca,es,de,en';
Comment on column gus_conidi.cid_concod is 'Identificador de contenido';
Comment on column gus_conidi.cid_titulo is 'Título del contenido';
Comment on column gus_conidi.cid_texto  is 'Para contenido HTML. Código HTML asociado principal al contenido que se mostrará al acceder a él';
Comment on column gus_conidi.cid_url    is 'Para contenido externo. URL asociado al contenido al que se redireccionará al acceder a él';
Comment on column gus_conidi.cid_txbeta is 'Para contenido HTML. Código HTML asociado al contenido en versión Beta hasta su validación y paso a CID_TEXTO';
Comment on column gus_conidi.cid_uri    is 'Uri para formar las urls';
Comment on column gus_conten.con_codi   is 'Identificador de contenido';
Comment on column gus_conten.con_caduca is 'Fecha de caducidad';
Comment on column gus_conten.con_public is 'Fecha de Publicación';
Comment on column gus_conten.con_visib  is 'Indica si es visible';
Comment on column gus_conten.con_orden  is 'Orden aparición';
Comment on column gus_conten.con_mnucod is 'Identificador menú';
Comment on column gus_conten.con_imgmen is 'Indentificador imagen';
Comment on column gus_docus.dcm_codi    is 'Identificador de documento';
Comment on column gus_docus.dcm_miccod  is 'Identificador del microsite';
Comment on column gus_docus.dcm_datos   is 'Identificador archivo';
Comment on column gus_docus.dcm_nombre  is 'Nombre del documento físico';
Comment on column gus_docus.dcm_tipo    is 'Identificacor de ficha';
Comment on column gus_docus.dcm_tamano  is 'identificador de procedimiento';
Comment on column gus_docus.dcm_pagina  is 'Orden aparición';
Comment on column gus_encidi.eid_codidi is 'Identificador del idioma ca,es,de,en';
Comment on column gus_encidi.eid_enccod is 'Identificador de contenido';
Comment on column gus_encidi.eid_titulo is 'Título de la Encuesta';
Comment on column gus_encidi.eid_uri    is 'Uri para formar las urls';
Comment on column gus_encust.enc_codi   is 'Identidicador Encuesta';
Comment on column gus_encust.enc_miccod is 'Identificador de Microsite';
Comment on column gus_encust.enc_public is 'Fecha de Publicación';
Comment on column gus_encust.enc_caduca is 'Fecha de Caducidad';
Comment on column gus_encust.enc_visib  is 'Indica si es visible';
Comment on column gus_encust.enc_pagina is 'Número de página';
Comment on column gus_encust.enc_muestr is 'Indica si se muestran los resultados de las encuestas al publico';
Comment on column gus_encvot.vot_codi   is 'Identificador Dato Respuesta';
Comment on column gus_encvot.vot_idencu is 'Identificador Encuesta';
Comment on column gus_encvot.vot_idpreg is 'Identificador Pregunta';
Comment on column gus_encvot.vot_idresp is 'Identificador de Respuesta';
Comment on column gus_encvot.vot_input  is 'Respuesta a la encuesta';
Comment on column gus_faq.faq_codi      is 'Identificador de Faq';
Comment on column gus_faq.faq_miccod    is 'Identificador de Microsite';
Comment on column gus_faq.faq_codtem    is 'Identificador del Tema';
Comment on column gus_faq.faq_fecha     is 'Fecha faq';
Comment on column gus_faq.faq_visib     is 'Indica si es o no visible';
Comment on column gus_faqidi.fid_codidi is 'Identificador del idioma ca,es,de,en';
Comment on column gus_faqidi.fid_faqcod is 'Identificador de contenido';
Comment on column gus_faqidi.fid_pregun is 'Texto de la Pregunta frecuente';
Comment on column gus_faqidi.fid_respu  is 'Texto de la Respuesta frecuente';
Comment on column gus_faqidi.fid_url    is 'URL asociado a la pregunta con información de interés';
Comment on column gus_faqidi.fid_urlnom is 'Texto del link para la URL asociado a la pregunta con información de interés';
Comment on column gus_frmcon.frm_codi   is 'Identidicador Formulario de Contacto';
Comment on column gus_frmcon.frm_miccod is 'Identificador de microsite';
Comment on column gus_frmcon.frm_email  is 'Direccion de correo';
Comment on column gus_frmcon.frm_visib  is 'Indica si es visibile';
Comment on column gus_frmidi.rid_codidi is 'Identificador del idioma ca,es,de,en';
Comment on column gus_frmidi.rid_flicod is 'Identificador de contenido';
Comment on column gus_frmidi.rid_texto  is 'Nomber del campo del formulario';
Comment on column gus_frmlin.fli_codi   is 'Identificador de la linea';
Comment on column gus_frmlin.fli_visib  is 'Indica si es visible';
Comment on column gus_frmlin.fli_tamano is 'Indica el tamaño';
Comment on column gus_frmlin.fli_lineas is 'Número de líneas';
Comment on column gus_frmlin.fli_orden  is 'Orden aparición';
Comment on column gus_frmlin.fli_obliga is 'Indica Obligacion de rellenar este campo en el formulario';
Comment on column gus_idioma.idi_codi   is 'Identificador Idioma';
Comment on column gus_idioma.idi_orden  is 'Ordenación';
Comment on column gus_idioma.idi_codest is 'Identificador para estadísticas';
Comment on column gus_idioma.idi_nombre is 'Nombre del Idioma';
Comment on column gus_menu.mnu_codi     is 'Identificador del menú';
Comment on column gus_menu.mnu_miccod   is 'Identificador de microsite';
Comment on column gus_menu.mnu_orden    is 'Orden aparición menú';
Comment on column gus_menu.mnu_imgmen   is 'Imagen menú';
Comment on column gus_menu.mnu_padre    is 'Nodo padre.  0=raiz o sin padre';
Comment on column gus_menu.mnu_visib    is 'Visiblidad del menu';
Comment on column gus_menu.mnu_modo     is 'F - Fijo, C - Carpeta';
Comment on column gus_micidi.mid_codidi is 'Identificador del idioma ca,es,de,en';
Comment on column gus_micidi.mid_miccod is 'Identificador del microsite';
Comment on column gus_micidi.mid_titulo is 'Nomber del microsite';
Comment on column gus_micidi.mid_keywords is 'información semántica: keywords';
Comment on column gus_micidi.mid_description is 'información semántica: description';
Comment on column gus_micros.mic_codi   is 'Identificador del microsite';
Comment on column gus_micros.mic_coduni is 'Codigo Unidad orgánica';
Comment on column gus_micros.mic_fecha  is 'Fecha mircrisite';
Comment on column gus_micros.mic_visib  is 'Indica si es visible.';
Comment on column gus_micros.mic_restri is 'N=internet, S=intranet';
Comment on column gus_micros.mic_sersel is 'Ultimos contenidos modificados. Listado de ids de contenidos separados por ;';
Comment on column gus_micros.mic_busca  is 'Incluir buscador';
Comment on column gus_micros.mic_clave  is 'Identificador de clave unica';
Comment on column gus_micros.mic_mnucrp is 'Menu corporativo';
Comment on column gus_micros.mic_cssptr is 'CSS genérico base utilizado por el microsite si no hay css personalizado. (A)zul, (R)ojo, (V)erde, (N)egro, (G)amarillo y (M)agenta';
Comment on column gus_micros.mic_uri    is 'URI única del microsite, usada para formar la url completa';
Comment on column gus_micros.mic_analytics is 'Clave de google analytics';
Comment on column gus_micros.mic_ftecod is 'Tema a aplicar';
Comment on column gus_micros.mic_desarrollo is 'Pone el sitio web en modo desarrollo, deshabilitando caché de plantillas.';
Comment on column gus_micusu.miu_codusu is 'Usuario del Microsite';
Comment on column gus_micusu.miu_codmic is 'Identificador Microsite';
Comment on column gus_mnuidi.mdi_mnucod is 'Identificador de contenido';
Comment on column gus_mnuidi.mdi_codidi is 'Identificador del idioma ca,es,de,en';
Comment on column gus_mnuidi.mdi_nombre is 'Nomber del nodo del menú';
Comment on column gus_musuar.msu_codi   is 'Identificador usuario de microsites';
Comment on column gus_musuar.msu_userna is 'Usuario de acceso al back de microsites. Debe coincidir con un usuario CAIB';
Comment on column gus_musuar.msu_passwo is 'No utilizado en la gestión actual: Para usuarios externos a CAIB: Password del usuario';
Comment on column gus_musuar.msu_nombre is 'Nombre completo del usuario';
Comment on column gus_musuar.msu_perfil is 'Perfil del usuriario. sacadmin-Administrador, sacsuper-Supervisor, sacoper-Operador';
Comment on column gus_musuar.msu_permis is 'Permisos para Tiny';
Comment on column gus_notics.not_codi   is 'Identificador de noticia';
Comment on column gus_notics.not_miccod is 'Identificador de microsite';
Comment on column gus_notics.not_imagen is 'Imagen noticia';
Comment on column gus_notics.not_caduca is 'Fecha caducidad';
Comment on column gus_notics.not_public is 'Fecha de Publicación';
Comment on column gus_notics.not_visib  is 'Indica si es visible';
Comment on column gus_notics.not_visweb is 'Indica si es visible en web';
Comment on column gus_notics.not_tipo   is 'Tipo de noticia';
Comment on column gus_notics.not_orden  is 'Orden de aparicion';
Comment on column gus_notidi.nid_codidi is 'Identificador del idioma ca,es,de,en';
Comment on column gus_notidi.nid_notcod is 'Identificador del documento';
Comment on column gus_notidi.nid_titulo is 'Titulo del documento';
Comment on column gus_notidi.nid_subtit is 'SubTitulo del documento';
Comment on column gus_notidi.nid_fuente is 'Nombre de la fuente del origen del documento';
Comment on column gus_notidi.nid_url    is 'URL del enlace a información externa';
Comment on column gus_notidi.nid_texto  is 'Texto Descripción del documento';
Comment on column gus_notidi.nid_urlnom is 'Nombre del enlace a información externa';
Comment on column gus_notidi.nid_uri    is 'Uri para formar las urls';
Comment on column gus_pregun.pre_codi   is 'Identificador de pregunta';
Comment on column gus_pregun.pre_enccod is 'Identificador de encuesta';
Comment on column gus_pregun.pre_imagen is 'Imagen pregunta';
Comment on column gus_pregun.pre_multir is 'Mutirespuesta';
Comment on column gus_pregun.pre_viscmp is 'Visible el componente';
Comment on column gus_pregun.pre_obliga is 'Obligacion responder';
Comment on column gus_pregun.pre_visib  is 'Indica si es visible';
Comment on column gus_pregun.pre_orden  is 'Orden de Aparicion';
Comment on column gus_pregun.pre_nresp  is 'Numero de Respuesta';
Comment on column gus_preidi.pid_codidi is 'Identificador del idioma ca,es,de,en';
Comment on column gus_preidi.pid_precod is 'Identificador de la preguta en la ecuenta';
Comment on column gus_preidi.pid_titulo is 'Título de la pregunta de la encuesta';
Comment on column gus_residi.rei_codidi is 'Identificador del idioma ca,es,de,en';
Comment on column gus_residi.rei_rescod is 'Codigo Respuesta';
Comment on column gus_residi.rei_titulo is 'Título de la respuesta';
Comment on column gus_respus.res_codi   is 'Codigo Respuesta';
Comment on column gus_respus.res_precod is 'Codigo de Pregunta';
Comment on column gus_respus.res_orden  is 'Orden aparicion';
Comment on column gus_respus.res_nresp  is 'Número Respuesta';
Comment on column gus_respus.res_tipo   is 'Tipo respuesta';
Comment on column gus_stats.sta_codi    is 'Identificador de estadistica';
Comment on column gus_stats.sta_item    is 'Identificador del Item';
Comment on column gus_stats.sta_mes     is 'Mes';
Comment on column gus_stats.sta_ref     is 'Referencia';
Comment on column gus_stats.sta_nacces  is 'Número de Accesos';
Comment on column gus_stats.sta_miccod  is 'Identificador de Microsite';
Comment on column gus_stats.sta_pub     is '0=privado, 1=publico';
Comment on column gus_temas.tem_codi    is 'Identificador del Tema agrupador de las faq';
Comment on column gus_temas.tem_miccod  is 'Identificador Microsite';
Comment on column gus_temidi.tid_codidi is 'Identificador del idioma ca,es,de,en';
Comment on column gus_temidi.tid_temcod is 'Codigo Tema';
Comment on column gus_temidi.tid_nombre is 'Nombre del tema que agrupa las faq';
Comment on column gus_tipser.tps_codi   is 'Identificador tipo de servicio';
Comment on column gus_tipser.tps_nombre is 'Nombre de servicio';
Comment on column gus_tipser.tps_visib  is 'Indica si es o no visible';
Comment on column gus_tipser.tps_ref    is 'Referencia significativa';
Comment on column gus_tipser.tps_url    is 'URL del action que lo gestiona';
Comment on column gus_tipser.tps_tipo   is 'Tipo de servicio 0, 1, 2';
Comment on column gus_tpnidi.tpi_codidi is 'Identificador del idioma ca,es,de,en';
Comment on column gus_tpnidi.tpi_tipcod is 'Codigo tipo listado';
Comment on column gus_tpnidi.tpi_nombre is 'Nombre del listado';
Comment on column gus_tpnidi.tpi_uri    is 'Uri para formar las urls del tipo';
Comment on column gus_tpnoti.tpn_codi   is 'Identificador tipo de listado';
Comment on column gus_tpnoti.tpn_miccod is 'Identificador Microsite';
Comment on column gus_tpnoti.tpn_tampag is 'Numero de registros si se aplica paginación';
Comment on column gus_tpnoti.tpn_tippag is 'Indica si se aplica paginación al listado 0.per nombre de registres/1.per any, segons data de publicació';
Comment on column gus_tpnoti.tpn_tpelem is 'Tipo de elemento: 0.Fitxa, 1.Enllaç, 2.Llista de Documents, 3.Conexió externa, 4.Galeria de fotos, 5.Mapa';
Comment on column gus_tpnoti.tpn_buscar is 'Indicador de si se aplica buscador al listado o no';
Comment on column gus_tpnoti.tpn_orden  is '0=por campo orden; 1=por fecha publicacion ascendente; 2=por fecha publicacion descendente;3=por titulo';
Comment on column gus_tpnoti.tpn_xurl   is 'URL de conexion listado externo';
Comment on column gus_tpnoti.tpn_xjndi  is 'No se utiliza: JNDI de conexion';
Comment on column gus_tpnoti.tpn_xlocal is 'No se utiliza: EJB Local o Remoto, (L o R)';
Comment on column gus_tpnoti.tpn_xauth  is 'No se utiliza: Tipo de autenticacion, 1=no, 2=estandar, 3=caib';
Comment on column gus_tpnoti.tpn_clasif is 'Clasificador de listados para el BackOffice de microsites';
Comment on column gus_usuarienc.use_codi   is 'Identificador usuario de encuestaa';
Comment on column gus_usuarienc.use_nombre is 'Nombre completo del usuario';
Comment on column gus_usuarienc.use_observ is 'Observaciones ';
Comment on column gus_usuresp.usr_codusu   is 'Identificador Usuario';
Comment on column gus_usuresp.usr_codresp  is 'Identificador Respuesta';
Comment on column gus_fr_tema.fte_nombre   is 'Nom del tema';
Comment on column gus_fr_tema.fte_css is 'CSS General del tema';
Comment on column gus_fr_tema.fte_actualizacion is 'Data d''actualització del tema';
Comment on column gus_fr_tema.fte_uri is 'URI única del tema, usada para formar urls de recursos';
Comment on column gus_fr_plantilla.pla_nombre is 'Nombre (identificador) de la plantilla';
Comment on column gus_fr_plantilla.pla_descripcion is 'Descripción y documentación de la plantilla';
Comment on column gus_fr_plantilla.pla_titulo is 'Título de la plantilla';
Comment on column gus_fr_version.fve_version  is 'versión de GUSITE para la que se ha desarrollado el tema';
Comment on column gus_fr_version.fve_nombre   is 'Nom del tema';
Comment on column gus_fr_perpla.ppl_titulo    is 'Título de la plantilla';
Comment on column gus_fr_perpla.ppl_orden     is 'Número de orden de la personalización de plantilla';
Comment on column gus_fr_perpla.ppl_contenido is 'Contenido de la plantilla';
Comment on column gus_fr_archivo.arc_codi is 'Identificador de documento';
Comment on column gus_fr_archivo.arc_path is 'Ruta virtual del archivo';
Comment on column gus_soljob.job_id is 'Identificador de la clase.';
Comment on column gus_soljob.job_fecini is 'Fecha de inicio';
Comment on column gus_soljob.job_fecfin is 'Fecha fin';
Comment on column gus_soljob.job_tipo is 'Tipo de job, siendo IDX_TODO - TODO, IDX_UA - INDEX BY UA, IDX_PDT - INDEXAR PENDIENTE, IDX_MIC - INDEXAR MICROSITE.';
Comment on column gus_soljob.job_descri is 'Info adicional';
Comment on column gus_soljob.job_idelem is 'Identificador del id elemento, microsite/ua';  
Comment on column gus_notics.not_latitud is 'Indica la ubicación de la notícia (Latitud)';
Comment on column gus_notics.not_longitud is 'Indica la ubicación de la notícia (Longitud)';
Comment on column gus_notics.not_icocolor is 'Indica el color hexadecimal del marcador de la ubicación (color del icono en el mapa)';
Comment on column gus_fcoidi.fci_codidi is 'Identificador del idioma ca,es,de,en,fr';
Comment on column gus_fcoidi.fci_fcocod is 'Codigo formulario de contacto';
Comment on column gus_fcoidi.fci_nombre is 'Nombre del formulario de contacto';
Comment on column gus_fcoidi.fci_uri    is 'Uri para formar las urls del formulario de contacto';

-- After section
ALTER TABLE GUS_ACTIDI ADD 
  CONSTRAINT GUS_ATIIDI_FK 
  FOREIGN KEY (ATI_CODIDI) 
  REFERENCES GUS_IDIOMA (IDI_CODI);

ALTER TABLE GUS_AGEIDI ADD 
  CONSTRAINT GUS_AIDIDI_FK 
  FOREIGN KEY (AID_CODIDI) 
  REFERENCES GUS_IDIOMA (IDI_CODI);
  
ALTER TABLE GUS_CMPIDI ADD
  CONSTRAINT GUS_CPIIDI_FK 
  FOREIGN KEY (CPI_CODIDI) 
  REFERENCES GUS_IDIOMA (IDI_CODI);
  
ALTER TABLE GUS_CONIDI ADD
  CONSTRAINT GUS_CIDIDI_FK 
  FOREIGN KEY (CID_CODIDI) 
  REFERENCES GUS_IDIOMA (IDI_CODI);
  
ALTER TABLE GUS_ENCIDI ADD
  CONSTRAINT GUS_EIDIDI_FK 
  FOREIGN KEY (EID_CODIDI) 
  REFERENCES GUS_IDIOMA (IDI_CODI);
  
ALTER TABLE GUS_FAQIDI ADD
  CONSTRAINT GUS_FIDIDI_FK 
  FOREIGN KEY (FID_CODIDI) 
  REFERENCES GUS_IDIOMA (IDI_CODI);

ALTER TABLE GUS_FRMIDI ADD
  CONSTRAINT GUS_RIDIDI_FK 
  FOREIGN KEY (RID_CODIDI) 
  REFERENCES GUS_IDIOMA (IDI_CODI);
  
ALTER TABLE GUS_FRQIDI ADD
  CONSTRAINT GUS_FQIIDI_FK 
  FOREIGN KEY (FQI_CODIDI) 
  REFERENCES GUS_IDIOMA (IDI_CODI);
  
ALTER TABLE GUS_MICIDI ADD
  CONSTRAINT GUS_MIDIDI_FK 
  FOREIGN KEY (MID_CODIDI) 
  REFERENCES GUS_IDIOMA (IDI_CODI);
  
ALTER TABLE GUS_MNUIDI ADD
  CONSTRAINT GUS_MDIIDI_FK 
  FOREIGN KEY (MDI_CODIDI) 
  REFERENCES GUS_IDIOMA (IDI_CODI);
  
ALTER TABLE GUS_NOTIDI ADD
  CONSTRAINT GUS_NIDIDI_FK 
  FOREIGN KEY (NID_CODIDI) 
  REFERENCES GUS_IDIOMA (IDI_CODI);
  
ALTER TABLE GUS_PREIDI ADD
  CONSTRAINT GUS_PIDIDI_FK 
  FOREIGN KEY (PID_CODIDI) 
  REFERENCES GUS_IDIOMA (IDI_CODI);

ALTER TABLE GUS_RESIDI ADD
  CONSTRAINT GUS_REIIDI_FK 
  FOREIGN KEY (REI_CODIDI) 
  REFERENCES GUS_IDIOMA (IDI_CODI);
  
ALTER TABLE GUS_TEMIDI ADD
  CONSTRAINT GUS_TIDIDI_FK 
  FOREIGN KEY (TID_CODIDI) 
  REFERENCES GUS_IDIOMA (IDI_CODI);

ALTER TABLE GUS_TPNIDI ADD
  CONSTRAINT GUS_TPIIDI_FK 
  FOREIGN KEY (TPI_CODIDI) 
  REFERENCES GUS_IDIOMA (IDI_CODI);

ALTER TABLE GUS_FCOIDI ADD
  CONSTRAINT GUS_FCIIDI_FK 
  FOREIGN KEY (FCI_CODIDI) 
  REFERENCES GUS_IDIOMA (IDI_CODI);
  
-- SEQUENCIES
CREATE SEQUENCE gus_seqsolr;
CREATE SEQUENCE gus_sequsuenc;
CREATE SEQUENCE gus_seqcmp;
CREATE SEQUENCE gus_seqcon;
CREATE SEQUENCE gus_seqban;
CREATE SEQUENCE gus_seqage;
CREATE SEQUENCE gus_seqact;
CREATE SEQUENCE gus_seq_all;
CREATE SEQUENCE gus_seqtpn;
CREATE SEQUENCE gus_seqtip;
CREATE SEQUENCE gus_seqtem;
CREATE SEQUENCE gus_seqsta;
CREATE SEQUENCE gus_seqres;
CREATE SEQUENCE gus_seqrda;
CREATE SEQUENCE gus_seqpre;
CREATE SEQUENCE gus_seqnot;
CREATE SEQUENCE gus_seqmpr;
CREATE SEQUENCE gus_seqmic;
CREATE SEQUENCE gus_seqmen;
CREATE SEQUENCE gus_seqfrq;
CREATE SEQUENCE gus_seqfrm;
CREATE SEQUENCE gus_seqfli;
CREATE SEQUENCE gus_seqfaq;
CREATE SEQUENCE gus_seqenc;
CREATE SEQUENCE gus_seqdoc;
CREATE SEQUENCE gus_sqm_all;
CREATE SEQUENCE gus_conv_seq;
CREATE SEQUENCE gus_ldistrb_seq;
CREATE SEQUENCE gus_seqaud; 
CREATE SEQUENCE gus_seqfte;
CREATE SEQUENCE gus_seqpla;
CREATE SEQUENCE gus_seqfve;
CREATE SEQUENCE gus_seqppl;
CREATE SEQUENCE gus_seqarc;
CREATE SEQUENCE gus_seqsolj;


