package com.example.demo.Services.UserServices;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;

import com.example.demo.Models.UserModels.User;
import com.example.demo.Models.UserModels.UserContact;
import com.example.demo.Repositories.UserRepositories.UserContactRepository;
import com.example.demo.Repositories.UserRepositories.UserRepository;
import com.example.demo.Validators.EmailValidator;
import com.example.demo.Validators.PhoneValidator;

import CyclicCleaner.Cleaner;

@Service
public class UserContactServiceImpl implements UserContactService {

	@Autowired
	private UserContactRepository userContactRepository;

	@Autowired
	private UserRepository userRepository;

	private PhoneValidator phoneValidator;

	private EmailValidator emailValidator = new EmailValidator();

	@Autowired
	private Cleaner cleaner;

	private static final String cacheValue = "UserContact";

	@Override
	@Caching(evict = { @CacheEvict(value = cacheValue, allEntries = true),
			@CacheEvict(value = "User", allEntries = true) })
	public UserContact createUserContact(UserContact userContact, String userId) {

		if (userContact == null || userId == null || !userContact.getUserId().equals(userId)) {

			throw new NullPointerException("False request...");

		}

		User user = null;

		try {

			user = userRepository.findById(userId).get();

			if (user == null) {

				throw new Exception();

			}

		} catch (Exception e) {

			throw new NoSuchElementException("No such user exist at here...");

		}

		try {

			UserContact contact = userContactRepository.findByUserId(userId).get();

			if (contact != null) {

				throw new ArithmeticException();

			}

		} catch (ArithmeticException e) {

			throw new ArithmeticException("This user's contact infor already setted...");

		}

		try {

			if (userContact.getEmail() != null) {

				UserContact contact = userContactRepository.findByEmail(userContact.getEmail()).get();

				if (contact != null) {

					throw new ArithmeticException();

				}

			}

		} catch (ArithmeticException e) {

			throw new NoSuchElementException("This email already exist at here...");

		} catch (Exception e) {

		}

		try {

			if (userContact.getPhone() != null) {

				UserContact contact = userContactRepository.findByPhone(userContact.getPhone()).get();

				if (contact != null) {

					throw new ArithmeticException();

				}

			}

		} catch (ArithmeticException e) {

			throw new NoSuchElementException("This phone already exist at here...");

		} catch (Exception e) {

		}

		try {

			if (userContact.getPhone() != null) {

				phoneValidator = new PhoneValidator(userContact.getPhone());

				if (phoneValidator.isValid()) {

				} else {

					throw new Exception();

				}

			}

		} catch (Exception e) {

			throw new ArithmeticException("Wrong phone number");

		}

		try {

			if (userContact.getEmail() != null) {

				if (emailValidator.isValidEmail(userContact.getEmail())) {

				} else {

					throw new Exception();

				}

			}

		} catch (Exception e) {

			throw new ArithmeticException("Wrong mail adress");

		}

		try {

			if (userContact.getPhone() == null && userContact.getEmail() == null) {

				throw new Exception();

			}

		} catch (Exception e) {

			throw new ArithmeticException("You must have to input either email or phone...");

		}

		try {

			userContact = userContactRepository.save(userContact);

			if (userContact == null) {

				throw new Exception();

			}

			return userContact;

		} catch (Exception e) {

			throw new ArithmeticException("User contact not saved...");

		}

	}

