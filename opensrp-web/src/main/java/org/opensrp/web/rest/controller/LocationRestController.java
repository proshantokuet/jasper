package org.opensrp.web.rest.controller;

import static org.springframework.http.HttpStatus.OK;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.json.JSONException;
import org.json.JSONObject;
import org.opensrp.common.dto.ClusterDTO;
import org.opensrp.common.dto.LocationDTO;
import org.opensrp.common.dto.ParaCenterDTO;
import org.opensrp.common.util.LocationColumn;
import org.opensrp.common.util.LocationTags;
import org.opensrp.core.entity.Cluster;
import org.opensrp.core.entity.Location;
import org.opensrp.core.entity.ParaCenter;
import org.opensrp.core.entity.Role;
import org.opensrp.core.entity.User;
import org.opensrp.core.service.ClusterService;
import org.opensrp.core.service.LocationService;
import org.opensrp.core.service.ParaCenterService;
import org.opensrp.core.service.UserService;
import org.opensrp.core.service.mapper.ClusterMapper;
import org.opensrp.core.service.mapper.LocationMapper;
import org.opensrp.core.service.mapper.ParaCenterMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;

@RequestMapping("rest/api/v1/location")
@RestController
public class LocationRestController {
	
	@Autowired
	private LocationService locationServiceImpl;
	
	@Autowired
	private ParaCenterService paraCenterService;
	
	@Autowired
	private ClusterService clusterService;
	
	@Autowired
	private UserService userServiceImpl;
	
	@Autowired
	private LocationMapper locationMapper;
	
	@Autowired
	private ParaCenterMapper paraCenterMapper;
	
	@Autowired
	private ClusterMapper clusterMapper;
	
	@RequestMapping(value = "/search", method = RequestMethod.GET)
	public String getLocationNameAndId(Model model, HttpSession session, @RequestParam String name) throws JSONException {
		return locationServiceImpl.search(name).toString();
		
	}
	
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public String getLocationList(Model model, HttpSession session) throws JSONException {
		return locationServiceImpl.list().toString();
		
	}
	
