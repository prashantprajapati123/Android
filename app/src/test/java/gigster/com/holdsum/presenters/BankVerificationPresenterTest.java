package gigster.com.holdsum.presenters;

import org.junit.Before;
import org.junit.Test;

import gigster.com.holdsum.R;
import gigster.com.holdsum.activities.BankVerificationActivity;
import gigster.com.holdsum.events.Events;
import gigster.com.holdsum.helper.FlowController;
import gigster.com.holdsum.services.RequestHandle;
import gigster.com.holdsum.services.RetrofitRequestHandle;
import gigster.com.holdsum.services.ServicesManager;

import static org.mockito.Mockito.verify;

/**
 * Created by tpaczesny on 2016-09-13.
 */
public class BankVerificationPresenterTest extends AbstractHoldsumPresenterTest<BankVerificationPresenter>{

    private BankVerificationActivity mMockedBankVerificationActivity;

    @Before
    public void setUp() {
        setUpBasicMocks(BankVerificationPresenter.class, BankVerificationActivity.class);
        mMockedBankVerificationActivity = (BankVerificationActivity) mMockedActivity;
    }

    @Test
    public void onExit_showErrorAndGoBack() {
        mPresenter.onVerificationExited();
        verify(mMockedBankVerificationActivity).showMessage(R.string.plaid_error);
        verify(mMockedFlowController).navigateBack(mMockedBankVerificationActivity);
    }

    @Test
    public void onSuccess_registerToken() {
        String token = "123";
        mPresenter.onVerificationSuccess(token);
        verify(mMockedServicesManager).updatePlaidToken("123");
        verify(mMockedBankVerificationActivity).disableVerificationUI();
        verify(mMockedBankVerificationActivity).showProgressDialog(R.string.registering_token);
    }

    @Test
    public void onRegistrationSuccess_goToHomeScreen() {
        mPresenter.onEvent(new Events.ServiceRequestSuccess<>(
                new RetrofitRequestHandle(null, ServicesManager.RequestType.UPDATE_PLAID_TOKEN),null));
        verify(mMockedBankVerificationActivity).hideProgressDialog();
        verify(mMockedFlowController).setTo(mMockedBankVerificationActivity, FlowController.Screen.HOME);
    }

    @Test
    public void onRegistrationFail_showErrorAndGoBack() {
        mPresenter.onEvent(new Events.ServiceRequestFailed(
                new RetrofitRequestHandle(null, ServicesManager.RequestType.UPDATE_PLAID_TOKEN)));
        verify(mMockedBankVerificationActivity).hideProgressDialog();
        verify(mMockedBankVerificationActivity).showMessage(R.string.token_registration_error);
        verify(mMockedFlowController).navigateBack(mMockedBankVerificationActivity);
    }

}