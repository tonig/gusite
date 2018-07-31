package es.caib.gusite.plugins.rolsac;

import java.util.ArrayList;
import java.util.List;

import es.caib.rolsac.api.v2.arxiu.ArxiuDTO;
import es.caib.rolsac.api.v2.edifici.EdificiCriteria;
import es.caib.rolsac.api.v2.edifici.EdificiDTO;
import es.caib.rolsac.api.v2.espaiTerritorial.EspaiTerritorialDTO;
import es.caib.rolsac.api.v2.exception.StrategyException;
import es.caib.rolsac.api.v2.fitxa.FitxaCriteria;
import es.caib.rolsac.api.v2.fitxa.FitxaDTO;
import es.caib.rolsac.api.v2.fitxaUA.FitxaUACriteria;
import es.caib.rolsac.api.v2.materia.MateriaCriteria;
import es.caib.rolsac.api.v2.materia.MateriaDTO;
import es.caib.rolsac.api.v2.normativa.NormativaCriteria;
import es.caib.rolsac.api.v2.normativa.NormativaDTO;
import es.caib.rolsac.api.v2.personal.PersonalCriteria;
import es.caib.rolsac.api.v2.personal.PersonalDTO;
import es.caib.rolsac.api.v2.procediment.ProcedimentCriteria;
import es.caib.rolsac.api.v2.procediment.ProcedimentDTO;
import es.caib.rolsac.api.v2.seccio.SeccioCriteria;
import es.caib.rolsac.api.v2.seccio.SeccioDTO;
import es.caib.rolsac.api.v2.servicio.ServicioCriteria;
import es.caib.rolsac.api.v2.servicio.ServicioDTO;
import es.caib.rolsac.api.v2.tractament.TractamentCriteria;
import es.caib.rolsac.api.v2.tractament.TractamentDTO;
import es.caib.rolsac.api.v2.tramit.TramitCriteria;
import es.caib.rolsac.api.v2.tramit.TramitDTO;
import es.caib.rolsac.api.v2.unitatAdministrativa.UnitatAdministrativaCriteria;
import es.caib.rolsac.api.v2.unitatAdministrativa.UnitatAdministrativaDTO;
import es.caib.rolsac.api.v2.unitatAdministrativa.UnitatAdministrativaQueryServiceStrategy;
import es.caib.rolsac.api.v2.usuari.UsuariCriteria;
import es.caib.rolsac.api.v2.usuari.UsuariDTO;

public class MiUnitatAdministrativaQueryServiceStrategy implements UnitatAdministrativaQueryServiceStrategy {

	public List<ServicioDTO> llistarServicios(long arg0, ServicioCriteria arg1) {
		return new ArrayList<>();
	}
	
	public void setUrl(String arg0) {
		return;
	}
	
	@Override
	public int getNumEdificis(Long arg0) throws StrategyException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getNumFilles(Long arg0) throws StrategyException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getNumFitxes(Long arg0) throws StrategyException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getNumMateries(Long arg0) throws StrategyException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getNumNormatives(Long arg0) throws StrategyException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getNumPersonal(Long arg0) throws StrategyException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getNumProcediments(Long arg0) throws StrategyException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getNumSeccions(Long arg0) throws StrategyException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getNumTramits(Long arg0) throws StrategyException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getNumUsuaris(Long arg0) throws StrategyException {
		// TODO Auto-generated method stub
		return 0;
	}
	
	public int getNumServicios(Long arg0) throws StrategyException {
		return 0;
	}

	@Override
	public List<Long> llistarDescendents(long arg0) throws StrategyException {
		// TODO Auto-generated method stub
		return new ArrayList<>();
	}

	@Override
	public List<EdificiDTO> llistarEdificis(long arg0, EdificiCriteria arg1) throws StrategyException {
		// TODO Auto-generated method stub
		return new ArrayList<>();
	}

	@Override
	public List<UnitatAdministrativaDTO> llistarFilles(long arg0, UnitatAdministrativaCriteria arg1)
			throws StrategyException {
		// TODO Auto-generated method stub
		return new ArrayList<>();
	}

