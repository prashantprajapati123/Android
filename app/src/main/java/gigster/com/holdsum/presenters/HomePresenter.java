package gigster.com.holdsum.presenters;

import android.os.Handler;

import org.greenrobot.eventbus.Subscribe;


import java.util.ArrayList;

import gigster.com.holdsum.activities.HomeActivity;
import gigster.com.holdsum.events.Events;
import gigster.com.holdsum.helper.DataConverter;
import gigster.com.holdsum.model.LoanRequest;
import gigster.com.holdsum.model.UserProfile;
import gigster.com.holdsum.services.RequestHandle;
import gigster.com.holdsum.services.ServicesManager;

/**
 * Created by tpaczesny on 2016-10-24.
 */

public class HomePresenter extends HoldsumBasePresenter<HomeActivity> {

    private RequestHandle mRequestHandle;
    private Handler mHandler;

    @Override
    public void setUp() {
        super.setUp();
        mHandler = new Handler();
    }

    @Override
    public void takeView(HomeActivity view) {
        super.takeView(view);
        mEventBus.register(this);

        // request profile data; post it to looper to make sure any interested fragments are properly resumed
        mHandler.post(new RequestUserDataRunnable());
        mHandler.post(new RequestLoansDataRunnable());
    }

    @Override
    public void dropView() {
        mEventBus.unregister(this);
        super.dropView();
    }

    @Subscribe
    public void onEvent(Events.ServiceRequestSuccess event) {
        if (event.getType() == ServicesManager.RequestType.GET_USER_PROFILE) {
            UserProfile userProfile = (UserProfile)event.body;
            mDataManager.setUserRegistrationData(DataConverter.UserProfileToRegistrationViewModel(userProfile));
            mDataManager.setUserStatus(DataConverter.UserProfileStatusToUserStatus(userProfile.status));
            mRequestHandle = null;
        }
        if (event.getType() == ServicesManager.RequestType.GET_LOANS) {
            mDataManager.setLoans((ArrayList<LoanRequest>)event.body);
            mRequestHandle = null;
        }
    }

    @Subscribe
    public void onEvent(Events.ServiceRequestFailed event) {
        if (event.getType() == ServicesManager.RequestType.GET_USER_PROFILE) {
            mRequestHandle = null;
        }
    }

    private class RequestLoansDataRunnable implements Runnable {

        @Override
        public void run() {
            // get data that needs to be fresh when user gets back to the app
            if (mRequestHandle == null) {
                mRequestHandle = mDataManager.getServicesManager().getLoans();
            }
        }
    }

    private class RequestUserDataRunnable implements Runnable {

        @Override
        public void run() {
            // get data that needs to be fresh when user gets back to the app
            if (mRequestHandle == null) {
                mRequestHandle = mDataManager.getServicesManager().getUserProfile();
            }
        }
    }

}
