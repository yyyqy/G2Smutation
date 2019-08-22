package org.cbioportal.g2smutation.util;

/**
 * StringUtil to deal with SQL contents
 * 
 * @author Juexin Wang
 *
 */
public class StringUtil {
	
	public String toSQLstring(String inputStr) {
		String outStr = inputStr;
		if (inputStr.contains("'")) {
			outStr = inputStr.replaceAll("'", "''");
		}
		return outStr;
	}	

}
