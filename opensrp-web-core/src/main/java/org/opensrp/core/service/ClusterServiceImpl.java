package org.opensrp.core.service;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.opensrp.core.entity.Cluster;
import org.opensrp.core.entity.Location;
import org.opensrp.core.entity.ParaCenter;
import org.opensrp.core.repository.ClusterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ClusterServiceImpl implements ClusterService {
	
	@Autowired
	private ClusterRepository clusterRepository;
	
	@Override
	public Cluster save(Cluster cluster) throws Exception {
		return clusterRepository.save(cluster);
	}
	
	@Override
	public Cluster findById(Integer id) {
		return clusterRepository.findById(id);
	}
	
	@Override
	public boolean isExist(Cluster cluster) {
		return clusterRepository.isExist(cluster);
	}
	
	@Override
	public List<Cluster> findAllByUnionId(List<Integer> ids) {
		if (ids != null && ids.size() > 0)
			return clusterRepository.findAllByUnionId(ids);
		else
			return new ArrayList<>();
	}
	
	@Override
	public List<Cluster> findAllByName(String name, Integer length, Integer start, String orderColumn, String orderDirection) {
		return clusterRepository.findAllByName(name, length, start, orderColumn, orderDirection);
	}
	
	@Override
	public Long countTotal(String name) {
		return clusterRepository.countTotal(name);
	}
	
	@Override
	public JSONObject getClusterDataOfDataTable(Integer draw, Long totalCluster, List<Cluster> clusters)
	    throws JSONException {
		JSONObject response = new JSONObject();
		response.put("draw", draw + 1);
		response.put("recordsTotal", totalCluster);
		response.put("recordsFiltered", totalCluster);
		JSONArray array = new JSONArray();
		for (Cluster entity : clusters) {
			JSONArray cluster = new JSONArray();
			cluster.put(entity.getName());
			cluster.put(entity.getCode());
			String paraCenters = "";
			
			Integer paraCenterSize = entity.getParaCenters().size();
			int i = 0;
			for (ParaCenter p : entity.getParaCenters()) {
				paraCenters += p.getName();
				i++;
				if (i != paraCenterSize)
					paraCenters += ", ";
			}
			cluster.put(paraCenters);
			
			String unions = "";
			Integer unionSize = entity.getUnions().size();
			i = 0;
			for (Location u : entity.getUnions()) {
				unions += u.getName();
				i++;
				if (i != unionSize)
					unions += ", ";
			}
			cluster.put(unions);
			
			String pourasabhas = "";
			Integer pourasabhaSize = entity.getPourasabhas().size();
			i = 0;
			for (Location pou : entity.getPourasabhas()) {
				pourasabhas += pou.getName();
				i++;
				if (i != pourasabhaSize)
					pourasabhas += ", ";
			}
			cluster.put(pourasabhas);
			
			cluster.put(entity.getUpazila().getName());
			cluster.put(entity.getDistrict().getName());
			cluster.put(entity.getDivision().getName());
			array.put(cluster);
		}
		response.put("data", array);
		
		return response;
	}
}
