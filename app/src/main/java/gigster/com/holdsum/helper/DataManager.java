package gigster.com.holdsum.helper;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.util.Calendar;
import java.util.Date;
import java.util.ArrayList;

import gigster.com.holdsum.R;
import gigster.com.holdsum.events.Events;
import gigster.com.holdsum.helper.enums.Mode;
import gigster.com.holdsum.helper.enums.UserStatus;
import gigster.com.holdsum.model.LoanRequest;
import gigster.com.holdsum.services.ServicesManager;
import gigster.com.holdsum.viewmodel.IntroViewModel;
import gigster.com.holdsum.viewmodel.RegistrationViewModel;
import gigster.com.holdsum.viewmodel.SummaryStepViewModel;

/**
 * Created by tpaczesny on 2016-09-03.
 */
public class DataManager {

    private static DataManager sInstance;

    public static DataManager getInstance() {
        if (sInstance == null)
            sInstance = new DataManager();
        return sInstance;
    }

    //  app data
    private Mode mMode;
    private String mServerToken;
    private RegistrationViewModel mRegistrationViewModel;
    private ArrayList<LoanRequest> loans;
    private boolean mHasProfile;
    private boolean mHasPlaid;
    private UserStatus mUserStatus;

    // helper objects
    private EventBus mEventBus;
    private ServicesManager mServicesManager;
    private DateFactory mDateFactory;
    private Context mAppContext;

    public static final String PREFERENCES_FILE = "DataManager";
    public static final String KEY_SERVER_TOKEN = "server_token";
    public static final String KEY_HAS_PROFILE = "has_profile";
    public static final String KEY_HAS_PLAID = "has_plaid";
    public static final String KEY_USER_STATUS = "user_status";
    private static final String REGISTRATION_FILE_NAME = "reg.dat";
    private static final String LOANS_FILE_NAME = "loans.dat";


    public void init(Context appContext, EventBus eventBus, ServicesManager servicesManager) {
        mAppContext = appContext;
        mEventBus = eventBus;
        mServicesManager = servicesManager;
        mDateFactory = new DateFactory();
        SharedPreferences pref = mAppContext.getSharedPreferences(PREFERENCES_FILE, Context.MODE_PRIVATE);
        mServerToken = pref.getString(KEY_SERVER_TOKEN, null);
        mHasProfile = pref.getBoolean(KEY_HAS_PROFILE, false);
        mHasPlaid = pref.getBoolean(KEY_HAS_PLAID, false);
        mUserStatus = UserStatus.safeValueOf(pref.getString(KEY_USER_STATUS, UserStatus.UNKNOWN.name()));
    }

    public void setDateFactory(DateFactory dateFactory) {
        mDateFactory = dateFactory;
    }

    // Local state

    public Mode getMode() {
        return mMode;
    }

    public void setMode(Mode mode) {
        mMode = mode;
        mEventBus.post(new Events.ModeChanged(mode));
    }

    public void setServerToken(String token) {
        mServerToken = token;
        mAppContext.getSharedPreferences(PREFERENCES_FILE, Context.MODE_PRIVATE).edit().putString(KEY_SERVER_TOKEN, token).apply();
    }

    public void clearServerToken() {
        mServerToken = null;
        mAppContext.getSharedPreferences(PREFERENCES_FILE, Context.MODE_PRIVATE).edit().remove(KEY_SERVER_TOKEN).apply();
    }

    public String getServerToken() {
        return mServerToken;
    }

    public void setHasProfile(boolean hasProfile) {
        mHasProfile = hasProfile;
        mAppContext.getSharedPreferences(PREFERENCES_FILE, Context.MODE_PRIVATE).edit().putBoolean(KEY_HAS_PROFILE, hasProfile).apply();
    }

    public boolean getHasProfile() {
        return mHasProfile;
    }

    public void setHasPlaid(boolean hasPlaid) {
        mHasPlaid = hasPlaid;
        mAppContext.getSharedPreferences(PREFERENCES_FILE, Context.MODE_PRIVATE).edit().putBoolean(KEY_HAS_PLAID, hasPlaid).apply();
    }

    public boolean getHasPlaid() {
        return mHasPlaid;
    }

    public void setUserStatus(UserStatus userStatus) {
        boolean changed = mUserStatus != userStatus;
        mUserStatus = userStatus;
        mAppContext.getSharedPreferences(PREFERENCES_FILE, Context.MODE_PRIVATE).edit().putString(KEY_USER_STATUS, userStatus.name()).apply();
        if (changed)
            mEventBus.post(new Events.UserStatusChanged(mUserStatus));
    }

    public UserStatus getUserStatus() {
        return mUserStatus;
    }

    public ArrayList<LoanRequest> getLoans() {
        if (loans == null) {
            loans = (ArrayList) Utils.loadFromFile(getLoansDataFile());
        }
        return loans;
    }

