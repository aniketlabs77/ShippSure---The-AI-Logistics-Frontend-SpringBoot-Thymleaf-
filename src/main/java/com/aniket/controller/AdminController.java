package com.aniket.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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

	@Autowired
	private ShipmentService shipmentService;

	@PostMapping("/login")
	public String login(@ModelAttribute Admin admin, HttpSession session, RedirectAttributes ra) throws IOException {
		Admin a = adminService.login(admin);
		if (a != null) {
			session.setAttribute("admin", a);
			return "redirect:/admin/admin-dashboard";
		} else {
			ra.addFlashAttribute("error", "Invalid Credentials");
			return "redirect:/login";
		}
	}

	@GetMapping("/admin-dashboard")
	public String adminDashboard(HttpSession session, Model m, RedirectAttributes ra) {
		if (session.getAttribute("admin") == null) {
			ra.addFlashAttribute("error", "Please Login first");
			return "redirect:/login";
		}

		List<Shipment> shipments = shipmentService.getAllShipments();
		if (shipments == null) {
			shipments = List.of();
		}

		long totalShipments = shipments.size();
		long activeShipments = shipments.stream()
				.filter(s -> "Pending".equalsIgnoreCase(s.getStatus()) || "InTransit".equalsIgnoreCase(s.getStatus()))
				.count();
		long deliveredShipments = shipments.stream()
				.filter(s -> "Delivered".equalsIgnoreCase(s.getStatus()))
				.count();

		List<Driver> pendingDrivers = adminService.getAllDrivers("Pending");
		if (pendingDrivers == null) {
			pendingDrivers = List.of();
		}

		m.addAttribute("shipments", shipments);
		m.addAttribute("totalShipments", totalShipments);
		m.addAttribute("activeShipments", activeShipments);
		m.addAttribute("deliveredShipments", deliveredShipments);
		m.addAttribute("pendingDrivers", pendingDrivers);
		m.addAttribute("pendingDriversCount", pendingDrivers.size());

		return "admin/admin-dashboard";
	}

	@GetMapping("/drivers")
	public String drivers(HttpSession session, @RequestParam(required = false) String status, Model m, RedirectAttributes ra) {
		if (session.getAttribute("admin") == null) {
			ra.addFlashAttribute("error", "Please Login first");
			return "redirect:/login";
		}

		String filterStatus = (status == null || status.isBlank()) ? "All" : status;
		List<Driver> drivers = adminService.getAllDrivers(filterStatus);
		if (drivers == null) {
			drivers = List.of();
		}

		m.addAttribute("drivers", drivers);
		m.addAttribute("currentStatus", filterStatus);
		return "admin/drivers";
	}

	@GetMapping("/drs")
	public String driversRedirect() {
		return "redirect:/admin/drivers";
	}

	@GetMapping("/DriverStatus")
	public String driverStatus(@RequestParam String status, @RequestParam String email, HttpSession session, RedirectAttributes ra) {
		if (session.getAttribute("admin") == null) {
			ra.addFlashAttribute("error", "Please Login first");
			return "redirect:/login";
		}
		adminService.setStatus(status, email);
		ra.addFlashAttribute("success", "Driver status updated to " + status);
		return "redirect:/admin/drivers";
	}

	@PostMapping("/DriverDetails")
	public String driverDetails(@RequestParam String email, Model m, HttpSession session, RedirectAttributes ra) {
		if (session.getAttribute("admin") == null) {
			ra.addFlashAttribute("error", "Please Login first");
			return "redirect:/login";
		}
		m.addAttribute("driver", adminService.getDriver(email));
		return "admin/driver_details";
	}
}
