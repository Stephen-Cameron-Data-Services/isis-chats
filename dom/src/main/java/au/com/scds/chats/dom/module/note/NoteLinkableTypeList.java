package au.com.scds.chats.dom.module.note;

public enum NoteLinkableTypeList {
	ACTIVITY, RECURRENT_ACTIVITY, ONEOFF_ACTIVITY, ACTIVITY_EVENT, PARTICIPANT, VOLUNTEER, PERSON;

	private String title;

	static {
		ACTIVITY.title = "All Activities";
		RECURRENT_ACTIVITY.title = "Recurrent Activity";
		ACTIVITY_EVENT.title = "Activity";
		ONEOFF_ACTIVITY.title = "One-off Activity";
		PARTICIPANT.title = "Participant";
		VOLUNTEER.title = "Volunteer";
		PERSON.title = "Person";
	}

	public String getTitle() {
		return title;
	}
}
