package com.way.sms;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;



public class GeoLocationTest {

	@Test
	public void produces_a_string_representation_of_the_geo_location() throws Exception {
		
		final String address = "#410, Brigade Millenium, J P Nagar, Bangalore";
		final String provider = "gps";
		final GeoLocation geoLocation = new GeoLocation(address, provider);
		assertThat(geoLocation.toString(),is(equalTo(String.format("My last known location according to %s is, %s",provider, address))));
	}
	
	@Test
	public void null_geo_location_produces_a_polite_and_funny_reply() throws Exception {
		final GeoLocation nullGeoLocation = GeoLocation.Null;
		assertThat(nullGeoLocation.toString(), is(equalTo("Doh! I couldn't figure out where I am. Could you check back a little later please")));
	}
	
}
