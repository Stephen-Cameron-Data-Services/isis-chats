package au.com.scds.chats.fixture.general;

import java.io.File;
import java.io.InputStream;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.apache.isis.applib.fixturescripts.FixtureScript;

import au.com.scds.chats.dom.general.Suburbs;
import au.com.scds.chats.dom.general.names.Names;
import au.com.scds.chats.fixture.jaxb.generated.ObjectFactory;


public class RecreateSuburbs extends FixtureScript {
	
    public RecreateSuburbs() {
        withDiscoverability(Discoverability.DISCOVERABLE);
    }
    
	@Override
	protected void execute(ExecutionContext ec) {

		Suburbs suburbs = new Suburbs();
		
		try {
			InputStream is = au.com.scds.chats.fixture.jaxb.generated.Names.class.getResourceAsStream("/au/com/scds/chats/fixture/jaxb/suburbs.xml");
			JAXBContext jaxbContext = JAXBContext.newInstance(ObjectFactory.class);
			Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
			au.com.scds.chats.fixture.jaxb.generated.Suburbs fixture = ((JAXBElement<au.com.scds.chats.fixture.jaxb.generated.Suburbs>) jaxbUnmarshaller.unmarshal(is)).getValue();
			for(au.com.scds.chats.fixture.jaxb.generated.Suburbs.Suburb suburb : fixture.getSuburb()){
				wrap(suburbs).createSuburb(suburb.getName(),suburb.getPostcode());
			}
			ec.addResult(this, suburbs.listAllSuburbs());
		} catch (JAXBException e) {
			e.printStackTrace();
		}

	}
	
  /*  @Override
    protected void execute(final ExecutionContext ec) {

        String name = checkParam("name", ec, String.class);

        this.simpleObject = wrap(simpleObjects).create(name);

        // also make available to UI
        ec.addResult(this, simpleObject);
    }*/



}
