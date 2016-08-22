package com.jokerzoid.intellij.plugin.stylelint;

import org.jetbrains.annotations.NonNls;

/**
 * @author Raul
 */
public class StylelintPluginException extends RuntimeException {
    public StylelintPluginException() {
    }

    public StylelintPluginException(@NonNls String message) {
        super(message);
    }

    public StylelintPluginException(String message, Throwable cause) {
        super(message, cause);
    }

    public StylelintPluginException(Throwable cause) {
        super(cause);
    }

    public StylelintPluginException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
