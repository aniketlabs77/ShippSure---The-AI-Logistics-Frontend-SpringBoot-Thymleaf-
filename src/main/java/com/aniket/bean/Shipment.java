package com.aniket.bean;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class Shipment {

	private Long id;
	private String vtype;
	private String description;
	private String customer_id;
	private String customer_name;
	private String driver_id;
	private String driver_name;
	private double amount;
	private double pickup_latitude;
	private double pickup_longitude;
	private double drop_latitude;
	private double drop_longitude;
	private double current_latitude;
	private double current_longitude;
	private String pickup_address;
	private String drop_address;
	private String status = "Pending";
	private LocalDateTime pickup_datetime;
	private LocalDateTime drop_datetime;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getVtype() {
		return vtype;
	}

	public void setVtype(String vtype) {
		this.vtype = vtype;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getCustomer_id() {
		return customer_id;
	}

	public void setCustomer_id(String customer_id) {
		this.customer_id = customer_id;
	}

	public String getCustomer_name() {
		return customer_name;
	}

	public void setCustomer_name(String customer_name) {
		this.customer_name = customer_name;
	}

	public String getDriver_id() {
		return driver_id;
	}

	public void setDriver_id(String driver_id) {
		this.driver_id = driver_id;
	}

	public String getDriver_name() {
		return driver_name;
	}

	public void setDriver_name(String driver_name) {
		this.driver_name = driver_name;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public double getPickup_latitude() {
		return pickup_latitude;
	}

	public void setPickup_latitude(double pickup_latitude) {
		this.pickup_latitude = pickup_latitude;
	}

	public double getPickup_longitude() {
		return pickup_longitude;
	}

	public void setPickup_longitude(double pickup_longitude) {
		this.pickup_longitude = pickup_longitude;
	}

	public double getDrop_latitude() {
		return drop_latitude;
	}

	public void setDrop_latitude(double drop_latitude) {
		this.drop_latitude = drop_latitude;
	}

	public double getDrop_longitude() {
		return drop_longitude;
	}

	public void setDrop_longitude(double drop_longitude) {
		this.drop_longitude = drop_longitude;
	}

	public double getCurrent_latitude() {
		return current_latitude;
	}

	public void setCurrent_latitude(double current_latitude) {
		this.current_latitude = current_latitude;
	}

	public double getCurrent_longitude() {
		return current_longitude;
	}

	public void setCurrent_longitude(double current_longitude) {
		this.current_longitude = current_longitude;
	}

	public String getPickup_address() {
		return pickup_address;
	}

	public void setPickup_address(String pickup_address) {
		this.pickup_address = pickup_address;
	}

	public String getDrop_address() {
		return drop_address;
	}

	public void setDrop_address(String drop_address) {
		this.drop_address = drop_address;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public LocalDateTime getPickup_datetime() {
		return pickup_datetime;
	}

	public void setPickup_datetime(LocalDateTime pickup_datetime) {
		this.pickup_datetime = pickup_datetime;
	}

	public LocalDateTime getDrop_datetime() {
		return drop_datetime;
	}

	public void setDrop_datetime(LocalDateTime drop_datetime) {
		this.drop_datetime = drop_datetime;
	}

	@Override
	public String toString() {
		return "Shipment [id=" + id + ", vtype=" + vtype + ", description=" + description + ", customer_id="
				+ customer_id + ", customer_name=" + customer_name + ", driver_id=" + driver_id + ", driver_name="
				+ driver_name + ", amount=" + amount + ", pickup_latitude=" + pickup_latitude + ", pickup_longitude="
				+ pickup_longitude + ", drop_latitude=" + drop_latitude + ", drop_longitude=" + drop_longitude
				+ ", current_latitude=" + current_latitude + ", current_longitude=" + current_longitude
				+ ", pickup_address=" + pickup_address + ", drop_address=" + drop_address + ", status=" + status
				+ ", pickup_datetime=" + pickup_datetime + ", drop_datetime=" + drop_datetime + "]";
	}
}
