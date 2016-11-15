package au.com.scds.chats.fixture.activity;

import java.io.InputStream;
import java.util.Date;

import javax.inject.Inject;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.JAXBIntrospector;
import javax.xml.bind.Unmarshaller;

import org.apache.isis.applib.fixturescripts.FixtureScript;
import org.apache.isis.applib.fixturescripts.FixtureScript.Discoverability;
import org.apache.isis.applib.fixturescripts.FixtureScript.ExecutionContext;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;

import au.com.scds.chats.dom.activity.Activities;
import au.com.scds.chats.dom.activity.ActivityEvent;
import au.com.scds.chats.dom.general.Sex;
import au.com.scds.chats.dom.general.Suburb;
import au.com.scds.chats.dom.general.Suburbs;
import au.com.scds.chats.dom.general.names.Region;
import au.com.scds.chats.dom.general.names.Regions;
import au.com.scds.chats.fixture.jaxb.generated.ObjectFactory;

public class RecreateActivities extends FixtureScript {

	@Inject
	Activities activitiesRepo;

	@Inject
	Regions regionsRepo;

	public RecreateActivities() {
		withDiscoverability(Discoverability.DISCOVERABLE);
	}

	@Override
	protected void execute(ExecutionContext ec) {
		
		Activities activitiesRepo = new Activities();
		Regions regionsRepo = new Regions();

		Region testRegion = regionsRepo.findOrCreateRegion("TEST");

		try {
			InputStream is = au.com.scds.chats.fixture.jaxb.generated.Names.class
					.getResourceAsStream("/au/com/scds/chats/fixture/jaxb/activities.xml");
			JAXBContext jaxbContext = JAXBContext.newInstance(ObjectFactory.class);
			Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
			au.com.scds.chats.fixture.jaxb.generated.Activities fixture = (au.com.scds.chats.fixture.jaxb.generated.Activities) JAXBIntrospector
					.getValue(jaxbUnmarshaller.unmarshal(is));
			for (au.com.scds.chats.fixture.jaxb.generated.ActivityEvent a : fixture.getActivity()) {
				Date start = a.getStartDateTime().toGregorianCalendar().getTime();
				activitiesRepo.findOrCreateOneOffActivity(a.getName(), new DateTime(start), testRegion);
			}
		} catch (JAXBException e) {
			e.printStackTrace();
		}
	}
}
