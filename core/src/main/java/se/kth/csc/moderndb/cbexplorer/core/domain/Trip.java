package se.kth.csc.moderndb.cbexplorer.core.domain;

/**
 * Trip POJO class for REST Service.
 *
 * Created by mhotan on 5/7/14.
 */
public class Trip {

    /**
     * Trip time
     */
    private long startTime, endTime;

    /**
     * User information
     */
    private String userType;
    private short userBirthYear, userGender;

    /**
     *
     */
    private Station startedFrom, endedAt;

    /**
     *
     */
    private Bike bike;

}