	@Override
	@Caching(evict = { @CacheEvict(value = cacheValue, allEntries = true),
			@CacheEvict(value = "User", allEntries = true) })
	public UserContact updateEmailByUserId(String email, String userId) {

		if (email == null || userId == null) {

			throw new NullPointerException("False request...");

		}

		User user = null;

		try {

			user = userRepository.findById(userId).get();

			if (user == null) {

				throw new Exception();

			}

		} catch (Exception e) {

			throw new NoSuchElementException("No such user exist at here...");

		}

		try {

			if (emailValidator.isValidEmail(email)) {

			} else {

				throw new Exception();

			}

		} catch (Exception e) {

			throw new ArithmeticException("Wrong mail adress");

		}

		UserContact contact = null;

		try {

			contact = userContactRepository.findByUserId(userId).get();

			if (contact == null) {

				throw new ArithmeticException();

			}

			if (!contact.getUserId().equals(userId)) {

				throw new Exception();

			}

			contact.setEmail(email);

		} catch (Exception e) {

			throw new ArithmeticException("This user's contact infor already setted...");

		}

		try {

			UserContact _contact = userContactRepository.findByEmail(email).get();

			if (_contact != null) {

				if (!_contact.getUserId().equals(userId)) {

					throw new ArithmeticException();

				}

			}

		} catch (ArithmeticException e) {

			throw new NoSuchElementException("This email already exist at here...");

		} catch (Exception e) {

		}

		try {

			int updated = userContactRepository.updateEmailByUserId(userId, email);

			if (updated >= 1) {

			} else {

				throw new Exception();

			}

		} catch (Exception e) {

			throw new ArithmeticException("Contact infor not updated...");

		}

		return contact;
	}

	@Override
	@Caching(evict = { @CacheEvict(value = cacheValue, allEntries = true),
			@CacheEvict(value = "User", allEntries = true) })
	public UserContact updatePhoneByUserId(String phone, String userId) {

		if (phone == null || userId == null) {

			throw new NullPointerException("False request...");

		}

		User user = null;

		try {

			user = userRepository.findById(userId).get();

			if (user == null) {

				throw new Exception();

			}

		} catch (Exception e) {

			throw new NoSuchElementException("No such user exist at here...");

		}

		try {

			phoneValidator = new PhoneValidator(phone);

			if (phoneValidator.isValid()) {

			} else {

				throw new Exception();

			}

		} catch (Exception e) {

			throw new ArithmeticException("Wrong phone number");

		}

		UserContact contact = null;

		try {

			contact = userContactRepository.findByUserId(userId).get();

			if (contact == null) {

				throw new ArithmeticException();

			}

			if (!contact.getUserId().equals(userId)) {

				throw new Exception();

			}

			contact.setPhone(phone);

		} catch (Exception e) {

			throw new ArithmeticException("This user's contact infor already setted...");

		}

		try {

			UserContact _contact = userContactRepository.findByPhone(phone).get();

			if (_contact != null) {

				if (!_contact.getUserId().equals(userId)) {

					throw new ArithmeticException();

				}

			}

		} catch (ArithmeticException e) {

			throw new NoSuchElementException("This email already exist at here...");

		} catch (Exception e) {

		}

		try {

			int updated = userContactRepository.updatePhoneByUserId(userId, phone);

			if (updated >= 1) {

			} else {

				throw new Exception();

			}

		} catch (Exception e) {

			throw new ArithmeticException("Contact infor not updated...");

		}

		return contact;
	}

	@Override
	@Caching(evict = { @CacheEvict(value = cacheValue, allEntries = true),
			@CacheEvict(value = "User", allEntries = true) })
	public UserContact updateContactByUserId(String userId, UserContact userContact) {

		if (userId == null || userContact == null || !userContact.getUserId().equals(userId)) {

			throw new NullPointerException("False request...");

		}

		User user = null;

		try {

			user = userRepository.findById(userId).get();

			if (user == null) {

				throw new Exception();

			}

		} catch (Exception e) {

			throw new NoSuchElementException("No such user exist at here...");

		}

		try {

			if (userContact.getPhone() != null) {

				phoneValidator = new PhoneValidator(userContact.getPhone());

				if (phoneValidator.isValid()) {

				} else {

					throw new Exception();

				}

			}

		} catch (Exception e) {

			throw new ArithmeticException("Wrong phone number");

		}

		try {

			if (userContact.getEmail() != null) {

				if (emailValidator.isValidEmail(userContact.getEmail())) {

				} else {

					throw new Exception();

				}

			}

		} catch (Exception e) {

			throw new ArithmeticException("Wrong mail adress");

		}

		UserContact contact = null;

		try {

			contact = userContactRepository.findByUserId(userId).get();

			if (contact == null) {

				throw new ArithmeticException();

			}

		} catch (Exception e) {

			throw new ArithmeticException("This user's contact infor already setted...");

		}

		try {

			UserContact _contact = userContactRepository.findByPhone(contact.getPhone()).get();

			if (_contact != null) {

				if (!_contact.getUserId().equals(userId)) {

					throw new ArithmeticException();

				}

			}

		} catch (ArithmeticException e) {

			throw new NoSuchElementException("This email already exist at here...");

		} catch (Exception e) {

		}

		try {

			if (userContact.getPhone() == null && userContact.getEmail() == null) {

				throw new Exception();

			}

		} catch (Exception e) {

			throw new ArithmeticException("You must have to input either email or phone...");

		}

		try {

			UserContact _contact = userContactRepository.findByEmail(userContact.getEmail()).get();

			if (_contact != null) {

				if (!_contact.getUserId().equals(userId)) {

					throw new ArithmeticException();

				}

			}

		} catch (ArithmeticException e) {

			throw new NoSuchElementException("This email already exist at here...");

		} catch (Exception e) {

		}

		try {

			int updated = userContactRepository.updateContactByUserId(userId, userContact.getEmail(),
					userContact.getPhone());

			if (updated >= 1) {

			} else {

				throw new Exception();

			}

		} catch (Exception e) {

			throw new ArithmeticException("Contact info not updated...");

		}

		return userContact;
	}

