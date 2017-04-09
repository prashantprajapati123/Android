package gigster.com.holdsum.model;

/**
 * Created by tpaczesny on 2016-09-27.
 */
public class LoanRequest {
    public String amount;
    public String repayment_date;
    public Choice[] responses;
    
    public class Choice {

        public Choice(int choice, String textbox) {
            this.choice = choice;
            this.textbox = textbox;
        }

        public int choice;
        public String textbox;
    }
}
