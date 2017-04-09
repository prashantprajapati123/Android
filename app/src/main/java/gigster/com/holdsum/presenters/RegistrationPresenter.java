package gigster.com.holdsum.presenters;

import android.databinding.Observable;
import android.databinding.ObservableField;

import org.greenrobot.eventbus.Subscribe;

import gigster.com.holdsum.R;
import gigster.com.holdsum.activities.RegistrationActivity;
import gigster.com.holdsum.events.Events;
import gigster.com.holdsum.helper.FlowController;
import gigster.com.holdsum.model.UserProfile;
import gigster.com.holdsum.services.RequestHandle;
import gigster.com.holdsum.services.ServicesManager;
import gigster.com.holdsum.viewmodel.RegistrationViewModel;

/**
 * Created by tpaczesny on 2016-09-02.
 */
public class RegistrationPresenter extends HoldsumBasePresenter<RegistrationActivity> {

    private RequestHandle mCurrentRequest;
    private RegistrationViewModel mData;

    @Override
    public void takeView(RegistrationActivity view) {
        super.takeView(view);
        mEventBus.register(this);
    }

    @Override
    public void dropView() {
        mEventBus.unregister(this);
        super.dropView();
    }

    public RegistrationViewModel getData() {
        if (mData == null)
            mData = new RegistrationViewModel();

        mData.employmentStatus.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable observable, int i) {
                statusSelectionChanged(((ObservableField<Integer>)observable).get());
            }
        });

        return mData;
    }

    @Override
    public void onViewCreated() {
        super.onViewCreated();
        getData().clear();
        statusSelectionChanged(getData().employmentStatus.get());
    }

    public void onNextClicked() {
        if (mCurrentRequest != null)
            return;

        if (!mData.hasValidSSN()) {
            mView.showMessage(R.string.ssn_must_match);
            return;
        }

        if (mView.isOnLastScreen()) {
            if (mData.isValid()) {
                //submit information
                mView.showProgressDialog(R.string.registering_profile);
                mCurrentRequest = mDataManager.getServicesManager().updateUserProfile(mData);
            } else {
                mView.showMessage(R.string.please_fill_all_fields);
            }
        } else {
            mView.goToNextScreen();
        }
    }

    public void statusSelectionChanged(int position) {
        boolean employed = position == 0;
        mView.updateViewForEmployedVariant(employed);
    }

    public void onPaystubsTakePhoto() {
        mView.takePaystubsPhoto();
    }

    public void onIdentificationTakePhoto() {
        mView.takeIdentificationPhoto();
    }

    @Subscribe
    public void onEvent(Events.ServiceRequestSuccess<UserProfile> event) {
        if (event.getType() == ServicesManager.RequestType.UPDATE_USER_PROFILE) {
            mCurrentRequest = null;
            mView.hideProgressDialog();
            mDataManager.setUserRegistrationData(mData);
            mDataManager.setHasProfile(true);
            if (!mDataManager.getHasPlaid())
                mFlowController.goTo(mView, FlowController.Screen.BANK_VERIFICATION, true);
            else
                mFlowController.setTo(mView, FlowController.Screen.HOME);
        }
    }

    @Subscribe
    public void onEvent(Events.ServiceRequestFailed event) {
        if (event.getType() == ServicesManager.RequestType.UPDATE_USER_PROFILE) {
            mCurrentRequest = null;
            mView.hideProgressDialog();
            mView.showMessage(event.apiError.getMessage(), R.string.profile_registration_error);
        }
    }

}
