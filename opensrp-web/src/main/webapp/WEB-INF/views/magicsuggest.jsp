<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<c:url var="messageUrl" value="/rest/api/v1/topic/message" />
<c:url var="topicUrl" value="/rest/api/v1/topic/topic-type" />

<script>
var messages = [];
'<c:forEach items="${activeMessages}" var="itm">'
	messages.push("${itm.getMessage().name}"); 
'</c:forEach>'
window.selectedMessages =  messages


var topics = [];
'<c:forEach items="${selectedTopicType}" var="itm">'
	topics.push("${itm.name}"); 
	
'</c:forEach>'
window.selectedTopics =  topics;

$(function() {	
	$('#message').magicSuggest({
		placeholder: '',
		data : '${messageUrl}',
		method: 'get',
		valueField : 'name',
		displayField : 'name',		
		name : 'message',
		/* inputCfg: {"class":"magicInput"}, */
		id: 'message',
		useCommaKey: true,
		required: true,
		allowFreeEntries: true,
		maxEntryLength:500,
		maxEntryRenderer: function(v) {
			    return '<div style="color:red">Error Typed Word TOO LONG </div>';
			  },
		value: selectedMessages
		
	})
	
	$('#type').magicSuggest({
		placeholder: '',
		data : '${topicUrl}',
		method: 'get',
		valueField : 'name',
		displayField : 'name',		
		name : 'type',
		/* inputCfg: {"class":"magicInput"}, */
		id: 'type',
		useCommaKey: true,
		required: true,
		allowFreeEntries: true,
		maxEntryLength:500,
		maxEntryRenderer: function(v) {
			    return '<div style="color:red">Error Typed Word TOO LONG </div>';
			  },
		value: selectedTopics
		
	})
})	
</script>


