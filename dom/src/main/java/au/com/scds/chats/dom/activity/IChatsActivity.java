package au.com.scds.chats.dom.activity;

import org.joda.time.DateTime;

import au.com.scds.chats.dom.general.names.Region;

/*
 * A common interface for ChatsActivity and ChatsParentedActivity (excludes ChatsRecurringActivity)
 */
public interface IChatsActivity {
	
	public String getName();
	public String getCodeName();
	public Region getRegion();
	public DateTime getStart();
	public boolean isCancelled();
}
