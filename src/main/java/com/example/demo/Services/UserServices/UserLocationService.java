package com.example.demo.Services.UserServices;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.demo.Models.UserModels.UserLocation;

public interface UserLocationService {

	public UserLocation addUserLocation(UserLocation userLocation, String userId);
	public UserLocation updateUserLocation(UserLocation userLocation, String userId, String id);
	
	public UserLocation findById(String id);
	public List<UserLocation> findAll();
	UserLocation findByUserId(@Param("userId") String userId);
	List<UserLocation> findByLocationNameContainingIgnoreCase(@Param("locationName") String locationName);
	List<UserLocation> findByLattitudeOrLongititude(@Param("lattitude") double lattitude,
			@Param("longititude") double longititude);
	
	public boolean deleteUserLocation(String id, String userId);
	
}
