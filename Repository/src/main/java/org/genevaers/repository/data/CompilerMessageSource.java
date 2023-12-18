package org.genevaers.repository.data;

public enum CompilerMessageSource {
    
    EXTRACT_FILTER("ExtFilter"), 
    EXTRACT_OUTPUT("ExtrOut"), 
    COLUMN("ColAssign"); 

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
