package com.way.sms;

import static org.easymock.EasyMock.expect;
import static org.easymock.classextension.EasyMock.createMock;
import static org.easymock.classextension.EasyMock.replay;
import static org.easymock.classextension.EasyMock.verify;
import static org.junit.Assert.assertFalse;

import org.junit.Test;

import android.content.Context;

public class WayServiceTest {

	@Test
	public void does_nothing_if_not_a_way_request() throws Exception {
		final RegularTextMessage waySms = createMock(RegularTextMessage.class);
		assertFalse(new WayService(new SMSService()).reply(waySms));
	}
	
	@Test
	public void replies_to_way_requests_with_current_location() throws Exception {
		final String from = "1234";
		final GeoLocation currentGeoLocation = new GeoLocation("#410, Brigade Millenium", "network");
		
		final Context context = createMock(Context.class);
		final RegularTextMessage waySms = mockWaySms(from);
		final SMSService smsService = mockSmsService(from, currentGeoLocation);
		
		new WayService(smsService).reply(waySms);
		
		verify(smsService);
	}

	private RegularTextMessage mockWaySms(final String from) {
		final RegularTextMessage waySms = createMock(RegularTextMessage.class);
		expect(waySms.from()).andReturn(from);
		expect(waySms.isWayRequest()).andReturn(true);
		expect(waySms.generateReply()).andReturn("My last known location according to network is, #410, Brigade Millenium");
		
		replay(waySms);
		return waySms;
	}

	private SMSService mockSmsService(String to, GeoLocation currentGeoLocation) {
		final SMSService smsService = createMock(SMSService.class);
		expect(smsService.send(to, currentGeoLocation.toString())).andReturn(true);
		
		replay(smsService);
		return smsService;
	}
}