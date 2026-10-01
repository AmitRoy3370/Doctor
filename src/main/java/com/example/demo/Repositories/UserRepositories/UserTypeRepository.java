package com.example.demo.Repositories.UserRepositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.Models.UserModels.UserTypes;

@Repository
public interface UserTypeRepository extends JpaRepository<UserTypes, String> {

    @Query(value = "SELECT * FROM user_types WHERE user_id = :userId", nativeQuery = true)
    UserTypes findByUserId(@Param("userId") String userId);

    @Query(value = """
            SELECT DISTINCT ut.*
            FROM user_types ut
            JOIN user_types_roles utr ON utr.user_types_id = ut.id
            WHERE LOWER(utr.type) LIKE LOWER(CONCAT('%', :type, '%'))
            """, nativeQuery = true)
    List<UserTypes> findByTypeContainingIgnoreCase(@Param("type") String type);

    List<UserTypes> findByUserIdIn(List<String> usersId);
    
    // Deletes both child rows and parent row safely
    @Modifying
    @Transactional
    @Query(value = "DELETE FROM user_types_roles WHERE user_types_id IN "
                 + "(SELECT id FROM user_types WHERE user_id = :userId)", nativeQuery = true)
    int deleteChildRolesByUserId(@Param("userId") String userId);

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM user_types WHERE user_id = :userId", nativeQuery = true)
    int deleteByUserIdNative(@Param("userId") String userId);

    @Query(value = "SELECT EXISTS(SELECT 1 FROM user_types WHERE user_id = :userId)", nativeQuery = true)
    boolean existsByUserIdNative(@Param("userId") String userId);
}