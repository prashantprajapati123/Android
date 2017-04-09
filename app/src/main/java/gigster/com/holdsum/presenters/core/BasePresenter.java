package gigster.com.holdsum.presenters.core;

/**
 * Created by tpaczesny on 2016-09-02.
 */
public class BasePresenter<T> {

    protected T mView;

    protected BasePresenter() {
    }

    /**
     * Initialize global references to model/helper classes. Don't call it for unit tests.
     */

    public void setUp() {
    }

    public void takeView(T view) {
        mView = view;
    }

    public void dropView() {
        mView = null;
    }

    public void onViewCreated() {
    }

    public void onViewDestroyed() {
    }

    public final boolean hasView() {
        return mView != null;
    }
}
