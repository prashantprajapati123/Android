package gigster.com.holdsum.helper;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;

import gigster.com.holdsum.activities.AboutActivity;
import gigster.com.holdsum.activities.BankVerificationActivity;
import gigster.com.holdsum.activities.ConfirmationActivity;
import gigster.com.holdsum.activities.EntranceActivity;
import gigster.com.holdsum.activities.HomeActivity;
import gigster.com.holdsum.activities.InfoActivity;
import gigster.com.holdsum.activities.LegalActivity;
import gigster.com.holdsum.activities.InvestActivity;
import gigster.com.holdsum.activities.LoginActivity;
import gigster.com.holdsum.activities.RegistrationActivity;
import gigster.com.holdsum.activities.SplashActivity;
import gigster.com.holdsum.activities.UserSignupActivity;

/**
 * Created by tpaczesny on 2016-09-12.
 *
 * Implements all transitions between activities
 */
public class FlowController {

    private static FlowController sInstance;

    public static FlowController getInstance() {
        if (sInstance == null)
            sInstance = new FlowController();
        return sInstance;
    }


    public enum Screen {
        SPLASH_SCREEN, ENTRANCE, INFO, LOGIN, REGISTRATION, BANK_VERIFICATION, HOME, CONFIRMATION, USER_SIGNUP, ABOUT, INVEST, LEGAL;
    }

    public static class Arguments {
        public static final String LOAN_ID = "loan_id";
    }

    public void goTo(Activity contextActivity, Screen screen, boolean keepCurrentScreenInStack, Bundle arguments) {
        startActivity(contextActivity,screen,keepCurrentScreenInStack,arguments,0);
    }

    public void goTo(Activity contextActivity, Screen screen, boolean keepCurrentScreenInStack) {
        goTo(contextActivity,screen,keepCurrentScreenInStack,null);
    }

    public void goTo(Fragment contextFragment, Screen screen, boolean keepCurrentScreenInStack, Bundle arguments) {
        goTo(contextFragment.getActivity(),screen,keepCurrentScreenInStack, arguments);
    }

    public void goTo(Fragment contextFragment, Screen screen, boolean keepCurrentScreenInStack) {
        goTo(contextFragment.getActivity(),screen,keepCurrentScreenInStack);
    }

    public void setTo(Activity contextActivity, Screen screen, Bundle arguments) {
        startActivity(contextActivity,screen,false,arguments,Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
    }

    public void setTo(Activity contextActivity, Screen screen) {
        setTo(contextActivity,screen,null);
    }

    public void setTo(Fragment contextFragment, Screen screen, Bundle arguments) {
        setTo(contextFragment.getActivity(),screen, arguments);
    }

    public void setTo(Fragment contextFragment, Screen screen) {
        setTo(contextFragment.getActivity(),screen);
    }

    private void startActivity(Activity contextActivity, Screen screen, boolean keepCurrentScreenInStack, Bundle arguments, int flags) {
        Intent i = new Intent(contextActivity, getActivityClassForScreen(screen));
        if (arguments != null)
            i.putExtras(arguments);
        i.addFlags(flags);
        contextActivity.startActivity(i);
        if (!keepCurrentScreenInStack)
            contextActivity.finish();
    }

    public void navigateBack(Activity contextActivity) {
        contextActivity.finish();
    }

    private static Class<? extends Activity> getActivityClassForScreen(Screen screen) {
        switch (screen) {
            case SPLASH_SCREEN:
                return SplashActivity.class;
            case ENTRANCE:
                return EntranceActivity.class;
            case INFO:
                return InfoActivity.class;
            case LOGIN:
                return LoginActivity.class;
            case REGISTRATION:
                return RegistrationActivity.class;
            case BANK_VERIFICATION:
                return BankVerificationActivity.class;
            case HOME:
                return HomeActivity.class;
            case CONFIRMATION:
                return ConfirmationActivity.class;
            case USER_SIGNUP:
                return UserSignupActivity.class;
            case ABOUT:
                return AboutActivity.class;
            case LEGAL:
                return LegalActivity.class;
            case INVEST:
                return InvestActivity.class;
            default:
                return null;
        }
    }



}