	@Override
	@Cacheable(value = cacheValue, key = "'findUserContactById_' + #id")
	public UserContact findContactById(String id) {

		if (id == null) {

			throw new NullPointerException("False request...");

		}

		try {

			UserContact contact = userContactRepository.findById(id).get();

			if (contact == null) {

				throw new Exception();

			}

			return contact;

		} catch (Exception e) {

			throw new NoSuchElementException("No such contact find at here...");

		}

	}

	@Override
	@Cacheable(value = cacheValue, key = "'findUserContactByUserId_' + #userId")
	public UserContact findByUserId(String userId) {

		if (userId == null) {

			throw new NoSuchElementException("False request....");

		}

		try {

			User user = userRepository.getById(userId);

			if (user == null) {

				throw new Exception();

			}

		} catch (Exception e) {

			throw new NoSuchElementException("No such user exist at here...");

		}

		try {

			UserContact contact = userContactRepository.findByUserId(userId).get();

			if (contact == null) {

				throw new Exception();

			}

			return contact;

		} catch (Exception e) {

			throw new NoSuchElementException("No such contact exist at here...");

		}

	}

	@Override
	@Cacheable(value = cacheValue, key = "'findBy' + '_' + 'emailAdress_' + #email")
	public UserContact findByEmail(String email) {

		if (email == null) {

			throw new NoSuchElementException("False request....");

		}

		try {

			UserContact contact = userContactRepository.findByEmail(email).get();

			if (contact == null) {

				throw new Exception();

			}

			return contact;

		} catch (Exception e) {

			throw new NoSuchElementException("No such contact exist at here...");

		}

	}

	@Override
	@Cacheable(value = cacheValue, key = "'findBy' + '_' + 'phoneNumber_' + #phone")
	public UserContact findByPhone(String phone) {

		if (phone == null) {

			throw new NoSuchElementException("False request....");

		}

		try {

			UserContact contact = userContactRepository.findByPhone(phone).get();

			if (contact == null) {

				throw new Exception();

			}

			return contact;

		} catch (Exception e) {

			throw new NoSuchElementException("No such contact exist at here...");

		}

	}

	@Override
	@Cacheable(value = cacheValue, key = "'findBy' + '_' + 'emailIgnoreCase_' + #email")
	public UserContact findByEmailIgnoreCase(String email) {

		if (email == null) {

			throw new NoSuchElementException("False request....");

		}

		try {

			UserContact contact = userContactRepository.findByEmailIgnoreCase(email).get();

			if (contact == null) {

				throw new Exception();

			}

			return contact;

		} catch (Exception e) {

			throw new NoSuchElementException("No such contact exist at here...");

		}

	}

