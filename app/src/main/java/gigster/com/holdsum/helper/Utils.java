package gigster.com.holdsum.helper;

import android.text.Html;
import android.text.Spanned;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Map;

/**
 * Created by Eshaan on 2/27/2016.
 */
public class Utils {

    private static SimpleDateFormat dateFormatForDisplay = new SimpleDateFormat("MM/dd/yy", Locale.US);
    private static SimpleDateFormat dateFormatForAPI = new SimpleDateFormat("yyyy-MM-dd", Locale.US);

    public static String formatDateForDisplay(Date date) {
        if (date != null)
            return dateFormatForDisplay.format(date);
        else
            return null;
    }

    public static String formatDateForDisplay(String dateStr) {
        return formatDateForDisplay(parseDate(dateStr));
    }

    public static Date parseDate(String dateStr) {
        if (dateStr == null)
            return null;

        Date date = null;
        try {
            date = dateFormatForDisplay.parse(dateStr);
        } catch (ParseException e) {
            try {
                date = dateFormatForAPI.parse(dateStr);
            } catch (ParseException e1) {
            }
        }
        return date;
    }

    public static String formatDateForAPI(Date date) {
        if (date != null)
            return dateFormatForAPI.format(date);
        else
            return null;
    }

    public static String formatDateForAPI(String dateStr) {
        return formatDateForAPI(parseDate(dateStr));
    }

    public static boolean isNumber(String numberStr) {
        try {
            Double.parseDouble(numberStr);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static Spanned colorText(Map<String, String> stringToColor) {
        StringBuilder builder = new StringBuilder();
        for (Map.Entry<String, String> entry : stringToColor.entrySet()) {
            builder.append("<font color="+entry.getValue()+">"+entry.getKey()+"</font> ");
        }
        return Html.fromHtml(builder.toString());
    }

    public static boolean serializeToFile(Serializable obj, File file) {
        ObjectOutputStream oos = null;
        try {
            oos = new ObjectOutputStream(new FileOutputStream(file));
            oos.writeObject(obj);
            oos.flush();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        } finally {
            closeQuietly(oos);
        }
    }

    public static Object loadFromFile(File file) {
        ObjectInputStream ois = null;
        try {
            ois = new ObjectInputStream(new FileInputStream(file));
            Object obj = ois.readObject();
            return obj;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        } finally {
            closeQuietly(ois);
        }
    }

    public static void closeQuietly(InputStream stream) {
        if (stream != null)
            try {
                stream.close();
            } catch (IOException e) {
            }
    }

    public static void closeQuietly(OutputStream stream) {
        if (stream != null)
            try {
                stream.close();
            } catch (IOException e) {
            }
    }

    public static double safeParseDouble(String doubleStr) {
        double result;
        try {
            result = Double.parseDouble(doubleStr);
        } catch (NumberFormatException e) {
            result = 0;
        }
        return result;
    }

}
