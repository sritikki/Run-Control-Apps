package org.genevaers.compilers.extract.astnodes;

import java.io.IOException;

import org.yaml.snakeyaml.emitter.Emitable;
import org.yaml.snakeyaml.events.Event;

public class FunctionAST extends ExtractBaseAST implements Emitable{

    String function;

    @Override
    public void emit(Event arg0) throws IOException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'emit'");
    }

    public void setFunction(String function) {
        this.function = function;
    }

    public String getFunction() {
        return function;
    }
    
}
