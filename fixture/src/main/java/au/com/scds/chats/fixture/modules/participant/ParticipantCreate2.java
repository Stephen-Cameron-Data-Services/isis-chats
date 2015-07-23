package au.com.scds.chats.fixture.modules.participant;

import java.math.BigDecimal;
import java.util.concurrent.Callable;

import org.apache.isis.applib.fixturescripts.FixtureScript;
import org.apache.isis.applib.services.clock.ClockService;
import org.apache.isis.applib.services.sudo.SudoService;
import org.joda.time.LocalDate;

import au.com.scds.chats.dom.modules.participant.Participant;
import au.com.scds.chats.dom.modules.participant.Participants;

public class ParticipantCreate2 extends FixtureScript {

	public static int numberCanned() {
		return 2; // keep in step with canned() !!
	}

	ParticipantData[] data = new ParticipantData[numberCanned()];

	// region > index (required)
	private int index;

	/**
	 * Which precanned object to create (index=0 to 11); required.
	 */
	public int getIndex() {
		return index;
	}

	public void setIndex(final int index) {
		this.index = index;
	}

	// endregion

	// region > username (optional)
	private String username;

	/**
	 * User to create items for; optional, defaults to current user.
	 */
	public String getUsername() {
		return username;
	}

	public void setUsername(final String username) {
		this.username = username;
	}

	// endregion

	// region > todoItem (output)
	private Participant participant;

	/**
	 * The todoitem created by the script (output).
	 */
	public Participant getParticipant() {
		return participant;
	}

	public void setParticipant(final Participant participant) {
		this.participant = participant;
	}

	// endregion

	@Override
	protected void execute(final ExecutionContext ec) {

		// required
		this.checkParam("index", ec, Integer.class);

		if (getIndex() < 0 || getIndex() >= numberCanned()) {
			throw new IllegalArgumentException(String.format(
					"Index of object to create must be in range [0, %d)",
					numberCanned()));
		}

		// defaults
		this.defaultParam("username", ec, getContainer().getUser().getName());

		// execute
		final ParticipantData pd = canned()[getIndex()];

		participant = sudoService.sudo(username, new Callable<Participant>() {
			@Override
			public Participant call() {
				final Participant participant = wrap(participants)
						.newParticipant(pd.fullName, pd.preferredName,
								pd.mobilePhoneNumber, pd.homePhoneNumber,
								pd.email, LocalDate.parse(pd.dob));
				wrap(participant).updateStreetAddress(pd.sStreet1, pd.sStreet2,
						pd.sSuburb, pd.sPostcode,
						Boolean.valueOf(pd.sStreetIsMail));
				if (!Boolean.valueOf(pd.sStreetIsMail)) {
					wrap(participant).updateMailAddress(pd.mStreet1,
							pd.mStreet2, pd.mSuburb, pd.mPostcode);
				}
				return participant;
			}
		});

		ec.addResult(this, participant);
	}

	private static double random(final double from, final double to) {
		return Math.random() * (to - from) + from;
	}

	protected LocalDate nowPlusDays(final int days) {
		return clockService.now().plusDays(days);
	}

	protected BigDecimal BD(final String str) {
		return new BigDecimal(str);
	}

	// region > injected services
	@javax.inject.Inject
	private Participants participants;

	@javax.inject.Inject
	protected ClockService clockService;

	@javax.inject.Inject
	private SudoService sudoService;

	// region // canned data
	private class ParticipantData {
		public String fullName;
		public String preferredName;
		public String mobilePhoneNumber;
		public String homePhoneNumber;
		public String email;
		public String sStreet1;
		public String sStreet2;
		public String sSuburb;
		public String sPostcode;
		public String sStreetIsMail;
		public String mStreet1;
		public String mStreet2;
		public String mSuburb;
		public String mPostcode;
		public String dob;
	}

	private ParticipantData[] canned() {

		int i = 0;
		data[i] = new ParticipantData();
		data[i].fullName = "Joseph Andrew Bloggs";
		data[i].preferredName = "Joe";
		data[i].mobilePhoneNumber = "0000 000 000";
		data[i].homePhoneNumber = "02 00 000 000";
		data[i].email = "joebloggs@unknown.com";
		data[i].sStreet1 = "Unit 10";
		data[i].sStreet2 = "The Aged Home";
		data[i].sSuburb = "Underwood";
		data[i].sPostcode = "1234";
		data[i].sStreetIsMail = "true";
		data[i].mStreet1 = "";
		data[i].mStreet2 = "";
		data[i].mSuburb = "";
		data[i].mPostcode = "";
		data[i].dob = "1935-10-28";

		data[++i] = new ParticipantData();
		data[i].fullName = "Frederick Arthur Dagg";
		data[i].preferredName = "Fred";
		data[i].mobilePhoneNumber = "0000 000 000";
		data[i].homePhoneNumber = "02 00 000 000";
		data[i].email = "fred.dagg@unknown.com";
		data[i].sStreet1 = "The Mansion";
		data[i].sStreet2 = "123 Hilltop Road";
		data[i].sSuburb = "Nobbs Hill";
		data[i].sPostcode = "1235";
		data[i].sStreetIsMail = "false";
		data[i].mStreet1 = "PO Box 1111";
		data[i].mStreet2 = "Post Office";
		data[i].mSuburb = "Sunnyville";
		data[i].mPostcode = "1236";
		data[i].dob = "1935-10-01";

		return data;
	}
}
