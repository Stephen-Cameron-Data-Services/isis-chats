package au.com.scds.chats.integtests.tests.modules.volunteer;

import static org.assertj.core.api.Assertions.assertThat;

import javax.inject.Inject;

import org.apache.isis.applib.fixturescripts.FixtureScripts;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.junit.Test;

import au.com.scds.chats.dom.call.ScheduledCall;
import au.com.scds.chats.dom.general.Sex;
import au.com.scds.chats.dom.participant.Participant;
import au.com.scds.chats.dom.participant.Participants;
import au.com.scds.chats.dom.volunteer.Volunteer;
import au.com.scds.chats.dom.volunteer.Volunteers;
import au.com.scds.chats.integtests.tests.DomainAppIntegTest;

public class VolunteersIntegTest extends DomainAppIntegTest {

	@Inject
	FixtureScripts fixtureScripts;
	@Inject
	Volunteers volunteers;
	@Inject
	Participants participants;

	@Test
	public void happyCase() throws Exception {

		// given
		DateTime startDateTime = new DateTime();

		// when
		Volunteer v = wrap(volunteers).create("Joe", "Volunteer", new LocalDate(), Sex.MALE);
		Participant p = wrap(participants).create("Mary", "Participant", new LocalDate(), Sex.FEMALE);

		// then
		// assertThat(v.getPerson().getFullname()).isEqualTo("Joe Volunteer");
		// assertThat(p.getPerson().getFullname()).isEqualTo("Mary
		// Participant");

		wrap(v).addScheduledCall(p, new DateTime());
		assertThat(v.getScheduled().size()).isEqualTo(1);
		assertThat(v.getScheduled().first().getTotalCalls()).isEqualTo(1);
		assertThat(v.getScheduled().first().getCompletedCalls()).isEqualTo(0);
	}

}
