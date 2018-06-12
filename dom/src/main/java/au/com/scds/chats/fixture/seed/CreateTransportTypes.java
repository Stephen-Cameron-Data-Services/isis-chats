package au.com.scds.chats.fixture.seed;

import javax.inject.Inject;
import org.apache.isis.applib.fixturescripts.FixtureScript;
import au.com.scds.chats.dom.general.names.TransportTypes;

public class CreateTransportTypes extends FixtureScript {

	public CreateTransportTypes() {
		withDiscoverability(Discoverability.DISCOVERABLE);
	}

	@Override
	protected void execute(ExecutionContext ec) {

		try {
			transportTypesRepo.findOrCreateTransportType("Chats Share");
			transportTypesRepo.findOrCreateTransportType("Community Transport");
			transportTypesRepo.findOrCreateTransportType("Lifeline Vehicle");
			transportTypesRepo.findOrCreateTransportType("Outsource");
			transportTypesRepo.findOrCreateTransportType("Self Travel");
			transportTypesRepo.findOrCreateTransportType("Taxi");
			transportTypesRepo.findOrCreateTransportType("Unknown");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Inject
	TransportTypes transportTypesRepo;
}
