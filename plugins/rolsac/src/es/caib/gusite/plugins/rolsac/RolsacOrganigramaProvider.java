package es.caib.gusite.plugins.rolsac;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;

import es.caib.gusite.plugins.PluginException;
import es.caib.gusite.plugins.organigrama.OrganigramaProvider;
import es.caib.gusite.plugins.organigrama.UnidadData;
import es.caib.gusite.plugins.organigrama.UnidadListData;
import es.caib.rolsac.api.v2.edifici.EdificiCriteria;
import es.caib.rolsac.api.v2.edifici.EdificiDTO;
import es.caib.rolsac.api.v2.edifici.EdificiQueryServiceAdapter;
import es.caib.rolsac.api.v2.exception.QueryServiceException;
import es.caib.rolsac.api.v2.idioma.IdiomaCriteria;
import es.caib.rolsac.api.v2.idioma.IdiomaQueryServiceAdapter;
import es.caib.rolsac.api.v2.rolsac.RolsacQueryService;
import es.caib.rolsac.api.v2.unitatAdministrativa.UnitatAdministrativaCriteria;
import es.caib.rolsac.api.v2.unitatAdministrativa.UnitatAdministrativaDTO;
import es.caib.rolsac.api.v2.unitatAdministrativa.UnitatAdministrativaOrdenacio;
import es.caib.rolsac.api.v2.unitatAdministrativa.UnitatAdministrativaQueryServiceAdapter;

@Service("OrganigramaProvider")
public class RolsacOrganigramaProvider implements OrganigramaProvider {

	private static Log log = LogFactory.getLog(RolsacOrganigramaProvider.class);
	static public final String UO_PORTAVOZ="270677";  // caib: 270677 nuestra: 51079
	private static final String DEFAULT_IDIOMA = "ca";
	
	@Override 
	public Collection<UnidadListData> getUnidades (String lang) throws PluginException {
		try {
			RolsacQueryService rqs = APIUtil.getRolsacQueryService();
			IdiomaCriteria idiomaCriteria =  new IdiomaCriteria();
			idiomaCriteria.setIdioma(lang);
			idiomaCriteria.setCodigoEstandar(lang);
			List<IdiomaQueryServiceAdapter> idiomas = new ArrayList<>();
			if (!APIUtil.isMock()) {
				idiomas = rqs.llistarIdiomes(idiomaCriteria);
			}
			if (idiomas.size() < 1) {
				lang = DEFAULT_IDIOMA;
				//throw new PluginException("Idioma " + lang + " no soportado en ROLSAC");
			}

			UnitatAdministrativaCriteria uaCriteria = new UnitatAdministrativaCriteria();
			uaCriteria.setOrdenar(new UnitatAdministrativaOrdenacio[] {UnitatAdministrativaOrdenacio.orden_asc});
			uaCriteria.setIdioma(lang);
			List<UnitatAdministrativaQueryServiceAdapter> listaUAs = new ArrayList<>();

			UnitatAdministrativaQueryServiceAdapter uaqsa = null;
			if (APIUtil.isMock()) {
				uaqsa = getMockUnitatAdministrativaQueryServiceAdapter();
				listaUAs.add(uaqsa);
			} else {
				listaUAs = rqs.llistarUnitatsAdministratives(uaCriteria);
			}
			// Comentado para usar Mocks
//			List<UnitatAdministrativaQueryServiceAdapter> listaUAs = rqs.llistarUnitatsAdministratives(uaCriteria);

			Collection<UnidadListData> nuevasUAs = new ArrayList<UnidadListData>();

			// La conselleria de Portavoz se trata a parte.
			for (UnitatAdministrativaQueryServiceAdapter conse : listaUAs) {
				conse.setIdioma(lang);
				nuevasUAs.add( transform2UnidadListData(conse) );
			}
			return nuevasUAs;

		} catch (QueryServiceException e) {
			log.error("No se han podido obtener las unidades principales ROLSAC", e);
			throw new PluginException("No se han podido obtener las unidades principales ROLSAC", e);
		}
	}
	
