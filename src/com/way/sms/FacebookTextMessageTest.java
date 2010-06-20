package com.way.sms;

import static org.junit.Assert.*;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.easymock.classextension.EasyMock;
import org.junit.Test;

import android.telephony.SmsMessage;
import static org.hamcrest.CoreMatchers.*;

import static org.easymock.classextension.EasyMock.*;

public class FacebookTextMessageTest {

	@Test
	public void identifies_a_facebook_way_sms() throws Exception {

		final SmsMessage smsMessage = wayRequest();
		final FacebookTextMessage facebookTextMessage = new FacebookTextMessage(smsMessage, new WayRequest(), EasyMock.createMock(Locator.class));

		assertThat(facebookTextMessage.isWayRequest(), is(true));

		verify(smsMessage);
	}

	@Test
	public void identifies_a_non_way_request() throws Exception {
		final SmsMessage smsMessage = nonWayRequest();
		final FacebookTextMessage facebookTextMessage = new FacebookTextMessage(smsMessage, new WayRequest(),EasyMock.createMock(Locator.class));

		assertThat(facebookTextMessage.isWayRequest(), is(false));

		verify(smsMessage);

	}

	@Test
	public void generates_a_reply_that_will_be_posted_to_the_senders_wall() throws Exception {
		final SmsMessage smsMessage = wayRequest();
		final Locator locatorMock = locatorMock();

		final FacebookTextMessage facebookTextMessage = new FacebookTextMessage(smsMessage, new WayRequest(), locatorMock);

		assertThat(facebookTextMessage.generateReply(), is(equalTo("Wall Aneesh Pu My last known location according to network is, #410, Brigade Millenium")));
		verify(locatorMock);
	}

	private Locator locatorMock() {
		final Locator locatorMock = EasyMock.createMock(Locator.class);
		expect(locatorMock.getCurrentGeoLocation()).andReturn(new GeoLocation("#410, Brigade Millenium", "network"));
		replay(locatorMock);
		return locatorMock;
	}

	private SmsMessage nonWayRequest() {
		final SmsMessage smsMessage = EasyMock.createMock(SmsMessage.class);

		expect(smsMessage.getMessageBody()).andReturn("Aneesh Pu Wrote on your Facebook Wall:\nHello world\nReply to post on Aneesh's wall.\nReply \"sub\" to subscribe to Aneesh's status");
		replay(smsMessage);

		return smsMessage;
	}

	private SmsMessage wayRequest() {
		final SmsMessage smsMessage = EasyMock.createMock(SmsMessage.class);

		expect(smsMessage.getMessageBody()).andReturn("Aneesh Pu Wrote on your Facebook Wall:\nWhere are you\nReply to post on Aneesh's wall.\nReply \"sub\" to subscribe to Aneesh's status");
		replay(smsMessage);

		return smsMessage;
	}

	@Test
	public void reg_ex_matching_facebook_messages() throws Exception {

		final Pattern pattern = Pattern.compile("([a-zA-Z ]*) Wrote on your Facebook Wall:\n([a-zA-Z ]*)\nReply to post on ([a-zA-Z]*)'s wall.\nReply \"sub\" to subscribe to ([a-zA-Z]*)'s status");
		final Matcher matcher = pattern.matcher("Aneesh Pu Wrote on your Facebook Wall:\nWhere are you\nReply to post on Aneesh's wall.\nReply \"sub\" to subscribe to Aneesh's status");
		assertThat(matcher.matches(), is(true));

		assertThat(matcher.group(1), is(equalTo("Aneesh Pu")));
		assertThat(matcher.group(2), is(equalTo("Where are you")));
		assertThat(matcher.group(3), is(equalTo("Aneesh")));
		assertThat(matcher.group(4), is(equalTo("Aneesh")));
	}
}