	@RequestMapping(value = "/location-name", method = RequestMethod.GET)
	public ResponseEntity<String> getLocationNameByUserName(Model model, HttpSession session, @RequestParam String name)
	    throws JSONException {
		User user = userServiceImpl.findByKey(name, "username", User.class);
		JSONObject response = new JSONObject();
		if (user != null) {
			String roleName = "";
			Set<Role> roles = user.getRoles();
			if (roles.size() != 0) {
				for (Role role : roles) {
					roleName = role.getName();
				}
			}
			String sqlQuery = "select team_member.id,core.location.name as name from core.team_member left join "
			        + "core.team_member_location on team_member.id = team_member_location.team_member_id left join "
			        + "core.location on  team_member_location.location_id = location.id where team_member.person_id =:userId";
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("userId", user.getId());
			List<Object[]> locations = locationServiceImpl.executeSelectQuery(sqlQuery, params);
			StringBuilder stringBuilder = new StringBuilder();
			int i = 0;
			for (Object[] location : locations) {
				if (i == 0) {
					stringBuilder.append(location[1]);
				} else {
					stringBuilder.append(",").append(location[1]);
				}
				i++;
			}
			
			response.put("locations", stringBuilder);
			response.put("role", roleName);
			return new ResponseEntity<>(response.toString(), OK);
		}
		
		return new ResponseEntity<>(response.toString(), HttpStatus.NO_CONTENT);
		
	}
	
	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public ResponseEntity<String> saveLocation(HttpSession session, ModelMap model, @RequestBody LocationDTO locationDTO)
	    throws Exception {
		Location location = locationMapper.map(locationDTO);
		try {
			if (!locationServiceImpl.locationExists(location)) {
				locationServiceImpl.save(location);
			} else {
				String errorMessage = "Specified location already exists, please specify another";
				return new ResponseEntity<>(new Gson().toJson(errorMessage), OK);
			}
		}
		catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(new Gson().toJson(e.getMessage()), OK);
		}
		return new ResponseEntity<>(new Gson().toJson(""), OK);
	}
	
	@RequestMapping(value = "para-center/save", method = RequestMethod.POST)
	public ResponseEntity<String> saveParaCenter(@RequestBody ParaCenterDTO paraCenterDTO) throws Exception {
		System.out.println(paraCenterDTO);
		try {
			ParaCenter paraCenter = paraCenterMapper.map(paraCenterDTO);
			if (!paraCenterService.isExist(paraCenter)) {
				paraCenterService.save(paraCenter);
			} else {
				String errorMessage = "Specified para center already exists, please specify another";
				return new ResponseEntity<>(new Gson().toJson(errorMessage), OK);
			}
		}
		catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(new Gson().toJson(e.getMessage()), OK);
		}
		return new ResponseEntity<>(new Gson().toJson(""), OK);
	}
	
	@RequestMapping(value = "cluster/save", method = RequestMethod.POST)
	public ResponseEntity<String> saveCluster(@RequestBody ClusterDTO clusterDTO) throws Exception {
		try {
			Cluster cluster = clusterMapper.map(clusterDTO);
			if (!clusterService.isExist(cluster)) {
				clusterService.save(cluster);
			} else {
				String errorMessage = "Specified cluster already exists, please specify another";
				return new ResponseEntity<>(new Gson().toJson(errorMessage), OK);
			}
		}
		catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(new Gson().toJson(e.getMessage()), OK);
		}
		return new ResponseEntity<>(new Gson().toJson(""), OK);
	}
	
	@RequestMapping(value = "/edit", method = RequestMethod.PUT)
	public ResponseEntity<String> editLocation(@RequestBody LocationDTO locationDTO) throws JSONException {
		return null;
	}
	
	@RequestMapping(value = "/list-ajax", method = RequestMethod.GET)
	public ResponseEntity<String> getLocationPagination(HttpServletRequest request) throws JSONException {
		Integer draw = Integer.valueOf(request.getParameter("draw"));
		String name = request.getParameter("search[value]");
		String orderColumn = request.getParameter("order[0][column]");
		String orderDirection = request.getParameter("order[0][dir]");
		orderColumn = LocationColumn.valueOf("_" + orderColumn).getValue();
		Integer start = Integer.valueOf(request.getParameter("start"));
		Integer length = Integer.valueOf(request.getParameter("length"));
		List<LocationDTO> locations = locationServiceImpl.getLocations(name, length, start, orderColumn, orderDirection);
		Integer totalLocation = locationServiceImpl.getLocationCount(name);
		JSONObject response = locationServiceImpl.getLocationDataOfDataTable(draw, totalLocation, locations);
		return new ResponseEntity<>(response.toString(), OK);
	}
	
	@RequestMapping(value = "/district-list", method = RequestMethod.GET)
	public ResponseEntity<String> getDistrictPagination(HttpServletRequest request) throws JSONException {
		Integer draw = Integer.valueOf(request.getParameter("draw"));
		String name = request.getParameter("search[value]");
		String orderColumn = request.getParameter("order[0][column]");
		String orderDirection = request.getParameter("order[0][dir]");
		orderColumn = LocationColumn.valueOf("_" + orderColumn).getValue();
		Integer start = Integer.valueOf(request.getParameter("start"));
		Integer length = Integer.valueOf(request.getParameter("length"));
		List<Location> locations = locationServiceImpl.getLocationsHierarchy(name, LocationTags.DISTRICT.getId(), length,
		    start, orderColumn, orderDirection);
		Integer totalLocation = locationServiceImpl.countTotal(name, LocationTags.DISTRICT.getId());
		JSONObject response = locationServiceImpl.getDistrictDataOfDataTable(draw, totalLocation, locations);
		return new ResponseEntity<>(response.toString(), OK);
	}
	
	@RequestMapping(value = "/upazila-list", method = RequestMethod.GET)
	public ResponseEntity<String> getUpazilaPagination(HttpServletRequest request) throws JSONException {
		Integer draw = Integer.valueOf(request.getParameter("draw"));
		String name = request.getParameter("search[value]");
		String orderColumn = request.getParameter("order[0][column]");
		String orderDirection = request.getParameter("order[0][dir]");
		orderColumn = LocationColumn.valueOf("_" + orderColumn).getValue();
		Integer start = Integer.valueOf(request.getParameter("start"));
		Integer length = Integer.valueOf(request.getParameter("length"));
		List<Location> locations = locationServiceImpl.getLocationsHierarchy(name,
		    LocationTags.UPAZILA_CITY_CORPORATION.getId(), length, start, orderColumn, orderDirection);
		Integer totalLocation = locationServiceImpl.countTotal(name, LocationTags.UPAZILA_CITY_CORPORATION.getId());
		JSONObject response = locationServiceImpl.getUpazilaDataOfDataTable(draw, totalLocation, locations);
		return new ResponseEntity<>(response.toString(), OK);
	}
	
	@RequestMapping(value = "/pourasabha-list", method = RequestMethod.GET)
	public ResponseEntity<String> getPourasabhaPagination(HttpServletRequest request) throws JSONException {
		Integer draw = Integer.valueOf(request.getParameter("draw"));
		String name = request.getParameter("search[value]");
		String orderColumn = request.getParameter("order[0][column]");
		String orderDirection = request.getParameter("order[0][dir]");
		orderColumn = LocationColumn.valueOf("_" + orderColumn).getValue();
		Integer start = Integer.valueOf(request.getParameter("start"));
		Integer length = Integer.valueOf(request.getParameter("length"));
		List<Location> locations = locationServiceImpl.getLocationsHierarchy(name, LocationTags.POURASABHA.getId(), length,
		    start, orderColumn, orderDirection);
		Integer totalLocation = locationServiceImpl.countTotal(name, LocationTags.POURASABHA.getId());
		JSONObject response = locationServiceImpl.getPourasabhaDataOfDataTable(draw, totalLocation, locations);
		return new ResponseEntity<>(response.toString(), OK);
	}
	
	@RequestMapping(value = "/union-list", method = RequestMethod.GET)
	public ResponseEntity<String> getUnionPagination(HttpServletRequest request) throws JSONException {
		Integer draw = Integer.valueOf(request.getParameter("draw"));
		String name = request.getParameter("search[value]");
		String orderColumn = request.getParameter("order[0][column]");
		String orderDirection = request.getParameter("order[0][dir]");
		orderColumn = LocationColumn.valueOf("_" + orderColumn).getValue();
		Integer start = Integer.valueOf(request.getParameter("start"));
		Integer length = Integer.valueOf(request.getParameter("length"));
		List<Location> locations = locationServiceImpl.getLocationsHierarchy(name, LocationTags.UNION_WARD.getId(), length,
		    start, orderColumn, orderDirection);
		Integer totalLocation = locationServiceImpl.countTotal(name, LocationTags.UNION_WARD.getId());
		JSONObject response = locationServiceImpl.getUnionDataOfDataTable(draw, totalLocation, locations);
		return new ResponseEntity<>(response.toString(), OK);
	}
	
	@RequestMapping(value = "/para-list", method = RequestMethod.GET)
	public ResponseEntity<String> getParaPagination(HttpServletRequest request) throws JSONException {
		Integer draw = Integer.valueOf(request.getParameter("draw"));
		String name = request.getParameter("search[value]");
		String orderColumn = request.getParameter("order[0][column]");
		String orderDirection = request.getParameter("order[0][dir]");
		orderColumn = LocationColumn.valueOf("_" + orderColumn).getValue();
		Integer start = Integer.valueOf(request.getParameter("start"));
		Integer length = Integer.valueOf(request.getParameter("length"));
		List<Location> locations = locationServiceImpl.getLocationsHierarchy(name, LocationTags.PARA.getId(), length, start,
		    orderColumn, orderDirection);
		Integer totalLocation = locationServiceImpl.countTotal(name, LocationTags.PARA.getId());
		JSONObject response = locationServiceImpl.getParaDataOfDataTable(draw, totalLocation, locations);
		return new ResponseEntity<>(response.toString(), OK);
	}
	
	@RequestMapping(value = "/para-center-list", method = RequestMethod.GET)
	public ResponseEntity<String> getParaCenterPagination(HttpServletRequest request) throws JSONException {
		Integer draw = Integer.valueOf(request.getParameter("draw"));
		String name = request.getParameter("search[value]");
		String orderColumn = request.getParameter("order[0][column]");
		String orderDirection = request.getParameter("order[0][dir]");
		orderColumn = LocationColumn.valueOf("_" + orderColumn).getValue();
		Integer start = Integer.valueOf(request.getParameter("start"));
		Integer length = Integer.valueOf(request.getParameter("length"));
		List<ParaCenter> paraCenters = paraCenterService.findAllByName(name, length, start, orderColumn, orderDirection);
		Long totalParaCenter = paraCenterService.countTotal(name);
		JSONObject response = paraCenterService.getParaCenterDataOfDataTable(draw, totalParaCenter, paraCenters);
		return new ResponseEntity<>(response.toString(), OK);
	}
	
	@RequestMapping(value = "/cluster-list", method = RequestMethod.GET)
	public ResponseEntity<String> getClusterPagination(HttpServletRequest request) throws JSONException {
		Integer draw = Integer.valueOf(request.getParameter("draw"));
		String name = request.getParameter("search[value]");
		String orderColumn = request.getParameter("order[0][column]");
		String orderDirection = request.getParameter("order[0][dir]");
		orderColumn = LocationColumn.valueOf("_" + orderColumn).getValue();
		Integer start = Integer.valueOf(request.getParameter("start"));
		Integer length = Integer.valueOf(request.getParameter("length"));
		List<Cluster> clusters = clusterService.findAllByName(name, length, start, orderColumn, orderDirection);
		Long totalCluster = clusterService.countTotal(name);
		JSONObject response = clusterService.getClusterDataOfDataTable(draw, totalCluster, clusters);
		return new ResponseEntity<>(response.toString(), OK);
	}
}
