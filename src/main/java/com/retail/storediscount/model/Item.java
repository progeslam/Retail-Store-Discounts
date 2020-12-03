package com.retail.storediscount.model;

import java.math.BigDecimal;
import java.util.UUID;

public class Item {

	private UUID id;

	private String name;

	private String code;

	private ItemType type;

	private BigDecimal price;

	public Item(ItemType itemType, BigDecimal price) {
		this.type = itemType;
		this.price = price;
	}

	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public ItemType getType() {
		return type;
	}

	public void setType(ItemType type) {
		this.type = type;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}
	
	public enum ItemType {
		GROCERY, OTHER
	}

}
