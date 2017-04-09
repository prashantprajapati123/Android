package gigster.com.holdsum.fragments;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import gigster.com.holdsum.R;
import gigster.com.holdsum.databinding.FragmentIntroBinding;
import gigster.com.holdsum.helper.Logger;
import gigster.com.holdsum.presenters.IntroPresenter;
import gigster.com.holdsum.presenters.core.PresenterFragment;
import gigster.com.holdsum.presenters.core.UsesPresenter;

/**
 * Created by Eshaan on 2/23/2016.
 */
@UsesPresenter(IntroPresenter.class)
public class IntroFragment extends HoldsumFragment<IntroPresenter> {

    private FragmentIntroBinding mBinding;

    public static IntroFragment newInstance() {
        return new IntroFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Logger.i("Fragment", "IntroFragment.onCreateView");
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_intro, container, false);
        View v = mBinding.getRoot();
        mBinding.setPresenter(mPresenter);
        mBinding.setIntro(mPresenter.getIntroViewModel());

        return v;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mBinding = null;
    }

}
