package gigster.com.holdsum.services;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by tpaczesny on 2016-10-03.
 */

public class APIError {
    public Map<String, Object> errors;

    public APIError(Map<String, Object> errors) {
        this.errors = errors;
    }

    public String getMessage() {
        if (errors == null)
            return null;

        StringBuilder builder = new StringBuilder();
        try {
            extractMessages(errors,builder);
        } catch (Exception e) {
            // in case JSON structure was not what we expected
            e.printStackTrace();
            return null;
        }
        // delete trailing newline
        if (builder.length() > 0)
            builder.deleteCharAt(builder.length()-1);

        return builder.toString();
    }

    private void extractMessages(Map<String,Object> map, StringBuilder builder) throws Exception {
        for (Object values : map.values()) {
            if (values instanceof List) {
                List<String> errorList = (List<String>) values;
                for (String errorMsg : errorList) {
                    builder.append(errorMsg);
                    builder.append('\n');
                }
            } else if (values instanceof Map) {
                extractMessages((Map<String, Object>) values,builder);
            }
        }
    }

    @Override
    public String toString() {
        return getMessage();
    }


}
