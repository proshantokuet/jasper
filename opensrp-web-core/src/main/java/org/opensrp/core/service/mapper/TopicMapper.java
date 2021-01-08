package org.opensrp.core.service.mapper;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.apache.commons.lang.IncompleteArgumentException;
import org.opensrp.core.dto.TopicDTO;
import org.opensrp.core.entity.Message;
import org.opensrp.core.entity.Topic;
import org.opensrp.core.entity.TopicMessage;
import org.opensrp.core.service.CommonService;
import org.opensrp.core.service.TopicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TopicMapper {
	
	@Autowired
	private TopicService topicService;
	
	@Autowired
	private CommonService commonService;
	
	public Topic map(TopicDTO dto, Topic topic) throws Exception {
		topic.setName(dto.getName());
		topic.setType(dto.getType());
		topic.setStatus(dto.isStatus());
		topic.setUuid(UUID.randomUUID() + "");
		topic.setTopicMessage(setTopicMessage(dto, topic));
		return topic;
	}
	
	private Set<TopicMessage> setTopicMessage(TopicDTO dto, Topic topic) throws Exception {
		Set<TopicMessage> msgSet = new HashSet<>();
		List<String> messages = dto.getMessages();
		boolean isUpdate = false;
		if (dto.getId() > 0) {
			isUpdate = commonService.UpdateByTopicId(dto.getId());
			
			if (!isUpdate) {
				throw new IncompleteArgumentException("Some problem occured, please contact with admin.");
			}
		} else {
			topic.setId(0);
		}
		for (String name : messages) {
			
			Message msg = commonService.findByKey(name, "name", Message.class);
			if (msg == null) {
				Message _msg = new Message();
				_msg.setName(name);
				_msg.setUuid(UUID.randomUUID() + "");
				msg = commonService.saveOrUpdate(_msg);
			}
			TopicMessage topicMessage;
			if (dto.getId() > 0) {
				Map<String, Object> map = new HashMap<>();
				map.put("topic", topic);
				map.put("message", msg);
				topicMessage = commonService.findOneByKeys(map, TopicMessage.class);
				
			} else {
				topicMessage = null;
			}
			
			if (topicMessage == null) {
				topicMessage = new TopicMessage();
				topicMessage.setMessage(msg);
				topicMessage.setTopic(topic);
				
			} else {
				topicMessage.setTopic(topic);
			}
			topicMessage.setStatus(true);
			msgSet.add(topicMessage);
			
		}
		return msgSet;
		
	}
}
