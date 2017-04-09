package gigster.com.holdsum.presenters;

import com.facebook.AccessToken;
import com.facebook.login.LoginManager;

import gigster.com.holdsum.activities.EntranceActivity;
import gigster.com.holdsum.helper.FlowController;
import gigster.com.holdsum.helper.enums.Mode;

/**
 * Created by tpaczesny on 2016-09-02.
 */
public class EntrancePresenter extends HoldsumBasePresenter<EntranceActivity> {


    @Override
    public void takeView(EntranceActivity view) {
        super.takeView(view);
    }

    public void checkIfUserLoggedIn() {
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        String serverToken = mDataManager.getServerToken();
        if (serverToken != null && mDataManager.getUserRegistrationData() != null &&
                mDataManager.getHasProfile() && mDataManager.getHasPlaid() && false) {
            mDataManager.setMode(Mode.BORROW);

            mFlowController.setTo(mView, FlowController.Screen.HOME);
        } else {
            mDataManager.clearAllData();
            if (accessToken != null)
                LoginManager.getInstance().logOut();
        }
    }

    public void onClickInvest() {
        mDataManager.setMode(Mode.INVEST);
        mFlowController.goTo(mView, FlowController.Screen.INFO, true);
    }

    public void onClickBorrow() {
        mDataManager.setMode(Mode.BORROW);
        mFlowController.goTo(mView, FlowController.Screen.INFO, true);
    }

    public void onClickLogin() {

        mFlowController.setTo(mView, FlowController.Screen.LOGIN);

        // FIXME: temp code
        /*AsyncTask asyncTask = new AsyncTask() {
            @Override
            protected Object doInBackground(Object[] objects) {
                DocuSign docuSign = new DocuSign();
                docuSign.login();
                return null;
            }
        };
        asyncTask.execute();*/
    }

}
