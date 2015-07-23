package au.com.scds.chats.fixture.modules.participant;

import java.math.BigDecimal;
import java.util.concurrent.Callable;

import org.apache.isis.applib.fixturescripts.FixtureScript;
import org.apache.isis.applib.services.clock.ClockService;
import org.apache.isis.applib.services.sudo.SudoService;
import org.joda.time.LocalDate;

import au.com.scds.chats.dom.modules.participant.Participant;
import au.com.scds.chats.dom.modules.participant.Participants;

public class ParticipantCreate3 extends FixtureScript {

	private ParticipantData data;
	
	public ParticipantCreate3(ParticipantData data){
		this.data = data;
	}

	// region > Participant (output)
	private Participant participant;

	/**
	 * The Participant created by the script (output).
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

		participant = wrap(participants).newParticipant(data.fullName,
				data.preferredName, data.mobilePhoneNumber, data.homePhoneNumber,
				data.email, LocalDate.parse(data.dob));
		wrap(participant).updateStreetAddress(data.sStreet1, data.sStreet2,
				data.sSuburb, data.sPostcode, Boolean.valueOf(data.sStreetIsMail));
		if (!Boolean.valueOf(data.sStreetIsMail)) {
			wrap(participant).updateMailAddress(data.mStreet1, data.mStreet2,
					data.mSuburb, data.mPostcode);
		}
		ec.addResult(this, participant);
	}

	// region > injected services
	@javax.inject.Inject
	private Participants participants;
}
