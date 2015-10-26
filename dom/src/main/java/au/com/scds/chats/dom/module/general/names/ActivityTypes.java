package au.com.scds.chats.dom.module.general.names;

import java.util.ArrayList;
import java.util.List;
import org.apache.isis.applib.annotation.*;
import org.apache.isis.applib.DomainObjectContainer;
import org.apache.isis.applib.annotation.DomainServiceLayout.MenuBar;
import org.apache.isis.applib.query.QueryDefault;

@DomainService(repositoryFor = ActivityType.class)
@DomainServiceLayout(menuBar = MenuBar.SECONDARY, named = "Administration", menuOrder = "100.1")
public class ActivityTypes {

	@Action(semantics = SemanticsOf.SAFE)
	@ActionLayout(bookmarking = BookmarkPolicy.AS_ROOT)
	@MemberOrder(sequence = "1")
	public List<ActivityType> listAllActivityTypes() {
		List<ActivityType> list = container.allMatches(new QueryDefault<>(ActivityType.class, "findAllActivityTypes"));
		return list;
	}

	@MemberOrder(sequence = "2")
	public List<ActivityType> createActivityType(final @ParameterLayout(named = "Activity Type Name") String name) {
		final ActivityType obj = create(name);
		return listAllActivityTypes();
	}
	
	@Programmatic
	protected ActivityType create(String name){
		final ActivityType obj = container.newTransientInstance(ActivityType.class);
		obj.setName(name);
		container.persistIfNotAlready(obj);
		container.flush();
		return obj;		
	}

	@Programmatic
	public String nameForActivityType(ActivityType activityType) {
		return (activityType != null) ? activityType.getName() : null;
	}

	@Programmatic
	public List<String> allNames() {
		List<ActivityType> activityTypes = listAllActivityTypes();
		List<String> names = new ArrayList<String>();
		for (ActivityType a : activityTypes) {
			names.add(a.getName());
		}
		return names;
	}

	@Programmatic
	public ActivityType activityTypeForName(String name) {
		if (name == null)
			return null;
		else
			return container.firstMatch(new QueryDefault<>(ActivityType.class, "findActivityTypeByName", "name", name));
	}

	@javax.inject.Inject
	DomainObjectContainer container;
}
