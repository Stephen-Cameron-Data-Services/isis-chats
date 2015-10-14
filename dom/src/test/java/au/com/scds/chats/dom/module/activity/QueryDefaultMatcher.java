package au.com.scds.chats.dom.module.activity;

import org.apache.isis.applib.query.Query;
import org.apache.isis.applib.query.QueryDefault;
import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;

public class QueryDefaultMatcher extends TypeSafeMatcher<QueryDefault> {

	Class typeClass;
	String queryName;
	
	QueryDefaultMatcher(Class typeClass, String queryName){
		this.typeClass = typeClass;
		this.queryName = queryName;
	}

	@Override
	public void describeTo(Description description) {
		description.appendText("a query ");
	}

	@Override
	protected boolean matchesSafely(QueryDefault query) {
		System.out.println(query.getQueryName());
		System.out.println(queryName);
		if(query.getResultType().getCanonicalName().equals(typeClass.getCanonicalName())){
			if(query.getQueryName().equals(queryName)){
				return true;
			}
		}
		return false;
	}
}

