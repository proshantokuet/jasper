package org.opensrp.web.rest.controller;

import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.http.HttpStatus.OK;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;
import org.opensrp.core.dto.TopicDTO;
import org.opensrp.core.dto.TopicType;
import org.opensrp.core.entity.Message;
import org.opensrp.core.entity.Topic;
import org.opensrp.core.service.CommonService;
import org.opensrp.core.service.mapper.TopicMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;

@RestController
@RequestMapping("rest/api/v1/topic")
public class TopicRestController {
	
	@Autowired
	private TopicMapper topicMapper;
	
	@Autowired
	private CommonService commonService;
	
	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public ResponseEntity<String> saveOrUpdate(@RequestBody TopicDTO dto) throws JSONException {
		
		JSONObject response = new JSONObject();
		try {
			Topic topic = commonService.findById(dto.getId(), "id", Topic.class);
			if (topic == null) {
				Topic exist = commonService.findByKey(dto.getName(), "name", Topic.class);
				System.err.println("dd:" + exist);
				if (exist != null) {
					response.put("msg", dto.getName() + " already exists please try another.");
					response.put("status", "ERROR");
					return new ResponseEntity<>(new Gson().toJson(response.toString()), HttpStatus.OK);
				}
				topic = new Topic();
				topic = topicMapper.map(dto, topic);
				response.put("msg", "Topic has been saved successfully ");
			} else {
				
				Map<String, Object> params = new HashMap<String, Object>();
				params.put("name", dto.getName());
				boolean isExists = commonService.isExistsWithoutThis("id", topic.getId(), params, Topic.class);
				if (isExists) {
					response.put("msg", dto.getName() + " already exists please try another.");
					response.put("status", "ERROR");
					return new ResponseEntity<>(new Gson().toJson(response.toString()), HttpStatus.OK);
				}
				topic = topicMapper.map(dto, topic);
				response.put("msg", "Topic has been updated successfully ");
			}
			
			commonService.saveOrUpdate(topic);
			response.put("status", "CREATED");
		}
		catch (Exception e) {
			response.put("status", "ERROR");
			response.put("msg", e.getMessage());
		}
		return new ResponseEntity<>(new Gson().toJson(response.toString()), HttpStatus.OK);
	}
	
	@RequestMapping(value = "/message", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
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
	
	@RequestMapping(value = "/topic-type", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
	public ResponseEntity<String> topicType() throws JSONException {
		try {
			List<TopicType> topicTypes = commonService.getTopicType();
			return new ResponseEntity<>(new Gson().toJson(topicTypes), OK);
		}
		catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(new Gson().toJson(e.getMessage()), NO_CONTENT);
		}
	}
}
