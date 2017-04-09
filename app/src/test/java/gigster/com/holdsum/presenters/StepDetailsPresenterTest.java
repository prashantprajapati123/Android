package gigster.com.holdsum.presenters;

import org.junit.Before;
import org.junit.Test;

import gigster.com.holdsum.events.Events;
import gigster.com.holdsum.fragments.StepDetailsFragment;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by tpaczesny on 2016-09-13.
 */
public class StepDetailsPresenterTest extends AbstractHoldsumPresenterTest<StepDetailsPresenter> {

    private StepDetailsFragment mMockedStepDetailsFragment;

    @Before
    public void setUp() {
        setUpBasicMocks(StepDetailsPresenter.class, StepDetailsFragment.class);
        mMockedStepDetailsFragment = (StepDetailsFragment) mMockedFragment;
    }

    @Test
    public void getStepsViewModels() {
        mPresenter.getStepsViewModels();
        verify(mMockedDataManger).getStepsForCurrentMode();
    }

    @Test
    public void onBackPressed_popSubScreen() {
        mPresenter.onBackPressed();
        verify(mMockedStepDetailsFragment).popScreen();
    }

    @Test
    public void onNextClicked_pushSubScreen() {
        mPresenter.onNextClicked();
        verify(mMockedStepDetailsFragment).pushScreen();
    }

    @Test
    public void onNextClicked_LastSubScreen_postNextEvent() {
        when(mMockedStepDetailsFragment.pushScreen()).thenReturn(false);
        mPresenter.onNextClicked();
        verify(mMockedEventBus).post(any(Events.NextClickedEvent.class));
    }

}