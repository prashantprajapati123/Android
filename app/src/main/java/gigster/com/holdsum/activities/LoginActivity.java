package gigster.com.holdsum.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.widget.Toast;

import com.facebook.CallbackManager;

import gigster.com.holdsum.R;
import gigster.com.holdsum.databinding.LoginBinding;
import gigster.com.holdsum.fragments.TopHeaderFragment;
import gigster.com.holdsum.helper.Logger;
import gigster.com.holdsum.presenters.LoginPresenter;
import gigster.com.holdsum.presenters.core.PresenterActivity;
import gigster.com.holdsum.presenters.core.UsesPresenter;

@UsesPresenter(LoginPresenter.class)
public class LoginActivity extends HoldsumActivity<LoginPresenter>  {

    private LoginBinding mBinding;
    private CallbackManager mCallbackManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Logger.i("Activity", "LoginActivity.onCreate");
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_login);
        mBinding.setPresenter(mPresenter);
        mBinding.setData(mPresenter.getData());

        mBinding.facebookLogin.setReadPermissions("user_friends");
        ((TopHeaderFragment)getFragmentManager().findFragmentById(R.id.top_bar_fragment)).setHeaderText(R.string.log_in);

        mCallbackManager = CallbackManager.Factory.create();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
    }

}
