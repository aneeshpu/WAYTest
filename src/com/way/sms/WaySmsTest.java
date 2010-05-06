package com.way.sms;

import static org.easymock.EasyMock.expect;
import static org.easymock.classextension.EasyMock.createMock;
import static org.easymock.classextension.EasyMock.replay;
import static org.easymock.classextension.EasyMock.verify;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import org.easymock.classextension.EasyMock;
import org.junit.Test;

import android.telephony.SmsMessage;

public class WaySmsTest {

	@Test
	public void identifies_a_way_request() throws Exception {

		final SmsMessage smsMessage = mockMessage("Where are you");
		assertThat(new WaySms(smsMessage).isWayRequest(),is(true));
		verify(smsMessage);
	}

	private SmsMessage mockMessage(final String messageBody) {
		final SmsMessage smsMessage = createMock(SmsMessage.class);
		expect(smsMessage.getMessageBody()).andReturn(messageBody);
		replay(smsMessage);
		return smsMessage;
	}

	@Test
	public void identifies_a_where_are_you_request() throws Exception {

		final SmsMessage smsMessage = mockMessage("where are you");
		assertTrue(new WaySms(smsMessage).isWayRequest());
		verify(smsMessage);
	}

	@Test
	public void responds_to_where_r_u() throws Exception {
		final SmsMessage smsMessage = mockMessage("Where r u");
		assertTrue(new WaySms(smsMessage).isWayRequest());
		verify(smsMessage);

	}
	
	@Test
	public void responds_to_where_are_you_with_question_mark() throws Exception {
		final SmsMessage smsMessage = mockMessage("Where r u?");
		assertThat(new WaySms(smsMessage).isWayRequest(),is(true));
		verify(smsMessage);
		
	}

	@Test
	public void responds_to_way() throws Exception {
		final SmsMessage smsMessage = mockMessage("WAY");
		assertThat("Should have responded to 'way'", new WaySms(smsMessage).isWayRequest(), is(true));
		verify(smsMessage);

	}

	@Test
	public void does_not_respond_to_w() throws Exception {
		final SmsMessage smsMessage = mockMessage("W");
		assertThat("Should not have replied to just W",new WaySms(smsMessage).isWayRequest(), is(false) );
		verify(smsMessage);

	}

	@Test
	public void ignores_white_spaces_in_the_request() throws Exception {
		final SmsMessage smsMessage = mockMessage("Where            are  you ");
		assertTrue(new WaySms(smsMessage).isWayRequest());
		verify(smsMessage);

	}

	@Test
	public void question_marks_are_allowed_at_the_end() throws Exception {
		final SmsMessage smsMessage = mockMessage("Where            are  you?");
		assertTrue(new WaySms(smsMessage).isWayRequest());
		verify(smsMessage);

	}

	@Test
	public void more_than_one_question_marks_are_allowed_in_the_end() throws Exception {
		final SmsMessage smsMessage = mockMessage("Where            are  you   ????");
		assertTrue(new WaySms(smsMessage).isWayRequest());
		verify(smsMessage);

	}

	@Test
	public void exclamation_marks_are_allowed_at_the_end() throws Exception {
		final SmsMessage smsMessage = mockMessage("Where            are  you!");
		assertTrue(new WaySms(smsMessage).isWayRequest());
		verify(smsMessage);

	}

	@Test
	public void identifies_a_non_way_request() throws Exception {

		final SmsMessage smsMessage = mockMessage("Where are your brains");
		assertFalse(new WaySms(smsMessage).isWayRequest());
		verify(smsMessage);

	}

	@Test
	public void returns_the_senders_address() throws Exception {
		final SmsMessage smsMessage = EasyMock.createMock(SmsMessage.class);
		final String fromAddress = "9746393678";
		expect(smsMessage.getOriginatingAddress()).andReturn(fromAddress);

		replay(smsMessage);

		final WaySms waySms = new WaySms(smsMessage);
		assertEquals(fromAddress, waySms.from());

		verify(smsMessage);
	}

}