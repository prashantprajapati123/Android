package gigster.com.holdsum.events;

import gigster.com.holdsum.helper.enums.Mode;
import gigster.com.holdsum.helper.enums.UserStatus;
import gigster.com.holdsum.services.APIError;
import gigster.com.holdsum.services.RequestHandle;
import gigster.com.holdsum.services.ServicesManager;

/**
 * Created by tpaczesny on 2016-09-03.
 */
public class Events {
    public static class NextClickedEvent { }

    public static class ModeChanged {
        public final Mode newValue;

        public ModeChanged(Mode mode) {
            newValue = mode;
        }
    }

    public static class UserStatusChanged {
        public final UserStatus newValue;

        public UserStatusChanged(UserStatus userStatus) {
            newValue = userStatus;
        }
    }

    public static class ServiceRequestFailed {
        public final RequestHandle requestHandle;
        public final APIError apiError;

        public ServiceRequestFailed(RequestHandle requestHandle) {
            this.requestHandle = requestHandle;
            this.apiError = new APIError(null);
        }

        public ServiceRequestFailed(RequestHandle requestHandle, APIError apiError) {
            this.requestHandle = requestHandle;
            this.apiError = apiError;
        }

        public ServicesManager.RequestType getType() {
            return requestHandle.getType();
        }
    }

    public static class ServiceRequestSuccess<T> {
        public final RequestHandle requestHandle;
        public final T body;

        public ServiceRequestSuccess(RequestHandle requestHandle, T result) {
            this.requestHandle = requestHandle;
            this.body = result; }

        public ServicesManager.RequestType getType() {
            return requestHandle.getType();
        }
    }



}
