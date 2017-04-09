package gigster.com.holdsum.activities;

import android.app.ProgressDialog;
import android.widget.Toast;

import gigster.com.holdsum.presenters.core.BasePresenter;
import gigster.com.holdsum.presenters.core.PresenterActivity;

/**
 * Created by tpaczesny on 2016-10-21.
 */

public abstract class HoldsumActivity<T extends BasePresenter> extends PresenterActivity<T> {

    protected ProgressDialog mProgressDialog;

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (mProgressDialog != null) {
            mProgressDialog.dismiss();
        }
    }

    public void showProgressDialog(int messageId) {
        mProgressDialog = ProgressDialog.show(this, getString(messageId), null, true, false);
    }

    public void hideProgressDialog() {
        mProgressDialog.cancel();
    }


    /**
     * Presents a message as a Toast object
     * @param message
     */
    public void showMessage(CharSequence message) {
        int toastLen = message.length() > 60 ? Toast.LENGTH_LONG : Toast.LENGTH_SHORT;
        Toast.makeText(this, message, toastLen).show();
    }

    public void showMessage(int messageId) {
        showMessage(getString(messageId));
    }

    /**
     * Version allowing to specify fallback resource id in case provide message is null.
     * @param message
     * @param fallbackMessageId
     */
    public void showMessage(CharSequence message, int fallbackMessageId) {
        if (message != null)
            showMessage(message);
        else
            showMessage(fallbackMessageId);
    }
}
