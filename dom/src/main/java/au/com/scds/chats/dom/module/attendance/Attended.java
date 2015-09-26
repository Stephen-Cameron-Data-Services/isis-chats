package au.com.scds.chats.dom.module.attendance;

import java.util.List;

import javax.jdo.annotations.Column;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;

import org.apache.isis.applib.annotation.Action;
import org.apache.isis.applib.annotation.BookmarkPolicy;
import org.apache.isis.applib.annotation.DomainObjectLayout;
import org.apache.isis.applib.annotation.Editing;
import org.apache.isis.applib.annotation.InvokeOn;
import org.apache.isis.applib.annotation.LabelPosition;
import org.apache.isis.applib.annotation.MemberGroupLayout;
import org.apache.isis.applib.annotation.MemberOrder;
import org.apache.isis.applib.annotation.Property;
import org.apache.isis.applib.annotation.PropertyLayout;
import org.apache.isis.applib.annotation.Where;
import org.apache.isis.applib.services.actinvoc.ActionInvocationContext;

import au.com.scds.chats.dom.AbstractDomainEntity;
import au.com.scds.chats.dom.module.activity.ActivityEvent;
import au.com.scds.chats.dom.module.participant.Participant;

@PersistenceCapable(identityType = IdentityType.DATASTORE)
@DomainObjectLayout(bookmarking = BookmarkPolicy.NEVER)
@MemberGroupLayout(columnSpans = { 6, 3, 0, 3 }, left = "General", middle = { "Admin" })
public class Attended extends AbstractDomainEntity implements Comparable<Attended>{

	public String title(){
		return getParticipant().getFullName() + (getAttended() ? " did attend " : " did NOT attend ") + getActivity().getName() + " on " + getActivity().getStartDateTime().toString("dd MMMM yyyy");
	}
	
	// {{ Activity (property)
	private ActivityEvent activity;

	@Column(allowsNull = "false")
	@Property(editing = Editing.DISABLED, editingDisabledReason = "This is a non-modifiable property")
	@PropertyLayout(hidden=Where.ALL_TABLES)
	@MemberOrder(sequence = "1")
	public ActivityEvent getActivity() {
		return activity;
	}

	void setActivity(final ActivityEvent activity) {
		if (activity == null || this.activity != null)
			return;
		this.activity = activity;
	}

	// }}

	// {{ Participant (property)
	private Participant participant;

	@Column(allowsNull = "false")
	@Property(editing = Editing.DISABLED, editingDisabledReason = "This is a non-modifiable property")
	@MemberOrder(sequence = "2")
	public Participant getParticipant() {
		return participant;
	}

	void setParticipant(final Participant participant) {
		this.participant = participant;
	}

	// }}

	// {{ Attended (property)
	private Boolean attended = false;

	@Column(allowsNull = "false")
	@MemberOrder(sequence = "3")
	public Boolean getAttended() {
		return attended;
	}

	void setAttended(final Boolean attended) {
		this.attended = attended;
	}
	// }}
	
	// {{ Content (property)
	private String comments;

	@Column(allowsNull = "true")
	@MemberOrder(sequence = "4")
	@PropertyLayout(multiLine = 20, labelPosition = LabelPosition.TOP, hidden = Where.ALL_TABLES)
	public String getComments() {
		return comments;
	}

	public void setComments(final String contents) {
		this.comments = contents;
	}

	@Override
	public int compareTo(Attended o) {
		return getParticipant().getPerson().compareTo(o.getParticipant().getPerson());
	}

	// }}
	
    //region > previous (action)
    @Action(invokeOn = InvokeOn.OBJECT_AND_COLLECTION)
    public Attended Attended() {
        if(!getAttended())
        	setAttended(true);
        return actionInvocationContext.getInvokedOn().isCollection()? null: this;
    }
    
    @Action(invokeOn = InvokeOn.OBJECT_AND_COLLECTION)
    public Attended NotAttended() {
        if(getAttended())
        	setAttended(false);
        return actionInvocationContext.getInvokedOn().isCollection()? null: this;
    }
    
    @Action(invokeOn = InvokeOn.OBJECT_ONLY)
    public AttendanceList Delete() {
    	AttendanceList attendances = getActivity().getAttendances();
    	attendances.removeAttended(this);
        return actionInvocationContext.getInvokedOn().isCollection()? null: attendances;
    }
    /**
     * public only so can be injected from integ tests
     */
    @javax.inject.Inject
    private ActionInvocationContext actionInvocationContext;

}
