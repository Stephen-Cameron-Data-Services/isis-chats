package au.com.scds.chats.dom.module.activity;

import org.apache.isis.applib.query.Query;
import org.apache.isis.applib.query.QueryDefault;
import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;

public class QueryDefaultMatcher extends TypeSafeMatcher<QueryDefault> {

	Class typeClass;
	String queryName;
	String reasons = "";
	
	QueryDefaultMatcher(Class typeClass, String queryName){
		this.typeClass = typeClass;
		this.queryName = queryName;
	}

	@Override
	public void describeTo(Description description) {
		description.appendText(reasons);
	}

	@Override
	protected boolean matchesSafely(QueryDefault query) {
		boolean matches = true;
		if(typeClass == null){
			reasons += " typeClass parameter is null, ";
			matches = false;
		}
		if(queryName == null){
			reasons += " queryName parameter is null, ";
			matches = false;
		}
		if(query == null){
			reasons += " QueryDefault parameter is null, ";
			matches = false;			
		}
		if(!query.getResultType().getCanonicalName().equals(typeClass.getCanonicalName())){
			reasons += " typeClass is not as expected (was " + query.getResultType().getCanonicalName() + " but expected " + typeClass.getCanonicalName() + ")";
			matches = false;
		}
		if(!query.getQueryName().equals(queryName)){
			reasons += " queryName is not as expected (was " + query.getQueryName() + " but expected " + queryName + ")";
			matches = false;
		}
		return matches;
	}
}

