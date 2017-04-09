package gigster.com.holdsum.services;

import org.greenrobot.eventbus.EventBus;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

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
import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.Callback;

import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by tpaczesny on 2016-09-15.
 */
public class ServicesManagerTest {

    private ServicesManager mServicesManager;
    private EventBus mMockedEventBus;
    private RetrofitServerApi mMockedRetrofitApi;


    @Before
    public void setUp() {
        mMockedEventBus = mock(EventBus.class);
        mMockedRetrofitApi = mock(RetrofitServerApi.class);

        mServicesManager = new ServicesManager(mMockedRetrofitApi, mMockedEventBus);
    }

    @Test
    public void registerByFacebook_request() {
        Call<AuthResponse> mockedCall = mock(Call.class);
        when(mMockedRetrofitApi.registerByFacebook(any(UserLogin.class))).thenReturn(mockedCall);

        mServicesManager.registerByFacebook("123");

        ArgumentCaptor<UserLogin> argument = ArgumentCaptor.forClass(UserLogin.class);
        verify(mMockedRetrofitApi).registerByFacebook(argument.capture());
        assertEquals("123", argument.getValue().access_token);

        verify(mockedCall).enqueue(any(Callback.class));
    }

    @Test
    public void loginByUsernamePass_request() {
        Call<AuthResponse> mockedCall = mock(Call.class);
        when(mMockedRetrofitApi.loginByUsernamePass(any(UserLogin.class))).thenReturn(mockedCall);

        mServicesManager.loginByUsernamePass("user", "pass123");

        ArgumentCaptor<UserLogin> argument = ArgumentCaptor.forClass(UserLogin.class);
        verify(mMockedRetrofitApi).loginByUsernamePass(argument.capture());
        assertEquals("user", argument.getValue().username);
        assertEquals("pass123", argument.getValue().password);

        verify(mockedCall).enqueue(any(Callback.class));
    }

    @Test
    public void registerByUsernamePass_request() {
        Call<AuthResponse> mockedCall = mock(Call.class);
        when(mMockedRetrofitApi.registerByUsernamePass(any(UserSignup.class))).thenReturn(mockedCall);

        mServicesManager.registerByUsernamePass("user", "email", "pass123");

        ArgumentCaptor<UserSignup> argument = ArgumentCaptor.forClass(UserSignup.class);
        verify(mMockedRetrofitApi).registerByUsernamePass(argument.capture());
        assertEquals("user", argument.getValue().username);
        assertEquals("email", argument.getValue().email);
        assertEquals("pass123", argument.getValue().password1);
        assertEquals("pass123", argument.getValue().password2);

        verify(mockedCall).enqueue(any(Callback.class));
    }

    @Test
    public void logout_request() {
        Call<Object> mockedCall = mock(Call.class);
        when(mMockedRetrofitApi.logout()).thenReturn(mockedCall);

        mServicesManager.logout();

        verify(mMockedRetrofitApi).logout();
        verify(mockedCall).enqueue(any(Callback.class));
    }

    @Test
    public void updatePlaidToken_request() {
        Call<Object> mockedCall = mock(Call.class);
        when(mMockedRetrofitApi.updatePlaidToken(any(PlaidToken.class))).thenReturn(mockedCall);

        mServicesManager.updatePlaidToken("123");

        ArgumentCaptor<PlaidToken> argument = ArgumentCaptor.forClass(PlaidToken.class);
        verify(mMockedRetrofitApi).updatePlaidToken(argument.capture());
        assertEquals("123", argument.getValue().token);

        verify(mockedCall).enqueue(any(Callback.class));
    }

