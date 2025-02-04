package com.ogborn.c868final.DAO;

import com.ogborn.c868final.Helper.JDBC;
import com.ogborn.c868final.Helper.Session;
import com.ogborn.c868final.Model.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * handles database access and operations involving user objects
 */
public class UserDAO{

    /**
     * gets the list of all users in the database
     * @return the list of users
     * @throws SQLException handling for a sql exception if thrown
     */
    public static ObservableList<User> getAllUsers() throws SQLException {
        ObservableList<User> allUsersList = FXCollections.observableArrayList();
        String allUsersSQL= "SELECT * FROM users";

        PreparedStatement allUsersStatement = JDBC.getConnection().prepareStatement(allUsersSQL);

        ResultSet result=allUsersStatement.executeQuery();

        while(result.next()){
            int userid=result.getInt("User_ID");
            String username=result.getString("User_Name");
            String password=result.getString("Password");
            User userResult= new User(userid, username, password);
            allUsersList.add(userResult);
        }
        return allUsersList;
    }

    /**
     * authenticates the supplied credentials
     * @param username the username
     * @param password the password
     * @return the user ID if exists, else return 0 (might need to be -1)
     * @throws SQLException less code-y than a try/catch
     */
    public static int authenticate(String username, String password) throws SQLException {
        String authenticateSQL = "SELECT * FROM users WHERE User_Name = ? AND Password = ?";
        PreparedStatement authenticateStatement = JDBC.getConnection().prepareStatement(authenticateSQL);

        authenticateStatement.setString(1, username);
        authenticateStatement.setString(2, password);

        ResultSet authenticateResult=authenticateStatement.executeQuery();

        if (authenticateResult.next()){
            if (authenticateResult.getString("User_Name").equals(username)) {
                if (authenticateResult.getString("Password").equals(password)) {
                    User authenticatedUser = new User(authenticateResult.getInt("User_ID"), authenticateResult.getString("User_Name"), authenticateResult.getString("Password"));
                    Session.setCurrentUser(authenticatedUser);

                    return authenticatedUser.getUserId();
                }
            }
        }
        //if no valid user found
        return -1;
    }
}
