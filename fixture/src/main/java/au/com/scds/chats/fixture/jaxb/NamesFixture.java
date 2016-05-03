package au.com.scds.chats.fixture.jaxb;

import java.io.File;
import java.io.InputStream;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.apache.isis.applib.fixturescripts.FixtureScript;

import au.com.scds.chats.fixture.jaxb.generated.Names;

public class NamesFixture extends FixtureScript {

	@Override
	protected void execute(ExecutionContext arg0) {

		try {
			InputStream is = Names.class.getResourceAsStream("names.xml");
			JAXBContext jaxbContext = JAXBContext.newInstance(Names.class);
			Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
			Names names = (Names) jaxbUnmarshaller.unmarshal(is);
			System.out.println(names);
		} catch (JAXBException e) {
			e.printStackTrace();
		}

	}

}
