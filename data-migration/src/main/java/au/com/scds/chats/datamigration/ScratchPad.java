package au.com.scds.chats.datamigration;

import java.math.BigInteger;
import java.util.List;

import javax.jdo.Query;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class ScratchPad {

	public static void main(String[] args) {
		
		final EntityManagerFactory emf = Persistence.createEntityManagerFactory("isis-chats-old");
		final EntityManager em = emf.createEntityManager();
		javax.persistence.Query q = em.createNativeQuery("select activities.id FROM " +
		"(select copiedFrom__activity_id, count(*) as count from lifeline.activities " +
		"group by copiedFrom__activity_id) as counts, lifeline.activities as activities " +
		"where not isnull(counts.copiedFrom__activity_id) " +
		"and activities.id = counts.copiedFrom__activity_id " +
		"order by counts.count desc; ");
		List<Long> copied = q.getResultList();
		for(Long id : copied){
			System.out.println(id);
		}
		em.close();
		
	}

}
