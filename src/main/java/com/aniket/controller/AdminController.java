package com.aniket.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.ui.Model; 
import com.aniket.bean.Admin;
import com.aniket.bean.Driver;
import com.aniket.bean.Shipment;
import com.aniket.service.AdminService;
import com.aniket.service.ShipmentService;

import jakarta.servlet.http.HttpSession;

@RequestMapping("/admin")
@Controller
public class AdminController {

	@Autowired
	private AdminService adminService;

	@PostMapping("/login")
	public String login(@ModelAttribute Admin admin, HttpSession session, 
			RedirectAttributes ra) throws IOException{
		
		Admin a = adminService.login(admin); 
		if(a != null) {
			session.setAttribute("admin", a); 
			return "redirect:/admin/admin-dashboard" ; 
		}
		else {
			ra.addFlashAttribute("error", "Invalid Credentials"); 
			return "redirect:/admin/admin-dashboard" ; 
		}
	}

	@GetMapping("/admin-dashboard")
public String adminDashboard(HttpSession session, Model m, RedirectAttributes ra ) {
		if(session.getAttribute("admin") == null) {
			ra.addFlashAttribute("error", "Please Login first"); 
			return "redirect:/login" ; 
		}
		
		List<Shipment> shipments = ShipmentService.getAllShipments(); 
		m.addAttribute("shipments", shipments); 
		return "admin/admin-dashboard";
	}
	
	
	@GetMapping("/drs")
	public String drivers( ) {
		return "admin/drivers";
	}
	
	@GetMapping("/drivers")
	public String drivers( @RequestParam(required = false)String status , RedirectAttributes ra) {
		List<Driver> drivers=adminService.getAllDrivers(status==null?"All":status);
		ra.addFlashAttribute("drivers",drivers);
		return "redirect:/admin/drs";
	}
	
	@GetMapping("/DriverStatus")
	public String driverStatus(@RequestParam String status,@RequestParam String email) {
		adminService.setStatus(status,email);
		return "redirect:/admin/drivers";
	}
	
	@PostMapping("/DriverDetails")
	public String driverDetails(@RequestParam String email, Model m) {
		m.addAttribute("driver",adminService.getDriver(email));
		return "admin/driver_details";
	}
}
