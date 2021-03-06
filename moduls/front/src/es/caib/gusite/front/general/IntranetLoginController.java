package es.caib.gusite.front.general;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author brujula-at4
 */
@Controller
public class IntranetLoginController extends BaseViewController {

	@RequestMapping("intranetLogin")
	public String login(HttpServletRequest request) {

		String redirect = (String) request.getSession().getAttribute("redirect");
		request.getSession().removeAttribute("redirect");
		return "redirect:" + redirect;
	}

	@Override
	public String setServicio() {
		return Microfront.RMICROSITE;
	}
}
