package org.genevaers.compilers.extract.emitters.rules;

import java.util.ArrayList;
import java.util.List;

import org.genevaers.compilers.extract.astnodes.ExtractBaseAST;
import org.genevaers.compilers.extract.emitters.rules.Rule.RuleResult;

public class RulesChecker {

    List<Rule> localRules = new ArrayList<>();

    public RuleResult apply(ExtractBaseAST op1, ExtractBaseAST op2) {
        return getResult(localRules, op1, op2);
    }

    public RuleResult applyRulesTo(List<Rule> rules, ExtractBaseAST op1, ExtractBaseAST op2) {
        return getResult(rules, op1, op2);
    }

    public void addRule(Rule r) {
        localRules.add(r);
    }
    
    private RuleResult getResult(List<Rule> rules, ExtractBaseAST op1, ExtractBaseAST op2) {
        RuleResult allPassed = RuleResult.RULE_PASSED;
        for (Rule rule : rules) {
            RuleResult ruleResult = rule.apply(op1, op2);
            allPassed = updateResult(allPassed, ruleResult);
        }
        return allPassed;
    }

    protected RuleResult updateResult(RuleResult allPassed, RuleResult ruleResult) {
        return ruleResult.ordinal() > allPassed.ordinal() ? ruleResult : allPassed;
    }

}
