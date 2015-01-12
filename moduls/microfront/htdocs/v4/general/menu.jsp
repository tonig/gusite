<%@ page language="java" contentType="text/html; charset=utf8"  pageEncoding="utf8"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>

                <!-- cercador -->
                <logic:equal name="MVS_microsite" property="buscador" value="S">
                    <h2 class="invisible">Cercador</h2>
                    <div id="indexCercador">
                        <form name="cercadorForm" action="cercar.do" method="post">
                            <input type="hidden" name="idsite" value="<bean:write name="MVS_idsite"/>">
                            <input type="hidden" name="lang" value="<bean:write name="MVS_idioma"/>">
                            <label for="cercadorTxt">
                                <span class="invisible">Text a cercar:</span>
                                <input name="cerca" id="cercadorTxt" type="text" value="" />
                            </label>
                            <button type="submit"><bean:message key="cercador.cercau"/></button>
                        </form>
                    </div>
                </logic:equal>
                <!-- /cercador -->


                <logic:present name="MVS_menu">
                    <h2 class="invisible">Menú general</h2>

                    <bean:define id="idcontenido" value="-1" type="java.lang.Object"/>
                    <logic:present name="MVS_contenido">
                        <bean:define id="idcontenido" name="MVS_contenido" property="id"/>
                    </logic:present>
                    <logic:present name="MVS_menu_cont_notic">
                        <bean:define id="idcontenido" name="MVS_menu_cont_notic" type="java.lang.String"/>
                    </logic:present>

                    <bean:define id="idiomaDefault" name="MVS_idioma"/>

                    <logic:equal name="MVS_microsite" property="tipomenu" value="1">
                        <ul>
                            <logic:iterate id="i" name="MVS_menu">
                                <logic:present name="i"  property="traducciones">

                                    <bean:define id="modo" name="i" property="modo"/>

                                    <logic:iterate id="j" name="i" property="traducciones">
                                        <bean:define id="idiomaIter" name="j" property="key"/>
                                        <% if ((("" + idiomaIter).toUpperCase()).equals("" + idiomaDefault)) { %>
                                            <li><a href="#" class="<%=((""+modo).equals("C"))?"pareADon":"fijo"%>"><bean:write name="j" property="value.nombre" filter="false"/></a>
                                        <% } %>
                                    </logic:iterate>

                                        <ul>
                                            <logic:iterate name="i" id="j" property="listacosas">

                                                <bean:define id="objeto" name="j" type="Object"/>
                                                <% if (objeto instanceof es.caib.gusite.micromodel.Contenido) { %>

                                                    <bean:define id="idconteactual1" name="j" property="id"/>
                                                    <bean:define id="urlExterna1" name="j" property="urlExterna"/>
                                                    <li<%=((""+idconteactual1).equals(""+idcontenido))?" id=\"p"+idconteactual1+"\" class=\"seleccionado\"":""%>>

                                                        <logic:iterate id="k" name="j" property="traducciones">
                                                            <bean:define id="idiomaIter" name="k" property="key"/>
                                                            <% if ((("" + idiomaIter).toUpperCase()).equals("" + idiomaDefault)) { %>
                                                                <a href="contenido.do?mkey=<bean:write name="MVS_microsite" property="claveunica" />&amp;lang=<bean:write name="MVS_idioma"/>&amp;cont=<bean:write name="j" property="id"/>" target="<%=((""+urlExterna1).equals("true"))?"_blank":"_self"%>" ><bean:write name="k" property="value.titulo"/></a>
                                                            <% } %>
                                                        </logic:iterate>
                                                    </li>

                                                <% } else { %>

                                                    <bean:size id="tamano" name="j" property="listacosas"/>
                                                    <logic:notEqual name="tamano" value="1">
                                                        <logic:present name="j" property="traducciones">

                                                            <logic:iterate id="k" name="j" property="traducciones">
                                                                <bean:define id="idiomaIter" name="k" property="key"/>
                                                                <% if ((("" + idiomaIter).toUpperCase()).equals("" + idiomaDefault)) { %>
                                                                    <li><a href="#" class="pareADon"><bean:write name="k" property="value.nombre"/></a>
                                                                <% } %>
                                                            </logic:iterate>

                                                            <ul>
                                                                <logic:iterate name="j" id="k" property="listacosas">
                                                                    <bean:define id="idconteactual" name="k" property="id"/>
                                                                    <bean:define id="urlExterna" name="k" property="urlExterna"/>
                                                                    <li<%=((""+idconteactual).equals(""+idcontenido))?" id=\"p"+idconteactual+"\" class=\"seleccionado\"":""%>>

                                                                        <logic:iterate id="l" name="k" property="traducciones">
                                                                            <bean:define id="idiomaIter" name="l" property="key"/>
                                                                            <% if ((("" + idiomaIter).toUpperCase()).equals("" + idiomaDefault)) { %>
                                                                                <a href="contenido.do?mkey=<bean:write name="MVS_microsite" property="claveunica"/>&amp;lang=<bean:write name="MVS_idioma"/>&amp;cont=<bean:write name="k" property="id"/>" target="<%=((""+urlExterna).equals("true"))?"_blank":"_self"%>"><bean:write name="l" property="value.titulo"/></a>
                                                                            <% } %>
                                                                        </logic:iterate>
                                                                    </li>
                                                                </logic:iterate>
                                                            </ul>
                                                            </li>
                                                        </logic:present>
                                                    </logic:notEqual>

                                                    <logic:equal name="tamano" value="1">
                                                        <bean:define id="idconteactual" name="j" property="listacosas[0].id"/>
                                                        <bean:define id="urlExterna" name="j" property="listacosas[0].urlExterna"/>
                                                        <li<%=((""+idconteactual).equals(""+idcontenido))?" id=\"p"+idconteactual+"\" class=\"seleccionado\"":""%>>

                                                            <logic:iterate id="k" name="j" property="traducciones">
                                                                <bean:define id="idiomaIter" name="k" property="key"/>
                                                                <% if ((("" + idiomaIter).toUpperCase()).equals("" + idiomaDefault)) { %>
                                                                    <a href="contenido.do?mkey=<bean:write name="MVS_microsite" property="claveunica"/>&amp;lang=<bean:write name="MVS_idioma"/>&amp;cont=<bean:write name="j" property="listacosas[0].id"/>" target="<%=((""+urlExterna).equals("true"))?"_blank":"_self"%>"><bean:write name="k" property="value.nombre"/></a>
                                                                <% } %>
                                                            </logic:iterate>

                                                        </li>
                                                    </logic:equal>
                                                <% } %>
                                            </logic:iterate>
                                        </ul>
                                    </li>
                                </logic:present>
                            </logic:iterate>
                        </ul>
                    </logic:equal>

                    <!-- menu con iconos -->
                    <logic:equal name="MVS_microsite" property="tipomenu" value="2">
                        <ul>
                            <logic:iterate id="i" name="MVS_menu">
                                <logic:present name="i"  property="traducciones">
                                    <li>
                                        <logic:present name="i" property="imagenmenu">

                                            <logic:iterate id="j" name="i" property="traducciones">
                                                <bean:define id="idiomaIter" name="j" property="key"/>
                                                <% if ((("" + idiomaIter).toUpperCase()).equals("" + idiomaDefault)) { %>
                                                    <img src="archivopub.do?ctrl=MCRST<bean:write name="MVS_idsite" />ZI<bean:write name="i" property="imagenmenu.id"/>&id=<bean:write name="i" property="imagenmenu.id"/>" alt="<bean:write name="j" property="value.nombre"/>"/>
                                                <% } %>
                                            </logic:iterate>

                                        </logic:present>

                                        <logic:iterate id="j" name="i" property="traducciones">
                                            <bean:define id="idiomaIter" name="j" property="key"/>
                                            <% if ((("" + idiomaIter).toUpperCase()).equals("" + idiomaDefault)) { %>
                                                <bean:write name="j" property="value.nombre" filter="false"/>
                                            <% } %>
                                        </logic:iterate>

                                        <ul>
                                            <logic:iterate name="i" id="j" property="listacosas">
                                                <bean:define id="objeto" name="j" type="Object"/>
                                                <% if (objeto instanceof es.caib.gusite.micromodel.Contenido) { %>
                                                    <bean:define id="idconteactual1" name="j" property="id"/>
                                                    <bean:define id="urlExterna1" name="j" property="urlExterna"/>
                                                    <li<%=((""+idconteactual1).equals(""+idcontenido))?" id=\"p"+idconteactual1+"\" class=\"seleccionado\"":""%>>

                                                        <logic:iterate id="k" name="j" property="traducciones">
                                                            <bean:define id="idiomaIter" name="k" property="key"/>
                                                            <% if ((("" + idiomaIter).toUpperCase()).equals("" + idiomaDefault)) { %>
                                                                <a href="contenido.do?mkey=<bean:write name="MVS_microsite" property="claveunica"/>&amp;lang=<bean:write name="MVS_idioma"/>&amp;cont=<bean:write name="j" property="id"/>" target="<%=((""+urlExterna1).equals("true"))?"_blank":"_self"%>"><bean:write name="k" property="value.titulo"/></a>
                                                            <% } %>
                                                        </logic:iterate>

                                                    </li>

                                                <% } else { %>
                                                    <bean:size id="tamano" name="j" property="listacosas"/>
                                                    <logic:notEqual name="tamano" value="1">
                                                        <li><a href="#">
                                                        
                                                            <logic:present name="j" property="imagenmenu">

                                                                <logic:iterate id="k" name="j" property="traducciones">
                                                                    <bean:define id="idiomaIter" name="k" property="key"/>
                                                                    <% if ((("" + idiomaIter).toUpperCase()).equals("" + idiomaDefault)) { %>
                                                                        <img src="archivopub.do?ctrl=MCRST<bean:write name="MVS_idsite" />ZI<bean:write name="j" property="imagenmenu.id"/>&id=<bean:write name="j" property="imagenmenu.id"/>" alt="<bean:write name="k" property="value.nombre"/>"/>
                                                                    <% } %>
                                                                </logic:iterate>

                                                            </logic:present>

                                                            <logic:iterate id="k" name="j" property="traducciones">
                                                                <bean:define id="idiomaIter" name="k" property="key"/>
                                                                <% if ((("" + idiomaIter).toUpperCase()).equals("" + idiomaDefault)) { %>
                                                                    <bean:write name="k" property="value.nombre"/>
                                                                <% } %>
                                                            </logic:iterate>

                                                        </a>
                                                        <ul>
                                                            <logic:iterate name="j" id="k" property="listacosas">
                                                                <bean:define id="idconteactual" name="k" property="id"/>
                                                                <bean:define id="urlExterna" name="k" property="urlExterna"/>
                                                                <li<%=((""+idconteactual).equals(""+idcontenido))?" id=\"p"+idconteactual+"\" class=\"seleccionado\"":""%>>

                                                                    <logic:iterate id="l" name="k" property="traducciones">
                                                                        <bean:define id="idiomaIter" name="l" property="key"/>
                                                                        <% if ((("" + idiomaIter).toUpperCase()).equals("" + idiomaDefault)) { %>
                                                                            <a href="contenido.do?mkey=<bean:write name="MVS_microsite" property="claveunica"/>&amp;lang=<bean:write name="MVS_idioma"/>&amp;cont=<bean:write name="k" property="id"/>" target="<%=((""+urlExterna).equals("true"))?"_blank":"_self"%>"><bean:write name="l" property="value.titulo"/></a>
                                                                        <% } %>
                                                                    </logic:iterate>

                                                                </li>
                                                            </logic:iterate>
                                                        </ul>
                                                        </li>
                                                    </logic:notEqual>

                                                    <logic:equal name="tamano" value="1">
                                                        <bean:define id="idconteactual" name="j" property="listacosas[0].id"/>
                                                        <bean:define id="urlExterna" name="j" property="listacosas[0].urlExterna"/>
                                                        <li<%=((""+idconteactual).equals(""+idcontenido))?" id=\"p"+idconteactual+"\" class=\"seleccionado\"":""%>>
                                                            <logic:present name="j" property="imagenmenu">

                                                                <logic:iterate id="k" name="j" property="traducciones">
                                                                    <bean:define id="idiomaIter" name="k" property="key"/>
                                                                    <% if ((("" + idiomaIter).toUpperCase()).equals("" + idiomaDefault)) { %>
                                                                        <img src="archivopub.do?ctrl=MCRST<bean:write name="MVS_idsite" />ZI<bean:write name="j" property="imagenmenu.id"/>&id=<bean:write name="j" property="imagenmenu.id"/>" alt="<bean:write name="k" property="value.nombre"/>"/>
                                                                    <% } %>
                                                                </logic:iterate>

                                                            </logic:present>

                                                            <logic:iterate id="k" name="j" property="traducciones">
                                                                <bean:define id="idiomaIter" name="k" property="key"/>
                                                                <% if ((("" + idiomaIter).toUpperCase()).equals("" + idiomaDefault)) { %>
                                                                    <a href="contenido.do?mkey=<bean:write name="MVS_microsite" property="claveunica"/>&amp;lang=<bean:write name="MVS_idioma"/>&amp;cont=<bean:write name="j" property="listacosas[0].id"/>" target="<%=((""+urlExterna).equals("true"))?"_blank":"_self"%>"><bean:write name="k" property="value.nombre"/></a>
                                                                <% } %>
                                                            </logic:iterate>

                                                        </li>
                                                    </logic:equal>
                                                <% } %>
                                            </logic:iterate>
                                        </ul>
                                    </li>
                                </logic:present>
                            </logic:iterate>
                        </ul>
                    </logic:equal>

                </logic:present>
