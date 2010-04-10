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
		final WaySms waySms = createMock(WaySms.class);
		assertFalse(new WayService(new SMSService(), new GeoLocationService(new MyGeoCoder())).reply(waySms, null));
	}
	
	@Test
	public void replies_to_way_requests_with_current_location() throws Exception {
		final String from = "1234";
		final GeoLocation currentGeoLocation = new GeoLocation("#410, Brigade Millenium", null);
		
		final Context context = createMock(Context.class);
		final WaySms waySms = mockWaySms(from);
		final SMSService smsService = mockSmsService(from, currentGeoLocation);
		final GeoLocationService geoLocationService = mockGeoLocationService(context, currentGeoLocation);
		
		new WayService(smsService, geoLocationService).reply(waySms, context);
		
		verify(smsService);
		verify(geoLocationService);
	}

	private WaySms mockWaySms(final String from) {
		final WaySms waySms = createMock(WaySms.class);
		expect(waySms.from()).andReturn(from);
		expect(waySms.isWayRequest()).andReturn(true);
		
		replay(waySms);
		return waySms;
	}

	private GeoLocationService mockGeoLocationService(Context context, GeoLocation currentGeoLocation) {
		final GeoLocationService geoLocationService = createMock(GeoLocationService.class);
		expect(geoLocationService.getCurrentGeoLocation(context)).andReturn(currentGeoLocation);
		
		replay(geoLocationService);
		return geoLocationService;
	}

	private SMSService mockSmsService(String to, GeoLocation currentGeoLocation) {
		final SMSService smsService = createMock(SMSService.class);
		expect(smsService.send(to, currentGeoLocation.toString())).andReturn(true);
		
		replay(smsService);
		return smsService;
	}
}