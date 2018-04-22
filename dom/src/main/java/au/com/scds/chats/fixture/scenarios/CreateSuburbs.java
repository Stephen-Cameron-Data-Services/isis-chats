package au.com.scds.chats.fixture.scenarios;

import java.io.InputStream;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBIntrospector;
import javax.xml.bind.Unmarshaller;

import org.apache.isis.applib.fixturescripts.FixtureScript;

import au.com.scds.chats.fixture.generated.ObjectFactory;
import au.com.scds.chats.fixture.generated.Suburb;
import au.com.scds.chats.fixture.generated.Suburbs;

public class CreateSuburbs extends FixtureScript {

	public CreateSuburbs() {
		withDiscoverability(Discoverability.DISCOVERABLE);
	}

	au.com.scds.chats.dom.general.Suburbs suburbs = new au.com.scds.chats.dom.general.Suburbs();

	@Override
	protected void execute(ExecutionContext ec) {

		try {
			// import object graph from XML
			InputStream is = this.getClass().getResourceAsStream("/au/com/scds/chats/fixture/activities.xml");
			JAXBContext jaxbContext = JAXBContext.newInstance(ObjectFactory.class);
			Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
			jaxbUnmarshaller.setEventHandler(new javax.xml.bind.helpers.DefaultValidationEventHandler());
			Suburbs _suburbs = (Suburbs) JAXBIntrospector.getValue(jaxbUnmarshaller.unmarshal(is));
			int i = 0;
			for (Suburb _suburb : _suburbs.getSuburb()) {
				wrap(suburbs).createSuburb(_suburb.getName(), _suburb.getPostcode());
				if(i++ == 10)
					break;
			}
			ec.addResult(this, suburbs.listAllSuburbs());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
