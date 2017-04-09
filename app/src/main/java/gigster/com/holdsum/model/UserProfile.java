package gigster.com.holdsum.model;

/**
 * Created by tpaczesny on 2016-10-05.
 */

public class UserProfile {
    public String first_name;
    public String last_name;
    public String middle_initial;
    public String email;
    public String sex;
    public String ssn;
    public String address;
    public String city;
    public String state;
    public String zip_code;
    public String pay_frequency;
    public String funds_source;
    public String monthly_income;
    public String next_paydate;
    public String employment_status;
    public String status;
    public Employment employment;

    public static class Employment {
        public String title;
        public String employer_name;
        public String address;
        public String city;
        public String state;
        public String zip_code;
    }



}
