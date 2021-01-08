package org.opensrp.common.repository.impl;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.type.StandardBasicTypes;
import org.opensrp.common.interfaces.LocationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class LocationRepositoryImpl implements LocationRepository {
	
	private static final Logger logger = Logger.getLogger(DatabaseRepositoryImpl.class);
	
	@Autowired
	private SessionFactory sessionFactory;
	
	@Override
	@Transactional
	public <T> List<T> getLocationsHierarchy(String name, Integer tagId, Integer length, Integer start, String orderColumn,
	                                         String orderDirection) {
		Session session = sessionFactory.getCurrentSession();
		String condition = " where locationTag.id = " + tagId;
		List<T> districtList = new ArrayList<T>();
		if (!StringUtils.isBlank(name)) {
			condition += " and upper(name) like '%" + name.toUpperCase() + "%'";
		}
		
		String hql = "from Location" + condition + " order by " + orderColumn + " " + orderDirection;
		districtList = session.createQuery(hql).setFirstResult(start).setMaxResults(length).list();
		
		return districtList;
	}
	
	@Override
	@Transactional
	public <T> T countTotal(String name, Integer tagId) {
		Session session = sessionFactory.getCurrentSession();
		String condition = " where location_tag_id = " + tagId;
		if (!StringUtils.isBlank(name))
			condition += " and upper(name) like '%" + name.toUpperCase() + "%'";
		List<T> locationSize = new ArrayList<T>();
		condition += ";";
		
		String hql = "select count(id) totalLocation from core.location" + condition;
		locationSize = session.createSQLQuery(hql).addScalar("totalLocation", StandardBasicTypes.INTEGER).list();
		
		return locationSize.get(0);
	}
	
	@Override
	@Transactional
	public <T> List<T> findAllByParents(List<Integer> ids) {
		Session session = sessionFactory.getCurrentSession();
		List<T> locations = new ArrayList<T>();
		
		String hql = "from Location where parent_location_id in :ids";
		Query query = session.createQuery(hql).setParameterList("ids", ids);
		locations = query.list();
		
		return locations.size() > 0 ? locations : null;
	}
}
