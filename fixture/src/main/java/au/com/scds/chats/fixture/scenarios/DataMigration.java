package au.com.scds.chats.fixture.scenarios;

import org.apache.isis.applib.DomainObjectContainer;
import org.apache.isis.applib.annotation.Programmatic;
import org.apache.isis.applib.fixturescripts.FixtureScript;


public class DataMigration extends FixtureScript {

	@Override
	@Programmatic
	protected void execute(ExecutionContext arg0) {
		// TODO Auto-generated method stub
		System.out.println(container);
	}
	
	@javax.inject.Inject
	private DomainObjectContainer container;

}
