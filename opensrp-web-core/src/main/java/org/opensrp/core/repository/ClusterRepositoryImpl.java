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
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.hibernate.type.StandardBasicTypes;
import org.opensrp.common.dto.ClusterDTO;
import org.opensrp.common.repository.impl.DatabaseRepositoryImpl;
import org.opensrp.core.entity.Cluster;
import org.opensrp.core.service.mapper.ClusterMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class ClusterRepositoryImpl implements ClusterRepository {
	
	private static final Logger logger = Logger.getLogger(DatabaseRepositoryImpl.class);
	
	private static final String className = "Cluster";
	
	@Autowired
	private SessionFactory sessionFactory;
	
	@Autowired
	private ClusterMapper clusterMapper;
	
	@Transactional
	@Override
	public Cluster save(Cluster cluster) throws Exception {
		Session session = sessionFactory.getCurrentSession();
		
		session.save(cluster);
		logger.info("saved successfully: " + cluster.getName());
		
		return cluster;
	}
	
	@Transactional
	@Override
	public List<Cluster> saveAll(List<Cluster> clusters) throws Exception {
		Session session = sessionFactory.getCurrentSession();
		
		for (int i = 0; i < clusters.size(); i++) {
			session.saveOrUpdate(clusters.get(i));
		}
		
		return clusters;
	}
	
	@SuppressWarnings("unchecked")
	@Transactional
	@Override
	public Cluster findById(Integer id) {
		Session session = sessionFactory.getCurrentSession();
		List<Cluster> result = new ArrayList<Cluster>();
		
		String sql = "from Cluster where id = :id";
		Query query = session.createQuery(sql).setParameter("id", id);
		result = query.list();
		
		return (result.size() > 0 ? result.get(0) : null);
	}
	
	@SuppressWarnings("unchecked")
	@Transactional
	@Override
	public List<Cluster> findAllByUnionId(List<Integer> ids) {
		Session session = sessionFactory.getCurrentSession();
		List<ClusterDTO> clusters = new ArrayList<ClusterDTO>();
		
		String hql = "select distinct c.id, c.name, c.code from core.cluster c, core.cluster_union cu where c.id = cu.cluster_id and cu.union_id in :ids";
		Query query = session.createSQLQuery(hql).addScalar("id", StandardBasicTypes.INTEGER)
		        .addScalar("name", StandardBasicTypes.STRING).addScalar("code", StandardBasicTypes.STRING)
		        .setParameterList("ids", ids).setResultTransformer(new AliasToBeanResultTransformer(ClusterDTO.class));
		clusters = query.list();
		
		return clusters.size() > 0 ? clusterMapper.map(clusters) : null;
	}
	
	@Transactional
	@Override
	public Cluster update(Cluster cluster) {
		Session session = sessionFactory.getCurrentSession();
		
		session.saveOrUpdate(cluster);
		logger.info("updated successfully: " + cluster.getName());
		
		return cluster;
	}
	
	@Transactional
	@Override
	public boolean delete(Cluster cluster) {
		Session session = sessionFactory.getCurrentSession();
		
		boolean returnValue = false;
		
		session.delete(cluster);
		
		returnValue = true;
		
		return returnValue;
	}
	
	@SuppressWarnings("unchecked")
	@Transactional
	@Override
	public boolean isExist(Cluster cluster) {
		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("code", cluster.getCode());
		
		return isExists(params, Cluster.class);
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
	public List<Cluster> findAllByName(String name, Integer length, Integer start, String orderColumn, String orderDirection) {
		Session session = sessionFactory.getCurrentSession();
		List<Cluster> clusters = new ArrayList<Cluster>();
		String condition = "";
		if (!StringUtils.isBlank(name)) {
			condition += " where name ilike '%" + name + "%'";
		}
		
		String queryString = "from Cluster" + condition + " order by " + orderColumn + " " + orderDirection;
		clusters = session.createQuery(queryString).setFirstResult(start).setMaxResults(length).list();
		
		return clusters;
	}
	
	@SuppressWarnings("unchecked")
	@Transactional
	@Override
	public List<Cluster> findAllById(List<Integer> ids) {
		Session session = sessionFactory.getCurrentSession();
		List<Cluster> clusters = new ArrayList<Cluster>();
		
		String hql = "from Cluster where id in :ids";
		Query query = session.createQuery(hql).setParameterList("ids", ids);
		clusters = query.list();
		
		return clusters.size() > 0 ? clusters : null;
	}
	
	@SuppressWarnings("unchecked")
	@Transactional
	@Override
	public Long countTotal(String name) {
		Session session = sessionFactory.getCurrentSession();
		String condition = "";
		if (!StringUtils.isBlank(name))
			condition += " where name ilike '%" + name + "%'";
		List<Long> clusterSize = new ArrayList<Long>();
		condition += ";";
		
		String hql = "select count(id) totalCluster from core.cluster" + condition;
		clusterSize = session.createSQLQuery(hql).addScalar("totalCluster", StandardBasicTypes.LONG).list();
		
		return clusterSize.get(0);
	}
}
