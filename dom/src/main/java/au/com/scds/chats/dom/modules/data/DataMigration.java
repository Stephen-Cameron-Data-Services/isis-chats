package au.com.scds.chats.dom.modules.data;

import org.apache.isis.core.commons.authentication.AuthenticationSession;
import org.apache.isis.core.runtime.sessiontemplate.AbstractIsisSessionTemplate;

/**
 * Tool for migration of data from old schema to 
 * new Isis generated one via Isis DOM.
 * 
 * @author stevec
 *
 */
public class DataMigration extends AbstractIsisSessionTemplate{
	
	@Override
	public void execute(final AuthenticationSession authSession, final Object context) {

	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		DataMigration migration = new DataMigration();
		//AuthenticationSession authSession = new AuthenticationSession();
		//migration.execute(authSession , null);

	}

}
