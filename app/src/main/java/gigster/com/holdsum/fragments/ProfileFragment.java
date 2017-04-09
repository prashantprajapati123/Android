package gigster.com.holdsum.fragments;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import gigster.com.holdsum.R;
import gigster.com.holdsum.databinding.ProfileDataBinding;
import gigster.com.holdsum.helper.Logger;
import gigster.com.holdsum.presenters.ProfilePresenter;
import gigster.com.holdsum.presenters.core.UsesPresenter;

/**
 * Created by Eshaan on 3/20/2016.
 */
@UsesPresenter(ProfilePresenter.class)
public class ProfileFragment extends HoldsumFragment<ProfilePresenter> {

    private ProfileDataBinding mBinding;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Logger.i("Fragment", "ProfileFragment.onCreateView");
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_profile, container, false);
        mBinding.setPresenter(mPresenter);

        return mBinding.getRoot();
    }


}
