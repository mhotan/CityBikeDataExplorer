package se.kth.csc.moderndb.cbexplorer.data;

import java.util.Date;

/**
 * Provides all needed attributes to fill the trip related tables in the database.
 * <p/>
 * Created by Jeannine on 04.05.14.
 */
public class TripDataObject {


    private long tripID;
    private long startStationID;
    private long endStationID;

    private Date startTime;
    private Date endTime;

    private long bikeID;
    private short gender;
    private int birthyear;
    private String userDescription;


    public TripDataObject(long tripID, long startStationID, long endStationID, Date startTime, Date endTime, long bikeID, short gender, int birthyear, String userDescription) {
        this.tripID = tripID;
        this.startStationID = startStationID;
        this.endStationID = endStationID;
        this.startTime = startTime;
        this.endTime = endTime;
        this.bikeID = bikeID;
        this.gender = gender;
        this.birthyear = birthyear;
        this.userDescription = userDescription;
    }

    public long getTripID() {
        return tripID;
    }

    public long getStartStationID() {
        return startStationID;
    }

    public long getEndStationID() {
        return endStationID;
    }

    public long getBikeID() {
        return bikeID;
    }

    public int getGender() {
        return gender;
    }

    public String getUserDescription() {
        return userDescription;
    }

    public Date getStartTime() {
        return startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public int getBirthyear() {
        return birthyear;
    }
}
