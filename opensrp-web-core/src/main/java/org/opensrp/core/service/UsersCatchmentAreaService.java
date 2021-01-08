package org.opensrp.core.service;

import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.opensrp.common.interfaces.DatabaseRepository;
import org.opensrp.core.entity.UsersCatchmentArea;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UsersCatchmentAreaService {
	
	private static final Logger logger = Logger.getLogger(UsersCatchmentAreaService.class);
	
	@Autowired
	private DatabaseRepository repository;
	
	@Autowired
	private SessionFactory sessionFactory;
	
	public <T> long save(T t) throws Exception {
		return repository.save(t);
	}
	
	public <T> long saveAll(List<T> t) throws Exception {
		return repository.saveAll(t);
	}
	
	public <T> int update(T t) throws Exception {
		return repository.update(t);
	}
	
	public <T> boolean delete(T t) {
		return repository.delete(t);
	}
	
	public <T> boolean deleteAllByKeys(List<Integer> locationIds, Integer userId) {
		return repository.deleteAllByKeys(locationIds, userId);
	}
	
	public <T> T findById(int id, String fieldName, Class<?> className) {
		return repository.findById(id, fieldName, className);
	}
	
	public <T> T findByKey(String value, String fieldName, Class<?> className) {
		return repository.findByKey(value, fieldName, className);
	}
	
	public <T> List<T> findAllByForeignKey(int value, String fieldName, String className) {
		return repository.findAllByForeignKey(value, fieldName, className);
	}
	
	public <T> T findOneByKeys(Map<String, Object> fielaValues, Class<?> className) {
		return repository.findByKeys(fielaValues, className);
	}
	
	public <T> List<T> findAllByKeys(Map<String, Object> fieldValues) {
		return repository.findAllByKeys(fieldValues, UsersCatchmentArea.class);
	}
	
	public <T> T findLastByKeys(Map<String, Object> fielaValues, String orderByFieldName, Class<?> className) {
		return repository.findLastByKey(fielaValues, orderByFieldName, className);
	}
	
	public <T> List<T> findAll(String tableClass) {
		return repository.findAll(tableClass);
	}
	
	@Transactional
	public Integer deleteAllByParentAndUser(int parentId, int userId) {
		Session session = sessionFactory.getCurrentSession();
		Integer usersCatchmentAreas = 0;
		
		String hql = "delete from core.users_catchment_area where parent_location_id = :parentId and user_id = :userId";
		usersCatchmentAreas = session.createSQLQuery(hql).setInteger("parentId", parentId).setInteger("userId", userId)
		        .executeUpdate();
		
		return usersCatchmentAreas;
	}
	
	@Transactional
	public Integer deleteByUser(Integer userId) {
		Session session = sessionFactory.getCurrentSession();
		Integer usersCatchmentAreas = 0;
		
		String hql = "delete from core.users_catchment_area where user_id = :userId";
		usersCatchmentAreas = session.createSQLQuery(hql).setInteger("userId", userId).executeUpdate();
		
		return usersCatchmentAreas;
	}
	
	public List<Object[]> getUsersCatchmentAreaTableAsJson(int userId) {
		return repository.getCatchmentArea(userId);
	}
	
	public int deleteCatchmentAreas(List<Integer> ids) {
		return repository.deleteCatchmentAreas(ids);
	}
	
	public List<Object[]> getCatchmentAreaForUserAsJson(int userId) {
		return repository.getCatchmentAreaForUser(userId);
	}
	
}
