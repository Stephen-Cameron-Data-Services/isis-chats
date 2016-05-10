package au.com.scds.chats.fixture.general;

import java.io.File;
import java.io.InputStream;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.apache.isis.applib.fixturescripts.FixtureScript;
import au.com.scds.chats.dom.general.names.Names;
import au.com.scds.chats.fixture.jaxb.generated.NamesFixture;
import au.com.scds.chats.fixture.jaxb.generated.ObjectFactory;



public class RecreateNames extends FixtureScript {

	@Override
	protected void execute(ExecutionContext ec) {

		Names names = new Names();
		
		try {
			InputStream is = NamesFixture.class.getResourceAsStream("/au/com/scds/chats/fixture/jaxb/names.xml");
			JAXBContext jaxbContext = JAXBContext.newInstance(ObjectFactory.class);
			Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
			NamesFixture fixture = ((JAXBElement<NamesFixture>) jaxbUnmarshaller.unmarshal(is)).getValue();
			for(String region: fixture.getRegions().getRegion())
				wrap(names).createRegion(region);
			for(String type: fixture.getActivityTypes().getActivityType())
				wrap(names).createActivityType(type);
			for(String type: fixture.getTransportTypes().getTransportType())
				wrap(names).createTransportType(type);
			//for(PeriodicityFixture periodicity: fixture.getPeriodicities().getPeriodicity())
				//System.out.println(periodicity.value());
			
			ec.addResult(this, names);
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
