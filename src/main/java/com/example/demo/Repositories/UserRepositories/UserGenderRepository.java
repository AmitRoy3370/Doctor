package com.example.demo.Repositories.UserRepositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.demo.Models.UserModels.UserGender;

@Repository
public interface UserGenderRepository extends JpaRepository<UserGender, String> {

	@Query(value = "SELECT * FROM user_gender WHERE user_id = :userId", nativeQuery = true)
	UserGender findUserGenderByUserIdNative(@Param("userId") String userId);

	List<UserGender> findByUserIdIn(List<String> usersId);

	// 👇 Added CAST to fix the type mismatch 👇
	@Query(value = "SELECT * FROM user_gender WHERE CAST(gender AS varchar) = :gender", nativeQuery = true)
	List<UserGender> findByGender(@Param("gender") String gender);

	@Query(value = "SELECT gender, COUNT(*) AS total FROM user_gender GROUP BY gender", nativeQuery = true)
	List<Object[]> countGroupedByGender();

	// 👇 Added CAST to fix the type mismatch 👇
	@Query(value = "SELECT * FROM user_gender WHERE CAST(gender AS varchar) = :gender AND user_id LIKE :pattern", nativeQuery = true)
	List<UserGender> findByGenderAndUserIdPattern(@Param("gender") String gender, @Param("pattern") String pattern);

	@Query(value = "SELECT * FROM user_gender", nativeQuery = true)
	List<UserGender> findAll();

	@Query(value = "SELECT EXISTS(SELECT 1 FROM user_gender WHERE user_id = :userId)", nativeQuery = true)
	boolean existsByUserIdNative(@Param("userId") String userId);

	@Query(value = "DELETE FROM user_gender WHERE user_id = :userId RETURNING *", nativeQuery = true)
	UserGender deleteByUserIdNative(@Param("userId") String userId);
}
