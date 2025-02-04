package com.ogborn.c868final.Helper;

import com.ogborn.c868final.Model.User;

import java.util.Locale;

/**
 * holds session-level data such as currently logged-in user, that way it's simpler to retrieve, and we don't have to actually find it multiple times
 */
public class Session {
    /**
     * the current logged-in user
     */
    private static User currentLoggedInUser;
    /**
     * current application locale
     */
    private static Locale currentLocale;

    /**
     * gets the current logged-in user
     * @return the user
     */
    public static User getCurrentUser(){return currentLoggedInUser;}

    /**
     * sets the logged-in user or nulls it, accordingly
     * @param user the user to set as logged-in, or null to force logout
     */
    public static void setCurrentUser(User user){currentLoggedInUser = user;}

    /**
     * gets the current Locale
     * @return the current Locale
     */
    public static Locale getCurrentLocale(){return currentLocale;}

    /**
     * sets the current locale, will be used with getDefault()
     * @param locale the locale to set
     */
    public static void setCurrentLocale(Locale locale){currentLocale = locale;}
}
