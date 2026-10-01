package DTOFiles;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import ENUMS.Gender;
import ENUMS.UserType;
import jakarta.persistence.Column;
import lombok.NonNull;

public class UserResponseDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6L;

	private String id;

	private String userName;

	private String name;

	private String profileImageId;

	private String password;

	private String contactInfoId;

	private String email;

	private String phone;

	private String userGenderId;

	private Gender gender;

	private String userLocationId;

	private String locationName;

	private double lattitude, longititude;

	private String userTypeId;

	private List<UserType> type = new ArrayList<>();

	public UserResponseDTO(String id, String userName, String name, String profileImageId, String password,
			String contactInfoId, String email, String phone, String userGenderId, Gender gender, String userLocationId,
			String locationName, double lattitude, double longititude, String userTypeId, List<UserType> type) {
		super();
		this.id = id;
		this.userName = userName;
		this.name = name;
		this.profileImageId = profileImageId;
		this.password = password;
		this.contactInfoId = contactInfoId;
		this.email = email;
		this.phone = phone;
		this.userGenderId = userGenderId;
		this.gender = gender;
		this.userLocationId = userLocationId;
		this.locationName = locationName;
		this.lattitude = lattitude;
		this.longititude = longititude;
		this.userTypeId = userTypeId;
		this.type = type;
	}

	public UserResponseDTO() {
		super();
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getProfileImageId() {
		return profileImageId;
	}

	public void setProfileImageId(String profileImageId) {
		this.profileImageId = profileImageId;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getContactInfoId() {
		return contactInfoId;
	}

	public void setContactInfoId(String contactInfoId) {
		this.contactInfoId = contactInfoId;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getUserGenderId() {
		return userGenderId;
	}

	public void setUserGenderId(String userGenderId) {
		this.userGenderId = userGenderId;
	}

	public Gender getGender() {
		return gender;
	}

	public void setGender(Gender gender) {
		this.gender = gender;
	}

	public String getUserLocationId() {
		return userLocationId;
	}

	public void setUserLocationId(String userLocationId) {
		this.userLocationId = userLocationId;
	}

	public String getLocationName() {
		return locationName;
	}

	public void setLocationName(String locationName) {
		this.locationName = locationName;
	}

	public double getLattitude() {
		return lattitude;
	}

	public void setLattitude(double lattitude) {
		this.lattitude = lattitude;
	}

	public double getLongititude() {
		return longititude;
	}

	public void setLongititude(double longititude) {
		this.longititude = longititude;
	}

	public String getUserTypeId() {
		return userTypeId;
	}

	public void setUserTypeId(String userTypeId) {
		this.userTypeId = userTypeId;
	}

	public List<UserType> getType() {
		return type;
	}

	public void setType(List<UserType> type) {
		this.type = type;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	@Override
	public String toString() {
		return "UserResponseDTO [id=" + id + ", userName=" + userName + ", name=" + name + ", profileImageId="
				+ profileImageId + ", password=" + password + ", contactInfoId=" + contactInfoId + ", email=" + email
				+ ", phone=" + phone + ", userGenderId=" + userGenderId + ", gender=" + gender + ", userLocationId="
				+ userLocationId + ", locationName=" + locationName + ", lattitude=" + lattitude + ", longititude="
				+ longititude + ", userTypeId=" + userTypeId + ", type=" + type + "]";
	}

}