	@Override 
	public Collection<UnidadListData> getUnidadesPrincipales(String lang) throws PluginException {

		try {
			RolsacQueryService rqs = APIUtil.getRolsacQueryService();
			IdiomaCriteria idiomaCriteria =  new IdiomaCriteria();
			idiomaCriteria.setIdioma(lang);
			idiomaCriteria.setCodigoEstandar(lang);
			List<IdiomaQueryServiceAdapter> idiomas = new ArrayList<>();// rqs.llistarIdiomes(idiomaCriteria);
			if (!APIUtil.isMock()) {
				idiomas = rqs.llistarIdiomes(idiomaCriteria);
			}
			if (idiomas.size() < 1) {
				lang = DEFAULT_IDIOMA;
				//throw new PluginException("Idioma " + lang + " no soportado en ROLSAC");
			}

			String idUOGovern = System.getProperty("es.caib.gusite.codigoUO.govern");
			if (idUOGovern == null || idUOGovern.equals("")) {
				throw new RuntimeException("No se estableció la propiedad de sistema es.caib.gusite.codigoUO.govern");
			}

			Long UO_GOVERN_IB = new Long(idUOGovern);
			UnitatAdministrativaCriteria uaCriteria = new UnitatAdministrativaCriteria();
			uaCriteria.setIdioma(lang);
			uaCriteria.setId(UO_GOVERN_IB.toString());

			UnitatAdministrativaQueryServiceAdapter ua = null;
			if (APIUtil.isMock()) {
				ua = getMockUnitatAdministrativaQueryServiceAdapter();
			} else {
				ua = rqs.obtenirUnitatAdministrativa(uaCriteria);
			}
			// Comentado para usar Mocks
			//UnitatAdministrativaQueryServiceAdapter ua = rqs.obtenirUnitatAdministrativa(uaCriteria);
			
			if (ua == null) {
				throw new RuntimeException("No se ha encontrado la uo govern, la propiedad de sistema es.caib.gusite.codigoUO.govern es:" + idUOGovern);
			}
			
			UnitatAdministrativaCriteria uaCriteriaFilles = new UnitatAdministrativaCriteria();
			uaCriteriaFilles.setIdioma(lang);
			uaCriteriaFilles.setOrdenar(new UnitatAdministrativaOrdenacio[] {UnitatAdministrativaOrdenacio.orden_asc});
			
			List<UnitatAdministrativaQueryServiceAdapter> listaUAs = ua.llistarFilles(uaCriteriaFilles);

			Collection<UnidadListData> nuevasUAs = new ArrayList<UnidadListData>();

			// La conselleria de Portavoz se trata a parte.
			for (UnitatAdministrativaQueryServiceAdapter conse : listaUAs) {
				if (!conse.getId().toString().equals(UO_PORTAVOZ)) {
					conse.setIdioma(lang);
					nuevasUAs.add( transform2UnidadListData(conse) );
				}
			}
			return nuevasUAs;


		} catch (QueryServiceException e) {
			log.error("No se han podido obtener las unidades principales ROLSAC", e);
			throw new PluginException("No se han podido obtener las unidades principales ROLSAC", e);
		}
		
	}