    @Test
    public void loanRequest_request() {
        Call<LoanRequestResponse> mockedCall = mock(Call.class);
        when(mMockedRetrofitApi.loanRequest(any(LoanRequest.class))).thenReturn(mockedCall);

        BorrowViewModel data = new BorrowViewModel();
        data.amount.set("500");
        data.repaymentDate.set("10/01/16");
        data.question1Answer.set(0);
        data.question2Answer.set(0);
        data.question2Textbox.set("Text");
        data.question3Answer.set(0);
        data.question4Answer.set(0);
        data.question5Answer.set(5);
        data.question5AnswerAlt.set(1);
        data.question6Answer.set(0);

        mServicesManager.loanRequest(data);

        ArgumentCaptor<LoanRequest> argument = ArgumentCaptor.forClass(LoanRequest.class);
        verify(mMockedRetrofitApi).loanRequest(argument.capture());

        assertEquals("500", argument.getValue().amount);
        assertEquals(Utils.formatDateForAPI(Utils.parseDate("10/01/16")), argument.getValue().repayment_date);
        assertEquals(1, argument.getValue().responses[0].choice);
        assertEquals(5, argument.getValue().responses[1].choice);
        assertEquals("Text", argument.getValue().responses[1].textbox);
        assertEquals(7, argument.getValue().responses[2].choice);
        assertEquals(11, argument.getValue().responses[3].choice);
        assertEquals(21, argument.getValue().responses[4].choice);
        assertEquals(25, argument.getValue().responses[5].choice);

        verify(mockedCall).enqueue(any(Callback.class));
    }

    @Test
    public void getSigningUrl_request() {
        Call<SigningUrl> mockedCall = mock(Call.class);
        when(mMockedRetrofitApi.getSigningUrl(anyInt())).thenReturn(mockedCall);

        mServicesManager.getSigningUrl(1);

        ArgumentCaptor<Integer> argument = ArgumentCaptor.forClass(Integer.class);
        verify(mMockedRetrofitApi).getSigningUrl(argument.capture());
        assertEquals(1, argument.getValue().intValue());

        verify(mockedCall).enqueue(any(Callback.class));
    }

    @Test
    public void updateUserProfile_request() {
        Call<UserProfile> mockedCall = mock(Call.class);
        when(mMockedRetrofitApi.updateUserProfile(
                any(MultipartBody.Part.class),any(MultipartBody.Part.class),any(MultipartBody.Part.class),
                any(MultipartBody.Part.class),any(MultipartBody.Part.class),any(MultipartBody.Part.class),
                any(MultipartBody.Part.class),any(MultipartBody.Part.class),any(MultipartBody.Part.class),
                any(MultipartBody.Part.class),any(MultipartBody.Part.class),any(MultipartBody.Part.class),
                any(MultipartBody.Part.class),any(MultipartBody.Part.class),any(MultipartBody.Part.class),
                any(MultipartBody.Part.class),any(MultipartBody.Part.class),any(MultipartBody.Part.class),
                any(MultipartBody.Part.class),any(MultipartBody.Part.class),any(MultipartBody.Part.class)
        )).thenReturn(mockedCall);

        RegistrationViewModel rvm = new RegistrationViewModel();
        rvm.initWithDummyCorrectData();
        mServicesManager.updateUserProfile(rvm);

        verify(mMockedRetrofitApi).updateUserProfile(any(MultipartBody.Part.class),any(MultipartBody.Part.class),any(MultipartBody.Part.class),
                any(MultipartBody.Part.class),any(MultipartBody.Part.class),any(MultipartBody.Part.class),
                any(MultipartBody.Part.class),any(MultipartBody.Part.class),any(MultipartBody.Part.class),
                any(MultipartBody.Part.class),any(MultipartBody.Part.class),any(MultipartBody.Part.class),
                any(MultipartBody.Part.class),any(MultipartBody.Part.class),any(MultipartBody.Part.class),
                any(MultipartBody.Part.class),any(MultipartBody.Part.class),any(MultipartBody.Part.class),
                any(MultipartBody.Part.class),any(MultipartBody.Part.class),any(MultipartBody.Part.class));

        verify(mockedCall).enqueue(any(Callback.class));
    }

    @Test
    public void getUserProfile_request() {
        Call<UserProfile> mockedCall = mock(Call.class);
        when(mMockedRetrofitApi.getUserProfile()).thenReturn(mockedCall);

        mServicesManager.getUserProfile();

        verify(mMockedRetrofitApi).getUserProfile();

        verify(mockedCall).enqueue(any(Callback.class));
    }


}