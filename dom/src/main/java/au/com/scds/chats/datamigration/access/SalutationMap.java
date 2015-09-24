package au.com.scds.chats.datamigration.access;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;

import org.apache.isis.applib.DomainObjectContainer;

import au.com.scds.chats.dom.module.general.codes.Region;
import au.com.scds.chats.dom.module.general.codes.Salutation;

public class SalutationMap extends BaseMap {

	EntityManager em;
	Map<BigInteger, Salutation> map = new HashMap<BigInteger, Salutation>();

	SalutationMap(EntityManager em) {
		this.em = em;
	}

	public au.com.scds.chats.dom.module.general.codes.Salutation map(BigInteger id) {
		if (id == null)
			return null;
		else {
			if (map.containsKey(id))
				return map.get(id);
			else {
				System.out.println("Salutation(" + id + ") not found");
				return null;
			}
		}
	}

	public void init(DomainObjectContainer container) {
		Salutation s = null;
		Map<String, Salutation> distinct = new HashMap<String, Salutation>();
		List<au.com.scds.chats.datamigration.model.Salutation> salutations = this.em
				.createQuery("select salutation from Salutation salutation", au.com.scds.chats.datamigration.model.Salutation.class).getResultList();
		for (au.com.scds.chats.datamigration.model.Salutation salutation : salutations) {
			if (!map.containsKey(salutation.getId())) {
				if (!distinct.containsKey(salutation.getCode())) {
					if (container != null) {
						s = container.newTransientInstance(Salutation.class);
					} else {
						s = new Salutation();
					}
					distinct.put(salutation.getCode(),s);
				} else {
					s = distinct.get(salutation.getCode());
				}
				s.setName(salutation.getCode());
				map.put(salutation.getId(), s);
				if (container != null) {
					container.persistIfNotAlready(s);
				}
				System.out.println("Salutation(" + s.getName() + ")");
			}
		}
	}
}