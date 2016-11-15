package au.com.scds.chats.fixture.activity;

import java.io.InputStream;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.apache.isis.applib.fixturescripts.FixtureScript;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;

import au.com.scds.chats.dom.activity.Activities;
import au.com.scds.chats.dom.activity.ActivityEvent;
import au.com.scds.chats.dom.general.Sex;
import au.com.scds.chats.dom.general.Suburb;
import au.com.scds.chats.dom.general.Suburbs;
import au.com.scds.chats.dom.general.names.Names;
import au.com.scds.chats.fixture.jaxb.generated.Address;
import au.com.scds.chats.fixture.jaxb.generated.ObjectFactory;
import au.com.scds.chats.fixture.jaxb.generated.Participation;
import au.com.scds.chats.fixture.jaxb.generated.Person;

public class RecreateOneOffActivities extends FixtureScript {

	public RecreateOneOffActivities() {
		withDiscoverability(Discoverability.DISCOVERABLE);
	}

	@Override
	protected void execute(ExecutionContext ec) {

		Activities activities = new Activities();
		Suburbs suburbs = new Suburbs();

		try {
			InputStream is = au.com.scds.chats.fixture.jaxb.generated.Names.class
					.getResourceAsStream("/au/com/scds/chats/fixture/jaxb/one-off-activity.xml");
			JAXBContext jaxbContext = JAXBContext.newInstance(ObjectFactory.class);
			Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
			au.com.scds.chats.fixture.jaxb.generated.ActivityEvent fixture = ((JAXBElement<au.com.scds.chats.fixture.jaxb.generated.ActivityEvent>) jaxbUnmarshaller.unmarshal(is))
					.getValue();
			ActivityEvent activity = wrap(activities).createOneOffActivity(fixture.getName(), "TO-DO",
					new DateTime(fixture.getStartDateTime().toGregorianCalendar().getTime()));
			wrap(activity).setActivityTypeName(fixture.getActivityType());
			wrap(activity).setDescription(fixture.getDescription());
			wrap(activity).setCostForParticipant(fixture.getCostForParticipant());
			au.com.scds.chats.fixture.jaxb.generated.Address a = fixture.getAddress();
			Suburb suburb = wrap(suburbs).findSuburb(a.getSuburb(), a.getPostcode());
			wrap(activity).updateLocation( null, a.getLocationName(), a.getStreet1(), a.getStreet2(), suburb);
			ec.addResult(this, activity);
			for (au.com.scds.chats.fixture.jaxb.generated.Participation par : fixture.getParticipation()) {
				au.com.scds.chats.fixture.jaxb.generated.Person per = par.getParticipant().getPerson();
				wrap(activity).addNewParticipant(per.getFirstname(), per.getSurname(),
						new LocalDate(per.getDateOfBirth().toGregorianCalendar().getTime()),
						Sex.valueOf(per.getSex().value()));
			}
		} catch (JAXBException e) {
			e.printStackTrace();
		}

	}

}
