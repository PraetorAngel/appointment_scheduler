package com.ogborn.c868final.Model;

import java.sql.Timestamp;

/**
 * describes Appointment objects and their methods
 */
public class Appointment {
    //<editor-fold desc="data members">
    /**
     * unique identifier for the appointment
     */
    private int appointmentId;

    /**
     * title of the appointment
     */
    private String title;

    /**
     * description of the appointment
     */
    private String description;

    /**
     * location of the appointment
     */
    private String location;

    /**
     * type of the appointment
     */
    private String type;

    /**
     * start timestamp of the appointment
     */
    private Timestamp start;

    /**
     * end timestamp of the appointment
     */
    private Timestamp end;

    /**
     * customer ID associated with the appointment
     */
    private int customerId;

    /**
     * user ID associated with the appointment
     */
    private int userId;

    /**
     * contact ID associated with the appointment
     */
    private int contactId;

    /**
     * contact name associated with the appointment
     */
    private String contactName;
    //</editor-fold>

    /**
     * constructor to instantiate an appointment
     * @param appointmentId the appointment id
     * @param title the appointment title
     * @param description the appointment description
     * @param location the appointment location
     * @param contactId the contact id
     * @param contactName the contact name
     * @param type the appointment type
     * @param start the appointment start time
     * @param end the appointment end time
     * @param customerId the customer id
     * @param userId the user id
     */
    public Appointment (int appointmentId, String title, String description, String location, int contactId, String contactName, String type, Timestamp start, Timestamp end, int customerId, int userId) {
        this.appointmentId = appointmentId;
        this.title = title;
        this.description = description;
        this.location = location;
        this.type = type;
        this.start = start;
        this.end = end;
        this.customerId = customerId;
        this.userId = userId;
        this.contactId = contactId;
        this.contactName = contactName;
    }

    /**
     * gets the appointment id number
     * @return the appointment id
     */
    public int getAppointmentId() {return appointmentId;}

    /**
     * gets the appointment title
     * @return the title
     */
    public String getTitle() {return title;}

    /**
     * gets the appointment description
     * @return the description
     */
    public String getDescription() {return description;}

    /**
     * gets the appointment location
     * @return the location
     */
    public String getLocation() {return location;}

    /**
     * gets the appointment type
     * @return the type
     */
    public String getType() {return type;}

    /**
     * gets the appointment start time
     * @return the start time
     */
    public Timestamp getStart() {return start;}

    /**
     * gets the appointment end time
     * @return the end time
     */
    public Timestamp getEnd() {return end;}

    /**
     * gets the customer id number
     * @return the customer id
     */
    public int getCustomerId() {return customerId;}

    /**
     * gets the user id number
     * @return the user id
     */
    public int getUserId() {return userId;}

    /**
     * gets the contact id number
     * @return the contact id
     */
    public int getContactId() {return contactId;}

    /**
     * gets the contact name
     * @return the contact name
     */
    public String getContactName() {return contactName;}

    /**
     * sets the appointment id
     * @param appointmentId the appointment id to set
     */
    public void setAppointmentID(int appointmentId) {
        this.appointmentId = appointmentId;
    }

    /**
     * sets the appointment title
     * @param title the title to set
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * sets the appointment description
     * @param description the description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * sets the appointment location
     * @param location the location to set
     */
    public void setLocation(String location) {
        this.location = location;
    }

    /**
     * sets the appointment type
     * @param type the type
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * sets the appointment start time
     * @param start the start time
     */
    public void setStart(Timestamp start) {
        this.start = start;
    }

    /**
     * sets the appointment end time
     * @param end the end time
     */
    public void setEnd(Timestamp end) {
        this.end = end;
    }

    /**
     * sets the customer id
     * @param customerId the customer id to set
     */
    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    /**
     * sets the user id
     * @param userId the user id to set
     */
    public void setUserId(int userId) {
        this.userId = userId;
    }

    /**
     * sets the contact id
     * @param contactId the contact id to set
     */
    public void setContactId(int contactId) {
        this.contactId = contactId;
    }

    /**
     * sets the contact name
     * @param contactName the contact name to set
     */
    public void setContactName(String contactName) {this.contactName = contactName;}
}
