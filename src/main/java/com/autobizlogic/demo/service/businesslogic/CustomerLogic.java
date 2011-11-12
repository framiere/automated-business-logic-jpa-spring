package com.autobizlogic.demo.service.businesslogic;

import static com.autobizlogic.abl.businesslogicengine.ConstraintFailure.failConstraint;

import java.math.BigDecimal;


import com.autobizlogic.abl.businesslogic.annotations.Constraint;
import com.autobizlogic.abl.businesslogic.annotations.CurrentBean;
import com.autobizlogic.abl.businesslogic.annotations.Sum;
import com.autobizlogic.demo.service.model.Customer;

public class CustomerLogic {

    @CurrentBean
    public Customer customer;

    @Constraint
    public void constraintCreditLimit() {
        if (customer.isPreferred()) {
            if (customer.getBalance().compareTo(customer.getCreditLimit().multiply(new BigDecimal(1.1))) > 0)
                failConstraint("Preferred Customer " + customer.getName() + " has a balance of " + customer.getBalance()
                        + ", which exceeds the credit limit of " + customer.getCreditLimit());
        } else {
            if (customer.getBalance().compareTo(customer.getCreditLimit()) > 0)
                failConstraint("Customer " + customer.getName() + " has a balance of " + customer.getBalance() + ", which exceeds the credit limit of "
                        + customer.getCreditLimit());
        }
    }

    @Sum("purchaseOrders.amountTotal where paid = false")
    public void deriveBalance() {
    }
}
