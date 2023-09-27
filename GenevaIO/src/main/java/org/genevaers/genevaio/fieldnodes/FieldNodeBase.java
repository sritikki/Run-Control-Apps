package org.genevaers.genevaio.fieldnodes;

import java.util.ArrayList;
import java.util.List;

public class FieldNodeBase {

    private String name;
    private ComparisonState state;
    private FieldNodeBase parent;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ComparisonState getState() {
        return state;
    }

    public void setState(ComparisonState state) {
        this.state = state;
    }

    public FieldNodeBase getParent() {
        return parent;
    }

    public void setParent(FieldNodeBase parent) {
        this.parent = parent;
    }

}
