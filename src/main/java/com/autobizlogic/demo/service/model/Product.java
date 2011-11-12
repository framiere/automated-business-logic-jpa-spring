package com.autobizlogic.demo.service.model;

import static javax.persistence.CascadeType.ALL;
import static javax.persistence.FetchType.LAZY;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.*;

@Entity
@Table(name = "product")
public class Product {

	@Id
	@Column(name="product_number", nullable=false)
	public long getProductNumber() { return productNumber; }
	public void setProductNumber(long prodNum) { this.productNumber = prodNum; }
	private long productNumber;

	@Column(name="name")
	public String getName() { return this.name; }
	public void setName(String name) { this.name = name; }
	private String name;

	@Column(name="price")
	public BigDecimal getPrice() { return price; }
	public void setPrice(BigDecimal price) { this.price = price; }
	private BigDecimal price;

	
	@OneToMany(cascade=ALL, fetch=LAZY, mappedBy="product")
	public Set<Lineitem> getLineitems() { return lineitems; }
	public void setLineitems(Set<Lineitem> lineitems) { this.lineitems = lineitems; }
	private Set<Lineitem> lineitems = new HashSet<Lineitem>();

}
