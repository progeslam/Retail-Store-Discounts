package com.retail.storediscount.model;

public class Discount {

	private User user;
    private Bill bill;
    
	public Discount(User user, Bill bill) {
		super();
		this.user = user;
		this.bill = bill;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public Bill getBill() {
		return bill;
	}
	public void setBill(Bill bill) {
		this.bill = bill;
	}
    
    
    
}
