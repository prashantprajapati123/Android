package gigster.com.holdsum.viewmodel;

import android.databinding.ObservableArrayList;
import android.databinding.ObservableField;
import android.databinding.ObservableInt;
import android.os.Parcel;
import android.os.Parcelable;

import gigster.com.holdsum.helper.Utils;

/**
 * Created by tpaczesny on 2016-09-08.
 */
public class BorrowViewModel {

    public static final int QUESTION_2_TEXTBOX_ANSWER = 0;
    public static final int QUESTION_5_ALT_ANSWER = 5;
    public static final int QUESTION_7_NO_TEXTBOX_ANSWER = 9;



    public final ObservableField<String> repaymentDate = new ObservableField<>("");
    public final ObservableField<String> amount = new ObservableField<>("");

    public final ObservableField<Integer> question1Answer = new ObservableField<>(-1);
    public final ObservableField<Integer> question2Answer = new ObservableField<>(-1);
    public final ObservableField<Integer> question3Answer = new ObservableField<>(-1);
    public final ObservableField<Integer> question4Answer = new ObservableField<>(-1);
    public final ObservableField<Integer> question5Answer = new ObservableField<>(-1);
    public final ObservableField<Integer> question5AnswerAlt = new ObservableField<>(-1);
    public final ObservableField<Integer> question6Answer = new ObservableField<>(-1);
    public final ObservableField<Integer> question7aAnswer = new ObservableField<>(-1);
    public final ObservableField<Integer> question7bAnswer = new ObservableField<>(-1);
    public final ObservableField<String> question2Textbox = new ObservableField<>("");
    public final ObservableField<String> question7aTextbox = new ObservableField<>("");
    public final ObservableField<String> question7bTextbox = new ObservableField<>("");

    public String fundsAvailable;
    public String fundsSource;
    public String nextPaydate;


    public BorrowViewModel() {
    }

    public boolean hasAllRequiredFields() {
        boolean hasRequiredFields = !amount.get().isEmpty() &&
                !repaymentDate.get().isEmpty() &&
                question1Answer.get() > -1 && question2Answer.get() > -1 &&
                (question2Answer.get() != QUESTION_2_TEXTBOX_ANSWER || !question2Textbox.get().isEmpty())
                && question3Answer.get() > -1 && question4Answer.get() > -1 && question5Answer.get() > -1 &&
                (question5Answer.get() != QUESTION_5_ALT_ANSWER || question5AnswerAlt.get() > -1) &&
                question6Answer.get() > -1 && question7aAnswer.get() > -1 &&
                (question7aAnswer.get() == QUESTION_7_NO_TEXTBOX_ANSWER || !question7aTextbox.get().isEmpty() ) &&
                question7bAnswer.get() > -1 &&
                (question7bAnswer.get() == QUESTION_7_NO_TEXTBOX_ANSWER || !question7bTextbox.get().isEmpty() );

        return hasRequiredFields;
    }

    public boolean hasAmountValid() {
        try {
            double amountNumber = Utils.safeParseDouble(amount.get());
            if (amountNumber > 0 && amountNumber <= Double.parseDouble(fundsAvailable))
                return true;
        } catch (NumberFormatException e) {
        }
        return false;
    }

    public void clear() {
        repaymentDate.set("");
        amount.set("");
        question1Answer.set(-1);
        question2Answer.set(-1);
        question3Answer.set(-1);
        question4Answer.set(-1);
        question5Answer.set(-1);
        question5AnswerAlt.set(-1);
        question6Answer.set(-1);
        question7aAnswer.set(-1);
        question7bAnswer.set(-1);
        question2Textbox.set("");
        question7aTextbox.set("");
        question7bTextbox.set("");

    }
}
