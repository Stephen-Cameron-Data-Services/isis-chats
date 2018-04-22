package au.com.scds.chats.fixture.seed;

import javax.inject.Inject;
import org.apache.isis.applib.fixturescripts.FixtureScript;
import au.com.scds.chats.dom.general.names.Regions;
public class CreateRegions extends FixtureScript {

	public CreateRegions() {
		withDiscoverability(Discoverability.DISCOVERABLE);
	}

	@Override
	protected void execute(ExecutionContext ec) {

		try {
			regionsRepo.findOrCreateRegion("SOUTH");
			regionsRepo.findOrCreateRegion("NORTH");
			regionsRepo.findOrCreateRegion("NORTH-WEST");
			regionsRepo.findOrCreateRegion("STATEWIDE");
			regionsRepo.findOrCreateRegion("TEST");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Inject
	Regions regionsRepo;
}
