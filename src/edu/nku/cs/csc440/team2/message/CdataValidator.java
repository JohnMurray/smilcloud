/*
 * CdataValidator.java
 */
package edu.nku.cs.csc440.team2.message;

/**
 * searches XML CDATA field strings for characters requiring escape sequences;
 * typically used as an anonymous class to find invalid characters while
 * ignoring exceptions
 * 
 * @author Shane Crandall
 * @version 1.0, 04/18/11
 */
class CdataValidator {

	/**
	 * all the characters requiring escape sequences in valid XML character data
	 */
	private static final String XML_ESC_CHARS = "&,\\,^,\r,\",>,[,<,\n,%,+,#,], ,\t";

	/**
	 * array of all the same characters
	 */
	private String[] xmlEscCharsArray = XML_ESC_CHARS.split(",");

	/**
	 * compares a string against all invalid characters
	 * 
	 * @param cData
	 *            string to validate
	 * @param exception
	 *            character string to ignore during validation
	 * @return validated input string or null if invalid
	 */
	String validate(String cData, String exception) {
		if (cData != null) {
			for (String string : xmlEscCharsArray) {
				if (cData.contains(string)) {
					if (exception.contains(string)) {
						return cData;
					} else {
						return null;
					}
				}
			}
		}
		return cData;
	}
}
