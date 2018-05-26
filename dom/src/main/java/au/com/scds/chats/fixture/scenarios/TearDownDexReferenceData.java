package au.com.scds.chats.fixture.scenarios;

import org.apache.isis.applib.fixturescripts.FixtureScript;
import org.apache.isis.applib.services.jdosupport.IsisJdoSupport;

public class TearDownDexReferenceData extends FixtureScript {

    @Override
    protected void execute(ExecutionContext executionContext) {

    	isisJdoSupport.executeUpdate("delete from \"chats\".\"dexreferenceitem\"");
    }


    @javax.inject.Inject
    private IsisJdoSupport isisJdoSupport;

}
