package com.way.sms.handler;

import static org.easymock.EasyMock.expect;
import static org.easymock.classextension.EasyMock.replay;
import static org.easymock.classextension.EasyMock.verify;

import org.easymock.EasyMock;
import org.junit.Test;

import com.way.sms.SMSService;
import com.way.sms.TextMessage;


public class TextMessageHandlerTest {

	@Test
	public void handles_a_regular_text_message() throws Exception {
		
		final String sender = "9885674423";
		final String reply = "According to network my last known location is, #410 Brigade Millenium";
		
		final TextMessage regularTextMessage = regularTextMessageMock(sender, reply, true);
		final SMSService smsSender = smsSenderMock(sender, reply);
		
		final WayHandler regularTextMessageHandler = new TextMessageHandler(regularTextMessage, smsSender, new DefaultTextMessageHandler());
		
		regularTextMessageHandler.handle();
		
		verify(smsSender);
		verify(regularTextMessage);
		
	}
	
	@Test
	public void delegates_to_next_handler_if_cannot_be_handled() throws Exception {
		
		final String sender = "9885674423";
		final String reply = "According to network my last known location is, #410 Brigade Millenium";
		
		final TextMessage faceBookTextMessage = textMessageMockThatDoesNotExpectAFromCall(false);
		final SMSService smsSender = smsSenderThatDoesNotExpectToBeCalled(sender, reply);
		
		final WayHandler nextTextMessageHandler = nextTextMessageHandler();
		
		final WayHandler regularTextMessageHandler = new TextMessageHandler(faceBookTextMessage, smsSender, nextTextMessageHandler);
		regularTextMessageHandler.handle();
		
		verify(smsSender);
		verify(faceBookTextMessage);
		verify(nextTextMessageHandler);

	}
	

	private TextMessage textMessageMockThatDoesNotExpectAFromCall(boolean isWayRequest) {
		final TextMessage regularTextMessage = EasyMock.createMock(TextMessage.class);

		expect(regularTextMessage.isWayRequest()).andReturn(isWayRequest);
		
		replay(regularTextMessage);
		return regularTextMessage;
	}

	private SMSService smsSenderThatDoesNotExpectToBeCalled(String sender, String reply) {
		final SMSService smsSender = org.easymock.classextension.EasyMock.createMock(SMSService.class);
		replay(smsSender);
		return smsSender;

	}

	private WayHandler nextTextMessageHandler() {
		final WayHandler nextTextMessageHandler = EasyMock.createMock(WayHandler.class);
		nextTextMessageHandler.handle();
		
		replay(nextTextMessageHandler);
		return nextTextMessageHandler;
	}

	private SMSService smsSenderMock(String to, String message) {
		final SMSService smsSender = org.easymock.classextension.EasyMock.createMock(SMSService.class);
		expect(smsSender.send(to, message)).andReturn(true);
		replay(smsSender);
		return smsSender;
	}

	private TextMessage regularTextMessageMock(String from, String reply, boolean isWayRequest) {
		final TextMessage regularTextMessage = EasyMock.createMock(TextMessage.class);

		expect(regularTextMessage.from()).andReturn(from);
		expect(regularTextMessage.isWayRequest()).andReturn(isWayRequest);
		expect(regularTextMessage.reply()).andReturn(reply);
		
		replay(regularTextMessage);
		return regularTextMessage;
	}
}