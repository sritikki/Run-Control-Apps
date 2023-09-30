package org.genevaers.genevaio.fieldnodes;

public class RecordNode extends FieldNodeBase{

    public RecordNode() {
        type = FieldNodeBase.Type.RECORD;
        state = ComparisonState.ORIGINAL;
    }

}
