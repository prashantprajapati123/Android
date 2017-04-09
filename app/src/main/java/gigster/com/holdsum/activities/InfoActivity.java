package gigster.com.holdsum.activities;

import android.app.Fragment;
import android.os.Bundle;

import gigster.com.holdsum.R;
import gigster.com.holdsum.fragments.IntroFragment;
import gigster.com.holdsum.fragments.StepDetailsFragment;
import gigster.com.holdsum.helper.Logger;
import gigster.com.holdsum.presenters.InfoPresenter;
import gigster.com.holdsum.presenters.core.PresenterActivity;
import gigster.com.holdsum.presenters.core.UsesPresenter;

/**
 * Created by Eshaan on 2/23/2016.
 */

@UsesPresenter(InfoPresenter.class)
public class InfoActivity extends HoldsumActivity<InfoPresenter> {

    Fragment introFragment;
    Fragment stepDetailsFragment;
    Fragment currentFragment;
    private String currentFragmentTag;
    private static final String INTRO_TAG = "intro";
    private static final String SUMMARY_TAG = "first_summary_tag";
    private static final String CURR_FRAG = "curr_frag";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Logger.i("Activity", "InfoActivity.onCreate");
        setContentView(R.layout.activity_info);
        if (savedInstanceState == null) {
            showIntroFragment();
        } else {
            currentFragmentTag = savedInstanceState.getString(CURR_FRAG);
            introFragment = getFragmentManager().getFragment(savedInstanceState, INTRO_TAG);
            stepDetailsFragment = getFragmentManager().getFragment(savedInstanceState, SUMMARY_TAG);
        }

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString(CURR_FRAG, currentFragmentTag);
        if (introFragment != null) {
            getFragmentManager().putFragment(outState, INTRO_TAG, introFragment);
        }
        if (stepDetailsFragment != null) {
            getFragmentManager().putFragment(outState, SUMMARY_TAG, stepDetailsFragment);
        }
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onBackPressed() {
        if (currentFragment == stepDetailsFragment && ((StepDetailsFragment) currentFragment).onBackPressed()) {
            // back handled by current fragment
            return;
        }

        super.onBackPressed();
        currentFragmentTag = INTRO_TAG;
    }


    public boolean isIntroFragmentShown() {
        return INTRO_TAG.equals(currentFragmentTag);
    }

    public boolean isStepDetailsFragmentShown() {
        return SUMMARY_TAG.equals(currentFragmentTag);
    }

    public void showStepDetailsFragment() {
        currentFragmentTag = SUMMARY_TAG;
        stepDetailsFragment = StepDetailsFragment.newInstance();
        currentFragment = stepDetailsFragment;
        getFragmentManager().beginTransaction().replace(R.id.root, stepDetailsFragment, SUMMARY_TAG).setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out).addToBackStack(null).commit();
    }

    public void showIntroFragment() {
        introFragment = IntroFragment.newInstance();
        currentFragment = introFragment;
        currentFragmentTag = INTRO_TAG;
        getFragmentManager().beginTransaction().replace(R.id.root, introFragment, INTRO_TAG).setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out).commit();
    }

}
