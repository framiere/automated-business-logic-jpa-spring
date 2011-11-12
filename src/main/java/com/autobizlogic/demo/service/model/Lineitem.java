package com.autobizlogic.demo.service.model;

import static javax.persistence.GenerationType.IDENTITY;

import java.math.BigDecimal;
import javax.persistence.*;

@Entity
@Table(name = "lineitem")
public class Lineitem {

	@Id @GeneratedValue(strategy=IDENTITY)
	@Column(name="lineitem_id")
	public long getLineitemId() { return lineitemId;}
	public void setLineitemId(long lineitemId) { this.lineitemId = lineitemId;}
	private long lineitemId;
	
	@Column(name="qty_ordered")
	public Integer getQtyOrdered() { return qtyOrdered; }
	public void setQtyOrdered(Integer qtyOrdered) { this.qtyOrdered = qtyOrdered; }
	private Integer qtyOrdered;
	
	@Column(name="product_price")
	public BigDecimal getProductPrice() { return productPrice; }
	public void setProductPrice(BigDecimal productPrice) { this.productPrice = productPrice; }
	private BigDecimal productPrice;
	
	@Column(name="amount")
	public BigDecimal getAmount() { return amount; }
	public void setAmount(BigDecimal amount) { this.amount = amount; }
	private BigDecimal amount;
	
	@ManyToOne
	@JoinColumn(name="product_number")
	public Product getProduct() { return product;}
	public void setProduct(Product product) { this.product = product; }
	private Product product;
	
	@ManyToOne
	@JoinColumn(name="order_number")
	public PurchaseOrder getPurchaseOrder() { return purchaseOrder; }
	public void setPurchaseOrder(PurchaseOrder purchaseorder) { this.purchaseOrder = purchaseorder; }
	private PurchaseOrder purchaseOrder;
}
