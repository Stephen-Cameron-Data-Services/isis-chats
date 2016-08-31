package au.com.scds.chats.dom.general;

import org.apache.isis.applib.annotation.DomainObject;
import org.apache.isis.applib.annotation.Nature;
import org.apache.isis.applib.annotation.Property;

@DomainObject(nature = Nature.VIEW_MODEL)
public class Result {
	
	private String result;
	
	public Result() {
	}
	
	public Result(String result) {
		this.result = result;
	}

	public String title() {
		return getResult();
	}

	@Property()
	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}
	
	

}
