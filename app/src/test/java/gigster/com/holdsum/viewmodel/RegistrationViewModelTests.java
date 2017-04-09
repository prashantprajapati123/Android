package gigster.com.holdsum.viewmodel;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by tpaczesny on 2016-09-12.
 */
public class RegistrationViewModelTests {

    @Test
    public void isValid_correctDummyData_true() {
        RegistrationViewModel data = new RegistrationViewModel();
        data.initWithDummyCorrectData();
        assertTrue(data.isValid());
    }

    @Test
    public void isValid_missingFirstName_false() {
        RegistrationViewModel data = new RegistrationViewModel();
        data.initWithDummyCorrectData();
        data.firstName.set(null);
        assertFalse(data.isValid());
    }

    @Test
    public void isValid_emptyFirstName_false() {
        RegistrationViewModel data = new RegistrationViewModel();
        data.initWithDummyCorrectData();
        data.firstName.set("");
        assertFalse(data.isValid());
    }

    @Test
    public void isValid_missingLastName_false() {
        RegistrationViewModel data = new RegistrationViewModel();
        data.initWithDummyCorrectData();
        data.lastName.set(null);
        assertFalse(data.isValid());
    }

    @Test
    public void isValid_emptyLastName_false() {
        RegistrationViewModel data = new RegistrationViewModel();
        data.initWithDummyCorrectData();
        data.lastName.set("");
        assertFalse(data.isValid());
    }

    @Test
    public void hasValidSSN_matching_true() {
        RegistrationViewModel data = new RegistrationViewModel();
        data.initWithDummyCorrectData();
        data.ssn.set("12345");
        data.ssnConfirm.set("12345");
        assertTrue(data.hasValidSSN());
    }

    @Test
    public void hasValidSSN_mismatching_false() {
        RegistrationViewModel data = new RegistrationViewModel();
        data.initWithDummyCorrectData();
        data.ssn.set("12345");
        data.ssnConfirm.set("543321");
        assertFalse(data.hasValidSSN());
    }


}