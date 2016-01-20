package au.com.scds.chats.datamigration.access;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;

import org.apache.isis.applib.DomainObjectContainer;

import au.com.scds.chats.dom.module.general.names.Salutation;

public class SalutationMap{
	
	EntityManager em;
	Map<BigInteger, Salutation> map = new HashMap<BigInteger, Salutation>();

	public SalutationMap(EntityManager em) {
		this.em = em;
	}

	public au.com.scds.chats.dom.module.general.names.Salutation map(BigInteger id) {
		if (id == null)
			return null;
		else {
			if (map.containsKey(id))
				return map.get(id);
			else {
				System.out.println("Salutation(" + id + ") not found");
			}
		}
		return map.get(id);
	}

	public void init(DomainObjectContainer container) {
		List<au.com.scds.chats.datamigration.model.Salutation> salutations = this.em.createQuery("select salutation from Salutation salutation", au.com.scds.chats.datamigration.model.Salutation.class).getResultList();
		for (au.com.scds.chats.datamigration.model.Salutation salutation : salutations) {
			if (!map.containsKey(salutation.getId())) {
				au.com.scds.chats.dom.module.general.names.Salutation newSalutation = new au.com.scds.chats.dom.module.general.names.Salutation();
				newSalutation.setName(salutation.getCode());
				map.put(salutation.getId(), newSalutation);
				System.out.println("Salutation(" + newSalutation.getName() + ")");
			}
		}
	}
}