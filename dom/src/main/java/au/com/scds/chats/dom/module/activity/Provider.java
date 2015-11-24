package au.com.scds.chats.dom.module.activity;

import javax.jdo.annotations.*;

import org.apache.isis.applib.annotation.BookmarkPolicy;
import org.apache.isis.applib.annotation.DomainObject;
import org.apache.isis.applib.annotation.DomainObjectLayout;
import org.apache.isis.applib.annotation.MemberOrder;
import org.apache.isis.applib.services.i18n.TranslatableString;

@DomainObject(objectType = "PROVIDER")
@DomainObjectLayout(bookmarking = BookmarkPolicy.AS_ROOT)
@PersistenceCapable(identityType = IdentityType.DATASTORE)
@Queries({ @Query(name = "listAllProviders", language = "JDOQL", value = "SELECT " + "FROM au.com.scds.chats.dom.module.activity.Provider "),
		@Query(name = "findProvidersByName", language = "JDOQL", value = "SELECT " + "FROM au.com.scds.chats.dom.module.activity.Provider " + "WHERE name.indexOf(:name) >= 0 ") })
@Unique(name = "Provider_name_UNQ", members = { "name" })
public class Provider {

	private String name;
	
	public TranslatableString title() {
		return TranslatableString.tr("Provider: {name}", "name", getName());
	}

	@MemberOrder(sequence = "1")
	@Column(allowsNull = "false", length = 40)
	public String getName() {
		return name;
	}

	public void setName(final String name) {
		this.name = name;
	}

}
