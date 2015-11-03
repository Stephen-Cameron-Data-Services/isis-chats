package au.com.scds.chats.dom.module.general.names;

import javax.jdo.annotations.Column;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;

@PersistenceCapable(identityType = IdentityType.DATASTORE)
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
