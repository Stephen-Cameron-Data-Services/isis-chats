package au.com.scds.chats.dom.module.activity;

import java.util.List;

import javax.jdo.annotations.*;

/**
 * 
 * An <code>Activity</code> that is used to schedule individual
 * <code>ActivityEvent</code> objects.
 * 
 * The term 'activity' is used in this domain in both a generic and specific
 * way, the term 'event' is not used but is more descriptive of what is
 * occurring, that is a series of calendar events make up a recurring activity.
 * 
 * @author stevec
 * 
 */
@PersistenceCapable()
//@DatastoreIdentity(strategy = javax.jdo.annotations.IdGeneratorStrategy.IDENTITY, column = "id")
@Inheritance(strategy = InheritanceStrategy.SUPERCLASS_TABLE)
@Queries({ @Query(name = "find", language = "JDOQL", value = "SELECT FROM au.com.scds.chats.dom.module.activity.RecurringActivity "),
		@Query(name = "findByName", language = "JDOQL", value = "SELECT FROM au.com.scds.chats.dom.module.activity.RecurringActivity WHERE name.indexOf(:name) >= 0 ") })
public class RecurringActivity extends AbstractActivity {

	private List<ActivityEvent> children;

	private Periodicity periodicity;

}