	@Override
	@Cacheable(value = cacheValue, key = "'findBy' + '_' + 'phoneIgnoreCase_' + #phone")
	public UserContact findByPhoneIgnoreCase(String phone) {

		if (phone == null) {

			throw new NoSuchElementException("False request....");

		}

		try {

			UserContact contact = userContactRepository.findByPhoneIgnoreCase(phone).get();

			if (contact == null) {

				throw new Exception();

			}

			return contact;

		} catch (Exception e) {

			throw new NoSuchElementException("No such contact exist at here...");

		}

	}

	@Override
	@Cacheable(value = cacheValue, key = "'findBy' + '_' + 'keyword_' + #keyword")
	public List<UserContact> searchContacts(String keyword) {

		if (keyword == null) {

			throw new NoSuchElementException("False request....");

		}

		try {

			List<UserContact> contact = userContactRepository.searchContacts(keyword);

			if (contact == null || contact.isEmpty()) {

				throw new Exception();

			}

			return contact;

		} catch (Exception e) {

			throw new NoSuchElementException("No such contact exist at here...");

		}

	}

	@Override
	@Cacheable(value = cacheValue, key = "'findBy' + '_' + 'emailPrefix_' + #email")
	public List<UserContact> findBysearchByEmailPrefix(String email) {

		if (email == null) {

			throw new NoSuchElementException("False request....");

		}

		try {

			List<UserContact> contact = userContactRepository.searchByEmail(email);

			if (contact == null || contact.isEmpty()) {

				throw new Exception();

			}

			return contact;

		} catch (Exception e) {

			throw new NoSuchElementException("No such contact exist at here...");

		}
	}

	@Override
	@Cacheable(value = cacheValue, key = "'findBy' + '_' + 'phonePrefix_' + #phone")
	public List<UserContact> findByPhonePrefix(String phone) {

		if (phone == null) {

			throw new NoSuchElementException("False request....");

		}

		try {

			List<UserContact> contact = userContactRepository.searchByPhone(phone);

			if (contact == null || contact.isEmpty()) {

				throw new Exception();

			}

			return contact;

		} catch (Exception e) {

			throw new NoSuchElementException("No such contact exist at here...");

		}
	}

	@Override
	@Cacheable(value = cacheValue, key = "'findByUser_' + #userId + '_' + 'keyword_' + #keyword")
	public List<UserContact> findByUserContactKeyWord(String userId, String keyword) {

		if (userId == null || keyword == null) {

			throw new NoSuchElementException("False request....");

		}

		try {

			List<UserContact> contact = userContactRepository.searchUserContacts(userId, keyword);

			if (contact == null || contact.isEmpty()) {

				throw new Exception();

			}

			return contact;

		} catch (Exception e) {

			throw new NoSuchElementException("No such contact exist at here...");

		}
	}

	@Override
	@Cacheable(value = cacheValue, key = "'exist_' + '_' + 'userId_' + #userId")
	public boolean existUserId(String userId) {

		if (userId == null) {

			throw new NullPointerException("False request...");

		}

		return userContactRepository.existsByUserId(userId);

	}

	@Override
	@Cacheable(value = cacheValue, key = "'exist_' + '_' + 'email_' + #email")
	public boolean existEmail(String email) {

		if (email == null) {

			throw new NullPointerException("False request...");

		}

		return userContactRepository.existsByEmail(email);
	}

	@Override
	@Cacheable(value = cacheValue, key = "'exist_' + '_' + 'phone_' + #phone")
	public boolean phoneExist(String phone) {

		if (phone == null) {

			throw new NullPointerException("False request...");

		}

		return userContactRepository.existsByPhone(phone);
	}

	@Override
	@Cacheable(value = cacheValue, key = "'existUserId_' + #userId + '_' + 'phone_' + #phone")
	public boolean existUserIdAndPhone(String userId, String phone) {

		if (phone == null || userId == null) {

			throw new NullPointerException("False request...");

		}

		return userContactRepository.existsByUserIdAndPhone(userId, phone);
	}

	@Override
	@Cacheable(value = cacheValue, key = "'existUserId_' + #userId + '_' + 'email_' + #email")
	public boolean existUserIdAndEmail(String userId, String email) {

		if (email == null || userId == null) {

			throw new NullPointerException("False request...");

		}

		return userContactRepository.existsByUserIdAndEmail(userId, email);
	}

