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
		String api = "/shipment/getAllShipmentById/" + id ; 
		ResponseEntity<Shipment> result=restTemplate.exchange(url+api,HttpMethod.GET,null,Shipment.class);
		return result.getBody();
	}

	public static List<Shipment> getAllShipments() {
		String api = "shipment/getAllShipments"; 
		ResponseEntity<List<Shipment>> res = restTemplate.exchange(
				url+api, 
				HttpMethod.GET, 
				null, 
				new ParameterizedTypeReference<List<Shipment>>() {}); 
		
		return res.getBody(); 
	}


}