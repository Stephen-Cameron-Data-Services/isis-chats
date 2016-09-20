package au.com.scds.chats.dom.participant;

import javax.jdo.annotations.Column;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import org.apache.isis.applib.annotation.DomainObject;
import org.apache.isis.applib.annotation.Editing;
import org.apache.isis.applib.annotation.Property;

import au.com.scds.chats.dom.AbstractChatsDomainEntity;
import au.com.scds.chats.dom.activity.Activity;

@DomainObject(objectType = "WAITLISTED")
@PersistenceCapable(identityType=IdentityType.DATASTORE)
public class WaitListedParticipant extends AbstractChatsDomainEntity implements Comparable<WaitListedParticipant>{
	
	private Activity activity;
	private Participant participant;
	
	public String title(){
		return getParticipant().getFullName();
	}
	
	@Property(editing=Editing.DISABLED)
	@Column(allowsNull="false")
	public Activity getActivity() {
		return activity;
	}
	
	public void setActivity(Activity activity) {
		this.activity = activity;
	}
	
	@Property(editing=Editing.DISABLED)
	@Column(allowsNull="false")
	public Participant getParticipant() {
		return participant;
	}
	
	public void setParticipant(Participant participant) {
		this.participant = participant;
	}

	@Override
	public int compareTo(WaitListedParticipant o) {
		return getCreatedOn().compareTo(o.getCreatedOn());
	}

}
