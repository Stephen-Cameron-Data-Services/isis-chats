package au.com.scds.chats.dom.activity;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.jdo.annotations.Column;
import javax.jdo.annotations.Discriminator;
import javax.jdo.annotations.Inheritance;
import javax.jdo.annotations.InheritanceStrategy;
import javax.jdo.annotations.Join;
import javax.jdo.annotations.NotPersistent;
import javax.jdo.annotations.PersistenceCapable;

import org.apache.isis.applib.annotation.DomainObject;
import org.apache.isis.applib.annotation.Editing;
import org.apache.isis.applib.annotation.Property;
import org.apache.isis.applib.annotation.Where;

import au.com.scds.chats.dom.general.names.ActivityType;
import au.com.scds.chats.dom.participant.Participant;
import au.com.scds.chats.dom.participant.Participation;

@DomainObject(objectType = "PACTIVITY")
@PersistenceCapable()
@Inheritance(strategy = InheritanceStrategy.SUPERCLASS_TABLE)
@Discriminator(value = "PACTIVITY")
public class ParentedActivityEvent extends ActivityEvent {

	protected RecurringActivity parentActivity;
	@Join
	private List<Participation> ignored = new ArrayList<>();

	@Property(editing = Editing.DISABLED, editingDisabledReason = "This Activity belongs to its parent Recurring Activity")
	@Column(allowsNull = "true")
	public final RecurringActivity getParentActivity() {
		return parentActivity;
	}

	public void setParentActivity(final RecurringActivity activity) {
		this.parentActivity = activity;
	}

	public List<Participation> getIgnored() {
		return ignored;
	}

	public void setIgnored(List<Participation> ignored) {
		this.ignored = ignored;
	}

	/**
	 * Adds a parent Participation to the 'ignored' list,
	 */
	public ParentedActivityEvent ignoreParentParticipation(Participation participation) {
		getIgnored().add(participation);
		return this;
	}

	public List<Participation> choices0IgnoreParentParticipation() {
		ArrayList<Participation> list = new ArrayList<>();
		for (Participation p : getParentActivity().getParticipations()) {
			if (!isIgnored(p))
				list.add(p);
		}
		return list;
	}

	/**
	 * Returns an ignored parent Participation back into to the actual list
	 */
	public ParentedActivityEvent returnIgnoredParentParticipation(Participation participation) {
		getIgnored().remove(participation);
		return this;
	}

	public List<Participation> choices0ReturnIgnoredParentParticipation() {
		return getIgnored();
	}

	/**
	 * Check if a Participation is ignored.
	 */
	public boolean isIgnored(Participation participation) {
		return getIgnored().contains(participation);
	}

	// >>> Overrides <<< //
	@Override
	public String title() {
		if (getParentActivity() != null && super.getName() == null) {
			return getParentActivity().getName();
		}
		return super.title();
	}

	/**
	 * List of Participations for an ActivityEvent is the list of its parent
	 * RecurringActivity, if present, plus any locally added.
	 * 
	 * Note: this Participation list is used to generate a Participant list in
	 * {@link Activity#getParticipants()}.
	 */
	@Override
	public SortedSet<Participation> getParticipations() {
		SortedSet<Participation> list = new TreeSet<Participation>();
		if (getParentActivity() != null) {
			for (Participation p : getParentActivity().getParticipations()) {
				if (!isIgnored(p))
					list.add(p);
			}
		}
		for (Participation p : super.getParticipations()) {
			list.add(p);
		}
		return list;
	}

	/**
	 * Participants & Participations lists are combined list from child and
	 * parent Activity, but only want to remove Participants from child list.
	 * 
	 * Called by Participants#deletedParticipation()
	 */
	@Override
	public void removeParticipation(Participation participation) {
		if (super.getParticipations().contains(participation))
			super.getParticipations().remove(participation);
	}

	/**
	 * Participants & Participations lists are combined list from child and
	 * parent Activity, but only want to remove Participants from child list.
	 */
	@Override
	public ActivityEvent removeParticipant(final Participant participant) {
		if (participant == null)
			return this;
		for (Participation p : super.getParticipations()) {
			if (p.getParticipant().equals(participant)) {
				participantsRepo.deleteParticipation(p);
				break;
			}
		}
		return this;
	}

	/**
	 * Only want to remove Participants from local list.
	 */
	@Override
	public List<Participant> choices0RemoveParticipant() {
		List<Participant> list = new ArrayList();
		for (Participation p : super.getParticipations()) {
			list.add(p.getParticipant());
		}
		return list;
	}

	@Override
	@NotPersistent
	public ActivityType getActivityType() {
		if (getParentActivity() != null && super.getActivityType() == null) {
			return getParentActivity().getActivityType();
		}
		return super.getActivityType();
	}

	@Override
	@NotPersistent
	public String getCostForParticipant() {
		if (getParentActivity() != null && super.getCostForParticipant() == null) {
			return getParentActivity().getCostForParticipant();
		}
		return super.getCostForParticipant();
	}

	@Override
	@NotPersistent
	public String getDescription() {
		if (getParentActivity() != null && super.getDescription() == null) {
			return getParentActivity().getDescription();
		}
		return super.getDescription();
	}

	@Override
	@NotPersistent
	public String getAddressLocationName() {
		if (getParentActivity() != null && super.getAddressLocationName() == null) {
			return getParentActivity().getAddressLocationName();
		}
		return super.getAddressLocationName();
	}

	@Override
	@NotPersistent
	public String getStreetAddress() {
		if (getParentActivity() != null && super.getStreetAddress() == null) {
			return getParentActivity().getStreetAddress();
		}
		return super.getStreetAddress();
	}

	@Override
	@NotPersistent
	public Integer getCutoffLimit() {
		if (getParentActivity() != null && super.getCutoffLimit() == null) {
			return getParentActivity().getCutoffLimit();
		}
		return super.getCutoffLimit();
	}

	@Override
	@NotPersistent
	public org.isisaddons.wicket.gmap3.cpt.applib.Location getLocation() {
		if (getParentActivity() != null && super.getLocation() == null) {
			return getParentActivity().getLocation();
		}
		return super.getLocation();
	}

}
