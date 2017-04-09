package gigster.com.holdsum.presenters;

import android.app.Activity;
import android.app.Fragment;

import org.greenrobot.eventbus.EventBus;

import gigster.com.holdsum.helper.DataManager;
import gigster.com.holdsum.helper.FlowController;
import gigster.com.holdsum.services.ServicesManager;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by tpaczesny on 2016-09-12.
 */
public abstract class AbstractHoldsumPresenterTest<T extends HoldsumBasePresenter> {

    protected T mPresenter;
    protected DataManager mMockedDataManger;
    protected ServicesManager mMockedServicesManager;
    protected FlowController mMockedFlowController;
    protected Activity mMockedActivity;
    protected Fragment mMockedFragment;
    protected EventBus mMockedEventBus;


    protected void setUpBasicMocks(Class<? extends HoldsumBasePresenter> presenterClass, Class<?> viewClass) {
        try {
            mPresenter = (T) presenterClass.newInstance();
        } catch (InstantiationException e) {
            throw new Error(e);
        } catch (IllegalAccessException e) {
            throw new Error(e);
        }

        // mock data manager and related services manager
        mMockedServicesManager = mock(ServicesManager.class);

        mMockedDataManger = mock(DataManager.class);
        when(mMockedDataManger.getServicesManager()).thenReturn(mMockedServicesManager);

        mPresenter.setDataManger(mMockedDataManger);

        // mock flow controller
        mMockedFlowController = mock(FlowController.class);
        mPresenter.setFlowController(mMockedFlowController);

        // mock event bus
        mMockedEventBus = mock(EventBus.class);
        mPresenter.setEventBus(mMockedEventBus);

        // mock view
        Object mockedView = mock(viewClass);
        if (mockedView instanceof Activity)
            mMockedActivity = (Activity) mockedView;
        else if (mockedView instanceof Fragment)
            mMockedFragment = (Fragment) mockedView;

        mPresenter.takeView(mockedView);
    }


}