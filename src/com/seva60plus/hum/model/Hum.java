package com.seva60plus.hum.model;

import java.io.Serializable;

public class Hum implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 3L;
	private String subscriberId;
	private String imeiNumber;
	private String name;
	private String phone;
	private String countryCode;
	private String dateOfBirth;
	private String gender;
	private String email;
	private String isGeoEnabled;
	private int id;

	public String getSubscriberId() {
		return subscriberId;
	}

	public void setSubscriberId(String subscriberId) {
		this.subscriberId = subscriberId;
	}

	public String getImeiNumber() {
		return imeiNumber;
	}

	public void setImeiNumber(String imeiNumber) {
		this.imeiNumber = imeiNumber;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getCountryCode() {
		return countryCode;
	}

	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}

	public String getDateOfBirth() {
		return dateOfBirth;
	}

	public void setDateOfBirth(String dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getIsGeoEnabled() {
		return isGeoEnabled;
	}

	public void setIsGeoEnabled(String isGeoEnabled) {
		this.isGeoEnabled = isGeoEnabled;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
}
