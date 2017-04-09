package gigster.com.holdsum.presenters;

import org.greenrobot.eventbus.Subscribe;

import gigster.com.holdsum.R;
import gigster.com.holdsum.activities.BankVerificationActivity;
import gigster.com.holdsum.events.Events;
import gigster.com.holdsum.helper.FlowController;
import gigster.com.holdsum.helper.Logger;
import gigster.com.holdsum.services.RequestHandle;
import gigster.com.holdsum.services.ServicesManager;

/**
 * Created by tpaczesny on 2016-09-02.
 */

public class BankVerificationPresenter extends HoldsumBasePresenter<BankVerificationActivity> {

    private RequestHandle mRequestHandle;

    @Override
    public void takeView(BankVerificationActivity view) {
        super.takeView(view);
        mEventBus.register(this);
    }

    @Override
    public void dropView() {
        mEventBus.unregister(this);
        super.dropView();
    }

    public void onVerificationSuccess(String token) {
        Logger.i("BankVerificationPresenter", "onVerificationSuccessful(token)");
        mRequestHandle = mDataManager.getServicesManager().updatePlaidToken(token);
        if (mView != null) {
            mView.disableVerificationUI();
            mView.showProgressDialog(R.string.registering_token);
        }
    }

    public void onVerificationExited() {
        Logger.i("BankVerificationPresenter", "onVerificationExited(token)");
        if (mRequestHandle != null) {
            mRequestHandle.cancel();
            mRequestHandle = null;
        }
        if (mView != null) {
            mView.showMessage(R.string.plaid_error);
            mFlowController.navigateBack(mView);
        }
    }

    @Subscribe
    public void onEvent(Events.ServiceRequestSuccess<Object> event) {
        if (event.getType() == ServicesManager.RequestType.UPDATE_PLAID_TOKEN) {
            mDataManager.setHasPlaid(true);
            if (mView != null) {
                mView.hideProgressDialog();
                mFlowController.setTo(mView, FlowController.Screen.HOME);
            }
        }
    }

    @Subscribe
    public void onEvent(Events.ServiceRequestFailed event) {
        if (event.getType() == ServicesManager.RequestType.UPDATE_PLAID_TOKEN) {
            if (mView != null) {
                mView.hideProgressDialog();
                mView.showMessage(R.string.token_registration_error);
                mFlowController.navigateBack(mView);
            }
        }
    }


}