	@Override
	@Cacheable(value = cacheValue, key = "'countAllContact'")
	public long countAllContacts() {

		return userContactRepository.countAllContacts();
	}

	@Override
	@Cacheable(value = cacheValue, key = "'findUsersWithUserId_' + #userId")
	public long countByUserId(String userId) {

		if (userId == null) {

			throw new NullPointerException("False request...");

		}

		return userContactRepository.countByUserId(userId);
	}

	@Override
	@Cacheable(value = cacheValue, key = "'findUsersWithEmail'")
	public long countUsersWithEmail() {

		return userContactRepository.countUsersWithEmail();
	}

	@Override
	@Cacheable(value = cacheValue, key = "'findUsersWithPhone'")
	public long countUsersWithPhone() {
		return userContactRepository.countUsersWithPhone();
	}

	@Override
	@Cacheable(value = cacheValue, key = "'findUsersWithoutContactInfo'")
	public long countUsersWithoutContactInfo() {
		return userContactRepository.countUsersWithoutContactInfo();
	}

	@Override
	@Cacheable(value = cacheValue, key = "'findUsersWithUserIdAsc'")
	public List<UserContact> getAllContactsOrderByUserIdAsc() {
		return userContactRepository.getAllContactsOrderByUserIdAsc();
	}

	@Override
	@Cacheable(value = cacheValue, key = "'findUsersWithUserIdDesc'")
	public List<UserContact> getAllContactsOrderByUserIdDesc() {
		return userContactRepository.getAllContactsOrderByUserIdDesc();
	}

	@Override
	@Cacheable(value = cacheValue, key = "'findUsersWithEmailAsc'")
	public List<UserContact> getAllContactsOrderByEmailAsc() {
		return userContactRepository.getAllContactsOrderByEmailAsc();
	}

	@Override
	@Cacheable(value = cacheValue, key = "'findUsersWithEmailDesc'")
	public List<UserContact> getAllContactsOrderByEmailDesc() {
		return userContactRepository.getAllContactsOrderByEmailDesc();
	}

	@Override
	@Cacheable(value = cacheValue, key = "'findUsersWithEmail'")
	public List<UserContact> findUsersWithEmail() {
		return userContactRepository.findUsersWithEmail();
	}

	@Override
	@Cacheable(value = cacheValue, key = "'findUsersWithPhone'")
	public List<UserContact> findUsersWithPhone() {
		return userContactRepository.findUsersWithPhone();
	}

	@Override
	@Cacheable(value = cacheValue, key = "'findUsersWithoutContactInfo'")
	public List<UserContact> findUsersWithoutContactInfo() {
		return userContactRepository.findUsersWithoutContactInfo();
	}

	@Override
	@Cacheable(value = cacheValue, key = "'findUsersWithBothEmailAndPhone'")
	public List<UserContact> findUsersWithBothEmailAndPhone() {
		return userContactRepository.findUsersWithBothEmailAndPhone();
	}

	@Override
	@Cacheable(value = cacheValue, key = "'findUsersWithMissingContactInfo'")
	public List<UserContact> findUsersWithMissingContactInfo() {
		return userContactRepository.findUsersWithMissingContactInfo();
	}

	@Override
	@Caching(evict = { @CacheEvict(value = cacheValue, allEntries = true),
			@CacheEvict(value = "User", allEntries = true) })
	public boolean removeEmailByUserId(String userId) {

		if (userId == null) {

			throw new NullPointerException("Fasle request...");

		}

		User user = null;

		try {

			user = userRepository.findById(userId).get();

			if (user == null) {

				throw new Exception();

			}

		} catch (Exception e) {

			throw new NoSuchElementException("No such user exist at here...");

		}

		UserContact contact = null;

		try {

			contact = userContactRepository.findByUserId(userId).get();

			if (contact == null) {

				throw new ArithmeticException();

			}

			if (!contact.getUserId().equals(userId)) {

				throw new Exception();

			}

		} catch (Exception e) {

			throw new ArithmeticException("This user's contact infor already setted...");

		}

		int removed = userContactRepository.removeEmailByUserId(userId);

		if (removed >= 1) {

			return true;

		} else {

			return false;

		}

	}

