package gigster.com.holdsum.presenters.core;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by tpaczesny on 2016-09-02.
 */
public abstract class PresenterActivity<T extends BasePresenter> extends AppCompatActivity {

    protected T mPresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresenter = (T) Presenters.get(this.getClass());
        mPresenter.takeView(this);
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mPresenter.onViewCreated();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.onViewDestroyed();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mPresenter.dropView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!mPresenter.hasView())
            mPresenter.takeView(this);
    }
}
