package au.com.scds.chats.datamigration.access;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;

import org.apache.isis.applib.DomainObjectContainer;

import au.com.scds.chats.dom.general.Location;
import au.com.scds.chats.dom.general.names.TransportType;
import au.com.scds.chats.dom.general.names.TransportTypes;

public class TransportTypeMap {

	EntityManager em;
	Map<BigInteger, TransportType> map = new HashMap<BigInteger, TransportType>();

	
	TransportTypes tMap;
	TransportType unknown;
	
	public TransportTypeMap(EntityManager em) {
		this.em = em;
	}

	public au.com.scds.chats.dom.general.names.TransportType map(BigInteger id) {
		if (id == null)
			return null;
		else {
			if (map.containsKey(id))
				return map.get(id);
			else if (id.intValueExact() == 0) {
				return null;
			} else {
				System.out.println("TransportType(" + id + ") not found");
			}
		}
		return map.get(id);
	}

	public void init(TransportTypes TransportTypes2) {
		tMap = TransportTypes2;
		Map<String, TransportType> temp = new HashMap<String, TransportType>();
		List<au.com.scds.chats.datamigration.model.Transporttype> TransportTypes = this.em
				.createQuery("select t from Transporttype t", au.com.scds.chats.datamigration.model.Transporttype.class)
				.getResultList();
		for (au.com.scds.chats.datamigration.model.Transporttype type : TransportTypes) {
			if (temp.containsKey(type.getTitle())) {
				map.put(type.getId(), temp.get(type.getTitle()));
				System.out.println("TransportType(duplicate=" + type.getTitle() + ")");
			} else {
				TransportType transportType = tMap.transportTypeForName(type.getTitle());
				if (transportType != null) {
					map.put(type.getId(), transportType);
					temp.put(type.getTitle(), transportType);
					System.out.println("TransportType(" + transportType.getName() + ")");
				} else {
					System.out.println("UNKNOWN TransportType(" + type.getTitle() + ")");
				}
			}
		}
	}

	public TransportType getUnknown() {
		if(unknown == null)
			unknown = tMap.transportTypeForName("Unknown");
		return unknown;
	}
}
