/*
 *
 *  Copyright 2015 Stephen Cameron Data Services
 *
 *
 *  Licensed under the Apache License, Version 2.0 (the
 *  "License"); you may not use this file except in compliance
 *  with the License.  You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing,
 *  software distributed under the License is distributed on an
 *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  KIND, either express or implied.  See the License for the
 *  specific language governing permissions and limitations
 *  under the License.
 */
package au.com.scds.chats.dom.report.view;

import javax.jdo.annotations.Extension;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.Inheritance;
import javax.jdo.annotations.InheritanceStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Queries;
import javax.jdo.annotations.Query;

import org.apache.isis.applib.annotation.DomainObject;
import org.apache.isis.applib.annotation.Editing;
import org.apache.isis.applib.annotation.MemberOrder;
import org.apache.isis.applib.annotation.Programmatic;
import org.apache.isis.applib.annotation.Property;
import org.apache.isis.applib.annotation.ViewModel;
import org.isisaddons.module.security.dom.tenancy.ApplicationTenancy;
import org.isisaddons.module.security.dom.tenancy.WithApplicationTenancy;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;

@ViewModel
@DomainObject(editing = Editing.DISABLED)
@PersistenceCapable(
		identityType = IdentityType.NONDURABLE,
		table = "VolunteeredTimeForCallsByYearMonth",
		extensions = { @Extension(
				vendorName = "datanucleus",
				key = "view-definition",
				value = "CREATE VIEW VolunteeredTimeForCallsByYearMonth "
						+ "( "
						+ " {this.callScheduleYearMonth}, "
						+ " {this.hoursVolunteered}, "
						+ " {this.hoursOnCalls}, "
						+ " {this.regionName} "
						+ ") AS "
						+ "SELECT  "
						+ "  EXTRACT(YEAR_MONTH FROM calendardaycallschedule.calendardate) AS callScheduleYearMonth,  "
						+ "  ROUND(SUM(TIMESTAMPDIFF(MINUTE,volunteeredtime.startdatetime,volunteeredtime.enddatetime))/60,1) AS hoursVolunteered,  "
						+ "  ROUND(SUM(TIMESTAMPDIFF(MINUTE,telephonecall.startdatetime,telephonecall.enddatetime))/60,1) AS hoursOnCalls,"
						+ "  calendardaycallschedule.region_name  "
						+ "FROM  "
						+ "  calendardaycallschedule, "
						+ "  volunteeredtime, "
						+ "  telephonecall "
						+ "WHERE  "
						+ "  volunteeredtime.callschedule_calendardaycallschedule_id = calendardaycallschedule.calendardaycallschedule_id AND "
						+ "  telephonecall.callschedule_calendardaycallschedule_id = calendardaycallschedule.calendardaycallschedule_id "
						+ "GROUP BY  "
						+ "  EXTRACT(YEAR_MONTH FROM calendardaycallschedule.calendardate), "
						+ "  calendardaycallschedule.region_name;") })
@Queries({
		@Query(name = "allVolunteeredTimeForCallsByYearMonth",
				language = "JDOQL",
				value = "SELECT FROM au.com.scds.chats.dom.report.view.VolunteeredTimeForCallsByYearMonth") })
@Inheritance(strategy = InheritanceStrategy.NEW_TABLE)
public class VolunteeredTimeForCallsByYearMonth implements WithApplicationTenancy{

	private Integer callScheduleYearMonth;
	private Float hoursVolunteered;
	private Float hoursOnCalls;
	private String regionName;

	@Property()
	@MemberOrder(sequence = "1")
	public Integer getCallScheduleYearMonth() {
		return callScheduleYearMonth;
	}

	public void setCallScheduleYearMonth(Integer callScheduleYearMonth) {
		this.callScheduleYearMonth = callScheduleYearMonth;
	}

	@Property()
	@MemberOrder(sequence="2")
	public Float getHoursVolunteered() {
		return hoursVolunteered;
	}

	public void setHoursVolunteered(Float hoursVolunteered) {
		this.hoursVolunteered = hoursVolunteered;
	}

	@Property()
	@MemberOrder(sequence="3")	
	public Float getHoursOnCalls() {
		return hoursOnCalls;
	}

	public void setHoursOnCalls(Float hoursOnCalls) {
		this.hoursOnCalls = hoursOnCalls;
	}
	
	@Property()
	@MemberOrder(sequence = "4")
	public String getRegionName() {
		return regionName;
	}

	public void setRegionName(String region) {
		this.regionName = region;
	}

	@Programmatic
	public ApplicationTenancy getApplicationTenancy() {
		ApplicationTenancy tenancy = new ApplicationTenancy();
		if (getRegionName().equals("STATEWIDE") || getRegionName().equals("TEST"))
			tenancy.setPath("/");
		else {
			tenancy.setPath("/" + getRegionName() + "_");
		}
		return tenancy;
	}

}