	@Override
	public List<FitxaDTO> llistarFitxes(long arg0, FitxaCriteria arg1, FitxaUACriteria arg2) throws StrategyException {
		// TODO Auto-generated method stub
		return new ArrayList<>();
	}

	@Override
	public List<MateriaDTO> llistarMateries(long arg0, MateriaCriteria arg1) throws StrategyException {
		// TODO Auto-generated method stub
		return new ArrayList<>();
	}

	@Override
	public List<NormativaDTO> llistarNormatives(long arg0, NormativaCriteria arg1) throws StrategyException {
		// TODO Auto-generated method stub
		return new ArrayList<>();
	}

	@Override
	public List<PersonalDTO> llistarPersonal(long arg0, PersonalCriteria arg1) throws StrategyException {
		// TODO Auto-generated method stub
		return new ArrayList<>();
	}

	@Override
	public List<ProcedimentDTO> llistarProcediments(long arg0, ProcedimentCriteria arg1) throws StrategyException {
		// TODO Auto-generated method stub
		return new ArrayList<>();
	}

	@Override
	public List<SeccioDTO> llistarSeccions(long arg0, SeccioCriteria arg1) throws StrategyException {
		// TODO Auto-generated method stub
		return new ArrayList<>();
	}

	@Override
	public List<TramitDTO> llistarTramits(long arg0, TramitCriteria arg1) throws StrategyException {
		// TODO Auto-generated method stub
		return new ArrayList<>();
	}

	@Override
	public List<UsuariDTO> llistarUsuaris(long arg0, UsuariCriteria arg1) throws StrategyException {
		// TODO Auto-generated method stub
		return new ArrayList<>();
	}

	@Override
	public EspaiTerritorialDTO obtenirEspaiTerritorial(long arg0) throws StrategyException {
		// TODO Auto-generated method stub
		EspaiTerritorialDTO etDto = new EspaiTerritorialDTO();
		etDto.setCoordenadas("37.2056126,-3.6240871,15.92z");
		etDto.setId(1L);
		etDto.setLogo(1L);
		etDto.setMapa(1L);
		etDto.setNivel(1);
		etDto.setNombre("ddorado");
		etDto.setPadre(1L);
		return etDto;
	}

	@Override
	public ArxiuDTO obtenirFotog(Long arg0) throws StrategyException {
		// TODO Auto-generated method stub
		return new ArxiuDTO();
	}

	@Override
	public ArxiuDTO obtenirFotop(Long arg0) throws StrategyException {
		// TODO Auto-generated method stub
		return new ArxiuDTO();
	}

	@Override
	public ArxiuDTO obtenirLogoh(Long arg0) throws StrategyException {
		// TODO Auto-generated method stub
		return new ArxiuDTO();
	}

	@Override
	public ArxiuDTO obtenirLogos(Long arg0) throws StrategyException {
		// TODO Auto-generated method stub
		return new ArxiuDTO();
	}

	@Override
	public ArxiuDTO obtenirLogot(Long arg0) throws StrategyException {
		// TODO Auto-generated method stub
		return new ArxiuDTO();
	}

	@Override
	public ArxiuDTO obtenirLogov(Long arg0) throws StrategyException {
		// TODO Auto-generated method stub
		return new ArxiuDTO();
	}

	@Override
	public UnitatAdministrativaDTO obtenirPare(long arg0, String arg1) throws StrategyException {
		// TODO Auto-generated method stub
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
		return uaDto;
	}

	@Override
	public TractamentDTO obtenirTractament(long arg0, TractamentCriteria arg1) throws StrategyException {
		// TODO Auto-generated method stub
		TractamentDTO tDto = new TractamentDTO();
		tDto.setCargoF("ddorado");
		tDto.setCargoM("ddorado");
		tDto.setCodigoEstandar("1");
		tDto.setId(1L);
		tDto.setTipo("1");
		tDto.setTratamientoF("Señora");
		tDto.setTratamientoM("Señor");
		return tDto;
	}

}
