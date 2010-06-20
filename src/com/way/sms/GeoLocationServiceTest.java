package com.way.sms;

import static org.easymock.EasyMock.expect;
import static org.easymock.classextension.EasyMock.createMock;
import static org.easymock.classextension.EasyMock.replay;
import static org.easymock.classextension.EasyMock.verify;
import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Test;

import android.content.Context;
import android.location.Address;
import android.location.Location;
import android.location.LocationManager;


public class GeoLocationServiceTest {

	@Test
	public void returns_current_geo_location() throws Exception {
		
		final double latitude = 1d;
		final double longitude = 1d;
		
		final Location mockLocation = mockLocation(latitude, latitude);
		final LocationManager mockLocationManager = mockLocationManager(mockLocation);
		final Context context = mockContext(mockLocationManager);
		final String expectedAddress = "#410, Brigade Millenium";
		String addressLine1 = "Bengaluru, Karnataka";
		final Address mockAddress = mockAddress(expectedAddress, addressLine1);
		final MyGeoCoder mockMyGeoCoder = mockGeocoder(context,latitude, longitude, mockAddress);
		
		final GeoLocation address = new Locator(mockMyGeoCoder,context).getCurrentGeoLocation();
		
		assertEquals(GeoLocation.class, address.getClass());
		
		verify(context);
		verify(mockLocation);
		verify(mockLocationManager);
		verify(mockMyGeoCoder);
		verify(mockAddress);
	}

	private Address mockAddress(String address, String addressLine1) {
		final Address mockAddress = createMock(Address.class);
		expect(mockAddress.getAddressLine(0)).andReturn(address);
		expect(mockAddress.getAddressLine(1)).andReturn(addressLine1);
		replay(mockAddress);
		return mockAddress;
	}

	private MyGeoCoder mockGeocoder(Context context, double latitude, double longitude, Address address) {
		final MyGeoCoder mockMyGeoCoder = createMock(MyGeoCoder.class);
		final ArrayList<Address> addresses = new ArrayList<Address>();
		addresses.add(address);
		expect(mockMyGeoCoder.getFromLocation(context, latitude, longitude, 1)).andReturn(addresses);
		
		replay(mockMyGeoCoder);
		return mockMyGeoCoder;
	}

	private Context mockContext(LocationManager mockLocationManager) {
		final Context context = createMock(Context.class);
		expect(context.getSystemService(Context.LOCATION_SERVICE)).andReturn(mockLocationManager);
		
		replay(context);
		return context;
	}

	private LocationManager mockLocationManager(Location mockLocation) {
		final LocationManager locationManager = createMock(LocationManager.class);
		final ArrayList<String> allProviders = new ArrayList<String>();
		allProviders.add("gps");
		
		expect(locationManager.getAllProviders()).andReturn(allProviders);
		final Location lastKnownLocation = mockLocation;
		
		expect(locationManager.getLastKnownLocation("gps")).andReturn(lastKnownLocation);
		
		replay(locationManager);
		return locationManager;
	}

	private Location mockLocation(double latitude, double longitude) {
		final Location lastKnownLocation = createMock(Location.class);
		
		expect(lastKnownLocation.getLatitude()).andReturn(latitude);
		expect(lastKnownLocation.getLongitude()).andReturn(longitude);

		replay(lastKnownLocation);
		return lastKnownLocation;
	}
}
