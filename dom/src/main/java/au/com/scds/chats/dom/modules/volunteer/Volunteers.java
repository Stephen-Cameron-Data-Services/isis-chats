package au.com.scds.chats.dom.modules.volunteer;

import java.util.List;

import org.apache.isis.applib.DomainObjectContainer;
import org.apache.isis.applib.annotation.Action;
import org.apache.isis.applib.annotation.ActionLayout;
import org.apache.isis.applib.annotation.BookmarkPolicy;
import org.apache.isis.applib.annotation.MemberOrder;
import org.apache.isis.applib.annotation.ParameterLayout;
import org.apache.isis.applib.annotation.SemanticsOf;
import org.apache.isis.applib.query.QueryDefault;
/**
 * 
 * 
 * @author Steve Cameron Data Services
 *
 */
public class Volunteers {
	
    //region > listActive (action)
    @Action(
            semantics = SemanticsOf.SAFE
    )
    @ActionLayout(
            bookmarking = BookmarkPolicy.AS_ROOT
    )
    @MemberOrder(sequence = "1")
    public List<Volunteer> listActive() {
        //return container.allInstances(Participant.class);
        return container.allMatches(
                new QueryDefault<>(
                        Volunteer.class,
                        "findActive",
                        "status", "active"));
    }
    //endregion
    
    //region > listExited (action)
    @Action(
            semantics = SemanticsOf.SAFE
    )
    @ActionLayout(
            bookmarking = BookmarkPolicy.AS_ROOT
    )
    @MemberOrder(sequence = "2")
    public List<Volunteer> listExited() {
        //return container.allInstances(Participant.class);
        return container.allMatches(
                new QueryDefault<>(
                        Volunteer.class,
                        "findExited",
                        "status", "exited"));
    }
    //endregion

    //region > findByName (action)
    @Action(
            semantics = SemanticsOf.SAFE
    )
    @ActionLayout(
            bookmarking = BookmarkPolicy.AS_ROOT
    )
    @MemberOrder(sequence = "3")
    public List<Volunteer> findByName(
            @ParameterLayout(named="Name")
            final String name
    ) {
        return container.allMatches(
                new QueryDefault<>(
                        Volunteer.class,
                        "findByName",
                        "name", name));
    }
    //endregion
	
    //region > injected services

    @javax.inject.Inject 
    DomainObjectContainer container;

    //endregion
}
