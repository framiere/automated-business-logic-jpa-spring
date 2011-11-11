package autobizlogic.demo.buslogicdemospring.businesslogic;

import static com.autobizlogic.abl.businesslogicengine.ConstraintFailure.failConstraint;

import java.math.BigDecimal;

import autobizlogic.demo.buslogicdemospring.data.Customer;

import com.autobizlogic.abl.businesslogic.annotations.Constraint;
import com.autobizlogic.abl.businesslogic.annotations.CurrentBean;
import com.autobizlogic.abl.businesslogic.annotations.Sum;

public class CustomerLogic {

	@CurrentBean
	public Customer customer;
	
	@Constraint
	public void constraintCreditLimit() {
		if (customer.isPreferred()) {
			if (customer.getBalance().compareTo(customer.getCreditLimit().multiply(new BigDecimal(1.1))) > 0)
				failConstraint("Preferred Customer " + customer.getName() + 
						" has a balance of " + customer.getBalance() + 
						", which exceeds the credit limit of " + customer.getCreditLimit());
		} else {
			if (customer.getBalance().compareTo(customer.getCreditLimit()) > 0)
				failConstraint("Customer " + customer.getName() + 
						" has a balance of " + customer.getBalance() + 
						", which exceeds the credit limit of " + customer.getCreditLimit());
		}
	}
	
	@Sum("purchaseOrders.amountTotal where paid = false")
	public void deriveBalance() { }
}
