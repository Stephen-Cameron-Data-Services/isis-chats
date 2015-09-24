package au.com.scds.chats.dom.module.activity;

import java.util.List;

import org.joda.time.DateTime;

import au.com.scds.chats.dom.module.general.Location;
import au.com.scds.chats.dom.module.participant.Participant;
import au.com.scds.chats.dom.module.participant.Participation;

public interface Activity {

	public String getName();
	
	public void setName(String string);
	
	public Location getLocation();
	
	public void setLocation(Location location);
	
	public DateTime getStartDateTime();
	
	public void setStartDateTime(DateTime startDateTime);
	
	public Activity addParticipant(Participant participant);
	
	public Activity removeParticipant(Participant participant);
	
	public List<Participant> getParticipants();
	
	public List<Participation> getParticipationList();
	
}
