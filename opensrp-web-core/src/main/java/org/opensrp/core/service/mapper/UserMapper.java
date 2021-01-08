package org.opensrp.core.service.mapper;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.opensrp.common.dto.UserDTO;
import org.opensrp.core.entity.Cluster;
import org.opensrp.core.entity.Location;
import org.opensrp.core.entity.User;
import org.opensrp.core.service.ClusterService;
import org.opensrp.core.service.LocationService;
import org.opensrp.core.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserMapper {
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private ClusterService clusterService;
	
	@Autowired
	private LocationService locationService;
	
	public User map(UserDTO userDTO) throws ParseException {
		User user = new User();
		String[] roles = userDTO.getRoles().split(",");
		
		if (userDTO.getId() != null)
			user.setId(userDTO.getId());
		user.setFirstName(userDTO.getFirstName());
		user.setGender(userDTO.getGender());
		user.setIdetifier(userDTO.getIdetifier());
		user.setBirthDate(userDTO.getBirthDate());
		if (!StringUtils.isBlank(userDTO.getJoiningDate() + "")) {
			user.setJoiningDate(userDTO.getJoiningDate());
		}
		
		user.setEmail(userDTO.getEmail());
		user.setEnabled(true);
		user.setAcademicQualification(userDTO.getAcademicQualification());
		user.setEthnicCommunity(userDTO.getEthnicCommunity());
		user.setPermanentAddress(userDTO.getPermanentAddress());
		
		user.setLastName(userDTO.getLastName());
		user.setMobile(userDTO.getMobile());
		user.setPassword(userDTO.getPassword());
		user.setBrId(userDTO.getBrId());
		user.setNationalId(userDTO.getNationalId());
		
		user.setRoles(userService.setRoles(roles));
		User parentUser = userService.getLoggedInUser();
		user.setParentUser(parentUser);
		user.setUsername(userDTO.getUsername());
		
		if (userDTO.getDistricts() != null) {
			Set<Location> districts = new HashSet<>();
			userDTO.getDistricts().forEach(districtId -> {
				Location district = locationService.findById(districtId, "id", Location.class);
				districts.add(district);
			});
			user.setDistricts(districts);
		}
		
		if (userDTO.getUpazilas() != null) {
			Set<Location> upazilas = new HashSet<>();
			userDTO.getUpazilas().forEach(upazilaId -> {
				Location upazila = locationService.findById(upazilaId, "id", Location.class);
				upazilas.add(upazila);
			});
			user.setUpazilas(upazilas);
		}
		
		if (userDTO.getPourasabhas() != null) {
			Set<Location> pourasabhas = new HashSet<>();
			userDTO.getPourasabhas().forEach(pourasabhaId -> {
				Location pourasabha = locationService.findById(pourasabhaId, "id", Location.class);
				pourasabhas.add(pourasabha);
			});
			user.setPourasabhas(pourasabhas);
		}
		
		if (userDTO.getUnions() != null) {
			Set<Location> unions = new HashSet<>();
			userDTO.getUnions().forEach(unionId -> {
				Location union = locationService.findById(unionId, "id", Location.class);
				unions.add(union);
			});
			user.setUnions(unions);
		}
		
		if (userDTO.getClusters() != null) {
			Set<Cluster> clusters = new HashSet<>();
			Cluster cluster = clusterService.findById(userDTO.getClusters());
			clusters.add(cluster);
			user.setClusters(clusters);
		}
		
		return user;
	}
	
	public List<User> map(List<UserDTO> dtos) {
		List<User> users = new ArrayList<>();
		dtos.forEach(r -> {
			try {
				users.add(this.map(r));
			}
			catch (ParseException e) {
				e.printStackTrace();
			}
		});
		return users;
	}
}
