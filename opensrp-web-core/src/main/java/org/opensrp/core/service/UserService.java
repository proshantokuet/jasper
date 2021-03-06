/**
 * @author proshanto
 * */

package org.opensrp.core.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.opensrp.common.dto.ChangePasswordDTO;
import org.opensrp.common.dto.LocationTreeDTO;
import org.opensrp.common.dto.SSWithUCAIdDTO;
import org.opensrp.common.dto.UserAssignedLocationDTO;
import org.opensrp.common.dto.UserDTO;
import org.opensrp.common.exception.BadFormatException;
import org.opensrp.common.exception.BranchNotFoundException;
import org.opensrp.common.exception.LocationNotFoundException;
import org.opensrp.common.interfaces.DatabaseRepository;
import org.opensrp.core.dto.UserLocationDTO;
import org.opensrp.core.dto.WorkerIdDTO;
import org.opensrp.core.entity.Branch;
import org.opensrp.core.entity.Facility;
import org.opensrp.core.entity.FacilityWorker;
import org.opensrp.core.entity.FacilityWorkerType;
import org.opensrp.core.entity.Imei;
import org.opensrp.core.entity.Location;
import org.opensrp.core.entity.Role;
import org.opensrp.core.entity.Team;
import org.opensrp.core.entity.TeamMember;
import org.opensrp.core.entity.User;
import org.opensrp.core.entity.UsersCatchmentArea;
import org.opensrp.core.openmrs.service.OpenMRSServiceFactory;
import org.opensrp.core.service.mapper.UserMapper;
import org.opensrp.core.service.mapper.UsersCatchmentAreaMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {
	
	private static final Logger logger = Logger.getLogger(UserService.class);
	
	@Autowired
	private RoleService roleService;
	
	@Autowired
	private TeamService teamService;
	
	@Autowired
	private SessionFactory sessionFactory;
	
	@Autowired
	private TeamMemberService teamMemberServiceImpl;
	
	@Autowired
	private FacilityWorkerTypeService facilityWorkerTypeService;
	
	@Autowired
	private DatabaseRepository repository;
	
	@Autowired
	private OpenMRSServiceFactory openMRSServiceFactory;
	
	@Autowired
	private RoleService roleServiceImpl;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Autowired
	private EmailService emailService;
	
	@Autowired
	private FacilityService facilityService;
	
	@Autowired
	private UsersCatchmentAreaService usersCatchmentAreaService;
	
	@Autowired
	private UsersCatchmentAreaMapper usersCatchmentAreaMapper;
	
	@Autowired
	private LocationService locationServiceImpl;
	
	@Autowired
	private UserMapper userMapper;
	
	@Autowired
	private BranchService branchService;
	
	public Session getSessionFactory() {
		return sessionFactory.getCurrentSession();
	}
	
	public <T> long save(T t, boolean isUpdate) throws Exception {
		User user = (User) t;
		long createdUser = 0;
		
		user.setProvider(true);
		
		if (!isUpdate) {
			user.setPassword(passwordEncoder.encode(user.getPassword()));
			user.setUuid(UUID.randomUUID().toString());
			user.setPersonUUid(UUID.randomUUID().toString());
		}
		createdUser = repository.save(user);
		
		return createdUser;
	}
	
	@Transactional
	public User save(User user) throws Exception {
		Session session = sessionFactory.getCurrentSession();
		
		session.save(user);
		logger.info("saved successfully: " + user.getClass().getName());
		
		return user;
	}
	
	@Transactional
	public User update(User user) throws Exception {
		Session session = sessionFactory.getCurrentSession();
		
		session.update(user);
		logger.info("updated successfully: " + user.getClass().getName());
		
		return user;
	}
	
	@Transactional
	public User saveNew(User user, boolean isUpdate) throws Exception {
		User createdUser = new User();
		
		user.setProvider(true);
		
		if (!isUpdate) {
			user.setPassword(passwordEncoder.encode(user.getPassword()));
			user.setUuid(UUID.randomUUID().toString());
			user.setPersonUUid(UUID.randomUUID().toString());
		}
		createdUser = save(user);
		
		return createdUser;
	}
	
	@Transactional
	public User updateNew(User user, boolean isUpdate) throws Exception {
		User createdUser = new User();
		
		user.setProvider(true);
		user.setUuid(UUID.randomUUID().toString());
		user.setPersonUUid(UUID.randomUUID().toString());
		
		if (!isUpdate) {
			user.setPassword(passwordEncoder.encode(user.getPassword()));
		}
		createdUser = update(user);
		
		return createdUser;
	}
	
	public void setToasterToSession(HttpSession session, String heading, String message) {
		session.setAttribute("heading", heading);
		session.setAttribute("toastMessage", message);
		session.setAttribute("icon", heading.toLowerCase());
	}
	
	@Transactional
	public String changePassword(HttpSession session, ChangePasswordDTO dto) {
		try {
			User user = findByKey(dto.getUsername(), "username", User.class);
			Set<Role> roles = user.getRoles();
			Role firstRole = roles.iterator().next();
			String role = firstRole.getName();
			
			dto.setPassword(passwordEncoder.encode(dto.getPassword()));
			repository.updatePassword(dto);
			
			String message = "Your Password has been changed successfully!";
			setToasterToSession(session, "Success", message);
		}
		catch (Exception e) {
			e.printStackTrace();
			String message = "Your password not changed. Please try later.";
			setToasterToSession(session, "Error", message);
		}
		
		return null;
	}
	
	@Transactional
	public <T> int update(T t, String pass) throws Exception {
		User user = (User) t;
		if (!pass.equalsIgnoreCase("CCBBAAaaaaaaaaaaaaaaaaBBAACC")) {
			user.setPassword(passwordEncoder.encode(pass));
		}
		
		return repository.update(user);
	}
	
	public <T> boolean delete(T t) {
		return repository.delete(t);
	}
	
	public <T> T findById(int id, String fieldName, Class<?> className) {
		return repository.findById(id, fieldName, className);
		
	}
	
	public <T> T findByKey(String value, String fieldName, Class<?> className) {
		return repository.findByKey(value, fieldName, className);
	}
	
	public <T> List<T> findAll(String tableClass) {
		return repository.findAll(tableClass);
	}
	
	public <T> T findOneByKeys(Map<String, Object> fielaValues, Class<?> className) {
		return repository.findByKeys(fielaValues, className);
	}
	
	public Set<Role> setRoles(String[] selectedRoles) {
		Set<Role> roles = new HashSet<Role>();
		if (selectedRoles != null) {
			for (String roleId : selectedRoles) {
				Role role = repository.findById(Integer.parseInt(roleId), "id", Role.class);
				roles.add(role);
			}
		}
		return roles;
	}
	
	public String getRole(Set<Role> roles) {
		Iterator iter = roles.iterator();
		Role role = (Role) iter.next();
		return role.getName();
	}
	
	public Set<Branch> setBranches(String[] selectedBranches) {
		Set<Branch> branches = new HashSet<>();
		if (selectedBranches != null) {
			for (String branchId : selectedBranches) {
				Branch branch = repository.findById(Integer.parseInt(branchId), "id", Branch.class);
				branches.add(branch);
			}
		}
		return branches;
	}
	
	public Set<Branch> setBranch(String selectedBranches) {
		Set<Branch> branches = new HashSet<>();
		if (selectedBranches != null) {
			
			Branch branch = repository.findById(Integer.parseInt(selectedBranches), "id", Branch.class);
			branches.add(branch);
			
		}
		return branches;
	}
	
	public boolean isPasswordMatched(User account) {
		return passwordEncoder.matches(account.getRetypePassword(), passwordEncoder.encode(account.getPassword()));
	}
	
	public boolean isUserExist(String userName) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("username", userName);
		return repository.isExists(params, User.class);
	}
	
	public User convert(UserDTO userDTO) {
		User user = new User();
		String[] roles = userDTO.getRoles().split(",");
		user.setUsername(userDTO.getUsername());
		user.setEmail(userDTO.getEmail());
		user.setEnabled(true);
		user.setFirstName(userDTO.getFirstName());
		user.setGender("");
		user.setIdetifier(userDTO.getIdetifier());
		user.setLastName(userDTO.getLastName());
		user.setMobile(userDTO.getMobile());
		user.setPassword(userDTO.getPassword());
		user.setRoles(setRoles(roles));
		User parentUser = findById(userDTO.getParentUser(), "id", User.class);
		user.setParentUser(parentUser);
		
		return user;
		
	}
	
	// for setting user attributes from jsonObject -- April 10, 2019
	public User setUserInfoFromJSONObject(String username, JSONObject inputJSONObject, String password, Facility facility)
	    throws Exception {
		String facilityHeadDesignation = "Community Health Care Provider";
		logger.info("\nfacilityHeadDesignation : " + facilityHeadDesignation + "\n");
		User user = null;
		user = new User();
		Role roleOfCHCP = roleService.findByKey("CHCP", "name", Role.class);
		logger.info("\n Role Of CHCP : " + roleOfCHCP.toString() + "\n");
		String roleId = roleOfCHCP.getId() + "";
		//String[] roles = { "7" };
		String[] roles = { roleId };
		user.setUsername(username);
		if (username != null && !username.isEmpty()) {
			user.setEmail(username);
		}
		user.setEnabled(true);
		String facilityHeadIdentifier = inputJSONObject.getString("facility_head_provider_id");
		String facilityHeadName = inputJSONObject.getString("facility_head_provider_name");
		String[] nameArray = facilityHeadName.split("\\s+");
		String firstName = nameArray[0];
		String lastName = nameArray[nameArray.length - 1];
		if (firstName != null && !firstName.isEmpty()) {
			user.setFirstName(firstName);
		}
		if (lastName != null && !lastName.isEmpty()) {
			user.setLastName(lastName);
		}
		user.setGender("");
		user.setIdetifier("");
		String mobileNumber = inputJSONObject.getString("mobile1");
		if (mobileNumber != null && !mobileNumber.isEmpty()) {
			user.setMobile(mobileNumber);
		} else {
			user.setMobile("");
		}
		user.setPassword(password);
		user.setRoles(setRoles(roles));
		// User parentUser = findById(userDTO.getParentUser(), "id",User.class);
		// user.setParentUser("");
		
		// from user rest controller -- April 11, 2019
		//		user.setChcp(facility.getId() + "");
		logger.info(" \nUser : " + user.toString() + "\n");
		//		int numberOfUserSaved = (int) save(user, false);
		//		logger.info("\nNumUSER: " + numberOfUserSaved + " \nUser : " + user.toString() + "\n");
		
		// get facility by name from team table and then add it to team member
		Team team = new Team();
		TeamMember teamMember = new TeamMember();
		team = teamService.findByKey(facility.getName(), "name", Team.class);
		logger.info(" \nTeam : " + team.toString() + "\n");
		
		int[] locations = new int[5];
		locations[0] = team.getLocation().getId();
		user = findById(user.getId(), "id", User.class);
		logger.info(" \nUser(find by id from DB) : " + user.toString() + "\n");
		teamMember = teamMemberServiceImpl.setLocationAndPersonAndTeamAttributeInLocation(teamMember, user.getId(), team,
		    locations);
		teamMember.setIdentifier(facilityHeadIdentifier);
		logger.info(" \nTeamMember : " + teamMember.toString() + "\n");
		teamMemberServiceImpl.save(teamMember);
		
		FacilityWorker facilityWorker = new FacilityWorker();
		facilityWorker.setName(user.getFullName());
		facilityWorker.setIdentifier(user.getMobile());
		facilityWorker.setOrganization("Community Clinic");
		FacilityWorkerType facilityWorkerType = facilityWorkerTypeService
		        .findByKey("CHCP", "name", FacilityWorkerType.class);
		facilityWorker.setFacility(facility);
		facilityWorker.setFacilityWorkerType(facilityWorkerType);
		logger.info(" \nFacilityWorkerType : " + facilityWorkerType.toString() + "\n");
		facilityWorkerTypeService.save(facilityWorker);
		
		String mailBody = "Dear " + user.getFullName()
		        + ",\n\nYour login credentials for CBHC are given below -\nusername : " + user.getUsername()
		        + "\npassword : " + password;
		//		if (numberOfUserSaved > 0) {
		//			logger.info("<><><><><> in user rest controller before sending mail to-" + user.getEmail());
		//			emailService.sendSimpleMessage(user.getEmail(), "Login credentials for CBHC", mailBody);
		//
		//		}
		return user;
	}
	
	// end: setting user attributes from jsonObject
	
	public int[] getSelectedRoles(User account) {
		int[] selectedRoles = new int[200];
		Set<Role> getRoles = account.getRoles();
		int i = 0;
		for (Role role : getRoles) {
			selectedRoles[i] = role.getId();
			i++;
		}
		return selectedRoles;
	}
	
	public User getLoggedInUser() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		User user = repository.findByKey(auth.getName(), "username", User.class);
		return user;
	}
	
	@Transactional
	public <T> int updatePassword(T t) throws Exception {
		int updatedUser = 0;
		User user = (User) t;
		Set<Role> roles = user.getRoles();
		
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		updatedUser = repository.update(user);
		user.setProvider(false);
		
		return updatedUser;
	}
	
	public Map<Integer, String> getUserListAsMap() {
		List<User> users = findAll("User");
		Map<Integer, String> usersMap = new HashMap<Integer, String>();
		for (User user : users) {
			usersMap.put(user.getId(), user.getUsername());
			
		}
		return usersMap;
	}
	
	public List<User> findAllByKeysWithALlMatches(String name, boolean isProvider) {
		Map<String, String> fielaValues = new HashMap<String, String>();
		fielaValues.put("username", name);
		return repository.findAllByKeysWithALlMatches(isProvider, fielaValues, User.class);
	}
	
	public Map<Integer, String> getProviderListAsMap() {
		Map<String, String> fielaValues = new HashMap<String, String>();
		boolean isProvider = true;
		List<User> users = repository.findAllByKeysWithALlMatches(isProvider, fielaValues, User.class);
		Map<Integer, String> usersMap = new HashMap<Integer, String>();
		if (users != null) {
			for (User user : users) {
				usersMap.put(user.getId(), user.getUsername());
				
			}
		}
		return usersMap;
	}
	
	/**
	 * <p>
	 * This method set roles attribute to session, all roles and selected roles.
	 * </p>
	 *
	 * @param roles list of selected roles.
	 * @param session is an argument to the HttpSession's session .
	 */
	
	public List<Role> setRolesAttributes(int[] roles, HttpSession session) {
		//session.setAttribute("roles", repository.findAll("Role"));
		//fetch active roles to show on user edit view
		Map<String, Object> findCriteriaMap = new HashMap<String, Object>();
		findCriteriaMap.put("active", true);
		List<Role> activeRoles = repository.findAllByKeys(findCriteriaMap, Role.class);
		session.setAttribute("roles", activeRoles);
		session.setAttribute("selectedRoles", roles);
		return activeRoles;
	}
	
	public JSONArray getUserDataAsJson(String parentIndication, String parentKey) throws JSONException {
		JSONArray dataArray = new JSONArray();
		
		List<User> users = findAll("User");
		for (User user : users) {
			JSONObject dataObject = new JSONObject();
			dataObject.put("id", user.getId());
			User parentUser = user.getParentUser();
			if (parentUser != null) {
				dataObject.put(parentKey, parentUser.getId());
			} else {
				dataObject.put(parentKey, parentIndication);
			}
			dataObject.put("text", user.getFullName());
			dataArray.put(dataObject);
		}
		
		return dataArray;
		
	}
	
	public boolean deleteMHV(WorkerIdDTO workerIdDTO) throws JSONException {
		FacilityWorker facilityWorker = facilityWorkerTypeService.findById(workerIdDTO.getWorkerId(), "id",
		    FacilityWorker.class);
		Facility facility = new Facility();
		if (facilityWorker != null)
			facilityService.findById(facilityWorker.getFacility().getId(), "id", Facility.class);
		User user = new User();
		if (facility != null)
			findByKey(String.valueOf(facility.getId()), "chcp", User.class);
		TeamMember teamMember = new TeamMember();
		if (user != null)
			teamMemberServiceImpl.findByKey(String.valueOf(user.getId()), "person_id", TeamMember.class);
		
		String teamMemberDeleteStatus = openMRSServiceFactory.getOpenMRSConnector("member").delete(teamMember.getUuid());
		
		if (user != null)
			openMRSServiceFactory.getOpenMRSConnector("user").delete(user.getUuid());
		
		return true;
	}
	
	public String updateTeamMemberAndCatchmentAreas(HttpSession session, UserLocationDTO userLocationDTO) throws Exception {
		int parentId = 0;
		String errorMessage = "";
		TeamMember teamMember = teamMemberServiceImpl.findByForeignKey(userLocationDTO.getUserId(), "person_id",
		    "TeamMember");
		try {
			Integer isDeleted = 0;
			if (userLocationDTO.getAllLocation() != null && userLocationDTO.getAllLocation().length > 0) {
				int locationId = userLocationDTO.getAllLocation()[0];
				Location location = locationServiceImpl.findById(locationId, "id", Location.class);
				if (location != null) {
					parentId = location.getParentLocation().getId();
					
					Map<String, Object> map = new HashMap<>();
					map.put("userId", userLocationDTO.getUserId());
					map.put("parentLocationId", parentId);
					List<UsersCatchmentArea> currentAreas = usersCatchmentAreaService.findAllByKeys(map);
					List<Integer> currentLocationIds = new ArrayList<>();
					Set<Location> locationSet = new HashSet<>();
					
					if (currentAreas != null) {
						for (UsersCatchmentArea area : currentAreas) {
							currentLocationIds.add(area.getLocationId());
						}
						List<Location> currentLocations = locationServiceImpl.findAllById(currentLocationIds, "id",
						    "Location");
						
						for (Location l : currentLocations) {
							if (l.getParentLocation().getId() != parentId) {
								locationSet.add(l);
							}
						}
					}
					
					List<Integer> locationIds = new ArrayList<Integer>();
					if (userLocationDTO.getLocations() != null) {
						for (Integer id : userLocationDTO.getLocations()) {
							locationIds.add(id);
						}
						List<Location> newLocations = locationServiceImpl.findAllById(locationIds, "id", "Location");
						
						//updating parent of ss when update the location of sk
						if (userLocationDTO.getRole().equalsIgnoreCase("SK")) {
							repository.updateSSParentBySKAndLocation(userLocationDTO.getUserId(), 29, locationIds);
						}
						
						for (Location l : newLocations) {
							locationSet.add(l);
						}
					}
					
					teamMember.setLocations(locationSet);
					
					isDeleted = usersCatchmentAreaService.deleteAllByParentAndUser(parentId, userLocationDTO.getUserId());
				}
			}
			
			teamMemberServiceImpl.updateWithoutSendToOpenMRS(teamMember);
			if (userLocationDTO.getLocations() != null && userLocationDTO.getLocations().length > 0) {
				List<UsersCatchmentArea> usersCatchmentAreas = usersCatchmentAreaMapper.map(userLocationDTO.getLocations(),
				    userLocationDTO.getUserId());
				usersCatchmentAreaService.saveAll(usersCatchmentAreas);
			}
			String message = "Location updated successfully!";
			setToasterToSession(session, "Success", message);
		}
		catch (Exception e) {
			e.printStackTrace();
			errorMessage = "something went wrong";
			String message = "Location updated failed!";
			setToasterToSession(session, "Error", message);
		}
		return errorMessage;
	}
	
	public String saveTeamMemberAndCatchmentAreas(HttpSession session, UserLocationDTO userLocationDTO) throws Exception {
		String teamName = "HNPP-BRAC";
		String errorMessage = "";
		Team team = teamService.findByKey(teamName, "name", Team.class);
		TeamMember teamMember = new TeamMember();
		try {
			teamMember = teamMemberServiceImpl.setLocationAndPersonAndTeamAttributeInLocation(teamMember,
			    userLocationDTO.getUserId(), team, userLocationDTO.getLocations());
			
			TeamMember isExist = teamMemberServiceImpl.findByForeignKey(userLocationDTO.getUserId(), "person_id",
			    "TeamMember");
			
			if (isExist == null) {
				teamMemberServiceImpl.saveWithoutSendToOpenMRS(teamMember);
			} else {
				isExist.setLocations(teamMember.getLocations());
				teamMemberServiceImpl.updateWithoutSendToOpenMRS(isExist);
			}
			List<UsersCatchmentArea> usersCatchmentAreas = usersCatchmentAreaMapper.map(userLocationDTO.getLocations(),
			    userLocationDTO.getUserId());
			usersCatchmentAreaService.saveAll(usersCatchmentAreas);
			String message = "Location saved successfully!";
			setToasterToSession(session, "Success", message);
		}
		catch (Exception e) {
			errorMessage = "something went wrong";
			e.printStackTrace();
			String message = "Location saved failed!";
			setToasterToSession(session, "Error", message);
		}
		return errorMessage;
	}
	
	public List<Object[]> getUsersCatchmentAreaTableAsJson(int userId) {
		return usersCatchmentAreaService.getUsersCatchmentAreaTableAsJson(userId);
	}
	
	public List<Object[]> getCatchmentAreaTableForUser(int userId) {
		return usersCatchmentAreaService.getCatchmentAreaForUserAsJson(userId);
	}
	
	public List<LocationTreeDTO> getProviderLocationTreeByChildRole(int memberId, int childRoleId) {
		return repository.getProviderLocationTreeByChildRole(memberId, childRoleId);
	}
	
	private void updateOfUploadedCatchmentArea(UserLocationDTO userLocationDTO) throws Exception {
		int locationId = userLocationDTO.getLocations()[0];
		int userId = userLocationDTO.getUserId();
		List<UsersCatchmentArea> areas = repository.findAllByForeignKey(userId, "user_id", "UsersCatchmentArea");
		boolean isExistLocation = false;
		for (UsersCatchmentArea area : areas) {
			if (area.getLocationId() == locationId)
				isExistLocation = true;
		}
		if (!isExistLocation) {
			
			TeamMember teamMember = repository.findByForeignKey(userId, "person_id", "TeamMember");
			Location location = repository.findById(locationId, "id", Location.class);
			Set<Location> locationSet = teamMember.getLocations();
			locationSet.add(location);
			teamMember.setLocations(locationSet);
			UsersCatchmentArea usersCatchmentArea = usersCatchmentAreaMapper.map(location, userId);
			repository.update(teamMember);
			repository.save(usersCatchmentArea);
		}
	}
	
	public String uploadUser(HttpSession session, File csvFile) throws Exception {
		BufferedReader br = null;
		String cvsSplitBy = ";";
		String msg = "";
		String line = "";
		
		int position = 0;
		
		br = new BufferedReader(new FileReader(csvFile));
		
		while ((line = br.readLine()) != null) {
			String[] users = line.split(cvsSplitBy);
			if (position == 0) {
				position++;
				continue;
			} else {
				UserDTO userDTO = new UserDTO();
				if (users[0].length() > 0 && !users[0].equalsIgnoreCase("none")) {
					users[0] = users[0].trim();
					String fullName[] = users[0].split(" ");
					userDTO.setFirstName(fullName[0]);
					
					if (fullName.length > 1)
						userDTO.setLastName(fullName[1]);
					else if (fullName.length > 2)
						userDTO.setLastName(fullName[1] + " " + fullName[2]);
					else
						userDTO.setLastName(".");
					
					userDTO.setMobile(users[2].trim());
					Role role = repository.findByKey(users[3], "name", Role.class);
					String roles = String.valueOf(role.getId());
					userDTO.setRoles(roles);
					
					if (users[3].trim().equalsIgnoreCase("SK")) {
						userDTO.setPassword(userDTO.getMobile().substring(7));
						userDTO.setUsername(users[7].trim());
					} else if (users[3].trim().equalsIgnoreCase("SS")) {
						userDTO.setPassword("brac123456");
						userDTO.setUsername(users[7].trim());
					}
					
					userDTO.setEmail("");
					Branch branch = repository.findByKey(users[4], "code", Branch.class);
					String branches = "";
					if (branch != null) {
						branches = String.valueOf(branch.getId());
					} else {
						String errorMessage = "Branch: " + users[4].trim() + " not present in database. Please check line "
						        + (position + 1) + " of the csv file ";
						throw new BranchNotFoundException(errorMessage);
					}
					List<LocationTreeDTO> locations = repository.getUniqueLocation(users[5].toUpperCase(),
					    users[6].toUpperCase());
					User user = userMapper.map(userDTO);
					User isExists = repository.findByKey(user.getUsername(), "username", User.class);
					
					if (locations != null && locations.size() > 0) {
						if (isExists == null) {
							if (!users[3].trim().equalsIgnoreCase("SS")) {
								user = (User) openMRSServiceFactory.getOpenMRSConnector("user").add(user);
							}
							if ((user != null && !user.getUuid().isEmpty()) || users[3].trim().equalsIgnoreCase("SS")) {
								user.setPassword(passwordEncoder.encode(user.getPassword()));
								repository.save(user);
								User newUser = repository.findByKey(user.getUsername(), "username", User.class);
								logger.info("created new user:" + user.getUsername());
								int[] locationsForSave = new int[1];
								locationsForSave[0] = locations.get(0).getId();
								UserLocationDTO userLocationDTO = new UserLocationDTO();
								userLocationDTO.setUserId(newUser.getId());
								userLocationDTO.setLocations(locationsForSave);
								saveTeamMemberAndCatchmentAreas(session, userLocationDTO);
							} else {
								String errorMessage = "OpenMRS: Bad format found for this user. Please check line "
								        + (position + 1) + " of the csv file ";
								throw new BadFormatException(errorMessage);
							}
						} else {
							try {
								int[] locationsForSave = new int[1];
								locationsForSave[0] = locations.get(0).getId();
								UserLocationDTO userLocationDTO = new UserLocationDTO();
								userLocationDTO.setUserId(isExists.getId());
								userLocationDTO.setLocations(locationsForSave);
								updateOfUploadedCatchmentArea(userLocationDTO);
							}
							catch (Exception e) {
								e.printStackTrace();
								logger.info(e.fillInStackTrace());
							}
						}
					} else {
						String errorMessage = "Village: " + users[5].trim() + " and Union: " + users[6]
						        + " pair not present in database. Please check line " + (position + 1) + " of the csv file ";
						throw new LocationNotFoundException(errorMessage);
					}
				}
			}
			position++;
		}
		return msg;
	}
	
	public String uploadImei(HttpSession session, File csvFile) throws Exception {
		BufferedReader br = null;
		String cvsSplitBy = ",";
		String msg = "";
		int position = 0;
		String line = "";
		
		br = new BufferedReader(new FileReader(csvFile));
		
		List<Imei> imeis = new ArrayList<>();
		
		while ((line = br.readLine()) != null) {
			String[] imeiRecord = line.split(cvsSplitBy);
			if (position == 0) {
				position++;
				continue;
			} else {
				String imei1 = (imeiRecord.length > 0 && !StringUtils.isBlank(imeiRecord[0])) ? imeiRecord[0].trim() : "";
				String imei2 = (imeiRecord.length > 1 && !StringUtils.isBlank(imeiRecord[1])) ? imeiRecord[1].trim() : "";
				if (imei1 != null || imei2 != null) {
					Imei imei = new Imei();
					imei.setImei1(imei1);
					imei.setImei2(imei2);
					imei.setCreated(new Date());
					imei.setUpdated(new Date());
					imeis.add(imei);
				} else {
					throw new BadFormatException("Bad format found at line -" + (position + 1));
				}
			}
			position++;
		}
		long result = repository.saveAll(imeis);
		if (result > 0)
			msg = result + " record(s) saved successfully";
		else
			msg = "Saving failed!!! Please try again later...";
		
		return msg;
	}
	
	public List<Object[]> getUserListByFilterString(int locationId, int locationTagId, int roleId, int branchId,
	                                                String name, int limit, int offset, String orderColumn,
	                                                String orderDirection) {
		return repository.getUserListByFilterString(locationId, locationTagId, roleId, branchId, name, limit, offset,
		    orderColumn, orderDirection);
	}
	
	@SuppressWarnings("unchecked")
	@Transactional
	public List<String> getUserList(JSONObject json) {
		Session session = getSessionFactory();
		List<String> list = new ArrayList<String>();
		String hql = "select  *   from core.get_user_lists('" + json + "')";
		
		Query query = session.createSQLQuery(hql);
		list = query.list();
		return list;
	}
	
	@Transactional
	public int getUserListCount(JSONObject json) {
		Session session = getSessionFactory();
		BigInteger total = null;
		String hql = "select  *   from core.get_user_lists_count('" + json + "')";
		Query query = session.createSQLQuery(hql);
		total = (BigInteger) query.uniqueResult();
		
		return total.intValue();
	}
	
	public List<Object[]> getUserListWithoutCatchmentArea(int roleId, int branchId, String name, Integer limit,
	                                                      Integer offset, String orderColumn, String orderDirection) {
		return repository
		        .getUserListWithoutCatchmentArea(roleId, branchId, name, limit, offset, orderColumn, orderDirection);
	}
	
	public <T> T getUserListByFilterStringCount(int locationId, int locationTagId, int roleId, int branchId, String name,
	                                            int limit, int offset) {
		return repository.getUserListByFilterStringCount(locationId, locationTagId, roleId, branchId, name, limit, offset);
	}
	
	public <T> T getUserListWithoutCatchmentAreaCount(int roleId, int branchId, String name) {
		return repository.getUserListWithoutCatchmentAreaCount(roleId, branchId, name);
	}
	
	public List<UserAssignedLocationDTO> assignedLocationByRole(Integer roleId) {
		return repository.assignedLocationByRole(roleId);
	}
	
	public List<UserDTO> getChildUserFromParent(Integer userId, String roleName) {
		return roleName.equalsIgnoreCase("SK") ? repository.getChildUserByParentUptoUnion(userId, roleName) : repository
		        .getChildUserByParentUptoVillage(userId, roleName);
	}
	
	public List<UserDTO> getSSWithoutCatchmentArea(Integer userId) {
		return repository.getSSWithoutCatchmentAreaByAM(userId);
	}
	
	public boolean checkImei(String imei) {
		return repository.isExistsCustom(imei, Imei.class);
	}
	
	public List<SSWithUCAIdDTO> getSSListByLocation(Integer locationId, Integer roleId) {
		return repository.getSSListByLocation(locationId, roleId);
	}
	
	public Integer updateParentForSS(Integer ssId, String parentUsername) throws Exception {
		Integer isUpdate = 0;
		try {
			User sk = findByKey(parentUsername, "username", User.class);
			User ss = findById(ssId, "id", User.class);
			List<UsersCatchmentArea> areas = usersCatchmentAreaService.findAllByForeignKey(ssId, "user_id",
			    "UsersCatchmentArea");
			if (areas != null) {
				if (ss.getParentUser() != null) {
					List<Integer> locationIds = new ArrayList<>();
					for (UsersCatchmentArea area : areas) {
						locationIds.add(area.getLocationId());
					}
					usersCatchmentAreaService.deleteAllByKeys(locationIds, ss.getParentUser().getId()); // delete ss location from previous sk (parent)
				}
				List<UsersCatchmentArea> newParentAreas = prepareCatchmentArea(areas, sk.getId());
				usersCatchmentAreaService.saveAll(newParentAreas); // insert ss location to new sk (parent)
			}
			Set<Branch> branches = new HashSet<>();
			//			for (Branch b: sk.getBranches()) {
			//				branches.add(b);
			//			}
			//			if (branches != null) {
			//				ss.setBranches(branches);
			//				isUpdate = update(ss); //updating branch
			//			}
			repository.updateParentForSS(ssId, sk.getId());
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return isUpdate;
	}
	
	List<UsersCatchmentArea> prepareCatchmentArea(List<UsersCatchmentArea> ssAreas, Integer parentId) {
		List<UsersCatchmentArea> newAreas = new ArrayList<>();
		for (UsersCatchmentArea area : ssAreas) {
			UsersCatchmentArea newArea = new UsersCatchmentArea();
			newArea.setUserId(parentId);
			newArea.setLocationId(area.getLocationId());
			newArea.setCreated(new Date());
			newArea.setUpdated(new Date());
			newArea.setParentLocationId(area.getParentLocationId());
			newAreas.add(newArea);
		}
		return newAreas;
	}
	
	public JSONObject getUserDataOfDataTable(Integer draw, Integer totalUser, List<String> users, boolean editPermitted)
	    throws JSONException {
		JSONObject response = new JSONObject();
		response.put("draw", draw + 1);
		response.put("recordsTotal", totalUser);
		response.put("recordsFiltered", totalUser);
		JSONArray array = new JSONArray();
		for (String user : users) {
			JSONObject json = new JSONObject(user);
			
			JSONArray person = new JSONArray();
			person.put(json.opt("full_name"));
			person.put(json.opt("role_name")); //role
			person.put((json.opt("location_name") != "") ? json.opt("location_name") : "N/A");
			person.put(json.opt("username"));
			person.put(json.opt("mobile")); //phone number
			person.put(json.opt("email"));
			person.put(json.opt("joining_date"));
			String edit = editPermitted ? "<a href='/opensrp-dashboard/user/edit-ajax.html?id=" + json.opt("id")
			        + "&lang=en'>Edit</a>" : "";
			String actions = edit; //buttons
			person.put(actions);
			array.put(person);
		}
		response.put("data", array);
		return response;
	}
	
}
