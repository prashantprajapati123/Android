package gigster.com.holdsum.activities;

import android.app.ProgressDialog;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Toast;

import gigster.com.holdsum.R;
import gigster.com.holdsum.databinding.ConfirmationDataBinding;
import gigster.com.holdsum.fragments.TopHeaderFragment;
import gigster.com.holdsum.helper.FlowController;
import gigster.com.holdsum.presenters.ConfirmationPresenter;
import gigster.com.holdsum.presenters.core.PresenterActivity;
import gigster.com.holdsum.presenters.core.UsesPresenter;

/**
 * Created by Eshaan on 3/5/2016.
 */
@UsesPresenter(ConfirmationPresenter.class)
public class ConfirmationActivity extends HoldsumActivity<ConfirmationPresenter> {

    private ConfirmationDataBinding mBinding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_confirmation);
        mBinding.setPresenter(mPresenter);

        mPresenter.setLoanId(getIntent().getExtras().getInt(FlowController.Arguments.LOAN_ID,-1));

        ((TopHeaderFragment)getFragmentManager().findFragmentById(R.id.top_bar_fragment)).setHeaderText(R.string.confirmation);
    }


    public void loadUrl(String signUrl) {
        mBinding.docusignWebview.loadUrl(signUrl);
    }

    public void hideSignUI() {
        mBinding.docusignWebview.setVisibility(View.INVISIBLE);
        mBinding.allSignedLabel.setVisibility(View.VISIBLE);
    }


}
