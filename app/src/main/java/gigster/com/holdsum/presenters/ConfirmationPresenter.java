package gigster.com.holdsum.presenters;

import org.greenrobot.eventbus.Subscribe;

import gigster.com.holdsum.R;
import gigster.com.holdsum.activities.ConfirmationActivity;
import gigster.com.holdsum.events.Events;
import gigster.com.holdsum.helper.FlowController;
import gigster.com.holdsum.helper.Logger;
import gigster.com.holdsum.model.SigningUrl;
import gigster.com.holdsum.services.ServicesManager;
import gigster.com.holdsum.views.DocuSignWebView;

/**
 * Created by tpaczesny on 2016-09-02.
 */

public class ConfirmationPresenter extends HoldsumBasePresenter<ConfirmationActivity> {

    private DocuSignWebView.SigningStatus mSigningStatus = null;
    private int mLoanId;

    public void setSigningStatus(DocuSignWebView.SigningStatus digningStatus) {
        this.mSigningStatus = digningStatus;
    }

    @Override
    public void takeView(ConfirmationActivity view) {
        super.takeView(view);
        mEventBus.register(this);
    }

    @Override
    public void dropView() {
        mEventBus.unregister(this);
        super.dropView();
    }

    @Override
    public void onViewCreated() {
        super.onViewCreated();

        mView.showProgressDialog(R.string.loading_docs);
        mDataManager.getServicesManager().getSigningUrl(mLoanId);
    }

    @Override
    public void onViewDestroyed() {
        super.onViewDestroyed();

        mSigningStatus = null;
        mLoanId = -1;
    }

    public void setLoanId(int loanId) {
        mLoanId = loanId;
    }

    public void onConfirmLoan() {
        Logger.i("ConfirmationPresenter", "onConfirmLoan()");

        if (mSigningStatus != null && mSigningStatus == DocuSignWebView.SigningStatus.SIGNING_COMPLETE) {
            mFlowController.setTo(mView, FlowController.Screen.HOME);
        } else {
            mView.showMessage(R.string.doc_signing_prompt);
        }
    }

    public void onSigningFinished(DocuSignWebView.SigningStatus status) {
        if (mView == null)
            return;
        mSigningStatus = status;
        if (status == DocuSignWebView.SigningStatus.SIGNING_COMPLETE) {
            mView.hideSignUI();
        } else {
            mView.showMessage(R.string.doc_signing_error);
            mFlowController.navigateBack(mView);
        }
    }


    @Subscribe
    public void onEvent(Events.ServiceRequestSuccess<SigningUrl> event) {
        if (event.getType() == ServicesManager.RequestType.SIGNING_URL) {
            mView.loadUrl(event.body.url);
            mView.hideProgressDialog();
        }
    }

    @Subscribe
    public void onEvent(Events.ServiceRequestFailed event) {
        if (event.getType() == ServicesManager.RequestType.SIGNING_URL) {
            mView.hideProgressDialog();
            mView.showMessage(event.apiError.getMessage(), R.string.doc_get_signing_url_failed);
        }
    }

}
