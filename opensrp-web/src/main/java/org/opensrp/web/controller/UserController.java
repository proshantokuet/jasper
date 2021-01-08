package org.opensrp.web.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.json.JSONException;
import org.opensrp.common.dto.LocationOptionDTO;
import org.opensrp.common.service.impl.DatabaseServiceImpl;
import org.opensrp.common.util.LocationTags;
import org.opensrp.core.dto.Configs;
import org.opensrp.core.entity.Branch;
import org.opensrp.core.entity.Cluster;
import org.opensrp.core.entity.Location;
import org.opensrp.core.entity.Permission;
import org.opensrp.core.entity.Role;
import org.opensrp.core.entity.TeamMember;
import org.opensrp.core.entity.User;
import org.opensrp.core.service.BranchService;
import org.opensrp.core.service.ClusterService;
import org.opensrp.core.service.CommonService;
import org.opensrp.core.service.EmailService;
import org.opensrp.core.service.FacilityService;
import org.opensrp.core.service.FacilityWorkerService;
import org.opensrp.core.service.LocationService;
import org.opensrp.core.service.RoleService;
import org.opensrp.core.service.TeamMemberService;
import org.opensrp.core.service.UserService;
import org.opensrp.core.service.UsersCatchmentAreaService;
import org.opensrp.core.service.mapper.UserMapper;
import org.opensrp.core.util.FacilityHelperUtil;
import org.opensrp.web.util.PaginationUtil;
import org.opensrp.web.util.SearchUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

/**
 * <p>
 * The UserController program implements an application that simply contains the user related
 * information such as(show user list, add user, edit user etc).
 * </p>
 * 
 * @author proshanto.
 * @version 0.1.0
 * @since 2018-03-30
 */

@Controller
public class UserController {
	
	private static final Logger logger = Logger.getLogger(UserController.class);
	
	private static final int childRoleId = 29;
	
	private static final int villageTagId = 33;
	
	@Value("#{opensrp['bahmni.url']}")
	private String BAHMNI_VISIT_URL;
	
	@Autowired
	private DatabaseServiceImpl databaseServiceImpl;
	
	@Autowired
	private ClusterService clusterService;
	
	@Autowired
	private User account;
	
	@Autowired
	private Permission permission;
	
	@Autowired
	private UserService userServiceImpl;
	
	@Autowired
	private RoleService roleServiceImpl;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Autowired
	private PaginationUtil paginationUtil;
	
	@Autowired
	private LocationService locationServiceImpl;
	
	@Autowired
	private TeamMemberService teamMemberServiceImpl;
	
	@Autowired
	private FacilityService facilityService;
	
	@Autowired
	FacilityWorkerService facilityWorkerService;
	
	@Autowired
	private TeamMember teamMember;
	
	@Autowired
	private EmailService emailService;
	
	@Autowired
	private FacilityHelperUtil facilityHelperUtil;
	
	@Autowired
	private BranchService branchService;
	
	@Autowired
	private UsersCatchmentAreaService usersCatchmentAreaService;
	
	@Autowired
	private SearchUtil searchUtil;
	
	@Autowired
	private UserMapper userMapper;
	
	@Autowired
	private CommonService commonService;
	
	/**
	 * <p>
	 * showing user list, support pagination with search by user name
	 * </p>
	 * 
	 * @param request is an argument to the servlet's service
	 * @param session is an argument to the HttpSession's session
	 * @param model defines a holder for model attributes.
	 * @param locale is an argument to holds locale.
	 * @return user list view page
	 */
	@PostAuthorize("hasPermission(returnObject, 'USER')")
	@RequestMapping(value = "/user.html", method = RequestMethod.GET)
	public String userList(HttpServletRequest request, HttpSession session, Model model,
	                       @RequestParam(value = "role", required = false) Integer roleId,
	                       @RequestParam(value = "branch", required = false) Integer branchId,
	                       @RequestParam(value = "name", required = false) String name, Locale locale) {
		model.addAttribute("locale", locale);
		roleId = (roleId == null ? 0 : roleId);
		branchId = (branchId == null ? 0 : branchId);
		searchUtil.setDivisionAttribute(session);
		List<Branch> branches = branchService.findAll("Branch");
		List<Role> roles = roleServiceImpl.findAll("Role");
		session.setAttribute("branches", branches);
		session.setAttribute("roles", roles);
		session.setAttribute("selectedRole", roleId);
		session.setAttribute("selectedBranch", branchId);
		return "user/index";
	}
	
	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public String loginPage() {
		return "user/login";
	}
	
