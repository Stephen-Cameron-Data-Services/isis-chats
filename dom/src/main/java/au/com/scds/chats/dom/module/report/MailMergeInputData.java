package au.com.scds.chats.dom.module.report;

import java.util.List;

import javax.inject.Inject;
import javax.jdo.annotations.Query;

import org.apache.isis.applib.DomainObjectContainer;
import org.apache.isis.applib.annotation.DomainService;
import org.apache.isis.applib.annotation.DomainServiceLayout;
import org.apache.isis.applib.annotation.NatureOfService;
import org.apache.isis.applib.annotation.Optionality;
import org.apache.isis.applib.annotation.Parameter;
import org.apache.isis.applib.annotation.DomainServiceLayout.MenuBar;
import org.apache.isis.applib.query.QueryDefault;

import au.com.scds.chats.dom.module.general.Person;
import au.com.scds.chats.dom.module.participant.Participant;
import au.com.scds.chats.dom.module.participant.Participants;
import au.com.scds.chats.dom.module.report.view.MailMergeData;

@DomainService(nature=NatureOfService.VIEW_MENU_ONLY)
@DomainServiceLayout(menuBar = MenuBar.PRIMARY, named = "Reports", menuOrder = "70.10")
public class MailMergeInputData {

	public List<MailMergeData> allMailMergeData(){
		return container.allMatches(new QueryDefault<>(MailMergeData.class,"listMailMergeData"));
	}
	
	public List<MailMergeData> allActiveParticipantsMailMergeInputData(){
		return container.allMatches(new QueryDefault<>(MailMergeData.class,"listActiveParticipantMailMergeData"));
	}
	
	public List<MailMergeData> allActiveVolunteerMailMergeInputData(){
		return container.allMatches(new QueryDefault<>(MailMergeData.class,"listActiveVolunteerMailMergeData"));
	}
	
	@Inject
	DomainObjectContainer container;
	
}
