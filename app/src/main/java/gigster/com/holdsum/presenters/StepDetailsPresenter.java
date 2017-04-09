package gigster.com.holdsum.presenters;

import gigster.com.holdsum.events.Events;
import gigster.com.holdsum.fragments.StepDetailsFragment;
import gigster.com.holdsum.viewmodel.SummaryStepViewModel;

/**
 * Created by tpaczesny on 2016-09-02.
 */
public class StepDetailsPresenter extends HoldsumBasePresenter<StepDetailsFragment> {

    public SummaryStepViewModel[] getStepsViewModels() {
        return mDataManager.getStepsForCurrentMode();
    }

    public boolean onBackPressed() {
        if (mView != null) {
            return mView.popScreen();
        }
        else {
            return false;
        }
    }

    public void onNextClicked() {
        if (!mView.pushScreen()) {
            mEventBus.post(new Events.NextClickedEvent());
        }
    }



}
