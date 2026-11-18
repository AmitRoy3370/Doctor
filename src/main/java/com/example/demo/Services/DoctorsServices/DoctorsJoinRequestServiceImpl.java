package com.example.demo.Services.DoctorsServices;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.net.URI;
import java.net.URISyntaxException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.example.demo.ENUMS.UserType;
import com.example.demo.Configs.SupabaseProperties;
import com.example.demo.CyclicCleaner.Cleaner;
import com.example.demo.DTOFiles.DoctorJoinRequestPageResponse;
import com.example.demo.DTOFiles.DoctorJoinRequestResponse;
import com.example.demo.Models.DoctorsModels.Doctor;
import com.example.demo.Models.DoctorsModels.DoctorJoinRequest;
import com.example.demo.DTOFiles.DoctorJoinRequestResponse;
import com.example.demo.DTOFiles.DoctorJoinRequestPageResponse;
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

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class DoctorsJoinRequestServiceImpl implements DoctorsJoinRequestService {

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
	private DoctorsJoinRequestRepository joinRequestRepository;

	@Autowired
	private DoctorRepository doctorRepository;

	@Autowired
	private SupabaseProperties supabaseProperties;

	@Autowired
	private SupabaseStorageService supabaseStorageService;

	@Autowired
	private Cleaner cleaner;

	private static final String cacheValue = "DoctorJoinRequest";

	@Override
	@CacheEvict(value = cacheValue, allEntries = true)
	public DoctorJoinRequest sendRequest(DoctorJoinRequest request, MultipartFile cv, String userId) {

		if (request == null || userId == null || !request.getUserId().equals(userId)
				|| request.getBmdcRegistrationNumber() == null || request.getQualification() == null
				|| (request.getSpecialist() == null || request.getSpecialist().isEmpty())) {

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

			DoctorJoinRequest joinRequest = joinRequestRepository
					.findByBmdcRegistrationNumber(request.getBmdcRegistrationNumber());

			if (joinRequest != null) {

				throw new ArithmeticException();

			}

		} catch (ArithmeticException e) {

			throw new ArithmeticException("BMDC number is different for all doctor...");

		} catch (Exception e) {

			System.out.println(e.getMessage());

		}

		try {

			Doctor joinRequest = doctorRepository.findByBmdcRegistrationNumber(request.getBmdcRegistrationNumber());

			if (joinRequest != null) {

				throw new ArithmeticException();

			}

		} catch (ArithmeticException e) {

			throw new ArithmeticException("BMDC number is different for all doctor...");

		} catch (Exception e) {

			System.out.println(e.getMessage());

		}

		try {

			Doctor joinRequest = doctorRepository.findByUserId(user.getId());

			if (joinRequest != null) {

				throw new ArithmeticException();

			}

		} catch (ArithmeticException e) {

			throw new ArithmeticException("This user already sent a doctor join request...");

		} catch (Exception e) {

			System.out.println(e.getMessage());

		}

		try {

			DoctorJoinRequest joinRequest = joinRequestRepository.findByUserId(user.getId());

			if (joinRequest != null) {

				throw new ArithmeticException();

			}

		} catch (ArithmeticException e) {

			throw new ArithmeticException("This user already sent a doctor join request...");

		} catch (Exception e) {

			System.out.println(e.getMessage());

		}

		if (cv != null && !cv.isEmpty()) {

			String contentType = cv.getContentType();

			if (contentType != null && (contentType.equalsIgnoreCase("application/pdf")
					|| contentType.equalsIgnoreCase("application/msword") || contentType.equalsIgnoreCase(
							"application/vnd.openxmlformats-officedocument.wordprocessingml.document"))) {

				String fileUrl = uploadAdvocateCV(cv, user.getUserName());

				System.out.println("Uploaded file url :- " + fileUrl);

				if (fileUrl != null) {

					request.setUploadedCV(fileUrl);

				} else {

					throw new ArithmeticException("CV not uploaded...");

				}

			} else {

				throw new ArithmeticException("Please upload pdf document as the pdf or doc files....");

			}

		} else if (request.getUploadedCV() != null) {

			if (supabaseStorageService.isValidSupabaseUrl(request.getUploadedCV())) {

			} else {

				throw new ArithmeticException("False cv url...");

			}

		}

		request = joinRequestRepository.save(request);

		if (request == null) {

			throw new ArithmeticException("Request is not saved...");

		}

		return request;

	}

	@Override
	@CacheEvict(value = cacheValue, allEntries = true)
	public DoctorJoinRequest updateRequest(String id, DoctorJoinRequest request, MultipartFile cv, String userId) {

		if (id == null || request == null || userId == null || !request.getUserId().equals(userId)
				|| request.getBmdcRegistrationNumber() == null || request.getQualification() == null
				|| (request.getSpecialist() == null || request.getSpecialist().isEmpty())) {

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

			DoctorJoinRequest joinRequest = joinRequestRepository.findById(id).get();

			if (joinRequest == null) {

				throw new Exception();

			}

			if (!joinRequest.getUserId().equals(user.getId())) {

				if (!joinRequest.getId().equals(id)) {

					throw new ArithmeticException();

				}

			}

		} catch (Exception e) {

			throw new NoSuchElementException("No such request find at here...");

		}

		try {

			DoctorJoinRequest joinRequest = joinRequestRepository.findByUserId(user.getId());

			if (joinRequest != null) {

				if (!joinRequest.getId().equals(id)) {

					if (!joinRequest.getId().equals(id)) {

						throw new ArithmeticException();

					}

				}

			}

		} catch (ArithmeticException e) {

			throw new ArithmeticException("This user already sent a doctor join request...");

		} catch (Exception e) {

			System.out.println(e.getMessage());

		}

		try {

			DoctorJoinRequest joinRequest = joinRequestRepository
					.findByBmdcRegistrationNumber(request.getBmdcRegistrationNumber());

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

			DoctorJoinRequest joinRequest = joinRequestRepository.findById(id).get();

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

			Doctor joinRequest = doctorRepository.findByBmdcRegistrationNumber(request.getBmdcRegistrationNumber());

			if (joinRequest != null) {

				throw new ArithmeticException();

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

					if (request.getUploadedCV() != null) {

						String bucketName = supabaseProperties.getBucket().getCv();
						String fileName = user.getUserName();
						supabaseStorageService.deleteFile(bucketName, fileName);

					}

					request.setUploadedCV(fileUrl);

				} else {

					throw new ArithmeticException("CV not uploaded...");

				}

			} else {

				throw new ArithmeticException("Please upload pdf document as the pdf or doc files....");

			}

		} else if (request.getUploadedCV() != null) {

			if (supabaseStorageService.isValidSupabaseUrl(request.getUploadedCV())) {

			} else {

				throw new ArithmeticException("False cv url...");

			}

		}

		request.setId(id);
		request.setUpdatedAt(Instant.now());

		request = joinRequestRepository.save(request);

		if (request == null) {

			throw new ArithmeticException("Request is not saved...");

		}

		return request;

	}

	@Override
	@Caching(evict = { @CacheEvict(value = cacheValue, allEntries = true),
			@CacheEvict(value = "Doctor", allEntries = true) })
	public Doctor acceptRequest(String id, String userId, boolean status) {

		if (id == null || userId == null) {

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

		UserTypes type = null;

		try {

			type = typeRepository.findByUserId(user.getId());

			if (type == null) {

				throw new Exception();

			}

			System.out.println("types has :- " + type.getType());
			System.out.println("type is admin :- " + type.getType().contains("ADMIN"));

			System.out.println("1. Type object is null? " + (type == null));
System.out.println("2. What is the raw type value? " + type.getType());
System.out.println("3. What is the class of the type? " + type.getType().getClass().getName());
System.out.println("4. What is UserType.ADMIN? " + UserType.ADMIN);

            boolean isAdmin = type.getType().stream()
    .anyMatch(t -> t.name().equals("ADMIN"));

    System.out.println("user is an admin :- " + isAdmin);


			if (!isAdmin) {

				throw new Exception();

			}

		} catch (Exception e) {

			throw new ArithmeticException("Only admin can accept any doctor");

		}

		if (!status) {

			cleaner.removeDoctorsJoinRequest(id);

			return null;

		}

		DoctorJoinRequest joinRequest = null;

		try {

			joinRequest = joinRequestRepository.findById(id).get();

			if (joinRequest == null) {

				throw new Exception();

			}

		} catch (Exception e) {

			throw new NoSuchElementException("No such request find at here...");

		}

		try {

		    if(joinRequest.getUserId().equals(userId)) {

                    throw new Exception();

		    } else {



		    }

		} catch(Exception e) {

		    throw new ArithmeticException("You can't accept you as a doctor...");

		}

		cleaner.removeDoctorsJoinRequest(id);

		Doctor doctor = new Doctor();

		doctor.setId(id);
		doctor.setUserId(joinRequest.getUserId());
		doctor.setBio(joinRequest.getBio());
		doctor.setCreatedAt(Instant.now());
		doctor.setUpdatedAt(Instant.now());
		doctor.setSpecialist(joinRequest.getSpecialist());
		doctor.setBmdcRegistrationNumber(joinRequest.getBmdcRegistrationNumber());
		doctor.setQualification(joinRequest.getQualification());
		doctor.setHospitals(new ArrayList<>());
		doctor.setUploadedCV(joinRequest.getUploadedCV());
		doctor.setAccepted_user(userId);

		doctor = doctorRepository.save(doctor);

		return doctor;
	}

	@Override
	@Cacheable(value = cacheValue, key = "'findById_' + #id")
	public DoctorJoinRequestResponse findById(String id) {

		if (id == null) {

			throw new NullPointerException("False request...");

		}

		try {

			DoctorJoinRequest request = joinRequestRepository.findById(id).get();

			if (request == null) {

				throw new Exception();

			}

			return getDoctorJoinReqeustResponse(request);

		} catch (Exception e) {

			throw new NoSuchElementException("No such request find at here...");

		}

	}

	@Override
	@Cacheable(value = cacheValue, key = "'findAll'")
	public List<DoctorJoinRequestResponse> findAll() {

		try {

			List<DoctorJoinRequest> request = joinRequestRepository.findAll();

			if (request == null || request.isEmpty()) {

				throw new Exception();

			}

			return getDoctorJoinReqeustResponse(request);

		} catch (Exception e) {

			throw new NoSuchElementException("No such request find at here...");

		}
	}

	@Override
	@Cacheable(value = cacheValue, key = "'findAllPaginated_' + #page")
	public DoctorJoinRequestPageResponse findAll(Pageable page, HttpServletRequest http) {

	    System.out.println("I am in find all service....");


		// 1. Fetch from DB (no need for try-catch just for this)
		Page<DoctorJoinRequest> request = joinRequestRepository.findAll(page);

		if (request.isEmpty()) {
			throw new NoSuchElementException("No such request found at here...");
		}

		List<DoctorJoinRequestResponse> responses = getDoctorJoinReqeustResponse(request.toList());

		// 2. Build the base URL, stripping out the old page and size params
		// (But keeping any other params like &bio=cardio)
		String baseUrl = ServletUriComponentsBuilder.fromRequestUri(http).replaceQueryParam("page") // Removes ONLY
																									// "page"
				.replaceQueryParam("size") // Removes ONLY "size"
				.toUriString();

		// 3. Check if baseUrl already has a "?" (because of other params like bio)
		// to safely append our new page/size.
		String separator = baseUrl.contains("?") ? "&" : "?";

		String nextPageUrl = request.hasNext()
				? baseUrl + separator + "page=" + (request.getNumber() + 1) + "&size=" + request.getSize()
				: null;

		String previousPageUrl = request.hasPrevious()
				? baseUrl + separator + "page=" + (request.getNumber() - 1) + "&size=" + request.getSize()
				: null;

		// 4. Return correct Total Elements
		return new DoctorJoinRequestPageResponse(responses, request.getNumber(), request.getSize(),
				request.getTotalElements(), nextPageUrl, previousPageUrl);
	}

	@Override
	@Cacheable(value = cacheValue, key = "'findByUserId_' + #userId")
	public DoctorJoinRequestResponse findByUserId(String userId) {

		if (userId == null) {

			throw new NullPointerException("False request...");

		}

		try {

			DoctorJoinRequest request = joinRequestRepository.findByUserId(userId);

			if (request == null) {

				throw new Exception();

			}

			return getDoctorJoinReqeustResponse(request);

		} catch (Exception e) {

			throw new NoSuchElementException("No such request find at here...");

		}
	}

	@Override
	@Cacheable(value = cacheValue, key = "'findByUsersId_' + #usersId")
	public List<DoctorJoinRequestResponse> findByUserIdIn(List<String> usersId) {

		if (usersId.isEmpty()) {

			throw new NullPointerException("False request...");

		}

		try {

			List<DoctorJoinRequest> request = joinRequestRepository.findByUserIdIn(usersId);

			if (request == null || request.isEmpty()) {

				throw new Exception();

			}

			return getDoctorJoinReqeustResponse(request);

		} catch (Exception e) {

			throw new NoSuchElementException("No such request find at here...");

		}
	}

	@Override
	@Cacheable(value = cacheValue, key = "'findByBioPrefix_' + #bio")
	public List<DoctorJoinRequestResponse> findByBioContainingIgnoreCase(String bio) {

		if (bio == null) {

			throw new NullPointerException("False request...");

		}

		try {

			List<DoctorJoinRequest> request = joinRequestRepository.findByBioContainingIgnoreCase(bio);

			if (request == null || request.isEmpty()) {

				throw new Exception();

			}

			return getDoctorJoinReqeustResponse(request);

		} catch (Exception e) {

			throw new NoSuchElementException("No such request find at here...");

		}
	}

	@Override
	@Cacheable(value = cacheValue, key = "'findByPaginatedBmdcRegistrationNumber_' + #bmdcRegistrationNumber + '_' + #page")
	public DoctorJoinRequestResponse findByBmdcRegistrationNumber(String bmdcRegistrationNumber) {

		if (bmdcRegistrationNumber == null) {

			throw new NullPointerException("False request...");

		}

		try {

			DoctorJoinRequest request = joinRequestRepository.findByBmdcRegistrationNumber(bmdcRegistrationNumber);

			if (request == null) {

				throw new Exception();

			}

			return getDoctorJoinReqeustResponse(request);

		} catch (Exception e) {

			throw new NoSuchElementException("No such request find at here...");

		}
	}

	@Override
	@Cacheable(value = cacheValue, key = "'findByQualificationPrefix_' + #qualification")
	public List<DoctorJoinRequestResponse> findByQualificationContainingIgnoreCase(String qualification) {

		if (qualification == null) {

			throw new NullPointerException("False request...");

		}

		try {

			List<DoctorJoinRequest> request = joinRequestRepository
					.findByQualificationContainingIgnoreCase(qualification);

			if (request == null || request.isEmpty()) {

				throw new Exception();

			}

			return getDoctorJoinReqeustResponse(request);

		} catch (Exception e) {

			throw new NoSuchElementException("No such request find at here...");

		}
	}

	@Override
	@Cacheable(value = cacheValue, key = "'findByPaginatedQualificationPrefix_' + #qualification + '_' + #page")
	public DoctorJoinRequestPageResponse findByQualificationContainingIgnoreCase(String qualification, Pageable page,
			HttpServletRequest http) {

		if (qualification == null) {

			throw new NullPointerException("False request...");

		}

		try {

			Page<DoctorJoinRequest> request = joinRequestRepository
					.findByQualificationContainingIgnoreCase(qualification, page);

			if (request == null || request.isEmpty()) {

				throw new Exception();

			}

			List<DoctorJoinRequestResponse> responses = getDoctorJoinReqeustResponse(request.toList());

			// 2. Build the base URL, stripping out the old page and size params
			// (But keeping any other params like &bio=cardio)
			String baseUrl = ServletUriComponentsBuilder.fromRequestUri(http).replaceQueryParam("page") // Removes ONLY
																										// "page"
					.replaceQueryParam("size") // Removes ONLY "size"
					.toUriString();

			// 3. Check if baseUrl already has a "?" (because of other params like bio)
			// to safely append our new page/size.
			String separator = baseUrl.contains("?") ? "&" : "?";

			String nextPageUrl = request.hasNext()
					? baseUrl + separator + "page=" + (request.getNumber() + 1) + "&size=" + request.getSize()
					: null;

			String previousPageUrl = request.hasPrevious()
					? baseUrl + separator + "page=" + (request.getNumber() - 1) + "&size=" + request.getSize()
					: null;

			// 4. Return correct Total Elements
			return new DoctorJoinRequestPageResponse(responses, request.getNumber(), request.getSize(),
					request.getTotalElements(), nextPageUrl, previousPageUrl);

		} catch (Exception e) {

			throw new NoSuchElementException("No such request find at here...");

		}
	}

	@Override
	@Cacheable(value = cacheValue, key = "'findByHospitalsPrefix_' + #hospitals")
	public List<DoctorJoinRequestResponse> findByHospitalsContainingIgnoreCase(String hospitals) {

		if (hospitals == null) {

			throw new NullPointerException("False request...");

		}

		try {

			List<DoctorJoinRequest> request = joinRequestRepository.findByHospitalsContainingIgnoreCase(hospitals);

			if (request == null || request.isEmpty()) {

				throw new Exception();

			}

			return getDoctorJoinReqeustResponse(request);

		} catch (Exception e) {

			throw new NoSuchElementException("No such request find at here...");

		}

	}

	@Override
	@Cacheable(value = cacheValue, key = "'findByPaginatedHospitalsPrefix_' + #hospitals + '_' + #page")
	public DoctorJoinRequestPageResponse findByHospitalsContainingIgnoreCase(String hospitals, Pageable page,
			HttpServletRequest http) {

		if (hospitals == null) {

			throw new NullPointerException("False request...");

		}

		try {

			Page<DoctorJoinRequest> request = joinRequestRepository.findByHospitalsContainingIgnoreCase(hospitals,
					page);

			if (request == null || request.isEmpty()) {

				throw new Exception();

			}

			List<DoctorJoinRequestResponse> responses = getDoctorJoinReqeustResponse(request.toList());

			// 2. Build the base URL, stripping out the old page and size params
			// (But keeping any other params like &bio=cardio)
			String baseUrl = ServletUriComponentsBuilder.fromRequestUri(http).replaceQueryParam("page") // Removes ONLY
																										// "page"
					.replaceQueryParam("size") // Removes ONLY "size"
					.toUriString();

			// 3. Check if baseUrl already has a "?" (because of other params like bio)
			// to safely append our new page/size.
			String separator = baseUrl.contains("?") ? "&" : "?";

			String nextPageUrl = request.hasNext()
					? baseUrl + separator + "page=" + (request.getNumber() + 1) + "&size=" + request.getSize()
					: null;

			String previousPageUrl = request.hasPrevious()
					? baseUrl + separator + "page=" + (request.getNumber() - 1) + "&size=" + request.getSize()
					: null;

			// 4. Return correct Total Elements
			return new DoctorJoinRequestPageResponse(responses, request.getNumber(), request.getSize(),
					request.getTotalElements(), nextPageUrl, previousPageUrl);

		} catch (Exception e) {

			throw new NoSuchElementException("No such request find at here...");

		}
	}

	@Override
	@Cacheable(value = cacheValue, key = "'findBySpecialistPrefix_' + #specialist")
	public List<DoctorJoinRequestResponse> findBySpecialistContainingIgnoreCase(String specialist) {

		if (specialist == null) {

			throw new NullPointerException("False request...");

		}

		try {

			List<DoctorJoinRequest> request = joinRequestRepository.findBySpecialistContainingIgnoreCase(specialist);

			if (request == null || request.isEmpty()) {

				throw new Exception();

			}

			return getDoctorJoinReqeustResponse(request);

		} catch (Exception e) {

			throw new NoSuchElementException("No such request find at here...");

		}
	}

	@Override
	@Cacheable(value = cacheValue, key = "'findByPaginatedSpecialistPrefix_' + #specialist + '_' + #page")
	public DoctorJoinRequestPageResponse findBySpecialistContainingIgnoreCase(String specialist, Pageable pageable,
			HttpServletRequest http) {

		if (specialist == null) {

			throw new NullPointerException("False request...");

		}

		try {

			Page<DoctorJoinRequest> request = joinRequestRepository.findBySpecialistContainingIgnoreCase(specialist,
					pageable);

			if (request == null || request.isEmpty()) {

				throw new Exception();

			}

			List<DoctorJoinRequestResponse> responses = getDoctorJoinReqeustResponse(request.toList());

			// 2. Build the base URL, stripping out the old page and size params
			// (But keeping any other params like &bio=cardio)
			String baseUrl = ServletUriComponentsBuilder.fromRequestUri(http).replaceQueryParam("page") // Removes ONLY
																										// "page"
					.replaceQueryParam("size") // Removes ONLY "size"
					.toUriString();

			// 3. Check if baseUrl already has a "?" (because of other params like bio)
			// to safely append our new page/size.
			String separator = baseUrl.contains("?") ? "&" : "?";

			String nextPageUrl = request.hasNext()
					? baseUrl + separator + "page=" + (request.getNumber() + 1) + "&size=" + request.getSize()
					: null;

			String previousPageUrl = request.hasPrevious()
					? baseUrl + separator + "page=" + (request.getNumber() - 1) + "&size=" + request.getSize()
					: null;

			// 4. Return correct Total Elements
			return new DoctorJoinRequestPageResponse(responses, request.getNumber(), request.getSize(),
					request.getTotalElements(), nextPageUrl, previousPageUrl);

		} catch (Exception e) {

			throw new NoSuchElementException("No such request find at here...");

		}
	}

	@Override
	@Cacheable(value = cacheValue, key = "'findByCreatedDateAfter_' + #createdAt")
	public List<DoctorJoinRequestResponse> findByCreatedAtAfter(Instant createdAt) {

		if (createdAt == null) {

			throw new NullPointerException("False request...");

		}

		try {

			List<DoctorJoinRequest> request = joinRequestRepository.findByCreatedAtAfter(createdAt);

			if (request == null || request.isEmpty()) {

				throw new Exception();

			}

			return getDoctorJoinReqeustResponse(request);

		} catch (Exception e) {

			throw new NoSuchElementException("No such request find at here...");

		}
	}

	@Override
	@Cacheable(value = cacheValue, key = "'findByPaginatedCreateddDateAfter_' + #createdAt + '_' + #page")
	public DoctorJoinRequestPageResponse findByCreatedAtAfter(Instant createdAt, Pageable page,
			HttpServletRequest http) {

		if (createdAt == null) {

			throw new NullPointerException("False request...");

		}

		try {

			Page<DoctorJoinRequest> request = joinRequestRepository.findByCreatedAtAfter(createdAt, page);

			if (request == null || request.isEmpty()) {

				throw new Exception();

			}

			List<DoctorJoinRequestResponse> responses = getDoctorJoinReqeustResponse(request.toList());

			// 2. Build the base URL, stripping out the old page and size params
			// (But keeping any other params like &bio=cardio)
			String baseUrl = ServletUriComponentsBuilder.fromRequestUri(http).replaceQueryParam("page") // Removes ONLY
																										// "page"
					.replaceQueryParam("size") // Removes ONLY "size"
					.toUriString();

			// 3. Check if baseUrl already has a "?" (because of other params like bio)
			// to safely append our new page/size.
			String separator = baseUrl.contains("?") ? "&" : "?";

			String nextPageUrl = request.hasNext()
					? baseUrl + separator + "page=" + (request.getNumber() + 1) + "&size=" + request.getSize()
					: null;

			String previousPageUrl = request.hasPrevious()
					? baseUrl + separator + "page=" + (request.getNumber() - 1) + "&size=" + request.getSize()
					: null;

			// 4. Return correct Total Elements
			return new DoctorJoinRequestPageResponse(responses, request.getNumber(), request.getSize(),
					request.getTotalElements(), nextPageUrl, previousPageUrl);

		} catch (Exception e) {

			throw new NoSuchElementException("No such request find at here...");

		}
	}

	@Override
	@Cacheable(value = cacheValue, key = "'findByCreatedDateBefore_' + #createdAt")
	public List<DoctorJoinRequestResponse> findByCreatedAtBefore(Instant createdAt) {

		if (createdAt == null) {

			throw new NullPointerException("False request...");

		}

		try {

			List<DoctorJoinRequest> request = joinRequestRepository.findByCreatedAtBefore(createdAt);

			if (request == null || request.isEmpty()) {

				throw new Exception();

			}

			return getDoctorJoinReqeustResponse(request);

		} catch (Exception e) {

			throw new NoSuchElementException("No such request find at here...");

		}
	}

	@Override
	@Cacheable(value = cacheValue, key = "'findByPaginatedCreateddDateBefore_' + #createdAt + '_' + #page")
	public DoctorJoinRequestPageResponse findByCreatedAtBefore(Instant createdAt, Pageable page,
			HttpServletRequest http) {
		if (createdAt == null) {

			throw new NullPointerException("False request...");

		}

		try {

			Page<DoctorJoinRequest> request = joinRequestRepository.findByCreatedAtBefore(createdAt, page);

			if (request == null || request.isEmpty()) {

				throw new Exception();

			}

			List<DoctorJoinRequestResponse> responses = getDoctorJoinReqeustResponse(request.toList());

			// 2. Build the base URL, stripping out the old page and size params
			// (But keeping any other params like &bio=cardio)
			String baseUrl = ServletUriComponentsBuilder.fromRequestUri(http).replaceQueryParam("page") // Removes ONLY
																										// "page"
					.replaceQueryParam("size") // Removes ONLY "size"
					.toUriString();

			// 3. Check if baseUrl already has a "?" (because of other params like bio)
			// to safely append our new page/size.
			String separator = baseUrl.contains("?") ? "&" : "?";

			String nextPageUrl = request.hasNext()
					? baseUrl + separator + "page=" + (request.getNumber() + 1) + "&size=" + request.getSize()
					: null;

			String previousPageUrl = request.hasPrevious()
					? baseUrl + separator + "page=" + (request.getNumber() - 1) + "&size=" + request.getSize()
					: null;

			// 4. Return correct Total Elements
			return new DoctorJoinRequestPageResponse(responses, request.getNumber(), request.getSize(),
					request.getTotalElements(), nextPageUrl, previousPageUrl);

		} catch (Exception e) {

			throw new NoSuchElementException("No such request find at here...");

		}
	}

	@Override
	@Cacheable(value = cacheValue, key = "'findByUpdatedDateAfter_' + #updatedAt")
	public List<DoctorJoinRequestResponse> findByUpdatedAtAfter(Instant updatedAt) {

		if (updatedAt == null) {

			throw new NullPointerException("False request...");

		}

		try {

			List<DoctorJoinRequest> request = joinRequestRepository.findByUpdatedAtAfter(updatedAt);

			if (request == null || request.isEmpty()) {

				throw new Exception();

			}

			return getDoctorJoinReqeustResponse(request);

		} catch (Exception e) {

			throw new NoSuchElementException("No such request find at here...");

		}
	}

	@Override
	@Cacheable(value = cacheValue, key = "'findByPaginatedUpdatedDateAfter_' + #updatedAt + '_' + #page")
	public DoctorJoinRequestPageResponse findByUpdatedAtAfter(Instant updatedAt, Pageable page,
			HttpServletRequest http) {

		if (updatedAt == null) {

			throw new NullPointerException("False request...");

		}

		try {

			Page<DoctorJoinRequest> request = joinRequestRepository.findByUpdatedAtAfter(updatedAt, page);

			if (request == null || request.isEmpty()) {

				throw new Exception();

			}

			List<DoctorJoinRequestResponse> responses = getDoctorJoinReqeustResponse(request.toList());

			// 2. Build the base URL, stripping out the old page and size params
			// (But keeping any other params like &bio=cardio)
			String baseUrl = ServletUriComponentsBuilder.fromRequestUri(http).replaceQueryParam("page") // Removes ONLY
																										// "page"
					.replaceQueryParam("size") // Removes ONLY "size"
					.toUriString();

			// 3. Check if baseUrl already has a "?" (because of other params like bio)
			// to safely append our new page/size.
			String separator = baseUrl.contains("?") ? "&" : "?";

			String nextPageUrl = request.hasNext()
					? baseUrl + separator + "page=" + (request.getNumber() + 1) + "&size=" + request.getSize()
					: null;

			String previousPageUrl = request.hasPrevious()
					? baseUrl + separator + "page=" + (request.getNumber() - 1) + "&size=" + request.getSize()
					: null;

			// 4. Return correct Total Elements
			return new DoctorJoinRequestPageResponse(responses, request.getNumber(), request.getSize(),
					request.getTotalElements(), nextPageUrl, previousPageUrl);

		} catch (Exception e) {

			throw new NoSuchElementException("No such request find at here...");

		}
	}

	@Override
	@Cacheable(value = cacheValue, key = "'findByUpdatedDateBefore_' + #updatedAt")
	public List<DoctorJoinRequestResponse> findByUpdatedAtBefore(Instant updatedAt) {

		if (updatedAt == null) {

			throw new NullPointerException("False request...");

		}

		try {

			List<DoctorJoinRequest> request = joinRequestRepository.findByUpdatedAtBefore(updatedAt);

			if (request == null || request.isEmpty()) {

				throw new Exception();

			}

			return getDoctorJoinReqeustResponse(request);

		} catch (Exception e) {

			throw new NoSuchElementException("No such request find at here...");

		}
	}

	@Override
	@Cacheable(value = cacheValue, key = "'findByPaginatedUpdatedDateBefore_' + #updatedAt + '_' + #page")
	public DoctorJoinRequestPageResponse findByUpdatedAtBefore(Instant updatedAt, Pageable page,
			HttpServletRequest http) {

		if (updatedAt == null) {

			throw new NullPointerException("False request...");

		}

		try {

			Page<DoctorJoinRequest> request = joinRequestRepository.findByUpdatedAtBefore(updatedAt, page);

			if (request == null || request.isEmpty()) {

				throw new Exception();

			}

			List<DoctorJoinRequestResponse> responses = getDoctorJoinReqeustResponse(request.toList());

			// 2. Build the base URL, stripping out the old page and size params
			// (But keeping any other params like &bio=cardio)
			String baseUrl = ServletUriComponentsBuilder.fromRequestUri(http).replaceQueryParam("page") // Removes ONLY
																										// "page"
					.replaceQueryParam("size") // Removes ONLY "size"
					.toUriString();

			// 3. Check if baseUrl already has a "?" (because of other params like bio)
			// to safely append our new page/size.
			String separator = baseUrl.contains("?") ? "&" : "?";

			String nextPageUrl = request.hasNext()
					? baseUrl + separator + "page=" + (request.getNumber() + 1) + "&size=" + request.getSize()
					: null;

			String previousPageUrl = request.hasPrevious()
					? baseUrl + separator + "page=" + (request.getNumber() - 1) + "&size=" + request.getSize()
					: null;

			// 4. Return correct Total Elements
			return new DoctorJoinRequestPageResponse(responses, request.getNumber(), request.getSize(),
					request.getTotalElements(), nextPageUrl, previousPageUrl);

		} catch (Exception e) {

			throw new NoSuchElementException("No such request find at here...");

		}
	}

	@Override
	@Cacheable(value = cacheValue, key = "'findByCV_' + #uploadedCV")
	public List<DoctorJoinRequestResponse> findByUploadedCV(String uploadedCV) {

		if (uploadedCV == null) {

			throw new NullPointerException("False request...");

		}

		try {

			List<DoctorJoinRequest> request = joinRequestRepository.findByUploadedCV(uploadedCV);

			if (request == null || request.isEmpty()) {

				throw new Exception();

			}

			return getDoctorJoinReqeustResponse(request);

		} catch (Exception e) {

			throw new NoSuchElementException("No such request find at here...");

		}
	}

	@Override
	@Caching(evict = { @CacheEvict(value = cacheValue, allEntries = true),
			@CacheEvict(value = "Doctor", allEntries = true) })
	public boolean removeRequest(String id, String userId) {

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

		UserTypes type = null;

		try {

			type = typeRepository.findByUserId(user.getId());

			if (type == null) {

				throw new Exception();

			}

			boolean isAdmin = type.getType().stream()
    .anyMatch(t -> t.name().equals("ADMIN"));


			if (!isAdmin) {

				throw new Exception();

			}


			if (isAdmin) {

				return cleaner.removeDoctorsJoinRequest(id);

			}

		} catch (Exception e) {

			//throw new ArithmeticException("Only admin can accept any doctor");

		}

		DoctorJoinRequest joinRequest = null;

		try {

			joinRequest = joinRequestRepository.findById(id).get();

			if (joinRequest == null) {

				throw new Exception();

			}

			if (!joinRequest.getUserId().equals(user.getId())) {

				throw new Exception();

			}

		} catch (Exception e) {

			throw new NoSuchElementException("No such request find at here...");

		}

		return cleaner.removeDoctorsJoinRequest(id);
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

	private DoctorJoinRequestResponse getDoctorJoinReqeustResponse(DoctorJoinRequest request) {

		List<DoctorJoinRequest> list = new ArrayList<>();

		list.add(request);

		return getDoctorJoinReqeustResponse(list).get(0);

	}

	private List<DoctorJoinRequestResponse> getDoctorJoinReqeustResponse(List<DoctorJoinRequest> list) {

		List<DoctorJoinRequestResponse> responses = new ArrayList<>();

		CompletableFuture<List<String>> allUserIdFuture = CompletableFuture.supplyAsync(() -> list.stream().distinct()
				.filter(Objects::nonNull).map(DoctorJoinRequest::getUserId).collect(Collectors.toList()), executors);

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

		for (DoctorJoinRequest request : list) {

			try {

				DoctorJoinRequestResponse response = new DoctorJoinRequestResponse();

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
