package gigster.com.holdsum.activities;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.widget.ViewFlipper;

import gigster.com.holdsum.R;
import gigster.com.holdsum.databinding.RegistrationBinding;
import gigster.com.holdsum.databinding.RegistrationFirstScreenBinding;
import gigster.com.holdsum.databinding.RegistrationSecondScreenBinding;
import gigster.com.holdsum.databinding.RegistrationThirdScreenBinding;
import gigster.com.holdsum.fragments.TopHeaderFragment;
import gigster.com.holdsum.helper.Logger;
import gigster.com.holdsum.helper.TakePhotoHelper;
import gigster.com.holdsum.presenters.RegistrationPresenter;
import gigster.com.holdsum.presenters.core.PresenterActivity;
import gigster.com.holdsum.presenters.core.UsesPresenter;
import gigster.com.holdsum.viewmodel.RegistrationViewModel;

@UsesPresenter(RegistrationPresenter.class)
public class RegistrationActivity extends HoldsumActivity<RegistrationPresenter> {

    private RegistrationBinding mBinding;
    private RegistrationFirstScreenBinding mFirstScreenBinding;
    private RegistrationSecondScreenBinding mSecondScreenBinding;
    private RegistrationThirdScreenBinding mThirdScreenBinding;

    private RegistrationViewModel mData;
    private int mScreenIdx;

    private TakePhotoHelper mTakePhotoHelper;

    private static final int CAPTURE_PAYSTUB = 0;
    private static final int CAPTURE_ID = 1;

    private static final String SCREEN_IDX_KEY = "screen_idx";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Logger.i("Activity", "RegistrationActivity.onCreate");
        if (savedInstanceState == null) {
            mScreenIdx = 0;
        } else {
            mScreenIdx = savedInstanceState.getInt(SCREEN_IDX_KEY, mScreenIdx);
        }
        mData = mPresenter.getData();

        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_registration);
        mBinding.setPresenter(mPresenter);

        ViewFlipper viewFlipper = mBinding.viewFlipper;

        // set header name
        ((TopHeaderFragment)getFragmentManager().findFragmentById(R.id.top_bar_fragment)).setHeaderText(R.string.registration);

        // inflate all three sub-screens

        mFirstScreenBinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.first_registration_screen, viewFlipper, false);
        mFirstScreenBinding.setData(mData);

        mSecondScreenBinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.second_registration_screen, viewFlipper, false);
        mSecondScreenBinding.setData(mData);
        mSecondScreenBinding.setPresenter(mPresenter);

        mThirdScreenBinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.third_registration_screen, viewFlipper, false);
        mThirdScreenBinding.setData(mData);
        mThirdScreenBinding.setPresenter(mPresenter);


        // setup view flipper
        viewFlipper.setInAnimation(this, android.R.anim.fade_in);
        viewFlipper.setOutAnimation(this, android.R.anim.fade_out);

        viewFlipper.addView(mFirstScreenBinding.getRoot());
        viewFlipper.addView(mSecondScreenBinding.getRoot());
        viewFlipper.addView(mThirdScreenBinding.getRoot());
        viewFlipper.setDisplayedChild(mScreenIdx);

        // Take photo helper
        mTakePhotoHelper = new TakePhotoHelper(this);

        // test mData
        if (getResources().getBoolean(R.bool.debug)) {
            mData.initWithDummyCorrectData();
        }
    }

    public void goToNextScreen() {
        if (mScreenIdx < 2) {
            LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) mBinding.progress.getLayoutParams();
            mScreenIdx++;
            switch (mScreenIdx) {
                case 1:
                    mBinding.percentage.setText(R.string.sixty_six_percent);
                    lp.weight = 2f;
                    mBinding.progress.setLayoutParams(lp);
                    break;
                case 2:
                    mBinding.submit.setText(R.string.submit);
                    mBinding.percentage.setText(R.string.one_hundred_percent);
                    lp.weight = 3f;
                    mBinding.progress.setLayoutParams(lp);
                    break;
            }

            mBinding.viewFlipper.showNext();
        }
    }

    public void goToPreviousScreen() {
        if (mScreenIdx > 0) {
            mBinding.submit.setText(R.string.next);
            LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) mBinding.progress.getLayoutParams();
            switch (mScreenIdx) {
                case 1:
                    mBinding.percentage.setText(R.string.thirty_three_percent);
                    lp.weight = 1f;
                    mBinding.progress.setLayoutParams(lp);
                    break;
                case 2:
                    mBinding.percentage.setText(R.string.sixty_six_percent);
                    lp.weight = 2f;
                    mBinding.progress.setLayoutParams(lp);
                    break;
            }
            mScreenIdx--;
            mBinding.viewFlipper.showPrevious();
        }
    }

    public boolean isOnLastScreen() {
        return mScreenIdx == 2;
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        outState.putInt(SCREEN_IDX_KEY, mScreenIdx);
    }

    @Override
    public void onBackPressed() {
        if (mScreenIdx > 0) {
            goToPreviousScreen();
        } else {
            super.onBackPressed();
        }
    }

    public void updateViewForEmployedVariant(boolean employed) {
        if (employed) {
            mSecondScreenBinding.fundsSource.setVisibility(View.GONE);
            mSecondScreenBinding.monthlyIncome.setVisibility(View.VISIBLE);
            mSecondScreenBinding.sourceOfIncome.setVisibility(View.VISIBLE);
        } else {
            mSecondScreenBinding.fundsSource.setVisibility(View.VISIBLE);
            mSecondScreenBinding.monthlyIncome.setVisibility(View.GONE);
            mSecondScreenBinding.sourceOfIncome.setVisibility(View.GONE);
        }
    }

    /** Take photo functionality */

    public void takePaystubsPhoto() {
        mTakePhotoHelper.takePhoto(CAPTURE_PAYSTUB, "paystub", new TakePhotoHelper.PhotoReadyListener() {
            @Override
            public void photoReady(String filename) {
                mData.paystubPicturePath.set(filename);
                Toast.makeText(RegistrationActivity.this, R.string.paystubs_attached, Toast.LENGTH_SHORT).show();
                mThirdScreenBinding.paystubs.setHint(R.string.person_match);
                mThirdScreenBinding.paystubs.setHintTextColor(ContextCompat.getColor(RegistrationActivity.this, android.R.color.holo_green_dark));
                mThirdScreenBinding.paystubs.setOnClickListener(null);
            }
        });
    }

    public void takeIdentificationPhoto() {
        mTakePhotoHelper.takePhoto(CAPTURE_ID, "id", new TakePhotoHelper.PhotoReadyListener() {
            @Override
            public void photoReady(String filename) {
                mData.idPicturePath.set(filename);
                Toast.makeText(RegistrationActivity.this, R.string.id_attached, Toast.LENGTH_SHORT).show();
                mThirdScreenBinding.identification.setHint(R.string.employer_match);
                mThirdScreenBinding.identification.setHintTextColor(ContextCompat.getColor(RegistrationActivity.this, android.R.color.holo_green_dark));
                mThirdScreenBinding.identification.setOnClickListener(null);
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        mTakePhotoHelper.onRequestPermissionsResult(requestCode,permissions,grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
       mTakePhotoHelper.onActivityResult(requestCode,resultCode,data);
    }

}
