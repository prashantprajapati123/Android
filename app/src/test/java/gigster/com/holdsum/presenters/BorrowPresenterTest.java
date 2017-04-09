package gigster.com.holdsum.presenters;

import android.os.Bundle;

import org.junit.Before;
import org.junit.Test;

import java.util.Calendar;
import java.util.Date;

import gigster.com.holdsum.R;
import gigster.com.holdsum.events.Events;
import gigster.com.holdsum.fragments.BorrowFragment;
import gigster.com.holdsum.helper.FlowController;
import gigster.com.holdsum.helper.Utils;
import gigster.com.holdsum.helper.enums.UserStatus;
import gigster.com.holdsum.model.LoanRequestResponse;
import gigster.com.holdsum.services.APIError;
import gigster.com.holdsum.services.RetrofitRequestHandle;
import gigster.com.holdsum.services.ServicesManager;
import gigster.com.holdsum.viewmodel.BorrowViewModel;
import gigster.com.holdsum.views.DatePickerEditText;

import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by tpaczesny on 2016-09-13.
 */
public class BorrowPresenterTest extends AbstractHoldsumPresenterTest<BorrowPresenter> {

    private BorrowFragment mMockedBorrowFragment;

    @Before
    public void setUp() {
        setUpBasicMocks(BorrowPresenter.class, BorrowFragment.class);
        mMockedBorrowFragment = (BorrowFragment) mMockedFragment;

        Calendar calendar = Calendar.getInstance();
        calendar.set(2016,Calendar.OCTOBER, 1);
        Date mockedNow = calendar.getTime();
        calendar.set(2016,Calendar.OCTOBER, 14);
        Date mockedPaybackDate = calendar.getTime();
        calendar.set(2016,Calendar.NOVEMBER, 1);
        Date mockedNextPayDate = calendar.getTime();
        calendar.set(2016,Calendar.DECEMBER, 1);
        Date mockedFollowingPayDate = calendar.getTime();

        when(mMockedDataManger.getDefaultPaybackDate()).thenReturn(mockedPaybackDate);
        when(mMockedDataManger.getFundsAvailable()).thenReturn("500");
        when(mMockedDataManger.getFundsSource()).thenReturn("Savings");
        when(mMockedDataManger.getNextPaydate()).thenReturn(mockedNextPayDate);
        when(mMockedDataManger.getFollowingPaydate()).thenReturn(mockedFollowingPayDate);
        when(mMockedDataManger.getNextPaycheckAmount()).thenReturn(10000.0);
    }

    private void fillDataWithValidData(BorrowViewModel data) {
        data.amount.set("200");
        data.question1Answer.set(0);
        data.question2Answer.set(0);
        data.question2Textbox.set("Text");
        data.question3Answer.set(0);
        data.question4Answer.set(0);
        data.question5Answer.set(0);
        data.question6Answer.set(0);
        data.question7aAnswer.set(0);
        data.question7aTextbox.set("500");
        data.question7bAnswer.set(0);
        data.question7bTextbox.set("1000");
    }

    @Test
    public void onConfirmLoan_allDataCorrect_submitLoanRequest() {
        BorrowViewModel data = mPresenter.getData(); // make sure data object is created
        fillDataWithValidData(data);

        mPresenter.onConfirmLoan();
        verify(mMockedServicesManager).loanRequest(any(BorrowViewModel.class));
        verify(mMockedBorrowFragment).showProgressDialog(R.string.requesting_loan);
    }

    @Test
    public void onConfirmLoan_missingData1_showError() {
        BorrowViewModel data = mPresenter.getData(); // make sure data object is created

        mPresenter.onConfirmLoan();
        verify(mMockedBorrowFragment).showMessage(R.string.please_fill_all_fields);
    }

    @Test
    public void onConfirmLoan_missingData2_showError() {
        BorrowViewModel data = mPresenter.getData(); // make sure data object is created
        fillDataWithValidData(data);
        data.question1Answer.set(-1);

        mPresenter.onConfirmLoan();
        verify(mMockedBorrowFragment).showMessage(R.string.please_fill_all_fields);
    }

    @Test
    public void onConfirmLoan_missingData3_showError() {
        BorrowViewModel data = mPresenter.getData(); // make sure data object is created
        fillDataWithValidData(data);
        data.question2Answer.set(0);
        data.question2Textbox.set("");

        mPresenter.onConfirmLoan();
        verify(mMockedBorrowFragment).showMessage(R.string.please_fill_all_fields);
    }

    @Test
    public void onConfirmLoan_amount_too_big_showError() {
        when(mMockedDataManger.getFundsAvailable()).thenReturn("500");
        BorrowViewModel data = mPresenter.getData(); // make sure data object is created
        fillDataWithValidData(data);
        data.amount.set("501");

        mPresenter.onConfirmLoan();
        verify(mMockedBorrowFragment).showMessage(R.string.loan_amount_invalid);
    }

