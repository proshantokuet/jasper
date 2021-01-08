/**
 * @author proshanto
 * */

package org.opensrp.core.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.hibernate.type.StandardBasicTypes;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.opensrp.common.dto.LocationDTO;
import org.opensrp.common.dto.LocationTreeDTO;
import org.opensrp.common.interfaces.DatabaseRepository;
import org.opensrp.common.interfaces.LocationRepository;
import org.opensrp.common.util.TreeNode;
import org.opensrp.core.dto.LocationHierarchyDTO;
import org.opensrp.core.entity.Location;
import org.opensrp.core.entity.LocationTag;
import org.opensrp.core.entity.User;
import org.opensrp.core.openmrs.service.OpenMRSServiceFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.ui.ModelMap;

@Service
public class LocationService {
	
	private static final Logger logger = Logger.getLogger(LocationService.class);
	
	@Autowired
	private DatabaseRepository repository;
	
	@Autowired
	private LocationRepository locationRepository;
	
	@Autowired
	private SessionFactory sessionFactory;
	
	@Autowired
	private OpenMRSServiceFactory openMRSServiceFactory;
	
	@Autowired
	private LocationTagService locationTagServiceImpl;
	
	public LocationService() {
		
	}
	
	public List<Object[]> getLocationByTagId(int tagId) {
		String sqlQuery = "SELECT location.name, location.id from core.location WHERE location_tag_id=:location_tag_id";
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("location_tag_id", tagId);
		return repository.executeSelectQuery(sqlQuery, params);
	}
	
	public <T> List<T> getLocationByTag(int tagId) {
		String sqlQuery = "from Location WHERE location_tag_id=" + tagId;
		return repository.executeQuery(sqlQuery);
	}
	
	public List<Object[]> getChildData(int parentId) {
		String sqlQuery = "SELECT location.name, location.id from core.location where parent_location_id=:parentId";
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("parentId", parentId);
		return repository.executeSelectQuery(sqlQuery, params);
	}
	
	public List<Object[]> executeSelectQuery(String sqlQuery, Map<String, Object> params) {
		return repository.executeSelectQuery(sqlQuery, params);
	}
	
	public <T> long save(T t) throws Exception {
		Location location = (Location) t;
		location.setUuid(UUID.randomUUID().toString());
		
		repository.save(location);
		
		return location.getId();
	}
	
	public <T> long saveToOpenSRP(T t) throws Exception {
		Location location = (Location) t;
		long createdLocation = repository.save(location);
		return createdLocation;
	}
	
	public <T> int update(T t) throws JSONException {
		Location location = (Location) t;
		int updatedLocation = 0;
		
		updatedLocation = repository.update(location);
		
		return updatedLocation;
	}
	
	public <T> boolean delete(T t) {
		return repository.delete(t);
	}
	
	public <T> T findById(int id, String fieldName, Class<?> className) {
		return repository.findById(id, fieldName, className);
	}
	
	public <T> List<T> findAllById(List<Integer> ids, String fieldName, String className) {
		return repository.findAllById(ids, fieldName, className);
	}
	
	public <T> T findByKey(String value, String fieldName, Class<?> className) {
		return repository.findByKey(value, fieldName, className);
	}
	
	public <T> List<T> findAll(String tableClass) {
		return repository.findAll(tableClass);
	}
	
	public <T> List<T> findAllLocation(String tableClass) {
		return repository.findAllLocation(tableClass);
	}
	
	public <T> List<T> findAllLocationPartialProperty(Integer roleId) {
		return repository.findAllLocationPartialProperty(roleId);
	}
	
	public <T> List<T> findAllLocationByAM(Integer userId, Integer roleId) {
		return repository.getLocationByAM(userId, roleId);
	}
	
	public Location setCreatorParentLocationTagAttributeInLocation(Location location, int parentLocationId, int tagId) {
		
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		User creator = (User) repository.findByKey(auth.getName(), "username", User.class);
		Location parentLocation = (Location) repository.findById(parentLocationId, "id", Location.class);
		LocationTag locationTag = (LocationTag) repository.findById(tagId, "id", LocationTag.class);
		location.setCreator(creator);
		location.setParentLocation(parentLocation);
		location.setLocationTag(locationTag);
		
		return location;
	}
	
