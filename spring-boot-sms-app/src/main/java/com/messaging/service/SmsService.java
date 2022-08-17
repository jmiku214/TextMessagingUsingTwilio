package com.messaging.service;

import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;

import com.messaging.model.SmsPojo;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;

@Component
public class SmsService {

    private final String ACCOUNT_SID ="AC2f18b7aec9695416c5f4556b300897bf";

    private final String AUTH_TOKEN = "b97832cd890c13b4b499b2ab5a8b0751";

    private final String FROM_NUMBER = "+19707037874";

    public void send(SmsPojo sms) {
    	Twilio.init(ACCOUNT_SID, AUTH_TOKEN);

        Message message = Message.creator(new PhoneNumber(sms.getTo()), new PhoneNumber(FROM_NUMBER), sms.getMessage())
                .create();
        System.out.println("here is my id:"+message.getSid());// Unique resource ID created to manage this transaction

    }

    public void receive(MultiValueMap<String, String> smscallback) {
    }

}

