package au.com.scds.chats.integtest;

import java.time.Month;
import java.util.List;

import javax.inject.Inject;

import org.apache.isis.applib.fixturescripts.FixtureScripts;
import org.apache.isis.applib.services.xactn.TransactionService;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import au.com.scds.chats.dom.general.names.Regions;
import au.com.scds.chats.dom.report.DEXReporting;

import au.com.scds.chats.fixture.scenarios.CreateDexReportActivities;
import au.com.scds.chats.fixture.seed.CreateRegions;
import au.com.scds.chats.fixture.seed.CreateTransportTypes;
import au.com.scds.chats.report.view.ActivityAttendanceSummary;
import au.com.scds.chats.report.view.ParticipantActivityByMonthForDEX;

import static org.assertj.core.api.Assertions.assertThat;

public class DEXReportingTests extends IntegTestAbstract {

	@Inject
	FixtureScripts fixtureScripts;
	@Inject
	TransactionService transactionService;
	@Inject
	DEXReporting dexReportingMenu;
	@Inject
	Regions regions;

	private static Boolean setupDone = false;

	@Before
	public void setUp() throws Exception {
		if (!setupDone) {
			// given
			fixtureScripts.runFixtureScript(new CreateRegions(), null);
			fixtureScripts.runFixtureScript(new CreateTransportTypes(), null);
			CreateDexReportActivities fs = new CreateDexReportActivities();
			fixtureScripts.runFixtureScript(fs, null);
			transactionService.nextTransaction();
			setupDone = true;
		}
	}

	public static class Tests extends DEXReportingTests {

		@Test
		public void ReportingMenu_listAttendanceByYearMonthAndRegion() throws Exception {
			// then
			List<ParticipantActivityByMonthForDEX> list = dexReportingMenu.listAttendanceByYearMonthAndRegion("200112",
					regions.regionForName("SOUTH"));
			assertThat(list.size()).isEqualTo(6);
		}

		@Test
		public void ReportingMenu_checkAttendanceDataForMonth() throws Exception {
			// then
			List<ActivityAttendanceSummary> list = dexReportingMenu.checkAttendanceDataForMonth(2001, Month.DECEMBER,
					"SOUTH");
			assertThat(list.size()).isEqualTo(2);
			ActivityAttendanceSummary a1 = list.get(0);
			assertThat(a1.getActivityName()).isNotNull();
			assertThat(a1.getHasStartAndEndDateTimesCount()).isEqualTo(3);
			assertThat(a1.getHasArrivingAndDepartingTransportCount()).isEqualTo(3);
			ActivityAttendanceSummary a2 = list.get(1);
			assertThat(a2.getActivityName()).isNotNull();
			assertThat(a2.getHasStartAndEndDateTimesCount()).isEqualTo(3);
			assertThat(a2.getHasArrivingAndDepartingTransportCount()).isEqualTo(1);
		}

		@Test
		public void DEXReporting_findAttendanceSummary() throws Exception {
			// see above DEXReportingTests_checkAttendanceDataForMonth
		}

		@Test
		public void DEXReporting_findAttendances() throws Exception {

		}

		@Test
		public void DEXReporting_findVolunteeredTimes() throws Exception {

		}

		@Test
		public void DEXReporting_findCalls() throws Exception {

		}
	}
}
