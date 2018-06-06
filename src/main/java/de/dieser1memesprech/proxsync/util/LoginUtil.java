package de.dieser1memesprech.proxsync.util;

import javax.servlet.ServletRequest;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

/**
 * Created by Jeremias on 19.09.2017.
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
