package com.example.demo.Repositories.UserRepositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.Models.UserModels.User;

@Repository
public interface UserRepository extends JpaRepository<User, String> {

    // =================================================================
    // SINGLE RESULT QUERIES (unchanged)
    // =================================================================

    @Query("SELECT u FROM User u WHERE u.id = :id")
    Optional<User> findUserById(@Param("id") String id);

    @Query("SELECT u FROM User u WHERE u.userName = :userName")
    Optional<User> findByUserName(@Param("userName") String userName);

    @Query("""
            SELECT u
            FROM User u
            WHERE LOWER(u.userName) = LOWER(:userName)
            """)
    Optional<User> findByUserNameIgnoreCase(@Param("userName") String userName);

    @Query("""
            SELECT CASE
                   WHEN COUNT(u) > 0 THEN true
                   ELSE false
                   END
            FROM User u
            WHERE u.userName = :userName
            """)
    boolean existsByUserName(@Param("userName") String userName);

    // =================================================================
    // LIST QUERIES (existing — kept for backward compatibility)
    // =================================================================

    @Query("SELECT u FROM User u WHERE u.name = :name")
    List<User> findByName(@Param("name") String name);

    @Query("""
            SELECT u
            FROM User u
            WHERE LOWER(u.name) = LOWER(:name)
            """)
    List<User> findByNameIgnoreCase(@Param("name") String name);

    @Query("""
            SELECT u
            FROM User u
            WHERE LOWER(u.name) LIKE LOWER(CONCAT('%', :name, '%'))
            """)
    List<User> searchByName(@Param("name") String name);

    @Query("""
            SELECT u
            FROM User u
            WHERE LOWER(u.userName) LIKE LOWER(CONCAT('%', :userName, '%'))
            """)
    List<User> searchByUserName(@Param("userName") String userName);

    @Query("""
            SELECT u
            FROM User u
            WHERE LOWER(u.userName) LIKE LOWER(CONCAT('%', :keyword, '%'))
               OR LOWER(u.name) LIKE LOWER(CONCAT('%', :keyword, '%'))
            """)
    List<User> searchUsers(@Param("keyword") String keyword);

    @Query("""
            SELECT u
            FROM User u
            WHERE u.profileImageId = :profileImageId
            """)
    List<User> findByProfileImageId(@Param("profileImageId") String profileImageId);

    @Query("""
            SELECT u
            FROM User u
            WHERE u.profileImageId IS NULL
            """)
    List<User> findUsersWithoutProfileImage();

    @Query("""
            SELECT u
            FROM User u
            WHERE u.profileImageId IS NOT NULL
            """)
    List<User> findUsersWithProfileImage();

    @Query("SELECT u FROM User u")
    List<User> getAllUsers();

    @Query("""
            SELECT u
            FROM User u
            ORDER BY u.name ASC
            """)
    List<User> getAllUsersOrderByNameAsc();

    @Query("""
            SELECT u
            FROM User u
            ORDER BY u.name DESC
            """)
    List<User> getAllUsersOrderByNameDesc();

    // =================================================================
    // PAGINATED QUERIES (NEW — mirror of the list queries above)
    // =================================================================

    @Query("""
            SELECT u
            FROM User u
            ORDER BY u.name ASC
            """)
    Page<User> getUsersWithPagination(Pageable pageable);

    @Query("""
            SELECT u
            FROM User u
            WHERE LOWER(u.name) LIKE LOWER(CONCAT('%', :keyword, '%'))
               OR LOWER(u.userName) LIKE LOWER(CONCAT('%', :keyword, '%'))
            """)
    Page<User> searchUsersWithPagination(@Param("keyword") String keyword, Pageable pageable);

    @Query("SELECT u FROM User u")
    Page<User> getAllUsersPaged(Pageable pageable);

    @Query("""
            SELECT u
            FROM User u
            ORDER BY u.name ASC
            """)
    Page<User> getAllUsersOrderByNameAscPaged(Pageable pageable);

    @Query("""
            SELECT u
            FROM User u
            ORDER BY u.name DESC
            """)
    Page<User> getAllUsersOrderByNameDescPaged(Pageable pageable);

    @Query("""
            SELECT u
            FROM User u
            WHERE LOWER(u.name) LIKE LOWER(CONCAT('%', :name, '%'))
            """)
    Page<User> searchByNamePaged(@Param("name") String name, Pageable pageable);

    @Query("""
            SELECT u
            FROM User u
            WHERE LOWER(u.userName) LIKE LOWER(CONCAT('%', :userName, '%'))
            """)
    Page<User> searchByUserNamePaged(@Param("userName") String userName, Pageable pageable);

    @Query("""
            SELECT u
            FROM User u
            WHERE LOWER(u.name) = LOWER(:name)
            """)
    Page<User> findByNameIgnoreCasePaged(@Param("name") String name, Pageable pageable);

    @Query("""
            SELECT u
            FROM User u
            WHERE u.profileImageId = :profileImageId
            """)
    Page<User> findByProfileImageIdPaged(@Param("profileImageId") String profileImageId, Pageable pageable);

