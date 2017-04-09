package gigster.com.holdsum.views;

import android.app.DatePickerDialog;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import gigster.com.holdsum.helper.Utils;

/**
 * Created by tpaczesny on 2016-09-08.
 */
public class DatePickerEditText extends EditText implements DatePickerDialog.OnDateSetListener {

    private Calendar calendar;
    private OnValidationFailListener mOnValidationFailListener;
    private ValidationRule mValidationRule;

    public DatePickerEditText(Context context) {
        super(context);
    }

    public DatePickerEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public DatePickerEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        calendar = Calendar.getInstance();

        setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Date dateInView = Utils.parseDate(getText().toString());
                if (dateInView != null)
                    calendar.setTime(dateInView);

                new DatePickerDialog(DatePickerEditText.this.getContext(), DatePickerEditText.this
                        ,calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        calendar = null;
        setOnClickListener(null);
    }

    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear,
                          int dayOfMonth) {
        if (mValidationRule != null && !mValidationRule.validate(year,monthOfYear,dayOfMonth)) {
            if (mOnValidationFailListener != null)
                mOnValidationFailListener.onValidationFail(year,monthOfYear,dayOfMonth);
            return;
        }

        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, monthOfYear);
        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        updateLabel();
    }

    protected void updateLabel() {
        this.setText(Utils.formatDateForDisplay(calendar.getTime()));
    }

    public void setValidationRule(ValidationRule validationRule) {
        mValidationRule = validationRule;
    }


    public void setOnValidationFailListener(OnValidationFailListener onValidationFailListener) {
        this.mOnValidationFailListener = onValidationFailListener;
    }

    public interface OnValidationFailListener {
        void onValidationFail(int year, int monthOfYear, int dayOfMonth);
    }

    public interface ValidationRule {
        /**
         *
         * @param year
         * @param monthOfYear
         * @param dayOfMonth
         * @return true if selected year/month/day is valid
         */
        boolean validate(int year, int monthOfYear, int dayOfMonth);
    }
}
