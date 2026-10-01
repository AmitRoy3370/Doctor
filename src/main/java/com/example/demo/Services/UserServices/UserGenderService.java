package com.example.demo.Services.UserServices;

import java.util.List;

import com.example.demo.Models.UserModels.UserGender;

public interface UserGenderService {

	public UserGender addUserGender(UserGender gender, String userId);
	public UserGender updateUserGender(UserGender gender, String userId, String id);
	
	public UserGender findById(String id);
	public List<UserGender> findAll();
	UserGender findUserGenderByUserIdNative(String userId);
	List<UserGender> findByGender(String gender);
	List<Object[]> countGroupedByGender();
	List<UserGender> findByGenderAndUserIdPattern(String gender, String pattern);
	boolean existsByUserIdNative(String userId);
	
	boolean deleteByUserIdNative(String userId);
	boolean deleteUserGender(String id, String userId);
	
}
