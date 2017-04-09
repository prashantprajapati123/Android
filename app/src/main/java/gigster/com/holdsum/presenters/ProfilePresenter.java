package gigster.com.holdsum.presenters;

import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;

import gigster.com.holdsum.fragments.ProfileFragment;
import gigster.com.holdsum.helper.FlowController;

/**
 * Created by tpaczesny on 2016-09-02.
 */
public class ProfilePresenter extends HoldsumBasePresenter<ProfileFragment> {

    public void onLegal() {
        mFlowController.goTo(mView, FlowController.Screen.LEGAL, true);
    }

    public void onAbout() {
        mFlowController.goTo(mView, FlowController.Screen.ABOUT, true);
    }

    public void onLogout() {
        mDataManager.clearAllData();
        if (FacebookSdk.isInitialized()) {
            // condition needed for unit tests
            LoginManager.getInstance().logOut();
        }
        mDataManager.getServicesManager().logout();
        mFlowController.goTo(mView, FlowController.Screen.ENTRANCE, false);
    }

    public void onInvest()  {
        mFlowController.goTo(mView, FlowController.Screen.INVEST, true);
    }


}
