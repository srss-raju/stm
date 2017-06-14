package com.deloitte.smt.util;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Created by myelleswarapu on 01-05-2017.
 */
public class StringConverterUtil {
	private StringConverterUtil(){}

    public static List<String> convertStringToList(String orgVal) {
        return Arrays.asList(orgVal.split(Pattern.quote(Constants.MULTI_VALUE_SEPARATOR)));
    }
}
