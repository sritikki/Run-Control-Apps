package org.genevaers.compilers.extract.emitters.rules;

import java.util.ArrayList;
import java.util.List;

import org.genevaers.compilers.extract.astnodes.ExtractBaseAST;
import org.genevaers.compilers.extract.emitters.rules.Rule.RuleResult;

public class RulesChecker {

    List<Rule> rules = new ArrayList<>();

    public RuleResult apply(ExtractBaseAST op1, ExtractBaseAST op2) {
        RuleResult allPassed = RuleResult.RULE_PASSED;
        for (Rule rule : rules) {
            RuleResult ruleResult = rule.apply(op1, op2);
            allPassed =  ruleResult.ordinal() > allPassed.ordinal() ? ruleResult : allPassed;
        }
        return allPassed;
    }

    public void addRule(Rule r) {
        rules.add(r);
    }
    
}
