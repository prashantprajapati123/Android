package gigster.com.holdsum.viewmodel;

import android.databinding.ObservableField;

/**
 * Created by tpaczesny on 2016-09-08.
 */
public class UserSignupViewModel {

    public final ObservableField<String> username = new ObservableField<>("");
    public final ObservableField<String> email = new ObservableField<>("");
    public final ObservableField<String> password = new ObservableField<>("");
    public final ObservableField<String> confirmPassword = new ObservableField<>("");

    public boolean hasDataFilled() {
        return !username.get().isEmpty() && !email.get().isEmpty() && !password.get().isEmpty() && !confirmPassword.get().isEmpty();
    }

    public boolean hasPasswordCorrectlyConfirmed() {
        return password.get().equals(confirmPassword.get());
    }

    public boolean hasValidPassword() {
        return password.get().length() >= 6;
    }

    public void clear() {
        username.set("");
        email.set("");
        password.set("");
        confirmPassword.set("");
    }
}
