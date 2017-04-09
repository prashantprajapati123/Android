package gigster.com.holdsum.viewmodel;

import android.databinding.ObservableField;

/**
 * Created by tpaczesny on 2016-09-08.
 */
public class LoginViewModel {

    public final ObservableField<String> username = new ObservableField<>("");
    public final ObservableField<String> password = new ObservableField<>("");

    public boolean hasDataFilled() {
        return !username.get().isEmpty() && !password.get().isEmpty();
    }

    public void clear() {
        username.set("");
        password.set("");
    }

}
