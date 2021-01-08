package org.opensrp.web.rest.controller;

import static org.springframework.http.HttpStatus.OK;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.json.JSONObject;
import org.opensrp.common.dto.ChangePasswordDTO;
import org.opensrp.common.dto.UserDTO;
import org.opensrp.common.util.LocationTags;
import org.opensrp.core.entity.User;
import org.opensrp.core.service.EmailService;
import org.opensrp.core.service.FacilityService;
import org.opensrp.core.service.FacilityWorkerTypeService;
import org.opensrp.core.service.LocationService;
import org.opensrp.core.service.RoleService;
import org.opensrp.core.service.TeamMemberService;
import org.opensrp.core.service.TeamService;
import org.opensrp.core.service.UserService;
import org.opensrp.core.service.UsersCatchmentAreaService;
import org.opensrp.core.service.mapper.FacilityWorkerMapper;
import org.opensrp.core.service.mapper.UserMapper;
import org.opensrp.web.util.AuthenticationManagerUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;

@RequestMapping("rest/api/v1/user")
@RestController
public class UserRestController {
	
	@Autowired
	private UserService userServiceImpl;
	
	@Autowired
	private TeamMemberService teamMemberServiceImpl;
	
	@Autowired
	private TeamService teamService;
	
	@Autowired
	private FacilityService facilityService;
	
	@Autowired
	private RoleService roleService;
	
	@Autowired
	private FacilityWorkerTypeService facilityWorkerTypeService;
	
	@Autowired
	private LocationService locationService;
	
	@Autowired
	private EmailService emailService;
	
	@Autowired
	private FacilityWorkerMapper facilityWorkerMapper;
	
	@Autowired
	private UserMapper userMapper;
	
	@Autowired
	private UsersCatchmentAreaService usersCatchmentAreaService;
	
	private static final Logger logger = Logger.getLogger(UserRestController.class);
	
	//	private static final Integer SK_ROLE_ID = Roles.SK.getId();
	//	private static final Integer SS_ROLE_ID = Roles.SS.getId();
	private static final int villageTagId = LocationTags.PARA.getId();
	
	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public ResponseEntity<String> saveUser(@RequestBody UserDTO userDTO, ModelMap model) throws Exception {
		String userNameUniqueError = "";
		try {
			User user = userMapper.map(userDTO);
			User loggedInUser = AuthenticationManagerUtil.getLoggedInUser();
			user.setCreator(loggedInUser);
			boolean isExists = userServiceImpl.isUserExist(user.getUsername());
			if (!isExists) {
				User createdUser = userServiceImpl.saveNew(user, false);
				userNameUniqueError = String.valueOf(createdUser.getId());
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return new ResponseEntity<>(new Gson().toJson(userNameUniqueError), OK);
	}
	
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public ResponseEntity<String> updateUser(@RequestBody UserDTO userDTO, ModelMap model) throws Exception {
		
		JSONObject response = new JSONObject();
		try {
			User user = userMapper.map(userDTO);
			User existingUser = userServiceImpl.findById(user.getId(), "id", User.class);
			if (existingUser != null) {
				
				user.setCreator(existingUser.getCreator());
				user.setEnabled(existingUser.isEnabled());
				user.setParentUser(existingUser.getParentUser());
				user.setCreated(existingUser.getCreated());
				user.setUpdated(new Date());
				user.setPersonUUid(existingUser.getPersonUUid());
				user.setUuid(existingUser.getUuid());
				userServiceImpl.update(user, userDTO.getPassword());
				response.put("status", "SUCCESS");
				response.put("msg", "You have been successfully submitted ");
			} else {
				response.put("status", "ERROR");
				response.put("msg", "User does not exists");
			}
		}
		catch (Exception e) {
			e.printStackTrace();
			response.put("status", "ERROR");
			response.put("msg", e.getMessage());
		}
		return new ResponseEntity<>(new Gson().toJson(response.toString()), OK);
	}
	
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public ResponseEntity<String> saveAdd(@RequestBody UserDTO userDTO, ModelMap model) throws Exception {
		JSONObject response = new JSONObject();
		try {
			
			User user = userMapper.map(userDTO);
			
			User loggedInUser = AuthenticationManagerUtil.getLoggedInUser();
			user.setCreator(loggedInUser);
			user.setParentUser(loggedInUser);
			boolean isExists = userServiceImpl.isUserExist(user.getUsername());
			if (!isExists) {
				userServiceImpl.saveNew(user, false);
				response.put("status", "SUCCESS");
				response.put("msg", "You have been successfully submitted ");
				
			} else {
				response.put("status", "ERROR");
				response.put("msg", "User already exists");
			}
		}
		catch (Exception e) {
			e.printStackTrace();
			response.put("status", "ERROR");
			response.put("msg", e.getMessage());
		}
		return new ResponseEntity<>(new Gson().toJson(response.toString()), OK);
	}
	
	@RequestMapping(value = "/change-password", method = RequestMethod.POST)
	public ResponseEntity<String> changeUserPassword(HttpSession session, @RequestBody ChangePasswordDTO dto, ModelMap model) {
		try {
			userServiceImpl.changePassword(session, dto);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return new ResponseEntity<>(new Gson().toJson(""), OK);
	}
	
	@RequestMapping(value = "/password/edit", method = RequestMethod.POST)
	public ResponseEntity<String> editPassword(@RequestBody UserDTO userDTO) throws Exception {
		boolean isExists = userServiceImpl.isUserExist(userDTO.getUsername());
		String userNameUniqueError = "";
		User user = new User();
		if (!isExists) {
			user = userServiceImpl.convert(userDTO);
			userServiceImpl.save(user, false);
		} else {
			userNameUniqueError = "User name alreday taken.";
		}
		return new ResponseEntity<>(new Gson().toJson(userNameUniqueError), OK);
	}
	
	@RequestMapping(value = "/user-with-catchment-area", method = RequestMethod.GET)
	public ResponseEntity<String> userWithCatchmentArea(HttpServletRequest request) throws Exception {
		//		String branch = request.getParameter("branch");
		String role = request.getParameter("role");
		String key = request.getParameter("key");
		if (!StringUtils.isBlank(key)) {
			key = "%" + key + "%";
		}
		String orderColumn = request.getParameter("order[0][column]");
		String orderDirection = request.getParameter("order[0][dir]");
		//orderColumn = UserColumn.valueOf("_" + orderColumn).getValue();
		//		int locationId = locationService.getLocationId(request);
		//		Integer branchId = StringUtils.isBlank(branch)?0:Integer.valueOf(branch);
		Integer draw = Integer.valueOf(request.getParameter("draw"));
		Integer roleId = StringUtils.isBlank(role) ? 0 : Integer.valueOf(role);
		Integer start = Integer.valueOf(request.getParameter("start"));
		Integer length = Integer.valueOf(request.getParameter("length"));
		boolean editPermitted = AuthenticationManagerUtil.isPermitted("PERM_UPDATE_USER");
		JSONObject json = new JSONObject();
		json.put("role_id", roleId);
		json.put("key", key);
		json.put("offset", start);
		json.put("limit", length);
		List<String> users = userServiceImpl.getUserList(json);
		Integer totalUser = userServiceImpl.getUserListCount(json);
		
		JSONObject response = userServiceImpl.getUserDataOfDataTable(draw, totalUser, users, editPermitted);
		return new ResponseEntity<>(response.toString(), OK);
	}
}
