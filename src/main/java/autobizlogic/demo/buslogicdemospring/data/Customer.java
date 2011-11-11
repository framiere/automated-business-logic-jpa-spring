package autobizlogic.demo.buslogicdemospring.data;

import java.math.BigDecimal;
import java.util.List;
import java.util.Vector;

import javax.persistence.*;

@Entity
@Table(name = "customer")
public class Customer implements Cloneable {

	private String name;
	private BigDecimal balance;
	private BigDecimal creditLimit;
	private boolean preferred;
	
	@Id
	public String getName() {
		return name;
	}
	
	public void setName(String s) {
		name = s;
	}
	
	public BigDecimal getBalance() {
		return balance;
	}

	public void setBalance(BigDecimal balance) {
		this.balance = balance;
	}

	public BigDecimal getCreditLimit() {
		return creditLimit;
	}

	public void setCreditLimit(BigDecimal creditLimit) {
		this.creditLimit = creditLimit;
	}

	public boolean isPreferred() {
		return preferred;
	}

	public void setPreferred(boolean isPreferred) {
		this.preferred = isPreferred;
	}
	
	@OneToMany(cascade={CascadeType.ALL}, fetch=FetchType.EAGER, mappedBy="customer")
	@OrderBy("orderNumber desc")
	public List<Purchaseorder> getPurchaseorders() { return this.purchaseorders; }
	public void setPurchaseorders(List<Purchaseorder> purchaseorders) { this.purchaseorders = purchaseorders; }
	private List<Purchaseorder> purchaseorders = new Vector<Purchaseorder>();

	public Object clone() {
		try {
			return super.clone();
		}
		catch(CloneNotSupportedException ex) {
			ex.printStackTrace();
			throw new RuntimeException(ex);
		}
	}
}
