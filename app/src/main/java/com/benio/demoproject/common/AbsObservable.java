package com.benio.demoproject.common;

import android.database.Observable;

/**
 * Created by zhangzhibin on 2017/5/5.
 */
public class AbsObservable<T, O extends AbsObservable.Observer<T>> extends Observable<O> {

    public void notifyObservers(T data) {
        synchronized (mObservers) {
            // since onChanged() is implemented by the app, it could do anything, including
            // removing itself from {@link mObservers} - and that could cause problems if
            // an iterator is used on the ArrayList {@link mObservers}.
            // to avoid such problems, just march thru the list in the reverse order.
            for (int i = mObservers.size() - 1; i >= 0; i--) {
                mObservers.get(i).onChange(this, data);
            }
        }
    }

    public interface Observer<T> {

        /**
         * This method is called if the specified {@code AbsObservable} object's
         * {@code notifyObservers} method is called (because the {@code Observer}
         * object has been updated.
         *
         * @param observable the {@link AbsObservable} object.
         * @param data       The data being received.
         */
        void onChange(AbsObservable observable, T data);
    }
}
