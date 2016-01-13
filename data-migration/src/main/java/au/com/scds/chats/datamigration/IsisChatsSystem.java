package au.com.scds.chats.datamigration;

import java.util.List;

import javax.inject.Inject;

import au.com.scds.chats.dom.module.activity.Activities;
import au.com.scds.chats.dom.module.activity.ActivityEvent;

import com.google.common.collect.Lists;

import domainapp.app.DomainAppAppManifest;

//import org.incode.module.note.fixture.dom.notedemoobject.NoteDemoObject;
//import org.incode.module.note.fixture.dom.notedemoobject.NoteDemoObjectMenu;
//import org.incode.module.note.app.NoteModuleAppManifest;
//import org.incode.module.note.integtests.NoteModuleIntegTest;
//import org.isisaddons.module.fakedata.FakeDataModule;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import org.apache.isis.applib.fixturescripts.FixtureScripts;
import org.apache.isis.core.integtestsupport.IntegrationTestAbstract;
import org.apache.isis.core.integtestsupport.IsisSystemForTest;
import org.apache.isis.core.integtestsupport.scenarios.ScenarioExecutionForIntegration;
import org.apache.isis.objectstore.jdo.datanucleus.IsisConfigurationForJdoIntegTests;

public class IsisChatsSystem extends IntegrationTestAbstract {

	@Inject
	Activities activities;

	@Test
	public void listAll() throws Exception {

		final List<ActivityEvent> all = wrap(activities).listAllFutureActivities();
		System.out.print(all.size());
	}

	@BeforeClass
	public static void initClass() {
		org.apache.log4j.PropertyConfigurator.configure("logging.properties");

		IsisConfigurationForJdoIntegTests config = new IsisConfigurationForJdoIntegTests();
		config.put("isis.persistor.datanucleus.impl.javax.jdo.option.ConnectionDriverName", "com.mysql.jdbc.Driver");
		config.put("isis.persistor.datanucleus.impl.javax.jdo.option.ConnectionURL", "jdbc:mysql://localhost:3306/chats?zeroDateTimeBehavior=convertToNull");
		config.put("isis.persistor.datanucleus.impl.javax.jdo.option.ConnectionUserName", "chats");
		config.put("isis.persistor.datanucleus.impl.javax.jdo.option.ConnectionPassword", "password");

		IsisSystemForTest isft = IsisSystemForTest.getElseNull();
		if (isft == null) {
			isft = new IsisSystemForTest.Builder()
					.withLoggingAt(org.apache.log4j.Level.INFO)
					.with(new DomainAppAppManifest())
					.with(config)
					.build()
					.setUpSystem();
			IsisSystemForTest.set(isft);
		}

		// instantiating will install onto ThreadLocal
		new ScenarioExecutionForIntegration();
	}
}
