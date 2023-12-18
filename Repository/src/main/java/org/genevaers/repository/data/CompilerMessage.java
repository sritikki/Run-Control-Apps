package org.genevaers.repository.data;

public class CompilerMessage {
    private int viewid;
    private CompilerMessageSource source;
    private int srcLR;
    private int srcLF;
    private int columnNumber;
    private String detail;

    public CompilerMessage(int viewid, CompilerMessageSource source, int srcLR, int srcLF, int columnNumber, String detail) {
        this.viewid = viewid;
        this.source = source;
        this.srcLR = srcLR;
        this.srcLF = srcLF;
        this.columnNumber = columnNumber;
        this.detail = detail;
    }

    public int getViewid() {
        return viewid;
    }

    public void setViewid(int viewid) {
        this.viewid = viewid;
    }

    public CompilerMessageSource getSource() {
        return source;
    }

    public void setSource(CompilerMessageSource source) {
        this.source = source;
    }

    public int getSrcLR() {
        return srcLR;
    }

    public int getSrcLF() {
        return srcLF;
    }

    public int getColumnNumber() {
        return columnNumber;
    }

    public void setColumnNumber(int columnNumber) {
        this.columnNumber = columnNumber;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public void setSrcLR(int srcLR) {
        this.srcLR = srcLR;
    }

    public void setSrcLF(int srcLF) {
        this.srcLF = srcLF;
    }

}
