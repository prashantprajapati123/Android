package gigster.com.holdsum.presenters;

import org.junit.Before;
import org.junit.Test;

import gigster.com.holdsum.events.Events;
import gigster.com.holdsum.fragments.IntroFragment;

import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

/**
 * Created by tpaczesny on 2016-09-13.
 */
public class IntroPresenterTest extends AbstractHoldsumPresenterTest<IntroPresenter> {

    private IntroFragment mMockedIntroFragment;

    @Before
    public void setUp() {
        setUpBasicMocks(IntroPresenter.class, IntroFragment.class);
        mMockedIntroFragment = (IntroFragment) mMockedFragment;
    }

    @Test
    public void getIntroViewModel() {
        mPresenter.getIntroViewModel();
        verify(mMockedDataManger).getInfoForCurrentMode();
    }

    @Test
    public void onNextClicked() {
        mPresenter.onNextClicked();
        verify(mMockedEventBus).post(any(Events.NextClickedEvent.class));
    }

}