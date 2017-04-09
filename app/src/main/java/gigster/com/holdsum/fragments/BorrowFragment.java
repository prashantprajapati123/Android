package gigster.com.holdsum.fragments;


import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import gigster.com.holdsum.R;
import gigster.com.holdsum.databinding.BorrowDataBinding;
import gigster.com.holdsum.helper.Logger;
import gigster.com.holdsum.helper.enums.UserStatus;
import gigster.com.holdsum.presenters.BorrowPresenter;
import gigster.com.holdsum.presenters.core.PresenterFragment;
import gigster.com.holdsum.presenters.core.UsesPresenter;
import gigster.com.holdsum.viewmodel.BorrowViewModel;


/**
 * A simple {@link Fragment} subclass.
 */
@UsesPresenter(BorrowPresenter.class)
public class BorrowFragment extends HoldsumFragment<BorrowPresenter> {

    private BorrowViewModel mData;
    private BorrowDataBinding mBinding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Logger.i("Fragment", "BorrowFragment.onCreateView");

        mData = mPresenter.getData();

        // Inflate the layout for this fragment
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_borrow, container, false);
        mBinding.setPresenter(mPresenter);
        mBinding.setData(mData);

        mBinding.repaymentDatePicker.setValidationRule(mPresenter.getRepaymentDateValidationRule());

        return mBinding.getRoot();
    }

    public void showQuestion2TextBox() {
        mBinding.question2Textbox.setVisibility(View.VISIBLE);
    }

    public void hideQuestion2TextBox() {
        mData.question2Textbox.set("");
        mBinding.question2Textbox.setVisibility(View.GONE);
    }

    public void showQuestion5Alt() {
        mBinding.question5AltLabel.setVisibility(View.VISIBLE);
        mBinding.question5Alt.setVisibility(View.VISIBLE);
    }

    public void hideQuestion5Alt() {
        mData.question5AnswerAlt.set(-1);
        mBinding.question5AltLabel.setVisibility(View.GONE);
        mBinding.question5Alt.setVisibility(View.GONE);
    }

    public void showQuestion7aTextBox() {
        mBinding.question7aTextbox.setVisibility(View.VISIBLE);
    }

    public void hideQuestion7aTextBox() {
        mData.question7aTextbox.set("");
        mBinding.question7aTextbox.setVisibility(View.GONE);
    }

    public void showQuestion7b() {
        mBinding.question7b.setVisibility(View.VISIBLE);
    }

    public void hideQuestion7b() {
        mData.question7bAnswer.set(-1);
        mData.question7bTextbox.set("");
        mBinding.question7b.setVisibility(View.GONE);
        hideQuestion7bTextBox();
    }

    public void showQuestion7bTextBox() {
        mBinding.question7bTextbox.setVisibility(View.VISIBLE);
    }

    public void hideQuestion7bTextBox() {
        mData.question7bTextbox.set("");
        mBinding.question7bTextbox.setVisibility(View.GONE);
    }

    public void offerPaydateChange() {
        AlertDialog dialog = new AlertDialog.Builder(getActivity())
                            .setMessage(R.string.loan_offer_paydate_change)
                            .setPositiveButton(R.string.loan_offer_paydate_change_confirm, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    mPresenter.changeRepaymentDate();
                                }
                            })
                            .setNegativeButton(R.string.loan_offer_paydate_change_cancel, null)
                            .create();
        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                mPresenter.offerRepaymentDateChangeEnded();
            }
        });
        dialog.show();

    }

    public void updateForUserStatus(UserStatus userStatus) {
        if (userStatus == UserStatus.APPROVED) {
            mBinding.borrowRootLayout.setVisibility(View.VISIBLE);
            mBinding.notApprovedLayout.setVisibility(View.GONE);
        } else {
            mBinding.borrowRootLayout.setVisibility(View.GONE);
            mBinding.notApprovedLayout.setVisibility(View.VISIBLE);
        }
    }

}
