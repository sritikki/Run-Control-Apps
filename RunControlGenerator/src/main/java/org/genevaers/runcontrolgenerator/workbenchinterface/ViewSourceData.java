package org.genevaers.runcontrolgenerator.workbenchinterface;

public class ViewSourceData {
    int id;
    int viewID;
    String extractFilter;
    String outputLogic;
    int sequenceNumber;
    int sourceLrId;
    int sourceLfId;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getViewID() {
        return viewID;
    }

    public void setViewID(int viewID) {
        this.viewID = viewID;
    }

    public String getExtractFilter() {
        return extractFilter;
    }

    public void setExtractFilter(String extractFilter) {
        this.extractFilter = extractFilter;
    }

    public int getSequenceNumber() {
        return sequenceNumber;
    }

    public void setSequenceNumber(int sequenceNumber) {
        this.sequenceNumber = sequenceNumber;
    }

    public int getSourceLrId() {
        return sourceLrId;
    }

    public void setSourceLrId(int sourceLrId) {
        this.sourceLrId = sourceLrId;
    }

    public int getSourceLfId() {
        return sourceLfId;
    }

    public void setSourceLfId(int sourceLfId) {
        this.sourceLfId = sourceLfId;
    }

    public void setOutputLogic(String outputLogic) {
        this.outputLogic = outputLogic;
    }

    public String getOutputLogic() {
        return outputLogic;
    }
}
