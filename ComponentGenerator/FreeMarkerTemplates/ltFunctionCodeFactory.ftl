<#ftl strip_whitespace="true">
<#assign importList = [] />
<#assign javaList = false />
package org.genevaers.genevaio.ltfactory;
/*
** This file was automatically generated. 
**
** Do not edit.
*/

import org.genevaers.genevaio.ltfile.LTFileObject;
import org.genevaers.genevaio.ltfile.LogicTableArg;
import org.genevaers.repository.components.LRField;
import org.genevaers.repository.components.LookupPathKey;
import org.genevaers.repository.components.ViewColumn;

/**
 * Factory functions to get the left and right hand side Arithmetic Emitters
 * for an arithmetic expression.
 */
public interface LtFunctionCodeFactory {

    <#list funcCodeFunctions as fc>
    public LTFileObject ${fc};
    </#list>

}
