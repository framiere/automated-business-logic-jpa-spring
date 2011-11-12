package com.autobizlogic.demo.service.model;

import static javax.persistence.CascadeType.ALL;
import static javax.persistence.FetchType.EAGER;

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
	
	@OneToMany(cascade=ALL, fetch=EAGER, mappedBy="customer")
	@OrderBy("orderNumber desc")
	public List<PurchaseOrder> getPurchaseOrders() { return this.purchaseOrders; }
	public void setPurchaseOrders(List<PurchaseOrder> purchaseorders) { this.purchaseOrders = purchaseorders; }
	private List<PurchaseOrder> purchaseOrders = new Vector<PurchaseOrder>();

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
