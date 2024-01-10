package org.genevaers.compilers.extract.emitters.comparisonemitters;


import org.genevaers.compilers.extract.emitters.rules.CanCompareDates;
import org.genevaers.compilers.extract.emitters.rules.CompareFlipLhs;
import org.genevaers.compilers.extract.emitters.rules.CompareFlipRhs;
import org.genevaers.compilers.extract.emitters.rules.RulesChecker;

public class ComparisonRulesChecker extends RulesChecker{

    public ComparisonRulesChecker() {
        addRule(new CanCompareDates());
        addRule(new CompareFlipLhs());
        addRule(new CompareFlipRhs());
    }
    
}