	public <T> List<T> getVillageIdByProvider(int memberId, int childRoleId, int locationTagId) {
		return repository.getVillageIdByProvider(memberId, childRoleId, locationTagId);
	}
	
	public Map<Integer, String> getLocationTreeAsMap() {
		List<Location> locations = findAll("Location");
		Map<Integer, String> locationTreeAsMap = new HashMap<Integer, String>();
		if (locations != null) {
			for (Location location : locations) {
				locationTreeAsMap.put(location.getId(), location.getName());
				
			}
		}
		return locationTreeAsMap;
	}
	
	public boolean locationExistsForUpdate(Location location, boolean isOpenMRSCheck) throws JSONException {
		boolean exists = false;
		boolean isExistsInOpenMRS = false;
		JSONArray existinglocation = new JSONArray();
		String query = "";
		if (location != null) {
			exists = repository.entityExistsNotEqualThisId(location.getId(), location.getName(), "name", Location.class);
			
		}
		
		return exists;
	}
	
	public boolean locationExists(Location location) {
		boolean exists = false;
		if (location != null) {
			exists = repository.isLocationExists(location.getName(), Location.class);
		}
		return exists;
	}
	
	public void setSessionAttribute(HttpSession session, Location location, String parentLocationName) {
		
		Map<Integer, String> parentLocationMap = getLocationTreeAsMap();
		Map<Integer, String> tags = locationTagServiceImpl.getLocationTagListAsMap();
		
		session.setAttribute("parentLocation", parentLocationMap);
		if (location.getParentLocation() != null) {
			session.setAttribute("selectedParentLocation", location.getParentLocation().getId());
		} else {
			session.setAttribute("selectedParentLocation", 0);
		}
		session.setAttribute("tags", tags);
		if (location.getLocationTag() != null) {
			session.setAttribute("selectedTag", location.getLocationTag().getId());
		} else {
			session.setAttribute("selectedTag", 0);
		}
		
		session.setAttribute("parentLocationName", parentLocationName);
		
	}
	
	public void setModelAttribute(ModelMap model, Location location) {
		model.addAttribute("name", location.getName());
		model.addAttribute("uniqueErrorMessage", "Specified Location name already exists, please specify another");
		
	}
	
	public boolean sameEditedNameAndActualName(int id, String editedName) {
		boolean sameName = false;
		Location location = repository.findById(id, "id", Location.class);
		String actualName = location.getName();
		if (actualName.equalsIgnoreCase(editedName)) {
			sameName = true;
		}
		return sameName;
	}
	
	public static void treeTraverse(Map<String, TreeNode<String, Location>> lotree) {
		TreeNode<String, Location> treeNode = null;
		int i = 0;
		String div = "";
		for (Map.Entry<String, TreeNode<String, Location>> entry : lotree.entrySet()) {
			i++;
			treeNode = entry.getValue();
			Map<String, TreeNode<String, Location>> children = treeNode.getChildren();
			div = "</div>" + treeNode.getNode().getName() + "</div>";
			//System.err.println("Parent" + treeNode.getParent() + "child: " + i + "->" + treeNode.getNode().getName());
			System.err.println("I;" + div);
			if (children != null) {
				treeTraverse(children);
			} else {
				i = 0;
			}
		}
		
	}
	
	public JSONArray getLocationDataAsJson(String parentIndication, String parentKey) throws JSONException {
		JSONArray dataArray = new JSONArray();
		
		List<Location> locations = findAll("Location");
		for (Location location : locations) {
			JSONObject dataObject = new JSONObject();
			Location parentLocation = location.getParentLocation();
			if (parentLocation != null) {
				dataObject.put(parentKey, parentLocation.getId());
			} else {
				dataObject.put(parentKey, parentIndication);
			}
			dataObject.put("id", location.getId());
			dataObject.put("text", location.getName());
			dataObject.put("icon", location.getLocationTag().getName());
			dataArray.put(dataObject);
		}
		
		return dataArray;
		
	}
	