	@RequestMapping(value = "/session-expired", method = RequestMethod.GET)
	public ModelAndView sessionExpired(ModelAndView modelAndView) {
		
		modelAndView.setViewName("user/login");
		modelAndView.addObject("sessionExpiredMsg", "Your session has expired, please login again to continue");
		return modelAndView;
	}
	
	@PostAuthorize("hasPermission(returnObject, 'ADD_USER')")
	@RequestMapping(value = "/user/add-ajax.html", method = RequestMethod.GET)
	public ModelAndView addAjaxUser(Model model, HttpSession session, Locale locale) throws JSONException {
		int[] selectedRoles = null;
		List<Role> roles = userServiceImpl.setRolesAttributes(selectedRoles, session);
		model.addAttribute("districts", locationServiceImpl.getLocationByTag(LocationTags.DISTRICT.getId()));
		model.addAttribute("locale", locale);
		model.addAttribute("roles", roles);
		List<Configs> configs = commonService.getConfigs("aca");
		
		model.addAttribute("configs", configs);
		return new ModelAndView("user/add-ajax", "command", account);
	}
	
	@PostAuthorize("hasPermission(returnObject, 'ADD_USER')")
	@RequestMapping(value = "/user/{id}/change-password.html", method = RequestMethod.GET)
	public ModelAndView editPassword(Model model, HttpSession session, @PathVariable("id") int id, Locale locale) {
		model.addAttribute("locale", locale);
		User account = userServiceImpl.findById(id, "id", User.class);
		session.setAttribute("username", account.getUsername());
		return new ModelAndView("user/change-password");
	}
	
	@PostAuthorize("hasPermission(returnObject, 'ADD_USER')")
	@RequestMapping(value = "/user/{id}/change-password-ajax.html", method = RequestMethod.GET)
	public ModelAndView editPasswordAM(Model model, HttpSession session, @PathVariable("id") int id, Locale locale) {
		model.addAttribute("locale", locale);
		User account = userServiceImpl.findById(id, "id", User.class);
		model.addAttribute("account", account);
		session.setAttribute("username", account.getUsername());
		return new ModelAndView("user/change-password-ajax");
	}
	
