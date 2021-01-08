/**
 * @author proshanto
 * */

package org.opensrp.core.service;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.hibernate.SessionFactory;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.opensrp.common.interfaces.DatabaseRepository;
import org.opensrp.core.entity.Location;
import org.opensrp.core.entity.LocationTag;
import org.opensrp.core.entity.Team;
import org.opensrp.core.entity.TeamMember;
import org.opensrp.core.entity.User;
import org.opensrp.core.openmrs.service.OpenMRSServiceFactory;
import org.opensrp.core.service.mapper.TeamMemberMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.ui.ModelMap;

@Service
public class TeamMemberService {
	
	private static final Logger logger = Logger.getLogger(TeamMemberService.class);
	
	@Autowired
	private DatabaseRepository repository;
	
	@Autowired
	private SessionFactory sessionFactory;
	
	@Autowired
	private OpenMRSServiceFactory openMRSServiceFactory;
	
	@Autowired
	private UserService userServiceImpl;
	
	@Autowired
	private TeamService teamServiceImpl;
	
	@Autowired
	private LocationService locationServiceImpl;
	
	@Autowired
	private TeamMemberMapper teamMemberMapper;
	
	public TeamMemberService() {
		
	}
	
	public <T> long save(T t) throws Exception {
		TeamMember teamMember = (TeamMember) t;
		long createdTeamMember = 0;
		teamMember.setUuid(UUID.randomUUID().toString());
		createdTeamMember = repository.save(teamMember);
		
		return createdTeamMember;
	}
	
	public <T> long saveWithoutSendToOpenMRS(T t) throws Exception {
		TeamMember teamMember = (TeamMember) t;
		return repository.save(teamMember);
	}
	
	public <T> int update(T t) throws JSONException {
		TeamMember teamMember = (TeamMember) t;
		int updatedTag = 0;
		
		updatedTag = repository.update(teamMember);
		
		return updatedTag;
	}
	
	public <T> int updateWithoutSendToOpenMRS(T t) throws JSONException {
		TeamMember teamMember = (TeamMember) t;
		return repository.update(teamMember);
	}
	
	public <T> boolean delete(T t) throws JSONException {
		TeamMember teamMember = (TeamMember) t;
		boolean deletedTag = false;
		logger.info(teamMember.getUuid());
		
		deletedTag = repository.delete(teamMember);
		
		return deletedTag;
		//return repository.delete(t);
	}
	
	public <T> T findById(int id, String fieldName, Class<?> className) {
		return repository.findById(id, fieldName, className);
	}
	
	public <T> T findByForeignKey(int id, String fieldName, String className) {
		return repository.findByForeignKey(id, fieldName, className);
	}
	
	public <T> T findByKey(String value, String fieldName, Class<?> className) {
		return repository.findByKey(value, fieldName, className);
	}
	
	//this method was added on nov 28, 2018
	
	public <T> T findByKeys(Map<String, Object> fieldValues, Class<?> className) {
		return repository.findByKeys(fieldValues, className);
	}
	
	public <T> List<T> findAll(String tableClass) {
		return repository.findAll(tableClass);
	}
	
	public Map<Integer, String> getLocationTagListAsMap() {
		List<LocationTag> locationTags = findAll("LocationTag");
		Map<Integer, String> locationsTagMap = new HashMap<Integer, String>();
		for (LocationTag locationTag : locationTags) {
			locationsTagMap.put(locationTag.getId(), locationTag.getName());
			
		}
		return locationsTagMap;
	}
	
	public void setModelAttribute(ModelMap model, Team team) {
		model.addAttribute("name", team.getName());
		model.addAttribute("uniqueErrorMessage", "Specified Name already exists, please specify another");
		
	}
	
