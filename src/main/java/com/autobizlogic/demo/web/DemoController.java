package com.autobizlogic.demo.web;

import javax.inject.Inject;

import org.springframework.orm.hibernate3.HibernateSystemException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

import com.autobizlogic.abl.businesslogicengine.ConstraintException;
import com.autobizlogic.demo.service.DemoService;
import com.autobizlogic.demo.service.DemoService.Action;
import com.autobizlogic.demo.service.DemoService.Attribute;
import com.autobizlogic.demo.service.DemoService.DataType;

@Controller
@SessionAttributes("showEvents")
public class DemoController {

    @Inject
    private DemoService demoService;

    @RequestMapping(value = "/")
    public String index() {
        return "redirect:/declarative";
    }

    /**
     * Invoke the service to do the actual work. The handleRequestDeclarative method is marked as transactional, which means that any constraint failures should
     * happen when the method returns.
     * <p>
     * We catch the exception here, and display it as a friendly message.
     */
    @RequestMapping(value = "/declarative")
    public String declarativeTransaction(@RequestParam(required = false) String customerName, @RequestParam(required = false) Action action,
            @RequestParam(required = false) DataType dataType, @RequestParam(required = false) Attribute attribute,
            @RequestParam(required = false) String value, @RequestParam(required = false) String id, Model model) {
        try {
            demoService.handleRequestDeclarative(customerName, model, action, dataType, attribute, value, id);
        } catch (HibernateSystemException ex) {
            extractErrors(model, ex);
        }
        model.addAttribute("txMode", "declarative");
        model.addAttribute("controllerName", "declarative");
        return "index";
    }

    /**
     * Do the same thing, but this time use the service that does manual transactions, rather than declarative transactions.
     */
    @RequestMapping(value = "/manual")
    public String manualTransaction(@RequestParam(required = false) String customerName, @RequestParam(required = false) Action action,
            @RequestParam(required = false) DataType dataType, @RequestParam(required = false) Attribute attribute,
            @RequestParam(required = false) String value, @RequestParam(required = false) String id, Model model) {
        try {
            demoService.handleRequestManual(customerName, model, action, dataType, attribute, value, id);
        } catch (HibernateSystemException ex) {
            extractErrors(model, ex);
        }
        model.addAttribute("txMode", "manual");
        model.addAttribute("controllerName", "manual");
        return "index";
    }

    @ExceptionHandler(Throwable.class)
    public ModelAndView unknownErrorCaught(Exception e) {
        ModelAndView model = new ModelAndView("error");
        model.addObject("exception", e);
        model.addObject("errorMessage", e.getCause() != null ? e.getCause().getMessage() : e.getMessage());
        return model;
    }

    private void extractErrors(Model model, Throwable ex) {
        Throwable cause = ex.getCause();
        if (cause != null && (cause instanceof ConstraintException)) {
            ConstraintException cex = (ConstraintException) cause;
            model.addAttribute("errorMessage", cex.getMessage());
            model.addAttribute("constraintFailures", cex.getConstraintFailures());
        } else {
            model.addAttribute("errorMessage", ex.getMessage());
        }
    }
}
