package com.example.demo.Repositories.UserRepositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.Models.UserModels.UserContact;

@Repository
public interface UserContactRepository extends JpaRepository<UserContact, String> {

   
    @Query("SELECT uc FROM UserContact uc WHERE uc.id = :id")
    Optional<UserContact> findContactById(@Param("id") String id);

    @Query("SELECT uc FROM UserContact uc WHERE uc.userId = :userId")
    Optional<UserContact> findByUserId(@Param("userId") String userId);

    List<UserContact> findByUserIdIn(List<String> usersId);
    
    @Query("SELECT uc FROM UserContact uc WHERE uc.email = :email")
    Optional<UserContact> findByEmail(@Param("email") String email);

    @Query("SELECT uc FROM UserContact uc WHERE uc.phone = :phone")
    Optional<UserContact> findByPhone(@Param("phone") String phone);

    @Query("""
            SELECT uc
            FROM UserContact uc
            WHERE LOWER(uc.email) = LOWER(:email)
            """)
    Optional<UserContact> findByEmailIgnoreCase(@Param("email") String email);

    @Query("""
            SELECT uc
            FROM UserContact uc
            WHERE LOWER(uc.phone) = LOWER(:phone)
            """)
    Optional<UserContact> findByPhoneIgnoreCase(@Param("phone") String phone);

    
    @Query("""
            SELECT uc
            FROM UserContact uc
            WHERE LOWER(uc.email) LIKE LOWER(CONCAT('%', :keyword, '%'))
               OR LOWER(uc.phone) LIKE LOWER(CONCAT('%', :keyword, '%'))
            """)
    List<UserContact> searchContacts(@Param("keyword") String keyword);

    @Query("""
            SELECT uc
            FROM UserContact uc
            WHERE LOWER(uc.email) LIKE LOWER(CONCAT('%', :email, '%'))
            """)
    List<UserContact> searchByEmail(@Param("email") String email);

    @Query("""
            SELECT uc
            FROM UserContact uc
            WHERE LOWER(uc.phone) LIKE LOWER(CONCAT('%', :phone, '%'))
            """)
    List<UserContact> searchByPhone(@Param("phone") String phone);

    @Query("""
            SELECT uc
            FROM UserContact uc
            WHERE uc.userId = :userId
              AND (LOWER(uc.email) LIKE LOWER(CONCAT('%', :keyword, '%'))
                   OR LOWER(uc.phone) LIKE LOWER(CONCAT('%', :keyword, '%')))
            """)
    List<UserContact> searchUserContacts(@Param("userId") String userId, @Param("keyword") String keyword);

  
    @Query("""
            SELECT CASE
                   WHEN COUNT(uc) > 0 THEN true
                   ELSE false
                   END
            FROM UserContact uc
            WHERE uc.userId = :userId
            """)
    boolean existsByUserId(@Param("userId") String userId);

    @Query("""
            SELECT CASE
                   WHEN COUNT(uc) > 0 THEN true
                   ELSE false
                   END
            FROM UserContact uc
            WHERE uc.email = :email
            """)
    boolean existsByEmail(@Param("email") String email);

    @Query("""
            SELECT CASE
                   WHEN COUNT(uc) > 0 THEN true
                   ELSE false
                   END
            FROM UserContact uc
            WHERE uc.phone = :phone
            """)
    boolean existsByPhone(@Param("phone") String phone);

    @Query("""
            SELECT CASE
                   WHEN COUNT(uc) > 0 THEN true
                   ELSE false
                   END
            FROM UserContact uc
            WHERE uc.userId = :userId
              AND uc.email = :email
            """)
    boolean existsByUserIdAndEmail(@Param("userId") String userId, @Param("email") String email);

    @Query("""
            SELECT CASE
                   WHEN COUNT(uc) > 0 THEN true
                   ELSE false
                   END
            FROM UserContact uc
            WHERE uc.userId = :userId
              AND uc.phone = :phone
            """)
    boolean existsByUserIdAndPhone(@Param("userId") String userId, @Param("phone") String phone);

    
    @Query("SELECT COUNT(uc) FROM UserContact uc")
    long countAllContacts();

    @Query("SELECT COUNT(uc) FROM UserContact uc WHERE uc.userId = :userId")
    long countByUserId(@Param("userId") String userId);

    @Query("SELECT COUNT(uc) FROM UserContact uc WHERE uc.email IS NOT NULL")
    long countUsersWithEmail();

    @Query("SELECT COUNT(uc) FROM UserContact uc WHERE uc.phone IS NOT NULL")
    long countUsersWithPhone();

    @Query("SELECT COUNT(uc) FROM UserContact uc WHERE uc.email IS NULL AND uc.phone IS NULL")
    long countUsersWithoutContactInfo();

    @Query("SELECT uc FROM UserContact uc ORDER BY uc.userId ASC")
    List<UserContact> getAllContactsOrderByUserIdAsc();

    @Query("SELECT uc FROM UserContact uc ORDER BY uc.userId DESC")
    List<UserContact> getAllContactsOrderByUserIdDesc();

    @Query("SELECT uc FROM UserContact uc ORDER BY uc.email ASC")
    List<UserContact> getAllContactsOrderByEmailAsc();

