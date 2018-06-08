package au.com.scds.chats.integtest;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import javax.inject.Inject;

import org.apache.isis.applib.fixturescripts.FixtureScripts;
import org.apache.isis.applib.services.xactn.TransactionService;
import org.junit.Before;
import org.junit.Test;

import au.com.scds.chats.dom.activity.ChatsParticipant;
import au.com.scds.chats.dom.call.ChatsScheduledCall;
import au.com.scds.chats.dom.volunteer.Volunteer;
import au.com.scds.chats.dom.volunteer.VolunteersMenu;
import au.com.scds.chats.fixture.scenarios.CreateChatsCalls;

public class Calls_IntegTest extends IntegTestAbstract {

	@Inject
	FixtureScripts fixtureScripts;
	@Inject
	TransactionService transactionService;
	@Inject
	VolunteersMenu volunteers;

	List<ChatsScheduledCall> calls = null;

	@Before
	public void setUp() throws Exception {
		// given
		CreateChatsCalls fs = new CreateChatsCalls();
		fixtureScripts.runFixtureScript(fs, null);
		transactionService.nextTransaction();
		calls = fs.getCalls();
		assertThat(calls).isNotNull();
	}

	public static class Calls extends Calls_IntegTest {

		@Test
		public void accessible() throws Exception {
			// then
			assertThat(calls.size()).isEqualTo(3);
			assertThat(calls.get(0).getVolunteer()).isNotNull();
			assertThat(calls.get(0).getParticipant()).isNotNull();
			assertThat(calls.get(1).getVolunteer()).isNotNull();
			assertThat(calls.get(1).getParticipant()).isNotNull();
			assertThat(calls.get(2).getVolunteer()).isNotNull();
			assertThat(calls.get(2).getParticipant()).isNotNull();
		}
	}
}
