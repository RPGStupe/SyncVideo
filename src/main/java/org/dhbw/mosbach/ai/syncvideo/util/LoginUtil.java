package org.dhbw.mosbach.ai.syncvideo.util;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

/**
 * Util to check whether cookie is set
 */
public class LoginUtil {
    public static String checkCookie(HttpServletRequest req, String searchString) {
        Cookie cookie[] = req.getCookies();
        if(cookie!=null) { //There are cookies
            for (Cookie tempCookie : cookie) {
                if (tempCookie.getName().equals(searchString)) {
                    return tempCookie.getValue();
                }
            }
        }
        return ""; //this means no cookies were found
    }
}
