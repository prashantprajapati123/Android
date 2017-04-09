package gigster.com.holdsum.activities;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.widget.Toast;

import gigster.com.holdsum.R;
import gigster.com.holdsum.databinding.UserSignupBinding;
import gigster.com.holdsum.fragments.TopHeaderFragment;
import gigster.com.holdsum.helper.Logger;
import gigster.com.holdsum.presenters.UserSignupPresenter;
import gigster.com.holdsum.presenters.core.PresenterActivity;
import gigster.com.holdsum.presenters.core.UsesPresenter;

@UsesPresenter(UserSignupPresenter.class)
public class UserSignupActivity extends HoldsumActivity<UserSignupPresenter> {

    private UserSignupBinding mBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Logger.i("Activity", "UserSignupActivity.onCreate");

        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_user_signup);
        mBinding.setPresenter(mPresenter);
        mBinding.setData(mPresenter.getData());

        ((TopHeaderFragment)getFragmentManager().findFragmentById(R.id.top_bar_fragment)).setHeaderText(R.string.user_signup);
    }

}
