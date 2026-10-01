package com.example.demo.Services.UserServices;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.Configs.SupabaseProperties;
import com.example.demo.Models.UserModels.User;
import com.example.demo.Models.UserModels.UserContact;
import com.example.demo.Models.UserModels.UserGender;
import com.example.demo.Models.UserModels.UserLocation;
import com.example.demo.Models.UserModels.UserTypes;
import com.example.demo.Repositories.UserRepositories.UserContactRepository;
import com.example.demo.Repositories.UserRepositories.UserGenderRepository;
import com.example.demo.Repositories.UserRepositories.UserLocationRepository;
import com.example.demo.Repositories.UserRepositories.UserRepository;
import com.example.demo.Repositories.UserRepositories.UserTypeRepository;
import com.example.demo.Security.JwtUtil;
import com.example.demo.Services.SupabaseServices.SupabaseStorageService;

import CyclicCleaner.Cleaner;
import DTOFiles.JwtResponse;
import DTOFiles.PageResponseDTO;
import DTOFiles.UserResponseDTO;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private UserContactRepository contactRepository;

	@Autowired
	private UserTypeRepository typeRepository;

	@Autowired
	private UserGenderRepository genderRepository;

	@Autowired
	private UserLocationRepository locationRepository;

	@Autowired
	private JwtUtil jwtUtil;

	@Autowired
	private SupabaseProperties supabaseProperties;

	@Autowired
	private Cleaner cleaner;

	@Autowired
	private SupabaseStorageService supabaseStorageService;

	private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

	private static final String cacheValue = "User";

	@Override
	@Caching(evict = { @CacheEvict(value = cacheValue, allEntries = true),
			@CacheEvict(value = "UserContact", allEntries = true), @CacheEvict(value = "UserGender", allEntries = true),
			@CacheEvict(value = "UserLocation", allEntries = true),
			@CacheEvict(value = "UserTypes", allEntries = true) })
	public JwtResponse createUser(User user, MultipartFile file) {

		if (user == null) {

			throw new NullPointerException("False request...");

		}

		if (user.getUserName() == null || user.getPassword() == null || user.getName() == null) {

			throw new NullPointerException("Have to put all the data perfectly at here...");

		}

		try {

			User _user = userRepository.findByUserName(user.getUserName().trim()).get();

			if (_user != null) {

				throw new ArithmeticException();

			}

		} catch (ArithmeticException e) {

			throw new ArithmeticException("This username is already exist at here...");

		} catch (Exception e) {

		}

		System.out.println("before encrypted :- " + user.getPassword());

		user.setPassword(passwordEncoder.encode(user.getPassword()));

		System.out.println("after encrypted :- " + user.getPassword());

		try {

			if (file == null || file.isEmpty()) {

				throw new Exception();

			}

			String contentType = file.getContentType();

			if (contentType != null && contentType.startsWith("image/")) {

				String profileImageUrl = uploadProfileImage(file, user.getUserName());

				user.setProfileImageId(profileImageUrl);

			} else {

				throw new ArithmeticException();

			}

		} catch (ArithmeticException e) {

			throw new ArithmeticException("Profile image is image only....");

		} catch (Exception e) {

		}

		user = userRepository.save(user);

		if (user == null) {

			throw new ArithmeticException("Registration process is not complete....");

		}

		String token = jwtUtil.generateToken(user.getName());
		return new JwtResponse(token, user.getId());
	}

	@Override
	@Cacheable(value = cacheValue, key = "'login_' + #userName + '_' + #password")
	public JwtResponse logIn(String userName, String password) {

		if (userName == null || password == null) {

			throw new NullPointerException("False request...");

		}

		try {

			User user = userRepository.findByUserName(userName).get();

			if (user == null) {

				throw new Exception();

			}

			if (!passwordEncoder.matches(password, user.getPassword())) {

				throw new Exception();

			}

			String token = jwtUtil.generateToken(user.getName());
			return new JwtResponse(token, user.getId());

		} catch (Exception e) {

			throw new ArithmeticException("Invalid credential...");

		}

	}

	@Override
	@Caching(evict = { @CacheEvict(value = cacheValue, allEntries = true),
			@CacheEvict(value = "UserContact", allEntries = true), @CacheEvict(value = "UserGender", allEntries = true),
			@CacheEvict(value = "UserLocation", allEntries = true),
			@CacheEvict(value = "UserTypes", allEntries = true) })
	public User updateUser(User user, MultipartFile file, String id, String userId) {

		if (user == null || id == null || userId == null || user.getUserName() == null || user.getPassword() == null
				|| user.getName() == null) {

			throw new NullPointerException("False request...");

		}

		try {

			User _user = userRepository.findById(id).get();

			if (_user == null) {

				throw new Exception();

			}

			if (!user.getId().equals(userId)) {

				throw new Exception();

			}

		} catch (Exception e) {

			throw new NoSuchElementException("No such user find at here...");

		}

		try {

			User _user = userRepository.findByUserName(user.getUserName()).get();

			if (_user != null) {

				if (!_user.getId().equals(userId)) {

					throw new ArithmeticException();

				}

			}

		} catch (ArithmeticException e) {

			throw new ArithmeticException("Same userName already exist...");

		} catch (Exception e) {

		}

		System.out.println("before encrypted :- " + user.getPassword());

		user.setPassword(passwordEncoder.encode(user.getPassword()));

		System.out.println("after encrypted :- " + user.getPassword());

		try {

			if (file == null || file.isEmpty()) {

				throw new Exception();

			}

			String contentType = file.getContentType();

			if (contentType != null && contentType.startsWith("image/")) {

				String bucketName = supabaseProperties.getBucket().getProfile();

				String fileName = user.getUserName();

				supabaseStorageService.deleteFile(bucketName, fileName);

				String profileImageUrl = uploadProfileImage(file, user.getUserName());

				user.setProfileImageId(profileImageUrl);

			} else {

				throw new ArithmeticException();

			}

		} catch (ArithmeticException e) {

			throw new ArithmeticException("Profile image must be image not other file...");

		} catch (Exception e) {

		}

		try {

			int result = userRepository.updateUser(userId, user.getName(), user.getUserName(), user.getProfileImageId(),
					user.getPassword());

			if (result >= 1) {

				return user;

			} else {

				throw new Exception();

			}

		} catch (Exception e) {

			throw new ArithmeticException("Profile not updated...");

		}

	}

	@Override
	@Caching(evict = { @CacheEvict(value = cacheValue, allEntries = true),
			@CacheEvict(value = "UserContact", allEntries = true), @CacheEvict(value = "UserGender", allEntries = true),
			@CacheEvict(value = "UserLocation", allEntries = true),
			@CacheEvict(value = "UserTypes", allEntries = true) })
	public User updateName(String id, String name) {

		if (id == null || name == null) {

			throw new NullPointerException("False request...");

		}

		User _user = null;

		try {

			_user = userRepository.findById(id).get();

			if (_user == null) {

				throw new Exception();

			}

			if (!_user.getId().equals(id)) {

				throw new Exception();

			}

		} catch (Exception e) {

			throw new NoSuchElementException("No such user find at here...");

		}

		try {

			int isUpdated = userRepository.updateName(id, name);

			if (isUpdated >= 1) {

				_user.setName(name);

				return _user;

			} else {

				throw new Exception();

			}

		} catch (Exception e) {

			throw new ArithmeticException("User name is not updated...");

		}

	}

	@Override
	@Caching(evict = { @CacheEvict(value = cacheValue, allEntries = true),
			@CacheEvict(value = "UserContact", allEntries = true), @CacheEvict(value = "UserGender", allEntries = true),
			@CacheEvict(value = "UserLocation", allEntries = true),
			@CacheEvict(value = "UserTypes", allEntries = true) })
	public User updateUserName(String id, String userName, String userId) {

		if (id == null || userName == null) {

			throw new NullPointerException("False request...");

		}

		User _user = null;

		try {

			_user = userRepository.findById(id).get();

			if (_user == null) {

				throw new Exception();

			}

			if (!_user.getId().equals(userId)) {

				throw new Exception();

			}

		} catch (Exception e) {

			throw new NoSuchElementException("No such user find at here...");

		}

		try {

			int isUpdated = userRepository.updateUserName(id, userName);

			if (isUpdated >= 1) {

				_user.setUserName(userName);

				return _user;

			} else {

				throw new Exception();

			}

		} catch (Exception e) {

			throw new ArithmeticException("User name is not updated...");

		}

	}

	@Override
	@Caching(evict = { @CacheEvict(value = cacheValue, allEntries = true),
			@CacheEvict(value = "UserContact", allEntries = true), @CacheEvict(value = "UserGender", allEntries = true),
			@CacheEvict(value = "UserLocation", allEntries = true),
			@CacheEvict(value = "UserTypes", allEntries = true) })
	public User updateProfileImage(String id, MultipartFile file, String userId) {

		if (id == null || userId == null || file == null) {

			throw new NullPointerException("False request...");

		}

		User user = null;

		try {

			user = userRepository.findById(id).get();

			if (user == null) {

				throw new Exception();

			}

			if (!user.getId().equals(userId)) {

				throw new Exception();

			}

		} catch (Exception e) {

			throw new NoSuchElementException("No such user find at here...");

		}

		try {

			String contentType = file.getContentType();

			if (contentType != null && contentType.startsWith("image/")) {

				String bucketName = supabaseProperties.getBucket().getProfile();

				String fileName = user.getUserName();

				supabaseStorageService.deleteFile(bucketName, fileName);

				String profileImageUrl = uploadProfileImage(file, user.getUserName());

				user.setProfileImageId(profileImageUrl);

			} else {

				throw new ArithmeticException();

			}

		} catch (ArithmeticException e) {

			throw new ArithmeticException("Profile image must be image not other file...");

		} catch (Exception e) {

		}

		try {

			int result = userRepository.updateProfileImage(userId, user.getProfileImageId());

			if (result >= 1) {

				return user;

			} else {

				throw new Exception();

			}

		} catch (Exception e) {

			throw new ArithmeticException("Profile not updated...");

		}

	}

	@Override
	@Caching(evict = { @CacheEvict(value = cacheValue, allEntries = true),
			@CacheEvict(value = "UserContact", allEntries = true), @CacheEvict(value = "UserGender", allEntries = true),
			@CacheEvict(value = "UserLocation", allEntries = true),
			@CacheEvict(value = "UserTypes", allEntries = true) })
	public User updatePassword(String id, String password) {

		if (id == null || password == null) {

			throw new NullPointerException("False request...");

		}

		User user = null;

		try {

			user = userRepository.findById(id).get();

			if (user == null) {

				throw new Exception();

			}

		} catch (Exception e) {

			throw new NoSuchElementException("No such user find at here...");

		}

		try {

			String newPassword = passwordEncoder.encode(password);

			int updated = userRepository.updatePassword(id, newPassword);

			user.setPassword(newPassword);

			if (updated >= 1) {

				return user;

			} else {

				throw new Exception();

			}

		} catch (Exception e) {

			throw new ArithmeticException("User profile is not updated...");

		}

	}

	@Override
	@Cacheable(value = cacheValue, key = "'CountUsersNative'")
	public long countUsersNative() {

		return userRepository.countUsersNative();

	}

	@Override
	@Cacheable(value = cacheValue, key = "'findByNameNative_' + #name")
	public List<UserResponseDTO> searchByNameNative(String name) {

		if (name == null) {

			throw new NullPointerException("False request...");

		}

		try {

			List<User> list = userRepository.searchByNameNative(name);

			if (list.isEmpty()) {

				throw new Exception();

			}

			return getUserResponse(list);

		} catch (Exception e) {

			throw new NoSuchElementException("No such user find at here...");

		}

	}

	@Override
	@Cacheable(value = cacheValue, key = "'findByUserNameNative_' + #userName")
	public UserResponseDTO findByUserNameNative(String userName) {

		if (userName == null) {

			throw new NullPointerException("False request...");

		}

		try {

			User list = userRepository.findByUserNameNative(userName).get();

			if (list == null) {

				throw new Exception();

			}

			return getUserResponse(list);

		} catch (Exception e) {

			throw new NoSuchElementException("No such user find at here...");

		}

	}

	@Override
	@Cacheable(value = cacheValue, key = "'findAllNative'")
	public List<UserResponseDTO> findAllNative() {

		try {

			List<User> list = userRepository.findAllNative();

			if (list.isEmpty()) {

				throw new Exception();

			}

			return getUserResponse(list);

		} catch (Exception e) {

			throw new NoSuchElementException("No such user find at here...");

		}

	}

	@Override
	@Cacheable(value = cacheValue, key = "'findAllUserOrderByNameDesc'")
	public List<UserResponseDTO> getAllUsersOrderByNameDesc() {

		try {

			List<User> list = userRepository.getAllUsersOrderByNameDesc();

			if (list.isEmpty()) {

				throw new Exception();

			}

			return getUserResponse(list);

		} catch (Exception e) {

			throw new NoSuchElementException("No such user find at here...");

		}

	}

	@Override
	@Cacheable(value = cacheValue, key = "'findUsersPagination_' + #pageable")
	public Page<User> getUsersWithPagination(Pageable pageable) {

		return (userRepository.findAll(pageable));
	}

	@Override
	@Cacheable(value = cacheValue, key = "'findAllUsersByNameAsc'")
	public List<UserResponseDTO> getAllUsersOrderByNameAsc() {

		try {

			List<User> list = userRepository.getAllUsersOrderByNameAsc();

			if (list.isEmpty()) {

				throw new Exception();

			}

			return getUserResponse(list);

		} catch (Exception e) {

			throw new NoSuchElementException("No such user find at here...");

		}

	}

	@Override
	@Cacheable(value = cacheValue, key = "'findAll'")
	public List<UserResponseDTO> getAllUsers() {

		try {

			List<User> list = userRepository.getAllUsers();

			if (list.isEmpty()) {

				throw new Exception();

			}

			return getUserResponse(list);

		} catch (Exception e) {

			throw new NoSuchElementException("No such user find at here...");

		}

	}

	@Override
	@Cacheable(value = cacheValue, key = "'CountByProfileImageId_' + #profileImageId")
	public long countByProfileImageId(String profileImageId) {

		if (profileImageId == null) {

			throw new NullPointerException("False request...");

		}

		return userRepository.countByProfileImageId(profileImageId);

	}

	@Override
	@Cacheable(value = cacheValue, key = "'CountByName_' + #name")
	public long countByName(String name) {

		if (name == null) {

			throw new NullPointerException("False request...");

		}

		return userRepository.countByName(name);

	}

	@Override
	@Cacheable(value = cacheValue, key = "'CountAllUser'")
	public long countAllUsers() {

		return userRepository.countAllUsers();

	}

	@Override
	@Cacheable(value = cacheValue, key = "'findUsersWithProfileImage'")
	public List<UserResponseDTO> findUsersWithProfileImage() {

		try {

			List<User> list = userRepository.findUsersWithProfileImage();

			if (list.isEmpty()) {

				throw new Exception();

			}

			return getUserResponse(list);

		} catch (Exception e) {

			throw new NoSuchElementException("No such user find at here...");

		}

	}

	@Override
	@Cacheable(value = cacheValue, key = "'findUsersWithoutProfileImage'")
	public List<UserResponseDTO> findUsersWithoutProfileImage() {

		try {

			List<User> list = userRepository.findUsersWithoutProfileImage();

			if (list.isEmpty()) {

				throw new Exception();

			}

			return getUserResponse(list);

		} catch (Exception e) {

			throw new NoSuchElementException("No such user find at here...");

		}

	}

	@Override
	@Cacheable(value = cacheValue, key = "'findByProfileImageId_' + #profileImageId")
	public List<UserResponseDTO> findByProfileImageId(String profileImageId) {

		if (profileImageId == null) {

			throw new NullPointerException("False request...");

		}

		try {

			List<User> list = userRepository.findByProfileImageId(profileImageId);

			if (list.isEmpty()) {

				throw new Exception();

			}

			return getUserResponse(list);

		} catch (Exception e) {

			throw new NoSuchElementException("No such user find at here...");

		}

	}

	@Override
	@Cacheable(value = cacheValue, key = "'findByUsersKeyWord_' + #keyword")
	public List<UserResponseDTO> searchUsers(String keyWord) {

		if (keyWord == null) {

			throw new NullPointerException("Falsee request...");

		}

		try {

			List<User> list = userRepository.searchUsers(keyWord);

			if (list.isEmpty()) {

				throw new Exception();

			}

			return getUserResponse(list);

		} catch (Exception e) {

			throw new NoSuchElementException("No such user find at here...");

		}

	}

	@Override
	@Cacheable(value = cacheValue, key = "'findByUserNamePrefix_' + #userNamePrefix")
	public List<UserResponseDTO> searchByUserName(String userNamePrefix) {

		if (userNamePrefix == null) {

			throw new NoSuchElementException("False reqeust....");

		}

		try {

			List<User> list = userRepository.searchByUserName(userNamePrefix);

			if (list.isEmpty()) {

				throw new Exception();

			}

			return getUserResponse(list);

		} catch (Exception e) {

			throw new NoSuchElementException("No such user find at here...");

		}

	}

	@Override
	@Cacheable(value = cacheValue, key = "'findByName_' + #name")
	public List<UserResponseDTO> searchByName(String name) {

		if (name == null) {

			throw new NullPointerException("False request...");

		}

		try {

			List<User> list = userRepository.searchByName(name);

			if (list.isEmpty()) {

				throw new Exception();

			}

			return getUserResponse(list);

		} catch (Exception e) {

			throw new NoSuchElementException("No such user find at here...");

		}

	}

	@Override
	@Cacheable(value = cacheValue, key = "'findByNamePrefix_' + #namePrefix")
	public List<UserResponseDTO> findByNameIgnoreCase(String namePrefix) {

		if (namePrefix == null) {

			throw new NullPointerException("False request....");

		}

		try {

			List<User> list = userRepository.findByNameIgnoreCase(namePrefix);

			if (list.isEmpty()) {

				throw new Exception();

			}

			return getUserResponse(list);

		} catch (Exception e) {

			throw new NoSuchElementException("No such user find at here...");

		}

	}

	@Override
	@Cacheable(value = cacheValue, key = "'findByexistsUserName_' + #userName")
	public boolean existsByUserName(String userName) {

		if (userName == null) {

			throw new NoSuchElementException("False request...");

		}

		return userRepository.existsByUserName(userName);

	}

	@Override
	@Cacheable(value = cacheValue, key = "'findByUserNameIgnoreCase_' + #userName")
	public UserResponseDTO findByUserNameIgnoreCase(String userName) {

		if (userName == null) {

			throw new NullPointerException("False request...");

		}

		try {

			User list = userRepository.findByUserNameIgnoreCase(userName).get();

			if (list == null) {

				throw new Exception();

			}

			return getUserResponse(list);

		} catch (Exception e) {

			throw new NoSuchElementException("No such user find at here...");

		}

	}

	@Override
	@Cacheable(value = cacheValue, key = "'findByUserName_' + #userName")
	public UserResponseDTO findByUserName(String userName) {

		if (userName == null) {

			throw new NullPointerException("False request...");

		}

		try {

			User list = userRepository.findByUserName(userName).get();

			if (list == null) {

				throw new Exception();

			}

			return getUserResponse(list);

		} catch (Exception e) {

			throw new NoSuchElementException("No such user find at here...");

		}

	}

	@Override
	@Cacheable(value = cacheValue, key = "'findByUserId_' + #id")
	public UserResponseDTO findUserById(String id) {

		if (id == null) {

			throw new NullPointerException("False request...");

		}

		try {

			User list = userRepository.findUserById(id).get();

			if (list == null) {

				throw new Exception();

			}

			return getUserResponse(list);

		} catch (Exception e) {

			throw new NoSuchElementException("No such user find at here...");

		}

	}

	@Override
	@Caching(evict = { @CacheEvict(value = cacheValue, allEntries = true),
			@CacheEvict(value = "UserContact", allEntries = true), @CacheEvict(value = "UserGender", allEntries = true),
			@CacheEvict(value = "UserLocation", allEntries = true),
			@CacheEvict(value = "UserTypes", allEntries = true) })
	public boolean deleteUserById(String id, String userId) {

		if (id == null || userId == null) {

			throw new NoSuchElementException("False request...");

		}

		try {

			User _user = userRepository.findById(id).get();

			if (_user == null) {

				throw new Exception();

			}

			if (!_user.getId().equals(userId)) {

				throw new Exception();

			}

		} catch (Exception e) {

			throw new NoSuchElementException("No such user find at here...");

		}

		return cleaner.removeUserById(id);

	}

	@Override
	@Caching(evict = { @CacheEvict(value = cacheValue, allEntries = true),
			@CacheEvict(value = "UserContact", allEntries = true), @CacheEvict(value = "UserGender", allEntries = true),
			@CacheEvict(value = "UserLocation", allEntries = true),
			@CacheEvict(value = "UserTypes", allEntries = true) })
	public boolean deleteByProfileImageId(String id, String userId) {

		if (id == null || userId == null) {

			throw new NoSuchElementException("False request...");

		}

		try {

			List<User> _user = userRepository.findByProfileImageId(id);

			if (_user == null || _user.isEmpty()) {

				throw new Exception();

			}

		} catch (Exception e) {

			throw new NoSuchElementException("No such user find at here...");

		}

		return cleaner.deleteByProfileImageId(id);

	}

	@Override
	@Caching(evict = { @CacheEvict(value = cacheValue, allEntries = true),
			@CacheEvict(value = "UserContact", allEntries = true), @CacheEvict(value = "UserGender", allEntries = true),
			@CacheEvict(value = "UserLocation", allEntries = true),
			@CacheEvict(value = "UserTypes", allEntries = true) })
	public boolean deleteUsersWithoutProfileImage(String userId) {

		if (userId == null) {

			throw new NoSuchElementException("False request...");

		}

		return cleaner.deleteUsersWithoutProfileImage();
	}

	@Caching(evict = { @CacheEvict(value = cacheValue, allEntries = true),
			@CacheEvict(value = "UserContact", allEntries = true), @CacheEvict(value = "UserGender", allEntries = true),
			@CacheEvict(value = "UserLocation", allEntries = true),
			@CacheEvict(value = "UserTypes", allEntries = true) })
	private String uploadProfileImage(MultipartFile file, String userName) {
		try {
			String bucketName = supabaseProperties.getBucket().getProfile();
			String fileName = userName;

			// Convert MultipartFile to bytes
			byte[] fileBytes = file.getBytes();

			// Upload to Supabase
			String fileUrl = supabaseStorageService.uploadFile(bucketName, fileName, fileBytes, file.getContentType());

			return fileUrl;
		} catch (Exception e) {
			throw new RuntimeException("Failed to upload profile image: " + e.getMessage());
		}
	}

	// =================================================================
	// PAGINATED LOOKUPS
	// =================================================================

	@Override
	@Cacheable(value = cacheValue, key = "'getAllUsersPaged_p' + #pageable.pageNumber + '_s' + #pageable.pageSize")
	public PageResponseDTO<UserResponseDTO> getAllUsersPaged(Pageable pageable) {
		try {
			Page<User> page = userRepository.getAllUsersPaged(pageable);
			if (page.isEmpty()) {
				throw new Exception();
			}
			return toPageResponse(page);
		} catch (Exception e) {
			throw new NoSuchElementException("No such user find at here...");
		}
	}

	@Override
	@Cacheable(value = cacheValue, key = "'getAllUsersOrderByNameAscPaged_p' + #pageable.pageNumber + '_s' + #pageable.pageSize")
	public PageResponseDTO<UserResponseDTO> getAllUsersOrderByNameAscPaged(Pageable pageable) {
		try {
			Page<User> page = userRepository.getAllUsersOrderByNameAscPaged(pageable);
			if (page.isEmpty()) {
				throw new Exception();
			}
			return toPageResponse(page);
		} catch (Exception e) {
			throw new NoSuchElementException("No such user find at here...");
		}
	}

	@Override
	@Cacheable(value = cacheValue, key = "'getAllUsersOrderByNameDescPaged_p' + #pageable.pageNumber + '_s' + #pageable.pageSize")
	public PageResponseDTO<UserResponseDTO> getAllUsersOrderByNameDescPaged(Pageable pageable) {
		try {
			Page<User> page = userRepository.getAllUsersOrderByNameDescPaged(pageable);
			if (page.isEmpty()) {
				throw new Exception();
			}
			return toPageResponse(page);
		} catch (Exception e) {
			throw new NoSuchElementException("No such user find at here...");
		}
	}

	@Override
	@Cacheable(value = cacheValue, key = "'findAllNativePaged_p' + #pageable.pageNumber + '_s' + #pageable.pageSize")
	public PageResponseDTO<UserResponseDTO> findAllNativePaged(Pageable pageable) {
		try {
			Page<User> page = userRepository.findAllNativePaged(pageable);
			if (page.isEmpty()) {
				throw new Exception();
			}
			return toPageResponse(page);
		} catch (Exception e) {
			throw new NoSuchElementException("No such user find at here...");
		}
	}

	@Override
	@Cacheable(value = cacheValue, key = "'searchUsersPaged_' + #keyWord + '_p' + #pageable.pageNumber + '_s' + #pageable.pageSize")
	public PageResponseDTO<UserResponseDTO> searchUsersPaged(String keyWord, Pageable pageable) {
		if (keyWord == null) {
			throw new NullPointerException("False request...");
		}
		try {
			Page<User> page = userRepository.searchUsersWithPagination(keyWord, pageable);
			if (page.isEmpty()) {
				throw new Exception();
			}
			return toPageResponse(page);
		} catch (Exception e) {
			throw new NoSuchElementException("No such user find at here...");
		}
	}

	@Override
	@Cacheable(value = cacheValue, key = "'searchByNamePaged_' + #name + '_p' + #pageable.pageNumber + '_s' + #pageable.pageSize")
	public PageResponseDTO<UserResponseDTO> searchByNamePaged(String name, Pageable pageable) {
		if (name == null) {
			throw new NullPointerException("False request...");
		}
		try {
			Page<User> page = userRepository.searchByNamePaged(name, pageable);
			if (page.isEmpty()) {
				throw new Exception();
			}
			return toPageResponse(page);
		} catch (Exception e) {
			throw new NoSuchElementException("No such user find at here...");
		}
	}

	@Override
	@Cacheable(value = cacheValue, key = "'searchByNameNativePaged_' + #name + '_p' + #pageable.pageNumber + '_s' + #pageable.pageSize")
	public PageResponseDTO<UserResponseDTO> searchByNameNativePaged(String name, Pageable pageable) {
		if (name == null) {
			throw new NullPointerException("False request...");
		}
		try {
			Page<User> page = userRepository.searchByNameNativePaged(name, pageable);
			if (page.isEmpty()) {
				throw new Exception();
			}
			return toPageResponse(page);
		} catch (Exception e) {
			throw new NoSuchElementException("No such user find at here...");
		}
	}

	@Override
	@Cacheable(value = cacheValue, key = "'searchByUserNamePaged_' + #userNamePrefix + '_p' + #pageable.pageNumber + '_s' + #pageable.pageSize")
	public PageResponseDTO<UserResponseDTO> searchByUserNamePaged(String userNamePrefix, Pageable pageable) {
		if (userNamePrefix == null) {
			throw new NullPointerException("False request...");
		}
		try {
			Page<User> page = userRepository.searchByUserNamePaged(userNamePrefix, pageable);
			if (page.isEmpty()) {
				throw new Exception();
			}
			return toPageResponse(page);
		} catch (Exception e) {
			throw new NoSuchElementException("No such user find at here...");
		}
	}

	@Override
	@Cacheable(value = cacheValue, key = "'findByNameIgnoreCasePaged_' + #namePrefix + '_p' + #pageable.pageNumber + '_s' + #pageable.pageSize")
	public PageResponseDTO<UserResponseDTO> findByNameIgnoreCasePaged(String namePrefix, Pageable pageable) {
		if (namePrefix == null) {
			throw new NullPointerException("False request...");
		}
		try {
			Page<User> page = userRepository.findByNameIgnoreCasePaged(namePrefix, pageable);
			if (page.isEmpty()) {
				throw new Exception();
			}
			return toPageResponse(page);
		} catch (Exception e) {
			throw new NoSuchElementException("No such user find at here...");
		}
	}

	@Override
	@Cacheable(value = cacheValue, key = "'findByProfileImageIdPaged_' + #profileImageId + '_p' + #pageable.pageNumber + '_s' + #pageable.pageSize")
	public PageResponseDTO<UserResponseDTO> findByProfileImageIdPaged(String profileImageId, Pageable pageable) {
		if (profileImageId == null) {
			throw new NullPointerException("False request...");
		}
		try {
			Page<User> page = userRepository.findByProfileImageIdPaged(profileImageId, pageable);
			if (page.isEmpty()) {
				throw new Exception();
			}
			return toPageResponse(page);
		} catch (Exception e) {
			throw new NoSuchElementException("No such user find at here...");
		}
	}

	@Override
	@Cacheable(value = cacheValue, key = "'findUsersWithProfileImagePaged_p' + #pageable.pageNumber + '_s' + #pageable.pageSize")
	public PageResponseDTO<UserResponseDTO> findUsersWithProfileImagePaged(Pageable pageable) {
		try {
			Page<User> page = userRepository.findUsersWithProfileImagePaged(pageable);
			if (page.isEmpty()) {
				throw new Exception();
			}
			return toPageResponse(page);
		} catch (Exception e) {
			throw new NoSuchElementException("No such user find at here...");
		}
	}

	@Override
	@Cacheable(value = cacheValue, key = "'findUsersWithoutProfileImagePaged_p' + #pageable.pageNumber + '_s' + #pageable.pageSize")
	public PageResponseDTO<UserResponseDTO> findUsersWithoutProfileImagePaged(Pageable pageable) {
		try {
			Page<User> page = userRepository.findUsersWithoutProfileImagePaged(pageable);
			if (page.isEmpty()) {
				throw new Exception();
			}
			return toPageResponse(page);
		} catch (Exception e) {
			throw new NoSuchElementException("No such user find at here...");
		}
	}

	// =================================================================
	// HELPER — converts Page<User> → PageResponseDTO<UserResponseDTO>
	// =================================================================
	private PageResponseDTO<UserResponseDTO> toPageResponse(Page<User> page) {
		List<UserResponseDTO> enriched = getUserResponse(page.getContent());
		Page<UserResponseDTO> dtoPage = new PageImpl<>(enriched, page.getPageable(), page.getTotalElements());
		return new PageResponseDTO<>(dtoPage);
	}

	private ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor();

	private UserResponseDTO getUserResponse(User user) {

		List<User> list = new ArrayList<>();

		list.add(user);

		return getUserResponse(list).get(0);

	}

	private List<UserResponseDTO> getUserResponse(List<User> list) {

		List<UserResponseDTO> responses = new ArrayList<>();

		CompletableFuture<List<String>> usersIdListFuture = CompletableFuture
				.supplyAsync(() -> list.stream().distinct().map(User::getId).collect(Collectors.toList()), executor);

		CompletableFuture<Map<String, UserTypes>> userTypesMapFuture = usersIdListFuture.thenApplyAsync(usersId -> {

			if (usersId.isEmpty()) {

				return new HashMap<>();

			}

			return typeRepository.findByUserIdIn(usersId).stream().filter(Objects::nonNull)
					.collect(Collectors.toMap(UserTypes::getUserId, Function.identity()));

		}, executor);

		CompletableFuture<Map<String, UserContact>> userContactMapFuture = usersIdListFuture.thenApplyAsync(usersId -> {

			if (usersId.isEmpty()) {

				return new HashMap<>();

			}

			return contactRepository.findByUserIdIn(usersId).stream().filter(Objects::nonNull)
					.collect(Collectors.toMap(UserContact::getUserId, Function.identity()));

		}, executor);

		CompletableFuture<Map<String, UserLocation>> locationMapFuture = usersIdListFuture.thenApplyAsync(usersId -> {

			if (usersId.isEmpty()) {

				return new HashMap<>();

			}

			return locationRepository.findByUserIdIn(usersId).stream().filter(Objects::nonNull)
					.collect(Collectors.toMap(UserLocation::getUserId, Function.identity()));

		}, executor);

		CompletableFuture<Map<String, UserGender>> userGenderMapFuture = usersIdListFuture.thenApplyAsync(usersId -> {

			if (usersId.isEmpty()) {

				return new HashMap<>();

			}

			return genderRepository.findByUserIdIn(usersId).stream().filter(Objects::nonNull)
					.collect(Collectors.toMap(UserGender::getUserId, Function.identity()));

		}, executor);

		CompletableFuture.allOf(usersIdListFuture, userTypesMapFuture, userContactMapFuture, locationMapFuture,
				userGenderMapFuture).join();

		Map<String, UserTypes> userTypesMap = userTypesMapFuture.join();
		Map<String, UserContact> userContactMap = userContactMapFuture.join();
		Map<String, UserLocation> locationMap = locationMapFuture.join();
		Map<String, UserGender> userGenderMap = userGenderMapFuture.join();

		for (User user : list) {

			try {

				UserResponseDTO response = new UserResponseDTO();

				response.setId(user.getId());
				response.setName(user.getName());
				response.setPassword(user.getPassword());
				response.setUserName(user.getUserName());
				response.setProfileImageId(user.getProfileImageId());

				try {

					UserContact contact = userContactMap.get(user.getId());

					if (contact == null) {

						throw new Exception();

					}

					response.setContactInfoId(contact.getId());
					response.setEmail(contact.getEmail());
					response.setPhone(contact.getPhone());

				} catch (Exception e) {

					System.out.println(e.getMessage());

				}

				try {

					UserLocation location = locationMap.get(user.getId());

					if (location == null) {

						throw new Exception();

					}

					response.setUserLocationId(location.getId());
					response.setLocationName(location.getLocationName());
					response.setLattitude(location.getLattitude());
					response.setLongititude(location.getLongititude());

				} catch (Exception e) {

					System.out.println(e.getMessage());

				}

				try {

					UserGender gender = userGenderMap.get(user.getId());

					response.setId(gender.getId());
					response.setGender(gender.getGender());

				} catch (Exception e) {

					System.out.println(e.getMessage());

				}

				try {

					UserTypes types = userTypesMap.get(user.getId());

					response.setUserTypeId(types.getId());
					response.setType(types.getType());

				} catch (Exception e) {

					System.out.println(e.getMessage());

				}

				responses.add(response);

			} catch (Exception e) {

				System.out.println(e.getMessage());

			}

		}

		return responses;

	}

}
