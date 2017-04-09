package gigster.com.holdsum.presenters;

import org.junit.Before;
import org.junit.Test;

import gigster.com.holdsum.fragments.ProfileFragment;
import gigster.com.holdsum.helper.FlowController;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;

/**
 * Created by tpaczesny on 2016-09-13.
 */
public class ProfilePresenterTest extends AbstractHoldsumPresenterTest<ProfilePresenter> {

    private ProfileFragment mMockedProfileFragment;

    @Before
    public void setUp() {
        setUpBasicMocks(ProfilePresenter.class, ProfileFragment.class);
        mMockedProfileFragment = (ProfileFragment) mMockedFragment;
    }

    @Test
    public void onLogout_clearServerTokenAndGoToLoginScreen() {
        mPresenter.onLogout();
        verify(mMockedDataManger).clearAllData();
        verify(mMockedServicesManager).logout();
        verify(mMockedFlowController).goTo(mMockedProfileFragment, FlowController.Screen.ENTRANCE, false);
    }

}