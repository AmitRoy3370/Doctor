package com.example.demo.Services.UserServices;

import java.util.List;

import com.example.demo.Models.UserModels.UserContact;

public interface UserContactService {

	public UserContact createUserContact(UserContact userContact, String userId);
	
	public UserContact updateEmailByUserId(String email, String userId);
	public UserContact updatePhoneByUserId(String phone, String userId);
	public UserContact updateContactByUserId(String userId, UserContact userContact);
	
	public UserContact findContactById(String id);
	public UserContact findByUserId(String userId);
	public UserContact findByEmail(String email);
	public UserContact findByPhone(String phone);
	public UserContact findByEmailIgnoreCase(String email);
	public UserContact findByPhoneIgnoreCase(String phone);
	public List<UserContact> searchContacts(String keyword);
	public List<UserContact> findBysearchByEmailPrefix(String email);
	public List<UserContact> findByPhonePrefix(String phone);
	public List<UserContact> findByUserContactKeyWord(String userId, String keyword);
	public boolean existUserId(String userId);
	public boolean existEmail(String email);
	public boolean phoneExist(String phone);
	public boolean existUserIdAndPhone(String userId, String phone);
	public boolean existUserIdAndEmail(String userId, String email);
	public long countAllContacts();
	public long countByUserId(String userId);
	public long countUsersWithEmail();
	public long countUsersWithPhone();
	public long countUsersWithoutContactInfo();
	public List<UserContact> getAllContactsOrderByUserIdAsc();
	public List<UserContact> getAllContactsOrderByUserIdDesc();
	public List<UserContact> getAllContactsOrderByEmailAsc();
	public List<UserContact> getAllContactsOrderByEmailDesc();
	public List<UserContact> findUsersWithEmail();
	public List<UserContact> findUsersWithPhone();
	public List<UserContact> findUsersWithoutContactInfo();
	public List<UserContact> findUsersWithBothEmailAndPhone();
	public List<UserContact> findUsersWithMissingContactInfo();
	
	public boolean removeEmailByUserId(String userId);
	public boolean removePhoneByUserId(String userId);
	public boolean deleteContactById(String id);
	public boolean deleteContactByUserId(String userId);
	public boolean deleteContactByEmail(String email);
	public boolean deleteContactByPhone(String phone);
	public boolean deleteContactsWithoutInfo();
	public boolean deleteContactsWithoutEmail();
	public boolean deleteContactsWithoutPhone();
	
}
