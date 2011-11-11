package autobizlogic.demo.buslogicdemospring;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate3.HibernateSystemException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;

/**
 * I apologize in advance to all Spring purists -- I fully recognize that this is not very Springy.
 */

@Controller
@SessionAttributes({"showEvents"})
public class DemoController {
	
	private DemoService demoService;

	@Autowired
	public void setCustomerService(DemoService serv) {
		demoService = serv;
	}

	/**
	 * Invoke the service to do the actual work. The handleRequestDeclarative method is marked
	 * as transactional, which means that any constraint failures should happen when the method
	 * returns. We catch the exception here, and display it as a friendly message.
	 */
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String show(
			@RequestParam(required=false) String name,
			@RequestParam(required=false) String todo,
			WebRequest request,
			Model model) {

		try {
			demoService.handleRequestDeclarative(name, request, model);
		}
		catch(HibernateSystemException ex) {
			model.addAttribute("errorMessage", ex.getCause().getMessage());
		}
		model.addAttribute("txMode", "declarative");
		model.addAttribute("controllerName", "");
		
		return "buslogicdemospring";
	}

	/**
	 * Do the same thing, but this time use the service that does manual transactions,
	 * rather than declarative transactions.
	 */
	@RequestMapping(value = "/manual", method = RequestMethod.GET)
	public String show2(
			@RequestParam(required=false) String name,
			@RequestParam(required=false) String todo,
			WebRequest request,
			Model model) {
		
		demoService.handleRequestManual(name, request, model);
		model.addAttribute("txMode", "manual");
		model.addAttribute("controllerName", "manual");
		
		return "buslogicdemospring";
	}
}
