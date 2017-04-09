package gigster.com.holdsum.activities;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;

import gigster.com.holdsum.R;
import gigster.com.holdsum.databinding.EntranceBinding;
import gigster.com.holdsum.helper.Logger;
import gigster.com.holdsum.presenters.EntrancePresenter;
import gigster.com.holdsum.presenters.core.PresenterActivity;
import gigster.com.holdsum.presenters.core.UsesPresenter;

@UsesPresenter(EntrancePresenter.class)
public class EntranceActivity extends HoldsumActivity<EntrancePresenter> {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Logger.i("Activity", "EntranceActivity.onCreate");

        EntranceBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_entrance);
        binding.setPresenter(mPresenter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mPresenter.checkIfUserLoggedIn();
    }
}
