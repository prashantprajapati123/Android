package gigster.com.holdsum.presenters;

import org.greenrobot.eventbus.EventBus;

import gigster.com.holdsum.helper.DataManager;
import gigster.com.holdsum.helper.FlowController;
import gigster.com.holdsum.presenters.core.BasePresenter;

/**
 * Created by tpaczesny on 2016-09-02.
 */
public class HoldsumBasePresenter<T> extends BasePresenter<T> {

    protected DataManager mDataManager;
    protected FlowController mFlowController;
    protected EventBus mEventBus;

    @Override
    public void setUp() {
        super.setUp();
        mDataManager = DataManager.getInstance();
        mFlowController = FlowController.getInstance();
        mEventBus = EventBus.getDefault();
    }

    public void setDataManger(DataManager dataManger) {
        this.mDataManager = dataManger;
    }

    public void setFlowController(FlowController flowController) {
        this.mFlowController = flowController;
    }

    public void setEventBus(EventBus eventBus) {
        this.mEventBus = eventBus;
    }
}