	@Override 
	public Collection<UnidadListData> getUnidadesHijas(Serializable unidadId, String lang) throws PluginException {

		try {
			RolsacQueryService rqs = APIUtil.getRolsacQueryService();
			IdiomaCriteria idiomaCriteria =  new IdiomaCriteria();
			idiomaCriteria.setIdioma(lang);
			idiomaCriteria.setCodigoEstandar(lang);
			List<IdiomaQueryServiceAdapter> idiomas = new ArrayList<>();// rqs.llistarIdiomes(idiomaCriteria);
			if (!APIUtil.isMock()) {
				idiomas = rqs.llistarIdiomes(idiomaCriteria);
			}
			if (idiomas.size() < 1) {
				lang = DEFAULT_IDIOMA;
				//throw new PluginException("Idioma " + lang + " no soportado en ROLSAC");
			}

			UnitatAdministrativaCriteria uaCriteria = new UnitatAdministrativaCriteria();
			uaCriteria.setIdioma(lang);
			uaCriteria.setId(unidadId.toString());

			UnitatAdministrativaQueryServiceAdapter ua = null;
			if (APIUtil.isMock()) {
				ua = getMockUnitatAdministrativaQueryServiceAdapter();
			} else {
				ua = rqs.obtenirUnitatAdministrativa(uaCriteria);
			}
			
			// Comentado para usar Mocks
//			UnitatAdministrativaQueryServiceAdapter ua = rqs.obtenirUnitatAdministrativa(uaCriteria);
			UnitatAdministrativaCriteria uaCriteriaFilles = new UnitatAdministrativaCriteria();
			uaCriteriaFilles.setIdioma(lang);
			uaCriteriaFilles.setOrdenar(new UnitatAdministrativaOrdenacio[] {UnitatAdministrativaOrdenacio.orden_asc});
			List<UnitatAdministrativaQueryServiceAdapter> listaUAs = ua.llistarFilles(uaCriteriaFilles);

			Collection<UnidadListData> nuevasUAs = new ArrayList<UnidadListData>();

			for (UnitatAdministrativaQueryServiceAdapter conse : listaUAs) {
				conse.setIdioma(lang);
				nuevasUAs.add( transform2UnidadListData(conse) );
			}
			return nuevasUAs;


		} catch (QueryServiceException e) {
			log.error("No se han podido obtener las unidades principales ROLSAC", e);
			throw new PluginException("No se han podido obtener las unidades principales ROLSAC", e);
		}
		
	}

	
	@Override
	public UnidadData getUnidadData(Serializable unidadId, String lang) throws PluginException {


		try {
			// DIRECCION DEL PIE DE PAGINA
	
			RolsacQueryService rqs = APIUtil.getRolsacQueryService();
	
			// Obtener UA.
			UnitatAdministrativaCriteria uaCriteria = new UnitatAdministrativaCriteria();
			Long coduo = Long.valueOf(String.valueOf(unidadId));
			uaCriteria.setId(String.valueOf(coduo));
			uaCriteria.setIdioma(lang);

			UnitatAdministrativaQueryServiceAdapter ua = null;
			if (APIUtil.isMock()) {
				ua = getMockUnitatAdministrativaQueryServiceAdapter();
			} else {
				ua = rqs.obtenirUnitatAdministrativa(uaCriteria);
			}
			// Comentado para usar Mocks
//			UnitatAdministrativaQueryServiceAdapter ua;
//			ua = rqs.obtenirUnitatAdministrativa(uaCriteria);

			if (ua == null) {
				//No se encuentra la unidad administrativa
				return null;
			}

			ua.setIdioma(lang);
			return transform2UnidadData(ua);
	
		} catch (QueryServiceException e) {
			log.error(e.getMessage(), e);
			throw new PluginException(e.getMessage(), e);
			
		}
	}

	
	private UnidadData transform2UnidadData(UnitatAdministrativaQueryServiceAdapter ua) throws PluginException, QueryServiceException {

		UnidadData unidadData = new UnidadData();
		this.transform2UnidadListData(ua, unidadData);
		
		// Obtener edificios asociados y construir dirección.
		EdificiCriteria edificiCriteria = new EdificiCriteria();
		List<EdificiQueryServiceAdapter> edificios = ua.llistarEdificis(edificiCriteria);

		//TODO: mover la propiedad al espacio del plugin
		String absUrl = System.getProperty("es.caib.gusite.portal.url");

		EdificiDTO edificio = null;
		if (edificios.size() > 0) {
			edificio = edificios.get(0);
			unidadData.setUrlPlano(absUrl + "/govern/organigrama/planol.do?coduo=" + ua.getId() + "&lang=" + ua.getIdioma());
			unidadData.setDireccion(edificio.getDireccion());
			unidadData.setCodigoPostal(edificio.getCodigoPostal());
			unidadData.setPoblacion(edificio.getPoblacion());
		}

		// Preferencia del (tel, fax) de la Unidad Organica sobre el del
		// edificio
		if (ua.getTelefono() != null && ua.getTelefono().length() > 0) {
			unidadData.setTelefono(ua.getTelefono());
		} else if (edificio != null && edificio.getTelefono() != null && edificio.getTelefono().length() > 0) {
			unidadData.setTelefono(edificio.getTelefono());
		}

		if (ua.getFax() != null && ua.getFax().length() > 0) {
			unidadData.setFax(ua.getFax());
		} else if (edificio!= null && edificio.getFax() != null && edificio.getFax().length() > 0) {
			unidadData.setFax(edificio.getFax());
		}
		return unidadData;
	} 
	
