package au.com.scds.chats.dom.user;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.jdo.listener.CreateLifecycleListener;
import javax.jdo.listener.InstanceLifecycleEvent;
import javax.jdo.listener.StoreLifecycleListener;

import org.apache.isis.applib.DomainObjectContainer;
import org.apache.isis.applib.annotation.DomainService;
import org.apache.isis.applib.annotation.NatureOfService;
import org.apache.isis.applib.annotation.Programmatic;
import org.apache.isis.applib.security.UserMemento;
import org.apache.isis.applib.services.clock.ClockService;
import org.apache.isis.applib.services.jdosupport.IsisJdoSupport;
import org.isisaddons.module.security.dom.user.ApplicationUser;
import org.isisaddons.module.security.dom.user.ApplicationUsers;

import au.com.scds.chats.dom.AbstractTenantedDomainEntity;
import au.com.scds.chats.dom.module.general.names.Region;
import au.com.scds.chats.dom.module.general.names.Regions;

//@DomainService(nature = NatureOfService.DOMAIN)
public class UpdateableEntityServices implements CreateLifecycleListener, StoreLifecycleListener {

	@PostConstruct
	public void open() {
		System.out.println("open");
		isisJdoSupport.getJdoPersistenceManager().addInstanceLifecycleListener(this);
	}

	@PreDestroy
	public void close() {
		System.out.println("close");
		isisJdoSupport.getJdoPersistenceManager().removeInstanceLifecycleListener(this);
	}

	@Programmatic
	public void postCreate(InstanceLifecycleEvent arg0) {
		// TODO Auto-generated method stub
		System.out.println("postCreate");
	}

	@Programmatic
	public void preStore(InstanceLifecycleEvent event) {
		System.out.println("preStore1");
		final Object pi = event.getPersistentInstance();

		if (pi instanceof org.datanucleus.enhancement.Persistable) {
			boolean isPersistent = ((org.datanucleus.enhancement.Persistable) pi).dnIsPersistent();
			System.out.println("preStore2");
			if (!isPersistent) {
				if (pi instanceof CreateTrackedEntity) {
					System.out.println("preStore3");
					((CreateTrackedEntity) pi).setCreatedBy(container.getUser().getName());

					((CreateTrackedEntity) pi).setCreatedOn(clockService.nowAsDateTime());
				}
				if (pi instanceof AbstractTenantedDomainEntity) {
					if (applicationUserRepository != null) {
						System.out.println("preStore4");
						ApplicationUser user = applicationUserRepository.findUserByUsername(container.getUser().getName());
						if (user != null) {
							System.out.println("preStore5");
							String tenancyPath = user.getTenancy().getPath();
							if (tenancyPath.startsWith("/"))
								tenancyPath = tenancyPath.substring(1);
							Region region = regions.regionForName(tenancyPath);
							((AbstractTenantedDomainEntity) pi).setRegion(region);
						}
					}
				}
			} else {
				if (pi instanceof ModifyTrackedEntity) {
					System.out.println("preStore6");
					((ModifyTrackedEntity) pi).setLastModifiedBy(container.getUser().getName());

					((ModifyTrackedEntity) pi).setLastModifiedOn(clockService.nowAsDateTime());
				}
			}
		}
	}

	@Programmatic
	public void postStore(InstanceLifecycleEvent event) {
		// no-op
	}

	@Inject
	private DomainObjectContainer container;

	@Inject
	private ClockService clockService;

	@Inject
	private IsisJdoSupport isisJdoSupport;

	@Inject
	ApplicationUsers applicationUserRepository;

	@Inject
	Regions regions;

}