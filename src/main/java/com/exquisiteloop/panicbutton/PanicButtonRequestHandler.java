package com.exquisiteloop.panicbutton;

import java.util.Arrays;
import java.util.List;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.IoTButtonEvent;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;

public class PanicButtonRequestHandler implements RequestHandler<IoTButtonEvent,Void> {

	private static final String TWILIO_SID_VAR = "TWILIO_SID";
	private static final String TWILIO_AUTH_TOKEN_VAR = "TWILIO_AUTH_TOKEN";

	private static final String PANIC_BUTTON_FROM_VAR = "PANIC_BUTTON_FROM";
	private static final String PANIC_BUTTON_TO_VAR = "PANIC_BUTTON_TO";
	private static final String PANIC_BUTTON_MSG_VAR = "PANIC_BUTTON_MSG";

    public Void handleRequest(IoTButtonEvent event, Context context) {
    	
    	String twilioSid = System.getenv(TWILIO_SID_VAR);
    	String authToken = System.getenv(TWILIO_AUTH_TOKEN_VAR);
    	
    	if(null==twilioSid || null==authToken)
    		throw new RuntimeException(String.format("No Twilio credentials found, set environment variables for %s and %s.", TWILIO_SID_VAR, TWILIO_AUTH_TOKEN_VAR));

    	String fromPhone = System.getenv(PANIC_BUTTON_FROM_VAR);
    	String toPhone = System.getenv(PANIC_BUTTON_TO_VAR);
    	String msg = System.getenv(PANIC_BUTTON_MSG_VAR);
    	
    	if(null==fromPhone || null==toPhone || null==msg)
    		throw new RuntimeException(String.format("No phone numbers found, set environment variables for %s, %s, and %s.", PANIC_BUTTON_FROM_VAR, PANIC_BUTTON_TO_VAR, PANIC_BUTTON_MSG_VAR));

        Twilio.init(twilioSid, authToken);

    	PhoneNumber from = new PhoneNumber(fromPhone);
    	List<String> toPhones = Arrays.asList(toPhone.split("\\s*,\\s*"));
    	
    	for(String tp : toPhones) {
	    	PhoneNumber to = new PhoneNumber(tp);
	    	
	    	System.out.println(String.format("Sending message '%s' from %s to %s", msg, fromPhone, tp));
	    	
	        Message message = Message.creator(to, from, msg).create();
	        
	        Integer errorCode = message.getErrorCode();
	        if(null!=errorCode)
	        	System.out.println(String.format("Got Twilio error %d, %s.", errorCode, message.getErrorMessage()));
    	}
    	
        return null;

    }
}
