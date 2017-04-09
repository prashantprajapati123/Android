package gigster.com.holdsum.presenters;

import android.content.res.Resources;

import org.junit.Before;
import org.junit.Test;

import gigster.com.holdsum.R;
import gigster.com.holdsum.activities.LoginActivity;
import gigster.com.holdsum.events.Events;
import gigster.com.holdsum.helper.FlowController;
import gigster.com.holdsum.model.AuthResponse;
import gigster.com.holdsum.model.UserProfile;
import gigster.com.holdsum.services.APIError;
import gigster.com.holdsum.services.RequestHandle;
import gigster.com.holdsum.services.RetrofitRequestHandle;
import gigster.com.holdsum.services.ServicesManager;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by tpaczesny on 2016-09-13.
 */
public class LoginPresenterTest extends AbstractHoldsumPresenterTest<LoginPresenter> {

    private LoginActivity mMockedLoginActivity;

    @Before
    public void setUp() {
        setUpBasicMocks(LoginPresenter.class, LoginActivity.class);
        mMockedLoginActivity = (LoginActivity) mMockedActivity;

        // mock resources returned by data managet to make sure we dont test debug behaviour
        Resources mockedResources = mock(Resources.class);
        when(mockedResources.getBoolean(R.bool.debug)).thenReturn(false);
        when(mMockedDataManger.getResources()).thenReturn(mockedResources);
    }

    @Test
    public void onLoginClicked_emptyCredentials_showError() {
        mPresenter.getData().username.set("");
        mPresenter.getData().password.set("");
        mPresenter.onLoginClicked();
        verify(mMockedLoginActivity).showMessage(R.string.invalid_username_or_pass);
    }

    @Test
    public void onLoginClicked_emptyUsername_showError() {
        mPresenter.getData().username.set("");
        mPresenter.getData().password.set("pass");
        mPresenter.onLoginClicked();
        verify(mMockedLoginActivity).showMessage(R.string.invalid_username_or_pass);
    }

    @Test
    public void onLoginClicked_emptyPassword_showError() {
        mPresenter.getData().username.set("user");
        mPresenter.getData().password.set("");
        mPresenter.onLoginClicked();
        verify(mMockedLoginActivity).showMessage(R.string.invalid_username_or_pass);
    }

    @Test
    public void onLoginClicked_anyCredentials_loginRequest() {
        mPresenter.getData().username.set("user");
        mPresenter.getData().password.set("pass");
        mPresenter.onLoginClicked();
        verify(mMockedLoginActivity).showProgressDialog(R.string.signing_in);
        verify(mMockedServicesManager).loginByUsernamePass("user", "pass");
    }

    @Test
    public void onSignupClicked_goToRegistrationScreen() {
        mPresenter.onSignupClicked();
        verify(mMockedFlowController).goTo(mMockedLoginActivity, FlowController.Screen.USER_SIGNUP, true);
    }

    @Test
    public void onLoginSuccess_keyExistsAndCompleteProfile_saveKeyAndDownloadProfile() {
        mPresenter.getData(); // make sure data object is created
        AuthResponse authResponse = new AuthResponse();
        authResponse.key = "123";
        authResponse.has_plaid = true;
        authResponse.has_profile = true;

        RequestHandle mockedRequestHandle = mock(RequestHandle.class);
        when(mockedRequestHandle.getType()).thenReturn(ServicesManager.RequestType.LOGIN);
        Events.ServiceRequestSuccess<AuthResponse> successEvent = new Events.ServiceRequestSuccess<>(mockedRequestHandle, authResponse);
        mPresenter.setCurrentRequest(mockedRequestHandle);

        mPresenter.onEvent(successEvent);

        verify(mMockedDataManger).setServerToken("123");
        verify(mMockedDataManger).setHasProfile(true);
        verify(mMockedDataManger).setHasPlaid(true);

        verify(mMockedServicesManager).getUserProfile();
    }

    @Test
    public void onLoginSuccess_keyExistsButNoProfile_saveKeyAndGoToRegistrationScreen() {
        mPresenter.getData(); // make sure data object is created
        AuthResponse authResponse = new AuthResponse();
        authResponse.key = "123";
        authResponse.has_plaid = false;
        authResponse.has_profile = false;

        RequestHandle mockedRequestHandle = mock(RequestHandle.class);
        when(mockedRequestHandle.getType()).thenReturn(ServicesManager.RequestType.LOGIN);
        Events.ServiceRequestSuccess<AuthResponse> successEvent = new Events.ServiceRequestSuccess<>(mockedRequestHandle, authResponse);
        mPresenter.setCurrentRequest(mockedRequestHandle);

        mPresenter.onEvent(successEvent);
        verify(mMockedLoginActivity).hideProgressDialog();
        verify(mMockedDataManger).setServerToken("123");
        verify(mMockedDataManger).setHasProfile(false);
        verify(mMockedDataManger).setHasPlaid(false);
        verify(mMockedFlowController).goTo(mMockedLoginActivity, FlowController.Screen.REGISTRATION, true);
    }

