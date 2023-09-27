package org.genevaers.genevaio.fieldnodes;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class RecordNode extends FieldNodeBase{

    private String name;
    private ComparisonState state;
    private RecordNode parent;
    private Map<String, FieldNodeBase> childrenByName = new TreeMap<>();
    private List<FieldNodeBase> children = new ArrayList<>();

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

    public RecordNode getParent() {
        return parent;
    }

    public void setParent(RecordNode parent) {
        this.parent = parent;
    }

    public List<FieldNodeBase> getChildren() {
        return children;
    }

    public void setChildren(List<FieldNodeBase> children) {
        this.children = children;
    }

    public void add(FieldNodeBase rn, boolean compare) {
        children.add(rn);
    }

}
