package gigster.com.holdsum.presenters;

import org.junit.Before;
import org.junit.Test;

import gigster.com.holdsum.R;
import gigster.com.holdsum.activities.RegistrationActivity;
import gigster.com.holdsum.events.Events;
import gigster.com.holdsum.helper.FlowController;
import gigster.com.holdsum.model.UserProfile;
import gigster.com.holdsum.services.APIError;
import gigster.com.holdsum.services.RetrofitRequestHandle;
import gigster.com.holdsum.services.ServicesManager;
import gigster.com.holdsum.viewmodel.RegistrationViewModel;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by tpaczesny on 2016-09-13.
 */
public class RegistrationPresenterTest extends AbstractHoldsumPresenterTest<RegistrationPresenter> {

    private RegistrationActivity mMockedRegistrationActivity;

    @Before
    public void setUp() {
        setUpBasicMocks(RegistrationPresenter.class, RegistrationActivity.class);
        mMockedRegistrationActivity = (RegistrationActivity) mMockedActivity;
    }

    @Test
    public void onNextClicked_ssnInvalid_showSSNError() {
        RegistrationViewModel data = mPresenter.getData();
        data.initWithDummyCorrectData();
        data.ssn.set("123");
        data.ssnConfirm.set("321");
        mPresenter.onNextClicked();
        verify(mMockedRegistrationActivity).showMessage(R.string.ssn_must_match);
    }

    @Test
    public void onNextClicked_lastScreenAndDataValid_submitRequest() {
        RegistrationViewModel data = mPresenter.getData();
        data.initWithDummyCorrectData();

        when(mMockedRegistrationActivity.isOnLastScreen()).thenReturn(true);
        mPresenter.onNextClicked();
        verify(mMockedRegistrationActivity).showProgressDialog(R.string.registering_profile);
        verify(mMockedServicesManager).updateUserProfile(mPresenter.getData());
    }

    @Test
    public void onNextClicked_lastScreenAndDataInvalid_showError() {
        RegistrationViewModel data = mPresenter.getData();
        data.initWithDummyCorrectData();
        data.firstName.set(null);

        when(mMockedRegistrationActivity.isOnLastScreen()).thenReturn(true);
        mPresenter.onNextClicked();
        verify(mMockedRegistrationActivity).showMessage(R.string.please_fill_all_fields);
    }

    @Test
    public void onNextClicked_notLastScreen_goToNextScreen() {
        RegistrationViewModel data = mPresenter.getData();
        data.initWithDummyCorrectData();

        when(mMockedRegistrationActivity.isOnLastScreen()).thenReturn(false);
        mPresenter.onNextClicked();
        verify(mMockedRegistrationActivity).goToNextScreen();
    }

    @Test
    public void statusSelectionChanged_position0_updateFormForEmployed() {
        mPresenter.statusSelectionChanged(0);
        verify(mMockedRegistrationActivity).updateViewForEmployedVariant(true);
    }

    @Test
    public void statusSelectionChanged_position1_updateFormForUnemployed() {
        mPresenter.statusSelectionChanged(1);
        verify(mMockedRegistrationActivity).updateViewForEmployedVariant(false);
    }

    @Test
    public void onPaystubsTakePhoto() {
        mPresenter.onPaystubsTakePhoto();
        verify(mMockedRegistrationActivity).takePaystubsPhoto();
    }

    @Test
    public void onIdentificationTakePhoto() {
        mPresenter.onIdentificationTakePhoto();
        verify(mMockedRegistrationActivity).takeIdentificationPhoto();
    }

    @Test
    public void onRequestSuccess_noPlaidToken_navigateToBankVerification() {
        when(mMockedDataManger.getHasPlaid()).thenReturn(false);

        mPresenter.getData();
        mPresenter.onEvent(new Events.ServiceRequestSuccess<UserProfile>(
                new RetrofitRequestHandle(null, ServicesManager.RequestType.UPDATE_USER_PROFILE), null));

        verify(mMockedRegistrationActivity).hideProgressDialog();
        verify(mMockedFlowController).goTo(mMockedRegistrationActivity, FlowController.Screen.BANK_VERIFICATION, true);
    }

    @Test
    public void onRequestSuccess_hasPlaidToken_navigateToHome() {
        when(mMockedDataManger.getHasPlaid()).thenReturn(true);

        mPresenter.getData();
        mPresenter.onEvent(new Events.ServiceRequestSuccess<UserProfile>(
                new RetrofitRequestHandle(null, ServicesManager.RequestType.UPDATE_USER_PROFILE), null));

        verify(mMockedRegistrationActivity).hideProgressDialog();
        verify(mMockedFlowController).setTo(mMockedRegistrationActivity, FlowController.Screen.HOME);
    }

    @Test
    public void onSignupFailed_showError() {
        APIError mockApiError = mock(APIError.class);
        when(mockApiError.getMessage()).thenReturn("message");

        Events.ServiceRequestFailed failEvent = new Events.ServiceRequestFailed(
                new RetrofitRequestHandle(null, ServicesManager.RequestType.UPDATE_USER_PROFILE), mockApiError);
        mPresenter.onEvent(failEvent);
        verify(mMockedRegistrationActivity).hideProgressDialog();
        verify(mMockedRegistrationActivity).showMessage("message", R.string.profile_registration_error);
    }



}