    public void setLoans(ArrayList<LoanRequest> loans) {
        this.loans = loans;
        if (loans != null)
            Utils.serializeToFile(this.loans, getLoansDataFile());
        else
            getLoansDataFile().delete();
    }

    public RegistrationViewModel getUserRegistrationData() {
        if (mRegistrationViewModel == null) {
            mRegistrationViewModel = (RegistrationViewModel) Utils.loadFromFile(getRegistrationDataFile());
        }
        return mRegistrationViewModel;
    }

    public void setUserRegistrationData(RegistrationViewModel registrationViewModel) {
        mRegistrationViewModel = registrationViewModel;
        if (mRegistrationViewModel != null)
            Utils.serializeToFile(mRegistrationViewModel, getRegistrationDataFile());
        else
            getRegistrationDataFile().delete();
    }

    private File getLoansDataFile() {
        return new File(mAppContext.getFilesDir(), LOANS_FILE_NAME);
    }

    private File getRegistrationDataFile() {
        return new File(mAppContext.getFilesDir(), REGISTRATION_FILE_NAME);
    }

    public void clearAllData() {
        clearServerToken();
        setHasPlaid(false);
        setHasProfile(false);
        setUserRegistrationData(null);
    }

    // Business logic


    public String getFundsSource() {
        return getUserRegistrationData().fundsSource.get();
    }

    public String getFundsAvailable() {
        double monthlyIncome = Utils.safeParseDouble(getUserRegistrationData().monthlyIncome.get());
        double yearlyIncome = monthlyIncome * 12;
        if (yearlyIncome >= 35000)
            return "500";
        else if (yearlyIncome >= 30000)
            return "400";
        else if (yearlyIncome >= 25000)
            return "300";
        else if (yearlyIncome >= 20000)
            return "200";
        else if (yearlyIncome >= 17000)
            return "100";
        else
            return "0";
    }

    public Date getNextPaydate() {
        RegistrationViewModel registrationData = getUserRegistrationData();

        Date now = getNow();

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(Utils.parseDate(registrationData.nextPayDate.get()));

        int paymentTimeUnit;
        int paymentTimeStep;

        switch (registrationData.payFrequency.get()) {
            case RegistrationViewModel.PAY_FREQUENCY_WEEKLY:
                paymentTimeUnit = Calendar.WEEK_OF_MONTH;
                paymentTimeStep = 1;
                break;
            case RegistrationViewModel.PAY_FREQUENCY_BIWEEKLY:
                paymentTimeUnit = Calendar.WEEK_OF_MONTH;
                paymentTimeStep = 2;
                break;
            case RegistrationViewModel.PAY_FREQUENCY_MONTHLY:
            default:
                paymentTimeUnit = Calendar.MONTH;
                paymentTimeStep = 1;
                break;
        }

        while(now.after(calendar.getTime())) {
            calendar.add(paymentTimeUnit, paymentTimeStep);
        }

        return calendar.getTime();
    }

    public Date getFollowingPaydate() {
        RegistrationViewModel registrationData = getUserRegistrationData();

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(getNextPaydate());

        int paymentTimeUnit;
        int paymentTimeStep;

        switch (registrationData.payFrequency.get()) {
            case RegistrationViewModel.PAY_FREQUENCY_WEEKLY:
                paymentTimeUnit = Calendar.WEEK_OF_MONTH;
                paymentTimeStep = 1;
                break;
            case RegistrationViewModel.PAY_FREQUENCY_BIWEEKLY:
                paymentTimeUnit = Calendar.WEEK_OF_MONTH;
                paymentTimeStep = 2;
                break;
            case RegistrationViewModel.PAY_FREQUENCY_MONTHLY:
            default:
                paymentTimeUnit = Calendar.MONTH;
                paymentTimeStep = 1;
                break;
        }

        calendar.add(paymentTimeUnit, paymentTimeStep);

        return calendar.getTime();
    }

    public double getNextPaycheckAmount() {
        RegistrationViewModel registrationData = getUserRegistrationData();
        double monthlyIncome = Double.parseDouble(registrationData.monthlyIncome.get());
        switch (registrationData.payFrequency.get()) {
            case RegistrationViewModel.PAY_FREQUENCY_WEEKLY:
                return monthlyIncome/4.0;
            case RegistrationViewModel.PAY_FREQUENCY_BIWEEKLY:
                return monthlyIncome/2.0;
            case RegistrationViewModel.PAY_FREQUENCY_MONTHLY:
            default:
                return monthlyIncome;
        }
    }

    /**
     * From spec:
     * [PAYBACK DATE CALCULATED –BORROWERS NEXT PAYPERIOD IF NEXT PAY DATE IS > 7 DAYS, IF BORROWERS
     * NEXT PAY DATE IS < 7 DAYS THEN THIS DATE IS THE BORROWERS FOLLOWING PAY DATE (CALC’D BY THE
     * DATE THE BORROWER ENTERS AS THEIR NEXT PAY DATE & BY THEIR PAY FREQUENCEY)]
     * @return
     */

