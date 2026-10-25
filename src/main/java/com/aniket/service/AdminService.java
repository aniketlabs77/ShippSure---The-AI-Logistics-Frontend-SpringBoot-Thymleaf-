package com.aniket.service;


import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.aniket.bean.Admin;
import com.aniket.bean.Driver;

@Service
public class AdminService {
	
	@Value("${rest_webservice_url}")
	private String url ; 
	
	private RestTemplate rt = new RestTemplate(); 
	
	
	public Admin login(Admin admin) throws RestClientException {
		String api = "/admin/login"; 
		HttpEntity<Admin> requestEntity = new HttpEntity<Admin>(admin);
		ResponseEntity<Admin> result = rt.exchange(url+api, HttpMethod.POST, requestEntity, Admin.class); 
		return result.getBody();
	}


	public List<Driver> getAllDrivers(String status) {
		String api = "/admin/getAllDrivers/" + status ; 
		ResponseEntity<List> result = rt.exchange(url+api, HttpMethod.GET, null, List.class); 
		return result.getBody();
	}
	
	public boolean setStatus(String status, String email) {
		String api = "/driver/setStatus/" + status + "/" + email ; 
		ResponseEntity<Boolean> result = rt.exchange(url+api, HttpMethod.PUT, null, Boolean.class); 
		return result.getBody(); 
	}
	
	public Driver getDriver(String email) {
		String api = "/driver/findByEmail/" + email ; 
		ResponseEntity<Driver> result = rt.exchange(url+api, HttpMethod.GET, null, Driver.class); 
		return result.getBody(); 
	}
}

