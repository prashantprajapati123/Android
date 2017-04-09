package gigster.com.holdsum.presenters;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.login.LoginManager;

import org.greenrobot.eventbus.Subscribe;

import gigster.com.holdsum.R;
import gigster.com.holdsum.activities.LoginActivity;
import gigster.com.holdsum.events.Events;
import gigster.com.holdsum.helper.DataConverter;
import gigster.com.holdsum.helper.FlowController;
import gigster.com.holdsum.model.AuthResponse;
import gigster.com.holdsum.model.UserProfile;
import gigster.com.holdsum.services.RequestHandle;
import gigster.com.holdsum.services.ServicesManager;
import gigster.com.holdsum.viewmodel.LoginViewModel;

/**
 * Created by tpaczesny on 2016-09-02.
 */
public class LoginPresenter extends HoldsumBasePresenter<LoginActivity> {

    private RequestHandle mCurrentRequest;
    private AccessTokenTracker accessTokenTracker;
    private LoginViewModel mData;

    @Override
    public void setUp() {
        super.setUp();
        accessTokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(
                    AccessToken oldAccessToken,
                    AccessToken currentAccessToken) {
                if (currentAccessToken != null) {
                    mView.showProgressDialog(R.string.signing_in);
                    mCurrentRequest = mDataManager.getServicesManager().registerByFacebook(currentAccessToken.getToken());
                }
            }
        };
    }

    @Override
    public void takeView(LoginActivity view) {
        super.takeView(view);
        mEventBus.register(this);
        getData().clear();
    }

    @Override
    public void dropView() {
        mEventBus.unregister(this);
        super.dropView();
    }

    @Override
    public void onViewDestroyed() {
        super.onViewDestroyed();
        if (mCurrentRequest != null && !mCurrentRequest.hasEnded()) {
            mCurrentRequest.cancel();
        }

        accessTokenTracker.startTracking();
    }

    public LoginViewModel getData() {
        if (mData == null)
            mData = new LoginViewModel();
        return mData;
    }

    public void onLoginClicked() {
        if (mCurrentRequest != null && !mCurrentRequest.hasEnded())
            return;

        if (mData.hasDataFilled()) {
            mView.showProgressDialog(R.string.signing_in);
            mCurrentRequest = mDataManager.getServicesManager().loginByUsernamePass(
                    mData.username.get().trim(),mData.password.get());
        } else {
            mView.showMessage(R.string.invalid_username_or_pass);
        }
    }

    public void onSignupClicked() {
        mFlowController.goTo(mView, FlowController.Screen.USER_SIGNUP, true);
    }

    public void setCurrentRequest(RequestHandle requestHandle) {
        mCurrentRequest = requestHandle;
    }

    @Subscribe
    public void onEvent(Events.ServiceRequestSuccess event) {
        if (event.getType() == ServicesManager.RequestType.LOGIN ||
                event.getType() == ServicesManager.RequestType.REGISTER_BY_FB) { // login result
            AuthResponse authResp = (AuthResponse) event.body;
            String key = authResp.key;
            if (key != null) {
                mData.clear();
                mDataManager.setServerToken(key);
                mDataManager.setHasProfile(authResp.has_profile);
                mDataManager.setHasPlaid(authResp.has_plaid);
                
                if (authResp.has_profile) {
                    // retrieve profile during login
                    setCurrentRequest(mDataManager.getServicesManager().getUserProfile());
                } else {
                    if (mView != null) {
                        mView.hideProgressDialog();
                        mFlowController.goTo(mView, FlowController.Screen.REGISTRATION, true);
                    }
                }

            } else {
                onEvent(new Events.ServiceRequestFailed(event.requestHandle));
            }
        } else if (event.getType() == ServicesManager.RequestType.GET_USER_PROFILE) {
            UserProfile userProfile = (UserProfile) event.body;
            mDataManager.setUserRegistrationData(DataConverter.UserProfileToRegistrationViewModel(userProfile));
            mDataManager.setUserStatus(DataConverter.UserProfileStatusToUserStatus(userProfile.status));
            if (mView != null) {
                mView.hideProgressDialog();
                if (!mDataManager.getHasPlaid()) {
                    mFlowController.goTo(mView, FlowController.Screen.BANK_VERIFICATION, true);
                } else {
                    setCurrentRequest(mDataManager.getServicesManager().getLoans());
                }
            }
        } else if (event.getType() == ServicesManager.RequestType.GET_LOANS) {
            mDataManager.setLoans((java.util.ArrayList)event.body);
            mFlowController.setTo(mView, FlowController.Screen.HOME);
        }
        mCurrentRequest = null;
    }

    @Subscribe
    public void onEvent(Events.ServiceRequestFailed event) {
        if (mView != null) {
            mView.hideProgressDialog();
            mView.showMessage(event.apiError.getMessage(), R.string.login_failed);
        }
        if (event.getType() == ServicesManager.RequestType.REGISTER_BY_FB) {
            LoginManager.getInstance().logOut();
        }
        mCurrentRequest = null;
    }

}
