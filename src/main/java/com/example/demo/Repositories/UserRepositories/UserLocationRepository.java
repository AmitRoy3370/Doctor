package com.example.demo.Repositories.UserRepositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.demo.Models.UserModels.UserLocation;

@Repository
public interface UserLocationRepository extends JpaRepository<UserLocation, String> {

	@Query(value = "SELECT * FROM UserLocation WHERE user_id = :userId", nativeQuery = true)
	UserLocation findByUserId(@Param("userId") String userId);

	List<UserLocation> findByUserIdIn(List<String> usersId);
	
	@Query(value = "SELECT * FROM UserLocation WHERE LOWER(location_name) LIKE LOWER(CONCAT('%', :locationName, '%'))", nativeQuery = true)
	List<UserLocation> findByLocationNameContainingIgnoreCase(@Param("locationName") String locationName);

	@Query(value = "SELECT * FROM UserLocation WHERE lattitude = :lattitude OR longititude = :longititude", nativeQuery = true)
	List<UserLocation> findByLattitudeOrLongititude(@Param("lattitude") double lattitude,
			@Param("longititude") double longititude);

}