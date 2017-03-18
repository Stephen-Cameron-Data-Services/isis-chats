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
	public String getIntervalLengthFormatted() {
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

	@NotPersistent
	public Long getIntervalLengthInMinutes() {
		if (getStartDateTime() != null && getEndDateTime() != null) {
			Duration duration = new Duration(getStartDateTime(), getEndDateTime());
			return duration.getStandardMinutes();
		} else
			return null;
	}

	public static String validateStartAndFinishDateTimes(DateTime start, DateTime finish) {
		if (start != null && finish != null) {
			if (finish.isBefore(start) || finish.equals(start))
				return "End is before or equal to Start";
			else {
				Duration duration = new Duration(start, finish);
				if (duration.getStandardMinutes() == 0)
					return "End is equal to Start";
				if (duration.getStandardHours() > 12)
					return "End and Start are not in the same 12 hour period";
				if (finish.getDayOfWeek() != start.getDayOfWeek()) {
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
		return validateStartAndFinishDateTimes(start, getEndDateTime());
	}

	// NOTE Must keep end date time optional to be able to change start date
	// time to anything
	@Action()
	public StartAndFinishDateTime updateEndDateTime(
			@Parameter(optionality = Optionality.OPTIONAL) @ParameterLayout(named = "End Time") DateTime end) {
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
		return validateStartAndFinishDateTimes(getStartDateTime(), end);
	}

	@Action()
	public StartAndFinishDateTime updateEndDateTimeOffStart(
			@Parameter(optionality = Optionality.MANDATORY) @ParameterLayout(named = "Add N Minutes to Start Date Time") Integer minutes) {
		setEndDateTime(getStartDateTime().plusMinutes(minutes));
		return this;
	}

	public String validateUpdateEndDateTimeOffStart(Integer minutes) {
		return validateStartAndFinishDateTimes(getStartDateTime(), getStartDateTime().plusMinutes(minutes));
	}

	public String disableUpdateEndDateTimeOffStart() {
		if (getStartDateTime() == null)
			return "Start Date Time is Not Set";
		else
			return null;
	}

	protected DateTime trimSeconds(DateTime dateTime) {
		if (dateTime == null)
			return null;
		final DateTime hour = dateTime.hourOfDay().roundFloorCopy();
		final long millisSinceHour = new Duration(hour, dateTime).getMillis();
		final int roundedMinutes = ((int) Math.round(millisSinceHour / 60000.0));
		return hour.plusMinutes(roundedMinutes);
	}
}
