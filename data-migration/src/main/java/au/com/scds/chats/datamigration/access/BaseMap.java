package au.com.scds.chats.datamigration.access;

import java.math.BigInteger;

public class BaseMap {
	
	public Long BigInt2Long(BigInteger bigInt) {
		if (bigInt == null)
			return null;
		else
			return new Long(bigInt.longValue());
	}
	
	public String BigInt2String(BigInteger bigInt) {
		if (bigInt == null)
			return null;
		else
			return bigInt.toString();
	}
}
