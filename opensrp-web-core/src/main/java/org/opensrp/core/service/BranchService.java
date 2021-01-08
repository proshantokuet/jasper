package org.opensrp.core.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.hibernate.type.StandardBasicTypes;
import org.opensrp.common.interfaces.DatabaseRepository;
import org.opensrp.core.entity.Branch;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BranchService {
	
	@Autowired
	private DatabaseRepository repository;
	
	@Autowired
	private SessionFactory sessionFactory;
	
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
	
	public <T> T findOneByKeys(Map<String, Object> fieldValues, Class<?> className) {
		return repository.findByKeys(fieldValues, className);
	}
	
	public <T> List<T> findAllByKeys(Map<String, Object> fieldValues, Class<?> className) {
		return repository.findAllByKeys(fieldValues, className);
	}
	
	public <T> T findLastByKeys(Map<String, Object> fieldValues, String orderByFieldName, Class<?> className) {
		return repository.findLastByKey(fieldValues, orderByFieldName, className);
	}
	
	public <T> List<T> findAll(String tableClass) {
		return repository.findAll(tableClass);
	}
	
	public <T> T findByForeignKey(int id, String fieldName, String className) {
		return repository.findByForeignKey(id, fieldName, className);
	}
	
	@Transactional
	@SuppressWarnings("unchecked")
	public List<Branch> getBranchByUser(Integer user_id) {
		List<Branch> lists = new ArrayList<Branch>();
		Session session = sessionFactory.getCurrentSession();
		String hql = "select b.id,b.name,b.code from core.branch as b join core.user_branch as ub on b.id = ub.branch_id where user_id =:user_id";
		
		Query query = session.createSQLQuery(hql)
		
		.addScalar("id", StandardBasicTypes.INTEGER).addScalar("name", StandardBasicTypes.STRING)
		        .addScalar("code", StandardBasicTypes.STRING)
		        .setResultTransformer(new AliasToBeanResultTransformer(Branch.class));
		lists = query.setInteger("user_id", user_id).list();
		
		return lists;
	}
	
	public String commaSeparatedBranch(List<Branch> branches) {
		String branchIds = "";
		int size = branches.size(), iterate = 0;
		for (Branch branch : branches) {
			iterate++;
			branchIds += branch.getId();
			if (size != iterate)
				branchIds += ", ";
		}
		return branchIds;
	}
	
	//	public List<Object[]> getBranchByUser(String branchId, User user) {
	//		List<Object[]> branches = new ArrayList<>();
	//		if(!branchId.isEmpty() ){
	//			Branch branch = findById(Integer.parseInt(branchId), "id", Branch.class);
	//			Object[] obj = new Object[10];
	//			obj[0] = branch.getId();
	//			obj[1] = branch.getName();
	//			branches.add(obj);
	//		}else {
	//			for (Branch branch: user.getBranches()) {
	//				Object[] obj = new Object[10];
	//				obj[0] = branch.getId();
	//				obj[1] = branch.getName();
	//				branches.add(obj);
	//			}
	//		}
	//		return branches;
	//	}
}
