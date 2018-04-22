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

import au.com.scds.chats.dom.activity.ActivityMenu;
import au.com.scds.chats.dom.activity.ParticipantMenu;
import au.com.scds.chats.dom.general.Sex;
import au.com.scds.chats.fixture.generated.ChatsAttendance;
import au.com.scds.chats.fixture.generated.ChatsParentedActivity;
import au.com.scds.chats.fixture.generated.ChatsParticipant;
import au.com.scds.chats.fixture.generated.ChatsParticipation;
import au.com.scds.chats.fixture.generated.ChatsPerson;
import au.com.scds.chats.fixture.generated.ChatsRecurringActivity;
import au.com.scds.chats.fixture.generated.ObjectFactory;
import au.com.scds.chats.fixture.generated.RecurringActivities;

public class CreateChatsRecurringActivities extends FixtureScript {

	public CreateChatsRecurringActivities() {
		withDiscoverability(Discoverability.DISCOVERABLE);
	}

	private List<au.com.scds.chats.dom.activity.ChatsRecurringActivity> activities = new ArrayList<>();

	@Override
	protected void execute(ExecutionContext ec) {

		try {
			// import object graph from XML
			InputStream is = this.getClass().getResourceAsStream("/au/com/scds/chats/fixture/recurring_activities.xml");
			JAXBContext jaxbContext = JAXBContext.newInstance(ObjectFactory.class);
			Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
			jaxbUnmarshaller.setEventHandler(new javax.xml.bind.helpers.DefaultValidationEventHandler());
			RecurringActivities _activities = (RecurringActivities) JAXBIntrospector
					.getValue(jaxbUnmarshaller.unmarshal(is));
			for (ChatsRecurringActivity _parent : _activities.getRecurringActivity()) {
				au.com.scds.chats.dom.activity.ChatsRecurringActivity parent = activityMenu
						.createChatsRecurringActivity(_parent.getName(), _parent.getCodeName(),
								new DateTime(_parent.getStart()));
				au.com.scds.chats.dom.activity.ChatsParticipant participant = null;
				for (ChatsParticipation _participation : _parent.getParticipation()) {
					ChatsPerson _person = _participation.getParticipant().getPerson();
					participant = participantMenu.create(_person.getFirstname(), _person.getSurname(),
							LocalDate.fromDateFields(_person.getDateOfBirth()), Sex.valueOf(_person.getSex()));
					parent.addParticipation(participant);
				}
				for (ChatsParticipant _participant : _parent.getWaitListed()) {
					ChatsPerson _person = _participant.getPerson();
					participant = participantMenu.create(_person.getFirstname(), _person.getSurname(),
							LocalDate.fromDateFields(_person.getDateOfBirth()), Sex.valueOf(_person.getSex()));
					parent.addWaitListed(participant);
				}
				// au.com.scds.chats.base.impl.EventFacilitator facilitator =
				// null;
				// for(EventFacilitator _facilitator :
				// _parent.getEventFacilitator()){
				// facilitator =
				// eventMenu.createEventFacilitator(_facilitator.getPerson().getFullname());
				// parent.addFacilitator(facilitator);
				// }
				// same for child events
				for (ChatsParentedActivity _child : _parent.getChildEvent()) {
					parent.addChildEvent(new DateTime(_child.getStart()));
					// find the child just created
					au.com.scds.chats.dom.activity.ChatsParentedActivity child = null;
					for (au.com.scds.chats.dom.activity.ChatsParentedActivity e : parent.getChildActivities()) {
						if (e.getStart().equals(new DateTime(_child.getStart()))) {
							child = e;
							break;
						}
					}
					if (child != null) {
						for (ChatsParticipation _participation : _child.getParticipation()) {
							ChatsPerson _person = _participation.getParticipant().getPerson();
							participant = participantMenu.create(_person.getFirstname(), _person.getSurname(),
									LocalDate.fromDateFields(_person.getDateOfBirth()), Sex.valueOf(_person.getSex()));
							child.addParticipation(participant);
						}
						for (ChatsParticipant _participant : _parent.getWaitListed()) {
							ChatsPerson _person = _participant.getPerson();
							participant = participantMenu.create(_person.getFirstname(), _person.getSurname(),
									LocalDate.fromDateFields(_person.getDateOfBirth()), Sex.valueOf(_person.getSex()));
							child.addWaitListed(participant);
						}
						for (ChatsAttendance _attendance : _child.getAttendance()) {
							ChatsPerson _person = _attendance.getParticipant().getPerson();
							participant = participantMenu.create(_person.getFirstname(), _person.getSurname(),
									LocalDate.fromDateFields(_person.getDateOfBirth()), Sex.valueOf(_person.getSex()));
							child.addAttendance(participant);
						}
					}
				}
				activities.add(parent);
			}
		} catch (JAXBException e) {
			e.printStackTrace();
		}
	}

	public List<au.com.scds.chats.dom.activity.ChatsRecurringActivity> getActivities() {
		return this.activities;
	}

	@Inject
	ActivityMenu activityMenu;

	@Inject
	ParticipantMenu participantMenu;
}
