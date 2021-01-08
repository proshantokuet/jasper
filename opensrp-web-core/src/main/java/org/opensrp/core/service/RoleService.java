/**
 * @author proshanto
 * */

package org.opensrp.core.service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.opensrp.common.interfaces.DatabaseRepository;
import org.opensrp.common.util.RoleUtil;
import org.opensrp.core.entity.Permission;
import org.opensrp.core.entity.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RoleService {
	
	@Autowired
	private DatabaseRepository repository;
	
	public RoleService() {
		
	}
	
	public <T> long save(T t) throws Exception {
		
		return repository.save(t);
	}
	
	public <T> int update(T t) {
		return repository.update(t);
	}
	
	public <T> boolean delete(T t) {
		return false;
	}
	
	public <T> T findById(int id, String fieldName, Class<?> className) {
		return repository.findById(id, fieldName, className);
	}
	
	public <T> T findByKey(String value, String fieldName, Class<?> className) {
		return repository.findByKey(value, fieldName, className);
	}
	
	/**
	 * <p>
	 * This method gets all roles.
	 * </p>
	 * 
	 * @param tableClass is class name
	 */
	
	public <T> List<T> findAll(String tableClass) {
		return repository.findAll(tableClass);
	}
	
	public Set<Permission> setPermissions(int[] selectedPermissions) {
		Set<Permission> permissions = new HashSet<Permission>();
		if (selectedPermissions != null) {
			for (int permissionId : selectedPermissions) {
				Permission permission = repository.findById(permissionId, "id", Permission.class);
				permissions.add(permission);
			}
		}
		return permissions;
	}
	
	public boolean isOpenMRSRole(Set<Role> roles) {
		boolean isProvider = false;
		for (Role role : roles) {
			isProvider = RoleUtil.containsRole(role.getName());
			if (isProvider) {
				return false;
			}
		}
		return true;
	}
	
	public boolean isAdmin(Set<Role> roles) {
		boolean isAdmin = false;
		for (Role role : roles) {
			isAdmin = !RoleUtil.containsRole(role.getName());
			if (isAdmin) {
				return true;
			}
		}
		return false;
	}
}
