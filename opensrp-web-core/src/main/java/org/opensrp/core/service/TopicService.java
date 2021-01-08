package org.opensrp.core.service;

import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.opensrp.common.interfaces.DatabaseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TopicService {
	
	private static final Logger logger = Logger.getLogger(TopicService.class);
	
	@Autowired
	private DatabaseRepository repository;
	
	public <T> long save(T t) throws Exception {
		return repository.save(t);
	}
	
	public <T> int update(T t) throws Exception {
		return repository.update(t);
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
	
	public <T> T findOneByKeys(Map<String, Object> fielaValues, Class<?> className) {
		return repository.findByKeys(fielaValues, className);
	}
	
	public <T> List<T> findAllByKeys(Map<String, Object> fielaValues, Class<?> className) {
		return repository.findAllByKeys(fielaValues, className);
	}
	
	public <T> T findLastByKeys(Map<String, Object> fielaValues, String orderByFieldName, Class<?> className) {
		return repository.findLastByKey(fielaValues, orderByFieldName, className);
	}
	
	public <T> List<T> findAll(String tableClass) {
		return repository.findAll(tableClass);
	}
	
}
