package gigster.com.holdsum.services;

import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Map;

import gigster.com.holdsum.events.Events;
import gigster.com.holdsum.helper.DataConverter;
import gigster.com.holdsum.helper.Logger;
import gigster.com.holdsum.helper.Utils;
import gigster.com.holdsum.model.AuthResponse;
import gigster.com.holdsum.model.LoanRequestResponse;
import gigster.com.holdsum.model.SigningUrl;
import gigster.com.holdsum.model.UserLogin;
import gigster.com.holdsum.model.LoanRequest;
import gigster.com.holdsum.model.PlaidToken;
import gigster.com.holdsum.model.UserProfile;
import gigster.com.holdsum.model.UserSignup;
import gigster.com.holdsum.viewmodel.BorrowViewModel;
import gigster.com.holdsum.viewmodel.RegistrationViewModel;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by tpaczesny on 2016-09-13.
 */
public class ServicesManager {

    private RetrofitServerApi mServerApi;
    private EventBus mEventBus;
    private Gson mGson;

    public enum RequestType {
        REGISTER_BY_FB, LOGIN, REGISTER, UPDATE_PLAID_TOKEN, UPDATE_USER_PROFILE, LOAN_REQUEST, SIGNING_URL, GET_USER_PROFILE, GET_LOANS, LOGOUT;
    }

    public ServicesManager(RetrofitServerApi serverApi, EventBus eventBus) {
        mServerApi = serverApi;
        mEventBus = eventBus;
        mGson = new Gson();
    }

    public RequestHandle registerByFacebook(String accessToken) {
        UserLogin obj = new UserLogin();
        obj.access_token = accessToken;
        Call<AuthResponse> call = mServerApi.registerByFacebook(obj);
        return basicRequestHandle(call,RequestType.REGISTER_BY_FB);
    }

    public RequestHandle loginByUsernamePass(String username, String password) {
        UserLogin obj = new UserLogin();
        obj.username = username;
        obj.password = password;
        Call<AuthResponse> call = mServerApi.loginByUsernamePass(obj);
        return basicRequestHandle(call, RequestType.LOGIN);
    }

    /**
     * Performs silent logout. Result won't be notified.
     * @return
     */

    public RequestHandle logout() {
        Call<Object> call = mServerApi.logout();
        call.enqueue(new IgnoreCallback<>());
        return new RetrofitRequestHandle(call,RequestType.LOGOUT);
    }

    public RequestHandle registerByUsernamePass(String username, String email, String password) {
        UserSignup obj = new UserSignup();
        obj.username = username;
        obj.email = email;
        obj.password1 = password;
        obj.password2 = password;

        Call<AuthResponse> call = mServerApi.registerByUsernamePass(obj);
        return basicRequestHandle(call, RequestType.REGISTER);
    }

    public RequestHandle updatePlaidToken(String token) {
        PlaidToken obj = new PlaidToken();
        obj.token = token;
        Call<Object> call = mServerApi.updatePlaidToken(obj);
        return basicRequestHandle(call, RequestType.UPDATE_PLAID_TOKEN);
    }

    public RequestHandle loanRequest(BorrowViewModel bvm) {
        LoanRequest obj = DataConverter.BorrowViewModelToLoanRequest(bvm);

        Call<LoanRequestResponse> call = mServerApi.loanRequest(obj);
        return basicRequestHandle(call, RequestType.LOAN_REQUEST);
    }

    public RequestHandle getSigningUrl(int loanId) {
        Call<SigningUrl> call = mServerApi.getSigningUrl(loanId);
        return basicRequestHandle(call, RequestType.SIGNING_URL);
    }

    public RequestHandle updateUserProfile(RegistrationViewModel rvm) {
        MultipartBody.Part[] mp = DataConverter.RegistrationViewModelToMultiParts(rvm);

        Call<UserProfile> call = mServerApi.updateUserProfile(
                mp[0],mp[1],mp[2],mp[3],mp[4],mp[5],mp[6],mp[7],mp[8],mp[9],mp[10],mp[11],
                mp[12],mp[13],mp[14],mp[15],mp[16],mp[17],mp[18],mp[19],mp[20]
                );
        return basicRequestHandle(call, RequestType.UPDATE_USER_PROFILE);
    }

    public RequestHandle getLoans() {
        Call<List<LoanRequest>> call = mServerApi.getLoanRequests();
        return basicRequestHandle(call, RequestType.GET_LOANS);
    }

    public RequestHandle getUserProfile() {
        Call<UserProfile> call = mServerApi.getUserProfile();
        return basicRequestHandle(call, RequestType.GET_USER_PROFILE);
    }

    // helper methods
    private RequestHandle basicRequestHandle(Call call, RequestType type) {
        RetrofitRequestHandle requestHandle = new RetrofitRequestHandle(call,type);
        call.enqueue(new BasicCallback<>(requestHandle));
        return requestHandle;
    }

    // errors body parsing

    public APIError parseAPIError(Response response) {
        try {
            response.errorBody();

            Map errors = mGson.fromJson(response.errorBody().charStream(), Map.class);
            APIError apiError = new APIError(errors);
            return apiError;
        } catch (Exception e) {
            try {
                Logger.d("ServicesManager", "Request error ("+response.code()+"):\n" + response.errorBody().string());
            } catch (Exception e1) {
                Logger.d("ServicesManager", "Request error ("+response.code()+"):\n" + response.raw().message());
            }
            e.printStackTrace();
            return new APIError(null);
        }
    }

    // universal callback, add new if needed

     public class BasicCallback<T> implements Callback<T> {

        private final RetrofitRequestHandle mRequestHandle;

        public BasicCallback(RetrofitRequestHandle requestHandle) {
            mRequestHandle = requestHandle;
        }

        @Override
        public void onResponse(Call<T> call, Response<T> response) {
            mRequestHandle.setHasEnded(true);
            if (response.code() >= 200 && response.code() < 300) {
                Events.ServiceRequestSuccess<T> successEvent;
                if (response.body() != null)
                    successEvent = new Events.ServiceRequestSuccess<>(mRequestHandle, response.body());
                else
                    successEvent = new Events.ServiceRequestSuccess<>(mRequestHandle, null);

                mEventBus.post(successEvent);
            } else {
                APIError apiError = parseAPIError(response);
                mEventBus.post(new Events.ServiceRequestFailed(mRequestHandle, apiError));
            }
        }

        @Override
        public void onFailure(Call<T> call, Throwable t) {
            mRequestHandle.setHasEnded(true);
            mEventBus.post(new Events.ServiceRequestFailed(mRequestHandle));
        }
    }

    // callback that ignores the results
    public class IgnoreCallback<T> implements Callback<T> {

        @Override
        public void onResponse(Call<T> call, Response<T> response) {

        }

        @Override
        public void onFailure(Call<T> call, Throwable t) {

        }
    }
}
