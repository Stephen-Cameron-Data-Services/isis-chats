package au.com.scds.chats.dom.module.activity;

import org.joda.time.Duration;

public enum Periodicity {
	DAILY, WEEKLY, FORTNIGHTLY, MONTHLY, BIMONTHLY;
	
	private Duration duration;
	
    static {
        DAILY.duration = new Duration(24*60*60*1000);
        WEEKLY.duration = new Duration(7*24*60*60*1000);
        FORTNIGHTLY.duration = new Duration(14*24*60*60*1000);
        MONTHLY.duration = new Duration(28*24*60*60*1000);
        BIMONTHLY.duration = new Duration(56*24*60*60*1000);
    }

	public Duration getDuration() {
		return duration;
	}
}
