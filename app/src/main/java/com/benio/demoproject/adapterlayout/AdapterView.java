package com.benio.demoproject.adapterlayout;

/**
 * Created by benio on 2017/3/11.
 */
public interface AdapterView<T> {
    T getAdapter();

    void setAdapter(T adapter);
}