	@SuppressWarnings("null")
	public boolean isPersonAndIdentifierExists(ModelMap model, TeamMember teamMember, int[] locations) {
		boolean isExists = false;
		boolean isPersonExists = false;
		boolean isIdentifierExists = false;
		boolean isLocationsExists = false;
		if (teamMember != null) {
			isPersonExists = repository.entityExistsNotEqualThisId(teamMember.getId(), teamMember.getPerson(), "person",
			    TeamMember.class);
		}
		if (teamMember != null) {
			isIdentifierExists = repository.entityExistsNotEqualThisId(teamMember.getId(), teamMember.getIdentifier(),
			    "identifier", TeamMember.class);
		}
		if (isPersonExists) {
			model.addAttribute("uniqueNameErrorMessage", "Specified Person already exists, please specify another");
		}
		if (isIdentifierExists) {
			model.addAttribute("uniqueIdetifierErrorMessage", "Specified Identifier already exists, please specify another");
		}
		
		if (locations == null) {
			model.addAttribute("locationSelectErrorMessage", "Please Select Location");
			isLocationsExists = true;
		}
		
		if (isPersonExists || isIdentifierExists || isLocationsExists) {
			System.err.println("okkk");
			isExists = true;
		}
		
		return isExists;
	}
	
	public void setSessionAttribute(HttpSession session, TeamMember teamMember, String personName, int[] locations)
	    throws JSONException {
		
		Map<Integer, String> teams = teamServiceImpl.getTeamListAsMap();
		
		if (teamMember.getTeam() != null) {
			session.setAttribute("selectedTeamId", teamMember.getTeam().getId());
		} else {
			session.setAttribute("selectedTeamId", 0);
		}
		session.setAttribute("teams", teams);
		User person = teamMember.getPerson();
		if (person != null) {
			session.setAttribute("selectedPersonId", person.getId());
		} else {
			session.setAttribute("selectedPersonId", 0);
		}
		
		session.setAttribute("selectedLocationList", setExistingLocation(locations));
		session.setAttribute("personName", personName);
	}
	
	public String setExistingLocation(int[] locations) throws JSONException {
		JSONArray locationJsonArray = new JSONArray();
		if (locations != null && locations.length != 0) {
			
			for (int locationId : locations) {
				Location location = (Location) repository.findById(locationId, "id", Location.class);
				String locationName = locationServiceImpl.makeLocationName(location);
				JSONObject locationJsonObject = new JSONObject();
				locationJsonObject.put("value", locationName);
				locationJsonObject.put("id", location.getId());
				locationJsonArray.put(locationJsonObject);
			}
		}
		return locationJsonArray.toString();
	}
	
	public int[] getLocationIds(Set<Location> locations) {
		System.err.println("TT:" + locations.size());
		int[] locationIds = new int[locations.size()];
		if (locations != null) {
			int i = 0;
			for (Location location : locations) {
				locationIds[i] = location.getId();
				i++;
			}
		}
		return locationIds;
	}
	
	public void setModelAttribute(ModelMap model, Location location) {
		model.addAttribute("name", location.getName());
		model.addAttribute("uniqueErrorMessage", "Specified Location name already exists, please specify another");
		
	}
	
	public TeamMember setCreatorLocationAndPersonAndTeamAttributeInLocation(TeamMember teamMember, int personId, int teamId,
	                                                                        int[] locations) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		User creator = (User) repository.findByKey(auth.getName(), "username", User.class);
		User person = (User) repository.findById(personId, "id", User.class);
		Team team = (Team) repository.findById(teamId, "id", Team.class);
		Set<Location> locationSet = new HashSet<Location>();
		if (locations != null && locations.length != 0) {
			for (int locationId : locations) {
				Location location = (Location) repository.findById(locationId, "id", Location.class);
				if (location != null) {
					locationSet.add(location);
					
				}
			}
			teamMember.setLocations(locationSet);
		}
		
		teamMember.setCreator(creator);
		teamMember.setPerson(person);
		teamMember.setTeam(team);
		return teamMember;
	}
	
	public TeamMember setCreatorLocationAndPersonAndTeamAttributeInLocation(TeamMember teamMember, int personId, Team team,
	                                                                        int[] locations) {
		teamMember = teamMemberMapper.map(team, personId, locations);
		teamMember.setCreator(userServiceImpl.getLoggedInUser());
		
		return teamMember;
	}
	
	// if user comes from HRIS then there is no creator - april 23, 2019
	public TeamMember setLocationAndPersonAndTeamAttributeInLocation(TeamMember teamMember, int personId, Team team,
	                                                                 int[] locations) {
		teamMember = teamMemberMapper.map(team, personId, locations);
		return teamMember;
	}
	
	public TeamMember findByUserId(int userId) {
		return null;
	}
	// end: if user comes from HRIS then there is no creator - april 23, 2019
	
}
