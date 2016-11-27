package au.com.scds.chats.dom;

import javax.jdo.annotations.Column;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.Inheritance;
import javax.jdo.annotations.InheritanceStrategy;
import javax.jdo.annotations.NotPersistent;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Queries;
import javax.jdo.annotations.Query;

import org.apache.isis.applib.annotation.Action;
import org.apache.isis.applib.annotation.Editing;
import org.apache.isis.applib.annotation.Optionality;
import org.apache.isis.applib.annotation.Parameter;
import org.apache.isis.applib.annotation.ParameterLayout;
import org.apache.isis.applib.annotation.Property;
import org.joda.time.DateTime;
import org.joda.time.Duration;
import org.joda.time.Period;

/**
 * An interval as represented by a start and finish date-times on the same date
 * 
 * Implements time accounting requirements
 * 
 */
@PersistenceCapable()
@Inheritance(strategy = InheritanceStrategy.SUBCLASS_TABLE)
public abstract class StartAndFinishDateTime extends AbstractChatsDomainEntity {

	protected DateTime startDateTime;
	protected DateTime endDateTime;

	@Property(editing = Editing.DISABLED)
	@Column(allowsNull = "true")
	public DateTime getStartDateTime() {
		return startDateTime;
	}

	public void setStartDateTime(final DateTime startDateTime) {
		this.startDateTime = startDateTime;
	}

	@Property(editing = Editing.DISABLED)
	@Column(allowsNull = "true")
	public DateTime getEndDateTime() {
		return endDateTime;
	}

	public void setEndDateTime(final DateTime endDateTime) {
		this.endDateTime = endDateTime;
	}

	@NotPersistent
	public String getIntervalLength() {
		if (getStartDateTime() != null && getEndDateTime() != null) {
			Duration duration = new Duration(getStartDateTime(), getEndDateTime());
			Long hours = duration.getStandardHours();
			Long minutes = duration.getStandardMinutes();
			if (hours > 0)
				minutes = minutes - hours * 60;
			return String.format("%01d:%02d", hours, minutes);

		} else
			return null;
	}

	public static String validateStartAndEndDateTimes(DateTime start, DateTime end) {
		if (start != null && end != null) {
			if (end.isBefore(start) || end.equals(start))
				return "End is before or equal to Start";
			else {
				Duration duration = new Duration(start, end);
				if (duration.getStandardMinutes() == 0)
					return "End is equal to Start";
				if(duration.getStandardHours()>12)
					return "End and Start are not in the same 12 hour period";
				if (end.getDayOfWeek() != start.getDayOfWeek()) {
					return "End and Start are on different days of the week";
				}
			}
		}
		return null;
	}
	
	@Action()
	public StartAndFinishDateTime updateStartDateTime(
			@Parameter(optionality = Optionality.MANDATORY) @ParameterLayout(named = "Start Time") DateTime start) {
		setStartDateTime(trimSeconds(start));
		return this;
	}

	public DateTime default0UpdateStartDateTime() {
		if (getStartDateTime() == null)
			return getEndDateTime();
		else
			return getStartDateTime();
	}

	public String validateUpdateStartDateTime(DateTime start) {
		return validateStartAndEndDateTimes(start, getEndDateTime());
	}

	@Action()
	public StartAndFinishDateTime updateEndDateTime(
			@Parameter(optionality = Optionality.MANDATORY) @ParameterLayout(named = "End Time") DateTime end) {
		setEndDateTime(trimSeconds(end));
		return this;
	}

	public DateTime default0UpdateEndDateTime() {
		if (getEndDateTime() == null)
			return getStartDateTime();
		else
			return getEndDateTime();
	}

	public String validateUpdateEndDateTime(DateTime end) {
		return validateStartAndEndDateTimes(getStartDateTime(), end);
	}

	@Action()
	public StartAndFinishDateTime updateEndDateTimeOffStart(
			@Parameter(optionality = Optionality.MANDATORY) @ParameterLayout(named = "Add N Minutes to Start Date Time") Integer minutes) {
		setEndDateTime(getStartDateTime().plusMinutes(minutes));
		return this;
	}

	public String disableUpdateEndDateTimeOffStart() {
		if (getStartDateTime() == null)
			return "Start Date Time is Not Set";
		else
			return null;
	}
	
	protected DateTime trimSeconds(DateTime dateTime){
		final DateTime hour = dateTime.hourOfDay().roundFloorCopy();
	    final long millisSinceHour = new Duration(hour, dateTime).getMillis();
	    final int roundedMinutes = ((int)Math.round(millisSinceHour / 60000.0 ));
	    return hour.plusMinutes(roundedMinutes);
	}
}
