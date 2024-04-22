package org.genevaers.runcontrolgenerator.workbenchinterface;

public class ViewData {

    private int id;
    private String name;
    private int typeValue;
    private String formatFilter;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getTypeValue() {
        return typeValue;
    }

    public void setTypeValue(int typeValue) {
        this.typeValue = typeValue;
    }

    public void setFormatFilter(String formatFilter) {
        this.formatFilter = formatFilter;
    }

    public String getFormatFilter() {
        return formatFilter;
    }

}