	@Override
	@Caching(evict = { @CacheEvict(value = cacheValue, allEntries = true),
			@CacheEvict(value = "UserContact", allEntries = true), })
	public boolean removePhoneByUserId(String userId) {

		if (userId == null) {

			throw new NullPointerException("Fasle request...");

		}

		User user = null;

		try {

			user = userRepository.findById(userId).get();

			if (user == null) {

				throw new Exception();

			}

		} catch (Exception e) {

			throw new NoSuchElementException("No such user exist at here...");

		}

		UserContact contact = null;

		try {

			contact = userContactRepository.findByUserId(userId).get();

			if (contact == null) {

				throw new ArithmeticException();

			}

			if (!contact.getUserId().equals(userId)) {

				throw new Exception();

			}

		} catch (Exception e) {

			throw new ArithmeticException("This user's contact infor already setted...");

		}

		int removed = userContactRepository.removePhoneByUserId(userId);

		if (removed >= 1) {

			return true;

		} else {

			return false;

		}
	}

	@Override
	@Caching(evict = { @CacheEvict(value = cacheValue, allEntries = true),
			@CacheEvict(value = "UserContact", allEntries = true), })
	public boolean deleteContactById(String id) {

		if (id == null) {

			throw new NullPointerException("False request....");

		}

		try {

			UserContact contact = userContactRepository.findById(id).get();

			if (contact == null) {

				throw new Exception();

			}

		} catch (Exception e) {

			throw new NoSuchElementException("No such contact exist...");

		}

		boolean deleted = cleaner.deleteContactById(id);

		if (deleted) {

			return true;

		} else {

			return false;

		}

	}

	@Override
	@Caching(evict = { @CacheEvict(value = cacheValue, allEntries = true),
			@CacheEvict(value = "UserContact", allEntries = true), })
	public boolean deleteContactByUserId(String userId) {

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

			throw new NoSuchElementException("No such user exist at here...");

		}

		return cleaner.deleteContactByUserId(userId);
	}

	@Override
	@Caching(evict = { @CacheEvict(value = cacheValue, allEntries = true),
			@CacheEvict(value = "UserContact", allEntries = true), })
	public boolean deleteContactByEmail(String email) {

		if (email == null) {

			throw new NullPointerException("False request...");

		}

		try {

			UserContact contact = userContactRepository.findByEmail(email).get();

			if (contact == null) {

				throw new ArithmeticException();

			}

		} catch (Exception e) {

			throw new ArithmeticException("Invalid email information...");

		}

		return cleaner.deleteContactByEmail(email);
	}

	@Override
	@Caching(evict = { @CacheEvict(value = cacheValue, allEntries = true),
			@CacheEvict(value = "UserContact", allEntries = true), })
	public boolean deleteContactByPhone(String phone) {

		if (phone == null) {

			throw new NullPointerException("False request...");

		}

		try {

			UserContact contact = userContactRepository.findByPhone(phone).get();

			if (contact == null) {

				throw new ArithmeticException();

			}

		} catch (Exception e) {

			throw new ArithmeticException("Invalid email information...");

		}

		return cleaner.deleteContactByPhone(phone);
	}

	@Override
	@Caching(evict = { @CacheEvict(value = cacheValue, allEntries = true),
			@CacheEvict(value = "UserContact", allEntries = true), })
	public boolean deleteContactsWithoutInfo() {

		return cleaner.deleteUsersWithoutProfileImage();
	}

	@Override
	@Caching(evict = { @CacheEvict(value = cacheValue, allEntries = true),
			@CacheEvict(value = "UserContact", allEntries = true), })
	public boolean deleteContactsWithoutEmail() {

		return cleaner.deleteContactsWithoutEmail();
	}

	@Override
	@Caching(evict = { @CacheEvict(value = cacheValue, allEntries = true),
			@CacheEvict(value = "UserContact", allEntries = true), })
	public boolean deleteContactsWithoutPhone() {

		return cleaner.deleteContactsWithoutPhone();
	}

}