    public Date getDefaultPaybackDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(getNow());
        calendar.add(Calendar.DAY_OF_MONTH, 7);

        Date nextPaydate = getNextPaydate();

        if (calendar.getTime().before(nextPaydate))
            return nextPaydate;
        else
            return getFollowingPaydate();
    }

    public Date getNow() {
        return mDateFactory.getNow();
    }

    // Backend API
    public ServicesManager getServicesManager() {
        return mServicesManager;
    }

    // Static data

    public Resources getResources() {
        return mAppContext.getResources();
    }

    /**
     * Creates static intro data depending on the invest/borrow mode
     * @return
     */
    public IntroViewModel getInfoForCurrentMode() {
        IntroViewModel info = new IntroViewModel();
        switch (getMode()) {
            case INVEST:
                info.headerText = R.string.invest_3_steps;
                info.step1Text = R.string.tell_us_about_you;
                info.step2Text = R.string.become_lender_part;
                info.step3Defined = false;
                break;
            case BORROW:
                info.headerText = R.string.borrow_in_3_simple_steps;
                info.step1Text = R.string.register_your_account;
                info.step2Text = R.string.request_a_loan_up_to_500;
                info.step3Defined = true;
                info.step3Text = R.string.agree_e_sign_repayment_agreement;
                break;
        }
        return info;
    }

    /**
     * Creates static steps data depending on the invest/borrow mode
     * @return
     */
    public SummaryStepViewModel[] getStepsForCurrentMode() {
        SummaryStepViewModel[] steps;
        Mode mode = getMode();
        if (mode == Mode.INVEST) {
            steps = new SummaryStepViewModel[2];
        } else {
            steps = new SummaryStepViewModel[3];
        }

        // first step
        steps[0] = new SummaryStepViewModel();
        steps[0].headerText = (mode == Mode.INVEST ? R.string.tell_us_about_you : R.string.register_your_account);
        steps[0].headerImage = R.drawable.ic_one;
        steps[0].firstDefined = true;
        steps[0].firstText = R.string.enter_demographic_and_income_information;
        steps[0].firstImage = R.drawable.ic_info;
        steps[0].secondDefined = true;
        steps[0].secondText = R.string.upload_necessary_documentation;
        steps[0].secondImage = R.drawable.ic_upload;
        switch (mode) {
            case INVEST:
                steps[0].thirdDefined = false;
                steps[0].fourthDefined = false;
                steps[0].thirdText = R.string.empty;
                steps[0].fourthText = R.string.empty;
                break;
            case BORROW:
                steps[0].thirdDefined = true;
                steps[0].thirdText = R.string.link_your_checking_paypal_accounts;
                steps[0].thirdImage = R.drawable.ic_link;
                steps[0].fourthDefined = true;
                steps[0].fourthText = R.string.get_approved;
                steps[0].fourthImage = R.drawable.ic_approved;
                break;
        }

        // second step
        steps[1] = new SummaryStepViewModel();
        switch (mode) {
            case INVEST:
                steps[1].headerImage = R.drawable.ic_two;
                steps[1].headerText = R.string.become_lender_part;
                steps[1].firstText = R.string.invest_in_holdsum;
                steps[1].firstImage = R.drawable.ic_upload;
                steps[1].firstDefined = true;
                steps[1].secondDefined = false;
                steps[1].secondText = R.string.empty;
                steps[1].thirdDefined = false;
                steps[1].thirdText = R.string.empty;
                steps[1].fourthDefined = false;
                steps[1].fourthText = R.string.empty;
                break;
            case BORROW:
                steps[1].headerImage = R.drawable.ic_two;
                steps[1].headerText = R.string.request_a_loan_up_to_500;
                steps[1].firstText = R.string.enter_loan_amount;
                steps[1].firstImage = R.drawable.ic_cash;
                steps[1].secondText = R.string.approve_amout;
                steps[1].secondImage = R.drawable.ic_date;
                steps[1].firstDefined = true;
                steps[1].secondDefined = true;
                steps[1].thirdDefined = false;
                steps[1].thirdText = R.string.empty;
                steps[1].fourthDefined = false;
                steps[1].fourthText = R.string.empty;
                break;
        }

        // third step
        if (mode == Mode.BORROW) {
            steps[2] = new SummaryStepViewModel();
            steps[2].headerImage = R.drawable.ic_three;
            steps[2].headerText = R.string.agree_e_sign_repayment_agreement;
            steps[2].firstText = R.string.esign_your_loan;
            steps[2].firstImage = R.drawable.ic_sign;
            steps[2].secondText = R.string.receive_cash;
            steps[2].secondImage = R.drawable.ic_cash;
            steps[2].firstDefined = true;
            steps[2].secondDefined = true;
            steps[2].thirdDefined = false;
            steps[2].thirdText = R.string.empty;
            steps[2].fourthDefined = false;
            steps[2].fourthText = R.string.empty;
        }

        return steps;
    }



}
