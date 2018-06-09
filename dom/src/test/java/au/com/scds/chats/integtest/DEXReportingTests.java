package au.com.scds.chats.integtest;

import java.time.Month;
import java.util.List;

import javax.inject.Inject;

import org.apache.isis.applib.fixturescripts.FixtureScripts;
import org.apache.isis.applib.services.xactn.TransactionService;
import org.junit.Before;
import org.junit.Test;

import au.com.scds.chats.dom.activity.ChatsAttendance;
import au.com.scds.chats.dom.general.names.Regions;
import au.com.scds.chats.dom.report.DEXReporting;
import au.com.scds.chats.fixture.scenarios.CreateChatsActivities;
import au.com.scds.chats.fixture.scenarios.CreateDexReportActivities;
import au.com.scds.chats.fixture.seed.CreateRegions;
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

	@Before
	public void setUp() throws Exception {
		// given
		fixtureScripts.runFixtureScript(new CreateRegions(), null);
		CreateDexReportActivities fs = new CreateDexReportActivities();
		fixtureScripts.runFixtureScript(fs, null);
		transactionService.nextTransaction();
	}

	public static class DEXReportingTests_listAttendanceByYearMonthAndRegion extends DEXReportingTests {

		@Test
		public void listAttendanceByYearMonthAndRegion() throws Exception {
			// then
			List<ParticipantActivityByMonthForDEX> list = dexReportingMenu.listAttendanceByYearMonthAndRegion("200112",
					regions.regionForName("SOUTH"));
			assertThat(list.size()).isEqualTo(1);
		}
	}
	
	public static class DEXReportingTests_checkAttendanceDataForMonth extends DEXReportingTests {

		@Test
		public void checkAttendanceDataForMonth() throws Exception {
			// then
			List<ActivityAttendanceSummary> list = dexReportingMenu.checkAttendanceDataForMonth(2001, Month.DECEMBER, "SOUTH");
			assertThat(list.size()).isEqualTo(2);
			ActivityAttendanceSummary a1 = list.get(0);
			assertThat(a1.getActivityName()).isNotNull();
			assertThat(a1.getHasStartAndEndDateTimesCount()).isEqualTo(3);
			ActivityAttendanceSummary a2 = list.get(1);
			assertThat(a2.getActivityName()).isNotNull();
			assertThat(a2.getHasStartAndEndDateTimesCount()).isEqualTo(3);
		}
	}
}
