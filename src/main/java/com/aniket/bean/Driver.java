package com.aniket.bean;

import java.util.Arrays;

import lombok.Data;


public class Driver {
	private String email;
	private String name;
	private String phone;
	private String vehicle_type;
	private String vehicle_rc_no;
	private byte[] vehicle_rc;
	private String driving_license_no;
	private byte[] driving_license;
	private String password;
	private String status="Pending";
	
	// added afterupdate 1 
	private String city;
	private Double latitude;
	private Double longitude;
	private String rideStatus = "Busy";

	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public Double getLatitude() {
		return latitude;
	}
	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}
	public Double getLongitude() {
		return longitude;
	}
	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}
	public String getRideStatus() {
		return rideStatus;
	}
	public void setRideStatus(String rideStatus) {
		this.rideStatus = rideStatus;
	}
	@Override
	public String toString() {
		return "Driver [email=" + email + ", name=" + name + ", phone=" + phone + ", vehicle_type=" + vehicle_type
				+ ", vehicle_rc_no=" + vehicle_rc_no + ", vehicle_rc=" + Arrays.toString(vehicle_rc)
				+ ", driving_license_no=" + driving_license_no + ", driving_license=" + Arrays.toString(driving_license)
				+ ", password=" + password + ", status=" + status + ", city=" + city + ", latitude=" + latitude
				+ ", longitude=" + longitude + ", rideStatus=" + rideStatus + "]";
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
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
	public String getVehicle_type() {
		return vehicle_type;
	}
	public void setVehicle_type(String vehicle_type) {
		this.vehicle_type = vehicle_type;
	}
	public String getVehicle_rc_no() {
		return vehicle_rc_no;
	}
	public void setVehicle_rc_no(String vehicle_rc_no) {
		this.vehicle_rc_no = vehicle_rc_no;
	}
	public byte[] getVehicle_rc() {
		return vehicle_rc;
	}
	public void setVehicle_rc(byte[] vehicle_rc) {
		this.vehicle_rc = vehicle_rc;
	}
	public String getDriving_license_no() {
		return driving_license_no;
	}
	public void setDriving_license_no(String driving_license_no) {
		this.driving_license_no = driving_license_no;
	}
	public byte[] getDriving_license() {
		return driving_license;
	}
	public void setDriving_license(byte[] driving_license) {
		this.driving_license = driving_license;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	
	
}
