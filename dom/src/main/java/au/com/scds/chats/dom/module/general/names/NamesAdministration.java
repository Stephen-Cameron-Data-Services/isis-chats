package au.com.scds.chats.dom.module.general.names;

import java.util.List;

import org.apache.isis.applib.annotation.*;
import org.apache.isis.applib.annotation.DomainServiceLayout.MenuBar;

@DomainServiceLayout(menuBar = MenuBar.SECONDARY, named = "Administration", menuOrder = "100.10")
public class NamesAdministration {

	
    @CollectionLayout(render = RenderType.LAZILY)
    public List<ActivityType> getActivityTypes() {
        return activityTypes.listAllActivityTypes();
    }
    
	@javax.inject.Inject
	ActivityTypes activityTypes;
}
