package gigster.com.holdsum.fragments;

import android.app.Fragment;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.w3c.dom.Text;

import gigster.com.holdsum.R;
import gigster.com.holdsum.activities.EntranceActivity;
import gigster.com.holdsum.databinding.ProfileDataBinding;
import gigster.com.holdsum.databinding.TopHeaderDataBinding;
import gigster.com.holdsum.helper.Logger;
import gigster.com.holdsum.presenters.ProfilePresenter;
import gigster.com.holdsum.presenters.core.PresenterFragment;
import gigster.com.holdsum.presenters.core.UsesPresenter;

/**
 * Created by tpaczesny on 2016-09-09.
 */

public class TopHeaderFragment extends Fragment {


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Logger.i("Fragment", "TopHeaderFragment.onCreateView");
        TopHeaderDataBinding binding = DataBindingUtil.inflate(inflater, R.layout.top_header, container, false);
        binding.setListener(this);

        return binding.getRoot();
    }

    public void onBackClicked() {
        getActivity().onBackPressed();
    }

    public void setHeaderText(int resId) {
        ((TextView)getView().findViewById(R.id.header_text)).setText(resId);
    }
}
