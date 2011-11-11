package autobizlogic.demo.buslogicdemospring;

import java.math.BigDecimal;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate3.HibernateSystemException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.ui.Model;
import org.springframework.web.context.request.WebRequest;

import com.autobizlogic.abl.businesslogicengine.ConstraintException;

import autobizlogic.demo.buslogicdemospring.data.*;

@Service("customerService")
public class DemoService {

	private EntityManager em;

	@PersistenceContext
	public void setEntityManager(EntityManager em) {
		this.em = em;
	}
	
	/**
	 * Spring purists will no doubt cringe at seeing a WebRequest passed to a service,
	 * and domain objects passed back to the controller.
	 * This was done for expediency. Feel free to do otherwise in your own code.
	 */
	@Transactional
	public void handleRequestDeclarative(String name, WebRequest request, Model model) {

		handleRequest(name, request, model);
	}
	
	/**
	 * Actually process the request without regard for transactions. This gets called by two different
	 * methods, each doing transactions in its own way.
	 */
	private void handleRequest(String name, WebRequest request, Model model) {
		
		// Put all the customers and orders in the model for the selects. Clearly this could be optimized
		// with some caching.
		model.addAttribute("customers", em.createQuery("from Customer order by name").getResultList());
		model.addAttribute("products", em.createQuery("from Product order by name").getResultList());

		// Put the requested customer (or first customer if none requested) in the model
		Customer customer;
		if (name != null && name.length() > 0)
			customer = em.find(Customer.class, name);
		else
			customer = (Customer)em.createQuery("from Customer order by name").getResultList().get(0);
		model.addAttribute("customer", customer);
		
		// Pre-read all objects into memory, so that the view will not get a LazyInitializationException
		// when navigating the objects.
		for (Purchaseorder order : customer.getPurchaseorders()) {
			for (Lineitem item : order.getLineitems()) {
				Product product = item.getProduct();
			}
		}
		
		// Is there anything else we need to do?
		if ("update".equals(request.getParameter("todo")))
			doUpdate(customer, request, model);
		else if ("create".equals(request.getParameter("todo")))
			doCreate(customer, request);
		else if ("delete".equals(request.getParameter("todo")))
			doDelete(customer, request);
		else if ("prefs".equals(request.getParameter("todo")))
			doPrefs(request, model);
	}
	
	/**
	 * Process an insert, update or delete request.
	 */
	private void doUpdate(Customer customer, WebRequest request, Model model) {
		
		String att = request.getParameter("att");
		String value = request.getParameter("value");
		String id = request.getParameter("id");
		if ("Customer".equals(request.getParameter("type"))) {
			if ("creditLimit".equals(att))
				customer.setCreditLimit(new BigDecimal(value));
			else if ("isPreferred".equals(att))
				customer.setPreferred(!customer.isPreferred());
		}
		else if ("Order".equals(request.getParameter("type"))) {
			Purchaseorder order = em.find(Purchaseorder.class, new Long(id));
			if ("paid".equals(att))
				order.setPaid(!order.getPaid());
			else if ("notes".equals(att))
				order.setNotes(value);
			else if ("customer".equals(att)) {
				order.setCustomer(em.find(Customer.class, value));
				customer.getPurchaseorders().remove(order); // Fix this by hand so the objects will reflect reality
				model.addAttribute("fyi", "Order " + order.getOrderNumber() + " has been reassigned to customer " + value);
			}
		}
		else if ("Lineitem".equals(request.getParameter("type"))) {
			Lineitem item = em.find(Lineitem.class, new Long(id));
			if ("qtyOrdered".equals(att))
				item.setQtyOrdered(new Integer(value));
			else if ("product".equals(att))
				item.setProduct(em.find(Product.class, new Long(value)));
		}
	}
	
	private void doCreate(Customer customer, WebRequest request) {
		if ("Order".equals(request.getParameter("type"))) {
			Purchaseorder newOrder = new Purchaseorder();
			newOrder.setCustomer(customer);
			customer.getPurchaseorders().add(0, newOrder); // Fix this by hand so the objects will reflect reality
			newOrder.setPaid(Boolean.FALSE);
			newOrder.setAmountTotal(BigDecimal.ZERO);
			newOrder.setNotes("");
			em.persist(newOrder);
		}
		else if ("Lineitem".equals(request.getParameter("type"))) {
			Lineitem item = new Lineitem();
			Purchaseorder order = em.find(Purchaseorder.class, new Long(request.getParameter("id")));
			order.getLineitems().add(0, item); // Fix this by hand so the objects will reflect reality
			Product product = em.find(Product.class, 1L);
			item.setPurchaseorder(order);
			item.setProduct(product);
			item.setQtyOrdered(1);
			item.setAmount(BigDecimal.ZERO);
			item.setProductPrice(BigDecimal.ZERO);
			em.persist(item);
		}
	}
	
	private void doDelete(Customer customer, WebRequest request) {
		String id = request.getParameter("id");
		if ("Order".equals(request.getParameter("type"))) {
			Purchaseorder order = em.find(Purchaseorder.class, new Long(id));
			order.getCustomer().getPurchaseorders().remove(order); // Fix this by hand so the objects will reflect reality
			em.remove(order);
		}
		else if ("Lineitem".equals(request.getParameter("type"))) {
			Lineitem item = em.find(Lineitem.class, new Long(id));
			item.getPurchaseorder().getLineitems().remove(item); // Fix this by hand so the objects will reflect reality
			em.remove(item);
		}
	}
	
	private void doPrefs(WebRequest request, Model model) {
		
		String type = request.getParameter("type");
		if ("showEvents".equals(type)) {
			String value = request.getParameter("value");
			if (value == null || "false".equals(value))
				model.addAttribute("showEvents", false);
			else {
				model.addAttribute("showEvents", true);
				if (DemoEventListener.getInstance() != null)
					DemoEventListener.getInstance().resetEvents();
			}
		}
	}
	
	//////////////////////////////////////////////////////////////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// Declaring your service method as transactional is the easiest way to do it, but it is by no means the
	// only way. In particular, it has the disadvantage that you cannot (easily) catch any constraint failures: you have
	// to let Spring catch them.
	// This method shows another way, in which you handle the commit yourself, which allows you to catch any
	// constraint failures.
	
	private TransactionTemplate transactionTemplate = null;
	
	@Autowired
	public void setTransactionTemplate(PlatformTransactionManager transactionManager) {
		this.transactionTemplate = new TransactionTemplate(transactionManager);
	}

	// Note that this method is not marked as transactional
	public void handleRequestManual(final String name, final WebRequest request, final Model model) {

		try {
			transactionTemplate.execute(new TransactionCallbackWithoutResult() {
		    	protected void doInTransactionWithoutResult(TransactionStatus status) {
	
		    		handleRequest(name, request, model);
		    	}
			});
		}
		catch(HibernateSystemException ex) { 
			
			Throwable cause = ex.getCause();
			if (cause != null && (cause instanceof ConstraintException)) {
				ConstraintException cex = (ConstraintException)cause;
				model.addAttribute("errorMessage", cex.getMessage());
				model.addAttribute("constraintFailures", cex.getConstraintFailures());
			}
			else
				model.addAttribute("errorMessage", ex.getMessage());
		}
	}
}
