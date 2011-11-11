package autobizlogic.demo.buslogicdemospring.businesslogic;

import java.math.BigDecimal;

import autobizlogic.demo.buslogicdemospring.data.Customer;

import com.autobizlogic.abl.businesslogic.annotations.*;
import com.autobizlogic.abl.businesslogicengine.ConstraintFailure;

public class CustomerLogic {

	@CurrentBean
	public Customer customer;
	
	@Constraint
	public void constraintCreditLimit() {
		if (customer.isPreferred()) {
			if (customer.getBalance().compareTo(customer.getCreditLimit().multiply(new BigDecimal(1.1))) > 0)
				ConstraintFailure.failConstraint("Preferred Customer " + customer.getName() + 
						" has a balance of " + customer.getBalance() + 
						", which exceeds the credit limit of " + customer.getCreditLimit());
		} else {
			if (customer.getBalance().compareTo(customer.getCreditLimit()) > 0)
				ConstraintFailure.failConstraint("Customer " + customer.getName() + 
						" has a balance of " + customer.getBalance() + 
						", which exceeds the credit limit of " + customer.getCreditLimit());
		}
	}
	
	@Sum("purchaseorders.amountTotal where paid = false")
	public void deriveBalance() { }
}
