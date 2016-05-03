package au.com.scds.chats.fixture.jaxb;

import java.io.InputStream;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import au.com.scds.chats.fixture.jaxb.generated.Activity;
import au.com.scds.chats.fixture.jaxb.generated.Names;
import au.com.scds.chats.fixture.jaxb.generated.Names.ActivityTypes;

public class Scratch {

	public static void main(String[] args) {
		try {
			InputStream is = Names.class.getResourceAsStream("/au/com/scds/chats/fixture/jaxb/names.xml");
			JAXBContext jaxbContext = JAXBContext.newInstance(Names.class);
			Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
			Names names = (Names) jaxbUnmarshaller.unmarshal(is);
			for(String type: names.getActivityTypes().getActivityType())
				System.out.println(type);
		} catch (JAXBException e) {
			e.printStackTrace();
		}
	}
}
