package gigster.com.holdsum.services;

import java.util.List;
import gigster.com.holdsum.model.AuthResponse;
import gigster.com.holdsum.model.LoanRequestResponse;
import gigster.com.holdsum.model.SigningUrl;
import gigster.com.holdsum.model.UserLogin;
import gigster.com.holdsum.model.LoanRequest;
import gigster.com.holdsum.model.PlaidToken;
import gigster.com.holdsum.model.FcmToken;
import gigster.com.holdsum.model.UserProfile;
import gigster.com.holdsum.model.UserSignup;
import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;

/**
 * Created by Eshaan on 3/13/2016.
 */
public interface RetrofitServerApi {

    @POST("auth/facebook/")
    Call<AuthResponse> registerByFacebook(@Body UserLogin obj);

    @POST("auth/login/")
    Call<AuthResponse> loginByUsernamePass(@Body UserLogin obj);

    @POST("auth/logout/")
    Call<Object> logout();

    @POST("auth/registration/")
    Call<AuthResponse> registerByUsernamePass(@Body UserSignup obj);

    @POST("user/plaid-token/")
    Call<Object> updatePlaidToken(@Body PlaidToken obj);

    @POST("transaction/loan-request/")
    Call<LoanRequestResponse> loanRequest(@Body LoanRequest obj);

    @GET("transaction/loan-request/")
    Call<List<LoanRequest>> getLoanRequests();

    @POST("transaction/loan-request/{loanId}/signing-url/")
    Call<SigningUrl> getSigningUrl(@Path("loanId") int loanId);
    
    @POST("user/fcm-token/")
    Call<Object> updateFcmToken(@Body FcmToken obj);

    @GET("auth/user/")
    Call<UserProfile> getUserProfile();

    @Multipart
    @PUT("auth/user/")
    Call<UserProfile> updateUserProfile(@Part MultipartBody.Part firstName,
                                   @Part MultipartBody.Part middleInitial,
                                   @Part MultipartBody.Part lastName,
                                   @Part MultipartBody.Part ssn,
                                   @Part MultipartBody.Part sex,
                                   @Part MultipartBody.Part state,
                                   @Part MultipartBody.Part city,
                                   @Part MultipartBody.Part address,
                                   @Part MultipartBody.Part employmentStatus,
                                   @Part MultipartBody.Part zipCode,
                                   @Part MultipartBody.Part payFrequency,
                                   @Part MultipartBody.Part fundsSource,
                                   @Part MultipartBody.Part monthlyIncome,
                                   @Part MultipartBody.Part nextPaydate,
                                   @Part MultipartBody.Part employmentTitle,
                                   @Part MultipartBody.Part employmentEmployerName,
                                   @Part MultipartBody.Part employmentState,
                                   @Part MultipartBody.Part employmentZipCode,
                                   @Part MultipartBody.Part employmentCity,
                                   @Part MultipartBody.Part license,
                                   @Part MultipartBody.Part paystubs);

}
