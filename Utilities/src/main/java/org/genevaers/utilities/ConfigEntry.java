package org.genevaers.utilities;

public class ConfigEntry {
    String value = "";
    boolean hidden = false;

    public ConfigEntry(String value, boolean hidden) {
        this.value = value;
        this.hidden = hidden;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public boolean isHidden() {
        return hidden;
    }

    public void setHidden(boolean hidden) {
        this.hidden = hidden;
    }
}