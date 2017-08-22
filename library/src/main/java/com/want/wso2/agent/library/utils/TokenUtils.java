package com.want.wso2.agent.library.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by wisn on 2017/8/21.
 */

public class TokenUtils {
    private final static String TAG = "TokenUtils";
    private static final DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss",
                                                                      Locale.getDefault());
    /**
     * Validate the token expiration date.
     *
     * @param expirationDate - Token expiration date.
     * @return - Token status.
     */
    public static boolean isValid(Date expirationDate) {
        Date currentDate = new Date();
        String formattedDate = dateFormat.format(currentDate);
        currentDate = convertDate(formattedDate);
        boolean isExpired = currentDate.after(expirationDate);
        boolean isEqual = currentDate.equals(expirationDate);
        return isExpired || isEqual;

    }
    /**
     * Convert the date to the standard format.
     *
     * @param date - Date as a string.
     * @return - Formatted date.
     */
    public static Date convertDate(String date) {
        Date receivedDate = null;
        try {
            receivedDate = dateFormat.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return receivedDate;
    }
}
