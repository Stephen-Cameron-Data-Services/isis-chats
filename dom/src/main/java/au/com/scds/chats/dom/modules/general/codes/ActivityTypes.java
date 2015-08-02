package au.com.scds.chats.dom.modules.general.codes;

import java.util.ArrayList;
import java.util.List;

import org.apache.isis.applib.DomainObjectContainer;
import org.apache.isis.applib.annotation.Action;
import org.apache.isis.applib.annotation.ActionLayout;
import org.apache.isis.applib.annotation.BookmarkPolicy;
import org.apache.isis.applib.annotation.DomainService;
import org.apache.isis.applib.annotation.DomainServiceLayout;
import org.apache.isis.applib.annotation.DomainServiceLayout.MenuBar;
import org.apache.isis.applib.annotation.MemberOrder;
import org.apache.isis.applib.annotation.ParameterLayout;
import org.apache.isis.applib.annotation.Programmatic;
import org.apache.isis.applib.annotation.SemanticsOf;
import org.apache.isis.applib.query.QueryDefault;

import au.com.scds.chats.dom.modules.participant.Participant;

@DomainService(repositoryFor = ActivityType.class)
@DomainServiceLayout(menuBar = MenuBar.SECONDARY, named = "Administration", menuOrder = "100.1")
public class ActivityTypes {

	// activityType > listAll (action)
	@Action(semantics = SemanticsOf.SAFE)
	@ActionLayout(bookmarking = BookmarkPolicy.AS_ROOT)
	@MemberOrder(sequence = "1")
	public List<ActivityType> listAllActivityTypes() {
		return container.allInstances(ActivityType.class);
	}

	// endactivityType

	// activityType > create (action)
	@MemberOrder(sequence = "2")
	public List<ActivityType> createActivityType(
			final @ParameterLayout(named = "ActivityType Name") String name) {
		final ActivityType obj = container.newTransientInstance(ActivityType.class);
		obj.setName(name);
		container.persistIfNotAlready(obj);
		container.flush();
		return listAllActivityTypes();
	}

	// endactivityType

	// activityType > injected services

	@javax.inject.Inject
	DomainObjectContainer container;

	/**
	 * Returns the name of a ActivityType
	 * 
	 * @param activityType
	 *            : the ActivityType of interest
	 * @return
	 */
	@Programmatic
	public String nameForActivityType(ActivityType activityType) {
		return (activityType != null) ? activityType.getName() : null;
	}

	/**
	 * Returns a list of all ActivityType names exclusive of that of the activityType
	 * argument. If activityType is null, it will be the full set of ActivityType names.
	 * 
	 * @param activityType
	 *            : the ActivityType to exclude
	 * @return
	 */
	@Programmatic
	public List<String> listAllNamesExclusive(ActivityType activityType) {
		List<ActivityType> activityTypes = listAllActivityTypes();
		List<String> names = new ArrayList<String>();
		for (ActivityType r : activityTypes) {
			if (activityType != null) {
				if (r.getName().equals(activityType.getName())) {
					names.add(r.getName());
				}
			}else{
				names.add(r.getName());
			}
		}
		return names;
	}

	/**
	 * Returns the ActivityType having a specific name
	 * 
	 * @param activityType
	 * @return
	 */
	@Programmatic
	public ActivityType activityTypeForName(String name) {
		ActivityType activityType = container.firstMatch(new QueryDefault<>(ActivityType.class,
				"findActivityType", "name", name));
		return activityType;
	}

	// endactivityType
}
