package autobizlogic.demo.buslogicdemospring;

import javax.inject.Inject;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;

import autobizlogic.demo.buslogicdemospring.DemoService.Action;
import autobizlogic.demo.buslogicdemospring.DemoService.Attribute;
import autobizlogic.demo.buslogicdemospring.DemoService.DataType;

@Controller
@SessionAttributes("showEvents")
public class DemoController {

    @Inject
    private DemoService demoService;

    /**
     * Invoke the service to do the actual work. The handleRequestDeclarative method is marked as transactional, which means that any constraint failures should
     * happen when the method returns. We catch the exception here, and display it as a friendly message.
     */
    @RequestMapping(value = "/")
    public String declarative(@RequestParam(required = false) String customerName, @RequestParam(required = false) Action action,
            @RequestParam(required = false) DataType dataType, @RequestParam(required = false) Attribute attribute,
            @RequestParam(required = false) String value, @RequestParam(required = false) String id, Model model) {

        demoService.handleRequestDeclarative(customerName, model, action, dataType, attribute, value, id);
        model.addAttribute("txMode", "declarative");
        model.addAttribute("controllerName", "");

        return "buslogicdemospring";
    }

    /**
     * Do the same thing, but this time use the service that does manual transactions, rather than declarative transactions.
     */
    @RequestMapping(value = "/manual")
    public String manual(@RequestParam(required = false) String customerName, @RequestParam(required = false) Action action,
            @RequestParam(required = false) DataType dataType, @RequestParam(required = false) Attribute attribute,
            @RequestParam(required = false) String value, @RequestParam(required = false) String id, Model model) {

        demoService.handleRequestManual(customerName, model, action, dataType, attribute, value, id);
        model.addAttribute("txMode", "manual");
        model.addAttribute("controllerName", "manual");

        return "buslogicdemospring";
    }

    @ExceptionHandler(Exception.class)
    public String errorCaught(Model model, Exception e) {
        e.printStackTrace();
        model.addAttribute("errorMessage", e.getCause().getMessage());
        return "error";
    }
}
