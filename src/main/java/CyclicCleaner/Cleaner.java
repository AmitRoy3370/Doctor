package CyclicCleaner;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.Models.UserModels.User;
import com.example.demo.Models.UserModels.UserContact;
import com.example.demo.Models.UserModels.UserGender;
import com.example.demo.Models.UserModels.UserLocation;
import com.example.demo.Models.UserModels.UserTypes;
import com.example.demo.Repositories.UserRepositories.UserContactRepository;
import com.example.demo.Repositories.UserRepositories.UserGenderRepository;
import com.example.demo.Repositories.UserRepositories.UserLocationRepository;
import com.example.demo.Repositories.UserRepositories.UserRepository;
import com.example.demo.Repositories.UserRepositories.UserTypeRepository;
import com.example.demo.Services.UserServices.RedisService;

@Service
public class Cleaner {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private UserContactRepository userContactRepository;

	@Autowired
	private UserGenderRepository genderRepository;

	@Autowired
	private UserLocationRepository locationRepository;

	@Autowired
	private UserTypeRepository typeRepository;

	@Autowired
	private RedisService redisService;

	public boolean removeUserById(String id) {

		try {

			User user = userRepository.findById(id).get();

			if (user == null) {

				throw new Exception();

			}

			int deleted = userRepository.deleteUserById(id);

			if (deleted >= 1) {

				redisService.clearAllCaches();

				try {

					removeUserTypeByUserId(id);

				} catch (Exception e) {

				}

				try {

					UserLocation location = locationRepository.findByUserId(id);

					if (location == null) {

						throw new Exception();

					}

					deleteUserLocation(location.getId());

				} catch (Exception e) {

				}

				try {

					deleteUserGenderByUserIdNative(user.getId());

				} catch (Exception e) {

				}

				try {

					UserContact contact = userContactRepository.findByUserId(id).get();

					if (contact != null) {

						deleteContactById(contact.getId());

					}

				} catch (Exception e) {

				}

				return true;

			} else {

				throw new Exception();

			}

		} catch (Exception e) {

			return false;

		}

	}

	public boolean deleteByProfileImageId(String id) {

		try {

			List<User> user = userRepository.findByProfileImageId(id);

			if (user == null || user.isEmpty()) {

				throw new Exception();

			}

			int deleted = userRepository.deleteByProfileImageId(id);

			if (deleted >= 1) {

				redisService.clearAllCaches();

				for (User i : user) {

					try {

						UserContact contact = userContactRepository.findByUserId(i.getId()).get();

						if (contact != null) {

							deleteContactById(contact.getId());

						}

					} catch (Exception e) {

					}

					try {

						removeUserTypeByUserId(i.getId());

					} catch (Exception e) {

					}

					try {

						UserLocation location = locationRepository.findByUserId(i.getId());

						if (location == null) {

							throw new Exception();

						}

						deleteUserLocation(location.getId());

					} catch (Exception e) {

					}

					try {

						deleteUserGenderByUserIdNative(i.getId());

					} catch (Exception e) {

					}

				}

				return true;

			} else {

				throw new Exception();

			}

		} catch (Exception e) {

			return false;

		}

	}

	public boolean deleteUsersWithoutProfileImage() {

		try {

			List<User> users = userRepository.findUsersWithoutProfileImage();

			if (users.isEmpty()) {

				throw new Exception();

			}

			int deleted = userRepository.deleteUsersWithoutProfileImage();

			if (deleted >= 1) {

				redisService.clearAllCaches();

				for (User i : users) {

					try {

						UserContact contact = userContactRepository.findByUserId(i.getId()).get();

						if (contact != null) {

							deleteContactById(contact.getId());

						}

					} catch (Exception e) {

					}

					try {

						removeUserTypeByUserId(i.getId());

					} catch (Exception e) {

					}

					try {

						UserLocation location = locationRepository.findByUserId(i.getId());

						if (location == null) {

							throw new Exception();

						}

						deleteUserLocation(location.getId());

					} catch (Exception e) {

					}

					try {

						deleteUserGenderByUserIdNative(i.getId());

					} catch (Exception e) {

					}

				}

				return true;

			} else {

				throw new Exception();

			}

		} catch (Exception e) {

			return false;

		}

	}

	public boolean deleteContactById(String id) {

		try {

			UserContact contact = userContactRepository.findById(id).get();

			if (contact == null) {

				throw new Exception();

			}

			int deleted = userContactRepository.deleteContactById(id);

			if (deleted >= 1) {

				return true;

			} else {

				return false;

			}

		} catch (Exception e) {

			return false;

		}

	}

	public boolean deleteContactByEmail(String email) {

		try {

			UserContact contact = userContactRepository.findByEmail(email).get();

			if (contact == null) {

				throw new Exception();

			}

			int removed = userContactRepository.deleteContactByEmail(email);

			if (removed >= 1) {

				return true;

			} else {

				return false;

			}

		} catch (Exception e) {

			return false;

		}

	}

	public boolean deleteContactByPhone(String phone) {

		try {

			UserContact contact = userContactRepository.findByPhone(phone).get();

			if (contact == null) {

				throw new Exception();

			}

			int removed = userContactRepository.deleteContactByPhone(phone);

			if (removed >= 1) {

				return true;

			} else {

				return false;

			}

		} catch (Exception e) {

			return false;

		}

	}

	public boolean deleteContactsWithoutEmail() {

		try {

			int deleted = userContactRepository.deleteContactsWithoutEmail();

			return deleted >= 1;

		} catch (Exception e) {

			return false;

		}

	}

	public boolean deleteContactsWithoutPhone() {

		try {

			int deleted = userContactRepository.deleteContactsWithoutPhone();

			return deleted >= 1;

		} catch (Exception e) {

			return false;

		}

	}

	public boolean deleteContactByUserId(String userId) {

		try {

			UserContact contact = userContactRepository.findByUserId(userId).get();

			if (contact == null) {

				throw new Exception();

			}

			int deleted = userContactRepository.deleteContactByUserId(userId);

			return deleted >= 1;

		} catch (Exception e) {

			return false;

		}

	}

	public boolean deleteUserGenderByUserIdNative(String userId) {

		try {

			return genderRepository.deleteByUserIdNative(userId) != null;

		} catch (Exception e) {

			return false;

		}

	}

	public boolean deleteGender(String id) {

		try {

			UserGender gender = genderRepository.findById(id).get();

			if (gender == null) {

				throw new Exception();

			}

			long count = genderRepository.count();

			genderRepository.deleteById(id);

			return count != genderRepository.count();

		} catch (Exception e) {

			return false;

		}

	}

	public boolean deleteUserLocation(String id) {

		try {

			UserLocation location = locationRepository.findById(id).get();

			if (location == null) {

				throw new Exception();

			}

			long count = locationRepository.count();

			locationRepository.deleteById(id);

			return locationRepository.count() != count;

		} catch (Exception e) {

			return false;

		}

	}

	public boolean removeUserTypeByUserId(String userId) {

		try {

			return typeRepository.deleteByUserIdNative(userId) >= 1;

		} catch (Exception e) {

			return false;

		}

	}

	public boolean deleteUserType(String id) {

		try {

			UserTypes types = typeRepository.findById(id).get();

			if (types == null) {

				throw new Exception();

			}

			long count = typeRepository.count();

			typeRepository.deleteById(id);

			return count != typeRepository.count();

		} catch (Exception e) {

			return false;

		}

	}

}
