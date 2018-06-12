package au.com.scds.chats.fixture.seed;

import javax.inject.Inject;
import org.apache.isis.applib.fixturescripts.FixtureScript;
import org.apache.isis.applib.value.Password;
import org.isisaddons.module.security.dom.permission.ApplicationPermissionMode;
import org.isisaddons.module.security.dom.permission.ApplicationPermissionRule;
import org.isisaddons.module.security.dom.role.ApplicationRole;
import org.isisaddons.module.security.dom.role.ApplicationRoleRepository;
import org.isisaddons.module.security.dom.user.ApplicationUser;
import org.isisaddons.module.security.dom.user.ApplicationUserRepository;

public class ConfigureSecurity extends FixtureScript {

	public ConfigureSecurity() {
		withDiscoverability(Discoverability.NON_DISCOVERABLE);
	}

	@Override
	protected void execute(ExecutionContext ec) {

		try {
			//add 
			ApplicationRole role = rolesRepo.findByName("isis-module-security-regular-user");
			if (role != null) {
				role.addPackage(ApplicationPermissionRule.ALLOW, ApplicationPermissionMode.CHANGING,
						"au.com.scds.chats.dom");
				role.addPackage(ApplicationPermissionRule.ALLOW, ApplicationPermissionMode.CHANGING,
						"au.com.scds.chats.fixture");
				role.addPackage(ApplicationPermissionRule.ALLOW, ApplicationPermissionMode.CHANGING,
						"org.apache.isis.applib.services.layout");
				role.addPackage(ApplicationPermissionRule.ALLOW, ApplicationPermissionMode.CHANGING,
						"org.apache.isis.applib.services.fixturespec");
				role.addPackage(ApplicationPermissionRule.ALLOW, ApplicationPermissionMode.CHANGING,
						"org.apache.isis.applib.services.hsqldb");
				role.addPackage(ApplicationPermissionRule.VETO, ApplicationPermissionMode.VIEWING,
						"au.com.scds.eventschedule.base.menu");
				//role.addAction(ApplicationPermissionRule.VETO, ApplicationPermissionMode.VIEWING,
				//		"org.isisaddons.module.security.app.user", "MeService", "me");
			}
			
			//create user sven
			Password pass = new Password("pass");
			ApplicationUser sven = userRepo.newLocalUser("sven", pass, pass, role, true, "");
			sven.setAtPath("/");
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Inject
	ApplicationRoleRepository rolesRepo;

	@Inject
	ApplicationUserRepository userRepo;
}
