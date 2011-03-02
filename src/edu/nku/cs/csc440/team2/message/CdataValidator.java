package edu.nku.cs.csc440.team2.message;

/**
 *
 * @author Shane Crandall
 * @version 0
 */
class CdataValidator {

    private static final String XML_ESC_CHARS = "&,\\,^,\r,\",>,[,<,\n,%,+,#,], ,\t";
    private String[] xmlEscCharsArray = XML_ESC_CHARS.split(",");

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
