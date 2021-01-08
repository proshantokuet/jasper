package org.opensrp.core.service;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.opensrp.core.entity.ParaCenter;
import org.opensrp.core.repository.ParaCenterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ParaCenterServiceImpl implements ParaCenterService {
	
	@Autowired
	private ParaCenterRepository paraCenterRepository;
	
	@Override
	public ParaCenter save(ParaCenter paraCenter) throws Exception {
		return paraCenterRepository.save(paraCenter);
	}
	
	@Override
	public boolean isExist(ParaCenter paraCenter) {
		return paraCenterRepository.isExist(paraCenter);
	}
	
	@Override
	public List<ParaCenter> findAllByName(String name, Integer length, Integer start, String orderColumn,
	                                      String orderDirection) {
		return paraCenterRepository.findAllByName(name, length, start, orderColumn, orderDirection);
	}
	
	@Override
	public List<ParaCenter> findAllById(List<Integer> ids) {
		return paraCenterRepository.findAllById(ids);
	}
	
	@Override
	public List<ParaCenter> findAllByUnionId(List<Integer> ids) {
		return paraCenterRepository.findAllByUnionId(ids);
	}
	
	@Override
	public Long countTotal(String name) {
		return paraCenterRepository.countTotal(name);
	}
	
	@Override
	public JSONObject getParaCenterDataOfDataTable(Integer draw, Long totalParaCenter, List<ParaCenter> paraCenters)
	    throws JSONException {
		JSONObject response = new JSONObject();
		response.put("draw", draw + 1);
		response.put("recordsTotal", totalParaCenter);
		response.put("recordsFiltered", totalParaCenter);
		JSONArray array = new JSONArray();
		for (ParaCenter entity : paraCenters) {
			JSONArray paraCenter = new JSONArray();
			paraCenter.put(entity.getName());
			paraCenter.put(entity.getCode());
			paraCenter.put(entity.getPara().getName());
			paraCenter.put(entity.getUnion().getName());
			paraCenter.put(entity.getPourasabha().getName());
			paraCenter.put(entity.getUpazila().getName());
			paraCenter.put(entity.getDistrict().getName());
			paraCenter.put(entity.getDivision().getName());
			array.put(paraCenter);
		}
		response.put("data", array);
		
		return response;
	}
	
}
