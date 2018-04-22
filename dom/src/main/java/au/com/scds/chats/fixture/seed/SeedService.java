package au.com.scds.chats.fixture.seed;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import org.apache.isis.applib.annotation.DomainService;
import org.apache.isis.applib.annotation.DomainServiceLayout;
import org.apache.isis.applib.annotation.NatureOfService;
import org.apache.isis.applib.annotation.Programmatic;
import org.apache.isis.applib.fixturescripts.FixtureScripts;

@DomainService(
        nature = NatureOfService.DOMAIN
)
@DomainServiceLayout(
        menuOrder = "2000" // not visible, but determines the order initialized, Security module is 1000
)
public class SeedService {


    @Programmatic
    @PostConstruct
    public void init() {
        fixtureScripts.runFixtureScript(new SeedFixtureScript(), null);
    }

    @Inject
    FixtureScripts fixtureScripts;

}