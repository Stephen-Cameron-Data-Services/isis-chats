package au.com.scds.chats.dom;

import org.joda.time.DateTime;

import au.com.scds.chats.dom.general.names.Region;

public interface ChatsEntity {

	public String getCreatedBy();

	public DateTime getCreatedOn();

	public String getLastModifiedBy();

	public DateTime getLastModifiedOn();

	public Region getRegion();
	
	public void setCreatedBy(String createdBy);

	public void setCreatedOn(DateTime createdOn);
	
	public void setLastModifiedBy(String lastModifiedBy);

	public void setLastModifiedOn(DateTime lastModifiedOn);
	
	public void setRegion(Region region);

}
