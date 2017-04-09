package gigster.com.holdsum.presenters;

import org.junit.Before;
import org.junit.Test;

import gigster.com.holdsum.R;
import gigster.com.holdsum.activities.UserSignupActivity;
import gigster.com.holdsum.events.Events;
import gigster.com.holdsum.helper.FlowController;
import gigster.com.holdsum.model.AuthResponse;
import gigster.com.holdsum.services.APIError;
import gigster.com.holdsum.services.RetrofitRequestHandle;
import gigster.com.holdsum.services.ServicesManager;
import gigster.com.holdsum.viewmodel.UserSignupViewModel;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by tpaczesny on 2016-10-03.
 */
public class UserSignupPresenterTest extends AbstractHoldsumPresenterTest<UserSignupPresenter> {

    private UserSignupActivity mMockedUserSignupActivity;

    @Before
    public void setUp() {
        setUpBasicMocks(UserSignupPresenter.class, UserSignupActivity.class);
        mMockedUserSignupActivity = (UserSignupActivity) mMockedActivity;
    }

    private void fillDataWithValidData(UserSignupViewModel data) {
        data.username.set("user");
        data.email.set("email@test.com");
        data.password.set("123456");
        data.confirmPassword.set("123456");
    }

    @Test
    public void onNextClicked_usernameEmpty_showError() {
        UserSignupViewModel data = mPresenter.getData();
        fillDataWithValidData(data);
        data.username.set("");

        mPresenter.onNextClicked();
        verify(mMockedUserSignupActivity).showMessage(R.string.empty_credentials_error);
    }

    @Test
    public void onNextClicked_emailEmpty_showError() {
        UserSignupViewModel data = mPresenter.getData();
        fillDataWithValidData(data);
        data.email.set("");

        mPresenter.onNextClicked();
        verify(mMockedUserSignupActivity).showMessage(R.string.empty_credentials_error);
    }

    @Test
    public void onNextClicked_passwordEmpty_showError() {
        UserSignupViewModel data = mPresenter.getData();
        fillDataWithValidData(data);
        data.password.set("");

        mPresenter.onNextClicked();
        verify(mMockedUserSignupActivity).showMessage(R.string.empty_credentials_error);
    }

    @Test
    public void onNextClicked_passwordMismatch_showError() {
        UserSignupViewModel data = mPresenter.getData();
        fillDataWithValidData(data);
        data.confirmPassword.set("!123456");

        mPresenter.onNextClicked();
        verify(mMockedUserSignupActivity).showMessage(R.string.mismatch_passwords_error);
    }

    @Test
    public void onNextClicked_passwordTooShort_showError() {
        UserSignupViewModel data = mPresenter.getData();
        fillDataWithValidData(data);
        data.password.set("12345");
        data.confirmPassword.set("12345");

        mPresenter.onNextClicked();
        verify(mMockedUserSignupActivity).showMessage(R.string.too_short_password_error);
    }

    @Test
    public void onNextClicked_dataOk_registerRequest() {
        UserSignupViewModel data = mPresenter.getData();
        fillDataWithValidData(data);

        mPresenter.onNextClicked();
        verify(mMockedUserSignupActivity).showProgressDialog(R.string.registering_username);
        verify(mMockedServicesManager).registerByUsernamePass(data.username.get(),
                data.email.get(),data.password.get());
    }

    @Test
    public void onRequestSuccess_saveTokenHideProgressNavigateToRegistration() {
        mPresenter.getData();
        AuthResponse obj = new AuthResponse();
        obj.key = "123";
        mPresenter.onEvent(new Events.ServiceRequestSuccess<>(
                new RetrofitRequestHandle(null, ServicesManager.RequestType.REGISTER), obj));

        verify(mMockedDataManger).setServerToken("123");
        verify(mMockedUserSignupActivity).hideProgressDialog();
        verify(mMockedFlowController).goTo(mMockedUserSignupActivity, FlowController.Screen.REGISTRATION, false);
    }

    @Test
    public void onSignupFailed_showError() {
        APIError mockApiError = mock(APIError.class);
        when(mockApiError.getMessage()).thenReturn("message");

        Events.ServiceRequestFailed failEvent = new Events.ServiceRequestFailed(
                new RetrofitRequestHandle(null, ServicesManager.RequestType.REGISTER), mockApiError);
        mPresenter.onEvent(failEvent);
        verify(mMockedUserSignupActivity).hideProgressDialog();
        verify(mMockedUserSignupActivity).showMessage("message", R.string.username_registration_error);
    }

}