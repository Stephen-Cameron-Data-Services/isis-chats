package au.com.scds.chats.datamigration.access;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;

import au.com.scds.chats.dom.module.general.names.Salutation;
import au.com.scds.chats.dom.module.general.names.Salutations;

public class SalutationMap {

	EntityManager em;
	Map<BigInteger, Salutation> map = new HashMap<BigInteger, Salutation>();

	public SalutationMap(EntityManager em) {
		this.em = em;
	}

	public Salutation map(BigInteger id) {
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

	public void init(Salutations salutations2) {
		List<au.com.scds.chats.datamigration.model.Salutation> salutations = this.em
				.createQuery("select salutation from Salutation salutation",
						au.com.scds.chats.datamigration.model.Salutation.class)
				.getResultList();
		for (au.com.scds.chats.datamigration.model.Salutation s : salutations) {
			map.put(s.getId(), salutations2.salutationForName(s.getCode()));
		}
	}
}