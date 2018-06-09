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
import au.com.scds.chats.dom.activity.ParticipantsMenu;
import au.com.scds.chats.dom.general.Sex;
import au.com.scds.chats.dom.general.names.Regions;
import au.com.scds.chats.dom.volunteer.VolunteersForChatsActivityEventMixins.ActivityEvent_addVolunteeredTime;
import au.com.scds.chats.dom.volunteer.VolunteersMenu;
import au.com.scds.chats.fixture.generated.Activities;
import au.com.scds.chats.fixture.generated.ChatsActivity;
import au.com.scds.chats.fixture.generated.ChatsAttendance;
import au.com.scds.chats.fixture.generated.ChatsCall;
import au.com.scds.chats.fixture.generated.ChatsParticipant;
import au.com.scds.chats.fixture.generated.ChatsParticipation;
import au.com.scds.chats.fixture.generated.ChatsPerson;
import au.com.scds.chats.fixture.generated.ObjectFactory;
import au.com.scds.chats.fixture.generated.VolunteeredTime;
import lombok.Getter;

public class CreateDexReportActivities extends FixtureScript {

	public CreateDexReportActivities() {
		withDiscoverability(Discoverability.DISCOVERABLE);
	}

	@Getter
	private List<au.com.scds.chats.dom.activity.ChatsActivity> activities = new ArrayList<>();

	@Override
	protected void execute(ExecutionContext ec) {

		try {
			// import object graph from XML
			InputStream is = this.getClass().getResourceAsStream("/au/com/scds/chats/fixture/dex_activities.xml");
			JAXBContext jaxbContext = JAXBContext.newInstance(ObjectFactory.class);
			Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
			jaxbUnmarshaller.setEventHandler(new javax.xml.bind.helpers.DefaultValidationEventHandler());
			Activities _activities = (Activities) JAXBIntrospector.getValue(jaxbUnmarshaller.unmarshal(is));
			for (ChatsActivity _activity : _activities.getActivity()) {
				au.com.scds.chats.dom.activity.ChatsActivity activity = activityMenu
						.createOneOffActivity(_activity.getName(), "TEST", new DateTime(_activity.getStart()));
				activity.setRegion(regions.regionForName(_activity.getRegion()));
				activity.updateEndDateTime(new DateTime(_activity.getStart()));
				au.com.scds.chats.dom.activity.ChatsParticipant participant = null;
				au.com.scds.chats.dom.volunteer.Volunteer volunteer = null;
				//create attendances only
				for (ChatsAttendance _attendance : _activity.getAttendance()) {
					ChatsPerson _person = _attendance.getParticipant().getPerson();
					participant = participantMenu.create(_person.getFirstname(), _person.getSurname(),
							LocalDate.fromDateFields(_person.getDateOfBirth()), Sex.valueOf(_person.getSex()));
					activity.addAttendance(participant);
				}
				ActivityEvent_addVolunteeredTime mixin = new ActivityEvent_addVolunteeredTime(activity);
				mixin.volunteersRepo = volunteerMenu;
				for (VolunteeredTime _time : _activity.getVolunteeredTime()) {
					ChatsPerson _person = _time.getVolunteer().getPerson();
					volunteer = volunteerMenu.create(_person.getFirstname(), _person.getSurname(),
							LocalDate.fromDateFields(_person.getDateOfBirth()), Sex.valueOf(_person.getSex()));
					mixin.$$(volunteer, new DateTime(_time.getStart()), new DateTime(_time.getEnd()));
				}
				activities.add(activity);
			}
		} catch (JAXBException e) {
			e.printStackTrace();
		}
	}

	public List<au.com.scds.chats.dom.activity.ChatsActivity> getActivities() {
		return this.activities;
	}

	@Inject
	ActivityMenu activityMenu;

	@Inject
	ParticipantsMenu participantMenu;

	@Inject
	VolunteersMenu volunteerMenu;

	@Inject
	Regions regions;

}
