package com.messaging.controller;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.messaging.model.SmsPojo;
import com.messaging.service.SmsService;


@RestController
public class SMSController {

	@Autowired
	private SmsService  service;
	
	@Autowired
	private SimpMessagingTemplate websocket;
	
	private final String TOPIC_DESTINATION = "/lessons/sms";
	
	@RequestMapping(value = "/sms", method = RequestMethod.POST,
			consumes =MediaType.APPLICATION_JSON_VALUE, 
			produces = MediaType.APPLICATION_JSON_VALUE)
	public void smsSubmit(@RequestBody SmsPojo sms) {
		try {
		service.send(sms);
		}
		catch(Exception e) {
			websocket.convertAndSend(TOPIC_DESTINATION,getTimeStamp() + ":Error sending the SMS: "+e.getMessage());
			throw e;
		}
		websocket.convertAndSend(TOPIC_DESTINATION, getTimeStamp() + ": SMS has been sent!: "+sms.getTo());
	}
	
	@RequestMapping(value = "/smscallback", method = RequestMethod.POST,
			consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public void smsCallback(@RequestBody MultiValueMap<String, String> map) {
		service.receive(map);
		websocket.convertAndSend(TOPIC_DESTINATION,getTimeStamp() + ":Twilio has made a callback  request! Here are the contents"
				+map.toString());
	}
	
	private String getTimeStamp() {
		return DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").format(LocalDateTime.now());
	}
}
