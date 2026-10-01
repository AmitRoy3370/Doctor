package com.example.demo.Services.UserServices;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;

import com.example.demo.Models.UserModels.User;
import com.example.demo.Models.UserModels.UserLocation;
import com.example.demo.Repositories.UserRepositories.UserLocationRepository;
import com.example.demo.Repositories.UserRepositories.UserRepository;
import com.example.demo.Validators.AddressValidator;

import CyclicCleaner.Cleaner;

@Service
public class UserLocationServiceImpl implements UserLocationService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private UserLocationRepository locationRepository;

	private AddressValidator adressValidator = new AddressValidator();
	
	@Autowired
	private Cleaner cleaner;
	
	private static final String cacheValue = "UserLocation";

	@Override
	@Caching(evict = { @CacheEvict(value = cacheValue, allEntries = true),
			@CacheEvict(value = "User", allEntries = true) })
	public UserLocation addUserLocation(UserLocation userLocation, String userId) {

		if (userLocation == null || userId == null || !userLocation.getUserId().equals(userId)) {

			throw new NullPointerException("False request....");

		}

		try {

			User user = userRepository.findById(userId).get();

			if (user == null) {

				throw new Exception();

			}

		} catch (Exception e) {

			throw new NoSuchElementException("No such user find at here...");

		}

		try {

			UserLocation location = locationRepository.findByUserId(userId);

			if (location != null) {

				throw new Exception();

			}

		} catch (ArithmeticException e) {

			throw new ArithmeticException("This user's location is already setted....");

		} catch (Exception e) {

		}

		try {

			if (userLocation.getLocationName() == null || !adressValidator.isValidAddress(userLocation.getLocationName())) {

				throw new Exception();

			}
			
		} catch (Exception e) {

			throw new ArithmeticException("location data is not valid...");

		}

		userLocation = locationRepository.save(userLocation);

		if (userLocation == null) {

			throw new ArithmeticException("location is not saved...");

		}

		return userLocation;
	}

	@Override
	@Caching(evict = { @CacheEvict(value = cacheValue, allEntries = true),
			@CacheEvict(value = "User", allEntries = true) })
	public UserLocation updateUserLocation(UserLocation userLocation, String userId, String id) {

		if (userLocation == null || userId == null || !userLocation.getUserId().equals(userId)) {

			throw new NullPointerException("False request....");

		}

		try {

			User user = userRepository.findById(userId).get();

			if (user == null) {

				throw new Exception();

			}

		} catch (Exception e) {

			throw new NoSuchElementException("No such user find at here...");

		}

		try {

			UserLocation location = locationRepository.findById(id).get();

			if (location == null) {

				throw new Exception();

			}

			if (!location.getUserId().equals(userId)) {

				throw new Exception();

			}

		} catch (Exception e) {

			throw new NoSuchElementException("No such location find at here...");

		}

		try {

			UserLocation location = locationRepository.findByUserId(userId);

			if (location != null) {

				if (location.getId().equals(id)) {

				} else {

					throw new Exception();

				}

			}

		} catch (ArithmeticException e) {

			throw new ArithmeticException("This user's location is already setted....");

		} catch (Exception e) {

		}

		try {

			if (userLocation.getLocationName() == null || !adressValidator.isValidAddress(userLocation.getLocationName())) {

				throw new Exception();

			}
			
		} catch (Exception e) {

			throw new ArithmeticException("location data is not valid...");

		}

		userLocation.setId(id);

		userLocation = locationRepository.save(userLocation);

		if (userLocation == null) {

			throw new ArithmeticException("location is not saved...");

		}

		return userLocation;
	}

	@Override
	@Cacheable(value = cacheValue, key = "'findById_' + #id")
	public UserLocation findById(String id) {
		
		if(id == null) {
			
			throw new NullPointerException("False request...");
			
		}
		
		try {
			
			UserLocation location = locationRepository.findById(id).get();
			
			if(location == null) {
				
				throw new Exception();
				
			}
			
			return location;
			
		} catch(Exception e) {
			
			throw new NoSuchElementException("No such location find at here...");
			
		}
		
	}

	@Override
	@Cacheable(value = cacheValue, key = "'findAll'")
	public List<UserLocation> findAll() {

		try {
			
			List<UserLocation> location = locationRepository.findAll();
			
			if(location == null || location.isEmpty()) {
				
				throw new Exception();
				
			}
			
			return location;
			
		} catch(Exception e) {
			
			throw new NoSuchElementException("No such location find at here...");
			
		}
		
	}

	@Override
	@Cacheable(value = cacheValue, key = "'findByUserId_' + #userId")
	public UserLocation findByUserId(String userId) {

		if(userId == null) {
			
			throw new NullPointerException("False request...");
			
		}
		
		try {
			
			UserLocation location = locationRepository.findByUserId(userId);
			
			if(location == null) {
				
				throw new Exception();
				
			}
			
			return location;
			
		} catch(Exception e) {
			
			throw new NoSuchElementException("No such location find at here...");
			
		}
		
	}

	@Override
	@Cacheable(value = cacheValue, key = "'findByLocationNamePrefix_' + #locationName")
	public List<UserLocation> findByLocationNameContainingIgnoreCase(String locationName) {

		if(locationName == null) {
			
			throw new NullPointerException("False request...");
			
		}
		
		try {
			
			List<UserLocation> location = locationRepository.findByLocationNameContainingIgnoreCase(locationName);
			
			if(location == null) {
				
				throw new Exception();
				
			}
			
			return location;
			
		} catch(Exception e) {
			
			throw new NoSuchElementException("No such location find at here...");
			
		}
		
	}

	@Override
	@Cacheable(value = cacheValue, key = "'findByLatLong_' + #lattitude + '_' + #longititude")
	public List<UserLocation> findByLattitudeOrLongititude(double lattitude, double longititude) {

		try {
			
			List<UserLocation> location = locationRepository.findByLattitudeOrLongititude(lattitude, longititude);
			
			if(location == null) {
				
				throw new Exception();
				
			}
			
			return location;
			
		} catch(Exception e) {
			
			throw new NoSuchElementException("No such location find at here...");
			
		}
	}

	@Override
	@Caching(evict = { @CacheEvict(value = cacheValue, allEntries = true),
			@CacheEvict(value = "User", allEntries = true) })
	public boolean deleteUserLocation(String id, String userId) {

		if (userId == null || id == null) {

			throw new NullPointerException("False request....");

		}

		try {

			User user = userRepository.findById(userId).get();

			if (user == null) {

				throw new Exception();

			}

		} catch (Exception e) {

			throw new NoSuchElementException("No such user find at here...");

		}

		try {

			UserLocation location = locationRepository.findById(id).get();

			if (location == null) {

				throw new Exception();

			}

			if (!location.getUserId().equals(userId)) {

				throw new Exception();

			}

		} catch (Exception e) {

			throw new NoSuchElementException("No such location find at here...");

		}

		return cleaner.deleteGender(id);
	}

}
