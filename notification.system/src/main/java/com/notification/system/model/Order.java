package com.notification.system.model;

public class Order {
	private Long id;
	private String items;
	private String customerEmail;
	private OrderStatus status;

	public Long getId ( ) {
		return id;
	}

	public void setId ( Long id ) {
		this.id = id;
	}

	public String getItems ( ) {
		return items;
	}

	public void setItems ( String items ) {
		this.items = items;
	}

	public String getCustomerEmail ( ) {
		return customerEmail;
	}

	public void setCustomerEmail ( String customerEmail ) {
		this.customerEmail = customerEmail;
	}

	public OrderStatus getStatus ( ) {
		return status;
	}

	public void setStatus ( OrderStatus status ) {
		this.status = status;
	}
}