    @Query("""
            SELECT u
            FROM User u
            WHERE u.profileImageId IS NULL
            """)
    Page<User> findUsersWithoutProfileImagePaged(Pageable pageable);

    @Query("""
            SELECT u
            FROM User u
            WHERE u.profileImageId IS NOT NULL
            """)
    Page<User> findUsersWithProfileImagePaged(Pageable pageable);

    // =================================================================
    // COUNT QUERIES (unchanged)
    // =================================================================

    @Query("SELECT COUNT(u) FROM User u")
    long countAllUsers();

    @Query("""
            SELECT COUNT(u)
            FROM User u
            WHERE u.name = :name
            """)
    long countByName(@Param("name") String name);

    @Query("""
            SELECT COUNT(u)
            FROM User u
            WHERE u.profileImageId = :profileImageId
            """)
    long countByProfileImageId(@Param("profileImageId") String profileImageId);

    // =================================================================
    // LOGIN QUERIES (unchanged)
    // =================================================================

    @Query("""
            SELECT u
            FROM User u
            WHERE u.userName = :userName
              AND u.password = :password
            """)
    Optional<User> login(@Param("userName") String userName, @Param("password") String password);

    @Query("""
            SELECT CASE
                   WHEN COUNT(u) > 0 THEN true
                   ELSE false
                   END
            FROM User u
            WHERE u.userName = :userName
              AND u.password = :password
            """)
    boolean isValidLogin(@Param("userName") String userName, @Param("password") String password);

    // =================================================================
    // UPDATE QUERIES (unchanged)
    // =================================================================

    @Modifying
    @Transactional
    @Query("""
            UPDATE User u
            SET u.name = :name
            WHERE u.id = :id
            """)
    int updateName(@Param("id") String id, @Param("name") String name);

    @Modifying
    @Transactional
    @Query("""
            UPDATE User u
            SET u.userName = :userName
            WHERE u.id = :id
            """)
    int updateUserName(@Param("id") String id, @Param("userName") String userName);

    @Modifying
    @Transactional
    @Query("""
            UPDATE User u
            SET u.profileImageId = :profileImageId
            WHERE u.id = :id
            """)
    int updateProfileImage(@Param("id") String id, @Param("profileImageId") String profileImageId);

    @Modifying
    @Transactional
    @Query("""
            UPDATE User u
            SET u.password = :password
            WHERE u.id = :id
            """)
    int updatePassword(@Param("id") String id, @Param("password") String password);

    @Modifying
    @Transactional
    @Query("""
            UPDATE User u
            SET u.name = :name,
                u.userName = :userName,
                u.profileImageId = :profileImageId,
                u.password = :password
            WHERE u.id = :id
            """)
    int updateUser(@Param("id") String id,
                   @Param("name") String name,
                   @Param("userName") String userName,
                   @Param("profileImageId") String profileImageId,
                   @Param("password") String password);

    // =================================================================
    // DELETE QUERIES (unchanged)
    // =================================================================

    @Modifying
    @Transactional
    @Query("DELETE FROM User u WHERE u.id = :id")
    int deleteUserById(@Param("id") String id);

    @Modifying
    @Transactional
    @Query("""
            DELETE FROM User u
            WHERE u.profileImageId = :profileImageId
            """)
    int deleteByProfileImageId(@Param("profileImageId") String profileImageId);

    @Modifying
    @Transactional
    @Query("""
            DELETE FROM User u
            WHERE u.profileImageId IS NULL
            """)
    int deleteUsersWithoutProfileImage();

    // =================================================================
    // NATIVE QUERIES (existing + paginated)
    // =================================================================

    @Query(value = "SELECT * FROM \"User\"", nativeQuery = true)
    List<User> findAllNative();

    @Query(value = "SELECT * FROM \"User\" WHERE \"userName\" = :userName", nativeQuery = true)
    Optional<User> findByUserNameNative(@Param("userName") String userName);

    @Query(value = """
            SELECT *
            FROM "User"
            WHERE LOWER(name) LIKE LOWER(CONCAT('%', :name, '%'))
            """, nativeQuery = true)
    List<User> searchByNameNative(@Param("name") String name);

    @Query(value = "SELECT COUNT(*) FROM \"User\"", nativeQuery = true)
    long countUsersNative();

    // ---------------- NATIVE PAGINATED ----------------

    @Query(value = "SELECT * FROM \"User\" ORDER BY name ASC",
           countQuery = "SELECT COUNT(*) FROM \"User\"",
           nativeQuery = true)
    Page<User> findAllNativePaged(Pageable pageable);

    @Query(value = """
            SELECT *
            FROM "User"
            WHERE LOWER(name) LIKE LOWER(CONCAT('%', :name, '%'))
            ORDER BY name ASC
            """,
           countQuery = """
            SELECT COUNT(*)
            FROM "User"
            WHERE LOWER(name) LIKE LOWER(CONCAT('%', :name, '%'))
            """,
           nativeQuery = true)
    Page<User> searchByNameNativePaged(@Param("name") String name, Pageable pageable);
}