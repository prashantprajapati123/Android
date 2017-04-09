package gigster.com.holdsum.application;

import android.app.Application;

import com.crashlytics.android.Crashlytics;
import com.facebook.FacebookSdk;
import com.facebook.stetho.Stetho;

import org.greenrobot.eventbus.EventBus;

import gigster.com.holdsum.R;
import gigster.com.holdsum.helper.DataManager;
import gigster.com.holdsum.helper.Logger;
import gigster.com.holdsum.services.RetrofitFactory;
import gigster.com.holdsum.services.ServicesManager;
import io.fabric.sdk.android.Fabric;

/**
 * Created by Eshaan on 2/27/2016.
 */
public class HoldSumApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Logger.init(this);
        ServicesManager servicesManager = new ServicesManager(
                RetrofitFactory.buildRetrofitServerApi(this),
                EventBus.getDefault());
        DataManager.getInstance().init(this, EventBus.getDefault(), servicesManager);
        FacebookSdk.sdkInitialize(getApplicationContext());
        if (getResources().getBoolean(R.bool.debug)) {
            Stetho.initializeWithDefaults(this);
        } else {
            Fabric.with(this, new Crashlytics());
        }
    }
}
