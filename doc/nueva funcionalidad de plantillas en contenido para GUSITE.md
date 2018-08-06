# Nueva funcionalidad de plantillas para contenidos

Esta funcionalidad se basa en poder definir una plantilla distinta para cada uno de los contenidos que están asociados a un microsite.

Con este objetivo, antes de crear los contenidos, deberiamos (no es necesario) tener al menos una plantilla tipo "contenido/contenido" la cual nos servirá como plantilla por defecto, el resto de plantillas las tomará el sistema como plantillas que pueden asociarse a un contenido, apareciendo en una lista desplegable a la hora de insertar nuevos contenidos. Si no tuvieramos plantillas, en el formulario de contenidos, no nos aparecera la lista desplegable.

![imagen de formulario de contenidos](file:///pantallazo_seleccion_de_plantilla_para_contenido.png){ width=100% }.

Una vez rellenada la información necesaria para el contenido, en el microsite debería aparecer el contenido con la nueva plantilla seleccionada.

Para obtener las nuevas funcionalidades de plantillas asociadas a los contenidos, ejecutar las siguientes sentencias en la base de datos GUSITE de postgres (estas sentencias están en el fichero %GUSITE%/scripts/bbdd/1.5/postgres/gusite_actualizaciones.sql)
       
1. ALTER TABLE gus_micros ADD COLUMN mic_indexado numeric(1,0) DEFAULT 0;
2. ALTER TABLE gus_micros ADD COLUMN mic_idxcor numeric(2,0) DEFAULT -1;
3. ALTER TABLE gus_micros ADD COLUMN mic_menori varchar2(1)  DEFAULT 'C';
4. ALTER TABLE gus_conten ADD COLUMN con_plantilla numeric DEFAULT -1;
5. INSERT INTO gus_musuar (msu_codi, msu_userna, msu_passwo, msu_nombre, msu_observ, msu_perfil) VALUES (1000001, 'admin', 'admin', 'Usuario administrador', 'admin@gusite.es', 'gusadmin');
6. Insertar las propiedades correspondientes del fichero jboss-service.xml
7. ALTER TABLE gus_compos ADD COLUMN cmp_visual varchar2 DEFAULT 'L'
