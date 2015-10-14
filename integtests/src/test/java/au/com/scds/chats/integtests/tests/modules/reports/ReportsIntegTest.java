package au.com.scds.chats.integtests.tests.modules.reports;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import javax.inject.Inject;

import org.apache.isis.applib.fixturescripts.FixtureScripts;
import org.joda.time.DateTime;
import org.junit.Test;

import au.com.scds.chats.dom.module.activity.Activities;
import au.com.scds.chats.dom.module.activity.ActivityEvent;
import au.com.scds.chats.dom.module.activity.RecurringActivity;
import au.com.scds.chats.integtests.tests.DomainAppIntegTest;
import au.com.scds.chats.integtests.tests.module.activity.ActivitiesIntegTest;
//import au.com.scds.chats.dom.module.reports.ParticipantDateLastAttendedResult;
//import au.com.scds.chats.dom.module.reports.ReportsList;

public class ReportsIntegTest extends DomainAppIntegTest {
/*
    @Inject
    ReportsList reports;
    
    @Inject 
    Activities activities;

    public static class Create extends ReportsIntegTest {

        @Test
        public void happyCase() throws Exception {

            // given
        	DateTime startDateTime = new DateTime();
            RecurringActivity obj = wrap(activities).createRecurringActivity("Foobar", startDateTime);

            // when
        	List<ParticipantDateLastAttendedResult> results = wrap(reports).ParticipantsByMonthsInactiveReport(obj);

            // then
            assertThat(results.size()).isEqualTo(0);
        }
    }   */
}
