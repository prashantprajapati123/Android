package gigster.com.holdsum.presenters;

import android.databinding.Observable;
import android.databinding.ObservableField;
import android.os.Bundle;

import org.greenrobot.eventbus.Subscribe;

import java.util.Calendar;

import gigster.com.holdsum.R;
import gigster.com.holdsum.events.Events;
import gigster.com.holdsum.fragments.BorrowFragment;
import gigster.com.holdsum.helper.FlowController;
import gigster.com.holdsum.helper.Logger;
import gigster.com.holdsum.helper.Utils;
import gigster.com.holdsum.model.LoanRequestResponse;
import gigster.com.holdsum.services.RequestHandle;
import gigster.com.holdsum.services.ServicesManager;
import gigster.com.holdsum.viewmodel.BorrowViewModel;
import gigster.com.holdsum.views.DatePickerEditText;

/**
 * Created by tpaczesny on 2016-09-02.
 */

public class BorrowPresenter extends HoldsumBasePresenter<BorrowFragment> {

    private RequestHandle mCurrentRequest;
    private BorrowViewModel mData;
    private boolean mOfferingRepaymentDateChange = false;

    @Override
    public void takeView(BorrowFragment view) {
        super.takeView(view);
        mEventBus.register(this);
    }

    @Override
    public void dropView() {
        mData.clear();
        initData();
        mEventBus.unregister(this);
        super.dropView();
    }

    public BorrowViewModel getData() {
        if (mData == null) {
            mData = new BorrowViewModel();
            initData();
        }

        return mData;
    }

    private void initData() {
        mData.fundsAvailable = mDataManager.getFundsAvailable();
        mData.repaymentDate.set(Utils.formatDateForDisplay(mDataManager.getDefaultPaybackDate()));
        mData.fundsSource = mDataManager.getFundsSource();
        mData.nextPaydate = Utils.formatDateForDisplay(mDataManager.getNextPaydate());

        mData.question2Answer.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable observable, int i) {
                question2SelectionChanged(((ObservableField<Integer>)observable).get());
            }
        });
        mData.question5Answer.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable observable, int i) {
                question5SelectionChanged(((ObservableField<Integer>)observable).get());
            }
        });
        mData.question7aAnswer.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable observable, int i) {
                question7aSelectionChanged(((ObservableField<Integer>)observable).get());
            }
        });
        mData.question7bAnswer.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable observable, int i) {
                question7bSelectionChanged(((ObservableField<Integer>)observable).get());
            }
        });
    }

    @Override
    public void onViewCreated() {
        super.onViewCreated();
        // needed so shown/hidden form parts are updated properly after activity restart
        question2SelectionChanged(getData().question2Answer.get());
        question5SelectionChanged(getData().question5Answer.get());
        question7aSelectionChanged(getData().question7aAnswer.get());
        question7bSelectionChanged(getData().question7bAnswer.get());

        mView.updateForUserStatus(mDataManager.getUserStatus());
    }

    public void onConfirmLoan() {
        Logger.i("BorrowPresenter", "onConfirmLoan()");
        if (mCurrentRequest != null)
            return;

        if (mData.hasAllRequiredFields()) {
            if (mData.hasAmountValid()) {
                if (!offerRepaymentDateChange()) {
                    mOfferingRepaymentDateChange = false;
                    mView.showProgressDialog(R.string.requesting_loan);
                    mDataManager.getServicesManager().loanRequest(mData);
                }
            } else {
                mView.showMessage(R.string.loan_amount_invalid);
            }
        } else {
            mView.showMessage(R.string.please_fill_all_fields);
        }

    }

    private boolean offerRepaymentDateChange() {
        if (mOfferingRepaymentDateChange)
            return false;

        if (mDataManager.getFollowingPaydate().after(Utils.parseDate(mData.repaymentDate.get()))) {
            double totalBills = Utils.safeParseDouble(mData.question7aTextbox.get()) + Utils.safeParseDouble(mData.question7bTextbox.get());
            double nextPaycheckAmount = mDataManager.getNextPaycheckAmount();
            if (totalBills >= nextPaycheckAmount * 0.8) {
                mOfferingRepaymentDateChange = true;
                mView.offerPaydateChange();
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    public void changeRepaymentDate() {
        mData.repaymentDate.set(Utils.formatDateForDisplay(mDataManager.getFollowingPaydate()));
    }

    public void offerRepaymentDateChangeEnded() {
        onConfirmLoan();
    }

    public void question2SelectionChanged(int position) {
        boolean own = (position == BorrowViewModel.QUESTION_2_TEXTBOX_ANSWER);
        if (own) {
            mView.showQuestion2TextBox();
        } else {
            mView.hideQuestion2TextBox();
        }
    }

    public void question5SelectionChanged(int position) {
        boolean na = (position == BorrowViewModel.QUESTION_5_ALT_ANSWER);
        if (na) {
            mView.showQuestion5Alt();
        } else {
            mView.hideQuestion5Alt();
        }
    }

    public void question7aSelectionChanged(int position) {
        boolean notNone = (position > -1 && position != BorrowViewModel.QUESTION_7_NO_TEXTBOX_ANSWER);
        if (notNone) {
            mView.showQuestion7aTextBox();
            mView.showQuestion7b();
        } else {
            mView.hideQuestion7aTextBox();
            mView.hideQuestion7b();
        }
    }

    public void question7bSelectionChanged(int position) {
        boolean notNone = (position > -1 && position != BorrowViewModel.QUESTION_7_NO_TEXTBOX_ANSWER);
        if (notNone) {
            mView.showQuestion7bTextBox();
        } else {
            mView.hideQuestion7bTextBox();
        }
    }

    public void onInvalidRepaymentDateValidationFail() {
        mView.showMessage(R.string.date_selection_error);
    }

    public DatePickerEditText.ValidationRule getRepaymentDateValidationRule() {
        return new DatePickerEditText.ValidationRule() {
            @Override
            public boolean validate(int year, int monthOfYear, int dayOfMonth) {
                //date not in the future
                Calendar calendar = Calendar.getInstance();
                int currentYear = calendar.get(Calendar.YEAR);
                int currentMonth = calendar.get(Calendar.MONTH);
                int currentDay = calendar.get(Calendar.DAY_OF_MONTH);
                return (year > currentYear || (year == currentYear && monthOfYear > currentMonth) || (year == currentYear && monthOfYear == currentMonth && dayOfMonth > currentDay));
            }
        };
    }

    @Subscribe
    public void onEvent(Events.ServiceRequestSuccess<LoanRequestResponse> event) {
        if (event.getType() == ServicesManager.RequestType.LOAN_REQUEST) {
            mCurrentRequest = null;
            mView.hideProgressDialog();
            Bundle args = new Bundle();
            args.putInt(FlowController.Arguments.LOAN_ID, event.body.id);
            mFlowController.goTo(mView, FlowController.Screen.CONFIRMATION, true, args);
        }
    }

    @Subscribe
    public void onEvent(Events.ServiceRequestFailed event) {
        if (event.getType() == ServicesManager.RequestType.LOAN_REQUEST) {
            mCurrentRequest = null;
            mView.hideProgressDialog();
            mView.showMessage(event.apiError.getMessage(), R.string.loan_request_error);
        }
    }

    @Subscribe
    public void onEvent(Events.UserStatusChanged event) {
        mView.updateForUserStatus(event.newValue);
    }

}
