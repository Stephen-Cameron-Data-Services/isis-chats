package au.com.scds.chats.dom;

import javax.inject.Inject;

import org.apache.isis.applib.annotation.DomainObject;
import org.apache.isis.applib.annotation.DomainService;
import org.apache.isis.applib.annotation.NatureOfService;
import org.apache.isis.applib.annotation.Programmatic;
import org.isisaddons.module.security.dom.user.ApplicationUser;
import org.isisaddons.module.security.dom.user.ApplicationUserRepository;
import org.joda.time.DateTime;

import au.com.scds.chats.dom.general.names.Region;
import au.com.scds.chats.dom.general.names.Regions;

@DomainService(nature=NatureOfService.DOMAIN)
public class ChatsDomainEntitiesService {
	
	public void setUpdatedBy(ChatsEntity entity, String updatedBy) {
		if (entity.getCreatedBy() == null) {
			entity.setCreatedBy(updatedBy);
			//always set the region of a new record off the user tenancy.
			if (userRepository != null) {
				ApplicationUser user = userRepository.findByUsername(updatedBy);
				if (user != null && user.getAtPath() != null) {
					String name = regionNameOfApplicationUser(user);
					Region region = regions.regionForName(name);
					if (region != null)
						entity.setRegion(region);
					else
						System.out.println("Error: user's region named " + name + " not found");
				} else {
					System.out.println("Error: user tenancy not found");
				}
			} else {
				System.out.println("Error: userRepository is null, setting region to TEST");
				entity.setRegion(regions.regionForName("TEST"));
			}
		} else {
			entity.setLastModifiedBy(updatedBy);
		}
	}
	
	public String regionNameOfApplicationUser(ApplicationUser user){
		String path = user.getAtPath();
		String name = null;
		if (path.equals("/")) {
			name = "STATEWIDE";
		} else if (path.matches("^\\/[_A-Za-z0-9-]+_$")) {
			name = path.substring(1, path.length() - 1);
		}
		return name;
	}

	public void setUpdatedAt(ChatsEntity entity, java.sql.Timestamp updatedAt) {
		if (entity.getCreatedOn() == null)
			entity.setCreatedOn(new DateTime(updatedAt));
		else
			entity.setLastModifiedOn(new DateTime(updatedAt));
	}


	public String getAtPath(ChatsEntity entity) {
		if (entity.getRegion().getName().equals("STATEWIDE") || entity.getRegion().getName().equals("TEST"))
			return "/";
		else {
			return "/" + entity.getRegion().getName() + "_";
		}
	}
	
	@Inject
	protected ApplicationUserRepository userRepository;

	@Inject
	protected Regions regions;

}
