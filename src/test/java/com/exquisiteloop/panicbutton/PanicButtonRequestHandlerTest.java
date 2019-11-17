package com.exquisiteloop.panicbutton;

import org.junit.Before;
import org.junit.Test;

import com.amazonaws.services.lambda.runtime.events.IoTButtonEvent;

public class PanicButtonRequestHandlerTest {
	
	private PanicButtonRequestHandler panicButton;
	
	@Before
	public void setup() {
		panicButton = new PanicButtonRequestHandler();
	}

	@Test
	public void testClick() throws Exception {
		IoTButtonEvent event = new IoTButtonEvent();
		panicButton.handleRequest(event, null);
	}

}
