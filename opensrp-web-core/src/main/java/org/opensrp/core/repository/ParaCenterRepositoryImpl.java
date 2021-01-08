package org.opensrp.core.repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.hibernate.type.StandardBasicTypes;
import org.opensrp.common.repository.impl.DatabaseRepositoryImpl;
import org.opensrp.core.entity.ParaCenter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class ParaCenterRepositoryImpl implements ParaCenterRepository {
	
	private static final Logger logger = Logger.getLogger(DatabaseRepositoryImpl.class);
	
	private static final String className = "ParaCenter";
	
	@Autowired
	private SessionFactory sessionFactory;
	
	@Transactional
	@Override
	public ParaCenter save(ParaCenter paraCenter) throws Exception {
		Session session = sessionFactory.getCurrentSession();
		
		session.save(paraCenter);
		logger.info("saved successfully: " + paraCenter.getName());
		
		return paraCenter;
	}
	
	@Transactional
	@Override
	public List<ParaCenter> saveAll(List<ParaCenter> paraCenters) throws Exception {
		Session session = sessionFactory.getCurrentSession();
		
		for (int i = 0; i < paraCenters.size(); i++) {
			session.saveOrUpdate(paraCenters.get(i));
		}
		logger.info("saved successfully: all para-centers");
		
		return paraCenters;
	}
	
	@Transactional
	@Override
	public ParaCenter update(ParaCenter paraCenter) {
		Session session = sessionFactory.getCurrentSession();
		
		session.saveOrUpdate(paraCenter);
		logger.info("updated successfully: " + paraCenter.getName());
		
		return paraCenter;
	}
	
	@Transactional
	@Override
	public boolean delete(ParaCenter paraCenter) {
		Session session = sessionFactory.getCurrentSession();
		
		boolean returnValue = false;
		
		logger.info("deleting: " + paraCenter.getName());
		session.delete(paraCenter);
		
		returnValue = true;
		
		return returnValue;
	}
	
	@Transactional
	@Override
	public boolean isExist(ParaCenter paraCenter) {
		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("code", paraCenter.getCode());
		return isExists(params, ParaCenter.class);
	}
	
	@Transactional
	public boolean isExists(Map<String, Object> fieldValues, Class<?> className) {
		Session session = sessionFactory.getCurrentSession();
		List<Object> result = new ArrayList<Object>();
		
		Criteria criteria = session.createCriteria(className);
		for (Map.Entry<String, Object> entry : fieldValues.entrySet()) {
			criteria.add(Restrictions.eq(entry.getKey(), entry.getValue()));
			criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		}
		result = criteria.list();
		
		return (result.size() > 0 ? true : false);
	}
	
	@SuppressWarnings("unchecked")
	@Transactional
	@Override
	public List<ParaCenter> findAllByName(String name, Integer length, Integer start, String orderColumn,
	                                      String orderDirection) {
		Session session = sessionFactory.getCurrentSession();
		List<ParaCenter> paraCenters = new ArrayList<ParaCenter>();
		String condition = "";
		if (!StringUtils.isBlank(name)) {
			name = name.toUpperCase();
			condition += " where upper(name) like '%" + name + "%'";
		}
		
		String queryString = "from ParaCenter" + condition + " order by " + orderColumn + " " + orderDirection;
		System.out.println("QUERY:: " + queryString);
		paraCenters = session.createQuery(queryString).setFirstResult(start).setMaxResults(length).list();
		
		return paraCenters;
	}
	
	@SuppressWarnings("unchecked")
	@Transactional
	@Override
	public List<ParaCenter> findAllById(List<Integer> ids) {
		Session session = sessionFactory.openSession();
		List<ParaCenter> paraCenters = new ArrayList<ParaCenter>();
		
		String hql = "from ParaCenter where id in :ids";
		Query query = session.createQuery(hql).setParameterList("ids", ids);
		paraCenters = query.list();
		
		return paraCenters.size() > 0 ? paraCenters : null;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	@Transactional
	public List<ParaCenter> findAllByUnionId(List<Integer> ids) {
		Session session = sessionFactory.getCurrentSession();
		List<ParaCenter> paraCenters = new ArrayList<ParaCenter>();
		
		String hql = "from ParaCenter where union_id in :ids";
		Query query = session.createQuery(hql).setParameterList("ids", ids);
		paraCenters = query.list();
		
		return paraCenters.size() > 0 ? paraCenters : null;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	@Transactional
	public Long countTotal(String name) {
		Session session = sessionFactory.openSession();
		String condition = "";
		if (!StringUtils.isBlank(name)) {
			name = name.toUpperCase();
			condition += " where upper(name) like '%" + name + "%'";
		}
		List<Long> paraCenterSize = new ArrayList<Long>();
		condition += ";";
		
		String hql = "select count(id) totalParaCenter from core.para_center" + condition;
		paraCenterSize = session.createSQLQuery(hql).addScalar("totalParaCenter", StandardBasicTypes.LONG).list();
		
		return paraCenterSize.get(0);
	}
}
