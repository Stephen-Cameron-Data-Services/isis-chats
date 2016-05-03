package au.com.scds.chats.dom.scratch;

import java.util.List;

import javax.inject.Inject;

import org.apache.isis.applib.DomainObjectContainer;
import org.apache.isis.applib.annotation.Action;
import org.apache.isis.applib.annotation.DomainService;
import org.apache.isis.applib.annotation.DomainServiceLayout;
import org.apache.isis.applib.annotation.MemberOrder;
import org.apache.isis.applib.annotation.NatureOfService;
import org.apache.isis.applib.annotation.DomainServiceLayout.MenuBar;

//@DomainService(repositoryFor = Place.class,nature=NatureOfService.VIEW_MENU_ONLY)
//@DomainServiceLayout(menuBar = MenuBar.SECONDARY, named = "Administration", menuOrder = "1000")
public class Places {
	
	@Action()
	@MemberOrder(sequence = "1")
	public List<Place> listAllPlaces() {
		List<Place> list = container.allInstances(Place.class);
		return list;
	}
	
	@Action()
	@MemberOrder(sequence = "2")
	public Place createPlace(){
		Place place = container.newTransientInstance(Place.class);
		container.persist(place);
		return place;	
	}
	
	@Inject
	DomainObjectContainer container;

}
