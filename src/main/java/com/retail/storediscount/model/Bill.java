package com.retail.storediscount.model;

import java.time.LocalDateTime;
import java.util.List;

public class Bill {

	private LocalDateTime billTime = LocalDateTime.now();
    private List<Item> items;
    

	public List<Item> getItems() {
		return items;
	}

	public void setItems(List<Item> items) {
		this.items = items;
	}

	public LocalDateTime getBillTime() {
		return billTime;
	}

	public void setBillTime(LocalDateTime billTime) {
		this.billTime = billTime;
	}

}
