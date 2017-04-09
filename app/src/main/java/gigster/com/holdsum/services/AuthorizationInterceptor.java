package gigster.com.holdsum.services;

import java.io.IOException;

import gigster.com.holdsum.helper.DataManager;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Adds Authorization header to all requests. Header will be added only if a server token is known by the app.
 * Created by tpaczesny on 2016-09-27.
 */
public class AuthorizationInterceptor implements Interceptor{
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request original = chain.request();

        Request request;
        String token;
        if ((token = DataManager.getInstance().getServerToken()) != null) {

            // Request customization: add request headers
            Request.Builder requestBuilder = original.newBuilder()
                    .header("Authorization", "Token " + token);

            request = requestBuilder.build();
        } else {
            request = original;
        }
        return chain.proceed(request);
    }
}
