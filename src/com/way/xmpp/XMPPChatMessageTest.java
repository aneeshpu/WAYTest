package com.way.xmpp;

import org.easymock.classextension.EasyMock;
import org.jivesoftware.smack.packet.Message;
import org.junit.Test;

import com.way.sms.GeoLocation;
import com.way.sms.Locator;
import com.way.sms.WayRequest;

import static org.easymock.EasyMock.expect;
import static org.easymock.classextension.EasyMock.*;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

public class XMPPChatMessageTest {

	@Test
	public void identifies_a_way_message() throws Exception {

		Message message = xmppChatMessageContainingTextWhereAreYou();

		WayRequest mockWayRequest = mockWayRequest();
		XMPPChatMessage xmppChatMessage = new XMPPChatMessage(message, mockWayRequest, locatorMock());
		assertThat("Where are you was not identified as a WAY request!", xmppChatMessage.isWayRequest(), is(true));

		verify(message);
		verify(mockWayRequest);

	}
	
	@Test
	public void identifies_a_non_way_request() throws Exception {
		Message message = xmppChatMessage_Not_ContainingTextWhereAreYou();

		WayRequest mockWayRequestThatRepliesInNegative = mockWayRequestThatRepliesInNegative();
		XMPPChatMessage xmppChatMessage = new XMPPChatMessage(message, mockWayRequestThatRepliesInNegative, locatorMock());
		assertThat("How are you was identified as a WAY request!", xmppChatMessage.isWayRequest(), is(false));

		verify(message);
		verify(mockWayRequestThatRepliesInNegative);
		
	}

	private WayRequest mockWayRequestThatRepliesInNegative() {
		WayRequest wayRequest = EasyMock.createMock(WayRequest.class);
		expect(wayRequest.isWayRequest(isA(String.class))).andReturn(false);

		replay(wayRequest);
		return wayRequest;
	}
	
	@Test
	public void generates_reply_for_way() throws Exception {
		
		XMPPChatMessage xmppChatMessage = new XMPPChatMessage(null, null, locatorMock());
		assertThat(xmppChatMessage.reply(), is(equalTo("My last known location according to network is, #410, Brigade Millenium")));
	}
	
	@Test
	public void extracts_sender_information_from_message() throws Exception {
		Message message = xmppChatMessageWithFrom();

		XMPPChatMessage xmppChatMessage = new XMPPChatMessage(message, null, null);
		assertThat("should have found gmail@rajnikant.com as sender", xmppChatMessage.from(), is(equalTo("gmail@rajnikant.com")));

		verify(message);

	}
	
	private Message xmppChatMessageWithFrom() {
		Message message = createMock(Message.class);
		expect(message.getFrom()).andReturn("gmail@rajnikant.com");
		replay(message);
		return message;

	}

	private Locator locatorMock() {
		final Locator locatorMock = EasyMock.createMock(Locator.class);
		expect(locatorMock.getCurrentGeoLocation()).andReturn(new GeoLocation("#410, Brigade Millenium", "network"));
		replay(locatorMock);
		return locatorMock;
	}


	private Message xmppChatMessage_Not_ContainingTextWhereAreYou() {
		Message message = createMock(Message.class);
		expect(message.getBody()).andReturn("How are you");
		replay(message);
		return message;
	}

	private Message xmppChatMessageContainingTextWhereAreYou() {
		Message message = createMock(Message.class);
		expect(message.getBody()).andReturn("Where are you");
		replay(message);
		return message;
	}

	private WayRequest mockWayRequest() {
		WayRequest wayRequest = EasyMock.createMock(WayRequest.class);
		expect(wayRequest.isWayRequest(isA(String.class))).andReturn(true);

		replay(wayRequest);
		return wayRequest;
	}
}
