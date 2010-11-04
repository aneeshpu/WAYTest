package com.way.xmpp;

import org.easymock.classextension.EasyMock;
import org.jivesoftware.smack.packet.Message;
import org.junit.Test;

import com.way.sms.WayRequest;

import static org.easymock.classextension.EasyMock.*;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

public class XMPPChatMessageTest {

	@Test
	public void identifies_a_way_message() throws Exception {

		Message message = xmppChatMessageContainingTextWhereAreYou();

		XMPPChatMessage xmppChatMessage = new XMPPChatMessage(message, mockWayRequest());
		assertThat("Where are you was not identified as a WAY request!", xmppChatMessage.isWayRequest(), is(true));

		verify(message);

	}
	
	@Test
	public void identifies_a_non_way_request() throws Exception {
		Message message = xmppChatMessage_Not_ContainingTextWhereAreYou();

		XMPPChatMessage xmppChatMessage = new XMPPChatMessage(message, mockWayRequestThatRepliesInNegative());
		assertThat("How are you was identified as a WAY request!", xmppChatMessage.isWayRequest(), is(false));

		verify(message);
		
	}

	private WayRequest mockWayRequestThatRepliesInNegative() {
		WayRequest wayRequest = EasyMock.createMock(WayRequest.class);
		expect(wayRequest.isWayRequest(isA(String.class))).andReturn(false);

		replay(wayRequest);
		return wayRequest;
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
