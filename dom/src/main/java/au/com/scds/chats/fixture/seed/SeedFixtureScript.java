package au.com.scds.chats.fixture.seed;

import org.apache.isis.applib.fixturescripts.FixtureScript;

import au.com.scds.chats.fixture.scenarios.CreateDexReferenceData;

public class SeedFixtureScript  extends FixtureScript {

	@Override
	protected void execute(ExecutionContext executionContext) {
		executionContext.executeChild(this, new CreateRegions());
		//executionContext.executeChild(this, new CreateSuburbs());
		//executionContext.executeChild(this, new CreateDexReferenceData());
		executionContext.executeChild(this, new ConfigureSecurity());		
	}
}
