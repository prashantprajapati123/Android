package gigster.com.holdsum.helper.enums;

/**
 * Created by tpaczesny on 24.10.2016.
 */
public enum UserStatus {
    UNKNOWN, PENDING, APPROVED, DENIED;

    /**
        Needed for unit testing only.
     */
    public static UserStatus safeValueOf(String name)
            throws IllegalArgumentException {
        if (name == null)
            return UNKNOWN;
        return valueOf(name);
    }
}
