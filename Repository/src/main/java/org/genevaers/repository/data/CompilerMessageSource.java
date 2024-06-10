package org.genevaers.repository.data;

public enum CompilerMessageSource {
    
    EXTRACT_FILTER("ExtFilter"), 
    EXTRACT_OUTPUT("ExtrOut"), 
    COLUMN("ColAssign"),
    COLUMN_CALC("ColCalc"), 
    FORMAT_FILTER("FormatFilter"), 
    VIEW_PROPS("View Properties");

    private final String value;

    private CompilerMessageSource(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return this.value;
    }

    public String value() {
        return this.value;
    }

}
