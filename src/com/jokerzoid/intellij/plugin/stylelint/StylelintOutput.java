package com.jokerzoid.intellij.plugin.stylelint;

import java.util.List;

/**
 * This class represents the output of the stylelint process using the '-f json' modifier
 * @author Raul
 */
public class StylelintOutput {
    private String source;
    private Boolean errored;
    private List<StylelintWarning> warnings;

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public Boolean getErrored() {
        return errored;
    }

    public void setErrored(Boolean errored) {
        this.errored = errored;
    }

    public List<StylelintWarning> getWarnings() {
        return warnings;
    }

    public void setWarnings(List<StylelintWarning> warnings) {
        this.warnings = warnings;
    }
}
