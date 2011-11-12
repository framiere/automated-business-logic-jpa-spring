package com.autobizlogic.demo.service.model;

import static javax.persistence.CascadeType.ALL;
import static javax.persistence.GenerationType.IDENTITY;

import java.math.BigDecimal;
import java.util.List;
import java.util.Vector;

import javax.persistence.*;

@Entity
@Table(name = "purchaseorder")
public class PurchaseOrder {

	@Id @GeneratedValue(strategy=IDENTITY)
	@Column(name="order_number", nullable=false)
	public Long getOrderNumber() { return orderNumber; }
	public void setOrderNumber(Long orderNumber) { this.orderNumber = orderNumber; }
	private Long orderNumber;

	@Column(name="amount_total")
	public BigDecimal getAmountTotal() { return amountTotal; }
	public void setAmountTotal(BigDecimal amountTotal) { this.amountTotal = amountTotal; }
	private BigDecimal amountTotal;

	@Column(name="paid")
	public Boolean getPaid() { return paid; }
	public void setPaid(Boolean paid) { this.paid = paid; }
	private Boolean paid;

	@Column(name="notes")
	public String getNotes() { return notes; }
	public void setNotes(String notes) { this.notes = notes; }
	private String notes;

	@ManyToOne
	@JoinColumn(name="customer_name", nullable=false)
	public Customer getCustomer() { return customer; }
	public void setCustomer(Customer customer) { this.customer = customer; }
	private Customer customer;

	@OneToMany(mappedBy="purchaseOrder", cascade=ALL)
	@OrderBy("lineitemId desc")
	public List<Lineitem> getLineitems() { return lineitems; }
	public void setLineitems(List<Lineitem> lineitems) { this.lineitems = lineitems;}
	private List<Lineitem> lineitems = new Vector<Lineitem>();
}
