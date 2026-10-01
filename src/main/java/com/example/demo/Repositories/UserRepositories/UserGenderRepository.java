package com.example.demo.Repositories.UserRepositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.demo.Models.UserModels.UserGender;

@Repository
public interface UserGenderRepository extends JpaRepository<UserGender, String> {

	@Query(value = "SELECT * FROM UserGender WHERE user_id = :userId", nativeQuery = true)
	UserGender findUserGenderByUserIdNative(@Param("userId") String userId);

	List<UserGender> findByUserIdIn(List<String> usersId);
	
	@Query(value = "SELECT * FROM UserGender WHERE gender = :gender", nativeQuery = true)
	List<UserGender> findByGender(@Param("gender") String list);

	@Query(value = "SELECT gender, COUNT(*) AS total FROM UserGender GROUP BY gender", nativeQuery = true)
	List<Object[]> countGroupedByGender();

	@Query(value = "SELECT * FROM UserGender WHERE gender = :gender AND user_id LIKE :pattern", nativeQuery = true)
	List<UserGender> findByGenderAndUserIdPattern(@Param("gender") String gender, @Param("pattern") String pattern);

	@Query(value = "SELECT * FROM UserGender", nativeQuery = true)
	List<UserGender> findAll();

	@Query(value = "SELECT EXISTS(SELECT 1 FROM UserGender WHERE user_id = :userId)", nativeQuery = true)
	boolean existsByUserIdNative(@Param("userId") String userId);

	@Query(value = "DELETE FROM UserGender WHERE user_id = :userId RETURNING *", nativeQuery = true)
	UserGender deleteByUserIdNative(@Param("userId") String userId);
}