    @Test
    public void onLoginSuccess_keyExistsButPlaidTokenMissing_saveKeyAndGetUserProfile() {
        mPresenter.getData(); // make sure data object is created
        AuthResponse authResponse = new AuthResponse();
        authResponse.key = "123";
        authResponse.has_plaid = false;
        authResponse.has_profile = true;

        RequestHandle mockedRequestHandle = mock(RequestHandle.class);
        when(mockedRequestHandle.getType()).thenReturn(ServicesManager.RequestType.LOGIN);
        Events.ServiceRequestSuccess<AuthResponse> successEvent = new Events.ServiceRequestSuccess<>(mockedRequestHandle, authResponse);
        mPresenter.setCurrentRequest(mockedRequestHandle);

        mPresenter.onEvent(successEvent);
        verify(mMockedDataManger).setServerToken("123");
        verify(mMockedDataManger).setHasProfile(true);
        verify(mMockedDataManger).setHasPlaid(false);

        verify(mMockedServicesManager).getUserProfile();

    }

    @Test
    public void onLoginSuccess_keyNull_handleAsError() {
        AuthResponse authResponse = new AuthResponse();
        authResponse.key = null;
        RequestHandle mockedRequestHandle = mock(RequestHandle.class);
        when(mockedRequestHandle.getType()).thenReturn(ServicesManager.RequestType.LOGIN);
        Events.ServiceRequestSuccess<AuthResponse> successEvent = new Events.ServiceRequestSuccess<>(mockedRequestHandle, authResponse);
        mPresenter.setCurrentRequest(mockedRequestHandle);
        mPresenter.onEvent(successEvent);
        verify(mMockedLoginActivity).hideProgressDialog();
        verify(mMockedLoginActivity).showMessage(null, R.string.login_failed);
    }

    @Test
    public void onGetProfileSuccess_saveProfileAndGoToHomeScreen() {
        when(mMockedDataManger.getHasPlaid()).thenReturn(true);

        UserProfile userProfile = new UserProfile();
        userProfile.employment = new UserProfile.Employment();
        RequestHandle mockedRequestHandle = mock(RequestHandle.class);
        when(mockedRequestHandle.getType()).thenReturn(ServicesManager.RequestType.GET_USER_PROFILE);
        Events.ServiceRequestSuccess<UserProfile> successEvent = new Events.ServiceRequestSuccess<>(mockedRequestHandle, userProfile);
        mPresenter.setCurrentRequest(mockedRequestHandle);

        mPresenter.onEvent(successEvent);
        verify(mMockedLoginActivity).hideProgressDialog();
        verify(mMockedFlowController).setTo(mMockedLoginActivity, FlowController.Screen.HOME);
    }

    @Test
    public void onGetProfileSuccess_noPlaidToken_saveProfileAndGoToBankVerificationScreen() {
        when(mMockedDataManger.getHasPlaid()).thenReturn(false);

        UserProfile userProfile = new UserProfile();
        userProfile.employment = new UserProfile.Employment();
        RequestHandle mockedRequestHandle = mock(RequestHandle.class);
        when(mockedRequestHandle.getType()).thenReturn(ServicesManager.RequestType.GET_USER_PROFILE);
        Events.ServiceRequestSuccess<UserProfile> successEvent = new Events.ServiceRequestSuccess<>(mockedRequestHandle, userProfile);
        mPresenter.setCurrentRequest(mockedRequestHandle);

        mPresenter.onEvent(successEvent);
        verify(mMockedLoginActivity).hideProgressDialog();
        verify(mMockedFlowController).goTo(mMockedLoginActivity, FlowController.Screen.BANK_VERIFICATION, true);
    }


    @Test
    public void onLoginFailed_showError() {
        APIError mockApiError = mock(APIError.class);
        when(mockApiError.getMessage()).thenReturn("message");

        RequestHandle mockedRequestHandle = mock(RequestHandle.class);
        when(mockedRequestHandle.getType()).thenReturn(ServicesManager.RequestType.LOGIN);
        Events.ServiceRequestFailed failEvent = new Events.ServiceRequestFailed(mockedRequestHandle, mockApiError);
        mPresenter.setCurrentRequest(mockedRequestHandle);

        mPresenter.onEvent(failEvent);
        verify(mMockedLoginActivity).hideProgressDialog();
        verify(mMockedLoginActivity).showMessage("message", R.string.login_failed);
    }

}