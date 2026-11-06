package com.aniket.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.aniket.jwt.JwtUtil;

@Service
public class AILogisticService {

	@Value("${rest_webservice_url}")
	private String url ; 
	
	@Value("${frontend_url}")
	private String f_url ; 
	
	private RestTemplate restTemplate = new RestTemplate(); 
	private BCryptPasswordEncoder bcrypt = new BCryptPasswordEncoder(); 
	
	
	@Autowired 
	private MailService mailService ; 
	
	@Autowired 
	private JwtUtil jwtUtil ; 
	
	public void sendPasswordResetLink(String email) {
		String token = jwtUtil.generateToken(email); 
		String link = f_url+"/resetPassword?token=" + token ; 
		String message = "Click the link to reset Password"+link ; 
//		mailService.simpleMail(email, "Ai Logistic: Password reset link", message);
		mailService.htmlMail(email, "ShippSure: Password reset link", message);
	}
	
//	Situation	-> Returns
//	Token is valid (correct signature, not expired)	-> The email stored in the token (its subject)
//	Token is invalid, tampered, expired, or malformed -> null
	public String verifyToken(String token) {
		return jwtUtil.validateToken(token); 
	}
	
	public void updatePassword(String email, String password) {
		password = bcrypt.encode(password); 
		
		String api = "/updatePassword"; 
		List<String> data = new ArrayList<>(); 
		data.add(email); 
		data.add(password); 
		
		HttpEntity<List> reqEntity = new HttpEntity<List>(data);
		restTemplate.exchange(url+api, HttpMethod.PUT, reqEntity, Boolean.class);  
	}
}
