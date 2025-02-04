/**
 * Module definition for the c868 Final project.
 *
 * This module includes the following:
 * - javafx.controls and javafx.fxml for the JavaFX UI
 * - java.sql and mysql.connector.j for database access
 * - multiple packages for models, controllers, DAO, and helpers
 *
 * Packages are opened and exported to allow proper access for JavaFX and external components.
 */
module c868final {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires mysql.connector.j;

    opens com.ogborn.c868final to javafx.fxml;
    exports com.ogborn.c868final;

    opens com.ogborn.c868final.Controller to javafx.fxml;
    exports com.ogborn.c868final.Controller;

    opens com.ogborn.c868final.Model to javafx.fxml;
    exports com.ogborn.c868final.Model;

    opens com.ogborn.c868final.DAO to javafx.fxml;
    exports com.ogborn.c868final.DAO;

    opens com.ogborn.c868final.Helper to javafx.fxml;
    exports com.ogborn.c868final.Helper;
}
