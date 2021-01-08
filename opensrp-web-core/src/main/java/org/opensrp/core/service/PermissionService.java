/**
 * @author proshanto
 * */

package org.opensrp.core.service;

import java.util.List;

import org.opensrp.common.interfaces.DatabaseRepository;
import org.opensrp.common.util.PermissionName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PermissionService {
	
	@Autowired
	private DatabaseRepository repository;
	
	public PermissionService() {
		
	}
	
	public void addPermission() throws Exception {
		for (PermissionName permission : PermissionName.values()) {
			if (findByKey(permission.name(), "name", org.opensrp.core.entity.Permission.class) == null) {
				org.opensrp.core.entity.Permission perm = new org.opensrp.core.entity.Permission();
				perm.setName(permission.name());
				save(perm);
			}
		}
	}
	
	public <T> long save(T t) throws Exception {
		return repository.save(t);
	}
	
	public <T> int update(T t) {
		// TODO Auto-generated method stub
		return 0;
	}
	
	public <T> boolean delete(T t) {
		// TODO Auto-generated method stub
		return false;
	}
	
	public <T> T findById(int id, String fieldName, Class<?> className) {
		// TODO Auto-generated method stub
		return null;
	}
	
	public <T> T findByKey(String value, String fieldName, Class<?> className) {
		return repository.findByKey(value, fieldName, className);
	}
	
	public <T> List<T> findAll(String tableClass) {
		// TODO Auto-generated method stub
		return repository.findAll("Permission");
	}
}
