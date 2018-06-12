package au.com.scds.chats.dom.general.names;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.apache.isis.applib.DomainObjectContainer;
import org.apache.isis.applib.annotation.Action;
import org.apache.isis.applib.annotation.ActionLayout;
import org.apache.isis.applib.annotation.BookmarkPolicy;
import org.apache.isis.applib.annotation.CollectionLayout;
import org.apache.isis.applib.annotation.MemberOrder;
import org.apache.isis.applib.annotation.Optionality;
import org.apache.isis.applib.annotation.Parameter;
import org.apache.isis.applib.annotation.ParameterLayout;
import org.apache.isis.applib.annotation.Programmatic;
import org.apache.isis.applib.annotation.Property;
import org.apache.isis.applib.annotation.RenderType;
import org.apache.isis.applib.annotation.SemanticsOf;
import org.apache.isis.applib.annotation.ViewModel;
import org.apache.isis.applib.query.QueryDefault;

import au.com.scds.chats.dom.general.Suburb;
import au.com.scds.chats.dom.general.Suburbs;
import au.com.scds.chats.dom.volunteer.VolunteerRole;
import au.com.scds.chats.dom.volunteer.VolunteerRoles;

@ViewModel()
public class Names {
	
	public String title(){
		return "Name Lists";
	}

	@CollectionLayout(render=RenderType.EAGERLY)
	public List<Region> getRegions() {
		return regions.listAllRegions();
	}

	public Names createRegion(final @Parameter(optionality=Optionality.MANDATORY) @ParameterLayout(named = "Region Name") String name) {
		regions.createRegion(name);
		return this;
	}
	
	@CollectionLayout(render=RenderType.EAGERLY)
	public List<ActivityType> getActivityTypes() {
		return activityTypes.listAllActivityTypes();
	}

	public Names createActivityType(final @Parameter(optionality=Optionality.MANDATORY) @ParameterLayout(named = "ActivityType Name") String name) {
		activityTypes.createActivityType(name);
		return this;
	}
	
	@CollectionLayout(render=RenderType.EAGERLY)
	public List<TransportType> getTransportTypes() {
		return transportTypes.listAllTransportTypes();
	}

	public Names createTransportType(final @Parameter(optionality=Optionality.MANDATORY) @ParameterLayout(named = "TransportType Name") String name) {
		transportTypes.createTransportType(name);
		return this;
	}
	
	@CollectionLayout(render=RenderType.EAGERLY)
	public List<Salutation> getSalutations() {
		return salutations.listAllSalutations();
	}

	public Names createSalutation(final @Parameter(optionality=Optionality.MANDATORY) @ParameterLayout(named = "Salutation Name") String name) {
		salutations.createSalutation(name);
		return this;
	}
	
	@CollectionLayout(render=RenderType.EAGERLY)
	public List<VolunteerRole> getVolunteerRoles() {
		return volunteerRoles.listAllVolunteerRoles();
	}

	public Names createVolunteerRole(final @Parameter(optionality=Optionality.MANDATORY) @ParameterLayout(named = "VolunteerRole Name") String name) {
		volunteerRoles.createVolunteerRole(name);
		return this;
	}
	
	@CollectionLayout(render=RenderType.EAGERLY)
	public List<Suburb> getSuburbs() {
		return suburbs.listAllSuburbs();
	}

	public Names createSuburb(final @Parameter(optionality=Optionality.MANDATORY) @ParameterLayout(named = "Suburb Name") String name,
			final @Parameter(optionality=Optionality.MANDATORY) @ParameterLayout(named = "Postcode") Integer postcode) {
		suburbs.createSuburb(name, postcode);
		return this;
	}

	@Inject
	Regions regions;
	
	@Inject
	ActivityTypes activityTypes;
	
	@Inject
	TransportTypes transportTypes;
	
	@Inject
	Salutations salutations;
	
	@Inject
	VolunteerRoles volunteerRoles;
	
	@Inject
	Suburbs suburbs;
}
