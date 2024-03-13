package org.genevaers.runcontrolgenerator.workbenchinterface;

import org.genevaers.repository.components.enums.DataType;
import org.genevaers.repository.components.enums.DateCode;
import org.genevaers.repository.components.enums.ExtractArea;
import org.genevaers.repository.components.enums.JustifyId;

public class ColumnInfo {
    int columnNumber;
    int columnId;
    ExtractArea extractArea;
    DataType dataType; // May have to set via ordinal
    boolean signed;
    int length;
    int numDecimalPlaces;
    DateCode dateCode;
    int rounding;
    JustifyId alignment;
    String numericMask; // Needed?
    int startPosition;
    int ordinal;

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

    public ExtractArea getExtractArea() {
        return extractArea;
    }

    public void setExtractArea(ExtractArea extractArea) {
        this.extractArea = extractArea;
    }

    public DataType getDataType() {
        return dataType;
    }

    public void setDataType(DataType dataType) {
        this.dataType = dataType;
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

    public DateCode getDateCode() {
        return dateCode;
    }

    public void setDateCode(DateCode dateCode) {
        this.dateCode = dateCode;
    }

    public int getRounding() {
        return rounding;
    }

    public void setRounding(int rounding) {
        this.rounding = rounding;
    }

    public JustifyId getAlignment() {
        return alignment;
    }

    public void setAlignment(JustifyId alignment) {
        this.alignment = alignment;
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

}
