package com.example.demo.Controllers.UserControllers;

import com.example.demo.Models.UserModels.User;
import com.example.demo.Services.UserServices.UserService;
import DTOFiles.JwtResponse;
import DTOFiles.UserResponseDTO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api/users")
public class UserController {

	@Autowired
	private UserService userService;

	// ===============================
	// AUTHENTICATION ENDPOINTS
	// ===============================

	/**
	 * Register a new user with profile image POST /api/users/register
	 */
	@PostMapping("/register")
	public ResponseEntity<?> registerUser(@RequestPart("user") User user,
			@RequestPart(value = "profileImage", required = false) MultipartFile profileImage) {
		try {
			JwtResponse response = userService.createUser(user, profileImage);
			return ResponseEntity.status(HttpStatus.CREATED).body(response);
		} catch (NullPointerException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse(e.getMessage()));
		} catch (ArithmeticException e) {
			return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse(e.getMessage()));
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(errorResponse("Registration failed: " + e.getMessage()));
		}
	}

	/**
	 * User login POST /api/users/login
	 */
	@PostMapping("/login")
	public ResponseEntity<?> login(@RequestParam String userName, @RequestParam String password) {
		try {
			JwtResponse response = userService.logIn(userName, password);
			return ResponseEntity.ok(response);
		} catch (NullPointerException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse(e.getMessage()));
		} catch (ArithmeticException e) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse(e.getMessage()));
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(errorResponse("Login failed: " + e.getMessage()));
		}
	}

	// ===============================
	// USER CRUD OPERATIONS
	// ===============================

	/**
	 * Get user by ID GET /api/users/{id}
	 */
	@GetMapping("/{id}")
	public ResponseEntity<?> getUserById(@PathVariable String id) {
		try {
			UserResponseDTO user = userService.findUserById(id);
			return ResponseEntity.ok(user);
		} catch (NullPointerException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse(e.getMessage()));
		} catch (NoSuchElementException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse(e.getMessage()));
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(errorResponse("Failed to fetch user: " + e.getMessage()));
		}
	}

	/**
	 * Get user by username GET /api/users/username/{userName}
	 */
	@GetMapping("/username/{userName}")
	public ResponseEntity<?> getUserByUsername(@PathVariable String userName) {
		try {
			UserResponseDTO user = userService.findByUserName(userName);
			return ResponseEntity.ok(user);
		} catch (NullPointerException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse(e.getMessage()));
		} catch (NoSuchElementException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse(e.getMessage()));
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(errorResponse("Failed to fetch user: " + e.getMessage()));
		}
	}

	/**
	 * Get user by username (case insensitive) GET
	 * /api/users/username/ignorecase/{userName}
	 */
	@GetMapping("/username/ignorecase/{userName}")
	public ResponseEntity<?> getUserByUsernameIgnoreCase(@PathVariable String userName) {
		try {
			UserResponseDTO user = userService.findByUserNameIgnoreCase(userName);
			return ResponseEntity.ok(user);
		} catch (NullPointerException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse(e.getMessage()));
		} catch (NoSuchElementException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse(e.getMessage()));
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(errorResponse("Failed to fetch user: " + e.getMessage()));
		}
	}

	// ===============================
	// GET ALL USERS WITH PAGINATION
	// ===============================

	/**
	 * Get all users with pagination GET
	 * /api/users?page=0&size=10&sortBy=name&direction=asc
	 */
	@GetMapping
	public ResponseEntity<?> getAllUsers(@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size, @RequestParam(defaultValue = "name") String sortBy,
			@RequestParam(defaultValue = "asc") String direction) {
		try {
			Sort.Direction sortDirection = direction.equalsIgnoreCase("desc") ? Sort.Direction.DESC
					: Sort.Direction.ASC;
			Pageable pageable = PageRequest.of(page, size, Sort.by(sortDirection, sortBy));
			Page<User> users = userService.getUsersWithPagination(pageable);
			return ResponseEntity.ok(users);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(errorResponse("Failed to fetch users: " + e.getMessage()));
		}
	}

	/**
	 * Get all users with pagination (native query) GET
	 * /api/users/native?page=0&size=10
	 */
	@GetMapping("/native")
	public ResponseEntity<?> getAllUsersNative(@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size) {
		try {
			Pageable pageable = PageRequest.of(page, size);
			// Note: You may need to add this method to your service/repository
			// For now using the existing method
			Page<User> users = userService.getUsersWithPagination(pageable);
			return ResponseEntity.ok(users);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(errorResponse("Failed to fetch users: " + e.getMessage()));
		}
	}

	// ===============================
	// GET ALL USERS (NO PAGINATION)
	// ===============================

	/**
	 * Get all users GET /api/users/all
	 */
	@GetMapping("/all")
	public ResponseEntity<?> getAllUsers() {
		try {
			List<UserResponseDTO> users = userService.getAllUsers();
			return ResponseEntity.ok(users);
		} catch (NoSuchElementException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse(e.getMessage()));
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(errorResponse("Failed to fetch users: " + e.getMessage()));
		}
	}

	/**
	 * Get all users sorted by name ascending GET /api/users/sorted/asc
	 */
	@GetMapping("/sorted/asc")
	public ResponseEntity<?> getAllUsersSortedByNameAsc() {
		try {
			List<UserResponseDTO> users = userService.getAllUsersOrderByNameAsc();
			return ResponseEntity.ok(users);
		} catch (NoSuchElementException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse(e.getMessage()));
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(errorResponse("Failed to fetch users: " + e.getMessage()));
		}
	}

	/**
	 * Get all users sorted by name descending GET /api/users/sorted/desc
	 */
	@GetMapping("/sorted/desc")
	public ResponseEntity<?> getAllUsersSortedByNameDesc() {
		try {
			List<UserResponseDTO> users = userService.getAllUsersOrderByNameDesc();
			return ResponseEntity.ok(users);
		} catch (NoSuchElementException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse(e.getMessage()));
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(errorResponse("Failed to fetch users: " + e.getMessage()));
		}
	}

	/**
	 * Get all users (native query) GET /api/users/native/all
	 */
	@GetMapping("/native/all")
	public ResponseEntity<?> getAllUsersNative() {
		try {
			List<UserResponseDTO> users = userService.findAllNative();
			return ResponseEntity.ok(users);
		} catch (NoSuchElementException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse(e.getMessage()));
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(errorResponse("Failed to fetch users: " + e.getMessage()));
		}
	}

	// ===============================
	// SEARCH ENDPOINTS
	// ===============================

	/**
	 * Search users by keyword GET /api/users/search?keyword=john
	 */
	@GetMapping("/search")
	public ResponseEntity<?> searchUsers(@RequestParam String keyword) {
		try {
			List<UserResponseDTO> users = userService.searchUsers(keyword);
			return ResponseEntity.ok(users);
		} catch (NullPointerException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse(e.getMessage()));
		} catch (NoSuchElementException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse(e.getMessage()));
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(errorResponse("Search failed: " + e.getMessage()));
		}
	}

	/**
	 * Search users by name GET /api/users/search/name?name=john
	 */
	@GetMapping("/search/name")
	public ResponseEntity<?> searchByName(@RequestParam String name) {
		try {
			List<UserResponseDTO> users = userService.searchByName(name);
			return ResponseEntity.ok(users);
		} catch (NullPointerException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse(e.getMessage()));
		} catch (NoSuchElementException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse(e.getMessage()));
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(errorResponse("Search failed: " + e.getMessage()));
		}
	}

	/**
	 * Search users by name (case insensitive) GET
	 * /api/users/search/name/ignorecase?name=john
	 */
	@GetMapping("/search/name/ignorecase")
	public ResponseEntity<?> searchByNameIgnoreCase(@RequestParam String name) {
		try {
			List<UserResponseDTO> users = userService.findByNameIgnoreCase(name);
			return ResponseEntity.ok(users);
		} catch (NullPointerException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse(e.getMessage()));
		} catch (NoSuchElementException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse(e.getMessage()));
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(errorResponse("Search failed: " + e.getMessage()));
		}
	}

	/**
	 * Search users by username GET /api/users/search/username?username=john
	 */
	@GetMapping("/search/username")
	public ResponseEntity<?> searchByUsername(@RequestParam String username) {
		try {
			List<UserResponseDTO> users = userService.searchByUserName(username);
			return ResponseEntity.ok(users);
		} catch (NullPointerException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse(e.getMessage()));
		} catch (NoSuchElementException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse(e.getMessage()));
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(errorResponse("Search failed: " + e.getMessage()));
		}
	}

	/**
	 * Search users by name (native query) GET
	 * /api/users/search/native/name?name=john
	 */
	@GetMapping("/search/native/name")
	public ResponseEntity<?> searchByNameNative(@RequestParam String name) {
		try {
			List<UserResponseDTO> users = userService.searchByNameNative(name);
			return ResponseEntity.ok(users);
		} catch (NullPointerException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse(e.getMessage()));
		} catch (NoSuchElementException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse(e.getMessage()));
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(errorResponse("Search failed: " + e.getMessage()));
		}
	}

	/**
	 * Get user by username (native query) GET /api/users/native/username/{userName}
	 */
	@GetMapping("/native/username/{userName}")
	public ResponseEntity<?> getUserByUsernameNative(@PathVariable String userName) {
		try {
			UserResponseDTO user = userService.findByUserNameNative(userName);
			return ResponseEntity.ok(user);
		} catch (NullPointerException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse(e.getMessage()));
		} catch (NoSuchElementException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse(e.getMessage()));
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(errorResponse("Failed to fetch user: " + e.getMessage()));
		}
	}

	// ===============================
	// UPDATE ENDPOINTS
	// ===============================

	/**
	 * Update complete user profile PUT /api/users/{id}
	 */
	@PutMapping("/{id}")
	public ResponseEntity<?> updateUser(@PathVariable String id, @RequestPart("user") User user,
			@RequestPart(value = "profileImage", required = false) MultipartFile profileImage,
			@RequestParam String userId) {
		try {
			User updatedUser = userService.updateUser(user, profileImage, id, userId);
			return ResponseEntity.ok(updatedUser);
		} catch (NullPointerException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse(e.getMessage()));
		} catch (NoSuchElementException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse(e.getMessage()));
		} catch (ArithmeticException e) {
			return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse(e.getMessage()));
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(errorResponse("Update failed: " + e.getMessage()));
		}
	}

	/**
	 * Update user name PUT /api/users/{id}/name
	 */
	@PutMapping("/{id}/name")
	public ResponseEntity<?> updateName(@PathVariable String id, @RequestParam String name) {
		try {
			User updatedUser = userService.updateName(id, name);
			return ResponseEntity.ok(updatedUser);
		} catch (NullPointerException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse(e.getMessage()));
		} catch (NoSuchElementException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse(e.getMessage()));
		} catch (ArithmeticException e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(errorResponse("Update failed: " + e.getMessage()));
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(errorResponse("Update failed: " + e.getMessage()));
		}
	}

	/**
	 * Update username PUT /api/users/{id}/username
	 */
	@PutMapping("/{id}/username")
	public ResponseEntity<?> updateUsername(@PathVariable String id, @RequestParam String userName,
			@RequestParam String userId) {
		try {
			User updatedUser = userService.updateUserName(id, userName, userId);
			return ResponseEntity.ok(updatedUser);
		} catch (NullPointerException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse(e.getMessage()));
		} catch (NoSuchElementException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse(e.getMessage()));
		} catch (ArithmeticException e) {
			return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse(e.getMessage()));
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(errorResponse("Update failed: " + e.getMessage()));
		}
	}

	/**
	 * Update profile image PUT /api/users/{id}/profileimage
	 */
	@PutMapping("/{id}/profileimage")
	public ResponseEntity<?> updateProfileImage(@PathVariable String id, @RequestParam("file") MultipartFile file,
			@RequestParam String userId) {
		try {
			User updatedUser = userService.updateProfileImage(id, file, userId);
			return ResponseEntity.ok(updatedUser);
		} catch (NullPointerException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse(e.getMessage()));
		} catch (NoSuchElementException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse(e.getMessage()));
		} catch (ArithmeticException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse(e.getMessage()));
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(errorResponse("Update failed: " + e.getMessage()));
		}
	}

	/**
	 * Update password PUT /api/users/{id}/password
	 */
	@PutMapping("/{id}/password")
	public ResponseEntity<?> updatePassword(@PathVariable String id, @RequestParam String password) {
		try {
			User updatedUser = userService.updatePassword(id, password);
			return ResponseEntity.ok(updatedUser);
		} catch (NullPointerException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse(e.getMessage()));
		} catch (NoSuchElementException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse(e.getMessage()));
		} catch (ArithmeticException e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(errorResponse("Update failed: " + e.getMessage()));
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(errorResponse("Update failed: " + e.getMessage()));
		}
	}

	// ===============================
	// DELETE ENDPOINTS
	// ===============================

	/**
	 * Delete user by ID DELETE /api/users/{id}
	 */
	@DeleteMapping("/{id}")
	public ResponseEntity<?> deleteUserById(@PathVariable String id, @RequestParam String userId) {
		try {
			boolean deleted = userService.deleteUserById(id, userId);
			if (deleted) {
				return ResponseEntity.ok(successResponse("User deleted successfully"));
			} else {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse("Failed to delete user"));
			}
		} catch (NullPointerException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse(e.getMessage()));
		} catch (NoSuchElementException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse(e.getMessage()));
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(errorResponse("Delete failed: " + e.getMessage()));
		}
	}

	/**
	 * Delete users by profile image ID DELETE
	 * /api/users/profileimage/{profileImageId}
	 */
	@DeleteMapping("/profileimage/{profileImageId}")
	public ResponseEntity<?> deleteByProfileImageId(@PathVariable String profileImageId, @RequestParam String userId) {
		try {
			boolean deleted = userService.deleteByProfileImageId(profileImageId, userId);
			if (deleted) {
				return ResponseEntity.ok(successResponse("Users deleted successfully"));
			} else {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse("Failed to delete users"));
			}
		} catch (NullPointerException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse(e.getMessage()));
		} catch (NoSuchElementException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse(e.getMessage()));
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(errorResponse("Delete failed: " + e.getMessage()));
		}
	}

	/**
	 * Delete users without profile image DELETE /api/users/noprofileimage
	 */
	@DeleteMapping("/noprofileimage")
	public ResponseEntity<?> deleteUsersWithoutProfileImage(@RequestParam String userId) {
		try {
			boolean deleted = userService.deleteUsersWithoutProfileImage(userId);
			if (deleted) {
				return ResponseEntity.ok(successResponse("Users without profile image deleted successfully"));
			} else {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse("Failed to delete users"));
			}
		} catch (NullPointerException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse(e.getMessage()));
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(errorResponse("Delete failed: " + e.getMessage()));
		}
	}

	// ===============================
	// COUNT ENDPOINTS
	// ===============================

	/**
	 * Get total count of users GET /api/users/count
	 */
	@GetMapping("/count")
	public ResponseEntity<?> countAllUsers() {
		try {
			long count = userService.countAllUsers();
			return ResponseEntity.ok(Map.of("totalUsers", count));
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(errorResponse("Failed to count users: " + e.getMessage()));
		}
	}

	/**
	 * Get count of users by name GET /api/users/count/name?name=john
	 */
	@GetMapping("/count/name")
	public ResponseEntity<?> countByName(@RequestParam String name) {
		try {
			long count = userService.countByName(name);
			return ResponseEntity.ok(Map.of("count", count));
		} catch (NullPointerException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse(e.getMessage()));
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(errorResponse("Failed to count users: " + e.getMessage()));
		}
	}

	/**
	 * Get count of users by profile image ID GET
	 * /api/users/count/profileimage/{profileImageId}
	 */
	@GetMapping("/count/profileimage/{profileImageId}")
	public ResponseEntity<?> countByProfileImageId(@PathVariable String profileImageId) {
		try {
			long count = userService.countByProfileImageId(profileImageId);
			return ResponseEntity.ok(Map.of("count", count));
		} catch (NullPointerException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse(e.getMessage()));
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(errorResponse("Failed to count users: " + e.getMessage()));
		}
	}

	/**
	 * Get total count of users (native query) GET /api/users/count/native
	 */
	@GetMapping("/count/native")
	public ResponseEntity<?> countUsersNative() {
		try {
			long count = userService.countUsersNative();
			return ResponseEntity.ok(Map.of("totalUsers", count));
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(errorResponse("Failed to count users: " + e.getMessage()));
		}
	}

	// ===============================
	// USER PROFILE IMAGE ENDPOINTS
	// ===============================

	/**
	 * Get users with profile image GET /api/users/withprofileimage
	 */
	@GetMapping("/withprofileimage")
	public ResponseEntity<?> findUsersWithProfileImage() {
		try {
			List<UserResponseDTO> users = userService.findUsersWithProfileImage();
			return ResponseEntity.ok(users);
		} catch (NoSuchElementException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse(e.getMessage()));
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(errorResponse("Failed to fetch users: " + e.getMessage()));
		}
	}

	/**
	 * Get users without profile image GET /api/users/withoutprofileimage
	 */
	@GetMapping("/withoutprofileimage")
	public ResponseEntity<?> findUsersWithoutProfileImage() {
		try {
			List<UserResponseDTO> users = userService.findUsersWithoutProfileImage();
			return ResponseEntity.ok(users);
		} catch (NoSuchElementException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse(e.getMessage()));
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(errorResponse("Failed to fetch users: " + e.getMessage()));
		}
	}

	/**
	 * Get users by profile image ID GET /api/users/profileimage/{profileImageId}
	 */
	@GetMapping("/profileimage/{profileImageId}")
	public ResponseEntity<?> findByProfileImageId(@PathVariable String profileImageId) {
		try {
			List<UserResponseDTO> users = userService.findByProfileImageId(profileImageId);
			return ResponseEntity.ok(users);
		} catch (NullPointerException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse(e.getMessage()));
		} catch (NoSuchElementException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse(e.getMessage()));
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(errorResponse("Failed to fetch users: " + e.getMessage()));
		}
	}

	/**
	 * Check if username exists GET /api/users/exists/username?userName=john
	 */
	@GetMapping("/exists/username")
	public ResponseEntity<?> existsByUsername(@RequestParam String userName) {
		try {
			boolean exists = userService.existsByUserName(userName);
			return ResponseEntity.ok(Map.of("exists", exists));
		} catch (NullPointerException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse(e.getMessage()));
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(errorResponse("Failed to check username: " + e.getMessage()));
		}
	}
	
	// ===============================
	// PAGINATED LIST ENDPOINTS (NEW)
	// ===============================

	/**
	 * GET /api/users/paged?page=0&size=20&sort=name,asc
	 */
	@GetMapping("/paged")
	public ResponseEntity<?> getAllUsersPaged(
	        @PageableDefault(size = 20, sort = "name") Pageable pageable) {
	    try {
	        return ResponseEntity.ok(userService.getAllUsersPaged(pageable));
	    } catch (NoSuchElementException e) {
	        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse(e.getMessage()));
	    } catch (Exception e) {
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
	                .body(errorResponse("Failed to fetch users: " + e.getMessage()));
	    }
	}

	/**
	 * GET /api/users/paged/sorted/asc?page=0&size=20
	 */
	@GetMapping("/paged/sorted/asc")
	public ResponseEntity<?> getAllUsersSortedAscPaged(
	        @PageableDefault(size = 20, sort = "name") Pageable pageable) {
	    try {
	        return ResponseEntity.ok(userService.getAllUsersOrderByNameAscPaged(pageable));
	    } catch (NoSuchElementException e) {
	        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse(e.getMessage()));
	    } catch (Exception e) {
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
	                .body(errorResponse("Failed to fetch users: " + e.getMessage()));
	    }
	}

	/**
	 * GET /api/users/paged/sorted/desc?page=0&size=20
	 */
	@GetMapping("/paged/sorted/desc")
	public ResponseEntity<?> getAllUsersSortedDescPaged(
	        @PageableDefault(size = 20, sort = "name") Pageable pageable) {
	    try {
	        return ResponseEntity.ok(userService.getAllUsersOrderByNameDescPaged(pageable));
	    } catch (NoSuchElementException e) {
	        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse(e.getMessage()));
	    } catch (Exception e) {
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
	                .body(errorResponse("Failed to fetch users: " + e.getMessage()));
	    }
	}

	/**
	 * GET /api/users/paged/native?page=0&size=20
	 */
	@GetMapping("/paged/native")
	public ResponseEntity<?> getAllUsersNativePaged(
	        @PageableDefault(size = 20, sort = "name") Pageable pageable) {
	    try {
	        return ResponseEntity.ok(userService.findAllNativePaged(pageable));
	    } catch (NoSuchElementException e) {
	        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse(e.getMessage()));
	    } catch (Exception e) {
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
	                .body(errorResponse("Failed to fetch users: " + e.getMessage()));
	    }
	}

	/**
	 * GET /api/users/paged/search?keyword=john&page=0&size=20
	 */
	@GetMapping("/paged/search")
	public ResponseEntity<?> searchUsersPaged(@RequestParam String keyword,
	                                          @PageableDefault(size = 20) Pageable pageable) {
	    try {
	        return ResponseEntity.ok(userService.searchUsersPaged(keyword, pageable));
	    } catch (NullPointerException e) {
	        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse(e.getMessage()));
	    } catch (NoSuchElementException e) {
	        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse(e.getMessage()));
	    } catch (Exception e) {
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
	                .body(errorResponse("Search failed: " + e.getMessage()));
	    }
	}

	/**
	 * GET /api/users/paged/search/name?name=john&page=0&size=20
	 */
	@GetMapping("/paged/search/name")
	public ResponseEntity<?> searchByNamePaged(@RequestParam String name,
	                                           @PageableDefault(size = 20) Pageable pageable) {
	    try {
	        return ResponseEntity.ok(userService.searchByNamePaged(name, pageable));
	    } catch (NullPointerException e) {
	        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse(e.getMessage()));
	    } catch (NoSuchElementException e) {
	        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse(e.getMessage()));
	    } catch (Exception e) {
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
	                .body(errorResponse("Search failed: " + e.getMessage()));
	    }
	}

	/**
	 * GET /api/users/paged/search/name/ignorecase?name=john&page=0&size=20
	 */
	@GetMapping("/paged/search/name/ignorecase")
	public ResponseEntity<?> searchByNameIgnoreCasePaged(@RequestParam String name,
	                                                     @PageableDefault(size = 20) Pageable pageable) {
	    try {
	        return ResponseEntity.ok(userService.findByNameIgnoreCasePaged(name, pageable));
	    } catch (NullPointerException e) {
	        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse(e.getMessage()));
	    } catch (NoSuchElementException e) {
	        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse(e.getMessage()));
	    } catch (Exception e) {
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
	                .body(errorResponse("Search failed: " + e.getMessage()));
	    }
	}

	/**
	 * GET /api/users/paged/search/username?username=john&page=0&size=20
	 */
	@GetMapping("/paged/search/username")
	public ResponseEntity<?> searchByUsernamePaged(@RequestParam String username,
	                                               @PageableDefault(size = 20) Pageable pageable) {
	    try {
	        return ResponseEntity.ok(userService.searchByUserNamePaged(username, pageable));
	    } catch (NullPointerException e) {
	        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse(e.getMessage()));
	    } catch (NoSuchElementException e) {
	        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse(e.getMessage()));
	    } catch (Exception e) {
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
	                .body(errorResponse("Search failed: " + e.getMessage()));
	    }
	}

	/**
	 * GET /api/users/paged/search/native/name?name=john&page=0&size=20
	 */
	@GetMapping("/paged/search/native/name")
	public ResponseEntity<?> searchByNameNativePaged(@RequestParam String name,
	                                                 @PageableDefault(size = 20) Pageable pageable) {
	    try {
	        return ResponseEntity.ok(userService.searchByNameNativePaged(name, pageable));
	    } catch (NullPointerException e) {
	        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse(e.getMessage()));
	    } catch (NoSuchElementException e) {
	        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse(e.getMessage()));
	    } catch (Exception e) {
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
	                .body(errorResponse("Search failed: " + e.getMessage()));
	    }
	}

	/**
	 * GET /api/users/paged/withprofileimage?page=0&size=20
	 */
	@GetMapping("/paged/withprofileimage")
	public ResponseEntity<?> findUsersWithProfileImagePaged(
	        @PageableDefault(size = 20) Pageable pageable) {
	    try {
	        return ResponseEntity.ok(userService.findUsersWithProfileImagePaged(pageable));
	    } catch (NoSuchElementException e) {
	        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse(e.getMessage()));
	    } catch (Exception e) {
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
	                .body(errorResponse("Failed to fetch users: " + e.getMessage()));
	    }
	}

	/**
	 * GET /api/users/paged/withoutprofileimage?page=0&size=20
	 */
	@GetMapping("/paged/withoutprofileimage")
	public ResponseEntity<?> findUsersWithoutProfileImagePaged(
	        @PageableDefault(size = 20) Pageable pageable) {
	    try {
	        return ResponseEntity.ok(userService.findUsersWithoutProfileImagePaged(pageable));
	    } catch (NoSuchElementException e) {
	        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse(e.getMessage()));
	    } catch (Exception e) {
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
	                .body(errorResponse("Failed to fetch users: " + e.getMessage()));
	    }
	}

	/**
	 * GET /api/users/paged/profileimage/{profileImageId}?page=0&size=20
	 */
	@GetMapping("/paged/profileimage/{profileImageId}")
	public ResponseEntity<?> findByProfileImageIdPaged(@PathVariable String profileImageId,
	                                                   @PageableDefault(size = 20) Pageable pageable) {
	    try {
	        return ResponseEntity.ok(userService.findByProfileImageIdPaged(profileImageId, pageable));
	    } catch (NullPointerException e) {
	        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse(e.getMessage()));
	    } catch (NoSuchElementException e) {
	        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse(e.getMessage()));
	    } catch (Exception e) {
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
	                .body(errorResponse("Failed to fetch users: " + e.getMessage()));
	    }
	}

	// ===============================
	// HELPER METHODS
	// ===============================

	private Map<String, Object> errorResponse(String message) {
		Map<String, Object> response = new HashMap<>();
		response.put("success", false);
		response.put("message", message);
		response.put("timestamp", System.currentTimeMillis());
		return response;
	}

	private Map<String, Object> successResponse(String message) {
		Map<String, Object> response = new HashMap<>();
		response.put("success", true);
		response.put("message", message);
		response.put("timestamp", System.currentTimeMillis());
		return response;
	}
}
