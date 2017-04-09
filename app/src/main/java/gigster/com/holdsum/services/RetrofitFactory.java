package gigster.com.holdsum.services;

import android.content.Context;

import com.facebook.stetho.okhttp3.StethoInterceptor;

import gigster.com.holdsum.R;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by tpaczesny on 2016-09-15.
 */
public class RetrofitFactory {

    public static RetrofitServerApi buildRetrofitServerApi(Context appContext) {

        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .addInterceptor(new AuthorizationInterceptor());

        if (appContext.getResources().getBoolean(R.bool.debug)) {
            builder.addNetworkInterceptor(new StethoInterceptor());
        }

        OkHttpClient client = builder.build();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(appContext.getString(R.string.base_url))
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        return retrofit.create(RetrofitServerApi.class);
    }
}
