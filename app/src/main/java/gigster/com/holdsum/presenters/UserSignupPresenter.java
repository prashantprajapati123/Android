package gigster.com.holdsum.presenters;

import org.greenrobot.eventbus.Subscribe;

import gigster.com.holdsum.R;
import gigster.com.holdsum.activities.UserSignupActivity;
import gigster.com.holdsum.events.Events;
import gigster.com.holdsum.helper.FlowController;
import gigster.com.holdsum.model.AuthResponse;
import gigster.com.holdsum.services.RequestHandle;
import gigster.com.holdsum.services.ServicesManager;
import gigster.com.holdsum.viewmodel.UserSignupViewModel;

/**
 * Created by tpaczesny on 2016-09-02.
 */
public class UserSignupPresenter extends HoldsumBasePresenter<UserSignupActivity> {

    private RequestHandle mCurrentRequest;
    private RequestHandle mCurrentRequestBackup;
    private UserSignupViewModel mData;

    @Override
    public void onViewDestroyed() {
        super.onViewDestroyed();
        getData().clear();
    }

    @Override
    public void takeView(UserSignupActivity view) {
        super.takeView(view);
        mEventBus.register(this);
    }

    @Override
    public void dropView() {
        mEventBus.unregister(this);
        super.dropView();
    }

    public UserSignupViewModel getData() {
        if (mData == null)
            mData = new UserSignupViewModel();
        return mData;
    }

    public void onNextClicked() {
        if (mCurrentRequest != null && !mCurrentRequest.hasEnded())
            return;

        if (!mData.hasDataFilled()) {
            mView.showMessage(R.string.empty_credentials_error);
            return;
        }

        if (!mData.hasPasswordCorrectlyConfirmed()) {
            mView.showMessage(R.string.mismatch_passwords_error);
            return;
        }

        if (!mData.hasValidPassword()) {
            mView.showMessage(R.string.too_short_password_error);
            return;
        }

        mView.showProgressDialog(R.string.registering_username);
        mCurrentRequest = mDataManager.getServicesManager().registerByUsernamePass(mData.username.get(),
                mData.email.get(),mData.password.get());
    }

    @Subscribe
    public void onEvent(Events.ServiceRequestSuccess<AuthResponse> event) {
//        if (event.getType() == ServicesManager.RequestType.REGISTER) {
            mCurrentRequest = null;
            mView.hideProgressDialog();
            mDataManager.setServerToken(event.body.key);
            mFlowController.goTo(mView, FlowController.Screen.REGISTRATION, false);
//        }
    }

    @Subscribe
    public void onEvent(Events.ServiceRequestFailed event) {
        if (event.getType() == ServicesManager.RequestType.REGISTER) {
            mCurrentRequestBackup = mDataManager.getServicesManager().loginByUsernamePass(mData.username.get(), mData.password.get());
            mCurrentRequest = null;
            mView.hideProgressDialog();
            //mView.showMessage(event.apiError.getMessage(), R.string.username_registration_error);
        }
    }

}
