package gigster.com.holdsum.viewmodel;

import android.databinding.ObservableField;

import java.io.Serializable;

/**
 * Created by Eshaan on 3/1/2016.
 */
public class RegistrationViewModel implements Serializable {

    public static final int SEX_MALE = 0;
    public static final int SEX_FEMALE = 1;

    public static final int EMPLOYMENT_STATUS_EMPLOYED = 0;
    public static final int EMPLOYMENT_STATUS_NOT_EMPLOYED = 1;

    public static final int PAY_FREQUENCY_WEEKLY = 0;
    public static final int PAY_FREQUENCY_BIWEEKLY = 1;
    public static final int PAY_FREQUENCY_MONTHLY = 2;

    public static final int ACCOUNT_TYPE_CHECKING = 0;
    public static final int ACCOUNT_TYPE_SAVINGS = 1;

    public final ObservableField<String> firstName = new ObservableField<>();
    public final ObservableField<String> middleInitial = new ObservableField<>();
    public final ObservableField<String> ssn = new ObservableField<>();
    public final ObservableField<String> ssnConfirm = new ObservableField<>();
    public final ObservableField<String> lastName = new ObservableField<>();
    public final ObservableField<Integer> sex = new ObservableField<>(-1);
    public final ObservableField<String> address = new ObservableField<>();
    public final ObservableField<String> city = new ObservableField<>();
    public final ObservableField<String> state = new ObservableField<>();
    public final ObservableField<String> zip = new ObservableField<>();
    public final ObservableField<Integer> employmentStatus = new ObservableField<>(-1);
    public final ObservableField<String> fundsSource = new ObservableField<>();
    public final ObservableField<String> monthlyIncome = new ObservableField<>();
    public final ObservableField<String> sourceOfIncome = new ObservableField<>();
    public final ObservableField<Integer> payFrequency = new ObservableField<>(-1);
    public final ObservableField<String> nextPayDate = new ObservableField<>();
    public final ObservableField<String> roleTitle = new ObservableField<>();
    public final ObservableField<String> placeOfEmployment = new ObservableField<>();
    public final ObservableField<String> employerAddress = new ObservableField<>();
    public final ObservableField<String> employerCity = new ObservableField<>();
    public final ObservableField<String> employerState = new ObservableField<>();
    public final ObservableField<String> employerZip = new ObservableField<>();
    public final ObservableField<String> idPicturePath = new ObservableField<>();
    public final ObservableField<String> paystubPicturePath = new ObservableField<>();
    public final ObservableField<Integer> accountType = new ObservableField<>(-1);
    public final ObservableField<String> accountNumber = new ObservableField<>();
    public final ObservableField<String> routingNumber = new ObservableField<>();

    public RegistrationViewModel() {
    }

    public boolean isValid() {
        String[] requiredFields = new String[] {this.firstName.get(), this.lastName.get(), this.address.get(),
                this.city.get(), this.state.get(), this.nextPayDate.get(),
                this.roleTitle.get(), this.placeOfEmployment.get(), this.employerAddress.get(), this.employerCity.get(), this.employerState.get(), this.employerZip.get(),
                this.idPicturePath.get(), this.paystubPicturePath.get(), this.ssn.get(), this.ssnConfirm.get(), this.accountNumber.get(), this.routingNumber.get()};
        boolean valid = true;
        for (int i=0;i<requiredFields.length;i++) {
            valid = requiredFields[i] != null && !requiredFields[i].isEmpty();
            if (!valid)
                return false;
        }

        if (this.payFrequency.get() == -1 || this.accountType.get() == -1 || this.sex.get() == -1)
            return false;

        // depends on employment employmentStatus
        if (employmentStatus.get() != -1 && employmentStatus.get() == EMPLOYMENT_STATUS_EMPLOYED) {
            valid = valid && monthlyIncome.get() != null && !monthlyIncome.get().isEmpty();
            valid = valid && sourceOfIncome.get() != null && !sourceOfIncome.get().isEmpty();
        } else {
            valid = valid && fundsSource.get() != null && !fundsSource.get().isEmpty();
        }

        return valid;
    }


    public boolean hasValidSSN() {
        return ssn.get() != null && ssnConfirm.get() != null && ssn.get().equals(ssnConfirm.get());

    }

    public void initWithDummyCorrectData() {
        firstName.set("John");
        middleInitial.set("T");
        lastName.set("Doe");
        sex.set(SEX_MALE);
        address.set("Street 1");
        city.set("City 1");
        state.set("CA");
        zip.set("11111");
        ssn.set("123-45-6789");
        ssnConfirm.set("123-45-6789");
        fundsSource.set("Savings");
        employmentStatus.set(EMPLOYMENT_STATUS_EMPLOYED);
        monthlyIncome.set("5000");
        sourceOfIncome.set("Employment 2");
        payFrequency.set(PAY_FREQUENCY_WEEKLY);
        nextPayDate.set("09/02/16");
        roleTitle.set("CEO");
        accountType.set(ACCOUNT_TYPE_CHECKING);
        accountNumber.set("1122223333444455556666");
        routingNumber.set("111");
        placeOfEmployment.set("Company");
        employerAddress.set("Street 2");
        employerCity.set("City 2");
        employerState.set("CA");
        employerZip.set("22222");
        idPicturePath.set("/data/data/gigster.com.holdsum/test1.jpg");
        paystubPicturePath.set("/data/data/gigster.com.holdsum/test2.jpg");
    }

    public void clear() {
        firstName.set("");
        middleInitial.set("");
        lastName.set("");
        sex.set(-1);
        address.set("");
        city.set("");
        state.set("");
        zip.set("");
        ssn.set("");
        ssnConfirm.set("");
        fundsSource.set("");
        employmentStatus.set(-1);
        monthlyIncome.set("");
        sourceOfIncome.set("");
        payFrequency.set(-1);
        nextPayDate.set("");
        roleTitle.set("");
        accountType.set(-1);
        accountNumber.set("");
        routingNumber.set("");
        placeOfEmployment.set("");
        employerAddress.set("");
        employerCity.set("");
        employerState.set("");
        employerZip.set("");
        idPicturePath.set("");
        paystubPicturePath.set("");
    }
}
