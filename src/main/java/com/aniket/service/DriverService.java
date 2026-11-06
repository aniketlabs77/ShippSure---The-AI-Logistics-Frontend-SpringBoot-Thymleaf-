package com.aniket.service;

import com.aniket.security.SecurityConfig;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.aniket.bean.Driver;


@Service
public class DriverService {
	private final SecurityConfig securityConfig;

	@Value("${rest_webservice_url}")
	private String url ; 
	
	private RestTemplate restTemplate = new RestTemplate();

	DriverService(SecurityConfig securityConfig) {
		this.securityConfig = securityConfig;
	} 
	
	public boolean register(Driver driver) {
		String api = "/driver/register"; 
		HttpEntity<Driver> requestEntity = new HttpEntity<Driver>(driver); 
		ResponseEntity<Boolean> result = restTemplate.exchange(url+api, HttpMethod.POST, requestEntity, Boolean.class); 
		return result.getBody(); 
	}
	
	public Driver findByEmail(String email) {
		String api = "/driver/findByEmail/" + email; 
		ResponseEntity<Driver> result = restTemplate.exchange(url+api, HttpMethod.GET, null, Driver.class); 
		return result.getBody(); 
	}

	public List<Driver> getDriversByCityAndStatusAndRideStatus(String city,String status,String rideStatus) {
		String api="/driver/findAllByCityAndStatusAndRideStatus/"+city+"/"+status+"/"+rideStatus;
		ResponseEntity<List<Driver>> result =restTemplate.exchange(
					url+api,
	                HttpMethod.GET,
	                null,
	                new ParameterizedTypeReference<List<Driver>>() {}
	            );
		return result.getBody();
	}

	public String setRideStatus(String email) {
		String api = "/driver/setRideStatus/" + email;
		ResponseEntity<String> result = restTemplate.exchange(url + api, HttpMethod.PUT, null, String.class);
		return result.getBody();
	}

	public Driver assignBestDriver(String vtype, String pickup_latitude, String pickup_longitude) {
		List<Driver> allFreeDrivers = getDriversByStatusAndRideStatus("Activated", "Free"); 
		
		if(allFreeDrivers == null || allFreeDrivers.isEmpty()) {
			return null ; 
		}
		
		List<Driver> matching = allFreeDrivers.stream()
				.filter(d -> d.getVehicle_type()!=null && d.getVehicle_type().equalsIgnoreCase(vtype))
				.toList(); 
		
		if(matching.isEmpty()) {
			return null ; 
		}
		
		double pLat = Double.parseDouble(pickup_latitude); 
		double pLng = Double.parseDouble(pickup_longitude); 
		
		// STAGE 1: Rank candidates by Haversine straight-line distance, take top 5
		List<Driver> topCandidates = matching.stream()
				.filter(d -> d.getLatitude()!=null && d.getLongitude()!=null)
				.sorted((d1, d2) -> Double.compare(
						calculateHaversineKm(d1.getLatitude(), d1.getLongitude(), pLat, pLng), 
						calculateHaversineKm(d2.getLatitude(), d2.getLongitude(), pLat, pLng)
						))
						.limit(5)
						.toList(); 
		
		if(topCandidates.isEmpty()) {
			return matching.get(0); 
		}
		
		// STAGE 2: Query OSRM road travel time (seconds) for candidates, pick minimum ETA
		Driver best = null ; 
		double minDuration = Double.MAX_VALUE; 
		
		for(Driver d : topCandidates) {
			double duration = getOsrmDuration(d.getLatitude(), d.getLongitude(), pLat, pLng); 
			if(duration < minDuration) {
				minDuration = duration ; 
				best = d ; 
			}
		}
		
		return (best != null && minDuration<Double.MAX_VALUE) ? best : topCandidates.get(0); 
	}
	
	
	public List<Driver> getDriversByStatusAndRideStatus(String status, String rideStatus){
		String api = "/driver/findAllByStatusAndRideStatus/" + status + "/" + rideStatus ; 
		ResponseEntity<List<Driver>> result = restTemplate.exchange(
					url + api, 
					HttpMethod.GET, 
					null, 
					new ParameterizedTypeReference<List<Driver>>() {}
		); 
		
		return result.getBody(); 
	}
	
	private double calculateHaversineKm(double lat1, double lon1, double lat2, double lon2) {
		double dLat = Math.toRadians(lat2 - lat1);
		double dLon = Math.toRadians(lon2 - lon1);
		double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
				+ Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
				* Math.sin(dLon / 2) * Math.sin(dLon / 2);
		double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
		return 6371.0 * c;
	}
	private double getOsrmDuration(double fromLat, double fromLng, double toLat, double toLng) {
		try {
			RestTemplate rt = new RestTemplate();
			String osrmUrl = "https://router.project-osrm.org/route/v1/driving/"
					+ fromLng + "," + fromLat + ";" + toLng + "," + toLat + "?overview=false";
			String json = rt.getForObject(osrmUrl, String.class);
			ObjectMapper mapper = new ObjectMapper();
			JsonNode root = mapper.readTree(json);
			if (root.has("routes") && root.get("routes").size() > 0) {
				return root.get("routes").get(0).get("duration").asDouble();
			}
		} catch (Exception e) {
			// Fallback: if OSRM is unreachable, ignore duration and let Haversine decide
		}
		return Double.MAX_VALUE;
	}
			
}
