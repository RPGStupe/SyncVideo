package de.dieser1memesprech.proxsync.util;

import javax.servlet.ServletRequest;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

/**
 * Created by Jeremias on 19.09.2017.
 */
public class LoginUtil {
    public static String CheckCookie(HttpServletRequest req, String name)
    {
        String SearchString;
        Cookie cookie[] = req.getCookies();
        SearchString=name;

        if(cookie!=null) //There are cookies
        {
            for(int i =0 ; i<cookie.length ; i++)
            {
                Cookie tempCookie= cookie[i];

                if(tempCookie.getName().equals(SearchString))
                {
                    return tempCookie.getValue();
                }
            }
        }
        return ""; //this means no cookies were found
    }
}
