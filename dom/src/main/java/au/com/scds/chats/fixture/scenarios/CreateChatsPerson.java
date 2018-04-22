package au.com.scds.chats.fixture.scenarios;


import javax.inject.Inject;
import org.apache.isis.applib.fixturescripts.FixtureScript;
import org.joda.time.LocalDate;
import au.com.scds.chats.dom.general.ChatsPerson;
import au.com.scds.chats.dom.general.Persons;
import au.com.scds.chats.dom.general.Sex;

public class CreateChatsPerson extends FixtureScript {

	public CreateChatsPerson() {
		withDiscoverability(Discoverability.DISCOVERABLE);
	}

	private ChatsPerson person = null;

	@Override
	protected void execute(ExecutionContext ec) {

		try {
			person = personsMenu.createPerson("Bob", "Brown", new LocalDate(), Sex.MALE);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public ChatsPerson getPerson() {
		return this.person;
	}

	@Inject
	Persons personsMenu;

}
