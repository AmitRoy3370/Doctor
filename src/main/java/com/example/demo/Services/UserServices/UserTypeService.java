package com.example.demo.Services.UserServices;

import java.util.List;

import com.example.demo.Models.UserModels.UserTypes;

public interface UserTypeService {

	public UserTypes addUserTypes(UserTypes types, String userId);
	public UserTypes updateUserTypes(UserTypes type, String userId, String id);
	
	public UserTypes findById(String id);
	public List<UserTypes> findAll();
	UserTypes findByUserId(String userId);
	List<UserTypes> findByTypeContainingIgnoreCase(String type);
	boolean existsByUserIdNative(String userId);
	
	boolean deleteByUserIdNative(String userId);
	boolean deleteUserType(String id, String userId);
	
}