    @Test
    public void onConfirmLoan_billsTooBig_showPaybackDateChangeOffer() {
        when(mMockedDataManger.getNextPaycheckAmount()).thenReturn(10000.0);
        BorrowViewModel data = mPresenter.getData(); // make sure data object is created
        fillDataWithValidData(data);
        data.question7aTextbox.set("7000");
        data.question7bTextbox.set("1000");

        mPresenter.onConfirmLoan();
        verify(mMockedBorrowFragment).offerPaydateChange();
    }

    @Test
    public void onConfirmLoan_billsTooBig2_showPaybackDateChangeOffer() {
        when(mMockedDataManger.getNextPaycheckAmount()).thenReturn(10000.0);
        BorrowViewModel data = mPresenter.getData(); // make sure data object is created
        fillDataWithValidData(data);
        data.question7aTextbox.set("7000");
        data.question7bTextbox.set("7000");

        mPresenter.onConfirmLoan();
        verify(mMockedBorrowFragment).offerPaydateChange();
    }

    @Test
    public void changeRepaymentDate_followingPaycheckDate() {
        BorrowViewModel data = mPresenter.getData();
        mPresenter.changeRepaymentDate();

        assertEquals(data.repaymentDate.get(), Utils.formatDateForDisplay(mMockedDataManger.getFollowingPaydate()));
    }

    @Test
    public void onInvalidRepaymentDateValidationFail_showError() {
        mPresenter.onInvalidRepaymentDateValidationFail();
        verify(mMockedBorrowFragment).showMessage(R.string.date_selection_error);
    }

    @Test
    public void repaymentDateValidationRule_futureDate_valid() {
        DatePickerEditText.ValidationRule validationRule = mPresenter.getRepaymentDateValidationRule();
        Calendar now = Calendar.getInstance();
        now.add(Calendar.DAY_OF_MONTH, 1);
        assertTrue(validationRule.validate(now.get(Calendar.YEAR), now.get(Calendar.MONTH), now.get(Calendar.DAY_OF_MONTH)+1));
    }

    @Test
    public void repaymentDateValidationRule_today_invalid() {
        DatePickerEditText.ValidationRule validationRule = mPresenter.getRepaymentDateValidationRule();
        Calendar now = Calendar.getInstance();
        assertFalse(validationRule.validate(now.get(Calendar.YEAR), now.get(Calendar.MONTH), now.get(Calendar.DAY_OF_MONTH)));
    }

    @Test
    public void repaymentDateValidationRule_pastYearDate_invalid() {
        DatePickerEditText.ValidationRule validationRule = mPresenter.getRepaymentDateValidationRule();
        Calendar now = Calendar.getInstance();
        now.add(Calendar.YEAR, -1);
        assertFalse(validationRule.validate(now.get(Calendar.YEAR), now.get(Calendar.MONTH), now.get(Calendar.DAY_OF_MONTH)));
    }

    @Test
    public void repaymentDateValidationRule_pastMonthDate_invalid() {
        DatePickerEditText.ValidationRule validationRule = mPresenter.getRepaymentDateValidationRule();
        Calendar now = Calendar.getInstance();
        now.add(Calendar.MONTH, -1);
        assertFalse(validationRule.validate(now.get(Calendar.YEAR), now.get(Calendar.MONTH), now.get(Calendar.DAY_OF_MONTH)));
    }

    @Test
    public void repaymentDateValidationRule_pastDayDate_invalid() {
        DatePickerEditText.ValidationRule validationRule = mPresenter.getRepaymentDateValidationRule();
        Calendar now = Calendar.getInstance();
        now.add(Calendar.DAY_OF_MONTH, -1);
        assertFalse(validationRule.validate(now.get(Calendar.YEAR), now.get(Calendar.MONTH), now.get(Calendar.DAY_OF_MONTH)+1));
    }

    @Test
    public void onRequestSuccess_hideProgressNavigateToConfirmation() {
        mPresenter.getData();
        LoanRequestResponse response = new LoanRequestResponse();
        mPresenter.onEvent(new Events.ServiceRequestSuccess<>(
                new RetrofitRequestHandle(null, ServicesManager.RequestType.LOAN_REQUEST), response));

        verify(mMockedBorrowFragment).hideProgressDialog();
        verify(mMockedFlowController).goTo(eq(mMockedBorrowFragment), eq(FlowController.Screen.CONFIRMATION), eq(true), any(Bundle.class));
    }

    @Test
    public void onSignupFailed_showError() {
        APIError mockApiError = mock(APIError.class);
        when(mockApiError.getMessage()).thenReturn("message");

        Events.ServiceRequestFailed failEvent = new Events.ServiceRequestFailed(
                new RetrofitRequestHandle(null, ServicesManager.RequestType.LOAN_REQUEST), mockApiError);
        mPresenter.onEvent(failEvent);
        verify(mMockedBorrowFragment).hideProgressDialog();
        verify(mMockedBorrowFragment).showMessage("message", R.string.loan_request_error);
    }

    @Test
    public void onUserStatusChange_updateUI() {
        mPresenter.onEvent(new Events.UserStatusChanged(UserStatus.APPROVED));
        verify(mMockedBorrowFragment).updateForUserStatus(UserStatus.APPROVED);
    }

}