package au.com.scds.chats.dom.module.general.names;

import javax.jdo.annotations.Column;
import javax.jdo.annotations.IdentityType;

@javax.jdo.annotations.PersistenceCapable(identityType = IdentityType.DATASTORE)
@javax.jdo.annotations.DatastoreIdentity(strategy = javax.jdo.annotations.IdGeneratorStrategy.IDENTITY, column = "id")
public class EnglishSkill {

	private String name;

	@Column(allowsNull="false")
	public String getEnglishSkill() {
		return name;
	}

	public void setEnglishSkill(final String name) {
		this.name = name;
	}

}
