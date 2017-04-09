package gigster.com.holdsum.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.webkit.WebView;

import gigster.com.holdsum.R;
import gigster.com.holdsum.fragments.TopHeaderFragment;
import gigster.com.holdsum.presenters.core.NoPresenter;

/**
 * Created by tpaczesny on 2016-10-21.
 */
@NoPresenter
public class AboutActivity extends HoldsumActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        ((WebView)findViewById(R.id.about_webview)).loadUrl(getString(R.string.about_url));

        ((TopHeaderFragment)getFragmentManager().findFragmentById(R.id.top_bar_fragment)).setHeaderText(R.string.about);
    }

}
