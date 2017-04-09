package gigster.com.holdsum.views;

import android.content.Context;
import android.net.Uri;
import android.util.AttributeSet;
import android.util.Log;
import android.webkit.WebView;
import android.webkit.WebViewClient;

/**
 * Created by tpaczesny on 2016-09-12.
 */
public class PlaidWebView extends WebView {
    private OnVerificationSuccessListener mSuccessListener;
    private OnVerificationExitedListener mExitListener;

    private static final String PLAID_CALLBACK_SCHEME = "holdsum";
    private static final String PLAID_CALLBACK_HOST_ON_SUCCESS = "handlePublicToken";
    private static final String PLAID_CALLBACK_HOST_ON_EXIT = "handleOnExit";

    public PlaidWebView(Context context) {
        super(context);
        init();
    }

    public PlaidWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public PlaidWebView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        getSettings().setJavaScriptEnabled(true);
        setWebViewClient(new PlaidWebViewClient());
        loadUrl("file:///android_asset/plaid.html");
    }

    public void setOnVerificationSuccessListener(OnVerificationSuccessListener listener) {
        mSuccessListener = listener;
    }

    public void setOnVerificationExitedListener(OnVerificationExitedListener listener) {
        mExitListener = listener;
    }

    private class PlaidWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            Uri uri = Uri.parse(url);
            if (uri.getScheme().equals(PLAID_CALLBACK_SCHEME)) {
                Log.d("PlaidWebView", "callback: " + url);
                if (uri.getHost().equals(PLAID_CALLBACK_HOST_ON_SUCCESS)) {
                    mSuccessListener.onSuccess(uri.getFragment());
                } else if (uri.getHost().equals(PLAID_CALLBACK_HOST_ON_EXIT)) {
                    mExitListener.onExit();
                }
                return true;
            } else {
                return false;
            }
        }
    }

    public interface OnVerificationSuccessListener {
        void onSuccess(String publicToken);
    }

    public interface OnVerificationExitedListener {
        void onExit();
    }
}
