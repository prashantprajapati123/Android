package gigster.com.holdsum.views;

import android.content.Context;
import android.net.Uri;
import android.util.AttributeSet;
import android.util.Log;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import java.util.Locale;

/**
 * Created by tpaczesny on 2016-09-26.
 */
public class DocuSignWebView extends WebView {
    private OnSigningFinishedListener mListener;

    public static final String DOCUSIGN_CALLBACK_SCHEME = "holdsum";

    public enum SigningStatus {

        CANCEL, DECLINE, EXCEPTION, FAX_PENDING, SESSION_TIMEOUT, SIGNING_COMPLETE, TTL_EXPIRED, VIEWING_COMPLETE;

        /** Based on DocuSign documentation:
         * https://docs.docusign.com/esign/guide/usage/embedded_signing.html
         * @param event
         * @return
         */
        public static SigningStatus fromEventString(String event) {
            return valueOf(event.toUpperCase(Locale.getDefault()));
        }
    }

    public DocuSignWebView(Context context) {
        super(context);
        init();
    }

    public DocuSignWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public DocuSignWebView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        getSettings().setJavaScriptEnabled(true);
        setWebViewClient(new DocuSignWebViewClient());
    }

    public void setOnSigningFinishedListener(OnSigningFinishedListener listener) {
        mListener = listener;
    }

    private class DocuSignWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            Uri uri = Uri.parse(url);
            if (uri.getScheme().equals(DOCUSIGN_CALLBACK_SCHEME)) {
                Log.d("DocuSignWebView", "callback: " + url);
                String event = uri.getQueryParameter("event");
                Log.d("DocuSignWebView", "result: " + event);
                mListener.onFinished(SigningStatus.fromEventString(event));
                return true;
            }
            else {
                return false;
            }
        }
    }

    public interface OnSigningFinishedListener {
        void onFinished(SigningStatus status);
    }

}
