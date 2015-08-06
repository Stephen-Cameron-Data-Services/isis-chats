package au.com.scds.chats.dom.module.data;

import org.apache.isis.applib.DomainObjectContainer;
import org.apache.isis.applib.annotation.Action;
import org.apache.isis.applib.annotation.DomainService;
import org.apache.isis.applib.annotation.DomainServiceLayout;
import org.apache.isis.applib.annotation.NatureOfService;
import org.apache.isis.applib.annotation.RestrictTo;

import au.com.scds.chats.datamigration.access.DataMapper;


@DomainService(nature = NatureOfService.VIEW_MENU_ONLY)
@DomainServiceLayout(named = "Prototyping", menuBar = DomainServiceLayout.MenuBar.SECONDARY, menuOrder = "500.20")
public class DataMigration {

	@Action(restrictTo = RestrictTo.PROTOTYPING)
	public void RunMigration() {
		DataMapper mapper = new DataMapper();
		try {
			mapper.doProcess(null);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@javax.inject.Inject
	DomainObjectContainer container;
}
