package gigster.com.holdsum.views;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.support.v7.widget.AppCompatSpinner;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;

import java.util.Arrays;
import java.util.List;

/**
 * Created by tpaczesny on 2016-09-29.
 */

public class HintSpinner extends AppCompatSpinner {
    public HintSpinner(Context context) {
        super(context);
    }

    public HintSpinner(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context,attrs);
    }

    public HintSpinner(Context context, int mode) {
        super(context, mode);
    }

    public HintSpinner(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context,attrs);
    }

    public HintSpinner(Context context, AttributeSet attrs, int defStyleAttr, int mode) {
        super(context, attrs, defStyleAttr, mode);
        init(context,attrs);
    }

    public HintSpinner(Context context, AttributeSet attrs, int defStyleAttr, int mode, Resources.Theme popupTheme) {
        super(context, attrs, defStyleAttr, mode, popupTheme);
        init(context,attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.getResources().obtainAttributes(attrs, new int[]{android.R.attr.entries});
        CharSequence[] optionsArray = typedArray.getTextArray(0);
        typedArray.recycle();
        HintAdapter adapter = new HintAdapter(context, Arrays.asList(optionsArray), android.support.v7.appcompat.R.layout.support_simple_spinner_dropdown_item);
        adapter.setDropDownViewResource(android.support.v7.appcompat.R.layout.support_simple_spinner_dropdown_item);
        setAdapter(adapter);
        setNoSelection();

        setFocusable(true);
        setFocusableInTouchMode(true);
    }




    @Override
    public int getSelectedItemPosition() {
        int pos = super.getSelectedItemPosition();
        if (pos == getAdapter().getCount())
            return -1;
        else
            return pos;
    }

    @Override
    public Object getSelectedItem() {
        int pos = super.getSelectedItemPosition();
        if (pos == getAdapter().getCount())
            return null;
        else
            return super.getSelectedItem();
    }

    public void setNoSelection() {
        setSelection(getAdapter().getCount());
    }

    public class HintAdapter
            extends ArrayAdapter<CharSequence> {

        public HintAdapter(Context theContext, List<CharSequence> objects, int theLayoutResId) {
            super(theContext, theLayoutResId, android.R.id.text1, objects);
        }

        @Override
        public int getCount() {
            // don't display last item. It is used as hint.
            int count = super.getCount();
            return count > 0 ? count - 1 : count;
        }
    }

    private final OnFocusChangeListener clickOnFocus = new OnFocusChangeListener() {

        @Override
        public void onFocusChange(View v, boolean hasFocus) {

            // We don't want focusing the spinner with the d-pad to expand it in
            // the future, so remove this listener until the next touch event.
            setOnFocusChangeListener(null);
            performClick();
        }
    };

    // Add whatever constructor(s) you need.  Call
    // setFocusableInTouchMode(true) in them.

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        int action = event.getAction();
        if (action == MotionEvent.ACTION_DOWN) {

            // Only register the listener if the spinner does not already have
            // focus, otherwise tapping it would leave the listener attached.
            if (!hasFocus()) {
                setOnFocusChangeListener(clickOnFocus);
            }
        } else if (action == MotionEvent.ACTION_CANCEL) {
            setOnFocusChangeListener(null);
        }
        return super.onTouchEvent(event);
    }
}
