package gigster.com.holdsum.presenters;

import org.junit.Before;
import org.junit.Test;

import gigster.com.holdsum.activities.EntranceActivity;
import gigster.com.holdsum.helper.FlowController;
import gigster.com.holdsum.helper.enums.Mode;

import static org.mockito.Mockito.*;

/**
 * Created by tpaczesny on 2016-09-12.
 */
public class EntrancePresenterTest extends AbstractHoldsumPresenterTest<EntrancePresenter> {


    @Before
    public void setUp() {
        setUpBasicMocks(EntrancePresenter.class, EntranceActivity.class);
    }

    @Test
    public void onClickInvest_setModeInvestAndNavigateToInfo() {
        mPresenter.onClickInvest();
        verify(mMockedDataManger).setMode(Mode.INVEST);
        verify(mMockedFlowController).goTo(mMockedActivity, FlowController.Screen.INFO, true);
    }

    @Test
    public void onClickBorrow_setModeBorrowAndNavigateToInfo() {
        mPresenter.onClickBorrow();
        verify(mMockedDataManger).setMode(Mode.BORROW);
        verify(mMockedFlowController).goTo(mMockedActivity, FlowController.Screen.INFO, true);
    }

    @Test
    public void onClickLogin_navigateToLogin() {
        mPresenter.onClickLogin();
        verify(mMockedFlowController).setTo(mMockedActivity, FlowController.Screen.LOGIN);
    }


}