package com.aniket.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.aniket.bean.Customer;
import com.aniket.service.AILogisticService;
import com.aniket.service.AiService;
import com.aniket.service.CustomerService;

import jakarta.servlet.http.HttpSession;

@Controller
public class AILogisticsController {

	@Autowired
	private AiService aiService;
	
	@Autowired
	private CustomerService customerService ; 
	
	@Autowired
	private AILogisticService aiLogisticService ; 

	

	@GetMapping("/")
	public String home() {
		return "index";
	}

	@GetMapping("/login")
	public String login(@RequestParam(required = false) String role) {
		return "login";
	}

	@GetMapping("/register")
	public String register(@RequestParam(required = false) String role) {
		return "register" ;
	} 
	
	@GetMapping("/book-shipment")
	public String bookShipment() {
		return "book-shipment";
	}

	@GetMapping("/shipments")
	public String shipments() {
		return "shipments";
	}

	@GetMapping("/shipment")
	public String shipment() {
		return "shipment";
	}

	@GetMapping("/customer-dashboard")
	public String customerDashboard() {
		return "customer-dashboard";
	}

	@GetMapping("/logout")
	public String logout(HttpSession session) {
		session.invalidate(); 
		return "redirect:/";
	}

	@GetMapping("/PredictVehicle")
	public String predictVehicle(@RequestParam String weight, @RequestParam String width, @RequestParam String length,
			@RequestParam String height, RedirectAttributes ra) {
		String sys_prompt = """
				Act as Logistic Vehicle Assigner.
				You will be provided with weight, height, length and
				width of the product,
				you have to suggest the vehicle type.
				Vehicle type must be strictly from the following vehicle only.
				Vehicle list: Bike, Cargo Auto, Mini Truck, Truck.
				MUST ONLY GIVE ONE WORD ANSWER FROM VEHICLE LIST 
				""";
		String user_prompt = "Here is the product dimensions: Weight: " + weight + " , Width: " + width + " , Height: "
				+ height + " and length: " + length;

		String result = aiService.askAi(sys_prompt, user_prompt);
		ra.addFlashAttribute("result", result);

		return "redirect:/";
	}
	
	
	@GetMapping("/forget-password")
	public String forgetPassword() {
		return "forget-password";
	}
	
	@PostMapping("/forgetPasswordLogic")
	public String forgetPasswordLogic(@RequestParam String email, RedirectAttributes ra) {
		Customer customer =customerService.findByEmail(email);
		if(customer==null) {
			ra.addFlashAttribute("error", "Email is invalid!");
		}else {
			aiLogisticService.sendPasswordResetLink(email);
			ra.addFlashAttribute("success", "Password reset link has been send!");
		}
		return "redirect:/forget-password";
	}
	
	@GetMapping("/resetPassword")
	public String resetPassword(@RequestParam String token, RedirectAttributes ra, Model m) {
		String email = aiLogisticService.verifyToken(token); 
		
		if(email != null) {
			m.addAttribute("email", email); 
			return "resetPassword"; 
		}
		else {
			ra.addFlashAttribute("error", "Token verification Failed!"); 
			return "redirect:/login" ;
		}
	}
	
	
	@PostMapping("/updatePassword")
	public String updatePassword(@RequestParam String email, @RequestParam String password, RedirectAttributes ra) {
		aiLogisticService.updatePassword(email, password); 
		ra.addFlashAttribute("success", "Pasword reset successfully!"); 
		return "redirect:/login" ; 
	}
}
