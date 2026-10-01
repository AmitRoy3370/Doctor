package com.example.demo.Services.UserServices;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;

import com.example.demo.Models.UserModels.User;
import com.example.demo.Models.UserModels.UserGender;
import com.example.demo.Repositories.UserRepositories.UserGenderRepository;
import com.example.demo.Repositories.UserRepositories.UserRepository;

import CyclicCleaner.Cleaner;

@Service
public class UserGenderServiceImpl implements UserGenderService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private UserGenderRepository genderRepository;

	@Autowired
	private Cleaner cleaner;

	private static final String cacheValue = "UserGender";
	
	@SuppressWarnings("deprecation")
	@Override
	@Caching(evict = { @CacheEvict(value = cacheValue, allEntries = true),
			@CacheEvict(value = "User", allEntries = true) })
	public UserGender addUserGender(UserGender gender, String userId) {

		if (gender == null || userId == null || gender.getUserId().equals(userId)) {

			throw new NullPointerException("False request...");

		}

		User user = null;

		try {

			user = userRepository.getById(userId);

			if (user == null) {

				throw new Exception();

			}

		} catch (Exception e) {

			throw new NoSuchElementException("No such user find at here....");

		}

		try {

			if (gender.getGender() == null) {

				throw new Exception();

			}

		} catch (Exception e) {

			throw new ArithmeticException("User must have a gender...");

		}

		try {

			UserGender _userGender = genderRepository.findUserGenderByUserIdNative(userId);

			if (_userGender == null) {

			} else {

				throw new ArithmeticException();

			}

		} catch (ArithmeticException e) {

			throw new ArithmeticException("This user gender is already setted...");

		} catch (Exception e) {

		}

		gender = genderRepository.save(gender);

		if (gender == null) {

			throw new ArithmeticException("Gender is not set....");

		}

		return gender;
	}

	@Override
	@Caching(evict = { @CacheEvict(value = cacheValue, allEntries = true),
			@CacheEvict(value = "User", allEntries = true) })
	public UserGender updateUserGender(UserGender gender, String userId, String id) {

		if (id == null || gender == null || userId == null || gender.getUserId().equals(userId)) {

			throw new NullPointerException("False request...");

		}

		UserGender userGender = null;

		try {

			userGender = genderRepository.findById(id).get();

			if (userGender == null) {

				throw new Exception();

			}

			if (!userGender.getUserId().equals(userId)) {

				throw new Exception();

			}

		} catch (Exception e) {

			throw new NoSuchElementException("no such gender find at here...");

		}

		User user = null;

		try {

			user = userRepository.getById(userId);

			if (user == null) {

				throw new Exception();

			}

		} catch (Exception e) {

			throw new NoSuchElementException("No such user find at here....");

		}

		try {

			if (gender.getGender() == null) {

				throw new Exception();

			}

		} catch (Exception e) {

			throw new ArithmeticException("User must have a gender...");

		}

		try {

			UserGender _userGender = genderRepository.findUserGenderByUserIdNative(userId);

			if (_userGender == null) {

			} else {

				if (_userGender.getId().equals(id)) {

				} else {

					throw new ArithmeticException();

				}

			}

		} catch (ArithmeticException e) {

			throw new ArithmeticException("This user gender is already setted...");

		} catch (Exception e) {

		}

		gender.setId(id);

		gender = genderRepository.save(gender);

		if (gender == null) {

			throw new ArithmeticException("Gender is not set....");

		}

		return gender;
	}

	@Override
	@Cacheable(value = cacheValue, key = "'findById_' + #id")
	public UserGender findById(String id) {
		
		if(id == null) {
			
			throw new NullPointerException("False request...");
			
		}
		
		try {
			
			UserGender gender = genderRepository.findById(id).get();
			
			if(gender == null) {
				
				throw new Exception();
				
			}
			
			return gender;
			
		} catch(Exception e) {
			
			throw new NoSuchElementException("No such gender find at here...");
			
		}
		
	}

	@Override
	@Cacheable(value = cacheValue, key = "'findAll'")
	public List<UserGender> findAll() {

		try {
			
			List<UserGender> gender = genderRepository.findAll();
			
			if(gender == null || gender.isEmpty()) {
				
				throw new Exception();
				
			}
			
			return gender;
			
		} catch(Exception e) {
			
			throw new NoSuchElementException("No such gender find at here...");
			
		}
	}

	@Override
	@Cacheable(value = cacheValue, key = "'findByUserIdNative_' + #userId")
	public UserGender findUserGenderByUserIdNative(String userId) {

		if(userId == null) {
			
			throw new NullPointerException("False request...");
			
		}
		
		try {
			
			UserGender gender = genderRepository.findUserGenderByUserIdNative(userId);
			
			if(gender == null) {
				
				throw new Exception();
				
			}
			
			return gender;
			
		} catch(Exception e) {
			
			throw new NoSuchElementException("No such gender find at here...");
			
		}
	}

	@Override
	@Cacheable(value = cacheValue, key = "'findByGender_' + #gender")
	public List<UserGender> findByGender(String gender) {

		if(gender == null) {
			
			throw new NullPointerException("False request...");
			
		}
		
		try {
			
			List<UserGender> list = genderRepository.findByGender(gender);
			
			if(list == null || list.isEmpty()) {
				
				throw new Exception();
				
			}
			
			return list;
			
		} catch(Exception e) {
			
			throw new NoSuchElementException("No such gender find at here...");
			
		}
	}

	@Override
	@Cacheable(value = cacheValue, key = "'findByCountGroupByGender'")
	public List<Object[]> countGroupedByGender() {
		
		return genderRepository.countGroupedByGender();
	}

	@Override
	@Cacheable(value = cacheValue, key = "'findByUserIdPrefeixAndGender_' + #gender + '_' + #userId")
	public List<UserGender> findByGenderAndUserIdPattern(String gender, String pattern) {

		if(pattern == null || gender == null) {
			
			throw new NullPointerException("False request...");
			
		}
		
		try {
			
			List<UserGender> list = genderRepository.findByGenderAndUserIdPattern(gender, pattern);
			
			if(list == null || list.isEmpty()) {
				
				throw new Exception();
				
			}
			
			return list;
			
		} catch(Exception e) {
			
			throw new NoSuchElementException("No such gender find at here...");
			
		}
	}

	@SuppressWarnings("deprecation")
	@Override
	@Cacheable(value = cacheValue, key = "'findByUserExistById_' + #userId")
	public boolean existsByUserIdNative(String userId) {

		if (userId == null) {

			throw new NullPointerException("False request...");

		}

		User user = null;

		try {

			user = userRepository.getById(userId);

			if (user == null) {

				throw new Exception();

			}

		} catch (Exception e) {

			throw new NoSuchElementException("No such user find at here....");

		}
		
		return genderRepository.existsByUserIdNative(userId);
		
	}

	@SuppressWarnings("deprecation")
	@Override
	@Caching(evict = { @CacheEvict(value = cacheValue, allEntries = true),
			@CacheEvict(value = "User", allEntries = true) })
	public boolean deleteByUserIdNative(String userId) {

		if (userId == null) {

			throw new NullPointerException("False request...");

		}

		User user = null;

		try {

			user = userRepository.getById(userId);

			if (user == null) {

				throw new Exception();

			}

		} catch (Exception e) {

			throw new NoSuchElementException("No such user find at here....");

		}
		return cleaner.deleteUserGenderByUserIdNative(userId);
	}

	@SuppressWarnings("deprecation")
	@Override
	@Caching(evict = { @CacheEvict(value = cacheValue, allEntries = true),
			@CacheEvict(value = "User", allEntries = true) })
	public boolean deleteUserGender(String id, String userId) {

		if (id == null || userId == null) {

			throw new NullPointerException("False request...");

		}

		User user = null;

		try {

			user = userRepository.getById(userId);

			if (user == null) {

				throw new Exception();

			}

		} catch (Exception e) {

			throw new NoSuchElementException("No such user find at here....");

		}

		UserGender userGender = null;

		try {

			userGender = genderRepository.findById(id).get();

			if (userGender == null) {

				throw new Exception();

			}

			if (!userGender.getUserId().equals(userId)) {

				throw new Exception();

			}

		} catch (Exception e) {

			throw new NoSuchElementException("no such gender find at here...");

		}

		return cleaner.deleteGender(id);

	}

}
