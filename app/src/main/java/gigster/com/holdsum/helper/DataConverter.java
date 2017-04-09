package gigster.com.holdsum.helper;

import java.io.File;

import gigster.com.holdsum.helper.enums.UserStatus;
import gigster.com.holdsum.model.LoanRequest;
import gigster.com.holdsum.model.UserProfile;
import gigster.com.holdsum.viewmodel.BorrowViewModel;
import gigster.com.holdsum.viewmodel.RegistrationViewModel;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

/**
 * Created by tpaczesny on 2016-10-06.
 */

public class DataConverter {

    public static LoanRequest BorrowViewModelToLoanRequest(BorrowViewModel bvm) {
        // map local answer IDs into ones expected by API
        final int[][] RESPONSE_ID_MAP = new int[][] {
                {1,2,3,4},
                {5,6},
                {7,8,9,10},
                {11,12,13,14},
                {15,16,17,18,19,20,21,22,23,24},
                {25,26,27,28,29,30}};

        LoanRequest obj = new LoanRequest();
        obj.amount = bvm.amount.get();
        obj.repayment_date = Utils.formatDateForAPI(Utils.parseDate(bvm.repaymentDate.get()));
        obj.responses = new LoanRequest.Choice[6];
        obj.responses[0] = obj.new Choice(RESPONSE_ID_MAP[0][bvm.question1Answer.get()], null);
        obj.responses[1] = obj.new Choice(RESPONSE_ID_MAP[1][bvm.question2Answer.get()],
                !bvm.question2Textbox.get().isEmpty() ? bvm.question2Textbox.get() : null);
        obj.responses[2] = obj.new Choice(RESPONSE_ID_MAP[2][bvm.question3Answer.get()], null);
        obj.responses[3] = obj.new Choice(RESPONSE_ID_MAP[3][bvm.question4Answer.get()], null);
        obj.responses[4] = obj.new Choice(RESPONSE_ID_MAP[4][
                bvm.question5Answer.get() + (bvm.question5AnswerAlt.get() >= 0 ? bvm.question5AnswerAlt.get() : 0)], null);
        obj.responses[5] = obj.new Choice(RESPONSE_ID_MAP[5][bvm.question6Answer.get()], null);
        return obj;
    }

    public static MultipartBody.Part[] RegistrationViewModelToMultiParts(RegistrationViewModel rvm) {
        MultipartBody.Part firstName = MultipartBody.Part.createFormData("first_name",rvm.firstName.get());
        MultipartBody.Part middleInitial = MultipartBody.Part.createFormData("middle_initial",rvm.middleInitial.get());
        MultipartBody.Part lastName = MultipartBody.Part.createFormData("last_name",rvm.lastName.get());
        MultipartBody.Part ssn = MultipartBody.Part.createFormData("ssn",rvm.ssn.get());
        MultipartBody.Part sex = MultipartBody.Part.createFormData("sex",
                choiceToSexString(rvm.sex.get()));
        MultipartBody.Part state = MultipartBody.Part.createFormData("state",rvm.state.get());
        MultipartBody.Part city = MultipartBody.Part.createFormData("city",rvm.city.get());
        MultipartBody.Part address = MultipartBody.Part.createFormData("address",rvm.address.get());
        MultipartBody.Part employmentStatus = MultipartBody.Part.createFormData("employment_status",
                choiceToEmploymentStatusString(rvm.employmentStatus.get()));
        MultipartBody.Part zipCode = MultipartBody.Part.createFormData("zip_code",rvm.zip.get());
        MultipartBody.Part payFrequency = MultipartBody.Part.createFormData("pay_frequency",
                choiceToPayFrequencyString(rvm.payFrequency.get()));
        String fundsSourceStr = rvm.employmentStatus.get() == RegistrationViewModel.EMPLOYMENT_STATUS_EMPLOYED ? rvm.sourceOfIncome.get() : rvm.fundsSource.get();
        MultipartBody.Part fundsSource = MultipartBody.Part.createFormData("funds_source",fundsSourceStr);
        MultipartBody.Part monthlyIncome = MultipartBody.Part.createFormData("monthly_income",rvm.monthlyIncome.get());
        MultipartBody.Part nextPaydate = MultipartBody.Part.createFormData("next_paydate", Utils.formatDateForAPI(rvm.nextPayDate.get()));
        MultipartBody.Part employmentTitle = MultipartBody.Part.createFormData("employment.title",rvm.roleTitle.get());
        MultipartBody.Part employmentEmployerName = MultipartBody.Part.createFormData("employment.employer_name",rvm.placeOfEmployment.get());
        MultipartBody.Part employmentState = MultipartBody.Part.createFormData("employment.state",rvm.employerState.get());
        MultipartBody.Part employmentZipCode = MultipartBody.Part.createFormData("employment.zip_code",rvm.employerZip.get());
        MultipartBody.Part employmentCity = MultipartBody.Part.createFormData("employment.city",rvm.employerCity.get());
        File idPictureFile = new File(rvm.idPicturePath.get());
        RequestBody idPictureBody = RequestBody.create(MediaType.parse("image/jpeg"),idPictureFile);
        MultipartBody.Part license = MultipartBody.Part.createFormData("license",idPictureFile.getName(), idPictureBody);
        File paystubPicturefile = new File(rvm.paystubPicturePath.get());
        RequestBody paystubPictureBody = RequestBody.create(MediaType.parse("image/jpeg"),paystubPicturefile);
        MultipartBody.Part paystubs = MultipartBody.Part.createFormData("paystubs",paystubPicturefile.getName(), paystubPictureBody);

        return new MultipartBody.Part[] {
                firstName,middleInitial,lastName,ssn,sex,state,city,address,employmentStatus,zipCode,
                payFrequency,fundsSource,monthlyIncome,nextPaydate,employmentTitle,
                employmentEmployerName,employmentState,employmentZipCode,employmentCity,license,paystubs
        };
    }

