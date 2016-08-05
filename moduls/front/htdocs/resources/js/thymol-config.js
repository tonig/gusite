var	thRoot="//dgtic/web/proyectos/ADCaib/_gforge_caib_svn/gusite-1.2_tc/moduls/front/htdocs";	// In an exploded war, this is usually webapp root
var	thPath="./";		// In an exploded war, this is usually WEB-INF/<some-path>
var thDebug = true;


//PARA DEFINIR VARIABLES
var thVars = [
            [ "MVS_css",     "resources/css/estils.css" ],
            [ "MVS_micrositetitulo",     "Título Microsite Thymol" ],
            [ "MVS_home_campanya",        "Campaña Thymol" ],
            [ "MVS_microsite",     "MicroSite Thymol" ],
            [ "MVS_microsite.tipomenu",     "1" ],
            [ "MVS_home_agenda_calendario",     "-1" ],
            [ "MVS_home_agenda_listado",     "Agenda Listado Thymol" ],
            [ "MVS_home_noticias",     "-1" ],
            [ "MVS_microsite.tipocabecera",     "1" ],
            [ "MVS_microsite.traduce.cabecerapersonal",     "Valor 2 traducido cabecera" ],
            [ "MVS_microsite.restringido",     "S" ],
            [ "MVS2_mollapan",     "Mollapan Thymol" ],
            [ "MVS_microsite.menucorporativo",     "S" ],
            [ "MVS_idioma",     "ca" ],
            [ "MVS2_uos",     "#[ 'uo1', 'uo2', 'uo3', 'uo4']" ],
            [ "i.codi",     "1" ],
            [ "i.abreviatura",     "Organigrama Abreviatura Thymol" ],
            [ "request.getHeader('Authorization')",     "Autorizado" ],
            [ "MVS_microsite.claveunica",     "Clave única Thymol" ],
            [ "MVS_microsite.tipopie",     "1" ],
            [ "MVS_microsite.traduce",     "distinto null" ],
            [ "MVS_microsite.traduce.piepersonal",     "Valor 2 traducido pie" ],
            [ "MVS_microsite.id",     "Id Microsite" ],
            [ "MVS_seulet",     "seulet no null" ],
            [ "MVS_listaidiomas",     "#[ 'Castellano', 'Català']" ],
            [ "i.key",    "ca" ],
            [ "i.value",    "Català" ],
            [ "MVS_listacabecera",    "#[ 'http://www.cabecera1.com', 'http://www.cabecera2.com', 'http://www.cabecera3.com', 'http://www.cabecera4.com']" ],
            [ "i.value1",    "http://www.cabecera1.ca" ],
            [ "direccion",    "Carrer caib, 87" ],
            [ "MVS_microsite.buscador",    "S" ],
            [ "MVS_idsite",    "siteID" ],
            [ "MVS_menu",    "#[ 'Menu1', 'Menu2', 'Menu3', 'Menu4']" ],
            [ "MVS_contenido",    "Contenido Thymol" ],
            [ "MVS_contenido.id",    "2" ],
            [ "MVS_menu_cont_notic",    null ],
            [ "i.traduce",    "Traduce Menu" ],
            [ "i.modo",    "'4'" ],
            [ "i.traduce.nombre",    "Nombre traducido" ],
            [ "MVS_microsite.idiomas",    "#[ 'Español', 'Català', 'English', 'Deutsch']" ],
            [ "i.imagenmenu",    "Imagen menu" ],
            [ "MVS_idsite",    "Id Site" ],
            [ "i.imagenmenu.id",    "Id Imagen menu" ],
            [ "MVS_contacto_titulo",    "Señor Contacto titulo" ],
            [ "MVS_home_tmp_campanya",    "Home Temp Campaña" ],
            [ "i.traduce.nombre",    "Nombre traducido" ],
            [ "MVS_listapie",    "#[ 'http://www.pie1.com', 'http://www.pie2.com', 'http://www.pie3.com', 'http://www.pie4.com']" ],
            [ "MVS_listadoanyos",    "#[ 'año1', 'año2', 'año3', 'año4']" ],
            [ "MVS_claseelemento.id",    "Id elemento" ],
            [ "MVS_tipolistado",    "Listado tipo 1" ],
            [ "MVS_anyo",    "Año listado" ],
            [ "MVS_claseelemento.buscador",    "S" ],
            [ "MVS_parametros_pagina.nreg",    "0" ],
            [ "MVS_noticia.traduce.titulo",    "Noticia traduce título" ],
            [ "MVS_noticia.traduce.subtitulo",    "Noticia traduce subtítulo" ],
            [ "MVS_noticia.traduce.texto",    "Noticia traduce texto" ],
            [ "MVS_noticia",    "Noticia" ],
            [ "MVS_agenda",    "Agenda" ],
            [ "MVS_contenido.traduce.titulo",    "Contenido traduce título" ],
            [ "MVS_contenido.traduce.texto",    "Contenido traduce texto" ],
            [ "MVS_contenido.traduce",    "Contenido traduce" ],
            [ "MVS_tipobeta",    "beta" ],
            [ "MVS_contenido.traduce.txbeta",    "Contenido beta" ],
            [ "MVS_contenido.traduce.texto",    "Contenido NO beta" ],
            [ "MVS_agenda_diaevento",    "Agenda día evento" ],
            [ "MVS_agenda_lista",    "#[ 'agenda1', 'agenda2', 'agenda3', 'agenda4']" ],
            [ "i.actividad.traduce",    "Actividad traduce" ],
            [ "i.actividad.traduce.nombre",    "actividad traduce nombre" ],
            [ "i.actividad",    "actividad i" ],
            [ "i.traduce.titulo",    "traduce título i" ],            
            [ "i.finicio",    "24/11/1987" ],            
            [ "i.ffin",    "04/07/2014" ],
            [ "i.traduce.imagen",    "traduce imagen i" ],
            [ "MVS_servicio",    "MVS_servicio" ],
            [ "i.id",    "i id" ],
            [ "i.traduce.imagen.id",    "id traduce imagen i" ],
            [ "i.traduce.descripcion",    "traduce descripcion i" ],
            [ "i.traduce.documento",    "traduce documento i" ],
            [ "i.traduce.documento.mime",    "traduce documento i mime" ],
            [ "i.traduce.documento.peso",    "traduce documento i peso" ],
            [ "i.traduce.documento.id",    "traduce documento i id" ],
            [ "i.traduce.documento.nombre",    "traduce documento i nombre" ],
            [ "i.traduce.url",    "traduce url i" ],
            [ "i.traduce.urlnom",    "traduce nombre url i" ],

            
            
        ];

