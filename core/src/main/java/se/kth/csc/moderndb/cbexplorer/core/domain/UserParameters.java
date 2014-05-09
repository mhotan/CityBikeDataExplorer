package se.kth.csc.moderndb.cbexplorer.core.domain;

/**
 * POJO UserParameters class.
 *
 * Created by Jeannine on 07.05.14.
 */
public class UserParameters {

    /**
     * Describes the gender of the User.
     * "M" is for male, "F" for female, anything else does not have a meaning
     */
    private char gender;
    /**
     * The starting point of the age range
     */
    private int startRangeAge;
    /**
     * The end point of the age range
     */
    private int endRangeAge;
    /**
     * Description of the user type
     */
    private String userType;

    /**
     * Empty Constructor needed for POJO objects
     */
    public UserParameters() {
    }

    /**
     * Creates a new POJO UserParameters object.
     * @param gender user's gender
     * @param startRangeAge start of user's age range
     * @param endRangeAge end of user's age range
     * @param userType user's type description
     */
    public UserParameters(char gender, int startRangeAge, int endRangeAge, String userType) {
        this.gender = gender;
        this.startRangeAge = startRangeAge;
        this.endRangeAge = endRangeAge;
        this.userType = userType;
    }

    public char getGender() {
        return gender;
    }

    public int getStartRangeAge() {
        return startRangeAge;
    }

    public int getEndRangeAge() {
        return endRangeAge;
    }

    public String getUserType() {
        return userType;
    }
}
