package org.genevaers.runcontrolgenerator.workbenchinterface;

public class ColumnData {
    int viewID;
    int columnNumber;
    int columnId;
    int extractAreaValue;
    int dataTypeValue; // May have to set via ordinal
    boolean signed;
    int length;
    int numDecimalPlaces;
    int dateCodeValue;
    int rounding;
    int alignmentValue;
    String numericMask; // Needed?
    int startPosition;
    int ordinal;
    String name;

    public int getViewID() {
        return viewID;
    }

    public void setViewID(int viewID) {
        this.viewID = viewID;
    }
    
    public int getColumnNumber() {
        return columnNumber;
    }

    public void setColumnNumber(int columnNumber) {
        this.columnNumber = columnNumber;
    }

    public int getColumnId() {
        return columnId;
    }

    public void setColumnId(int columnId) {
        this.columnId = columnId;
    }

    public int getExtractAreaValue() {
        return extractAreaValue;
    }

    public void setExtractAreaValue(int extractArea) {
        this.extractAreaValue = extractArea;
    }

    public int getDataTypeValue() {
        return dataTypeValue;
    }

    public void setDataTypeValue(int dataType) {
        this.dataTypeValue = dataType;
    }

    public boolean isSigned() {
        return signed;
    }

    public void setSigned(boolean signed) {
        this.signed = signed;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public int getNumDecimalPlaces() {
        return numDecimalPlaces;
    }

    public void setNumDecimalPlaces(int numDecimalPlaces) {
        this.numDecimalPlaces = numDecimalPlaces;
    }

    public int getDateCodeValue() {
        return dateCodeValue;
    }

    public void setDateCodeValue(int dateCode) {
        this.dateCodeValue = dateCode;
    }

    public int getRounding() {
        return rounding;
    }

    public void setRounding(int rounding) {
        this.rounding = rounding;
    }

    public int getAlignment() {
        return alignmentValue;
    }

    public void setAlignment(int alignment) {
        this.alignmentValue = alignment;
    }

    public String getNumericMask() {
        return numericMask;
    }

    public void setNumericMask(String numericMask) {
        this.numericMask = numericMask;
    }

    public int getStartPosition() {
        return startPosition;
    }

    public void setStartPosition(int startPosition) {
        this.startPosition = startPosition;
    }

    public int getOrdinal() {
        return ordinal;
    }

    public void setOrdinal(int ordinal) {
        this.ordinal = ordinal;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
