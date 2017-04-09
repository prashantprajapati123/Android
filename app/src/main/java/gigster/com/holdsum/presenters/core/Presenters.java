package gigster.com.holdsum.presenters.core;

import java.util.HashMap;

import gigster.com.holdsum.helper.Logger;


/**
 * Created by tpaczesny on 2016-09-02.
 */
public class Presenters {

    private static HashMap<String, BasePresenter> sMap = new HashMap<>();

    public static BasePresenter get(Class<?> viewClass) {
        BasePresenter presenter = sMap.get(viewClass.getName());
        if (presenter == null) {
            presenter = newPresenter(viewClass);
            sMap.put(viewClass.getName(), presenter);
        }
        return presenter;
    }

    private static BasePresenter newPresenter(Class<?> viewClass) {
        BasePresenter presenter = null;
        try {
            NoPresenter noPresenterAnnotation = viewClass.getAnnotation(NoPresenter.class);
            if (noPresenterAnnotation != null) {
                presenter = BasePresenter.class.newInstance();
            } else {
                UsesPresenter annotation = viewClass.getAnnotation(UsesPresenter.class);
                Class<? extends BasePresenter> presenterClass = annotation.value();
                presenter = presenterClass.newInstance();
                presenter.setUp();
            }
        } catch (InstantiationException e) {
            Logger.fail(e);
        } catch (IllegalAccessException e) {
            Logger.fail(e);
        } catch (NullPointerException e) {
            Logger.fail(e);
        }

        return presenter;
    }

    public static void destroy(BasePresenter presenter) {
        sMap.remove(presenter.getClass().getName());
    }

}
