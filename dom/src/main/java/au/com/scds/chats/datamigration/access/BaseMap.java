package au.com.scds.chats.datamigration.access;

import java.math.BigInteger;

public class BaseMap {

	public Long BigInt2Long(BigInteger bigInt) {
		if (bigInt == null)
			return null;
		else
			return new Long(bigInt.longValue());
	}

	public String TrimToLength(String s, int l) {
		if (s != null) {
			if (s.length() > l) {
				return s.substring(0, l - 1);
			} else {
				return s;
			}
		}
		return null;
	}
}
