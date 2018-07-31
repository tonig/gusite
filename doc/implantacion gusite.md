# Instalacion de GUSITE en local para base de datos postgresql

1. Cambiar el nombre del fichero “build.USERNAME.properties” sustituyendo “USERNAME” por el nombre del usuario del sistema, ej.: mi usuario es ddorado, entonces tendré que cambiar el nombre a → build.ddorado.properties.
2. Cambiar el contenido del fichero build.USERNAME.properties:
	- en deploy.dir, poner la url del directorio de despliegue. Ej. : en mi caso tengo el jboss desplegado en mi directorio desarrollo de la unidad C de mi disco duro, entonces tendré que poner la siguiente línea “deploy.dir=C:\\desarrollo\\jboss-5.2.0\\server\\default\\deploycaib”
	- De igual forma el parametro jbossServiceDeploy.dir tendré que poner lo mismo. Ej: para el ejemplo anterior “jbossServiceDeploy.dir=C:\\desarrollo\\jboss-5.2.0\\server\\default\\deploycaib”  
	- El siguiente parámetro a modificar es el mvn.repository.path que es el directorio donde esta el repositorio de maven, Ej.: en mi caso el directorio esta en ddorado/.m2/repository con lo cual en ese parámetro habría que poner “mvn.repository.path=C:\\Users\\ddorado\\.m2\\repository\\”       
3. En build.xml cambiar la url “http://GovernIB.github.io/maven/maven/” por “https://raw.githubusercontent.com/GovernIB/maven/gh-pages/maven/”    
4. En default.properties cambiar 
    - “hibernate.dialect=org.hibernate.dialect.Oracle9iDialect” por “hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect”
    - y “hibernate.jdbc.use_streams_for_binary=false” por “hibernate.jdbc.use_streams_for_binary=true”
5. Añadimos en el fichero moduls/utilities/build.xml la siguiente linea: “<include name="jcommon-1.0.0.jar"/>” en la definicion del fileset “<fileset dir="${lib.dir}/webcaib">” (linea 37)

// para que nos funcione en local, hay que moquear la salida de cualquier objeto UnitatAdministrativa.

6. Creamos una clase Mock que nos haga la implementación de UnitatAdministrativaQueryServiceStrategy.
        ◦ Mockeamos la salida del método getUnidades de RolsacOrganigramaProvider para que recoja datos de la UnitatAdministrativa y añadimos la clase mock creada
        ◦ Mockeamos la salida del método getUnidadesPrincipales de RolsacOrganigramaProvider para que recoja datos de la UnitatAdministrativa y añadimos la clase mock creada
        ◦ Mockeamos la salida del método getUnidadesHijas de RolsacOrganigramaProvider para que recoja datos de la UnitatAdministrativa y añadimos la clase mock creada
        ◦ Mockeamos la salida del método getUnidadesData de RolsacOrganigramaProvider para que recoja datos de la UnitatAdministrativa y añadimos la clase mock creada
          
Para poder utilizar una base de datos PostgreSQL necesitaremos:
       
7. Incluir un fichero en el directorio de despliegue de jboss, con nombre “gusite-ds.xml” y con el siguiente contenido que define el datasource:       
````
       <?xml version="1.0" encoding="UTF-8"?>
       <datasources>
         <local-tx-datasource>
           <jndi-name>es.caib.microsite.db</jndi-name>
       	<!--  POSTGRESQL   -->
           <connection-url>jdbc:postgresql://host_postgres:5432/gusite</connection-url>
           <driver-class>org.postgresql.Driver</driver-class>
           <user-name>usuario_postgres</user-name>
           <password>password_postgres</password>
         </local-tx-datasource>
       </datasources>       
````	   
8. Ejecutar las sentencias incluidas en los ficheros
    - %GUSITE%/scripts/bbdd/1.5/postgres/creacion.sql
    - %GUSITE%/scripts/bbdd/1.5/postgres/gusite_create_schema.sql
    - %GUSITE%/scripts/bbdd/1.5/postgres/gusite_create_data.sql
          
Para obtener las nuevas funcionalidades de plantillas asociadas a los contenidos, ejecutar las siguientes sentencias en la base de datos GUSITE de postgres (estas sentencias están en el fichero %GUSITE%/scripts/bbdd/1.5/postgres/gusite_actualizaciones.sql)
       
9. ALTER TABLE gus_micros ADD COLUMN mic_indexado numeric(1,0) DEFAULT 0;
10. ALTER TABLE gus_micros ADD COLUMN mic_idxcor numeric(2,0) DEFAULT -1;
11. ALTER TABLE gus_micros ADD COLUMN mic_menori varchar2(1)  DEFAULT 'C';
12. ALTER TABLE gus_conten ADD COLUMN con_plantilla numeric DEFAULT -1;
13. INSERT INTO gus_musuar (msu_codi, msu_userna, msu_passwo, msu_nombre, msu_observ, msu_perfil) VALUES (1000001, 'admin', 'admin', 'Usuario administrador', 'admin@gusite.es', 'gusadmin');
14. Insertar las propiedades correspondientes del fichero jboss-service.xml

**Nota:** En el repositorio con los cambios nuevos habrá que realizar los pasos 1, 2, 7, 8, 9, 10, 11, 12, 13, 14 anteriores.

# Nueva funcionalidad de plantillas para contenidos

Esta funcionalidad se basa en poder definir una plantilla distinta para cada uno de los contenidos que están asociados a un microsite.

Con este objetivo, antes de crear los contenidos, deberiamos (no es necesario) tener al menos una plantilla tipo "contenido/contenido" la cual nos servirá como plantilla por defecto, el resto de plantillas las tomará el sistema como plantillas que pueden asociarse a un contenido, apareciendo en una lista desplegable a la hora de insertar nuevos contenidos. Si no tuvieramos plantillas, en el formulario de contenidos, no nos aparecera la lista desplegable.

![imagen de formulario de contenidos](file:///pantallazo_seleccion_de_plantilla_para_contenido.png){ width=100% }.

Una vez rellenada la información necesaria para el contenido, en el microsite debería aparecer el contenido con la nueva plantilla seleccionada.

**Nota:** Para poder activar o desactivar el mockeo que las unidades administrativas, se ha puesto un parametro en jboss-service.xml para poder configurarlo:
- es.caib.gusite.api.rolsac.mock=S => realizará el mock de las unidades administrativas
- es.caib.gusite.api.rolsac.mock=X => sin valor, con otro valor distinto de S o si no existe, el mock no se realizará.
