<%@ page language="java" contentType="text/html; charset=utf8"  pageEncoding="utf8"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>



<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<meta http-equiv="X-UA-Compatible" content="IE=edge" />
	  <META http-equiv="CACHE-CONTROL" content="NO-CACHE">  <!-- For HTTP 1.1 -->
	  <META http-equiv="PRAGMA" content="NO-CACHE">         <!-- For HTTP 1.0 -->
	  <META http-equiv="refresh" content="15; URL=detallelogexport.do">	
	<title>Gestor Microsites</title>
	<link href="css/estils.css" rel="stylesheet" type="text/css" />
	<style>
	body { background:#FFE2C2; }
	</style>
</head>

<body>	
	
	<em>
		<bean:message key="micro.exportar.todos.log" /> <bean:write name="MVS_fechaexportprocessor" format="dd/MM/yyyy HH:mm:ss"/>
	</em>

	<br/><br/>
	
	<bean:write name="MVS_importprocessor" ignore="true" filter="false"/>

</body>
</html>