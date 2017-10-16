package au.com.scds.chats.dom.dex.reference;

import org.apache.isis.applib.annotation.DomainObject;
import org.apache.isis.applib.annotation.Nature;

@DomainObject(objectType="chats.disabilitydescription", nature=Nature.VIEW_MODEL)
public class DisabilityDescription {

	private String description;
	
	public DisabilityDescription(){}
	
	public DisabilityDescription(String description) {
		setDescription(description);
	}
	
	public String title(){
		return getDescription();
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}	
}
