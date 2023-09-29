package org.genevaers.genevaio.fieldnodes;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class FieldNodeBase {
    public enum Type {
        RECORD("Record"), 
        STRINGFIELD("String"),
        NUMBERFIELD("Number")
         ;

        private String name;
        private Type(String n) {
            name = n;
        }
        @Override
        public String toString() {
            return name;
        }

    }

    private String name;
    private ComparisonState state;
    private FieldNodeBase parent;
    protected Type type;
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

    public FieldNodeBase getParent() {
        return parent;
    }

    public void setParent(FieldNodeBase parent) {
        this.parent = parent;
    }

    public Type getType() {
        return type;
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
