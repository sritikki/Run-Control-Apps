package org.genevaers.repository.data;

public class InputReport {
    private String ddName;
    private String memberName;
    private String title;
    private String generationID;
    private int    recordCount;
    private String toBeProcessed;

    public void setDdName(String ddName) {
        this.ddName = ddName;
    }

    public String getDdName() {
        return ddName;
    }

    public void setGenerationID(String generationID) {
        this.generationID = generationID;
    }

    public String getGenerationID() {
        return generationID;
    }

    public void setMemberName(String memberName) {
        this.memberName = memberName;
    }

    public String getMemberName() {
        return memberName;
    }

    public void setRecordCount(int recordCount) {
        this.recordCount = recordCount;
    }

    public int getRecordCount() {
        return recordCount;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setToBeProcessed(String toBeProcessed) {
        this.toBeProcessed = toBeProcessed;
    }

    public String getToBeProcessed() {
        return toBeProcessed;
    }

}
