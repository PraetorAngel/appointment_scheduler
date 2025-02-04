package com.ogborn.c868final.Helper;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.ResourceBundle;

/**
 * The {@code MonthMap} class provides functionality to map numeric month indices
 * (1 through 12) to their localized names based on the current application locale.
 * It is primarily used to populate UI components like ComboBoxes with localized month names.
 */
public class MonthMap {
    /**
     * A map that holds numeric month indices and their corresponding localized names.
     * This map is dynamically updated based on the current locale.
     */
    private static final Map<Integer, String> monthMap = new LinkedHashMap<>();

    /**
     * Loads the month names into the map for the current locale.
     * Clears any existing data in the map and populates it with localized month names.
     */
    public static void loadMonthMap() {
        ResourceBundle loadedBundle = ResourceBundle.getBundle("com.ogborn.c868final.MonthMap_" + Session.getCurrentLocale().getLanguage());
        monthMap.clear();
        monthMap.put(1, loadedBundle.getString("january"));
        monthMap.put(2, loadedBundle.getString("february"));
        monthMap.put(3, loadedBundle.getString("march"));
        monthMap.put(4, loadedBundle.getString("april"));
        monthMap.put(5, loadedBundle.getString("may"));
        monthMap.put(6, loadedBundle.getString("june"));
        monthMap.put(7, loadedBundle.getString("july"));
        monthMap.put(8, loadedBundle.getString("august"));
        monthMap.put(9, loadedBundle.getString("september"));
        monthMap.put(10, loadedBundle.getString("october"));
        monthMap.put(11, loadedBundle.getString("november"));
        monthMap.put(12, loadedBundle.getString("december"));
    }

    /**
     * Returns the month map containing numeric indices and their corresponding localized names.
     *
     * @return a map of month indices to their localized names
     */
    public static Map<Integer, String> getMonthMap() {
        return monthMap;
    }
}
