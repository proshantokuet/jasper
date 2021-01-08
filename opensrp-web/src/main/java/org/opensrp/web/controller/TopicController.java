package org.opensrp.web.controller;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.opensrp.core.dto.TopicType;
import org.opensrp.core.entity.Topic;
import org.opensrp.core.entity.TopicMessage;
import org.opensrp.core.service.CommonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class TopicController {
	
	@Autowired
	private CommonService commonService;
	
	@RequestMapping(value = "/topic-list.html", method = RequestMethod.GET)
	public String list(Model model, Locale locale) {
		List<Topic> topics = commonService.findAll("Topic");
		model.addAttribute("topics", topics);
		
		model.addAttribute("locale", locale);
		model.addAttribute("title", "Topic list");
		return "topic/index";
	}
	
	@RequestMapping(value = "/topic/add.html", method = RequestMethod.GET)
	public String add(Model model, Locale locale) {
		model.addAttribute("locale", locale);
		
		model.addAttribute("title", "Add new topic");
		return "topic/add";
	}
	
	@RequestMapping(value = "/topic/{id}/edit.html", method = RequestMethod.GET)
	public String edit(@PathVariable("id") int id, Model model, Locale locale) {
		Topic topic = commonService.findById(id, "id", Topic.class);
		Set<TopicMessage> messages = topic.getTopicMessage();
		Set<TopicMessage> activeMessages = new HashSet<>();
		for (TopicMessage message : messages) {
			if (message.isStatus()) {
				activeMessages.add(message);
			}
		}
		Set<TopicType> selectedTopicType = new HashSet<TopicType>();
		String topicTypes = topic.getType();
		if (!StringUtils.isBlank(topicTypes)) {
			List<String> topicType = Arrays.asList(topicTypes.split(",", -1));
			for (String name : topicType) {
				TopicType tType = new TopicType();
				tType.setName(name);
				selectedTopicType.add(tType);
			}
		}
		
		model.addAttribute("locale", locale);
		model.addAttribute("topic", topic);
		model.addAttribute("activeMessages", activeMessages);
		model.addAttribute("selectedTopicType", selectedTopicType);
		model.addAttribute("title", "Edit topic");
		return "topic/edit";
	}
	
}
