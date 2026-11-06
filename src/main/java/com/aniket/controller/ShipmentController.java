package com.aniket.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.aniket.service.CustomerService;
import com.aniket.service.ShipmentService;

import jakarta.servlet.http.HttpSession;

@RequestMapping("/shipment")
@Controller
public class ShipmentController {
	
	@Autowired
	private CustomerService customerService; 
	
	@Autowired
	private ShipmentService shipmentService ;

	@Autowired
	private com.aniket.service.DriverService driverService; 
	
	@PostMapping("/ShipmentDetails")
	public String shipmentDetails(@RequestParam Long id, Model m) {
		m.addAttribute("shipment", shipmentService.getShipmentById(id)); 
		return "customer/shipment_details" ; 
	}
	
	@GetMapping("/ShipmentStatus")
	public String shipmentStatus(@RequestParam String status, @RequestParam(required = false) String email, @RequestParam Long id, HttpSession session) {
		shipmentService.setStatus(status, email, id); 

		if ("Delivered".equalsIgnoreCase(status)) {
			com.aniket.bean.Shipment s = shipmentService.getShipmentById(id);
			if (s != null && s.getDriver_id() != null && !s.getDriver_id().isEmpty()) {
				driverService.setRideStatus(s.getDriver_id());
			}
		}
		
		if(session.getAttribute("customer") != null) {
			return "redirect:/customer/shipments" ; 
		}
		
		if(session.getAttribute("driver") != null) {
			return "redirect:/driver/driver-dashboard" ; 
		}
		return "redirect:/"; 
	}
	
	@PostMapping("/UpdateLocation")
	public String updateLocation(@RequestParam double current_latitude, @RequestParam double current_longitude, @RequestParam Long id, HttpSession session) {
		shipmentService.updateLocation(current_latitude, current_longitude, id); 
		return "redirect:/driver/ShipmentDetails?id="+id ; 
	}
}
