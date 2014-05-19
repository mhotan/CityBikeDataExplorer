package se.kth.csc.moderndb.cbexplorer.core.params;

import se.kth.csc.moderndb.cbexplorer.core.range.ShortRange;

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
     * Range of birth year.
     */
    private ShortRange birthYearRange;

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
     * @param birthYearRange Range of birth years.
     * @param userType user's type description
     */
    public UserParameters(char gender, ShortRange birthYearRange, String userType) {
        this.gender = gender;
        this.birthYearRange = birthYearRange;
        this.userType = userType;
    }

    public char getGender() {
        return gender;
    }

    public ShortRange getBirthYearRange() {
        return birthYearRange;
    }

    public String getUserType() {
        return userType;
    }
}
