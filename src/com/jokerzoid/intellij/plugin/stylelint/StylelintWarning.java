package com.jokerzoid.intellij.plugin.stylelint;

import com.intellij.openapi.util.TextRange;

/**
 * This class represents the warnings listed inside a stylelint process output
 * @author Raul
 */
public class StylelintWarning {
    private Integer line;
    private Integer column;
    private String rule;
    private String severity;
    private String text;
    private Integer offset;

    public Integer getLine() {
        return line;
    }

    public void setLine(Integer line) {
        this.line = line;
    }

    public Integer getColumn() {
        return column;
    }

    public void setColumn(Integer column) {
        this.column = column;
    }

    public String getRule() {
        return rule;
    }

    public void setRule(String rule) {
        this.rule = rule;
    }

    public String getSeverity() {
        return severity;
    }

    public void setSeverity(String severity) {
        this.severity = severity;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Integer getOffset() {
        return offset;
    }

    public void setOffset(Integer offset) {
        this.offset = offset;
    }
}
