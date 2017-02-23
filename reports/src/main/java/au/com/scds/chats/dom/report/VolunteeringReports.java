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
package au.com.scds.chats.dom.report;

import java.util.List;

import javax.inject.Inject;

import org.apache.isis.applib.DomainObjectContainer;
import org.apache.isis.applib.annotation.DomainService;
import org.apache.isis.applib.annotation.DomainServiceLayout;
import org.apache.isis.applib.annotation.NatureOfService;
import org.apache.isis.applib.annotation.DomainServiceLayout.MenuBar;
import org.apache.isis.applib.query.QueryDefault;

import au.com.scds.chats.dom.report.view.VolunteeredTimeByVolunteerAndRoleAndYearMonth;
import au.com.scds.chats.dom.report.view.VolunteeredTimeForActivityByVolunteerAndRoleAndYearMonth;
import au.com.scds.chats.dom.report.view.VolunteeredTimeForActivityByYearMonth;
import au.com.scds.chats.dom.report.view.VolunteeredTimeForCallsByVolunteerAndYearMonth;
import au.com.scds.chats.dom.report.view.VolunteeredTimeForCallsByYearMonth;

//@DomainService(nature=NatureOfService.VIEW_MENU_ONLY)
//@DomainServiceLayout(menuBar = MenuBar.PRIMARY, named = "Reports", menuOrder = "70.1")
public class VolunteeringReports {
	
	public List<VolunteeredTimeByVolunteerAndRoleAndYearMonth> volunteeredTimeByMonth(){
		return container.allMatches(new QueryDefault<>(VolunteeredTimeByVolunteerAndRoleAndYearMonth.class,"allVolunteeredTimeByVolunteerAndRoleAndYearMonth"));
	}
	
	public List<VolunteeredTimeForActivityByYearMonth> volunteeredTimeForActivitiesByMonth(){
		return container.allMatches(new QueryDefault<>(VolunteeredTimeForActivityByYearMonth.class,"allVolunteeredTimeForActivityByYearMonth"));
	}
	
	public List<VolunteeredTimeForActivityByVolunteerAndRoleAndYearMonth> volunteeredTimeForActivitiesByVolunteerAndMonth(){
		return container.allMatches(new QueryDefault<>(VolunteeredTimeForActivityByVolunteerAndRoleAndYearMonth.class,"allVolunteeredTimeForActivityByVolunteerAndRoleAndYearMonth"));
	}
	
	public List<VolunteeredTimeForCallsByYearMonth> volunteeredTimeForCallsByMonth(){
		return container.allMatches(new QueryDefault<>(VolunteeredTimeForCallsByYearMonth.class,"allVolunteeredTimeForCallsByYearMonth"));
	}
	
	public List<VolunteeredTimeForCallsByVolunteerAndYearMonth> volunteeredTimeForCallsByVolunteerAndMonth(){
		return container.allMatches(new QueryDefault<>(VolunteeredTimeForCallsByVolunteerAndYearMonth.class,"allVolunteeredTimeForCallsByVolunteerAndYearMonth"));
	}
	
	@Inject
	DomainObjectContainer container;
}