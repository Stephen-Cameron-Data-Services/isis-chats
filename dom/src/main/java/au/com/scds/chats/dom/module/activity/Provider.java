package au.com.scds.chats.dom.module.activity;

import javax.jdo.annotations.*;

import org.apache.isis.applib.annotation.BookmarkPolicy;
import org.apache.isis.applib.annotation.DomainObject;
import org.apache.isis.applib.annotation.DomainObjectLayout;
import org.apache.isis.applib.annotation.MemberOrder;
import org.apache.isis.applib.services.i18n.TranslatableString;

@PersistenceCapable(identityType=IdentityType.DATASTORE)
@DatastoreIdentity(
        strategy=IdGeneratorStrategy.IDENTITY,
         column="id")
@Queries({
        @Query(
                name = "listAllProviders", language = "JDOQL",
                value = "SELECT "
                        + "FROM au.com.scds.chats.dom.module.activity.Provider "),
        @Query(
                name = "findProviderByName", language = "JDOQL",
                value = "SELECT "
                        + "FROM au.com.scds.chats.dom.module.activity.Provider "
                        + "WHERE name.indexOf(:name) >= 0 ")
})
@Unique(name="Provider_name_UNQ", members = {"name"})
@DomainObject(
        objectType = "PROVIDER"
)
@DomainObjectLayout(
        bookmarking = BookmarkPolicy.AS_ROOT
)
public class Provider {
	
    //region > identificatiom
    public TranslatableString title() {
        return TranslatableString.tr("Provider: {name}", "name", getName());
    }
    //endregion
	
	// {{ Name (property)
	private String name;

	@Column(allowsNull="false", length=40)
	@MemberOrder(sequence = "1")
	public String getName() {
		return name;
	}

	public void setName(final String name) {
		this.name = name;
	}
	// }}

}
