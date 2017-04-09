package gigster.com.holdsum.helper.binding;

import android.databinding.BindingAdapter;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.Switch;

import gigster.com.holdsum.views.DatePickerEditText;
import gigster.com.holdsum.views.DocuSignWebView;
import gigster.com.holdsum.views.PlaidWebView;

/**
 * Created by tpaczesny on 2016-09-05.
 */
public class BindingAdapters {

    @BindingAdapter("bind:src_res")
    public static void setImageResource(ImageView view, int resId) {
        view.setImageResource(resId);
    }

    @BindingAdapter("bind:onCheckedChange")
    public static void setOnCheckedChangeListener(CompoundButton view, CompoundButton.OnCheckedChangeListener listener) {
        view.setOnCheckedChangeListener(listener);
    }

    @BindingAdapter("bind:onValidationFail")
    public static void setOnValidationFailListener(DatePickerEditText datePickerEditText, DatePickerEditText.OnValidationFailListener listener) {
        datePickerEditText.setOnValidationFailListener(listener);
    }

    @BindingAdapter("bind:onSuccess")
    public static void setOnVerificationSuccessListener(PlaidWebView plaidWebView, PlaidWebView.OnVerificationSuccessListener listener) {
        plaidWebView.setOnVerificationSuccessListener(listener);
    }

    @BindingAdapter("bind:onExit")
    public static void setOnVerificationExitedListener(PlaidWebView plaidWebView, PlaidWebView.OnVerificationExitedListener listener) {
        plaidWebView.setOnVerificationExitedListener(listener);
    }

    @BindingAdapter("bind:onFinished")
    public static void setOnSigningFinishedListner(DocuSignWebView docuSignWebView, DocuSignWebView.OnSigningFinishedListener listener) {
        docuSignWebView.setOnSigningFinishedListener(listener);
    }

}
