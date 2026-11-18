package com.example.demo.Services.DoctorsServices;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Caching;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.example.demo.Configs.SupabaseProperties;
import com.example.demo.CyclicCleaner.Cleaner;
import com.example.demo.DTOFiles.DoctorJoinRequestResponse;
import com.example.demo.DTOFiles.DoctorPageResponse;
import com.example.demo.DTOFiles.DoctorResponse;
import com.example.demo.ENUMS.UserType;
import com.example.demo.Models.DoctorsModels.Doctor;
import com.example.demo.Models.DoctorsModels.DoctorJoinRequest;
import com.example.demo.Models.UserModels.User;
import com.example.demo.Models.UserModels.UserContact;
import com.example.demo.Models.UserModels.UserGender;
import com.example.demo.Models.UserModels.UserLocation;
import com.example.demo.Models.UserModels.UserTypes;
import com.example.demo.Repositories.DoctorsRepository.DoctorRepository;
import com.example.demo.Repositories.DoctorsRepository.DoctorsJoinRequestRepository;
import com.example.demo.Repositories.UserRepositories.UserContactRepository;
import com.example.demo.Repositories.UserRepositories.UserGenderRepository;
import com.example.demo.Repositories.UserRepositories.UserLocationRepository;
import com.example.demo.Repositories.UserRepositories.UserRepository;
import com.example.demo.Repositories.UserRepositories.UserTypeRepository;
import com.example.demo.Services.SupabaseServices.SupabaseStorageService;

import jakarta.servlet.http.HttpServletRequest;

