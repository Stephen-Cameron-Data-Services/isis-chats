package au.com.scds.chats.fixture.jaxb;

import java.io.InputStream;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import au.com.scds.chats.fixture.jaxb.generated.ActivityFixture;
import au.com.scds.chats.fixture.jaxb.generated.NamesFixture;
import au.com.scds.chats.fixture.jaxb.generated.ObjectFactory;
import au.com.scds.chats.fixture.jaxb.generated.PeriodicityFixture;

public class Scratch {

	public static void main(String[] args) {
		try {
			InputStream is = NamesFixture.class.getResourceAsStream("/au/com/scds/chats/fixture/jaxb/names.xml");
			JAXBContext jaxbContext = JAXBContext.newInstance(ObjectFactory.class);
			Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
			NamesFixture names = ((JAXBElement<NamesFixture>) jaxbUnmarshaller.unmarshal(is)).getValue();
			for(String region: names.getRegions().getRegion())
				System.out.println(region);
			for(String type: names.getActivityTypes().getActivityType())
				System.out.println(type);
			for(String type: names.getTransportTypes().getTransportType())
				System.out.println(type);
			for(PeriodicityFixture periodicity: names.getPeriodicities().getPeriodicity())
				System.out.println(periodicity.value());
		} catch (JAXBException e) {
			e.printStackTrace();
		}
	}
}
