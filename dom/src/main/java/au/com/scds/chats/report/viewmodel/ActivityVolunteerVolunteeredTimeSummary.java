package au.com.scds.chats.report.viewmodel;

import org.apache.isis.applib.annotation.Programmatic;
import org.apache.isis.applib.annotation.ViewModel;
import org.isisaddons.module.security.dom.tenancy.ApplicationTenancy;
import org.isisaddons.module.security.dom.tenancy.HasAtPath;

import au.com.scds.chats.report.view.ActivityVolunteerVolunteeredTime;

@ViewModel
public class ActivityVolunteerVolunteeredTimeSummary implements HasAtPath {

	private Long volunteerId;
	private String surname;
	private String firstName;
	private String regionName;
	private String volunteerStatus;
	private Integer totalMinutes = 0;

	public ActivityVolunteerVolunteeredTimeSummary() {

	}

	public ActivityVolunteerVolunteeredTimeSummary(ActivityVolunteerVolunteeredTime time) {

		setVolunteerId(time.getVolunteerId());
		setFirstName(time.getFirstName());
		setSurname(time.getSurname());
		setRegionName(time.getRegionName());
		setVolunteerStatus(time.getVolunteerStatus());
	}

	public String title() {
		return getFirstName() + " " + getSurname();
	}

	public Long getVolunteerId() {
		return volunteerId;
	}

	public void setVolunteerId(Long volunteerId) {
		this.volunteerId = volunteerId;
	}

	public Integer getTotalMinutes() {
		return totalMinutes;
	}

	public void setTotalMinutes(Integer totalMinutes) {
		this.totalMinutes = totalMinutes;
	}

	public String getSurname() {
		return surname;
	}

	public void setSurname(String surname) {
		this.surname = surname;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getRegionName() {
		return regionName;
	}

	public void setRegionName(String region) {
		this.regionName = region;
	}

	public String getVolunteerStatus() {
		return volunteerStatus;
	}

	public void setVolunteerStatus(String volunteerStatus) {
		this.volunteerStatus = volunteerStatus;
	}

	@Programmatic
	public void addTime(ActivityVolunteerVolunteeredTime time) {
		if (time != null) {
			setTotalMinutes(getTotalMinutes() + time.getMinutes());
		}
	}

	@Override
	public String getAtPath() {
		if (getRegionName().equals("STATEWIDE"))
			return "/";
		else {
			return "/" + getRegionName() + "_";
		}
	}
}