@Service
public class DoctorServiceImpl implements DoctorService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private UserContactRepository contactRepository;

	@Autowired
	private UserLocationRepository locationRepository;

	@Autowired
	private UserGenderRepository genderRepository;

	@Autowired
	private UserTypeRepository typeRepository;

	@Autowired
	private DoctorRepository doctorRepository;

	@Autowired
	private DoctorsJoinRequestRepository joinRequestRepository;

	@Autowired
	private SupabaseProperties supabaseProperties;

	@Autowired
	private SupabaseStorageService supabaseStorageService;

	@Autowired
	private Cleaner cleaner;

	private static final String cacheValue = "Doctor";

	@Override
	@Caching(evict = { @CacheEvict(value = cacheValue, allEntries = true),
			@CacheEvict(value = "Hospital", allEntries = true) })
	public Doctor updateDoctor(String id, Doctor doctor, String userId, MultipartFile cv) {

		if (id == null || doctor == null || userId == null || !doctor.getUserId().equals(userId)
				|| doctor.getBmdcRegistrationNumber() == null || doctor.getQualification() == null
				|| (doctor.getSpecialist() == null || doctor.getSpecialist().isEmpty()) || doctor.getAccepted_user() == null) {

			throw new NullPointerException("False request....");

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

			Doctor joinRequest = doctorRepository.findByBmdcRegistrationNumber(doctor.getBmdcRegistrationNumber());

			if (joinRequest != null) {

				if (!joinRequest.getId().equals(id)) {

					throw new ArithmeticException();

				}

			}

		} catch (ArithmeticException e) {

			throw new ArithmeticException("BMDC number is different for all doctor...");

		} catch (Exception e) {

			System.out.println(e.getMessage());

		}

		try {

			DoctorJoinRequest joinRequest = joinRequestRepository.findByUserId(userId);

			if (joinRequest != null) {

				if (joinRequest.getUserId().equals(user.getId())) {

				} else {

					throw new ArithmeticException();

				}

			}

		} catch (ArithmeticException e) {

			throw new ArithmeticException("This user already sent a doctor join request...");

		} catch (Exception e) {

			System.out.println(e.getMessage());

		}

		try {

			DoctorJoinRequest joinRequest = joinRequestRepository
					.findByBmdcRegistrationNumber(doctor.getBmdcRegistrationNumber());

			if (joinRequest != null) {

				if (joinRequest.getUserId().equals(user.getId())) {

				} else {

					throw new ArithmeticException();

				}

			}

		} catch (ArithmeticException e) {

			throw new ArithmeticException("BMDC number is different for all doctor...");

		} catch (Exception e) {

			System.out.println(e.getMessage());

		}

		try {

			Doctor joinRequest = doctorRepository.findByUserId(user.getId());

			if (joinRequest != null) {

				if (!joinRequest.getId().equals(id)) {

					throw new ArithmeticException();

				}

			}

		} catch (ArithmeticException e) {

			throw new ArithmeticException("This user already sent a doctor join request...");

		} catch (Exception e) {

			System.out.println(e.getMessage());

		}

		try {

			Doctor _doctor = doctorRepository.findById(id).get();

			if (_doctor == null) {

				throw new Exception();

			}

			if (!_doctor.getUserId().equals(userId) || !_doctor.getAccepted_user().equals(doctor.getAccepted_user())) {

				throw new Exception();

			}

		} catch (Exception e) {

			throw new NoSuchElementException("No such doctor find at here...");

		}

		if (cv != null && !cv.isEmpty()) {

                if(supabaseStorageService.fileExists(supabaseProperties.getBucket().getCv(), user.getUserName())) {

                    supabaseStorageService.deleteFile(supabaseProperties.getBucket().getCv(), user.getUserName());

                }

			String contentType = cv.getContentType();

			if (contentType != null && (contentType.equalsIgnoreCase("application/pdf")
					|| contentType.equalsIgnoreCase("application/msword") || contentType.equalsIgnoreCase(
							"application/vnd.openxmlformats-officedocument.wordprocessingml.document"))) {

				String fileUrl = uploadAdvocateCV(cv, user.getUserName());

				if (fileUrl != null) {

					doctor.setUploadedCV(fileUrl);

				} else {

					throw new ArithmeticException("CV not uploaded...");

				}

			} else {

				throw new ArithmeticException("Please upload pdf document as the pdf or doc files....");

			}

		} else if (doctor.getUploadedCV() != null) {

			if (supabaseStorageService.isValidSupabaseUrl(doctor.getUploadedCV())) {

			} else {

				throw new ArithmeticException("False cv url...");

			}

		}

		doctor.setId(id);

		doctor = doctorRepository.save(doctor);

		if (doctor == null) {

			throw new ArithmeticException("Doctor not saved...");

		}

		return doctor;

	}

	@Override
	@Cacheable(value = cacheValue, key = "'findById_' + #id")
	public DoctorResponse findById(String id) {

		if (id == null) {

			throw new NullPointerException("False request....");

		}

		try {

			Doctor doctor = doctorRepository.findById(id).get();

			if (doctor == null) {

				throw new Exception();

			}

			return getDoctorResponse(doctor);

		} catch (Exception e) {

			throw new NoSuchElementException("No such doctor find at here");

		}

	}

	@Override
	@Cacheable(value = cacheValue, key = "'findAll'")
	public List<DoctorResponse> findAll() {

		try {

			List<Doctor> doctor = doctorRepository.findAll();

			if (doctor == null) {

				throw new Exception();

			}

			return getDoctorResponse(doctor);

		} catch (Exception e) {

			throw new NoSuchElementException("No such doctor find at here");

		}

	}

	@Override
	@Cacheable(value = cacheValue, key = "'findAllPaginated_' + #page")
	public DoctorPageResponse findAll(Pageable page, HttpServletRequest http) {

		Page<Doctor> doctorsPage = doctorRepository.findAll(page);

		if (doctorsPage == null || !doctorsPage.hasContent()) {

			throw new NoSuchElementException("No such doctor find at here....");

		}

		List<DoctorResponse> responses = getDoctorResponse(doctorsPage.getContent());

		// 2. Build the base URL, stripping out the old page and size params
		// (But keeping any other params like &bio=cardio)
		String baseUrl = ServletUriComponentsBuilder.fromRequestUri(http).replaceQueryParam("page") // Removes ONLY
																									// "page"
				.replaceQueryParam("size") // Removes ONLY "size"
				.toUriString();

		// 3. Check if baseUrl already has a "?" (because of other params like bio)
		// to safely append our new page/size.
		String separator = baseUrl.contains("?") ? "&" : "?";

		String nextPage = doctorsPage.hasNext()
				? baseUrl + separator + "page=" + (doctorsPage.getNumber() + 1) + "&size=" + doctorsPage.getSize()
				: null;

		String previousPage = doctorsPage.hasPrevious()
				? baseUrl + separator + "page=" + (doctorsPage.getNumber() - 1) + "&size=" + doctorsPage.getSize()
				: null;

		return new DoctorPageResponse(responses, doctorsPage.getNumber(), doctorsPage.getSize(),
				doctorsPage.getTotalElements(), nextPage, previousPage);

	}

		@Override
	@Cacheable(value = cacheValue, key = "'findByAcceptedUser_' + #accepted_user")
	public List<DoctorResponse> findByAccepted_user(String accepted_user) {

		if (accepted_user == null) {
			throw new NullPointerException("False request....");
		}

		try {
			List<Doctor> doctor = doctorRepository.findByAccepted_user(accepted_user);
			if (doctor == null) {
				throw new Exception();
			}
			return getDoctorResponse(doctor);
		} catch (Exception e) {
			throw new NoSuchElementException("No such doctor find at here");
		}
	}

	@Override
	@Cacheable(value = cacheValue, key = "'findByAcceptedUserPaginated_' + #accepted_user + '_' + #page")
	public DoctorPageResponse findByAccepted_user(String accepted_user, Pageable page, HttpServletRequest http) {

		if (accepted_user == null) {
			throw new NullPointerException("False request....");
		}

		try {
			Page<Doctor> doctorsPage = doctorRepository.findByAccepted_user(accepted_user, page);

			if (doctorsPage == null || !doctorsPage.hasContent()) {
				throw new Exception();
			}

			List<DoctorResponse> responses = getDoctorResponse(doctorsPage.getContent());

			// 2. Build the base URL, stripping out the old page and size params
			String baseUrl = ServletUriComponentsBuilder.fromRequestUri(http).replaceQueryParam("page")
					.replaceQueryParam("size")
					.toUriString();

			// 3. Safely append our new page/size
			String separator = baseUrl.contains("?") ? "&" : "?";

			String nextPage = doctorsPage.hasNext()
					? baseUrl + separator + "page=" + (doctorsPage.getNumber() + 1) + "&size=" + doctorsPage.getSize()
					: null;

			String previousPage = doctorsPage.hasPrevious()
					? baseUrl + separator + "page=" + (doctorsPage.getNumber() - 1) + "&size=" + doctorsPage.getSize()
					: null;

			return new DoctorPageResponse(responses, doctorsPage.getNumber(), doctorsPage.getSize(),
					doctorsPage.getTotalElements(), nextPage, previousPage);

		} catch (Exception e) {
			throw new NoSuchElementException("No such doctor find at here");
		}
	}

	@Override
	@Cacheable(value = cacheValue, key = "'findByUserId_' + #userId")
	public DoctorResponse findByUserId(String userId) {

		if (userId == null) {

			throw new NullPointerException("False request....");

		}

		try {

			Doctor doctor = doctorRepository.findByUserId(userId);

			if (doctor == null) {

				throw new Exception();

			}

			return getDoctorResponse(doctor);

		} catch (Exception e) {

			throw new NoSuchElementException("No such doctor find at here");

		}

	}

	@Override
	@Cacheable(value = cacheValue, key = "'findByUsersId_' + #usersId")
	public List<DoctorResponse> findByUserIdIn(List<String> usersId) {

		if (usersId == null || usersId.isEmpty()) {

			throw new NullPointerException("False request....");

		}

		try {

			List<Doctor> doctor = doctorRepository.findByUserIdIn(usersId);

			if (doctor == null) {

				throw new Exception();

			}

			return getDoctorResponse(doctor);

		} catch (Exception e) {

			throw new NoSuchElementException("No such doctor find at here");

		}

	}

	@Override
	@Cacheable(value = cacheValue, key = "'findByBioPrefix_' + #bio")
	public List<DoctorResponse> findByBioContainingIgnoreCase(String bio) {

		if (bio == null) {

			throw new NullPointerException("False request....");

		}

		try {

			List<Doctor> doctor = doctorRepository.findByBioContainingIgnoreCase(bio);

			if (doctor == null) {

				throw new Exception();

			}

			return getDoctorResponse(doctor);

		} catch (Exception e) {

			throw new NoSuchElementException("No such doctor find at here");

		}

	}

	@Override
	@Cacheable(value = cacheValue, key = "'findByBioPrefixPaginated_' + '_' + #bio + '_' +  #page")
	public DoctorPageResponse findByBioContainingIgnoreCase(String bio, Pageable page, HttpServletRequest http) {

		if (bio == null) {

			throw new NullPointerException("False request....");

		}

		try {

			Page<Doctor> doctorsPage = doctorRepository.findByBioContainingIgnoreCase(bio, page);

			if (doctorsPage == null || !doctorsPage.hasContent()) {

				throw new Exception();

			}

			List<DoctorResponse> responses = getDoctorResponse(doctorsPage.getContent());

			// 2. Build the base URL, stripping out the old page and size params
			// (But keeping any other params like &bio=cardio)
			String baseUrl = ServletUriComponentsBuilder.fromRequestUri(http).replaceQueryParam("page") // Removes ONLY
																										// "page"
					.replaceQueryParam("size") // Removes ONLY "size"
					.toUriString();

			// 3. Check if baseUrl already has a "?" (because of other params like bio)
			// to safely append our new page/size.
			String separator = baseUrl.contains("?") ? "&" : "?";

			String nextPage = doctorsPage.hasNext()
					? baseUrl + separator + "page=" + (doctorsPage.getNumber() + 1) + "&size=" + doctorsPage.getSize()
					: null;

			String previousPage = doctorsPage.hasPrevious()
					? baseUrl + separator + "page=" + (doctorsPage.getNumber() - 1) + "&size=" + doctorsPage.getSize()
					: null;

			return new DoctorPageResponse(responses, doctorsPage.getNumber(), doctorsPage.getSize(),
					doctorsPage.getTotalElements(), nextPage, previousPage);

		} catch (Exception e) {

			throw new NoSuchElementException("No such doctor find at here");

		}
	}

	@Override
	@Cacheable(value = cacheValue, key = "'findByBmdcRegistrationNumber_' + '_' + #bmdcRegistrationNumber")
	public DoctorResponse findByBmdcRegistrationNumber(String bmdcRegistrationNumber) {

		if (bmdcRegistrationNumber == null) {

			throw new NullPointerException("False request....");

		}

		try {

			Doctor doctor = doctorRepository.findByBmdcRegistrationNumber(bmdcRegistrationNumber);

			if (doctor == null) {

				throw new Exception();

			}

			return getDoctorResponse(doctor);

		} catch (Exception e) {

			throw new NoSuchElementException("No such doctor find at here");

		}

	}

	@Override
	@Cacheable(value = cacheValue, key = "'findByQualificationPrefix_' + '_' + #qualification")
	public List<DoctorResponse> findByQualificationContainingIgnoreCase(String qualification) {

		if (qualification == null) {

			throw new NullPointerException("False request....");

		}

		try {

			List<Doctor> doctor = doctorRepository.findByQualificationContainingIgnoreCase(qualification);

			if (doctor == null) {

				throw new Exception();

			}

			return getDoctorResponse(doctor);

		} catch (Exception e) {

			throw new NoSuchElementException("No such doctor find at here");

		}
	}

	@Override
	@Cacheable(value = cacheValue, key = "'findByQualificationPrefixPaginated_' + '_' + #qualification + '_' +  #page")
	public DoctorPageResponse findByQualificationContainingIgnoreCase(String qualification, Pageable page,
			HttpServletRequest http) {

		if (qualification == null) {

			throw new NullPointerException("False request....");

		}

		try {

			Page<Doctor> doctorsPage = doctorRepository.findByQualificationContainingIgnoreCase(qualification, page);

			if (doctorsPage == null || !doctorsPage.hasContent()) {

				throw new Exception();

			}

			List<DoctorResponse> responses = getDoctorResponse(doctorsPage.getContent());

			// 2. Build the base URL, stripping out the old page and size params
			// (But keeping any other params like &bio=cardio)
			String baseUrl = ServletUriComponentsBuilder.fromRequestUri(http).replaceQueryParam("page") // Removes ONLY
																										// "page"
					.replaceQueryParam("size") // Removes ONLY "size"
					.toUriString();

			// 3. Check if baseUrl already has a "?" (because of other params like bio)
			// to safely append our new page/size.
			String separator = baseUrl.contains("?") ? "&" : "?";

			String nextPage = doctorsPage.hasNext()
					? baseUrl + separator + "page=" + (doctorsPage.getNumber() + 1) + "&size=" + doctorsPage.getSize()
					: null;

			String previousPage = doctorsPage.hasPrevious()
					? baseUrl + separator + "page=" + (doctorsPage.getNumber() - 1) + "&size=" + doctorsPage.getSize()
					: null;

			return new DoctorPageResponse(responses, doctorsPage.getNumber(), doctorsPage.getSize(),
					doctorsPage.getTotalElements(), nextPage, previousPage);

		} catch (Exception e) {

			throw new NoSuchElementException("No such doctor find at here");

		}
	}

	@Override
	@Cacheable(value = cacheValue, key = "'findByHospitalsPrefix_' + '_' + #hospitals + '_' +  #page")
	public List<DoctorResponse> findByHospitalsContainingIgnoreCase(String hospitals) {

		if (hospitals == null) {

			throw new NullPointerException("False request....");

		}

		try {

			List<Doctor> doctor = doctorRepository.findByHospitalsContainingIgnoreCase(hospitals);

			if (doctor == null) {

				throw new Exception();

			}

			return getDoctorResponse(doctor);

		} catch (Exception e) {

			throw new NoSuchElementException("No such doctor find at here");

		}
	}

	@Override
	@Cacheable(value = cacheValue, key = "'findByHospitaPrefixPaginated_' + '_' + #hospitals + '_' +  #page")
	public DoctorPageResponse findByHospitalsContainingIgnoreCase(String hospitals, Pageable page,
			HttpServletRequest http) {

		if (hospitals == null) {

			throw new NullPointerException("False request....");

		}

		try {

			Page<Doctor> doctorsPage = doctorRepository.findByHospitalsContainingIgnoreCase(hospitals, page);

			if (doctorsPage == null || !doctorsPage.hasContent()) {

				throw new Exception();

			}

			List<DoctorResponse> responses = getDoctorResponse(doctorsPage.getContent());

			// 2. Build the base URL, stripping out the old page and size params
			// (But keeping any other params like &bio=cardio)
			String baseUrl = ServletUriComponentsBuilder.fromRequestUri(http).replaceQueryParam("page") // Removes ONLY
																										// "page"
					.replaceQueryParam("size") // Removes ONLY "size"
					.toUriString();

			// 3. Check if baseUrl already has a "?" (because of other params like bio)
			// to safely append our new page/size.
			String separator = baseUrl.contains("?") ? "&" : "?";

			String nextPage = doctorsPage.hasNext()
					? baseUrl + separator + "page=" + (doctorsPage.getNumber() + 1) + "&size=" + doctorsPage.getSize()
					: null;

			String previousPage = doctorsPage.hasPrevious()
					? baseUrl + separator + "page=" + (doctorsPage.getNumber() - 1) + "&size=" + doctorsPage.getSize()
					: null;

			return new DoctorPageResponse(responses, doctorsPage.getNumber(), doctorsPage.getSize(),
					doctorsPage.getTotalElements(), nextPage, previousPage);

		} catch (Exception e) {

			throw new NoSuchElementException("No such doctor find at here");

		}
	}

	@Override
	@Cacheable(value = cacheValue, key = "'findBySpecialistPrefix_' + '_' + #specialist + '_' +  #page")
	public List<DoctorResponse> findBySpecialistContainingIgnoreCase(String specialist) {

		if (specialist == null) {

			throw new NullPointerException("False request....");

		}

		try {

			List<Doctor> doctor = doctorRepository.findBySpecialistContainingIgnoreCase(specialist);

			if (doctor == null) {

				throw new Exception();

			}

			return getDoctorResponse(doctor);

		} catch (Exception e) {

			throw new NoSuchElementException("No such doctor find at here");

		}
	}

	@Override
	@Cacheable(value = cacheValue, key = "'findBySpecialistPrefixPaginated_' + '_' + #specialist + '_' +  #page")
	public DoctorPageResponse findBySpecialistContainingIgnoreCase(String specialist, Pageable page,
			HttpServletRequest http) {

		if (specialist == null) {

			throw new NullPointerException("False request....");

		}

		try {

			Page<Doctor> doctorsPage = doctorRepository.findBySpecialistContainingIgnoreCase(specialist, page);

			if (doctorsPage == null || !doctorsPage.hasContent()) {

				throw new Exception();

			}

			List<DoctorResponse> responses = getDoctorResponse(doctorsPage.getContent());

			// 2. Build the base URL, stripping out the old page and size params
			// (But keeping any other params like &bio=cardio)
			String baseUrl = ServletUriComponentsBuilder.fromRequestUri(http).replaceQueryParam("page") // Removes ONLY
																										// "page"
					.replaceQueryParam("size") // Removes ONLY "size"
					.toUriString();

			// 3. Check if baseUrl already has a "?" (because of other params like bio)
			// to safely append our new page/size.
			String separator = baseUrl.contains("?") ? "&" : "?";

			String nextPage = doctorsPage.hasNext()
					? baseUrl + separator + "page=" + (doctorsPage.getNumber() + 1) + "&size=" + doctorsPage.getSize()
					: null;

			String previousPage = doctorsPage.hasPrevious()
					? baseUrl + separator + "page=" + (doctorsPage.getNumber() - 1) + "&size=" + doctorsPage.getSize()
					: null;

			return new DoctorPageResponse(responses, doctorsPage.getNumber(), doctorsPage.getSize(),
					doctorsPage.getTotalElements(), nextPage, previousPage);

		} catch (Exception e) {

			throw new NoSuchElementException("No such doctor find at here");

		}
	}

	@Override
	@Cacheable(value = cacheValue, key = "'findByCreatedAtAfter_' + '_' + #createdAt")
	public List<DoctorResponse> findByCreatedAtAfter(Instant createdAt) {

		if (createdAt == null) {

			throw new NullPointerException("False request....");

		}

		try {

			List<Doctor> doctor = doctorRepository.findByCreatedAtAfter(createdAt);

			if (doctor == null) {

				throw new Exception();

			}

			return getDoctorResponse(doctor);

		} catch (Exception e) {

			throw new NoSuchElementException("No such doctor find at here");

		}
	}

	@Override
	@Cacheable(value = cacheValue, key = "'findByCreatedAtAfterPaginated_' + '_' + #createdAt + '_' +  #page")
	public DoctorPageResponse findByCreatedAtAfter(Instant createdAt, Pageable page, HttpServletRequest http) {

		if (createdAt == null) {

			throw new NullPointerException("False request....");

		}

		try {

			Page<Doctor> doctorsPage = doctorRepository.findByCreatedAtAfter(createdAt, page);

			if (doctorsPage == null || !doctorsPage.hasContent()) {

				throw new Exception();

			}

			List<DoctorResponse> responses = getDoctorResponse(doctorsPage.getContent());

			// 2. Build the base URL, stripping out the old page and size params
			// (But keeping any other params like &bio=cardio)
			String baseUrl = ServletUriComponentsBuilder.fromRequestUri(http).replaceQueryParam("page") // Removes ONLY
																										// "page"
					.replaceQueryParam("size") // Removes ONLY "size"
					.toUriString();

			// 3. Check if baseUrl already has a "?" (because of other params like bio)
			// to safely append our new page/size.
			String separator = baseUrl.contains("?") ? "&" : "?";

			String nextPage = doctorsPage.hasNext()
					? baseUrl + separator + "page=" + (doctorsPage.getNumber() + 1) + "&size=" + doctorsPage.getSize()
					: null;

			String previousPage = doctorsPage.hasPrevious()
					? baseUrl + separator + "page=" + (doctorsPage.getNumber() - 1) + "&size=" + doctorsPage.getSize()
					: null;

			return new DoctorPageResponse(responses, doctorsPage.getNumber(), doctorsPage.getSize(),
					doctorsPage.getTotalElements(), nextPage, previousPage);

		} catch (Exception e) {

			throw new NoSuchElementException("No such doctor find at here");

		}
	}

	@Override
	@Cacheable(value = cacheValue, key = "'findByCreatedAtBefore_' + '_' + #createdAt + '_' +  #page")
	public List<DoctorResponse> findByCreatedAtBefore(Instant createdAt) {

		if (createdAt == null) {

			throw new NullPointerException("False request....");

		}

		try {

			List<Doctor> doctor = doctorRepository.findByCreatedAtBefore(createdAt);

			if (doctor == null) {

				throw new Exception();

			}

			return getDoctorResponse(doctor);

		} catch (Exception e) {

			throw new NoSuchElementException("No such doctor find at here");

		}
	}

	@Override
	@Cacheable(value = cacheValue, key = "'findByCreatedAtBeforePaginated_' + '_' + #createdAt + '_' +  #page")
	public DoctorPageResponse findByCreatedAtBefore(Instant createdAt, Pageable page, HttpServletRequest http) {

		if (createdAt == null) {

			throw new NullPointerException("False request....");

		}

		try {

			Page<Doctor> doctorsPage = doctorRepository.findByCreatedAtBefore(createdAt, page);

			if (doctorsPage == null || !doctorsPage.hasContent()) {

				throw new Exception();

			}

			List<DoctorResponse> responses = getDoctorResponse(doctorsPage.getContent());

			// 2. Build the base URL, stripping out the old page and size params
			// (But keeping any other params like &bio=cardio)
			String baseUrl = ServletUriComponentsBuilder.fromRequestUri(http).replaceQueryParam("page") // Removes ONLY
																										// "page"
					.replaceQueryParam("size") // Removes ONLY "size"
					.toUriString();

			// 3. Check if baseUrl already has a "?" (because of other params like bio)
			// to safely append our new page/size.
			String separator = baseUrl.contains("?") ? "&" : "?";

			String nextPage = doctorsPage.hasNext()
					? baseUrl + separator + "page=" + (doctorsPage.getNumber() + 1) + "&size=" + doctorsPage.getSize()
					: null;

			String previousPage = doctorsPage.hasPrevious()
					? baseUrl + separator + "page=" + (doctorsPage.getNumber() - 1) + "&size=" + doctorsPage.getSize()
					: null;

			return new DoctorPageResponse(responses, doctorsPage.getNumber(), doctorsPage.getSize(),
					doctorsPage.getTotalElements(), nextPage, previousPage);

		} catch (Exception e) {

			throw new NoSuchElementException("No such doctor find at here");

		}
	}

	@Override
	@Cacheable(value = cacheValue, key = "'findByUpdatedAtAfterPaginated_' + '_' + #updatedAt")
	public List<DoctorResponse> findByUpdatedAtAfter(Instant updatedAt) {

		if (updatedAt == null) {

			throw new NullPointerException("False request....");

		}

		try {

			List<Doctor> doctor = doctorRepository.findByUpdatedAtAfter(updatedAt);

			if (doctor == null) {

				throw new Exception();

			}

			return getDoctorResponse(doctor);

		} catch (Exception e) {

			throw new NoSuchElementException("No such doctor find at here");

		}
	}

	@Override
	@Cacheable(value = cacheValue, key = "'findByUpdatedAtAfterPaginated_' + '_' + #updatedAt + '_' +  #page")
	public DoctorPageResponse findByUpdatedAtAfter(Instant updatedAt, Pageable page, HttpServletRequest http) {

		if (updatedAt == null) {

			throw new NullPointerException("False request....");

		}

		try {

			Page<Doctor> doctorsPage = doctorRepository.findByUpdatedAtAfter(updatedAt, page);

			if (doctorsPage == null || !doctorsPage.hasContent()) {

				throw new Exception();

			}

			List<DoctorResponse> responses = getDoctorResponse(doctorsPage.getContent());

			// 2. Build the base URL, stripping out the old page and size params
			// (But keeping any other params like &bio=cardio)
			String baseUrl = ServletUriComponentsBuilder.fromRequestUri(http).replaceQueryParam("page") // Removes ONLY
																										// "page"
					.replaceQueryParam("size") // Removes ONLY "size"
					.toUriString();

			// 3. Check if baseUrl already has a "?" (because of other params like bio)
			// to safely append our new page/size.
			String separator = baseUrl.contains("?") ? "&" : "?";

			String nextPage = doctorsPage.hasNext()
					? baseUrl + separator + "page=" + (doctorsPage.getNumber() + 1) + "&size=" + doctorsPage.getSize()
					: null;

			String previousPage = doctorsPage.hasPrevious()
					? baseUrl + separator + "page=" + (doctorsPage.getNumber() - 1) + "&size=" + doctorsPage.getSize()
					: null;

			return new DoctorPageResponse(responses, doctorsPage.getNumber(), doctorsPage.getSize(),
					doctorsPage.getTotalElements(), nextPage, previousPage);

		} catch (Exception e) {

			throw new NoSuchElementException("No such doctor find at here");

		}
	}

	@Override
	@Cacheable(value = cacheValue, key = "'findByUpdatedAtBefore_' + '_' + #updatedAt")
	public List<DoctorResponse> findByUpdatedAtBefore(Instant updatedAt) {

		if (updatedAt == null) {

			throw new NullPointerException("False request....");

		}

		try {

			List<Doctor> doctor = doctorRepository.findByUpdatedAtBefore(updatedAt);

			if (doctor == null) {

				throw new Exception();

			}

			return getDoctorResponse(doctor);

		} catch (Exception e) {

			throw new NoSuchElementException("No such doctor find at here");

		}
	}

	@Override
	@Cacheable(value = cacheValue, key = "'findByUpdatedAtBeforePaginated_' + '_' + #updatedAt + '_' +  #page")
	public DoctorPageResponse findByUpdatedAtBefore(Instant updatedAt, Pageable page, HttpServletRequest http) {

		if (updatedAt == null) {

			throw new NullPointerException("False request....");

		}

		try {

			Page<Doctor> doctorsPage = doctorRepository.findByUpdatedAtBefore(updatedAt, page);

			if (doctorsPage == null || !doctorsPage.hasContent()) {

				throw new Exception();

			}

			List<DoctorResponse> responses = getDoctorResponse(doctorsPage.getContent());

			// 2. Build the base URL, stripping out the old page and size params
			// (But keeping any other params like &bio=cardio)
			String baseUrl = ServletUriComponentsBuilder.fromRequestUri(http).replaceQueryParam("page") // Removes ONLY
																										// "page"
					.replaceQueryParam("size") // Removes ONLY "size"
					.toUriString();

			// 3. Check if baseUrl already has a "?" (because of other params like bio)
			// to safely append our new page/size.
			String separator = baseUrl.contains("?") ? "&" : "?";

			String nextPage = doctorsPage.hasNext()
					? baseUrl + separator + "page=" + (doctorsPage.getNumber() + 1) + "&size=" + doctorsPage.getSize()
					: null;

			String previousPage = doctorsPage.hasPrevious()
					? baseUrl + separator + "page=" + (doctorsPage.getNumber() - 1) + "&size=" + doctorsPage.getSize()
					: null;

			return new DoctorPageResponse(responses, doctorsPage.getNumber(), doctorsPage.getSize(),
					doctorsPage.getTotalElements(), nextPage, previousPage);

		} catch (Exception e) {

			throw new NoSuchElementException("No such doctor find at here");

		}
	}

	@Override
	@Cacheable(value = cacheValue, key = "'findByUploadedCV_' + '_' + #uploadedCV")
	public List<DoctorResponse> findByUploadedCV(String uploadedCV) {

		if (uploadedCV == null) {

			throw new NullPointerException("False request....");

		}

		try {

			List<Doctor> doctor = doctorRepository.findByUploadedCV(uploadedCV);

			if (doctor == null) {

				throw new Exception();

			}

			return getDoctorResponse(doctor);

		} catch (Exception e) {

			throw new NoSuchElementException("No such doctor find at here");

		}
	}

	@Override
	@Caching(evict = { @CacheEvict(value = cacheValue, allEntries = true),
			@CacheEvict(value = "Hospital", allEntries = true) })
	public boolean deleteDoctor(String id, String userId) {

		if (id == null || userId == null) {

			throw new NullPointerException("False request....");

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

		Doctor joinRequest = null;

		try {

		    joinRequest = doctorRepository.findById(id).get();

			if (joinRequest == null) {

				throw new Exception();

			}


		} catch(Exception e) {

		    throw new NoSuchElementException("No such doctor find at here...");

		}

		UserTypes type = null;

		try {

			type = typeRepository.findByUserId(user.getId());

			if (type == null) {

				throw new Exception();

			}

			boolean isAdmin = type.getType().stream()
    .anyMatch(t -> t.name().equals("ADMIN"));

			if (!isAdmin || !joinRequest.getAccepted_user().equals(user.getId())) {

				return cleaner.removeDoctorsJoinRequest(id);

			} else if(isAdmin && joinRequest.getAccepted_user().equals(user.getId())) {

               return cleaner.removeDoctor(id);
			}

		} catch (Exception e) {

			throw new ArithmeticException("Only admin can remove any doctor");

		}

		try {

			if (!joinRequest.getUserId().equals(user.getId())) {

				throw new Exception();

			}

		} catch (Exception e) {

			throw new NoSuchElementException("No such request find at here...");

		}

		return cleaner.removeDoctor(id);
	}

	private String uploadAdvocateCV(MultipartFile file, String userName) {
		try {
			String bucketName = supabaseProperties.getBucket().getCv();
			String fileName = userName;

			System.out.println("bucket name :- " + bucketName);
			System.out.println("file name :- " + fileName);

			// Convert MultipartFile to bytes
			byte[] fileBytes = file.getBytes();

			System.out.println("file byted :- " + fileBytes);

			// Upload to Supabase
			String fileUrl = supabaseStorageService.uploadFile(bucketName, fileName, fileBytes, file.getContentType());

			System.out.println("file url :- " + fileUrl);

			return fileUrl;
		} catch (Exception e) {

			System.out.println("file upload error :- " + e.getMessage());

			throw new RuntimeException("Failed to upload profile image: " + e.getMessage());
		}
	}

	private ExecutorService executors = Executors.newVirtualThreadPerTaskExecutor();

	private DoctorResponse getDoctorResponse(Doctor doctor) {

		List<Doctor> list = new ArrayList<>();

		list.add(doctor);

		return getDoctorResponse(list).get(0);

	}

	private List<DoctorResponse> getDoctorResponse(List<Doctor> list) {

		List<DoctorResponse> responses = new ArrayList<>();

		CompletableFuture<List<String>> allUserIdFuture = CompletableFuture.supplyAsync(() -> list.stream().distinct()
				.filter(Objects::nonNull).map(Doctor::getUserId).collect(Collectors.toList()), executors);

		CompletableFuture<Map<String, User>> userMapFuture = allUserIdFuture.thenApplyAsync(usersId -> {

			if (usersId.isEmpty()) {

				return new HashMap<>();

			}

			return userRepository.findAllById(usersId).stream()
					.collect(Collectors.toMap(User::getId, Function.identity()));

		}, executors);

		CompletableFuture<Map<String, UserContact>> contactMapFuture = allUserIdFuture.thenApplyAsync(usersId -> {

			if (usersId.isEmpty()) {

				return new HashMap<>();

			}

			return contactRepository.findByUserIdIn(usersId).stream().filter(Objects::nonNull)
					.collect(Collectors.toMap(UserContact::getUserId, Function.identity()));

		}, executors);

		CompletableFuture<Map<String, UserLocation>> locationMapFuture = allUserIdFuture.thenApplyAsync(usersId -> {

			if (usersId.isEmpty()) {

				return new HashMap<>();

			}

			return locationRepository.findByUserIdIn(usersId).stream().filter(Objects::nonNull)
					.collect(Collectors.toMap(UserLocation::getUserId, Function.identity()));

		}, executors);

		CompletableFuture<Map<String, UserTypes>> typeMapFuture = allUserIdFuture.thenApplyAsync(usersId -> {

			if (usersId.isEmpty()) {

				return new HashMap<>();

			}

			return typeRepository.findByUserIdIn(usersId).stream().filter(Objects::nonNull)
					.collect(Collectors.toMap(UserTypes::getUserId, Function.identity()));

		}, executors);

		CompletableFuture<Map<String, UserGender>> genderMapFuture = allUserIdFuture.thenApplyAsync(usersId -> {

			if (usersId.isEmpty()) {

				return new HashMap<>();

			}

			return genderRepository.findByUserIdIn(usersId).stream().filter(Objects::nonNull)
					.collect(Collectors.toMap(UserGender::getUserId, Function.identity()));

		}, executors);

		CompletableFuture.allOf(allUserIdFuture, userMapFuture, contactMapFuture, locationMapFuture, typeMapFuture,
				genderMapFuture).join();

		Map<String, User> userMap = userMapFuture.join();
		Map<String, UserContact> contactMap = contactMapFuture.join();
		Map<String, UserLocation> locationMap = locationMapFuture.join();
		Map<String, UserTypes> typeMap = typeMapFuture.join();
		Map<String, UserGender> genderMap = genderMapFuture.join();

		for (Doctor request : list) {

			try {

				DoctorResponse response = new DoctorResponse();

				response.setId(request.getId());
				response.setUpdatedAt(request.getUpdatedAt());
				response.setCreatedAt(request.getCreatedAt());
				response.setUserId(request.getUserId());
				response.setBio(request.getBio());
				response.setBmdcRegistrationNumber(request.getBmdcRegistrationNumber());
				response.setQualification(request.getQualification());
				response.setHospitals(request.getHospitals());
				response.setSpecialist(request.getSpecialist());
				response.setUploadedCV(request.getUploadedCV());
				response.setAccepted_user(request.getAccepted_user());

				try {

					User user = userMap.get(request.getUserId());

					response.setUserName(user.getUserName());
					response.setName(user.getName());

				} catch (Exception e) {

					System.out.println(e.getMessage());

				}

				try {

					UserContact contact = contactMap.get(request.getUserId());

					response.setContact(contact);

				} catch (Exception e) {

					System.out.println(e.getMessage());

				}

				try {

					UserLocation location = locationMap.get(request.getUserId());

					response.setLocation(location);

				} catch (Exception e) {

					System.out.println(e.getMessage());

				}

				try {

					UserGender gender = genderMap.get(request.getUserId());

					response.setUserGender(gender);

				} catch (Exception e) {

					System.out.println(e.getMessage());

				}

				try {

					UserTypes types = typeMap.get(request.getUserId());

					response.setTypes(types);

				} catch (Exception e) {

					System.out.println(e.getMessage());

				}

				responses.add(response);

			} catch (Exception e) {

				System.out.println(e.getMessage());

			}

		}

		return responses;

	}

}