    @Query("SELECT uc FROM UserContact uc ORDER BY uc.email DESC")
    List<UserContact> getAllContactsOrderByEmailDesc();

   
    @Query("SELECT uc FROM UserContact uc WHERE uc.email IS NOT NULL")
    List<UserContact> findUsersWithEmail();

    @Query("SELECT uc FROM UserContact uc WHERE uc.phone IS NOT NULL")
    List<UserContact> findUsersWithPhone();

    @Query("SELECT uc FROM UserContact uc WHERE uc.email IS NULL AND uc.phone IS NULL")
    List<UserContact> findUsersWithoutContactInfo();

    @Query("SELECT uc FROM UserContact uc WHERE uc.email IS NOT NULL AND uc.phone IS NOT NULL")
    List<UserContact> findUsersWithBothEmailAndPhone();

    @Query("SELECT uc FROM UserContact uc WHERE uc.email IS NULL OR uc.phone IS NULL")
    List<UserContact> findUsersWithMissingContactInfo();

    
    @Modifying
    @Transactional
    @Query("""
            UPDATE UserContact uc
            SET uc.email = :email
            WHERE uc.userId = :userId
            """)
    int updateEmailByUserId(@Param("userId") String userId, @Param("email") String email);

    @Modifying
    @Transactional
    @Query("""
            UPDATE UserContact uc
            SET uc.phone = :phone
            WHERE uc.userId = :userId
            """)
    int updatePhoneByUserId(@Param("userId") String userId, @Param("phone") String phone);

    @Modifying
    @Transactional
    @Query("""
            UPDATE UserContact uc
            SET uc.email = :email,
                uc.phone = :phone
            WHERE uc.userId = :userId
            """)
    int updateContactByUserId(@Param("userId") String userId, @Param("email") String email,
            @Param("phone") String phone);

    @Modifying
    @Transactional
    @Query("""
            UPDATE UserContact uc
            SET uc.email = NULL
            WHERE uc.userId = :userId
            """)
    int removeEmailByUserId(@Param("userId") String userId);

    @Modifying
    @Transactional
    @Query("""
            UPDATE UserContact uc
            SET uc.phone = NULL
            WHERE uc.userId = :userId
            """)
    int removePhoneByUserId(@Param("userId") String userId);

    
    @Modifying
    @Transactional
    @Query("DELETE FROM UserContact uc WHERE uc.id = :id")
    int deleteContactById(@Param("id") String id);

    @Modifying
    @Transactional
    @Query("DELETE FROM UserContact uc WHERE uc.userId = :userId")
    int deleteContactByUserId(@Param("userId") String userId);

    @Modifying
    @Transactional
    @Query("DELETE FROM UserContact uc WHERE uc.email = :email")
    int deleteContactByEmail(@Param("email") String email);

    @Modifying
    @Transactional
    @Query("DELETE FROM UserContact uc WHERE uc.phone = :phone")
    int deleteContactByPhone(@Param("phone") String phone);

    @Modifying
    @Transactional
    @Query("DELETE FROM UserContact uc WHERE uc.email IS NULL AND uc.phone IS NULL")
    int deleteContactsWithoutInfo();

    @Modifying
    @Transactional
    @Query("DELETE FROM UserContact uc WHERE uc.email IS NULL")
    int deleteContactsWithoutEmail();

    @Modifying
    @Transactional
    @Query("DELETE FROM UserContact uc WHERE uc.phone IS NULL")
    int deleteContactsWithoutPhone();

    @Query("SELECT uc.userId FROM UserContact uc WHERE uc.email = :email")
    Optional<String> findUserIdByEmail(@Param("email") String email);

    @Query("SELECT uc.userId FROM UserContact uc WHERE uc.phone = :phone")
    Optional<String> findUserIdByPhone(@Param("phone") String phone);

    @Query("""
            SELECT uc.userId
            FROM UserContact uc
            WHERE uc.email IS NOT NULL
               OR uc.phone IS NOT NULL
            """)
    List<String> findAllUserIdsWithContactInfo();

    @Query(value = "SELECT * FROM user_contact", nativeQuery = true)
    List<UserContact> findAllNative();

    @Query(value = "SELECT * FROM user_contact WHERE user_id = :userId", nativeQuery = true)
    Optional<UserContact> findByUserIdNative(@Param("userId") String userId);

    @Query(value = "SELECT * FROM user_contact WHERE email = :email", nativeQuery = true)
    Optional<UserContact> findByEmailNative(@Param("email") String email);

    @Query(value = "SELECT * FROM user_contact WHERE phone = :phone", nativeQuery = true)
    Optional<UserContact> findByPhoneNative(@Param("phone") String phone);

    @Query(value = "SELECT COUNT(*) FROM user_contact", nativeQuery = true)
    long countAllContactsNative();

    @Query(value = "SELECT * FROM user_contact ORDER BY user_id ASC", nativeQuery = true)
    List<UserContact> findAllOrderByUserIdAscNative();

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM user_contact WHERE user_id = :userId", nativeQuery = true)
    int deleteContactByUserIdNative(@Param("userId") String userId);
}