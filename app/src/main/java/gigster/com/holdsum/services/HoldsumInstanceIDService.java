package gigster.com.holdsum.services;

import android.util.Log;

import gigster.com.holdsum.helper.DataManager;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;


public class HoldsumInstanceIDService extends FirebaseInstanceIdService {

    private static final String TAG = "HoldsumInstanceID";
    
    private RequestHandle mRequestHandle;

    protected DataManager mDataManager;

    @Override
    public void onTokenRefresh() {
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG, "Refreshed token: " + refreshedToken);
        sendRegistrationToServer(refreshedToken);
    }
    
    private void sendRegistrationToServer(String token) {
        Log.i(TAG, "sendRegistrationToServer(token)");
        //mRequestHandle = mDataManager.getServicesManager().updateFcmToken(token);
    }
}