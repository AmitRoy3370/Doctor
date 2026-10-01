package com.example.demo.Services.UserServices;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.Models.UserModels.User;

import DTOFiles.JwtResponse;
import DTOFiles.PageResponseDTO;
import DTOFiles.UserResponseDTO;

public interface UserService {

    // ---- Auth ----
    public JwtResponse createUser(User user, MultipartFile profileImage);
    public JwtResponse logIn(String userName, String password);

    // ---- Update ----
    public User updateUser(User user, MultipartFile profileImage, String id, String userId);
    public User updateName(String id, String name);
    public User updateUserName(String id, String userName, String userId);
    public User updateProfileImage(String id, MultipartFile file, String userId);
    public User updatePassword(String id, String password);

    // ---- Count ----
    public long countUsersNative();
    public long countByProfileImageId(String profileImageId);
    public long countByName(String name);
    public long countAllUsers();

    // ---- Single-result lookups ----
    public List<UserResponseDTO> searchByNameNative(String name);
    public UserResponseDTO findByUserNameNative(String userName);
    public UserResponseDTO findByUserNameIgnoreCase(String userName);
    public UserResponseDTO findByUserName(String userName);
    public UserResponseDTO findUserById(String id);
    public boolean existsByUserName(String userName);

    // ---- Non-paginated list lookups (kept) ----
    public List<UserResponseDTO> findAllNative();
    public List<UserResponseDTO> getAllUsersOrderByNameDesc();
    public List<UserResponseDTO> getAllUsersOrderByNameAsc();
    public List<UserResponseDTO> getAllUsers();
    public List<UserResponseDTO> findUsersWithProfileImage();
    public List<UserResponseDTO> findUsersWithoutProfileImage();
    public List<UserResponseDTO> findByProfileImageId(String profileImageId);
    public List<UserResponseDTO> searchUsers(String keyWord);
    public List<UserResponseDTO> searchByUserName(String userNamePrefix);
    public List<UserResponseDTO> searchByName(String name);
    public List<UserResponseDTO> findByNameIgnoreCase(String namePrefix);

    // ---- Paginated lookups (NEW) ----
    public Page<User> getUsersWithPagination(Pageable pageable);
    public PageResponseDTO<UserResponseDTO> getAllUsersPaged(Pageable pageable);
    public PageResponseDTO<UserResponseDTO> getAllUsersOrderByNameAscPaged(Pageable pageable);
    public PageResponseDTO<UserResponseDTO> getAllUsersOrderByNameDescPaged(Pageable pageable);
    public PageResponseDTO<UserResponseDTO> findAllNativePaged(Pageable pageable);
    public PageResponseDTO<UserResponseDTO> searchUsersPaged(String keyWord, Pageable pageable);
    public PageResponseDTO<UserResponseDTO> searchByNamePaged(String name, Pageable pageable);
    public PageResponseDTO<UserResponseDTO> searchByNameNativePaged(String name, Pageable pageable);
    public PageResponseDTO<UserResponseDTO> searchByUserNamePaged(String userNamePrefix, Pageable pageable);
    public PageResponseDTO<UserResponseDTO> findByNameIgnoreCasePaged(String namePrefix, Pageable pageable);
    public PageResponseDTO<UserResponseDTO> findByProfileImageIdPaged(String profileImageId, Pageable pageable);
    public PageResponseDTO<UserResponseDTO> findUsersWithProfileImagePaged(Pageable pageable);
    public PageResponseDTO<UserResponseDTO> findUsersWithoutProfileImagePaged(Pageable pageable);

    // ---- Delete ----
    public boolean deleteUserById(String id, String userId);
    public boolean deleteByProfileImageId(String profileImageId, String userId);
    public boolean deleteUsersWithoutProfileImage(String userId);
}