package org.opensrp.core.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javax.transaction.Transactional;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.json.JSONArray;
import org.json.JSONObject;
import org.opensrp.common.interfaces.DatabaseRepository;
import org.opensrp.core.entity.HealthId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class HealthIdService {
	
	private static final Logger logger = Logger.getLogger(HealthIdService.class);
	
	private static int HEALTH_ID_LIMIT = 200;
	
	@Autowired
	private DatabaseRepository repository;
	
	@Autowired
	private SessionFactory sessionFactory;
	
	@Autowired
	HealthId healthId;
	
	public HealthIdService() {
		
	}
	
	public <T> long save(T t) throws Exception {
		return repository.save(t);
	}
	
	public <T> long saveAll(List<T> t) throws Exception {
		return repository.saveAll(t);
	}
	
	public <T> int delete(T t) {
		int i = 0;
		if (repository.delete(t)) {
			i = 1;
		} else {
			i = -1;
		}
		return i;
	}
	
	public <T> T findById(int id, String fieldName, Class<?> className) {
		return repository.findById(id, fieldName, className);
	}
	
	@SuppressWarnings("unchecked")
	public <T> List<T> findAll(String tableClass) {
		return (List<T>) repository.findAll(tableClass);
	}
	
	public <T> T findByKey(String value, String fieldName, Class<?> className) {
		return repository.findByKey(value, fieldName, className);
	}
	
	public <T> long update(T t) throws Exception {
		return repository.update(t);
	}
	
	@SuppressWarnings("resource")
	public String uploadHealthId(File csvFile) throws Exception {
		String msg = "";
		BufferedReader br = null;
		String line = "";
		String cvsSplitBy = ",";
		
		int position = 0;
		String[] tags = null;
		try {
			br = new BufferedReader(new FileReader(csvFile));
			int count = 0;
			while ((line = br.readLine()) != null) {
				String[] healthIdFromCsv = line.split(cvsSplitBy);
				if (position == 0) {
					tags = healthIdFromCsv;
					//logger.info("tags >> " + healthIdFromCsv[0]);
				} else {
					
					String hId = healthIdFromCsv[0].trim();
					if (!hId.isEmpty() && hId != null) {
						HealthId matchedHealthId = findByKey(hId, "hId", HealthId.class);
						if (matchedHealthId != null) {
							logger.info("<><><> Similar hId :" + matchedHealthId.toString());
						} else {
							HealthId healthId = new HealthId();
							healthId.sethId(healthIdFromCsv[0].trim()); // health_id
							healthId.setType("Reserved");
							logger.info(healthId.toString());
							save(healthId);
							count++;
						}
					}
				}
				position++;
			}
			msg = "Number of health-id uploaded successfully :  " + count;
			
		}
		catch (Exception e) {
			e.printStackTrace();
			msg = "Exception occured - " + e.getMessage() + " - " + e.toString();
			logger.info(msg);
		}
		return msg;
	}
	
	@Transactional
	public synchronized JSONObject getHealthIdAndUpdateRecord() throws Exception {
		
		JSONObject healthIds = new JSONObject();
		Session session = sessionFactory.getCurrentSession();
		
		Criteria criteria = session.createCriteria(HealthId.class);
		criteria.setMaxResults(250);
		criteria.add(Restrictions.eq("status", false));
		criteria.add(Restrictions.eq("type", "Reserved"));
		criteria.addOrder(Order.asc("id"));
		List<HealthId> result = criteria.list();
		List<String> list = new ArrayList<String>();
		for (HealthId healthId : result) {
			healthId.setStatus(true);
			session.saveOrUpdate(healthId);
			list.add(healthId.gethId());
		}
		if (list.size() != 0) {
			healthIds.put("identifiers", list);
		}
		
		return healthIds;
	}
	
	@Transactional
	public JSONObject getSingleHealthIdAndUpdateRecord() throws Exception {
		JSONObject healthIds = new JSONObject();
		Session session = sessionFactory.getCurrentSession();
		
		Criteria criteria = session.createCriteria(HealthId.class);
		criteria.setMaxResults(1);
		criteria.add(Restrictions.eq("status", false));
		criteria.add(Restrictions.eq("type", "Reserved"));
		criteria.addOrder(Order.asc("id"));
		List<HealthId> result = criteria.list();
		for (HealthId healthId : result) {
			healthId.setStatus(true);
			if (update(healthId) == 1) {
				healthIds.put("identifiers", healthId.gethId());
			}
			;
		}
		
		return healthIds;
	}
	
	public static <T, U> List<U> convertIntListToStringList(List<T> listOfInteger, Function<T, U> function) {
		return listOfInteger.stream().map(function).collect(Collectors.toList());
	}
	
	public JSONArray generateHouseholdId(int[] villageIds) throws Exception {
		JSONArray villageCodes = new JSONArray();
		for (int i = 0; i < villageIds.length; i++) {
			if (villageIds[i] == 0)
				break;
			Integer number = repository.maxByHealthId(villageIds[i], "location_id", "health_id");
			
			List<Integer> listOfInteger = IntStream.rangeClosed(number + 1, number + HEALTH_ID_LIMIT).boxed()
			        .collect(Collectors.toList());
			List<String> listOfString = convertIntListToStringList(listOfInteger,
			    s -> StringUtils.leftPad(String.valueOf(s), 4, "0"));
			
			HealthId healthId = new HealthId();
			
			healthId.setCreated(new Date());
			healthId.sethId(String.valueOf(number + HEALTH_ID_LIMIT));
			healthId.setLocationId(villageIds[i]);
			healthId.setStatus(true);
			
			long isSaved = repository.save(healthId);
			if (isSaved > 0) {
				JSONObject villageCode = new JSONObject();
				villageCode.put("village_id", villageIds[i]);
				JSONArray ids = new JSONArray();
				for (String healthId1 : listOfString) {
					ids.put(healthId1);
				}
				villageCode.put("generated_code", ids);
				villageCodes.put(villageCode);
			}
		}
		return villageCodes;
	}
}