//PARA DEFINIR MESSAGES .PROPERTIES
var thMessages = [
            
                  ["microsites.name",                             "CAIB - Microsites"],
                  ["microsites.build",                             "01/07/2010"],
                  ["agenda.agenda",                             "Agenda"],
                  ["WEB2_GEN003",                             "Serveis"],
                  ["WEB_CIU002",                             "Inventari de procediments"],
                  ["WEB_CIU028",                             "Ajuts, beques i subvencions"],
                  ["WEB2_SEULINK",                             "http://seuelectronica.caib.es"],
                  ["WEB2_SEU",                             "Seu electrónica"],
                  ["WEB2_CONGENERAL",                             "http\://www.plataformadecontractacio.caib.es"],
                  ["WEB2_CONTRATACIO",                             "Contractació"],
                  ["WEB_CIU064",                             "/sacmicrofront/home.do?mkey=M10050410524913563635&amp;lang=ca&amp;campa=yes"],
                  ["WEB2_TEM102",                             "Atenció al ciutadà"],
                  ["WEB2_GEN137",                             "/sacmicrofront/contenido.do?idsite=292&amp;cont=7654&amp;lang=ca"],
                  ["WEB2_GEN138",                             "Compromisos de Servei"],
                  ["WEB2_GEN140",                             "https\://www.caib.es/sistrafront/inicio?language\=ca&modelo\=PD0018ENCW&version\=1"],
                  ["WEB2_GEN139",                             "Ajuda'ns a millorar el web"],
                  ["WEB_MOD041",                             "Emergències 112"],
                  ["WEB_CIU003",                             "El Govern"],
                  ["WEB2_GOV001",                             "Presidència"],
                  ["WEB_POR015",                             "Conselleries"],
                  ["WEB_ILL013",                             "El president"],
                  ["WEB2_TEM103",                             "Descobreix Balears"],
                  ["WEB2_GEN141",                             "Sala de premsa"],
                  ["WEB2_GEN119",                             "Convocatòries"],
                  ["WEB2_GEN123",                             "Notes de premsa"],
                  ["COM_COG001",                             "Consells de Govern"],
                  ["COM_VID001",                             "Videoteca"],
                  ["COM_COM013",                             "Guia de la comunicació"],
                  ["COM_COM003",                             "Mitjans a internet"],
                  ["COM_COM018",                             "Directori Twitter"],
                  ["COM_COM019",                             "Manual d'identitat corporativa"],
                  ["WEB_CER020",                             "Butlletí Oficial"],
                  ["cabecera.volverinicio",                             "Torna a l`inici del portal"],
                  ["general.accessibilitat",                             "Accessibilitat"],
                  ["general.traduccion",                             "Automatic translation. Sorry for the inconvenience"],
                  ["cercador.cercau",                             "Cerqueu"],
                  ["cercador.cercar",								"Cercar"],
                  ["cabecera.idioma",                             "Idioma"],
                  ["accessibilitat.compromis",                             "Compromís del Govern de les Illes Balears"],
                  ["accessibilitat.compromis.texte1",                             "Un dels principals objectius d'interés social del Govern de les Illes Balears és posar tota la informació de la  web a disposició del major nombre de ciutadans possible, evitant la info-exclussió; i promovent l'accessibilitat per aconseguir la universalització de la informació."],
                  ["accessibilitat.compromis.texte2",                             "Seguint les recomanacions de la <acronym title='Web Accessibility Iniciative'>WAI</acronym> (traduït al català , <em>Iniciativa per l'Accessibilitat a la Web</em>) hem utilitzat unes pautes referents a millorar  la navegació i accedir  tots els continguts dels microsites d'una manera clara i senzilla. Amb això, pretenem aconseguir  un nivell Doble A d'accessibilitat, a més de la major part de les del nivell Triple A, objectiu ja assolit en el portal o Web Coporativa, i no tant en les webs departamentals o microsites. A  aquest efecte, tenim incorporada en l'edició de qualsevol microsite l'execució automàtica del test  W3C, que desplega les mancances  d'accessibilitat  de prioritat 1 i 2. Si l'usuari gestor les atén i corregeix, es podrà  afirmar que el microsite segueix les normes de programació establertes per la W3C, estament internacional encarregat de crear els estàndards de creació i comunicació a Internet."],
                  ["accessibilitat.compromis.texte3",                             "Tot i això, el Govern de les Illes Balears pretén seguir millorant i oferir als ciutadans el millor servei possible. Si teniu qualsevol iniciativa, dubte, o suggeriment, vos demanam que us poseu en contacte amb nosaltres mitjançant la pàgina de <a href='http: //web2.caib.es/qssi?lang=ca'>contacte</a>."],
                  ["accessibilitat.dreceres",                             "Dreceres de teclat"],
                  ["accessibilitat.dreceres.texte1",                             "Hem configurat tecles d'accés directe als apartats més importants de la web. Són les següents:"],
                  ["accessibilitat.dreceres.texte2",                             "<dt>Tecla <span>0</span></dt><dd>Anar a la pàgina d'inici del portal</dd><dt>Tecla <span>1</span></dt><dd>Accesabilitat</dd><dt>Tecla <span>2</span></dt><dd>Anar a la pàgina d'inici del microsite</dd>"],
                  ["accessibilitat.dreceres.texte3",                             "Per usar aquestes dreceres de teclat heu de pitjar:"],
                  ["accessibilitat.dreceres.texte4",                             "<dt>Internet Explorer per a Windows</dt><dd><abbr title='Tecla ALT'>ALT</abbr> + dresera seguit de la tecla <abbr title='Tecla INTRO'>INTRO</abbr></dd><dt>Firefox per a Windows Ver. 1.5 e inferiors , Mozilla per a Windows, Netscape Ver. 6 y superiors per Windows</dt><dd><abbr title='Tecla ALT'>ALT</abbr> + dresera</dd><dt>Firefox Ver. 2 per a Windows</dt><dd><abbr title='Tecla ALT'>ALT</abbr> + <abbr title='Tecla SHIFT'>SHIFT</abbr> + dresera</dd><dt>Opera per a Windows o Mac</dt><dd><abbr title='Tecla SHIFT'>SHIFT</abbr> + <abbr title='Tecla ESC'>ESC</abbr> + dresera</dd><dt>Safari Ver. 1.2 (sols Mac), Firefox per a Mac (incluyendo Firefox Ver. 2), Mozilla per a Mac, Netscape Ver. 6 y superiors per Mac</dt><dd><abbr title='Tecla CTRL'>CTRL</abbr> + dresera</dd>"],
                  ["accessibilitat.eines",                             "Eines de navegació a Internet"],
                  ["accessibilitat.eines.texte1",                             "Des de la web del Govern volem aconsellar a tots el ciutadans l'ús dels millors navegadors web, per això recomanem la descàrrega de les darreres versions d'aquestos per gaudir d'una connexió segura, ràpida i satisfactòria."],
                  ["accessibilitat.eines.texte2",                             "Aquí teniu enllaços per instal·lar les versions més noves i completes dels navegadors més coneguts:"],
                  ["pleasewait.favor",                             "Esperi, si us plau"],
                  ["pleasewait.peticion",                             "La seva petició s`està  processant"],
                  ["mapa.mapaweb",                             "Mapa web"],
                  ["mapa.apartados",                             "Apartats generals"],
                  ["noticia.anyos",                             "Anys"],
                  ["listarnoticias.introduce",                             "Introduïu el text a cercar"],
                  ["general.buscar",                             "Cerca"],
                  ["listarnoticias.nohay",                             "No s`han trobat"],                  
                  ["agenda.eventos",                             "Esdeveniments"],                  
                  ["agenda.evento",                             "esdeveniment"],
                  ["agenda.cuando",                             "a les"],
                  ["agenda.descdocumento",                             "Per a <strong>més informació</strong> podeu descarregar el següent document "],
                  ["general.archivo",                             "Arxiu"],
                  ["url.adicional",                             "Informació Addicional"],
                  ["agenda.nohay",                             "No s`ha trobat cap acte o esdeveniment per aquest dia"]

                  
                  
                  
                  
                  
              ];