    public static RegistrationViewModel UserProfileToRegistrationViewModel(UserProfile userProfile) {
        RegistrationViewModel rvm = new RegistrationViewModel();
        rvm.firstName.set(userProfile.first_name);
        rvm.middleInitial.set(userProfile.middle_initial);
        rvm.lastName.set(userProfile.last_name);
        rvm.ssn.set(userProfile.ssn);
        rvm.sex.set(sexStringToChoice(userProfile.sex));
        rvm.state.set(userProfile.state);
        rvm.city.set(userProfile.city);
        rvm.address.set(userProfile.address);
        rvm.employmentStatus.set(employmentStatusStringToChoice(userProfile.employment_status));
        rvm.zip.set(userProfile.zip_code);
        rvm.payFrequency.set(payFrequencyStringToChoice(userProfile.pay_frequency));
        if (rvm.employmentStatus.get() == RegistrationViewModel.EMPLOYMENT_STATUS_EMPLOYED)
            rvm.sourceOfIncome.set(userProfile.funds_source);
        else
            rvm.fundsSource.set(userProfile.funds_source);
        rvm.monthlyIncome.set(userProfile.monthly_income);
        rvm.nextPayDate.set(Utils.formatDateForDisplay(userProfile.next_paydate));
        rvm.roleTitle.set(userProfile.employment.title);
        rvm.placeOfEmployment.set(userProfile.employment.employer_name);
        rvm.employerState.set(userProfile.employment.state);
        rvm.employerZip.set(userProfile.employment.zip_code);
        return rvm;
    }

    private static int sexStringToChoice(String sexStr) {
        if (sexStr == null)
            return -1;
        return sexStr.equals("m") ? RegistrationViewModel.SEX_MALE : RegistrationViewModel.SEX_FEMALE;
    }

    private static String choiceToSexString(int sex) {
        return sex == RegistrationViewModel.SEX_MALE ? "m" : "f";
    }

    private static int employmentStatusStringToChoice(String statusStr) {
        if (statusStr == null)
            return -1;
        return statusStr.equals("employed") ? RegistrationViewModel.EMPLOYMENT_STATUS_EMPLOYED : RegistrationViewModel.EMPLOYMENT_STATUS_NOT_EMPLOYED;
    }

    private static String choiceToEmploymentStatusString(int status) {
        return status == RegistrationViewModel.EMPLOYMENT_STATUS_EMPLOYED ? "employed" : "unemployed";
    }

    private static int payFrequencyStringToChoice(String payFrequencyStr) {
        if (payFrequencyStr == null)
            return -1;
        switch (payFrequencyStr) {
            case "weekly": return RegistrationViewModel.PAY_FREQUENCY_WEEKLY;
            case "biweekly": return RegistrationViewModel.PAY_FREQUENCY_BIWEEKLY;
            case "monthly": return RegistrationViewModel.PAY_FREQUENCY_MONTHLY;
            default: return -1;
        }
    }

    private static String choiceToPayFrequencyString(int payFrequency) {
        switch (payFrequency) {
            case RegistrationViewModel.PAY_FREQUENCY_WEEKLY: return "weekly";
            case RegistrationViewModel.PAY_FREQUENCY_BIWEEKLY: return "biweekly";
            case RegistrationViewModel.PAY_FREQUENCY_MONTHLY: return "monthly";
            default: return "";
        }
    }

    public static UserStatus UserProfileStatusToUserStatus(String status) {
        if (status == null)
            return UserStatus.UNKNOWN;
        switch (status) {
            case "pending": return UserStatus.PENDING;
            case "approved": return UserStatus.APPROVED;
            case "denied": return UserStatus.DENIED;
            default: return UserStatus.UNKNOWN;
        }
    }
}
