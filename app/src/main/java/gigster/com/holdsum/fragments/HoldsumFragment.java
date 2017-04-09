package gigster.com.holdsum.fragments;

import gigster.com.holdsum.activities.HoldsumActivity;
import gigster.com.holdsum.presenters.core.BasePresenter;
import gigster.com.holdsum.presenters.core.PresenterFragment;

/**
 * Created by tpaczesny on 2016-10-21.
 */

public abstract class HoldsumFragment<T extends BasePresenter> extends PresenterFragment<T> {

    public void showProgressDialog(int messageId) {
        ((HoldsumActivity)getActivity()).showProgressDialog(messageId);
    }

    public void hideProgressDialog() {
        ((HoldsumActivity)getActivity()).hideProgressDialog();
    }

    /**
     * Presents a message as a Toast object
     * @param message
     */
    public void showMessage(CharSequence message) {
        ((HoldsumActivity)getActivity()).showMessage(message);
    }

    public void showMessage(int messageId) {
        ((HoldsumActivity)getActivity()).showMessage(messageId);
    }

    /**
     * Version allowing to specify fallback resource id in case provide message is null.
     * @param message
     * @param fallbackMessageId
     */
    public void showMessage(CharSequence message, int fallbackMessageId) {
        ((HoldsumActivity)getActivity()).showMessage(message,fallbackMessageId);
    }

}


