package gigster.com.holdsum.presenters;

import org.junit.Before;
import org.junit.Test;

import gigster.com.holdsum.activities.InfoActivity;
import gigster.com.holdsum.events.Events;
import gigster.com.holdsum.helper.FlowController;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by tpaczesny on 2016-09-13.
 */
public class InfoPresenterTest extends AbstractHoldsumPresenterTest<InfoPresenter> {

    private InfoActivity mMockedInfoActivity;

    @Before
    public void setUp() {
        setUpBasicMocks(InfoPresenter.class, InfoActivity.class);
        mMockedInfoActivity = (InfoActivity) mMockedActivity;
    }

    @Test
    public void onNextClicked_isInIntro_goToStepsDescription() {
        when(mMockedInfoActivity.isIntroFragmentShown()).thenReturn(true);
        mPresenter.onNextClicked();
        verify(mMockedInfoActivity).showStepDetailsFragment();
    }

    @Test
    public void onNextClicked_isNotInIntro_goToLoginScreen() {
        when(mMockedInfoActivity.isIntroFragmentShown()).thenReturn(false);
        when(mMockedInfoActivity.isStepDetailsFragmentShown()).thenReturn(true);
        mPresenter.onNextClicked();
        verify(mMockedFlowController).goTo(mMockedInfoActivity, FlowController.Screen.LOGIN, false);
    }

    @Test
    public void onEventNextClicked_isInIntro_goToStepsDescription() {
        when(mMockedInfoActivity.isIntroFragmentShown()).thenReturn(true);
        mPresenter.onEvent(new Events.NextClickedEvent());
        verify(mMockedInfoActivity).showStepDetailsFragment();
    }

    @Test
    public void onEventNextClicked_isNotInIntro_goToLoginScreen() {
        when(mMockedInfoActivity.isIntroFragmentShown()).thenReturn(false);
        when(mMockedInfoActivity.isStepDetailsFragmentShown()).thenReturn(true);
        mPresenter.onEvent(new Events.NextClickedEvent());
        verify(mMockedFlowController).goTo(mMockedInfoActivity, FlowController.Screen.LOGIN, false);
    }


}