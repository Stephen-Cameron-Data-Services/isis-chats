package au.com.scds.chats.dom.volunteer;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.apache.isis.applib.annotation.Action;
import org.apache.isis.applib.annotation.ActionLayout;
import org.apache.isis.applib.annotation.CollectionLayout;
import org.apache.isis.applib.annotation.Contributed;
import org.apache.isis.applib.annotation.Mixin;
import org.apache.isis.applib.annotation.Programmatic;
import org.apache.isis.applib.annotation.SemanticsOf;
import org.joda.time.DateTime;

import au.com.scds.chats.dom.activity.ChatsParticipation;
import au.com.scds.chats.dom.activity.TransportView;
import au.com.scds.eventschedule.base.impl.activity.ActivityEvent;
import au.com.scds.eventschedule.base.impl.activity.Participation;

public class VolunteersForChatsActivityEventMixins {
	
	@Mixin
	public static class ActivityEvent_volunteeredTimes {

		private final ActivityEvent activity;

		public ActivityEvent_volunteeredTimes(ActivityEvent activity) {
			this.activity = activity;
		}

	    @Action(semantics=SemanticsOf.SAFE)                     
	    @ActionLayout(contributed=Contributed.AS_ASSOCIATION)   
	    @CollectionLayout(defaultView="table")
		public List<VolunteeredTimeForActivity> $$() {
	    	return volunteersRepo.listVolunteeredTimeForActivity(this.activity);
		}

		@Inject
		public VolunteersMenu volunteersRepo;
	}
	
	@Mixin
	public static class ActivityEvent_addVolunteeredTime {

		private final ActivityEvent activity;

		public ActivityEvent_addVolunteeredTime(ActivityEvent activity) {
			this.activity = activity;
		}

	    @Action()                     
	    @ActionLayout(contributed=Contributed.AS_ACTION)   
		public ActivityEvent $$(Volunteer volunteer, DateTime start, DateTime end)  {
			VolunteeredTimeForActivity time = volunteersRepo.createVolunteeredTimeForActivity(volunteer, this.activity, start, end);
	    	return this.activity;
		}
	    
		public List<Volunteer> choices0$$() {
			return volunteersRepo.listActiveVolunteers();
		}

		public List<VolunteerRole> choices1$$() {
			return volunteersRepo.listVolunteerRoles();
		}

		@Inject
		public VolunteersMenu volunteersRepo;
	}
	
	@Mixin
	public static class ActivityEvent_removeVolunteeredTime {

		private final ActivityEvent activity;

		public ActivityEvent_removeVolunteeredTime(ActivityEvent activity) {
			this.activity = activity;
		}

	    @Action()                     
	    @ActionLayout(contributed=Contributed.AS_ACTION)  
		public ActivityEvent $$(VolunteeredTimeForActivity time)  {
			volunteersRepo.deleteVolunteeredTimeForActivity(time);
	    	return this.activity;
		}
	    
		public List<VolunteeredTimeForActivity> choices0$$() {
			return volunteersRepo.getVolunteeredTimesForActivity(this.activity);
		}

		@Inject
		public VolunteersMenu volunteersRepo;
	}
	
	@Mixin
	public static class ActivityEvent_updateAllVolunteeredTimesToDefaults {

		private final ActivityEvent activity;

		public ActivityEvent_updateAllVolunteeredTimesToDefaults(ActivityEvent activity) {
			this.activity = activity;
		}

	    @Action()                     
	    @ActionLayout(contributed=Contributed.AS_ACTION)  
		public ActivityEvent $$()  {
			for (VolunteeredTimeForActivity time : volunteersRepo.getVolunteeredTimesForActivity(this.activity)) {
				time.updateStartDateTime(activity.getStart());
				time.updateEndDateTime(activity.getEnd());
			}
	    	return this.activity;
		}
	    
		@Inject
		public VolunteersMenu volunteersRepo;
	}
	
	@Mixin
	public static class ActivityEvent_showTransportList {

		private final ActivityEvent activity;

		public ActivityEvent_showTransportList(ActivityEvent activity) {
			this.activity = activity;
		}

	    @Action()                     
	    @ActionLayout(contributed=Contributed.AS_ACTION)  
		public List<TransportView> $$()  {
			List<TransportView> list = new ArrayList<>();
			for (Participation p : this.activity.getParticipations()) {
				list.add(new TransportView((ChatsParticipation)p));
			}
			for (VolunteeredTimeForActivity v : volunteersRepo.getVolunteeredTimesForActivity(this.activity)) {
				list.add(new TransportView(v));
			}
			return list;
		}
	    
		@Inject
		public VolunteersMenu volunteersRepo;
	}
}
