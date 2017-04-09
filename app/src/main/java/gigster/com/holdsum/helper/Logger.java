package gigster.com.holdsum.helper;

import android.content.Context;
import android.util.Log;

import gigster.com.holdsum.R;

/**
 * Created by tpaczesny on 2016-09-01.
 *
 * Simple wrapper over system logs that allows to disable them based on app config.
 */
public class Logger {

    private static boolean logsEnabled = false;

    public static void init(Context appContext) {
        logsEnabled = appContext.getResources().getBoolean(R.bool.logsEnabled);
    }

    public static void i(String tag, String message) {
        if (logsEnabled)
            Log.i(tag,message);
    }

    public static void d(String tag, String message) {
        if (logsEnabled)
            Log.d(tag,message);
    }

    public static void e(String tag, String message) {
        if (logsEnabled)
            Log.e(tag,message);
    }

    public static void v(String tag, String message) {
        if (logsEnabled)
            Log.v(tag,message);
    }

    public static void w(String tag, String message) {
        if (logsEnabled)
            Log.w(tag,message);
    }


    public static void fail(Throwable t) {
        t.printStackTrace();
        // kill the app on fail to stop further consequences
        throw new Error(t);
    }
}
