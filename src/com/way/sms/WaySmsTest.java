package com.way.sms;

import static org.easymock.EasyMock.expect;
import static org.easymock.classextension.EasyMock.createMock;
import static org.easymock.classextension.EasyMock.replay;
import static org.easymock.classextension.EasyMock.verify;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.easymock.classextension.EasyMock;
import org.junit.Test;

import android.telephony.SmsMessage;


public class WaySmsTest {

	@Test
	public void identifies_a_way_request() throws Exception {
		
		final SmsMessage smsMessage = createMock(SmsMessage.class);
		expect(smsMessage.getMessageBody()).andReturn("Where are you");
		replay(smsMessage);
		final WaySms waySms = new WaySms(smsMessage);
		
		assertTrue(waySms.isWayRequest());
		verify(smsMessage);
	}
	
	@Test
	public void identifies_a_non_way_request() throws Exception {

		final SmsMessage smsMessage = EasyMock.createMock(SmsMessage.class);
		expect(smsMessage.getMessageBody()).andReturn("Where is your brain");
		replay(smsMessage);
		final WaySms waySms = new WaySms(smsMessage);
		
		assertFalse(waySms.isWayRequest());
		verify(smsMessage);

	}
	
	@Test
	public void returns_the_senders_address() throws Exception {
		final SmsMessage smsMessage = EasyMock.createMock(SmsMessage.class);
		final String fromAddress = "9746393678";
		expect(smsMessage.getOriginatingAddress()).andReturn(fromAddress);
		
		replay(smsMessage);
		
		final WaySms waySms = new WaySms(smsMessage);
		assertEquals(fromAddress,waySms.from());
		
		verify(smsMessage);
	}
	
	
}