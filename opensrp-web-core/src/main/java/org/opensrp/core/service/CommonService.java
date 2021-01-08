package org.opensrp.core.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.hibernate.type.StandardBasicTypes;
import org.opensrp.common.interfaces.DatabaseRepository;
import org.opensrp.core.dto.Configs;
import org.opensrp.core.dto.TopicType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 * <p>
 * Concrete implementation of a DatabaseRepository.<br/>
 * The main contract here is the communication with hibernate repository through
 * {@link #sessionFactory}.currently Various types of query and database operation are supported
 * both entity class and view, but has a lot of chance to improve it and its gets maturity day by
 * day.So its only perform Database operation related action.<br/>
 * </p>
 * <b>Exposes One interface:</b>
 * <ul>
 * <li>{@link DatabaseRepository} to the application</li>
 * </ul>
 * <br/>
 * This class is not thread-safe.
 *
 * @author proshanto
 * @author nursat
 * @author prince
 * @version 0.1.0
 * @since 2018-05-30
 */
@Repository
public class CommonService {
	
	private static final Logger logger = Logger.getLogger(CommonService.class);
	
	@Autowired
	private SessionFactory sessionFactory;
	
	@Autowired
	private DatabaseRepository repository;
	
	@Transactional
	public <T> int update(T t) throws Exception {
		return repository.update(t);
	}
	
	public Session getSessionFactory() {
		return sessionFactory.getCurrentSession();
	}
	
	public <T> boolean delete(T t) {
		return repository.delete(t);
	}
	
	@Transactional
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
	
	@Transactional
	public boolean isExistsWithoutThis(String primaryKey, int value, Map<String, Object> fieldValues, Class<?> className) {
		Session session = sessionFactory.getCurrentSession();
		List<Object> result = new ArrayList<Object>();
		
		Criteria criteria = session.createCriteria(className);
		for (Map.Entry<String, Object> entry : fieldValues.entrySet()) {
			criteria.add(Restrictions.eq(entry.getKey(), entry.getValue()));
			criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		}
		criteria.add(Restrictions.ne(primaryKey, value));
		result = criteria.list();
		
		return (result.size() > 0 ? true : false);
	}
	
	@Transactional
	public <T> T saveOrUpdate(T t) throws Exception {
		Session session = sessionFactory.getCurrentSession();
		
		session.saveOrUpdate(t);
		logger.info("saved successfully: " + t.getClass().getName());
		
		return t;
	}
	
	@Transactional
	public boolean UpdateByTopicId(int topicId) {
		Session session = sessionFactory.getCurrentSession();
		boolean returnValue = false;
		
		String hql = "update core.topic_messages set status=false  where topic_id = :topic_id";
		Query query = session.createSQLQuery(hql).setInteger("topic_id", topicId);
		query.executeUpdate();
		
		returnValue = true;
		
		return returnValue;
	}
	
	@SuppressWarnings("unchecked")
	@Transactional
	public List<TopicType> getTopicType() {
		
		Session session = getSessionFactory();
		List<TopicType> topicList = new ArrayList<TopicType>();
		String hql = "select  id,name from core.topic_type";
		
		Query query = session.createSQLQuery(hql).addScalar("id", StandardBasicTypes.LONG)
		        .addScalar("name", StandardBasicTypes.STRING)
		        .setResultTransformer(new AliasToBeanResultTransformer(TopicType.class));
		topicList = query.list();
		
		return topicList;
	}
	
	@SuppressWarnings("unchecked")
	@Transactional
	public List<Configs> getConfigs(String type) {
		
		Session session = getSessionFactory();
		List<Configs> configs = new ArrayList<Configs>();
		String hql = "select  id,name,type from core.config where type=:type";
		
		Query query = session.createSQLQuery(hql).addScalar("id", StandardBasicTypes.LONG)
		        .addScalar("name", StandardBasicTypes.STRING).addScalar("type", StandardBasicTypes.STRING)
		        .setString("type", type).setResultTransformer(new AliasToBeanResultTransformer(Configs.class));
		configs = query.list();
		
		return configs;
	}
	
	@Transactional
	public <T> T findById(Long id, String fieldName, Class<?> className) {
		Session session = getSessionFactory();
		List<T> result = new ArrayList<T>();
		
		Criteria criteria = session.createCriteria(className);
		criteria.add(Restrictions.eq(fieldName, id));
		result = criteria.list();
		
		return result.size() > 0 ? (T) result.get(0) : null;
	}
}
