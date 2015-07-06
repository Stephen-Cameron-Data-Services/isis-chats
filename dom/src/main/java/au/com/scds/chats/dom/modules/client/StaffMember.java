package au.com.scds.chats.dom.modules.client;

import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.VersionStrategy;
import org.apache.isis.applib.DomainObjectContainer;
import org.apache.isis.applib.Identifier;
import org.apache.isis.applib.annotation.Action;
import org.apache.isis.applib.annotation.BookmarkPolicy;
import org.apache.isis.applib.annotation.CollectionLayout;
import org.apache.isis.applib.annotation.DomainObject;
import org.apache.isis.applib.annotation.DomainObjectLayout;
import org.apache.isis.applib.annotation.Editing;
import org.apache.isis.applib.annotation.MemberOrder;
import org.apache.isis.applib.annotation.Parameter;
import org.apache.isis.applib.annotation.ParameterLayout;
import org.apache.isis.applib.annotation.Property;
import org.apache.isis.applib.annotation.RenderType;
import org.apache.isis.applib.annotation.SemanticsOf;
import org.apache.isis.applib.annotation.Title;
import org.apache.isis.applib.services.eventbus.ActionDomainEvent;
import org.apache.isis.applib.services.i18n.TranslatableString;
import org.apache.isis.applib.util.ObjectContracts;

import au.com.scds.chats.dom.modules.client.Client.UpdateNameDomainEvent;


import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.TreeSet;

@javax.jdo.annotations.PersistenceCapable(identityType=IdentityType.DATASTORE)
@javax.jdo.annotations.DatastoreIdentity(
        strategy=javax.jdo.annotations.IdGeneratorStrategy.IDENTITY,
         column="id")
@javax.jdo.annotations.Version(
        strategy=VersionStrategy.VERSION_NUMBER, 
        column="version")
@javax.jdo.annotations.Queries({
        @javax.jdo.annotations.Query(
                name = "find", language = "JDOQL",
                value = "SELECT "
                        + "FROM au.com.scds.chats.dom.modules.client.StaffMember "),
        @javax.jdo.annotations.Query(
                name = "findByName", language = "JDOQL",
                value = "SELECT "
                        + "FROM au.com.scds.chats.dom.modules.client.StaffMember "
                        + "WHERE name.indexOf(:name) >= 0 ")
})
@javax.jdo.annotations.Unique(name="StaffMember_name_UNQ", members = {"name"})
@DomainObject(
        objectType = "STAFF_MEMBER"
)
@DomainObjectLayout(
        bookmarking = BookmarkPolicy.AS_ROOT
)

public class StaffMember implements Comparable<StaffMember> {

	// {{ Name (property)
	private String name;
	
    @javax.jdo.annotations.Column(allowsNull="false", length = 40)
    @Title(sequence="1")
    @Property(
            editing = Editing.DISABLED
    )
	@MemberOrder(sequence = "1")
	public String getName() {
		return name;
	}

	public void setName(final String name) {
		this.name = name;
	}
	// }}
	
	// region > updateName (action)

	public static class UpdateNameDomainEvent extends ActionDomainEvent<Client> {
		public UpdateNameDomainEvent(final Client source,
				final Identifier identifier, final Object... arguments) {
			super(source, identifier, arguments);
		}
	}

	@Action(domainEvent = UpdateNameDomainEvent.class)
	public StaffMember updateName(
			@Parameter(maxLength = 40) @ParameterLayout(named = "New name") final String name) {
		setName(name);
		return this;
	}

	public String default0UpdateName() {
		return getName();
	}

	public TranslatableString validateUpdateName(final String name) {
		return name.contains("!") ? TranslatableString
				.tr("Exclamation mark is not allowed") : null;
	}
	// endregion

	@Override
	public int compareTo(StaffMember arg0) {
		// TODO Auto-generated method stub
		return 0;
	}


}
