package gigster.com.holdsum.presenters;

import gigster.com.holdsum.events.Events;
import gigster.com.holdsum.fragments.IntroFragment;
import gigster.com.holdsum.viewmodel.IntroViewModel;

/**
 * Created by tpaczesny on 2016-09-02.
 */
public class IntroPresenter extends HoldsumBasePresenter<IntroFragment> {

    public IntroViewModel getIntroViewModel() {
        return mDataManager.getInfoForCurrentMode();
    }

    public void onNextClicked() {
        mEventBus.post(new Events.NextClickedEvent());
    }


}
