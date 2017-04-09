package gigster.com.holdsum.services;

/**
 * Created by tpaczesny on 2016-09-13.
 */
public interface RequestHandle {

    void cancel();

    boolean hasEnded();

    ServicesManager.RequestType getType();

}
