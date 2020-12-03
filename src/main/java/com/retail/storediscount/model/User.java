package com.retail.storediscount.model;

import java.time.LocalDate;
import java.util.UUID;

public class User {

	private UUID id;
	private String name;
	private String email;
	private String phone;
	private String address;
	private LocalDate joinDate;
	private UserType type;

	public User(UserType userType, LocalDate joinDate) {
		this.type = userType;
		this.joinDate = joinDate;
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

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public LocalDate getJoinDate() {
		return joinDate;
	}

	public void setJoinDate(LocalDate joinDate) {
		this.joinDate = joinDate;
	}

	public UserType getType() {
		return type;
	}

	public void setType(UserType type) {
		this.type = type;
	}
	
	public enum UserType {
		REGULAR, EMPLOYEE, AFFILIATE
	}

}
