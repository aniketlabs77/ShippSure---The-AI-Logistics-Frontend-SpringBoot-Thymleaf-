package com.aniket.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.aniket.service.CustomerService;
import com.aniket.service.ShipmentService;

@RequestMapping("/shipemnt")
@Controller
public class ShipmentController {
	
	@Autowired
	private CustomerService customerService; 
	
	@Autowired
	private ShipmentService shipmentService ; 
	
	@PostMapping("/shimentDetails")
	public String shipmentDetails(@RequestParam Long id, Model m) {
		m.addAttribute("shipment", shipmentService.getShipmentById(id)); 
		return "customer/shipment_details" ; 
	}
}