	//	public JSONArray getLocationWithDisableFacility(HttpSession session, String parentIndication, String parentKey, List<UserAssignedLocationDTO> userAssignedLocationDTOS, Integer userId, String role, Integer loggedInUserId, Integer roleId) throws JSONException {
	//		JSONArray dataArray = new JSONArray();
	//
	//		Map<Integer, Integer> locationMap = new HashMap<>();
	//		if (roleId != Roles.AM.getId()) {
	//			for (UserAssignedLocationDTO dto : userAssignedLocationDTOS) {
	//				locationMap.put(dto.getLocationId(), dto.getId());
	//			}
	//		}
	//		List<LocationDTO> locations = new ArrayList<>();
	//
	//		if (role.equalsIgnoreCase(Roles.AM.getName()) || (roleId == Roles.SS.getId() || roleId == Roles.SK.getId())) {
	//			locations = findAllLocationByAM(loggedInUserId, roleId);
	//		} else {
	//			locations = findAllLocationPartialProperty(roleId);
	//		}
	//
	//		for (LocationDTO location : locations) {
	//			JSONObject dataObject = new JSONObject();
	//			if (location.getParentLocationId() != null) {
	//				dataObject.put(parentKey, location.getParentLocationId());
	//			} else {
	//				dataObject.put(parentKey, parentIndication);
	//			}
	//			JSONObject state = new JSONObject();
	//
	//			if (locationMap.get(location.getId())!=null && !locationMap.get(location.getId()).equals(userId) ) {
	//
	//				if (locationMap.containsKey(location.getId())) {
	//					state.put("disabled", true);
	//				} else if (locationMap.get(location.getId()) != null && location.getParentLocationId() != null
	//						&& !locationMap.get(location.getParentLocationId()).equals(userId)) {
	//
	//					if (locationMap.containsKey(location.getParentLocationId())) {
	//						locationMap.put(location.getId(), locationMap.get(location.getParentLocationId()));
	//						state.put("disabled", true);
	//					}
	//					else state.put("disabled", false);
	//				} else {
	//					state.put("disabled", false);
	//				}
	//			} else {
	//				state.put("disabled", false);
	//			}
	//
	//			dataObject.put("id", location.getId());
	//			dataObject.put("text", location.getLocationName()+"("+location.getLocationTagName()+")");
	//			dataObject.put("icon", location.getUsers()==null?
	//					location.getLocationName():location.getLocationName()+"("+location.getUsers()+")");
	//			dataObject.put("state", state);
	//			dataArray.put(dataObject);
	//		}
	//
	//		return dataArray;
	//
	//	}
	
	public List<Location> getAllByKeysWithALlMatches(String name) {
		Map<String, String> fieldValues = new HashMap<String, String>();
		fieldValues.put("name", name);
		boolean isProvider = false;
		return repository.findAllByKeysWithALlMatches(isProvider, fieldValues, Location.class);
	}
	
	public String makeParentLocationName(Location location) {
		String parentLocationName = "";
		String tagNme = "";
		String locationName = "";
		if (location.getParentLocation() != null) {
			location = repository.findById(location.getParentLocation().getId(), "id", Location.class);
			if (location.getParentLocation() != null) {
				parentLocationName = location.getParentLocation().getName() + " -> ";
			}
			
			if (location.getLocationTag() != null) {
				tagNme = "  (" + location.getLocationTag().getName() + ")";
			}
			locationName = location.getName();
		}
		return parentLocationName + locationName + tagNme;
	}
	
	public String makeLocationName(Location location) {
		String parentLocationName = "";
		String tagNme = "";
		String locationName = "";
		if (location.getParentLocation() != null) {
			if (location.getParentLocation() != null) {
				parentLocationName = location.getParentLocation().getName() + " -> ";
			}
			
			if (location.getLocationTag() != null) {
				tagNme = "  (" + location.getLocationTag().getName() + ")";
			}
			locationName = location.getName();
		}
		return parentLocationName + locationName + tagNme;
	}
	
	public JSONArray search(String name) throws JSONException {
		JSONArray locationJsonArray = new JSONArray();
		String locationName = "";
		String parentLocationName = "";
		List<Location> locations = getAllByKeysWithALlMatches(name);
		if (locations != null) {
			for (Location location : locations) {
				
				JSONObject locationJsonObject = new JSONObject();
				if (location.getParentLocation() != null) {
					parentLocationName = location.getParentLocation().getName() + " > ";
				} else {
					parentLocationName = "";
				}
				locationName = parentLocationName + location.getName();
				locationJsonObject.put("label", locationName);
				locationJsonObject.put("id", location.getId());
				locationJsonArray.put(locationJsonObject);
			}
		}
		return locationJsonArray;
	}
	
