package com.autobizlogic.demo.service;

import static java.lang.Boolean.FALSE;
import static java.math.BigDecimal.ZERO;

import java.math.BigDecimal;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.ui.Model;

import com.autobizlogic.demo.service.data.Customer;
import com.autobizlogic.demo.service.data.Lineitem;
import com.autobizlogic.demo.service.data.Product;
import com.autobizlogic.demo.service.data.PurchaseOrder;

@Service
public class DemoService {

    @PersistenceContext
    private EntityManager em;

    @Transactional
    public void handleRequestDeclarative(String customerName, Model model, Action action, DataType dataType, Attribute attribute, String value, String id) {
        handleRequest(customerName, model, action, dataType, attribute, value, id);
    }

    /**
     * Actually process the request without regard for transactions. This gets called by two different methods, each doing transactions in its own way.
     */
    private void handleRequest(String customerName, Model model, Action action, DataType dataType, Attribute attribute, String value, String id) {
        // Put all the customers and orders in the model for the selects.
        // Clearly this could be optimized with some caching.
        model.addAttribute("customers", em.createQuery("from Customer order by name").getResultList());
        model.addAttribute("products", em.createQuery("from Product order by name").getResultList());

        // Put the requested customer (or first customer if none requested) in the model
        Customer customer = getCustomer(customerName);
        model.addAttribute("customer", customer);

        // Is there anything else we need to do?
        if (action == null) {
            return; // nope
        }
        switch (action) {
        case update:
            doUpdate(model, customer, dataType, attribute, value, id);
            break;
        case create:
            doCreate(customer, dataType, id);
            break;
        case delete:
            doDelete(customer, dataType, id);
            break;
        case prefs:
            doPrefs(model, value);
            break;
        }
    }

    private Customer getCustomer(String customerName) {
        Customer customer;
        if (customerName != null && customerName.length() > 0) {
            customer = em.find(Customer.class, customerName);
        } else {
            customer = (Customer) em.createQuery("from Customer order by name").getResultList().get(0);
        }

        // Pre-read all objects into memory, so that the view will not get a LazyInitializationException
        // when navigating the objects.
        for (PurchaseOrder order : customer.getPurchaseOrders()) {
            for (Lineitem item : order.getLineitems()) {
                @SuppressWarnings("unused")
                Product product = item.getProduct();
            }
        }
        return customer;
    }

    public enum Action {
        update, create, delete, prefs;
    }

    public enum DataType {
        customer, order, lineitem;
    }

    public enum Attribute {
        creditLimit, isPreferred, paid, notes, customer, qtyOrdered, product
    }

    /**
     * Process an insert, update or delete request.
     */
    private void doUpdate(Model model, Customer customer, DataType dataType, Attribute attribute, String value, String id) {
        switch (dataType) {
        case customer:
            updateCustomer(customer, attribute, value);
            break;
        case order:
            updateOrder(model, customer, attribute, value, id);
            break;
        case lineitem:
            updateLineItem(attribute, value, id);
            break;
        }
    }

    private void updateLineItem(Attribute attribute, String value, String id) {
        Lineitem item = em.find(Lineitem.class, new Long(id));
        switch (attribute) {
        case qtyOrdered:
            item.setQtyOrdered(new Integer(value));
            break;
        case product:
            item.setProduct(em.find(Product.class, new Long(value)));
            break;
        }
    }

    private void updateOrder(Model model, Customer customer, Attribute attribute, String value, String id) {
        PurchaseOrder order = em.find(PurchaseOrder.class, new Long(id));
        switch (attribute) {
        case paid:
            order.setPaid(!order.getPaid());
            break;
        case notes:
            order.setNotes(value);
            break;
        case customer:
            order.setCustomer(em.find(Customer.class, value));
            customer.getPurchaseOrders().remove(order); // Fix this by hand so the objects will reflect reality
            model.addAttribute("fyi", "Order " + order.getOrderNumber() + " has been reassigned to customer " + value);
            break;
        }
    }

    private void updateCustomer(Customer customer, Attribute attribute, String value) {
        switch (attribute) {
        case creditLimit:
            customer.setCreditLimit(new BigDecimal(value));
            break;
        case isPreferred:
            customer.setPreferred(!customer.isPreferred());
            break;
        }
    }

    private void doCreate(Customer customer, DataType dataType, String id) {
        switch (dataType) {
        case order:
            createOrder(customer);
            break;
        case lineitem:
            createLineItem(id);
            break;
        }
    }

    private void createLineItem(String id) {
        Lineitem item = new Lineitem();
        PurchaseOrder order = em.find(PurchaseOrder.class, new Long(id));
        order.getLineitems().add(0, item); // Fix this by hand so the objects will reflect reality
        Product product = em.find(Product.class, 1L);
        item.setPurchaseOrder(order);
        item.setProduct(product);
        item.setQtyOrdered(1);
        item.setAmount(ZERO);
        item.setProductPrice(ZERO);
        em.persist(item);
    }

    private void createOrder(Customer customer) {
        PurchaseOrder newOrder = new PurchaseOrder();
        newOrder.setCustomer(customer);
        customer.getPurchaseOrders().add(0, newOrder); // Fix this by hand so the objects will reflect reality
        newOrder.setPaid(FALSE);
        newOrder.setAmountTotal(ZERO);
        newOrder.setNotes("");
        em.persist(newOrder);
    }

    private void doDelete(Customer customer, DataType dataType, String id) {
        switch (dataType) {
        case order:
            PurchaseOrder order = em.find(PurchaseOrder.class, new Long(id));
            order.getCustomer().getPurchaseOrders().remove(order); // Fix this by hand so the objects will reflect reality
            em.remove(order);
            break;
        case lineitem:
            Lineitem item = em.find(Lineitem.class, new Long(id));
            item.getPurchaseOrder().getLineitems().remove(item); // Fix this by hand so the objects will reflect reality
            em.remove(item);
            break;
        }
    }

    private void doPrefs(Model model, String value) {
        if (value == null || "false".equals(value)) {
            model.addAttribute("showEvents", false);
        } else {
            model.addAttribute("showEvents", true);
            if (DemoEventListener.getInstance() != null) {
                DemoEventListener.getInstance().resetEvents();
            }
        }
    }

    // ////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // ////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Declaring your service method as transactional is the easiest way to do it, but it is by no means the
    // only way. In particular, it has the disadvantage that you cannot (easily) catch any constraint failures: you have
    // to let Spring catch them.
    // This method shows another way, in which you handle the commit yourself, which allows you to catch any
    // constraint failures.

    private TransactionTemplate transactionTemplate;

    @Autowired
    public void setTransactionTemplate(PlatformTransactionManager transactionManager) {
        this.transactionTemplate = new TransactionTemplate(transactionManager);
    }

    // Note that this method is not marked as transactional
    public void handleRequestManual(final String customerName, final Model model, final Action action, final DataType dataType, final Attribute attribute,
            final String value, final String id) {
        try {
            transactionTemplate.execute(new TransactionCallbackWithoutResult() {
                @Override
                protected void doInTransactionWithoutResult(TransactionStatus status) {
                    handleRequest(customerName, model, action, dataType, attribute, value, id);
                }
            });
        } catch (Exception e) {
            // your exception logic here in the service
            // handle rollback, compensation, message whatever you need
            throw new RuntimeException(e);
        }
    }
}
