package gigster.com.holdsum.presenters;

import android.os.Bundle;

import org.greenrobot.eventbus.Subscribe;

import java.util.List;

import gigster.com.holdsum.R;
import gigster.com.holdsum.events.Events;
import gigster.com.holdsum.fragments.DashboardFragment;
import gigster.com.holdsum.fragments.StatsFragment;
import gigster.com.holdsum.helper.FlowController;
import gigster.com.holdsum.model.LoanRequest;
import gigster.com.holdsum.model.LoanRequestResponse;
import gigster.com.holdsum.presenters.core.BasePresenter;
import gigster.com.holdsum.services.RequestHandle;
import gigster.com.holdsum.services.ServicesManager;

/**
 * Created by tpaczesny on 2016-09-09.
 */
public class StatsPresenter extends HoldsumBasePresenter<StatsFragment> {

    private RequestHandle mCurrentRequest;
    private List<LoanRequest> mData;

    @Override
    public void takeView(StatsFragment view) {
        super.takeView(view);
        mEventBus.register(this);
        this.mData = mDataManager.getLoans();
    }

    @Override
    public void dropView() {
        mEventBus.unregister(this);
        super.dropView();
    }

    public List<LoanRequest> getData() {
        if (mData == null) {
            mData = mDataManager.getLoans();
        }
        return mData;
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

}
