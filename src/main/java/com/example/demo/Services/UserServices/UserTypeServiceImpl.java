package com.example.demo.Services.UserServices;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;

import com.example.demo.Models.UserModels.User;
import com.example.demo.Models.UserModels.UserTypes;
import com.example.demo.Repositories.UserRepositories.UserRepository;
import com.example.demo.Repositories.UserRepositories.UserTypeRepository;

import CyclicCleaner.Cleaner;

@Service
public class UserTypeServiceImpl implements UserTypeService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private UserTypeRepository typeRepository;

	@Autowired
	private Cleaner cleaner;
	
	private static final String cacheValue = "UserTypes";

	@Override
	@Caching(evict = { @CacheEvict(value = cacheValue, allEntries = true),
			@CacheEvict(value = "User", allEntries = true) })
	public UserTypes addUserTypes(UserTypes types, String userId) {

		if (types == null || userId == null || !types.getUserId().equals(userId)) {

			throw new NullPointerException("False request....");

		}

		User user = null;

		try {

			user = userRepository.findById(userId).get();

			if (user == null) {

				throw new Exception();

			}

		} catch (Exception e) {

			throw new NoSuchElementException("No such user find at here....");

		}

		try {

			UserTypes userType = typeRepository.findByUserId(userId);

			if (userType != null) {

				throw new ArithmeticException();

			}

		} catch (ArithmeticException e) {

			throw new ArithmeticException("This user types is not setted...");

		} catch (Exception e) {

		}

		try {

			if (types.getType() == null) {

				throw new Exception();

			}

		} catch (Exception e) {

			throw new ArithmeticException("User must have a type....");

		}

		types = typeRepository.save(types);

		if (types == null) {

			throw new ArithmeticException("User types not setted...");

		}

		return types;
	}

	@Override
	@Caching(evict = { @CacheEvict(value = cacheValue, allEntries = true),
			@CacheEvict(value = "User", allEntries = true) })
	public UserTypes updateUserTypes(UserTypes types, String userId, String id) {

		if (id == null || types == null || userId == null || !types.getUserId().equals(userId)) {

			throw new NullPointerException("False request....");

		}

		User user = null;

		try {

			user = userRepository.findById(userId).get();

			if (user == null) {

				throw new Exception();

			}

		} catch (Exception e) {

			throw new NoSuchElementException("No such user find at here....");

		}

		try {

			UserTypes userTypes = typeRepository.findById(id).get();

			if (userTypes == null) {

				throw new Exception();

			}

			if (!userTypes.getUserId().equals(userId)) {

				throw new Exception();

			}

		} catch (Exception e) {

			throw new NoSuchElementException("No such type find at here...");

		}

		try {

			UserTypes userType = typeRepository.findByUserId(userId);

			if (userType != null) {

				if (!userType.getId().equals(id)) {

					throw new ArithmeticException();

				}

			}

		} catch (ArithmeticException e) {

			throw new ArithmeticException("This user types is not setted...");

		} catch (Exception e) {

		}

		try {

			if (types.getType() == null) {

				throw new Exception();

			}

		} catch (Exception e) {

			throw new ArithmeticException("User must have a type....");

		}

		types.setId(id);

		types = typeRepository.save(types);

		if (types == null) {

			throw new ArithmeticException("User types not setted...");

		}

		return types;
	}

	@Override
	@Cacheable(value = cacheValue, key = "'findById_' + #id")
	public UserTypes findById(String id) {

		if (id == null) {

			throw new NullPointerException("False request...");

		}

		try {

			UserTypes types = typeRepository.findById(id).get();

			if (types == null) {

				throw new Exception();

			}

			return types;

		} catch (Exception e) {

			throw new NoSuchElementException("No such types find at here...");

		}

	}

	@Override
	@Cacheable(value = cacheValue, key = "'findAll'")
	public List<UserTypes> findAll() {

		try {

			List<UserTypes> list = typeRepository.findAll();

			if (list == null || list.isEmpty()) {

				throw new Exception();

			}

			return list;

		} catch (Exception e) {

			throw new NoSuchElementException("No such types find at here...");

		}

	}

	@Override
	@Cacheable(value = cacheValue, key = "'findByUserId_' + #userId")
	public UserTypes findByUserId(String userId) {

		if (userId == null) {

			throw new NullPointerException("False request...");

		}

		try {

			UserTypes types = typeRepository.findByUserId(userId);

			if (types == null) {

				throw new Exception();

			}

			return types;

		} catch (Exception e) {

			throw new NoSuchElementException("No such types find at here...");

		}
	}

	@Override
	@Cacheable(value = cacheValue, key = "'findByTypePrefix_' + #type")
	public List<UserTypes> findByTypeContainingIgnoreCase(String type) {
		
		if(type == null) {
			
			throw new NullPointerException("False request...");
			
		}
		
		try {

			List<UserTypes> list = typeRepository.findByTypeContainingIgnoreCase(type);

			if (list == null || list.isEmpty()) {

				throw new Exception();

			}

			return list;

		} catch (Exception e) {

			throw new NoSuchElementException("No such types find at here...");

		}
	}

	@Override
	@Cacheable(value = cacheValue, key = "'findByExistsByUserIdNative_' + #userId")
	public boolean existsByUserIdNative(String userId) {
		
		if(userId == null) {
			
			throw new NullPointerException("False request....");
			
		}
		
		return typeRepository.existsByUserIdNative(userId);
	}

	@Override
	@Caching(evict = { @CacheEvict(value = cacheValue, allEntries = true),
			@CacheEvict(value = "User", allEntries = true) })
	public boolean deleteByUserIdNative(String userId) {

		if (userId == null) {

			throw new NullPointerException("False request...");

		}

		User user = null;

		try {

			user = userRepository.findById(userId).get();

			if (user == null) {

				throw new Exception();

			}

		} catch (Exception e) {

			throw new NoSuchElementException("No such user find at here....");

		}

		return cleaner.removeUserTypeByUserId(userId);
	}

	@Override
	@Caching(evict = { @CacheEvict(value = cacheValue, allEntries = true),
			@CacheEvict(value = "User", allEntries = true) })
	public boolean deleteUserType(String id, String userId) {

		if (id == null || userId == null) {

			throw new NullPointerException("False request...");

		}

		User user = null;

		try {

			user = userRepository.findById(userId).get();

			if (user == null) {

				throw new Exception();

			}

		} catch (Exception e) {

			throw new NoSuchElementException("No such user find at here....");

		}

		try {

			UserTypes userTypes = typeRepository.findById(id).get();

			if (userTypes == null) {

				throw new Exception();

			}

			if (!userTypes.getUserId().equals(userId)) {

				throw new Exception();

			}

		} catch (Exception e) {

			throw new NoSuchElementException("No such type find at here...");

		}

		return cleaner.deleteUserType(id);
	}

}
