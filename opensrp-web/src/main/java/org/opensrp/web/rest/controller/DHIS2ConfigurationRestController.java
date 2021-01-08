package org.opensrp.web.rest.controller;

import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.http.HttpStatus.OK;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;
import org.opensrp.core.dto.DHIS2ConfigurationDTO;
import org.opensrp.core.entity.DhisConfiguration;
import org.opensrp.core.entity.Message;
import org.opensrp.core.service.CommonService;
import org.opensrp.core.service.mapper.DHIS2ConfigurationMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;

@RestController
@RequestMapping("rest/api/v1/dhis2-configuration")
public class DHIS2ConfigurationRestController {
	
	@Autowired
	private DHIS2ConfigurationMapper dhis2ConfigurationMapper;
	
	@Autowired
	private CommonService commonService;
	
	@RequestMapping(value = "/save-or-update", method = RequestMethod.POST)
	public ResponseEntity<String> saveOrUpdate(@RequestBody DHIS2ConfigurationDTO dto) throws JSONException {
		
		JSONObject response = new JSONObject();
		try {
			DhisConfiguration dhisConfiguration = commonService.findById(dto.getId(), "id", DhisConfiguration.class);
			if (dhisConfiguration == null) {
				Map<String, Object> map = new HashMap<>();
				map.put("formName", dto.getFormName());
				map.put("fieldName", dto.getFieldName());
				map.put("dhisId", dto.getDhisId());
				//dhisConfiguration = commonService.findByKey(dto.getDhisId(), "dhisId", DhisConfiguration.class);
				/*if (dhisConfiguration != null) {
					response.put("msg", dto.getDhisId() + " already exists please try another.");
					response.put("status", "ERROR");
					return new ResponseEntity<>(new Gson().toJson(response.toString()), HttpStatus.OK);
				} else {
					dhisConfiguration = null;
				}*/
				dhisConfiguration = commonService.findOneByKeys(map, DhisConfiguration.class);
				
				if (dhisConfiguration != null) {
					response.put("msg", dto.getDhisId() + ", " + dto.getFieldName() + " and " + dto.getFormName()
					        + " already exists please try another.");
					response.put("status", "ERROR");
					return new ResponseEntity<>(new Gson().toJson(response.toString()), HttpStatus.OK);
				}
				dhisConfiguration = new DhisConfiguration();
				dhisConfiguration = dhis2ConfigurationMapper.map(dto, dhisConfiguration);
				response.put("msg", "Dhis2 configuration has been saved successfully ");
			} else {
				
				Map<String, Object> params = new HashMap<String, Object>();
				Map<String, Object> dhis2IdParams = new HashMap<String, Object>();
				params.put("formName", dto.getFormName());
				params.put("fieldName", dto.getFieldName());
				params.put("dhisId", dto.getDhisId());
				//dhis2IdParams.put("dhisId", dto.getDhisId());
				
				/*boolean dhis2IdCheck = commonService.isExistsWithoutThis("id", dhisConfiguration.getId(), dhis2IdParams,
				    DhisConfiguration.class);*/
				/*if (dhis2IdCheck) {
					response.put("msg", dto.getDhisId() + " already exists please try another.");
					response.put("status", "ERROR");
					return new ResponseEntity<>(new Gson().toJson(response.toString()), HttpStatus.OK);
				}*/
				boolean isExists = commonService.isExistsWithoutThis("id", dhisConfiguration.getId(), params,
				    DhisConfiguration.class);
				if (isExists) {
					response.put("msg", dto.getDhisId() + ", " + dto.getFieldName() + " and " + dto.getFormName()
					        + " already exists please try another.");
					response.put("status", "ERROR");
					return new ResponseEntity<>(new Gson().toJson(response.toString()), HttpStatus.OK);
				}
				dhisConfiguration = dhis2ConfigurationMapper.map(dto, dhisConfiguration);
				response.put("msg", "Dhis2 configuration has been updated successfully ");
			}
			
			commonService.saveOrUpdate(dhisConfiguration);
			response.put("status", "CREATED");
		}
		catch (Exception e) {
			response.put("status", "ERROR");
			response.put("msg", e.getMessage());
		}
		return new ResponseEntity<>(new Gson().toJson(response.toString()), HttpStatus.OK);
	}
	
	@RequestMapping(value = "/message", method = RequestMethod.GET)
	public ResponseEntity<String> medicalCondition() throws JSONException {
		try {
			List<Message> messages = commonService.findAll("Message");
			return new ResponseEntity<>(new Gson().toJson(messages), OK);
		}
		catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(new Gson().toJson(e.getMessage()), NO_CONTENT);
		}
	}
}