	@RequestMapping(value = "/logout", method = RequestMethod.GET)
	public String logoutPage(HttpServletRequest request, HttpServletResponse response, Locale locale) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth != null) {
			new SecurityContextLogoutHandler().logout(request, response, auth);
		}
		return "redirect:/login?logout?lang=" + locale;//You can redirect wherever you want, but generally it's a good practice to show login screen again.
	}
	
	@RequestMapping(value = "user/provider.html", method = RequestMethod.GET)
	public String providerSearch(Model model, HttpSession session, @RequestParam String name) throws JSONException {
		
		List<User> users = userServiceImpl.findAllByKeysWithALlMatches(name, true);
		session.setAttribute("searchedUsers", users);
		return "user/search";
	}
	
	@RequestMapping(value = "user/user.html", method = RequestMethod.GET)
	public String userSearch(Model model, HttpSession session, @RequestParam String name) throws JSONException {
		System.err.println("user home start:" + System.currentTimeMillis());
		List<User> users = userServiceImpl.findAllByKeysWithALlMatches(name, false);
		session.setAttribute("searchedUsers", users);
		System.err.println("user home end:" + System.currentTimeMillis());
		return "user/search";
	}
	
	@RequestMapping(value = "user/edit-ajax.html", method = RequestMethod.GET)
	public ModelAndView editAjax(HttpSession session, @RequestParam("id") int id, Locale locale, Model model)
	    throws JSONException {
		int[] selectedRoles = null;
		Map<Integer, String> loc = new HashMap<>();
		
		User user = userServiceImpl.findById(id, "id", User.class);
		List<Role> roles = userServiceImpl.setRolesAttributes(selectedRoles, session);
		List<Cluster> clusters = new ArrayList<>();
		
		List<Location> districts = locationServiceImpl.getLocationByTag(LocationTags.DISTRICT.getId());
		List<Location> selectedDistricts = new ArrayList<>(user.getDistricts());
		for (Location l : selectedDistricts)
			loc.put(l.getId(), "selected");
		
		List<Location> selectedUpazilas = new ArrayList<>(user.getUpazilas());
		List<Location> upazilas = locationServiceImpl.getSiblings(selectedUpazilas);
		for (Location l : selectedUpazilas)
			loc.put(l.getId(), "selected");
		
		List<Location> selectedPourasabhas = new ArrayList<>(user.getPourasabhas());
		List<Location> pourasabhas = locationServiceImpl.getSiblings(selectedPourasabhas);
		for (Location l : selectedPourasabhas)
			loc.put(l.getId(), "selected");
		
		List<Location> selectedUnions = new ArrayList<>(user.getUnions());
		List<Location> unions = locationServiceImpl.getSiblings(selectedUnions);
		for (Location l : selectedUnions)
			loc.put(l.getId(), "selected");
		
		List<Integer> unionIds = locationServiceImpl.getLocationIds(selectedUnions);
		
		clusters = clusterService.findAllByUnionId(unionIds);
		
		List<LocationOptionDTO> districtWithSelected = new ArrayList<>();
		List<LocationOptionDTO> upazilaWithSelected = new ArrayList<>();
		List<LocationOptionDTO> pourasabhaWithSelected = new ArrayList<>();
		List<LocationOptionDTO> unionWithSelected = new ArrayList<>();
		
		for (Location location : districts)
			districtWithSelected.add(new LocationOptionDTO(location.getId(), location.getName(), loc.get(location.getId())));
		for (Location location : upazilas)
			upazilaWithSelected.add(new LocationOptionDTO(location.getId(), location.getName(), loc.get(location.getId())));
		for (Location location : pourasabhas)
			pourasabhaWithSelected
			        .add(new LocationOptionDTO(location.getId(), location.getName(), loc.get(location.getId())));
		for (Location location : unions)
			unionWithSelected.add(new LocationOptionDTO(location.getId(), location.getName(), loc.get(location.getId())));
		
		Integer selectedClusterId = user.getClusters() != null && user.getClusters().size() > 0 ? new ArrayList<>(
		        user.getClusters()).get(0).getId() : 0;
		Integer selectedRoleId = new ArrayList<>(user.getRoles()).get(0).getId();
		
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		String birthDate = formatter.format(user.getBirthDate());
		//String joiningDate = formatter.format(user.getJoiningDate());
		
		model.addAttribute("locale", locale);
		model.addAttribute("roles", roles);
		model.addAttribute("clusters", clusters);
		model.addAttribute("user", user);
		model.addAttribute("birthDate", birthDate);
		//model.addAttribute("joiningDate", joiningDate);
		model.addAttribute("districts", districtWithSelected);
		model.addAttribute("upazilas", upazilaWithSelected);
		model.addAttribute("pourasabhas", pourasabhaWithSelected);
		model.addAttribute("unions", unionWithSelected);
		session.setAttribute("selectedClusterId", selectedClusterId);
		session.setAttribute("selectedRoleId", selectedRoleId);
		
		List<Configs> configs = commonService.getConfigs("aca");
		
		model.addAttribute("configs", configs);
		return new ModelAndView("user/edit-ajax", "command", account);
	}
	
}
