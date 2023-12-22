package org.genevaers.compilers.extract.emitters.rules;

import org.genevaers.compilers.extract.astnodes.ExtractBaseAST;

public abstract class Rule {

    public enum RuleResult {
        RULE_PASSED,
        RULE_WARNING,
        RULE_ERROR
    }

    protected static final int MAX_ZONED_LENGTH = 31;


    String name;

    //Some rules will require only a single argument, so just null op2?
    public abstract RuleResult apply(ExtractBaseAST op1, ExtractBaseAST op2);

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
