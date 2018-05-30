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
package au.com.scds.chats.report;

import java.util.List;

import javax.inject.Inject;

import org.apache.isis.applib.annotation.Action;
import org.apache.isis.applib.annotation.DomainService;
import org.apache.isis.applib.annotation.NatureOfService;
import org.apache.isis.applib.annotation.Optionality;
import org.apache.isis.applib.annotation.Parameter;
import org.apache.isis.applib.annotation.ParameterLayout;
import org.apache.isis.applib.query.QueryDefault;
import org.apache.isis.applib.services.repository.RepositoryService;

import au.com.scds.chats.report.view.MailMergeData;

//Report
@DomainService(objectType="MailMerge", nature=NatureOfService.VIEW_MENU_ONLY)
public class MailMerge {
	
	@Action
	public List<MailMergeData> listMailMergeData(@Parameter(optionality=Optionality.MANDATORY)  @ParameterLayout(named="Mail Merge Data Group")
	MailMergeGroup group){
		switch (group){
		case Active_Participants:
			return repositoryService.allMatches(new QueryDefault<>(MailMergeData.class,"listActiveParticipantMailMergeData"));
		case Active_Volunteers:
			return repositoryService.allMatches(new QueryDefault<>(MailMergeData.class,"listActiveVolunteerMailMergeData"));
		case Inactive_Participants:
			return repositoryService.allMatches(new QueryDefault<>(MailMergeData.class,"listInactiveParticipantMailMergeData"));
		case Inactive_Volunteers:
			return repositoryService.allMatches(new QueryDefault<>(MailMergeData.class,"listInactiveVolunteerMailMergeData"));
		}
		return null;
	}
	
	private enum MailMergeGroup{
		Active_Participants,Active_Volunteers,Inactive_Participants,Inactive_Volunteers
	}
	
	@Inject
	RepositoryService repositoryService;
	
}
