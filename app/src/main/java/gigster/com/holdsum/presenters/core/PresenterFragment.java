package gigster.com.holdsum.presenters.core;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;

/**
 * Created by tpaczesny on 2016-09-02.
 */
public abstract class PresenterFragment<T extends BasePresenter> extends Fragment {

    protected T mPresenter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresenter = (T) Presenters.get(this.getClass());
        if (!mPresenter.hasView())
            mPresenter.takeView(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mPresenter.onViewDestroyed();
    }

    @Override
    public void onStop() {
        super.onStop();
        mPresenter.dropView();
    }

    @Override
    public void onStart() {
        super.onStart();
        if (!mPresenter.hasView())
            mPresenter.takeView(this);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (!mPresenter.hasView())
            mPresenter.takeView(this);

        mPresenter.onViewCreated();
    }

}
