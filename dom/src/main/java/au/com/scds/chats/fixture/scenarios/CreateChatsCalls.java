package au.com.scds.chats.fixture.scenarios;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.JAXBIntrospector;
import javax.xml.bind.Unmarshaller;

import org.apache.isis.applib.fixturescripts.FixtureScript;
import org.apache.isis.applib.fixturescripts.FixtureScript.Discoverability;
import org.apache.isis.applib.fixturescripts.FixtureScript.ExecutionContext;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;

import au.com.scds.chats.dom.activity.ParticipantsMenu;
import au.com.scds.chats.dom.call.CallsMenu;
import au.com.scds.chats.dom.call.ChatsScheduledCall;
import au.com.scds.chats.dom.general.Sex;
import au.com.scds.chats.dom.volunteer.VolunteersMenu;
import au.com.scds.chats.fixture.generated.Calls;
import au.com.scds.chats.fixture.generated.ChatsCall;
import au.com.scds.chats.fixture.generated.ChatsPerson;
import au.com.scds.chats.fixture.generated.ObjectFactory;
import lombok.Getter;

public class CreateChatsCalls extends FixtureScript {

	public CreateChatsCalls() {
		withDiscoverability(Discoverability.DISCOVERABLE);
	}

	@Getter
	private List<ChatsScheduledCall> calls = new ArrayList<>();

	@Override
	protected void execute(ExecutionContext ec) {

		try {
			// import object graph from XML
			InputStream is = this.getClass().getResourceAsStream("/au/com/scds/chats/fixture/calls.xml");
			JAXBContext jaxbContext = JAXBContext.newInstance(ObjectFactory.class);
			Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
			jaxbUnmarshaller.setEventHandler(new javax.xml.bind.helpers.DefaultValidationEventHandler());
			Calls _calls = (Calls) JAXBIntrospector.getValue(jaxbUnmarshaller.unmarshal(is));
			au.com.scds.chats.dom.activity.ChatsParticipant participant = null;
			au.com.scds.chats.dom.volunteer.Volunteer volunteer = null;
			for (ChatsCall _call : _calls.getCall()) {
				ChatsPerson _person1 = _call.getParticipant().getPerson();
				participant = participantMenu.create(_person1.getFirstname(), _person1.getSurname(),
						LocalDate.fromDateFields(_person1.getDateOfBirth()), Sex.valueOf(_person1.getSex()));
				ChatsPerson _person2 = _call.getVolunteer().getPerson();
				volunteer = volunteerMenu.create(_person2.getFirstname(), _person2.getSurname(),
						LocalDate.fromDateFields(_person2.getDateOfBirth()), Sex.valueOf(_person2.getSex()));
				ChatsScheduledCall call = callMenu.createScheduledCall(volunteer, participant, new DateTime(_call.getStart()));
				_call.setEnd(_call.getEnd());
				calls.add(call);
			}
		} catch (JAXBException e) {
			e.printStackTrace();
		}
	}

	@Inject
	ParticipantsMenu participantMenu;

	@Inject
	VolunteersMenu volunteerMenu;
	
	@Inject
	CallsMenu callMenu;

}
