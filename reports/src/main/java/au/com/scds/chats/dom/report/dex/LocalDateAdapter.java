package au.com.scds.chats.dom.report.dex;

import org.joda.time.LocalDate;

public class LocalDateAdapter {

	public static LocalDate unmarshal(String v) throws Exception {
		return new LocalDate(v);
	}

	public static String marshal(LocalDate v) throws Exception {
		return v.toString();
	}
}