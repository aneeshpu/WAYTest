package com.way.sms;

import static org.junit.Assert.*;

import org.junit.Test;


public class GeoLocationTest {

	@Test
	public void produces_a_string_representation_of_the_geo_location() throws Exception {
		
		final String address = "#410, Brigade Millenium, J P Nagar, Bangalore";
		final String provider = "gps";
		final GeoLocation geoLocation = new GeoLocation(address, provider);
		System.out.println(geoLocation.toString());
		assertEquals(String.format("%s, \n--provider:%s",address, provider), geoLocation.toString());
	}
	
}
