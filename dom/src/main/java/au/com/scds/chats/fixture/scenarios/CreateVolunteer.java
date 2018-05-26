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
import org.joda.time.DateTime;
import org.joda.time.LocalDate;

import au.com.scds.chats.dom.activity.ParticipantsMenu;
import au.com.scds.chats.dom.general.Sex;
import au.com.scds.chats.dom.volunteer.VolunteersMenu;
import au.com.scds.chats.fixture.generated.Volunteer;
import au.com.scds.chats.fixture.generated.Volunteers;
import au.com.scds.chats.fixture.generated.ChatsParticipant;
import au.com.scds.chats.fixture.generated.ChatsPerson;
import au.com.scds.chats.fixture.generated.ObjectFactory;

public class CreateVolunteer  extends FixtureScript {

	public CreateVolunteer() {
		withDiscoverability(Discoverability.DISCOVERABLE);
	}

	private List<au.com.scds.chats.dom.volunteer.Volunteer> volunteers = new ArrayList<>();

	@Override
	protected void execute(ExecutionContext ec) {

		try {
			// import object graph from XML
			InputStream is = this.getClass().getResourceAsStream("/au/com/scds/chats/fixture/volunteers.xml");
			JAXBContext jaxbContext = JAXBContext.newInstance(ObjectFactory.class);
			Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
			jaxbUnmarshaller.setEventHandler(new javax.xml.bind.helpers.DefaultValidationEventHandler());
			Volunteers _volunteers = (Volunteers) JAXBIntrospector.getValue(jaxbUnmarshaller.unmarshal(is));
			for (Volunteer _volunteer : _volunteers.getVolunteer()) {
				ChatsPerson _person = _volunteer.getPerson();
				au.com.scds.chats.dom.volunteer.Volunteer volunteer = volunteerMenu.create(
						_person.getFirstname(), _person.getSurname(),
						LocalDate.fromDateFields(_person.getDateOfBirth()), Sex.valueOf(_person.getSex()));
				volunteers.add(volunteer);
				for(Volunteer.CallAllocation _allocation : _volunteer.getCallAllocation()){
					_person = _allocation.getParticipant().getPerson();
					au.com.scds.chats.dom.activity.ChatsParticipant participant =  participantMenu.create(
							_person.getFirstname(), _person.getSurname(),
							LocalDate.fromDateFields(_person.getDateOfBirth()), Sex.valueOf(_person.getSex()));
					volunteer.addAllocatedCallParticipant(participant, _allocation.getIndicativeCallTime().toString());
				}
				for(Volunteer.Call _call : _volunteer.getCall()){
					_person = _call.getParticipant().getPerson();
					au.com.scds.chats.dom.activity.ChatsParticipant participant =  participantMenu.create(
							_person.getFirstname(), _person.getSurname(),
							LocalDate.fromDateFields(_person.getDateOfBirth()), Sex.valueOf(_person.getSex()));
					au.com.scds.chats.dom.call.ChatsScheduledCall call = volunteer.createScheduledCall(participant, new DateTime(_call.getStart()) );
					call.updateEndDateTime(new DateTime(_call.getEnd()));
					call.setSummaryNotes(_call.getNotes());
				}
			}
		} catch (JAXBException e) {
			e.printStackTrace();
		}
	}

	public List<au.com.scds.chats.dom.volunteer.Volunteer> getVolunteers() {
		return this.volunteers;
	}

	@Inject
	VolunteersMenu volunteerMenu;
	
	@Inject
	ParticipantsMenu participantMenu;

}
