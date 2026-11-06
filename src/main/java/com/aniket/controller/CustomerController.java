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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
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
	public String register(@ModelAttribute Customer customer, RedirectAttributes ra, HttpSession session) {
		
		customer.setPassword(bcrypt.encode(customer.getPassword())); 
		
		if(customerService.register(customer)) {
			ra.addFlashAttribute("msg", "Customer registered successfully"); 
			session.setAttribute("customer", customer); 
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
		
		if(customer == null) {
			ra.addFlashAttribute("error", "Invalid email or password"); 
			return "redirect:/login"; 
		}
		
		if(customer.getPassword() == null || customer.getPassword().isBlank()) {
			ra.addFlashAttribute("error", "This account is linked with Google Sign-In. Please click the Google button below, or use 'Forgot Password' to set a password."); 
			return "redirect:/login"; 
		}
		
		if(!bcrypt.matches(password, customer.getPassword())) {
			ra.addFlashAttribute("error", "Invalid email or password"); 
			return "redirect:/login"; 
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
	public String customerDashboard(HttpSession session, Model m, RedirectAttributes ra) {
		Customer customer = (Customer) session.getAttribute("customer");
		if (customer == null) {
			ra.addFlashAttribute("error", "Please Login First");
			return "redirect:/login";
		}

		List<Shipment> allShipments = shipmentService.getAllShipmentByCustomer_id(customer.getEmail(), "All");
		if (allShipments == null) {
			allShipments = List.of();
		}

		long totalCount = allShipments.size();
		long activeCount = allShipments.stream()
				.filter(s -> "Pending".equalsIgnoreCase(s.getStatus()) || "InTransit".equalsIgnoreCase(s.getStatus()))
				.count();
		long deliveredCount = allShipments.stream()
				.filter(s -> "Delivered".equalsIgnoreCase(s.getStatus()))
				.count();

		List<Shipment> recentShipments = allShipments.stream()
				.sorted((s1, s2) -> Long.compare(s2.getId(), s1.getId()))
				.limit(5)
				.toList();

		String aiSummary;
		if (activeCount > 0) {
			aiSummary = "You have " + activeCount + " active shipment" + (activeCount > 1 ? "s" : "")
					+ " moving right now. Live GPS tracking and real-time checkpoint updates are active.";
		} else if (deliveredCount > 0) {
			aiSummary = "All your previous shipments have been delivered successfully. Dispatch is ready for your next booking.";
		} else {
			aiSummary = "Welcome to ShippSure! Book your first shipment to get instant AI vehicle recommendation and verified driver dispatch.";
		}

		m.addAttribute("customer", customer);
		m.addAttribute("shipments", recentShipments);
		m.addAttribute("totalCount", totalCount);
		m.addAttribute("activeCount", activeCount);
		m.addAttribute("deliveredCount", deliveredCount);
		m.addAttribute("aiSummary", aiSummary);

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
	public String bookShipmentNow(@RequestParam String city, @RequestParam String vtype, @RequestParam String pickup_address, @RequestParam String drop_address, @RequestParam String pickup_latitude, @RequestParam String pickup_longitude, @RequestParam String drop_latitude, @RequestParam String drop_longitude, Model m, HttpSession session, RedirectAttributes ra) {
		if (session.getAttribute("customer") == null) {
			ra.addFlashAttribute("error", "Please Login First");
			return "redirect:/login";
		}
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
		
		Driver driver = driverService.assignBestDriver(vtype, pickup_latitude, pickup_longitude);

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
	public String PayNow(@ModelAttribute Shipment shipment, HttpSession session, RedirectAttributes ra) {
		Customer c = (Customer) session.getAttribute("customer"); 
		if (c == null) {
			ra.addFlashAttribute("error", "Please login first");
			return "redirect:/login";
		}
		shipment.setCustomer_id(c.getEmail()); 
		shipment.setCustomer_name(c.getName()); 
		shipment.setCurrent_latitude(shipment.getPickup_latitude()); 
		shipment.setCurrent_longitude(shipment.getPickup_longitude()); 
		shipment.setPickup_datetime(LocalDateTime.now()); 
		shipmentService.saveOrder(shipment); 
		
		if (shipment.getDriver_id() != null && !shipment.getDriver_id().isEmpty()) {
			driverService.setRideStatus(shipment.getDriver_id());
		}
		
		return "redirect:/customer/shipments"; 
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
	
	@PostMapping("/GetPrice")
	public String getPrice(@RequestParam String vtype, @RequestParam String pickup_latitude, @RequestParam String pickup_longitude, @RequestParam String drop_latitude, @RequestParam String drop_longitude, RedirectAttributes ra) {
	    String sys_prompt = """
	            Act as Distance Predictor between cities for Logistic application.
	            You will be provided with latitude-longitude of pickup address and drop address, 
	            you have to get distance in km between cities of given address by road.
	            Distance must be strictly in number only. 
	            Give only one number.
	            """;
	    String user_prompt = "Here is the latitude and longitude of pickup address: " + pickup_latitude + " - " + pickup_longitude + " and latitude and longitude of drop address: " + drop_latitude + " - " + drop_longitude;
	    String result = aiService.askAi(sys_prompt, user_prompt);
	    
	    ra.addFlashAttribute("price", result);
	    return "redirect:/customer/book-shipment-now";
	}
	
	@GetMapping("/shipment/{id}/tracking")
	@ResponseBody
	public Shipment getShipmentTracking(@PathVariable Long id) {
	    return shipmentService.getShipmentById(id); 
	}
}
