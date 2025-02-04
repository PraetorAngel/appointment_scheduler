package com.ogborn.c868final;

import com.ogborn.c868final.DAO.AppointmentDAO;
import com.ogborn.c868final.Helper.JDBC;
import com.ogborn.c868final.Helper.MonthMap;
import com.ogborn.c868final.Helper.Session;
import javafx.collections.ObservableList;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Locale;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class ReportTest {

    @BeforeAll
    public static void setup() {
        // Open the database connection before any tests run
        JDBC.openConnection();
    }

    @AfterAll
    public static void teardown() {
        // Close the database connection after all tests are done
        JDBC.closeConnection();
    }

    @Test
    public void validateReport1AcrossLocales() throws Exception {
        String[] locales = {"en", "es", "fr", "ru"}; // Locales to test
        ObservableList<String> types = AppointmentDAO.getAppointmentTypes();
        assertNotNull(types, "Appointment types should not be null");

        for (String locale : locales) {
            // Set the current locale
            Session.setCurrentLocale(new Locale(locale));
            MonthMap.loadMonthMap(); // Reload monthMap for the current locale

            Map<Integer, String> monthMap = MonthMap.getMonthMap(); // Use the updated map
            System.out.println("Locale: " + locale);

            for (Map.Entry<Integer, String> entry : monthMap.entrySet()) {
                int monthNumber = entry.getKey();
                String monthName = entry.getValue();

                for (String type : types) {
                    int count = AppointmentDAO.getMonthTypeCount(monthNumber, type); // Query the database
                    if (count > 0) {
                        System.out.println("Month: " + monthName + ", Type: " + type + ", Count: " + count);
                    }
                }
            }
        }
    }
}
