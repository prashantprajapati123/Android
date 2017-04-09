package gigster.com.holdsum.activities;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Toast;

import gigster.com.holdsum.R;
import gigster.com.holdsum.databinding.BankVerificationDataBinding;
import gigster.com.holdsum.fragments.TopHeaderFragment;
import gigster.com.holdsum.presenters.BankVerificationPresenter;
import gigster.com.holdsum.presenters.core.UsesPresenter;

/**
 * Created by tpaczesny on 2016-09-12.
 */
@UsesPresenter(BankVerificationPresenter.class)
public class BankVerificationActivity extends HoldsumActivity<BankVerificationPresenter> {

    private BankVerificationDataBinding mBinding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_bank_verification);
        mBinding.setPresenter(mPresenter);

        ((TopHeaderFragment)getFragmentManager().findFragmentById(R.id.top_bar_fragment)).setHeaderText(R.string.bank_verification);
    }

    public void disableVerificationUI() {
        mBinding.verificationWebview.setVisibility(View.INVISIBLE);
    }

}
