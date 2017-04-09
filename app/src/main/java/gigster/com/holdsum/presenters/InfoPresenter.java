package gigster.com.holdsum.presenters;

import org.greenrobot.eventbus.Subscribe;

import gigster.com.holdsum.activities.InfoActivity;
import gigster.com.holdsum.events.Events;
import gigster.com.holdsum.helper.FlowController;

/**
 * Created by tpaczesny on 2016-09-02.
 */
public class InfoPresenter extends HoldsumBasePresenter<InfoActivity> {

    @Override
    public void takeView(InfoActivity view) {
        super.takeView(view);
        mEventBus.register(this);
    }

    @Override
    public void dropView() {
        mEventBus.unregister(this);
        super.dropView();
    }

    public void onBackPressed() {
    }

    public void onNextClicked() {
        if (mView.isIntroFragmentShown()) {
            mView.showStepDetailsFragment();
        } else if (mView.isStepDetailsFragmentShown()) {
            mFlowController.goTo(mView, FlowController.Screen.LOGIN, false);
        }
    }

    @Subscribe
    public void onEvent(Events.NextClickedEvent event) {
        onNextClicked();
    }
}
