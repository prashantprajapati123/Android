package gigster.com.holdsum.services;

import retrofit2.Call;

/**
 * Created by tpaczesny on 2016-09-13.
 */
public class RetrofitRequestHandle implements RequestHandle{

    private final ServicesManager.RequestType mType;
    private final Call mCall;
    private boolean mEnded;

    public RetrofitRequestHandle(Call call, ServicesManager.RequestType type) {
        mCall = call;
        mType = type;
        mEnded = false;
    }

    @Override
    public void cancel() {
        mCall.cancel();
    }

    @Override
    public boolean hasEnded() {
        return mEnded;
    }

    @Override
    public ServicesManager.RequestType getType() {
        return mType;
    }

    public void setHasEnded(boolean ended) {
        mEnded = ended;
    }
}