	public JSONArray list() throws JSONException {
		JSONArray locationJsonArray = new JSONArray();
		String locationName = "";
		String parentLocationName = "";
		List<Location> locations = findAll("Location");
		if (locations != null) {
			for (Location location : locations) {
				
				JSONObject locationJsonObject = new JSONObject();
				if (location.getParentLocation() != null) {
					parentLocationName = location.getParentLocation().getName() + " > ";
				} else {
					parentLocationName = "";
				}
				locationName = parentLocationName + location.getName();
				locationJsonObject.put("value", locationName);
				locationJsonObject.put("id", location.getId());
				locationJsonArray.put(locationJsonObject);
			}
		}
		return locationJsonArray;
	}
	
	@Transactional
	public <T> T getLocationByNameAndparentId(int id, String fieldName, String name, String className) {
		Session session = sessionFactory.getCurrentSession();
		List<T> result = new ArrayList<T>();
		
		String hql = "from " + className + " where " + fieldName + " = :id and name=:name";
		Query query = session.createQuery(hql).setInteger("id", id).setString("name", name);
		result = query.list();
		
		return result.size() > 0 ? (T) result.get(0) : null;
	}
	
	@Transactional
	public int getLocationWithNameAndparentId(String name, int parentId) {
		Session session = sessionFactory.getCurrentSession();
		//repository.findByForeignKey(id, fieldName, className)
		BigInteger total = null;
		try {
			String hql = "select count(*) from core.location as l where name =:name and l.parent_location_id=:parentId";
			Query query = session.createSQLQuery(hql).setString("name", name).setInteger("parentId", parentId);
			total = (BigInteger) query.uniqueResult();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return total.intValue();
	}
	
	@Transactional
	public Location save(Location t) throws Exception {
		Session session = sessionFactory.getCurrentSession();
		
		session.saveOrUpdate(t);
		logger.info("saved successfully: " + t.getClass().getName());
		
		return t;
	}
	
	@Transactional
	@SuppressWarnings({ "resource", "unused" })
	public String uploadLocation(File csvFile) throws Exception {
		String msg = "";
		BufferedReader br = null;
		String line = "";
		String cvsSplitBy = ",";
		
		int position = 0;
		String[] tags = null;
		try {
			br = new BufferedReader(new FileReader(csvFile));
			while ((line = br.readLine()) != null) {
				String tag = "";
				String code = "";
				String name = "";
				String parent = "";
				String[] locations = line.split(cvsSplitBy);
				if (position == 0) {
					tags = locations;
					
				} else {
					
					LocationTag countryTag = findByKey(tags[1], "name", LocationTag.class);
					LocationTag divisionTag = findByKey(tags[3], "name", LocationTag.class);
					LocationTag districtTag = findByKey(tags[5], "name", LocationTag.class);
					LocationTag upazilaTag = findByKey(tags[7], "name", LocationTag.class);
					LocationTag pourasavaTag = findByKey(tags[9], "name", LocationTag.class);
					LocationTag unionTag = findByKey(tags[11], "name", LocationTag.class);
					LocationTag paraTag = findByKey(tags[13], "name", LocationTag.class);
					Location country = findByKey(locations[1].toUpperCase().trim(), "name", Location.class);
					
					if (country == null) {
						Location location = new Location();
						location.setCode(locations[0]);
						location.setName(locations[1].toUpperCase().trim());
						location.setLocationTag(countryTag);
						location.setParentLocation(null);
						location.setDescription(locations[1]);
						country = save(location);
					} else {
						Location location = country;
						location.setCode(locations[0]);
						location.setName(locations[1].toUpperCase().trim());
						location.setLocationTag(countryTag);
						location.setParentLocation(null);
						location.setDescription(locations[1]);
						country = save(location);
					}
					
					Location division = getLocationByNameAndparentId(country.getId(), "parentLocation",
					    locations[3].toUpperCase(), "Location");
					
					if (division == null) {
						Location location = new Location();
						location.setCode(locations[2]);
						location.setName(locations[3].toUpperCase().trim());
						location.setLocationTag(divisionTag);
						location.setParentLocation(country);
						location.setDescription(locations[3]);
						division = save(location);
					} else {
						Location location = division;
						location.setCode(locations[2]);
						location.setName(locations[3].toUpperCase().trim());
						location.setLocationTag(divisionTag);
						location.setParentLocation(country);
						location.setDescription(locations[3]);
						division = save(location);
						System.err.println("division:" + location);
					}
					
					Location district = getLocationByNameAndparentId(division.getId(), "parentLocation",
					    locations[5].toUpperCase(), "Location");
					
					if (district == null) {
						Location location = new Location();
						location.setCode(locations[4]);
						location.setName(locations[5].toUpperCase().trim());
						location.setLocationTag(districtTag);
						location.setParentLocation(division);
						location.setDescription(locations[5]);
						location.setDivision(division);
						district = save(location);
					} else {
						Location location = district;
						location.setCode(locations[4]);
						location.setName(locations[5].toUpperCase().trim());
						location.setLocationTag(districtTag);
						location.setParentLocation(division);
						location.setDescription(locations[5]);
						location.setDivision(division);
						district = save(location);
					}
					System.err.println("district.getId() :" + district.getId());
					Location upazila = getLocationByNameAndparentId(district.getId(), "parentLocation",
					    locations[7].toUpperCase(), "Location");
					System.err.println("upazila :" + upazila);
					if (upazila == null) {
						Location location = new Location();
						location.setCode(locations[6]);
						location.setName(locations[7].toUpperCase().trim());
						location.setLocationTag(upazilaTag);
						location.setParentLocation(district);
						location.setDescription(locations[7]);
						location.setDivision(division);
						location.setDistrict(district);
						upazila = save(location);
					} else {
						Location location = upazila;
						//System.err.println("upazila:" + location.getId());
						location.setCode(locations[6]);
						location.setName(locations[7].toUpperCase().trim());
						location.setLocationTag(upazilaTag);
						location.setParentLocation(district);
						location.setDescription(locations[7]);
						location.setDivision(division);
						location.setDistrict(district);
						upazila = save(location);
					}
					
					Location pourasava = getLocationByNameAndparentId(upazila.getId(), "parentLocation",
					    locations[9].toUpperCase(), "Location");
					if (pourasava == null) {
						Location location = new Location();
						location.setCode(locations[8]);
						location.setName(locations[9].toUpperCase().trim());
						location.setLocationTag(pourasavaTag);
						location.setParentLocation(upazila);
						location.setDescription(locations[9]);
						location.setDivision(division);
						location.setDistrict(district);
						location.setUpazila(upazila);
						pourasava = save(location);
					} else {
						Location location = pourasava;
						location.setCode(locations[8]);
						location.setName(locations[9].toUpperCase().trim());
						location.setLocationTag(pourasavaTag);
						location.setParentLocation(upazila);
						location.setDescription(locations[9]);
						location.setDivision(division);
						location.setDistrict(district);
						location.setUpazila(upazila);
						pourasava = save(location);
					}
					
					Location union = getLocationByNameAndparentId(pourasava.getId(), "parentLocation",
					    locations[11].toUpperCase(), "Location");
					
					if (union == null) {
						Location location = new Location();
						location.setCode(locations[10]);
						location.setName(locations[11].toUpperCase().trim());
						location.setLocationTag(unionTag);
						location.setParentLocation(pourasava);
						location.setDescription(locations[11]);
						location.setDivision(division);
						location.setDistrict(district);
						location.setUpazila(upazila);
						location.setPourasabha(pourasava);
						union = save(location);
					} else {
						Location location = union;
						location.setCode(locations[10]);
						location.setName(locations[11].toUpperCase().trim());
						location.setLocationTag(unionTag);
						location.setParentLocation(pourasava);
						location.setDescription(locations[11]);
						location.setDivision(division);
						location.setDistrict(district);
						location.setUpazila(upazila);
						location.setPourasabha(pourasava);
						union = save(location);
					}
					
					Location para = getLocationByNameAndparentId(union.getId(), "parentLocation",
					    locations[13].toUpperCase(), "Location");
					
					if (para == null) {
						Location location = new Location();
						location.setCode(locations[12]);
						location.setName(locations[13].toUpperCase().trim());
						location.setLocationTag(paraTag);
						location.setParentLocation(union);
						location.setDescription(locations[13]);
						location.setDivision(division);
						location.setDistrict(district);
						location.setUpazila(upazila);
						location.setPourasabha(pourasava);
						location.setUnion(union);
						para = save(location);
					} else {
						Location location = para;
						location.setCode(locations[12]);
						location.setName(locations[13].toUpperCase().trim());
						location.setLocationTag(paraTag);
						location.setParentLocation(union);
						location.setDescription(locations[13]);
						location.setDivision(division);
						location.setDistrict(district);
						location.setUpazila(upazila);
						location.setPourasabha(pourasava);
						location.setUnion(union);
						para = save(location);
					}
					
				}
				position++;
			}
			
		}
		catch (Exception e) {
			e.printStackTrace();
			logger.info("Some problem occurred, please contact with admin..");
			msg = "Some problem occurred, at position:" + position;
		}
		return msg;
	}
	
	public Set<Location> getLocationByIds(int[] locations) {
		Set<Location> locationSet = new HashSet<Location>();
		if (locations != null && locations.length != 0) {
			for (int locationId : locations) {
				Location location = repository.findById(locationId, "id", Location.class);
				if (location != null) {
					locationSet.add(location);
				}
			}
		}
		return locationSet;
	}
	
	public JSONArray convertLocationTreeToJSON(List<LocationTreeDTO> treeDTOS, boolean enableSimPrint) throws JSONException {
		JSONArray locationTree = new JSONArray();
		
		Map<String, Boolean> mp = new HashMap<>();
		JSONObject object = new JSONObject();
		JSONArray locations = new JSONArray();
		JSONObject fullLocation = new JSONObject();
		
		int counter = 0;
		String username = "";
		
		for (LocationTreeDTO treeDTO : treeDTOS) {
			counter++;
			if (mp.get(treeDTO.getUsername()) == null || !mp.get(treeDTO.getUsername())) {
				if (counter > 1) {
					object.put("username", username);
					object.put("locations", locations);
					object.put("simprints_enable", enableSimPrint);
					locationTree.put(object);
					locations = new JSONArray();
					object = new JSONObject();
				}
				mp.put(treeDTO.getUsername(), true);
			}
			
			username = treeDTO.getUsername();
			
			if (treeDTO.getLoc_tag_name().equalsIgnoreCase("country")) {
				if (counter > 1) {
					fullLocation = setEmptyValues(fullLocation);
					locations.put(fullLocation);
					fullLocation = new JSONObject();
				}
			}
			
			JSONObject location = new JSONObject();
			location.put("code", treeDTO.getCode());
			location.put("id", treeDTO.getId());
			location.put("name", treeDTO.getName());
			fullLocation.put(treeDTO.getLoc_tag_name().toLowerCase().replaceAll(" ", "_"), location);
			
			if (counter == treeDTOS.size()) {
				locations.put(fullLocation);
				object.put("username", username);
				object.put("locations", locations);
				object.put("simprints_enable", enableSimPrint);
				locationTree.put(object);
				object = new JSONObject();
				locations = new JSONArray();
			}
		}
		return locationTree;
	}
	
	private JSONObject getLocationProperty() throws JSONException {
		JSONObject property = new JSONObject();
		property.put("name", "");
		property.put("id", 0);
		property.put("code", "00");
		return property;
	}
	
	private JSONObject setEmptyValues(JSONObject fullLocation) throws JSONException {
		if (!fullLocation.has("country")) {
			fullLocation.put("country", getLocationProperty());
		}
		if (!fullLocation.has("division")) {
			fullLocation.put("division", getLocationProperty());
		}
		if (!fullLocation.has("district")) {
			fullLocation.put("district", getLocationProperty());
		}
		if (!fullLocation.has("city_corporation_upazila")) {
			fullLocation.put("city_corporation_upazila", getLocationProperty());
		}
		if (!fullLocation.has("pourasabha")) {
			fullLocation.put("pourasabha", getLocationProperty());
		}
		if (!fullLocation.has("union_ward")) {
			fullLocation.put("union_ward", getLocationProperty());
		}
		if (!fullLocation.has("village")) {
			fullLocation.put("village", getLocationProperty());
		}
		return fullLocation;
	}
	
	public int getLocationId(HttpServletRequest request) {
		Location country = repository.findByKey("BANGLADESH", "name", Location.class);
		int locationId = country.getId();
		if (request.getParameterMap().containsKey("division")) {
			String division = request.getParameter("division");
			if (division != null && division.length() > 0) {
				String[] location = division.split("\\?");
				if (!location[0].trim().equalsIgnoreCase("0") && !StringUtils.isBlank(location[0].trim()))
					locationId = Integer.parseInt(location[0].trim());
			}
		}
		if (request.getParameterMap().containsKey("district")) {
			String district = request.getParameter("district");
			if (district != null) {
				String[] location = district.split("\\?");
				if (!location[0].trim().equalsIgnoreCase("0") && !StringUtils.isBlank(location[0].trim()))
					locationId = Integer.parseInt(location[0].trim());
			}
		}
		if (request.getParameterMap().containsKey("upazila")) {
			String upazila = request.getParameter("upazila");
			if (upazila != null) {
				String[] location = upazila.split("\\?");
				if (!location[0].trim().equalsIgnoreCase("0") && !StringUtils.isBlank(location[0].trim()))
					locationId = Integer.parseInt(location[0].trim());
			}
		}
		if (request.getParameterMap().containsKey("pourasabha")) {
			String pourasabha = request.getParameter("pourasabha");
			if (pourasabha != null) {
				String[] location = pourasabha.split("\\?");
				if (!location[0].trim().equalsIgnoreCase("0") && !StringUtils.isBlank(location[0].trim()))
					locationId = Integer.parseInt(location[0].trim());
			}
		}
		if (request.getParameterMap().containsKey("union")) {
			String union = request.getParameter("union");
			if (union != null) {
				String[] location = union.split("\\?");
				if (!location[0].trim().equalsIgnoreCase("0") && !StringUtils.isBlank(location[0].trim()))
					locationId = Integer.parseInt(location[0].trim());
			}
		}
		if (request.getParameterMap().containsKey("village")) {
			String village = request.getParameter("village");
			if (village != null) {
				String[] location = village.split("\\?");
				if (!location[0].trim().equalsIgnoreCase("0") && !StringUtils.isBlank(location[0].trim()))
					locationId = Integer.parseInt(location[0].trim());
			}
		}
		return locationId;
	}
	
	@Transactional
	public LocationHierarchyDTO getLocationHierarchy(Integer locationId) {
		Session session = sessionFactory.getCurrentSession();
		LocationHierarchyDTO dto = new LocationHierarchyDTO();
		
		String hql = "select * from core.single_location_tree(:locationId);";
		Query query = session.createSQLQuery(hql).addScalar("villageId", StandardBasicTypes.INTEGER)
		        .addScalar("unionId", StandardBasicTypes.INTEGER).addScalar("pourasabhaId", StandardBasicTypes.INTEGER)
		        .addScalar("upazilaId", StandardBasicTypes.INTEGER).addScalar("districtId", StandardBasicTypes.INTEGER)
		        .addScalar("divisionId", StandardBasicTypes.INTEGER).setInteger("locationId", locationId)
		        .setResultTransformer(new AliasToBeanResultTransformer(LocationHierarchyDTO.class));
		dto = (LocationHierarchyDTO) query.list().get(0);
		return dto;
	}
	
	public List<LocationDTO> getLocations(String name, Integer length, Integer start, String orderColumn,
	                                      String orderDirection) {
		return repository.getLocations(name, length, start, orderColumn, orderDirection);
	}
	
	public Integer getLocationCount(String name) {
		return repository.getLocationCount(name);
	}
	
	public List<Location> getLocationsHierarchy(String name, Integer tagId, Integer length, Integer start,
	                                            String orderColumn, String orderDirection) {
		return locationRepository.getLocationsHierarchy(name, tagId, length, start, orderColumn, orderDirection);
	}
	
	public Integer countTotal(String name, Integer tagId) {
		return locationRepository.countTotal(name, tagId);
	}
	
	public JSONObject getLocationDataOfDataTable(Integer draw, Integer totalLocation, List<LocationDTO> locations)
	    throws JSONException {
		JSONObject response = new JSONObject();
		response.put("draw", draw + 1);
		response.put("recordsTotal", totalLocation);
		response.put("recordsFiltered", totalLocation);
		JSONArray array = new JSONArray();
		for (LocationDTO dto : locations) {
			JSONArray location = new JSONArray();
			location.put(dto.getName());
			location.put(dto.getDescription());
			location.put(dto.getCode());
			location.put(dto.getLocationTagName());
			array.put(location);
		}
		response.put("data", array);
		return response;
	}
	
	public JSONObject getDistrictDataOfDataTable(Integer draw, Integer totalLocation, List<Location> locations)
	    throws JSONException {
		JSONObject response = new JSONObject();
		response.put("draw", draw + 1);
		response.put("recordsTotal", totalLocation);
		response.put("recordsFiltered", totalLocation);
		JSONArray array = new JSONArray();
		for (Location dto : locations) {
			JSONArray location = new JSONArray();
			location.put(dto.getName());
			location.put(dto.getDivision().getName());
			location.put(dto.getCode());
			location.put(dto.getLocationTag().getName());
			array.put(location);
		}
		response.put("data", array);
		return response;
	}
	
	public JSONObject getUpazilaDataOfDataTable(Integer draw, Integer totalLocation, List<Location> locations)
	    throws JSONException {
		JSONObject response = new JSONObject();
		response.put("draw", draw + 1);
		response.put("recordsTotal", totalLocation);
		response.put("recordsFiltered", totalLocation);
		JSONArray array = new JSONArray();
		for (Location dto : locations) {
			JSONArray location = new JSONArray();
			location.put(dto.getName());
			location.put(dto.getDistrict().getName());
			location.put(dto.getCode());
			location.put(dto.getLocationTag().getName());
			array.put(location);
		}
		response.put("data", array);
		return response;
	}
	
	public JSONObject getPourasabhaDataOfDataTable(Integer draw, Integer totalLocation, List<Location> locations)
	    throws JSONException {
		JSONObject response = new JSONObject();
		response.put("draw", draw + 1);
		response.put("recordsTotal", totalLocation);
		response.put("recordsFiltered", totalLocation);
		JSONArray array = new JSONArray();
		for (Location dto : locations) {
			JSONArray location = new JSONArray();
			location.put(dto.getName());
			location.put(dto.getCode());
			location.put(dto.getUpazila().getName());
			location.put(dto.getDistrict().getName());
			location.put(dto.getDivision().getName());
			array.put(location);
		}
		response.put("data", array);
		return response;
	}
	
	public JSONObject getUnionDataOfDataTable(Integer draw, Integer totalLocation, List<Location> locations)
	    throws JSONException {
		JSONObject response = new JSONObject();
		response.put("draw", draw + 1);
		response.put("recordsTotal", totalLocation);
		response.put("recordsFiltered", totalLocation);
		JSONArray array = new JSONArray();
		for (Location dto : locations) {
			JSONArray location = new JSONArray();
			location.put(dto.getName());
			location.put(dto.getCode());
			location.put(dto.getPourasabha().getName());
			location.put(dto.getUpazila().getName());
			location.put(dto.getDistrict().getName());
			location.put(dto.getDivision().getName());
			array.put(location);
		}
		response.put("data", array);
		return response;
	}
	
	public JSONObject getParaDataOfDataTable(Integer draw, Integer totalLocation, List<Location> locations)
	    throws JSONException {
		JSONObject response = new JSONObject();
		response.put("draw", draw + 1);
		response.put("recordsTotal", totalLocation);
		response.put("recordsFiltered", totalLocation);
		JSONArray array = new JSONArray();
		for (Location dto : locations) {
			JSONArray location = new JSONArray();
			location.put(dto.getName());
			location.put(dto.getCode());
			location.put(dto.getUnion().getName());
			location.put(dto.getPourasabha().getName());
			location.put(dto.getUpazila().getName());
			location.put(dto.getDistrict().getName());
			location.put(dto.getDivision().getName());
			array.put(location);
		}
		response.put("data", array);
		return response;
	}
	
	public List<Location> findAllByParents(List<Integer> ids) {
		return locationRepository.findAllByParents(ids);
	}
	
	public List<Location> getSiblings(List<Location> locations) {
		List<Location> siblings = new ArrayList<>();
		Set<Integer> siblingIdSet = new HashSet<>();
		if (locations != null && locations.size() > 0) {
			for (Location location : locations) {
				siblingIdSet.add(location.getParentLocation().getId());
			}
			siblings = findAllByParents(new ArrayList<Integer>(siblingIdSet));
		}
		return siblings;
	}
	
	public List<Integer> getLocationIds(List<Location> locations) {
		Set<Integer> locationIdSet = new HashSet<>();
		if (locations != null && locations.size() > 0) {
			for (Location location : locations) {
				locationIdSet.add(location.getId());
			}
		}
		return new ArrayList<Integer>(locationIdSet);
	}
	
}
