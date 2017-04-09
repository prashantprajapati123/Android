package gigster.com.holdsum.activities;

import android.databinding.DataBindingUtil;
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
public class LegalActivity extends HoldsumActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DataBindingUtil.setContentView(this, R.layout.activity_legal);

        WebView webView = (WebView) findViewById(R.id.legal_webview);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl(getString(R.string.legal_url));

        ((TopHeaderFragment)getFragmentManager().findFragmentById(R.id.top_bar_fragment)).setHeaderText(R.string.legal_agreements);
    }


}
