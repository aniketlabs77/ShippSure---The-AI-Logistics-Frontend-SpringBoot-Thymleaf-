package com.aniket.controller;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.aniket.bean.Customer;
import com.aniket.bean.Driver;
import com.aniket.bean.Shipment;
import com.aniket.service.AiService;
import com.aniket.service.CustomerService;
import com.aniket.service.DriverService;
import com.aniket.service.ShipmentService;

import jakarta.servlet.http.HttpSession;

@RequestMapping("/customer")
@Controller
class CustomerController {

	@Autowired
	private CustomerService customerService ; 
	
	@Autowired
	private ShipmentService shipmentService ; 
	
	@Autowired
	private DriverService driverService ; 
	
	@Autowired
	private AiService aiService ; 
	private BCryptPasswordEncoder bcrypt = new BCryptPasswordEncoder(); 
	
	@PostMapping("/register")
	public String register(@ModelAttribute Customer customer, RedirectAttributes ra) {
		
		customer.setPassword(bcrypt.encode(customer.getPassword())); 
		
		if(customerService.register(customer)) {
			ra.addFlashAttribute("msg", "Customer registered successfully"); 
			return "redirect:/customer/customer-dashboard"; 
		}
		else {
			ra.addFlashAttribute("msg", "customer email already exists"); 
			return "redirect:/register";  
		}
	}
	
	@PostMapping("/login")
	public String login(@RequestParam String email, @RequestParam String password, HttpSession session, RedirectAttributes ra) {
		
		Customer customer = customerService.findByEmail(email); 
		
		if(customer == null || !bcrypt.matches(password, customer.getPassword())) {
			ra.addFlashAttribute("error", "Invalid eamil or password"); 
			return "redirect:/login" ; 
		}
		
		session.setAttribute("customer", customer); 
		return "redirect:/customer/customer-dashboard"; 
	}
	
	
	@GetMapping("/google-success")
	public String googleSuccess(OAuth2AuthenticationToken authToken, HttpSession session) {
		
		String email = authToken.getPrincipal().getAttribute("email"); 
		String name = authToken.getPrincipal().getAttribute("name"); 
		
		Customer customer = customerService.findByEmail(email); 
		
		if(customer==null) {
			customer = new Customer(); 
			
			customer.setEmail(email); 
			customer.setName(name); 
			customer.setPassword(null); 
			
			customerService.register(customer); 
		}
		
		session.setAttribute("customer", customer); 
		session.setAttribute("customerEmail", customer.getEmail()); 
		return "redirect:/customer/customer-dashboard";
	}
	
	
	@GetMapping("/customer-dashboard")
	public String customerDashboard(HttpSession session, RedirectAttributes ra) {
		
		if(session.getAttribute("customer") == null) {
			ra.addFlashAttribute("error", "Please Login First"); 
			return "redirect:/login" ; 
		}
		return "customer/customer-dashboard";
	}
	
	@GetMapping("/book-shipment")
	public String bookShipment(HttpSession session, RedirectAttributes ra) {
		
		if(session.getAttribute("customer") == null) {
			ra.addFlashAttribute("error", "Please Login First"); 
			return "redirect:/login" ; 
		}
		return "customer/book-shipment";
	}
	
	@PostMapping("/PredictVehicle")
	public String predictVehicle(@RequestParam String weight,@RequestParam String width,@RequestParam String length,@RequestParam String height, RedirectAttributes ra) {
		String sys_prompt="""
				Act as Logistic Vehicle Assigner.
				You will be provided with weight, height, length and 
				width of the product, 
				you have to suggest the vehicle type.
				Vehicle type must be strictly from Bike, Cargo Auto, Mini Truck, Truck only. 
				Give only vehicle name.
				""";
		String user_prompt="Here is the product dimensions: Weight: "+weight+" , Width: "+width+" , Height: "+height+" and length: "+length;
		String result = aiService.askAi(sys_prompt,user_prompt);
		ra.addFlashAttribute("result",result);
		return "redirect:/customer/book-shipment";
	}
	
	@PostMapping("/book-shipment-now")
	public String bookShipmentNow(@RequestParam String city,@RequestParam String vtype,@RequestParam String pickup_address,@RequestParam String drop_address,@RequestParam String pickup_latitude,@RequestParam String pickup_longitude, @RequestParam String drop_latitude,@RequestParam String drop_longitude, Model m) {
		if (city != null && (city.equalsIgnoreCase("New Delhi") || city.toLowerCase().contains("delhi"))) {
		    city = "Delhi";
		}
		int rate=0;
		switch(vtype) {
			case "Bike": 
				rate=10;
				break;
			case "Cargo Auto":
				rate=20;
				break;
			case "Mini Truck":
				rate=30;
				break;
			case "Truck":
				rate=40;
				break;
		}
		
		List<Driver> drivers=driverService.getDriversByCityAndStatusAndRideStatus(city,"Activated","Free");
		Driver driver=null;
		if(drivers!=null && drivers.size()!=0) {
			Random random=new Random();
			driver=drivers.get(random.nextInt(drivers.size()));
		}

		m.addAttribute("driver",driver);
		m.addAttribute("vtype",vtype);
		m.addAttribute("vrate",rate);
		m.addAttribute("pickup_address",pickup_address);
		m.addAttribute("drop_address",drop_address);
		m.addAttribute("pickup_latitude",pickup_latitude);
		m.addAttribute("pickup_longitude",pickup_longitude);
		m.addAttribute("drop_latitude",drop_latitude);
		m.addAttribute("drop_longitude",drop_longitude);
		return "customer/book-shipment-now";
	}
	

	
	@PostMapping("/PayNow")
	public String PayNow(@ModelAttribute Shipment shipment, HttpSession session) {
		Customer c = (Customer)session.getAttribute("customer"); 
		shipment.setCustomer_id(c.getEmail()); 
		shipment.setCustomer_name(c.getName()); 
		shipment.setCurrent_latitude(shipment.getPickup_latitude()); 
		shipment.setCurrent_longitude(shipment.getPickup_longitude()); 
		shipment.setPickup_datetime(LocalDateTime.now()); 
		shipmentService.saveOrder(shipment); 
		
		return "redirect:/customer/shipments" ; 
	}
	
	@GetMapping("/shipments")
	public String shipments(HttpSession session, @RequestParam(required = false)String status,RedirectAttributes ra,Model m) {
		if(session.getAttribute("customer")==null) {
			ra.addFlashAttribute("error", "Please Login First!");
			return "redirect:/login";
		}
		Customer c=(Customer)session.getAttribute("customer");
		List<Shipment> shipments=shipmentService.getAllShipmentByCustomer_id(c.getEmail(),status==null?"All":status);
		m.addAttribute("shipments",shipments);
		return "customer/shipments";
	}
}
