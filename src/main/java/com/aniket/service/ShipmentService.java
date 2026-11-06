package com.aniket.service;

import java.util.List;

import org.jspecify.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.aniket.bean.Shipment;



@Service
public class ShipmentService {

    @Value("${rest_webservice_url}")
    private String url;

    @Autowired
    private MailService mailService;
    
    private RestTemplate restTemplate = new RestTemplate();

    public boolean saveOrder(Shipment shipment) {
        String api = "/customer/saveorder";
        HttpEntity<Shipment> reqEntity = new HttpEntity<>(shipment);
        ResponseEntity<Boolean> result =
            restTemplate.exchange(url + api, HttpMethod.POST, reqEntity, Boolean.class);
        return Boolean.TRUE.equals(result.getBody());
    }

	public List<Shipment> getAllShipmentByCustomer_id(String customer_id, String status) {
		String api = "/shipment/getAllShipmentByCustomer_id/" + customer_id + "/" + status ; 
		
		ResponseEntity<List<Shipment>> res = restTemplate.exchange(url + api, HttpMethod.GET, null, new ParameterizedTypeReference<List<Shipment>>() {}); 
		return res.getBody(); 
	}

	public Shipment getShipmentById(Long id) {
		String api = "/shipment/getShipmentById/" + id ; 
		ResponseEntity<Shipment> result=restTemplate.exchange(url+api,HttpMethod.GET,null,Shipment.class);
		return result.getBody();
	}

	public List<Shipment> getAllShipments() {
		String api = "/shipment/getAllShipments"; 
		ResponseEntity<List<Shipment>> res = restTemplate.exchange(
				url+api, 
				HttpMethod.GET, 
				null, 
				new ParameterizedTypeReference<List<Shipment>>() {}); 
		
		return res.getBody(); 
	}

	
	public boolean setStatus(String status, String email, Long id) {
		if(status.equalsIgnoreCase("Delivered")) {
			String message = "Your Goods Delivered"; 
			mailService.htmlMail(email, "Your Goods are delivered", message); 
		}
		
		String api = "/shipment/setStatus/" + status + "/" + id ; 
		ResponseEntity<Boolean> result = restTemplate.exchange(url+api, HttpMethod.PUT, null, Boolean.class); 
		return Boolean.TRUE.equals(result.getBody()); 
	}
	
	public List<Shipment> getAllShipmentByDriver_id(String driver_id, String status){
		String api = "/shipment/getAllShipmentByDriver_id/" + driver_id + "/" + status ; 
		ResponseEntity<List<Shipment>> result = restTemplate.exchange(url + api, HttpMethod.GET, null, new ParameterizedTypeReference<List<Shipment>>() {}); 
		
		return result.getBody(); 
	}
	
	public boolean updateLocation(double current_latitude, double current_longitude, Long id) {
		String api = "/shipment/updateLocation/" + current_latitude + "/" + current_longitude + "/" + id ; 
		ResponseEntity<Boolean> result = restTemplate.exchange(url+api, HttpMethod.PUT, null, Boolean.class); 
		return Boolean.TRUE.equals((result.getBody())); 
	}
}