	private UnidadListData transform2UnidadListData(UnitatAdministrativaQueryServiceAdapter ua) throws PluginException, QueryServiceException {
		UnidadListData unidadData = new UnidadData();
		return this.transform2UnidadListData(ua, unidadData);
	}

	private UnidadListData transform2UnidadListData(UnitatAdministrativaQueryServiceAdapter ua, UnidadListData unidadData) throws PluginException, QueryServiceException {
		
		unidadData.setNombre(ua.getNombre());
		unidadData.setAbreviatura(ua.getAbreviatura());
		unidadData.setIdUnidad(ua.getId());
		unidadData.setValidacion(ua.getValidacion());
		String idUOGovern = System.getProperty("es.caib.gusite.codigoUO.govern");
		if (idUOGovern == null) {
			throw new RuntimeException("No se estableció la propiedad de sistema es.caib.gusite.codigoUO.govern");
		}
		
		if (ua.getPadre() != null && !ua.getId().toString().equals(idUOGovern)) { //La UO Govern de rolsac es la raíz
			unidadData.setIdUnidadPadre(ua.getPadre()); 
		}
		
		
		//TODO: mover la propiedad al espacio del plugin
		String absUrl = System.getProperty("es.caib.gusite.portal.url");

		unidadData.setUrl(absUrl + "/govern/organigrama/area.do?coduo=" + ua.getId() + "&lang=" + ua.getIdioma());
		unidadData.setDominio(ua.getDominio());
		return unidadData;
	} 
	

	private UnitatAdministrativaQueryServiceAdapter getMockUnitatAdministrativaQueryServiceAdapter() throws QueryServiceException {
		UnitatAdministrativaDTO uaDto = new UnitatAdministrativaDTO();
		uaDto.setAbreviatura("ddorado");
		uaDto.setAdministracionRemota(1L);
		uaDto.setBusinessKey("1");
		uaDto.setClaveHita("1");
		uaDto.setCodigoEstandar("1");
		uaDto.setCvResponsable("ddorado");
		uaDto.setDominio("localhost");
		uaDto.setEmail("ddorado@at4.net");
		uaDto.setEspacioTerrit(1L);
		uaDto.setFax("1");
		uaDto.setFotog(1L);
		uaDto.setFotop(1L);
		uaDto.setId(1L);
		uaDto.setIdExterno(1L);
		uaDto.setIdioma("ca");
		uaDto.setLogoh(1L);
		uaDto.setLogos(1L);
		uaDto.setLogot(1L);
		uaDto.setLogov(1L);
		uaDto.setNombre("ddorado");
		uaDto.setNumfoto1(1);
		uaDto.setNumfoto2(1);
		uaDto.setNumfoto3(1);
		uaDto.setNumfoto4(1);
		uaDto.setOrden(1L);
		uaDto.setPadre(1L);
		uaDto.setPresentacion("ddorado");
		uaDto.setResponsable("ddorado");
		uaDto.setResponsableEmail("ddorado@at4.net");
		uaDto.setSexoResponsable(1);
		uaDto.setTelefono("1");
		uaDto.setTratamiento(1L);
		uaDto.setUrl("http://localhost:8080");
		uaDto.setUrlRemota("http://localhost:8080");
		uaDto.setValidacion(1);

		UnitatAdministrativaQueryServiceAdapter ua = new UnitatAdministrativaQueryServiceAdapter(uaDto);
		ua.setUnitatAdministrativaQueryServiceStrategy(new MiUnitatAdministrativaQueryServiceStrategy());
		return ua;
	}

}
