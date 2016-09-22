package com.benio.demoproject.progress;

import android.support.annotation.NonNull;

/**
 * Interface used to format current value into a string for presentation.
 */
public interface Formatter {

    /**
     * Formats a string representation of the current value.
     *
     * @param value The currently progress value.
     * @return A formatted string representation.
     */
    @NonNull
    public String format(float value);
}