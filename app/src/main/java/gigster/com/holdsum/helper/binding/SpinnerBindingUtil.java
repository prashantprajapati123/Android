package gigster.com.holdsum.helper.binding;

import android.databinding.BindingAdapter;
import android.databinding.InverseBindingAdapter;
import android.databinding.InverseBindingListener;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import gigster.com.holdsum.views.HintSpinner;

/**
 * Inpired by:
 * http://stackoverflow.com/questions/37874091/android-spinner-data-binding-using-xml-and-show-the-selected-values
 * Created by tpaczesny on 2016-09-29.
 */

public class SpinnerBindingUtil {

    @BindingAdapter(value = {"bind:selectedValue", "bind:selectedValueAttrChanged"}, requireAll = false)
    public static void bindSpinnerData(Spinner pSpinner, String newSelectedValue, final InverseBindingListener newTextAttrChanged) {
        pSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                newTextAttrChanged.onChange();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        if (newSelectedValue != null) {
            int pos = ((ArrayAdapter<String>) pSpinner.getAdapter()).getPosition(newSelectedValue);
            pSpinner.setSelection(pos, true);
        }
    }
    @InverseBindingAdapter(attribute = "bind:selectedValue", event = "bind:selectedValueAttrChanged")
    public static String captureSelectedValue(Spinner pSpinner) {
        return (String) pSpinner.getSelectedItem();
    }

    @BindingAdapter(value = {"bind:selectedPosition", "bind:selectedPositionAttrChanged"}, requireAll = false)
    public static void bindSpinnerPosition(Spinner pSpinner, Integer newSelectedPosition, final InverseBindingListener newTextAttrChanged) {
        pSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                newTextAttrChanged.onChange();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        if (newSelectedPosition != null && newSelectedPosition >= 0) {
            pSpinner.setSelection(newSelectedPosition, true);
        } else if (pSpinner instanceof HintSpinner) {
            ((HintSpinner)pSpinner).setNoSelection();
        }
    }
    @InverseBindingAdapter(attribute = "bind:selectedPosition", event = "bind:selectedPositionAttrChanged")
    public static Integer captureSelectedPositionValue(Spinner pSpinner) {
        return pSpinner.getSelectedItemPosition();
    }

    @BindingAdapter("bind:onItemSelectedListener")
    public static void setOnItemSelectedListener(Spinner spinner, final OnItemSelected onItemSelectedListener) {
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                onItemSelectedListener.onItemSelected(adapterView,view,position,id);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                onItemSelectedListener.onItemSelected(adapterView,null,-1,-1);
            }
        });
    }

    public interface OnItemSelected {
        void onItemSelected(AdapterView<?> adapter, View view, int position, long id);
    }


}