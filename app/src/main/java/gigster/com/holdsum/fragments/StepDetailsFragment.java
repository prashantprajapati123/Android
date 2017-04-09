package gigster.com.holdsum.fragments;

import android.app.Fragment;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.greenrobot.eventbus.EventBus;

import gigster.com.holdsum.R;
import gigster.com.holdsum.databinding.FragmentStepDetailsBinding;
import gigster.com.holdsum.events.Events;
import gigster.com.holdsum.presenters.StepDetailsPresenter;
import gigster.com.holdsum.presenters.core.PresenterFragment;
import gigster.com.holdsum.presenters.core.UsesPresenter;
import gigster.com.holdsum.viewmodel.SummaryStepViewModel;

/**
 * A simple {@link Fragment} subclass.
 * to handle interaction events.
 */
@UsesPresenter(StepDetailsPresenter.class)
public class StepDetailsFragment extends HoldsumFragment<StepDetailsPresenter> {

    private static final String SCREEN_KEY = "screen";

    private FragmentStepDetailsBinding mBinding;
    private SummaryStepViewModel[] mSteps;
    private int mScreenIdx;

    public static StepDetailsFragment newInstance() {
        return new StepDetailsFragment();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState == null) {
            mScreenIdx = 0;
        } else {
            mScreenIdx = savedInstanceState.getInt(SCREEN_KEY);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        updateScreen();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater,R.layout.fragment_step_details, container, false);
        mBinding.setPresenter(mPresenter);
        mSteps = mPresenter.getStepsViewModels();
        return mBinding.getRoot();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mBinding = null;
        mSteps = null;
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(SCREEN_KEY, mScreenIdx);
    }

    public void updateScreen() {
        if (mScreenIdx == 3) {
            EventBus.getDefault().post(new Events.NextClickedEvent());
        } else {
            mBinding.setStep(mSteps[mScreenIdx]);
            mBinding.executePendingBindings();
        }
    }


    public boolean onBackPressed() {
       return mPresenter.onBackPressed();
    }

    /**
     * Advance to next screen/step info.
     * @return true if there was next step to navigate to; false if current step was the last one
     */
    public boolean pushScreen() {
        if (mScreenIdx+1 < mSteps.length) {
            mScreenIdx++;
            updateScreen();
            return true;
        }
        return false;
    }

    /**
     * Go back to previous screen/step info.
     * @return true if there was previous step to navigate to; false if current step was the first one
     */
    public boolean popScreen() {
        if (mScreenIdx != 0) {
            mScreenIdx--;
            updateScreen();
            return true;
        }
        return false;
    }

}
