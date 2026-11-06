package com.aniket.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.aniket.bean.Driver;
import com.aniket.bean.Shipment;
import com.aniket.service.DriverService;
import com.aniket.service.ShipmentService;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;


@RequestMapping("/driver")
@Controller
public class DriverController {
	
	@Autowired
	private DriverService driverService ; 
	
	@Autowired
	private ShipmentService shipmentService ; 
	
	private BCryptPasswordEncoder bcrypt = new BCryptPasswordEncoder(); 
	
	
	@PostMapping("/register")
	public String register(@ModelAttribute Driver driver, @RequestPart MultipartFile rc, @RequestPart MultipartFile lic, RedirectAttributes ra) throws IOException {
		BCryptPasswordEncoder bcrypt=new BCryptPasswordEncoder();
		driver.setPassword(bcrypt.encode(driver.getPassword())); 
		driver.setVehicle_rc(rc.getBytes()); 
		driver.setDriving_license(lic.getBytes()); 
		
		if(driverService.register(driver)) {
			ra.addFlashAttribute("success", "driver registered successfully ! now wait for the admin approval"); 
			return "redirect:/register"; 
		}
		else {
			ra.addFlashAttribute("error", "driver email already exists"); 
			return "redirect:/register" ; 
		}
	}
	
	@PostMapping("/login")
	public String login(@RequestParam String email, @RequestParam String password, HttpSession session, RedirectAttributes ra) {
		
		Driver driver = driverService.findByEmail(email); 
		
		if(driver==null || !bcrypt.matches(password, driver.getPassword())) {
			ra.addFlashAttribute("error", "Invalid email or password"); 
			return "redirect:/login" ; 
		}
		
		if(driver.getStatus().equalsIgnoreCase("Pending")) {
			ra.addFlashAttribute("error", "Verification Pending by Team ShippSure"); 
			return "redirect:/login" ; 
		}
		
		if(driver.getStatus().equalsIgnoreCase("Rejected")) {
			ra.addFlashAttribute("error", "Profile Rejected by Team ShippSure"); 
			return "redirect:/login" ; 
		}
		
		
		if(driver.getStatus().equalsIgnoreCase("Deactivated")) {
			ra.addFlashAttribute("error", "Profile Deactivated by Team ShippSure"); 
			return "redirect:/login" ; 
		}
		
		session.setAttribute("driver", driver); 
		return "redirect:/driver/driver-dashboard" ; 
		
	}
	
	@GetMapping("/driver-dashboard")
	public String driverDashboard(HttpSession session, @RequestParam(required = false) String status, Model m, RedirectAttributes ra) {
		if(session.getAttribute("driver") == null) {
			ra.addFlashAttribute("error", "Please Login First"); 
			return "redirect:/login"; 
		}
		
		Driver d = (Driver) session.getAttribute("driver");
		List<Shipment> allShipments = shipmentService.getAllShipmentByDriver_id(d.getEmail(), "All");
		if (allShipments == null) {
			allShipments = List.of();
		}

		long totalAssigned = allShipments.size();
		long activeCount = allShipments.stream()
				.filter(s -> "Pending".equalsIgnoreCase(s.getStatus()) || "InTransit".equalsIgnoreCase(s.getStatus()))
				.count();
		long deliveredCount = allShipments.stream()
				.filter(s -> "Delivered".equalsIgnoreCase(s.getStatus()))
				.count();

		List<Shipment> filteredShipments;
		if (status == null || status.equalsIgnoreCase("All")) {
			filteredShipments = allShipments;
		} else {
			filteredShipments = allShipments.stream()
					.filter(s -> status.equalsIgnoreCase(s.getStatus()))
					.toList();
		}

		m.addAttribute("driver", d);
		m.addAttribute("shipments", filteredShipments);
		m.addAttribute("totalAssigned", totalAssigned);
		m.addAttribute("activeCount", activeCount);
		m.addAttribute("deliveredCount", deliveredCount);
		m.addAttribute("currentStatus", status == null ? "All" : status);
		return "driver/driver-dashboard";
	}
	
	
	@GetMapping("/DriverRC")
	public void driverRC(@RequestParam String email, HttpServletResponse response) throws IOException {
		response.getOutputStream().write(driverService.findByEmail(email).getVehicle_rc());
	}
	
	
	@GetMapping("/DriverLicense")
	public void driverLicense(@RequestParam String email, HttpServletResponse response) throws IOException{
		response.getOutputStream().write(driverService.findByEmail(email).getDriving_license()); 
	}
	
	@GetMapping("/SetRideStatus")
	public String setRideStatus(HttpSession session, RedirectAttributes ra) {
		Driver d = (Driver) session.getAttribute("driver");
		if (d == null) {
			ra.addFlashAttribute("error", "Please login first");
			return "redirect:/login";
		}
		String s = driverService.setRideStatus(d.getEmail());
		d.setRideStatus(s);
		session.setAttribute("driver", d);
		return "redirect:/driver/driver-dashboard";
	}
	
	@RequestMapping("ShipmentDetails")
	public String shipmentDetails(@RequestParam Long id, Model m, RedirectAttributes ra, HttpSession session) {
		Driver d = (Driver)session.getAttribute("driver"); 
		if(d == null) {
			ra.addFlashAttribute("error", "Please Login first!"); 
			return "redirect:/login" ; 
		}
		
		m.addAttribute("shipment", shipmentService.getShipmentById(id)); 
		return "driver/shipment_details" ; 
	}
	
	@RequestMapping("/driver-earnings")
	public String driverEarnings(Model m, RedirectAttributes ra, HttpSession session) {
	    Driver d = (Driver) session.getAttribute("driver");
	    if (d == null) {
	        ra.addFlashAttribute("error", "Please Login First!");
	        return "redirect:/login";
	    }
	    List<Shipment> s = shipmentService.getAllShipmentByDriver_id(d.getEmail(), "Delivered");
	    double total = 0;
	    for (Shipment sh : s) {
	        total += sh.getAmount();
	    }
	    m.addAttribute("shipments", s);
	    m.addAttribute("total", total);
	    return "driver/driver-earnings";
	}
	
}
